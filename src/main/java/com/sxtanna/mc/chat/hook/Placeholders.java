package com.sxtanna.mc.chat.hook;

import com.sxtanna.mc.chat.base.State;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class Placeholders implements State
{

	@NotNull
	private final Plugin         plugin;
	@NotNull
	private final List<Replacer> hooked = new ArrayList<>();


	public Placeholders(@NotNull final Plugin plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		final List<String> added = new ArrayList<>();

		if (plugin.getConfig().getBoolean("options.extras.replacers.clip_papi") &&
			Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
		{
			try
			{
				hooked.add(me.clip.placeholderapi.PlaceholderAPI::setPlaceholders);

				added.add("PlaceholderAPI");
			}
			catch (final Throwable ex)
			{
				plugin.getLogger().log(Level.WARNING, "failed to register replacer for PlaceholderAPI", ex);
			}
		}

		if (plugin.getConfig().getBoolean("options.extras.replacers.mvdw_papi") &&
			Bukkit.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI"))
		{
			try
			{
				//noinspection NullableProblems
				hooked.add(be.maximvdw.placeholderapi.PlaceholderAPI::replacePlaceholders);

				added.add("MVdWPlaceholderAPI");
			}
			catch (final Throwable ex)
			{
				plugin.getLogger().log(Level.WARNING, "failed to register replacer for MVdWPlaceholderAPI", ex);
			}
		}

		if (added.isEmpty())
		{
			hooked.add((player, text) -> {
				final Player online = player == null ? null : player.getPlayer();
				if (online == null)
				{
					return text;
				}

				String replaced = text;

				final String name = player.getName();
				if (name != null)
				{
					replaced = replaced.replace("%player%", name);
				}

				// todo: more built in replacements?

				return replaced;
			});
		}

		if (plugin.getConfig().getBoolean("options.extras.replacers.items_adder.in_formats"))
		{
			try
			{
				final Replacer replacer = IAFontImages.get();
				if (replacer != null)
				{
					// this is proxied to avoid permission checking on the player, since this only replaces values in the format
					hooked.add(($, text) -> replacer.replace(null, text));
				}

				added.add("ItemsAdder");
			}
			catch (final Throwable ex)
			{
				plugin.getLogger().log(Level.WARNING, "failed to register replacer for ItemsAdder", ex);
			}
		}

		if (!added.isEmpty())
		{
			plugin.getLogger().info("Successfully registered replacers for " + added + ".");
		}
	}

	@Override
	public void kill()
	{
		hooked.clear();
	}


	@NotNull
	public String apply(@Nullable final OfflinePlayer player, @NotNull final String text)
	{
		if (hooked.isEmpty())
		{
			return text;
		}

		return hooked.stream()
					 .reduce(text, (next, replacer) -> replacer.replace(player, next), String::concat);
	}


	@FunctionalInterface
	public interface Replacer
	{

		@NotNull
		String replace(@Nullable final OfflinePlayer player, @NotNull final String text);

	}

}
