package com.sxtanna.mc.chat.core.data;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public enum ActionType
{
	HOVER
			{
				@NotNull
				@Override
				public Optional<ActionData> read(@NotNull final ConfigurationSection section)
				{
					final String text;

					if (!section.isList("text"))
					{
						text = section.getString("text");
					}
					else
					{
						text = String.join("\n", section.getStringList("text"));
					}


					return text == null ? Optional.empty() : Optional.of(ActionData.hover(text));
				}
			},
	CLICK
			{
				@NotNull
				@Override
				public Optional<ActionData> read(@NotNull final ConfigurationSection section)
				{
					final Set<String> keys = section.getKeys(false);
					if (keys.size() != 1)
					{
						return Optional.empty();
					}

					final String k = keys.iterator().next();

					final String v = section.getString(k);
					if (v == null || v.isEmpty())
					{
						return Optional.empty();
					}

					switch (k.toLowerCase())
					{
						case "exec_cmd":
							return Optional.of(ActionData.execCmd(v));
						case "show_cmd":
							return Optional.of(ActionData.showCmd(v));
						case "open_url":
							return Optional.of(ActionData.openUrl(v));
						default:
							return Optional.empty();
					}
				}
			};


	@NotNull
	public abstract Optional<ActionData> read(@NotNull final ConfigurationSection section);


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
