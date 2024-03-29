package com.sxtanna.mc.chat.core.events;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sxtanna.mc.chat.VoxChat;
import com.sxtanna.mc.chat.VoxChatPlugin;
import com.sxtanna.mc.chat.base.State;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public final class ChatListener implements State, Listener
{

    @NotNull
    private final VoxChatPlugin plugin;

    private       boolean       loaded;
    private final AtomicInteger sent = new AtomicInteger();

    public ChatListener(@NotNull final VoxChatPlugin plugin)
    {
        this.plugin = plugin;
    }


    @Override
    public void load()
    {
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.loaded = true;
    }

    @Override
    public void kill()
    {
        HandlerList.unregisterAll(this);

        this.loaded = false;
    }


    @Contract(pure = true)
    public boolean isLoaded()
    {
        return this.loaded;
    }

    public int getSentMessages()
    {
        return this.sent.get();
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(@NotNull final AsyncPlayerChatEvent event)
    {
        final Player player  = event.getPlayer();
        final String message = event.getMessage();

        final Optional<String> formatName = VoxChat.find(player);
        if (!formatName.isPresent())
        {
            return;
        }


        final VoxChatEvent voxChatEvent = new VoxChatEvent(event.isAsynchronous(), player, formatName.get(), message);
        this.plugin.getServer().getPluginManager().callEvent(voxChatEvent);

        if (voxChatEvent.isCancelled())
        {
            return;
        }

        final boolean messageWasSent = VoxChat.send(voxChatEvent.getFormat(), voxChatEvent.getPlayer(), voxChatEvent.getMessage(), Sets.union(Collections.singleton(Bukkit.getConsoleSender()), event.getRecipients()));
        if (!messageWasSent)
        {
            return;
        }

        this.sent.incrementAndGet();

        if (this.plugin.getConfig().getBoolean("options.cancel_chat_event", false))
        {
            event.setCancelled(true);
        }
        else
        {
            try
            {
                event.getRecipients().clear();
            }
            catch (final UnsupportedOperationException ignored)
            {
                // possible exception due to caller
            }
        }
    }

}
