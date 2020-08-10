package com.sxtanna.mc.chat.core.events;

import com.sxtanna.mc.chat.VoxChatPlugin;
import com.sxtanna.mc.chat.base.State;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class ChatListener implements State, Listener
{

	@NotNull
	private final VoxChatPlugin plugin;

	private boolean loaded;

	public ChatListener(@NotNull final VoxChatPlugin plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		loaded = true;
	}

	@Override
	public void kill()
	{
		HandlerList.unregisterAll(this);

		loaded = false;
	}


	@Contract(pure = true)
	public boolean isLoaded()
	{
		return loaded;
	}


	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onChat(@NotNull final AsyncPlayerChatEvent event)
	{
		final Player player  = event.getPlayer();
		final String message = event.getMessage();

		final Optional<String> formatName = plugin.getFormatManager().list().stream().filter(name -> player.hasPermission("voxchat.format." + name)).findFirst();
		if (!formatName.isPresent())
		{
			return;
		}


		final VoxChatEvent voxChatEvent = new VoxChatEvent(player, formatName.get(), message);
		plugin.getServer().getPluginManager().callEvent(voxChatEvent);

		if (voxChatEvent.isCancelled())
		{
			return;
		}

		final Optional<BaseComponent[]> components = plugin.getFormatManager().prepare(voxChatEvent.getFormat(), voxChatEvent.getPlayer(), voxChatEvent.getMessage());
		if (!components.isPresent())
		{
			return;
		}

		event.setCancelled(true);

		event.getRecipients().forEach(other -> other.spigot().sendMessage(components.get()));
	}

}
