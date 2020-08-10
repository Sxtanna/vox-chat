package com.sxtanna.mc.chat.cmds.impl;

import com.sxtanna.mc.chat.cmds.VoxChatCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import static net.md_5.bungee.api.ChatColor.DARK_GRAY;
import static net.md_5.bungee.api.ChatColor.GOLD;
import static net.md_5.bungee.api.ChatColor.GREEN;
import static net.md_5.bungee.api.ChatColor.RED;

public final class CommandReload extends VoxChatCommand
{

	public CommandReload()
	{
		super("reload");
	}


	@Override
	protected void evaluate(@NotNull final CommandSender sender, @NotNull final String label, @NotNull @Unmodifiable final List<String> params)
	{
		final Optional<Throwable> result = getPlugin().reloadPlugin();
		if (!result.isPresent())
		{

			sender.spigot().sendMessage(prefix().append(" successfully reloaded.")
												.color(GREEN)
												.create());

			return;
		}

		sender.spigot().sendMessage(prefix().append(" failed to reload")
											.color(RED)
											.append(":")
											.color(DARK_GRAY)
											.append("\n  ")
											.append(result.get().getMessage())
											.color(GOLD)
											.create());

		getPlugin().getLogger().log(Level.SEVERE, "failed to reload plugin", result.get());
	}

}
