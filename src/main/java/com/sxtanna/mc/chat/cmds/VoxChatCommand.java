package com.sxtanna.mc.chat.cmds;

import com.google.common.collect.ImmutableList;
import com.sxtanna.mc.chat.VoxChatPlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Objects;

import static com.sxtanna.mc.chat.util.Comp.build;
import static net.md_5.bungee.api.ChatColor.DARK_GRAY;
import static net.md_5.bungee.api.ChatColor.WHITE;
import static net.md_5.bungee.api.ChatColor.YELLOW;

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


	public final @NotNull String getLabel()
	{
		return labels.get(0);
	}


	public final @NotNull @Unmodifiable List<String> getAllLabels()
	{
		return labels;
	}

	public final @NotNull @Unmodifiable List<String> getAltLabels()
	{
		return labels.subList(1, labels.size());
	}


	protected void evaluate(@NotNull final CommandSender sender, @NotNull final String label, @NotNull @Unmodifiable final List<String> params)
	{}

	protected void complete(@NotNull final CommandSender sender, @NotNull final String label, @NotNull @Unmodifiable final List<String> params, @NotNull final List<String> suggestions)
	{}


	protected final @NotNull VoxChatPlugin getPlugin()
	{
		return plugin;
	}

	final void setPlugin(@NotNull final VoxChatPlugin plugin)
	{
		this.plugin = plugin;
	}


	protected final @NotNull ComponentBuilder prefix()
	{
		return build().append("::")
					  .color(WHITE)
					  .bold(true)

					  .append(" VoxChat ")
					  .color(DARK_GRAY)
					  .bold(true)

					  .event(new ClickEvent(ClickEvent.Action.OPEN_URL, getPlugin().getDescription().getWebsite()))
					  .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(build().append("v")
																						 .color(DARK_GRAY)
																						 .bold(false)
																						 .append(getPlugin().getDescription().getVersion())
																						 .color(WHITE)
																						 .append("\n")
																						 .append("  - Sxtanna")
																						 .color(YELLOW)
																						 .create())))

					  .append("::")
					  .color(WHITE)
					  .bold(true)

					  .append("")
					  .retain(ComponentBuilder.FormatRetention.NONE);
	}


	@Override
	public final boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof VoxChatCommand))
		{
			return false;
		}
		final VoxChatCommand that = (VoxChatCommand) o;
		return labels.equals(that.labels);
	}

	@Override
	public final int hashCode()
	{
		return Objects.hash(labels);
	}

	@Override
	public final String toString()
	{
		return String.format("VoxChatCommand[labels=%s]", labels);
	}

}
