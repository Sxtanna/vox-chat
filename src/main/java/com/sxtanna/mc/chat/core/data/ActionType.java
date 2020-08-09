package com.sxtanna.mc.chat.core.data;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum ActionType
{
	HOVER,
	CLICK;

	@NotNull
	public static Optional<ActionType> find(@NotNull final String name)
	{
		switch (name.toLowerCase())
		{
			case "hover":
				return Optional.of(HOVER);
			case "click":
				return Optional.of(CLICK);
			default:
				return Optional.empty();
		}
	}
}
