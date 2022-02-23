package com.sxtanna.mc.chat.hook;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class IAFontImages
{

    private static Placeholders.Replacer replacer;

    static
    {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("ItemsAdder"))
        {
            replacer = new IAFontImageReplacer();
        }
    }


    @Contract(pure = true)
    public static @Nullable Placeholders.Replacer get()
    {
        return replacer;
    }


    private static final class IAFontImageReplacer implements Placeholders.Replacer
    {

        @Override
        public @NotNull String replace(@Nullable final OfflinePlayer player, @NotNull final String text)
        {
            final Player online = player == null ? null : player.getPlayer();
            if (online == null)
            {
                return dev.lone.itemsadder.api.FontImages.FontImageWrapper.replaceFontImages(text);
            }
            else
            {
                return dev.lone.itemsadder.api.FontImages.FontImageWrapper.replaceFontImages(online, text);
            }
        }

    }

}
