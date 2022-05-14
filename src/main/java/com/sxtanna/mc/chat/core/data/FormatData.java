package com.sxtanna.mc.chat.core.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class FormatData
{

    @NotNull
    private final String name;

    private final boolean allowColors;
    private final boolean allowFormat;

    @NotNull
    private final String formatText;


    public FormatData(@NotNull final String name, final boolean allowColors, final boolean allowFormat, @NotNull final String formatText)
    {
        this.name = name;

        this.allowColors = allowColors;
        this.allowFormat = allowFormat;

        this.formatText = formatText;
    }


    @Contract(pure = true)
    public @NotNull String getName()
    {
        return name;
    }


    @Contract(pure = true)
    public boolean allowsColors()
    {
        return allowColors;
    }

    @Contract(pure = true)
    public boolean allowsFormat()
    {
        return allowFormat;
    }


    @Contract(pure = true)
    public @NotNull String getFormatText()
    {
        return formatText;
    }


    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof FormatData))
        {
            return false;
        }

        final FormatData that = (FormatData) o;
        return allowColors == that.allowColors &&
               allowFormat == that.allowFormat &&
               getName().equals(that.getName()) &&
               getFormatText().equals(that.getFormatText());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getName(), allowColors, allowFormat, getFormatText());
    }

    @Override
    public String toString()
    {
        return String.format("FormatData[name='%s', colors=%s, format=%s, formatText='%s']", name, allowColors, allowFormat, formatText);
    }

}
