package com.sxtanna.mc.chat.core.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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


	@NotNull
	@Contract(pure = true)
	public String getName()
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


	@NotNull
	@Contract(pure = true)
	public String getFormatText()
	{
		return formatText;
	}

}
