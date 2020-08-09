package com.sxtanna.mc.chat;

import com.sxtanna.mc.chat.core.ActionManager;
import com.sxtanna.mc.chat.core.FormatManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class VoxChat
{

	static VoxChat INSTANCE;

	@NotNull
	private static VoxChat getInstance()
	{
		return Objects.requireNonNull(INSTANCE, "VoxChat has not be enabled yet!");
	}


	@NotNull
	public static ActionManager getActionManager()
	{
		return getInstance().getPlugin().getActionManager();
	}

	@NotNull
	public static FormatManager getFormatManager()
	{
		return getInstance().getPlugin().getFormatManager();
	}


	@NotNull
	private final VoxChatPlugin plugin;


	VoxChat(@NotNull final VoxChatPlugin plugin)
	{
		this.plugin = plugin;
	}


	@NotNull
	public VoxChatPlugin getPlugin()
	{
		return plugin;
	}

}
