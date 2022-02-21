package com.sxtanna.mc.chat.core.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class VoxChatEvent extends Event implements Cancellable
{

	@NotNull
	private static final HandlerList HANDLERS = new HandlerList();


	@NotNull
	private final Player player;
	@NotNull
	private final String format;

	@NotNull
	private String message;

	private boolean cancelled;


	public VoxChatEvent(final boolean async, @NotNull final Player player, @NotNull final String format, @NotNull final String message)
	{
		super(async);

		this.player  = player;
		this.format  = format;
		this.message = message;
	}

	public VoxChatEvent(@NotNull final Player player, @NotNull final String format, @NotNull final String message)
	{
		this(true, player, format, message);
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


	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof VoxChatEvent))
		{
			return false;
		}

		final VoxChatEvent that = (VoxChatEvent) o;
		return isCancelled() == that.isCancelled() &&
			   getPlayer().equals(that.getPlayer()) &&
			   getFormat().equals(that.getFormat()) &&
			   getMessage().equals(that.getMessage());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getPlayer(), getFormat(), getMessage(), isCancelled());
	}

	@Override
	public String toString()
	{
		return String.format("VoxChatEvent[player=%s, format='%s', message='%s', cancelled=%s, handlers=%s]", player, format, message, cancelled, getHandlers());
	}


	@NotNull
	@Contract(pure = true)
	public static HandlerList getHandlerList()
	{
		return HANDLERS;
	}


}
