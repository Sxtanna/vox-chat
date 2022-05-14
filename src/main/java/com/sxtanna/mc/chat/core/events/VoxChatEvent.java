package com.sxtanna.mc.chat.core.events;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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


    @Contract(pure = true)
    public @NotNull Player getPlayer()
    {
        return this.player;
    }

    @Contract(pure = true)
    public @NotNull String getFormat()
    {
        return this.format;
    }

    @Contract(pure = true)
    public @NotNull String getMessage()
    {
        return this.message;
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


    @Contract(pure = true)
    @Override
    public @NotNull HandlerList getHandlers()
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
        return this.cancelled == that.cancelled &&
               this.player.equals(that.player) &&
               this.format.equals(that.format) &&
               this.message.equals(that.message);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.player, this.format, this.message, this.cancelled);
    }

    @Override
    public String toString()
    {
        return String.format("VoxChatEvent[player=%s, format='%s', message='%s', cancelled=%s, handlers=%s]", this.player, this.format, this.message, this.cancelled, getHandlers());
    }


    @Contract(pure = true)
    public static @NotNull HandlerList getHandlerList()
    {
        return HANDLERS;
    }


}
