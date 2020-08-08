package com.sxtanna.mc.chat;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class VoxChat
{
	static VoxChat INSTANCE;

	@NotNull
	public static VoxChat getInstance()
	{
		return Objects.requireNonNull(INSTANCE, "VoxChat has not be enabled yet!");
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
