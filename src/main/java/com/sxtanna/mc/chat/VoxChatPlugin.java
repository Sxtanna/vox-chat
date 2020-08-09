package com.sxtanna.mc.chat;

import com.sxtanna.mc.chat.cmds.VoxChatCommandRouter;
import com.sxtanna.mc.chat.core.ActionManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class VoxChatPlugin extends JavaPlugin
{

	@NotNull
	private final VoxChat api = new VoxChat(this);

	@NotNull
	private final ActionManager actionManager = new ActionManager(this);


	@Override
	public void onLoad()
	{
		saveDefaultConfig();
	}

	@Override
	public void onEnable()
	{
		actionManager.load();

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
		actionManager.kill();

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

}
