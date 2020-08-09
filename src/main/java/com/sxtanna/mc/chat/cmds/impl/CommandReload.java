package com.sxtanna.mc.chat.cmds.impl;

import com.sxtanna.mc.chat.cmds.VoxChatCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public final class CommandReload extends VoxChatCommand
{

	public CommandReload()
	{
		super("reload");
	}


	@Override
	protected void evaluate(@NotNull final CommandSender sender, @NotNull final String label, @NotNull @Unmodifiable final List<String> params)
	{

	}

}
