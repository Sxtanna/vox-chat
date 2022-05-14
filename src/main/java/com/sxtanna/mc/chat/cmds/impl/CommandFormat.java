package com.sxtanna.mc.chat.cmds.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

import com.sxtanna.mc.chat.VoxChat;
import com.sxtanna.mc.chat.cmds.VoxChatCommand;

import java.util.Collections;
import java.util.List;

public final class CommandFormat extends VoxChatCommand
{

    public CommandFormat()
    {
        super("format");
    }


    @Override
    protected void evaluate(@NotNull final CommandSender sender, @NotNull final String label, @NotNull @Unmodifiable final List<String> params)
    {
        if (!(sender instanceof Player))
        {
            sender.spigot().sendMessage(prefix().append(" you must be a player to do this.")
                                                .color(ChatColor.RED)
                                                .create());
            return;
        }

        if (params.size() < 1)
        {
            sender.spigot().sendMessage(prefix().append(" not enough arguments.")
                                                .color(ChatColor.RED)
                                                .create());
            return;
        }
        if (params.size() > 1)
        {
            sender.spigot().sendMessage(prefix().append(" too many arguments.")
                                                .color(ChatColor.RED)
                                                .create());
            return;
        }

        final Player player = (Player) sender;
        final String format = params.get(0);

        if (!VoxChat.getFormatManager().find(format).isPresent())
        {
            sender.spigot().sendMessage(prefix().append(" there is no format by that name.")
                                                .color(ChatColor.RED)
                                                .create());
            return;
        }

        final boolean messageWasSent = VoxChat.send(format,
                                                    player,

                                                    "Hi, this is&l a&a test&f message! :D",

                                                    Collections.singleton(sender));
        if (!messageWasSent)
        {
            sender.spigot().sendMessage(prefix().append(" failed to send message.")
                                                .color(ChatColor.RED)
                                                .create());
        }
    }

    @Override
    protected void complete(@NotNull final CommandSender sender, @NotNull final String label, @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions)
    {
        if (params.size() > 1)
        {
            return;
        }

        getPlugin().getFormatManager()
                   .list()
                   .stream()
                   .filter(possible -> params.isEmpty() || possible.toLowerCase().startsWith(params.get(0).toLowerCase()))
                   .forEach(suggestions::add);
    }

}
