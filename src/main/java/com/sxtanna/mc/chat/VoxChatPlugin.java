package com.sxtanna.mc.chat;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.sxtanna.mc.chat.cmds.VoxChatCommandRouter;
import com.sxtanna.mc.chat.core.ActionManager;
import com.sxtanna.mc.chat.core.FormatManager;
import com.sxtanna.mc.chat.core.events.ChatListener;
import com.sxtanna.mc.chat.hook.Placeholders;

import com.google.common.io.CharStreams;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import java.util.logging.Level;

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
            {
            }
        }


        this.actionManager.load();
        this.formatManager.load();

        this.replacer.load();
        this.listener.load();

        getServer().getServicesManager().register(VoxChat.class, this.api, this, ServicePriority.Normal);
        VoxChat.INSTANCE = this.api;

        final PluginCommand command = getCommand("voxchat");
        if (command != null)
        {
            final VoxChatCommandRouter router = new VoxChatCommandRouter(this);

            command.setExecutor(router);
            command.setTabCompleter(router);
        }

        if (getConfig().getBoolean("options.use_metrics", true))
        {
            try
            {
                initializeMetrics();
            }
            catch (final Throwable ex)
            {
                getLogger().log(Level.WARNING, "failed to initialize metrics", ex);
            }
        }

    }

    @Override
    public void onDisable()
    {
        this.listener.kill();
        this.replacer.kill();

        this.actionManager.kill();
        this.formatManager.kill();

        final PluginCommand command = getCommand("voxchat");
        if (command != null)
        {
            command.setExecutor(null);
            command.setTabCompleter(null);
        }

        getServer().getServicesManager().unregister(VoxChat.class, this.api);
        VoxChat.INSTANCE = null;
    }


    @Contract(pure = true)
    public @NotNull ActionManager getActionManager()
    {
        return this.actionManager;
    }

    @Contract(pure = true)
    public @NotNull FormatManager getFormatManager()
    {
        return this.formatManager;
    }


    @Contract(pure = true)
    public @NotNull Placeholders getReplacer()
    {
        return this.replacer;
    }

    @Contract(pure = true)
    public @NotNull ChatListener getListener()
    {
        return this.listener;
    }

    public @NotNull Optional<Throwable> reloadPlugin()
    {
        try
        {
            this.replacer.kill();

            this.actionManager.kill();
            this.formatManager.kill();

            reloadConfig();

            this.actionManager.load();
            this.formatManager.load();

            this.replacer.load();
        }
        catch (final Throwable ex)
        {
            return Optional.of(ex);
        }

        return Optional.empty();
    }


    private void initializeMetrics()
    {
        final Metrics metrics = new Metrics(this, 15190);

        metrics.addCustomChart(new SingleLineChart("sent_messages", () -> getListener().getSentMessages()));
        metrics.addCustomChart(new SimplePie("format_count", () -> String.valueOf(getFormatManager().size())));
    }

}
