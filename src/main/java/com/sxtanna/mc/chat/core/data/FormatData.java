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
        return this.name;
    }


    @Contract(pure = true)
    public boolean allowsColors()
    {
        return this.allowColors;
    }

    @Contract(pure = true)
    public boolean allowsFormat()
    {
        return this.allowFormat;
    }


    @Contract(pure = true)
    public @NotNull String getFormatText()
    {
        return this.formatText;
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
        return this.allowColors == that.allowColors &&
               this.allowFormat == that.allowFormat &&
               this.name.equals(that.name) &&
               this.formatText.equals(that.formatText);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.name, this.allowColors, this.allowFormat, this.formatText);
    }

    @Override
    public String toString()
    {
        return String.format("FormatData[name='%s', colors=%s, format=%s, formatText='%s']", this.name, this.allowColors, this.allowFormat, this.formatText);
    }

}
