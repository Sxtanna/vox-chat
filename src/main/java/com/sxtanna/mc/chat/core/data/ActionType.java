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

					final ActionData data;
					switch (k.toLowerCase())
					{
						case "exec_cmd":
							data = ActionData.clickExecCmd(v);
							break;
						case "show_cmd":
							data = ActionData.clickShowCmd(v);
							break;
						case "open_url":
							data = ActionData.clickOpenUrl(v);
							break;
						default:
							return Optional.empty();
					}

					return Optional.of(data);
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
