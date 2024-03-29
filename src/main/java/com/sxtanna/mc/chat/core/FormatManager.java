package com.sxtanna.mc.chat.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import com.sxtanna.mc.chat.VoxChatPlugin;
import com.sxtanna.mc.chat.base.State;
import com.sxtanna.mc.chat.core.data.FormatData;
import com.sxtanna.mc.chat.core.reader.VoxChatReader;
import com.sxtanna.mc.chat.core.reader.VoxChatRender;

import com.google.common.collect.ImmutableSet;
import org.commonmark.node.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.sxtanna.mc.chat.util.Comp.filter;
import static com.sxtanna.mc.chat.util.Comp.of;
import static com.sxtanna.mc.chat.util.Comp.walk;

public final class FormatManager implements State
{

    @NotNull
    private final VoxChatReader reader = new VoxChatReader();

    @NotNull
    private final VoxChatPlugin           plugin;
    @NotNull
    private final Map<String, FormatData> cached = new HashMap<>();


    public FormatManager(@NotNull final VoxChatPlugin plugin)
    {
        this.plugin = plugin;
    }


    @Override
    public void load()
    {
        loadFormats();
    }

    @Override
    public void kill()
    {
        this.cached.clear();
    }


    public int size()
    {
        return this.cached.size();
    }

    public @NotNull @Unmodifiable Set<String> list()
    {
        return ImmutableSet.copyOf(this.cached.keySet());
    }

    public @NotNull Optional<FormatData> find(@NotNull final String name)
    {
        return Optional.ofNullable(this.cached.get(name.toLowerCase()));
    }

    public void save(@NotNull final FormatData data)
    {
        this.cached.put(data.getName().toLowerCase(), data);
    }


    public @NotNull Optional<BaseComponent[]> prepare(@NotNull final String name, @NotNull final Player player, @NotNull final String message)
    {
        final Optional<FormatData> data = find(name);
        if (!data.isPresent())
        {
            return Optional.empty();
        }

        final Optional<Node> node = this.reader.read(data.get().getFormatText());
        if (!node.isPresent())
        {
            return Optional.empty();
        }

        final VoxChatRender render = new VoxChatRender(player);
        render.render(node.get());


        final BaseComponent[] renderComponents = render.getBuilder().create();
        walkReplacing(player, renderComponents);


        final BaseComponent[] searchComponents = filter(renderComponents, component -> component instanceof TextComponent && ((TextComponent) component).getText().contains("%message%"));
        if (searchComponents.length == 1)
        {
            final TextComponent text = (TextComponent) searchComponents[0];
            text.setText(text.getText().replace("%message%", ""));

            final BaseComponent[] messageComponent = of(message);
            if (!data.get().allowsColors())
            {
                final String bypassPermission = this.plugin.getConfig().getString("options.bypass.colors_permission");
                if (bypassPermission == null || !player.hasPermission(bypassPermission))
                {
                    walk(messageComponent, component -> component.setColor(null));
                }
            }

            if (!data.get().allowsFormat() && !player.hasPermission("voxchat.bypass.format"))
            {
                final String bypassPermission = this.plugin.getConfig().getString("options.bypass.format_permission");
                if (bypassPermission == null || !player.hasPermission(bypassPermission))
                {
                    walk(messageComponent, component ->
                    {
                        component.setBold(null);
                        component.setItalic(null);
                        component.setUnderlined(null);
                        component.setObfuscated(null);
                        component.setStrikethrough(null);
                    });
                }
            }

            for (final BaseComponent component : messageComponent)
            {
                text.addExtra(component);
            }
        }

        return Optional.of(renderComponents);
    }


    private void loadFormats()
    {
        final ConfigurationSection section = this.plugin.getConfig().getConfigurationSection("formats");
        if (section == null)
        {
            return;
        }

        for (final String name : section.getKeys(false))
        {
            final ConfigurationSection formatSection = section.getConfigurationSection(name);
            if (formatSection == null)
            {
                continue;
            }

            final String formatText = formatSection.getString("format");
            if (formatText == null)
            {
                continue;
            }

            final boolean allowColors = formatSection.getBoolean("allows.colors", false);
            final boolean allowFormat = formatSection.getBoolean("allows.format", false);

            save(new FormatData(name, allowColors, allowFormat, formatText));
        }
    }


    private void walkReplacing(@Nullable final OfflinePlayer player, @NotNull final BaseComponent[] components)
    {
        walk(components, component -> {
            final HoverEvent hoverEvent = component.getHoverEvent();
            if (hoverEvent != null)
            {
                walkReplacingHoverEvent(player, hoverEvent);
            }

            final ClickEvent clickEvent = component.getClickEvent();
            if (clickEvent != null)
            {
                component.setClickEvent(new ClickEvent(clickEvent.getAction(), this.plugin.getReplacer().apply(player, clickEvent.getValue())));
            }

            if (!(component instanceof TextComponent))
            {
                return;
            }

            final TextComponent text = (TextComponent) component;
            text.setText(this.plugin.getReplacer().apply(player, text.getText()));
        });
    }

    private void walkReplacingHoverEvent(@Nullable final OfflinePlayer player, @NotNull final HoverEvent event)
    {
        event.getContents().replaceAll(content -> {
            if (!(content instanceof Text))
            {
                return content;
            }

            Object value = ((Text) content).getValue();

            if (value instanceof String)
            {
                final String textValue = (String) value;

                return new Text(this.plugin.getReplacer().apply(player, textValue));
            }
            else if (value instanceof BaseComponent[])
            {
                final BaseComponent[] compValue = (BaseComponent[]) value;
                walkReplacing(player, compValue);

                return new Text(compValue);
            }

            return content;
        });
    }

}
