package com.sxtanna.mc.chat.cmds;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sxtanna.mc.chat.cmds.impl.CommandReload;
import com.sxtanna.mc.chat.cmds.impl.CommandToggle;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public final class VoxChatCommandRouter implements CommandExecutor, TabCompleter
{

	@NotNull
	@Unmodifiable
	private static final List<VoxChatCommand> COMMANDS = ImmutableList.of(new CommandReload(),
																		  new CommandToggle());

	@NotNull
	@Unmodifiable
	private static Map<String, VoxChatCommand> generateLookupMap()
	{
		final ImmutableMap.Builder<String, VoxChatCommand> builder = ImmutableMap.builder();

		COMMANDS.forEach(command -> command.getAllLabels().forEach(label -> builder.put(label.toLowerCase(), command)));

		return builder.build();
	}


	@NotNull
	private final Plugin                      plugin;
	@NotNull
	@Unmodifiable
	private final Map<String, VoxChatCommand> lookup;


	public VoxChatCommandRouter(@NotNull final Plugin plugin)
	{
		this.plugin = plugin;
		this.lookup = generateLookupMap();
	}


	@Override
	public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args)
	{
		if (args.length == 0)
		{
			final VoxChatCommand help = lookup.get("help");
			if (help != null)
			{
				help.evaluate(sender, "", Collections.emptyList());
			}

			return true;
		}


		final String         search = args[0].toLowerCase();
		final VoxChatCommand target = lookup.get(search);

		if (target == null)
		{

			sender.sendMessage(String.format("%sUnknown command %s'%s%s%s'%s",
											 ChatColor.RED, ChatColor.DARK_GRAY, ChatColor.WHITE, search, ChatColor.DARK_GRAY, ChatColor.RESET));

			return true;
		}

		if (!sender.hasPermission("voxchat.command." + target.getLabel().toLowerCase()))
		{

			sender.sendMessage(String.format("%sYou do not have permission to do this!%s",
											 ChatColor.RED, ChatColor.RESET));

			return true;
		}

		try
		{
			target.evaluate(sender, search, ImmutableList.copyOf(Arrays.copyOfRange(args, 1, args.length)));
		}
		catch (final Throwable ex)
		{
			plugin.getLogger().log(Level.SEVERE, "failed to evaluate command:" + target.getLabel(), ex);
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args)
	{
		final List<String> suggestions = new ArrayList<>();

		if (args.length > 1)
		{
			final VoxChatCommand target = lookup.get(args[0].toLowerCase());
			if (target != null)
			{
				target.complete(sender, alias, ImmutableList.copyOf(Arrays.copyOfRange(args, 1, args.length)), suggestions);
			}

			return suggestions;
		}

		COMMANDS.stream()
				// ensure sender has permission for this command
				.filter(target -> sender.hasPermission("voxchat.command." + target.getLabel().toLowerCase()))
				// get all labels of this command
				.map(VoxChatCommand::getAllLabels)
				// flatten, Stream<List<String>> -> Stream<String>
				.flatMap(Collection::stream)
				// ensure this suggestion starts with what they have typed
				.filter(target -> args.length == 0 || target.toLowerCase().startsWith(args[0].toLowerCase()))
				// add them to the suggestions list
				.forEach(suggestions::add);


		return suggestions;
	}

}
