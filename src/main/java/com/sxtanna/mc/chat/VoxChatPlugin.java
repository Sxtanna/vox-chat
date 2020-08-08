package com.sxtanna.mc.chat;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class VoxChatPlugin extends JavaPlugin
{

	@NotNull
	private final VoxChat api = new VoxChat(this);


	@Override
	public void onLoad()
	{
		saveDefaultConfig();
	}

	@Override
	public void onEnable()
	{
		getServer().getServicesManager().register(VoxChat.class, api, this, ServicePriority.Normal);
		VoxChat.INSTANCE = api;
	}

	@Override
	public void onDisable()
	{
		getServer().getServicesManager().unregister(VoxChat.class, api);
		VoxChat.INSTANCE = null;
	}

}
