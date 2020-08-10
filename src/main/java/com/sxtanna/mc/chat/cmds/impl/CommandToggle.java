package com.sxtanna.mc.chat.cmds.impl;

import com.sxtanna.mc.chat.cmds.VoxChatCommand;
import com.sxtanna.mc.chat.core.events.ChatListener;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

import static net.md_5.bungee.api.ChatColor.GRAY;
import static net.md_5.bungee.api.ChatColor.GREEN;
import static net.md_5.bungee.api.ChatColor.RED;

public final class CommandToggle extends VoxChatCommand
{

	public CommandToggle()
	{
		super("toggle", "enable", "disable");
	}


	@Override
	protected void evaluate(@NotNull final CommandSender sender, @NotNull final String label, @NotNull @Unmodifiable final List<String> params)
	{
		final ChatListener listener = getPlugin().getListener();

		final boolean desiredState;
		final boolean currentState = listener.isLoaded();

		switch (label.toLowerCase())
		{
			case "enable":
				desiredState = true;
				break;
			case "disable":
				desiredState = false;
				break;
			default:
				desiredState = !currentState;
				break;
		}

		if (currentState == desiredState)
		{

			sender.spigot().sendMessage(prefix().append(" is already ")
												.color(GRAY)
												.append(currentState ? "enabled" : "disabled")
												.color(currentState ? GREEN : RED)
												.create());

			return;
		}

		if (desiredState)
		{
			listener.load();
		}
		else
		{
			listener.kill();
		}

		sender.spigot().sendMessage(prefix().append(" is now ")
											.color(GRAY)
											.append(desiredState ? "enabled" : "disabled")
											.color(desiredState ? GREEN : RED)
											.create());
	}

}
