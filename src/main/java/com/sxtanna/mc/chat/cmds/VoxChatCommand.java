package com.sxtanna.mc.chat.cmds;

import com.google.common.collect.ImmutableList;
import com.sxtanna.mc.chat.VoxChatPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public abstract class VoxChatCommand
{

	private VoxChatPlugin plugin;

	@NotNull
	@Unmodifiable
	private final List<String> labels;


	protected VoxChatCommand(@NotNull final String label, @NotNull final String... aliases)
	{
		this.labels = ImmutableList.<String>builder().add(label).add(aliases).build();
	}


	@NotNull
	public final String getLabel()
	{
		return labels.get(0);
	}


	@NotNull
	@Unmodifiable
	public final List<String> getAllLabels()
	{
		return labels;
	}

	@NotNull
	@Unmodifiable
	public final List<String> getAltLabels()
	{
		return labels.subList(1, labels.size());
	}


	protected void evaluate(@NotNull final CommandSender sender, @NotNull final String label, @NotNull @Unmodifiable final List<String> params)
	{}

	protected void complete(@NotNull final CommandSender sender, @NotNull final String label, @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions)
	{}


	@NotNull
	protected final VoxChatPlugin getPlugin()
	{
		return plugin;
	}

	final void setPlugin(@NotNull final VoxChatPlugin plugin)
	{
		this.plugin = plugin;
	}

}
