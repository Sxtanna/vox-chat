package com.sxtanna.mc.chat.core;

import org.jetbrains.annotations.NotNull;

import org.bukkit.configuration.ConfigurationSection;

import com.sxtanna.mc.chat.VoxChatPlugin;
import com.sxtanna.mc.chat.base.State;
import com.sxtanna.mc.chat.core.data.ActionData;
import com.sxtanna.mc.chat.core.data.ActionType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ActionManager implements State
{

    @NotNull
    private final VoxChatPlugin                            plugin;
    @NotNull
    private final Map<ActionType, Map<String, ActionData>> cached = new HashMap<>();


    public ActionManager(@NotNull final VoxChatPlugin plugin)
    {
        this.plugin = plugin;
    }


    @Override
    public void load()
    {
        loadActions();
    }

    @Override
    public void kill()
    {
        cached.clear();
    }


    public @NotNull Optional<ActionData> find(@NotNull final ActionType type, @NotNull final String name)
    {
        return Optional.ofNullable(cached.get(type)).map(map -> map.get(name.toLowerCase()));
    }


    public void save(@NotNull final ActionType type, @NotNull final String name, @NotNull final ActionData action)
    {
        cached.computeIfAbsent(type, ($) -> new HashMap<>()).put(name.toLowerCase(), action);
    }


    private void loadActions()
    {
        final ConfigurationSection section = plugin.getConfig().getConfigurationSection("actions");
        if (section == null)
        {
            return;
        }

        for (final ActionType type : ActionType.values())
        {
            final ConfigurationSection typeSection = section.getConfigurationSection(type.name().toLowerCase());
            if (typeSection == null)
            {
                continue;
            }

            for (final String key : typeSection.getKeys(false))
            {
                final ConfigurationSection dataSection = typeSection.getConfigurationSection(key);
                if (dataSection == null)
                {
                    continue;
                }

                final Optional<ActionData> data = type.read(dataSection);
                if (!data.isPresent())
                {
                    continue;
                }

                save(type, key, data.get());
            }
        }
    }

}
