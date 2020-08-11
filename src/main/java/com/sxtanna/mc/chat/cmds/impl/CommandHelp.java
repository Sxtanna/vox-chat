package com.sxtanna.mc.chat.cmds.impl;

import com.sxtanna.mc.chat.cmds.VoxChatCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;

import static com.sxtanna.mc.chat.util.Comp.build;
import static com.sxtanna.mc.chat.util.Comp.ofBase;

public final class CommandHelp extends VoxChatCommand
{

	public CommandHelp()
	{
		super("help");
	}


	@Override
	protected void evaluate(@NotNull final CommandSender sender, @NotNull final String label, @NotNull @Unmodifiable final List<String> params)
	{
		final ComponentBuilder builder = prefix().append(" commands info")
												 .color(ChatColor.AQUA)
												 .append("\n\n")
												 .retain(ComponentBuilder.FormatRetention.NONE);

		if (sender.hasPermission("voxchat.command.format"))
		{
			final ComponentBuilder desc = build().append("  ")
												 .append(ofBase(build().append("/voxchat ")
																	   .color(ChatColor.GRAY)
																	   .append("format ")
																	   .color(ChatColor.GREEN)
																	   .append("[format name]")
																	   .color(ChatColor.BLUE)
																	   .create()))
												 .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/voxchat format "));

			getCommandDescription("voxchat.command.format").ifPresent(s -> desc.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(s))));

			builder.append(desc.create(), ComponentBuilder.FormatRetention.NONE)
				   .append("\n");
		}

		if (sender.hasPermission("voxchat.command.reload"))
		{
			final ComponentBuilder desc = build().append("  ")
												 .append(ofBase(build().append("/voxchat ")
																	   .color(ChatColor.GRAY)
																	   .append("reload")
																	   .color(ChatColor.GREEN)
																	   .create()))
												 .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/voxchat reload"));

			getCommandDescription("voxchat.command.reload").ifPresent(s -> desc.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(s))));


			builder.append(desc.create(), ComponentBuilder.FormatRetention.NONE)
				   .append("\n");
		}

		if (sender.hasPermission("voxchat.command.toggle"))
		{
			final ComponentBuilder desc = build().append("  ")
												 .append(ofBase(build().append("/voxchat ")
																	   .color(ChatColor.GRAY)
																	   .append("toggle")
																	   .color(ChatColor.GREEN)
																	   .create()))
												 .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/voxchat toggle"));

			getCommandDescription("voxchat.command.toggle").ifPresent(s -> desc.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(s))));


			builder.append(desc.create(), ComponentBuilder.FormatRetention.NONE)
				   .append("\n");
		}

		sender.spigot().sendMessage(builder.create());
	}


	private Optional<String> getCommandDescription(@NotNull final String permission)
	{
		final List<Permission> permissions = getPlugin().getDescription().getPermissions();

		for (final Permission perm : permissions)
		{
			if (!perm.getName().equalsIgnoreCase(permission))
			{
				continue;
			}

			return Optional.of(perm.getDescription());
		}

		return Optional.empty();
	}

}
