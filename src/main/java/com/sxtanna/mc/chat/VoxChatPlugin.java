package com.sxtanna.mc.chat;

import com.google.common.io.CharStreams;
import com.sxtanna.mc.chat.cmds.VoxChatCommandRouter;
import com.sxtanna.mc.chat.core.ActionManager;
import com.sxtanna.mc.chat.core.FormatManager;
import com.sxtanna.mc.chat.core.events.ChatListener;
import com.sxtanna.mc.chat.hook.Placeholders;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;

public final class VoxChatPlugin extends JavaPlugin
{

	@NotNull
	private final VoxChat api = new VoxChat(this);

	@NotNull
	private final ActionManager actionManager = new ActionManager(this);
	@NotNull
	private final FormatManager formatManager = new FormatManager(this);

	@NotNull
	private final ChatListener listener = new ChatListener(this);
	@NotNull
	private final Placeholders replacer = new Placeholders(this);


	@Override
	public void onLoad()
	{
		saveDefaultConfig();
	}

	@Override
	public void onEnable()
	{
		if (getConfig().getBoolean("options.show_banner_on_start", true))
		{
			try
			{
				final Reader banner = getTextResource("banner");
				if (banner != null)
				{
					//noinspection UnstableApiUsage
					getLogger().info("\n" + CharStreams.toString(banner));
				}
			}
			catch (final IOException ignored)
			{ }
		}


		actionManager.load();
		formatManager.load();

		replacer.load();
		listener.load();

		getServer().getServicesManager().register(VoxChat.class, api, this, ServicePriority.Normal);
		VoxChat.INSTANCE = api;

		final PluginCommand command = getCommand("voxchat");
		if (command != null)
		{
			final VoxChatCommandRouter router = new VoxChatCommandRouter(this);

			command.setExecutor(router);
			command.setTabCompleter(router);
		}
	}

	@Override
	public void onDisable()
	{
		listener.kill();
		replacer.kill();

		actionManager.kill();
		formatManager.kill();

		final PluginCommand command = getCommand("voxchat");
		if (command != null)
		{
			command.setExecutor(null);
			command.setTabCompleter(null);
		}

		getServer().getServicesManager().unregister(VoxChat.class, api);
		VoxChat.INSTANCE = null;
	}


	@NotNull
	@Contract(pure = true)
	public ActionManager getActionManager()
	{
		return actionManager;
	}

	@NotNull
	@Contract(pure = true)
	public FormatManager getFormatManager()
	{
		return formatManager;
	}


	@NotNull
	@Contract(pure = true)
	public Placeholders getReplacer()
	{
		return replacer;
	}

	@NotNull
	@Contract(pure = true)
	public ChatListener getListener()
	{
		return listener;
	}

	@NotNull
	public Optional<Throwable> reloadPlugin()
	{
		try
		{
			replacer.kill();

			actionManager.kill();
			formatManager.kill();

			reloadConfig();

			actionManager.load();
			formatManager.load();

			replacer.load();
		}
		catch (final Throwable ex)
		{
			return Optional.of(ex);
		}

		return Optional.empty();
	}

}
