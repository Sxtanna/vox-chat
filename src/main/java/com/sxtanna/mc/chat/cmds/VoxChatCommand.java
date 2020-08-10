package com.sxtanna.mc.chat.cmds;

import com.google.common.collect.ImmutableList;
import com.sxtanna.mc.chat.VoxChatPlugin;
import com.sxtanna.mc.chat.util.Comp;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

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


	@NotNull
	protected final ComponentBuilder prefix()
	{
		return Comp.build()
				   .append("::")
				   .color(WHITE)
				   .bold(true)

				   .append(" VoxChat ")
				   .color(DARK_GRAY)
				   .bold(true)

				   .event(new ClickEvent(ClickEvent.Action.OPEN_URL, getPlugin().getDescription().getWebsite()))
				   .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Comp.build()
																				   .append("v")
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

}
