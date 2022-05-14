package com.sxtanna.mc.chat;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;

import com.sxtanna.mc.chat.core.ActionManager;
import com.sxtanna.mc.chat.core.FormatManager;
import com.sxtanna.mc.chat.hook.IAFontImages;
import com.sxtanna.mc.chat.hook.Placeholders;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public final class VoxChat
{

    static VoxChat INSTANCE;

    private static @NotNull VoxChat getInstance()
    {
        return Objects.requireNonNull(INSTANCE, "VoxChat has not be enabled yet!");
    }

    public static @NotNull Optional<String> find(@NotNull final Player player)
    {
        return getFormatManager().list().stream().filter(name -> player.hasPermission("voxchat.format." + name)).findFirst();
    }

    public static boolean send(@NotNull final String format, @NotNull final Player player, @NotNull final String message, @NotNull final Collection<? extends CommandSender> recipients)
    {
        String temp = message;

        if (getInstance().getPlugin().getConfig().getBoolean("options.extras.replacers.items_adder.in_message"))
        {
            final Placeholders.Replacer replacer = IAFontImages.get();
            if (replacer != null)
            {
                temp = replacer.replace(player, temp);
            }
        }


        final Optional<BaseComponent[]> components = getFormatManager().prepare(format, player, temp);
        if (!components.isPresent())
        {
            return false;
        }

        recipients.forEach(other -> other.spigot().sendMessage(player.getUniqueId(), components.get()));
        return true;
    }


    public static @NotNull ActionManager getActionManager()
    {
        return getInstance().getPlugin().getActionManager();
    }

    public static @NotNull FormatManager getFormatManager()
    {
        return getInstance().getPlugin().getFormatManager();
    }

    public static @NotNull Placeholders getReplacer()
    {
        return getInstance().getPlugin().getReplacer();
    }


    @NotNull
    private final VoxChatPlugin plugin;


    VoxChat(@NotNull final VoxChatPlugin plugin)
    {
        this.plugin = plugin;
    }


    public @NotNull VoxChatPlugin getPlugin()
    {
        return this.plugin;
    }

}
