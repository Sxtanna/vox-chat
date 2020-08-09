package com.sxtanna.mc.chat.core.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class VoxChatEvent extends Event implements Cancellable
{

	private static final HandlerList HANDLERS = new HandlerList();

	@NotNull
	private final Player player;
	@NotNull
	private final String format;

	@NotNull
	private String message;

	private boolean cancelled;


	public VoxChatEvent(@NotNull final Player player, @NotNull final String format, @NotNull final String message)
	{
		super(true);

		this.player  = player;
		this.format  = format;
		this.message = message;
	}


	@NotNull
	@Contract(pure = true)
	public Player getPlayer()
	{
		return player;
	}

	@NotNull
	@Contract(pure = true)
	public String getFormat()
	{
		return format;
	}

	@NotNull
	@Contract(pure = true)
	public String getMessage()
	{
		return message;
	}

	@Contract(mutates = "this")
	public void setMessage(@NotNull final String message)
	{
		this.message = message;
	}


	@Override
	@Contract(pure = true)
	public boolean isCancelled()
	{
		return this.cancelled;
	}

	@Override
	@Contract(mutates = "this")
	public void setCancelled(final boolean cancel)
	{
		this.cancelled = cancel;
	}


	@NotNull
	@Contract(pure = true)
	@Override
	public HandlerList getHandlers()
	{
		return HANDLERS;
	}


	@NotNull
	@Contract(pure = true)
	public static HandlerList getHandlerList()
	{
		return HANDLERS;
	}


}
