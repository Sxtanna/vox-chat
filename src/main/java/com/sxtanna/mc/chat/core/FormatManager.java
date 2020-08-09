package com.sxtanna.mc.chat.core;

import com.sxtanna.mc.chat.VoxChatPlugin;
import com.sxtanna.mc.chat.base.State;
import com.sxtanna.mc.chat.core.data.FormatData;
import com.sxtanna.mc.chat.core.reader.VoxChatReader;
import com.sxtanna.mc.chat.core.reader.VoxChatRender;
import com.sxtanna.mc.chat.util.Comp;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.commonmark.node.Node;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
		cached.clear();
	}


	@NotNull
	public Optional<FormatData> find(@NotNull final String name)
	{
		return Optional.ofNullable(cached.get(name.toLowerCase()));
	}

	public void save(@NotNull final FormatData data)
	{
		cached.put(data.getName().toLowerCase(), data);
	}


	@NotNull
	public Optional<BaseComponent[]> prepare(@NotNull final String name, @NotNull final Player player, @NotNull final String message)
	{
		final Optional<FormatData> data = find(name);
		if (!data.isPresent())
		{
			return Optional.empty();
		}

		// apply placeholders to this
		@Language("Markdown")
		final String text = data.get().getFormatText().replace("%message%", message);

		final Optional<Node> node = reader.read(text);
		if (!node.isPresent())
		{
			return Optional.empty();
		}

		final VoxChatRender render = new VoxChatRender();
		render.render(node.get());


		final BaseComponent[] components = render.getBuilder().create();

		if (!data.get().allowsColors())
		{
			Comp.walk(components, component -> component.setColor(null));
		}

		if (!data.get().allowsFormat())
		{
			Comp.walk(components, component -> {
				component.setBold(null);
				component.setItalic(null);
				component.setUnderlined(null);
				component.setObfuscated(null);
				component.setStrikethrough(null);
			});
		}

		return Optional.of(components);
	}


	private void loadFormats()
	{
		final ConfigurationSection section = plugin.getConfig().getConfigurationSection("formats");
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

}
