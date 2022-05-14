package com.sxtanna.mc.chat.core.data;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

import static com.sxtanna.mc.chat.util.Comp.of;

public abstract class ActionData
{

	@NotNull
	private final String text;


	ActionData(@NotNull final String text)
	{
		this.text = text;
	}

	@Contract(pure = true)
	public final @NotNull String getText()
	{
		return text;
	}


	public @NotNull Optional<ClickEvent> getClickEvent()
	{
		return Optional.empty();
	}

	public @NotNull Optional<HoverEvent> getHoverEvent()
	{
		return Optional.empty();
	}


	@Override
	public final boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!getClass().isInstance(o))
		{
			return false;
		}

		final ActionData that = (ActionData) o;
		return getText().equals(that.getText());
	}

	@Override
	public final int hashCode()
	{
		return Objects.hash(getText());
	}

	@Override
	public final String toString()
	{
		return String.format("%s['%s']", getClass().getSimpleName(), text);
	}


	private static final class ActionDataHover extends ActionData
	{

		private ActionDataHover(@NotNull final String text)
		{
			super(text);
		}


		@Override
		public @NotNull Optional<HoverEvent> getHoverEvent()
		{
			return Optional.of(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(of(getText()))));
		}

	}

	private static final class ActionDataClickExecCmd extends ActionData
	{

		private ActionDataClickExecCmd(@NotNull final String text)
		{
			super(text);
		}


		@Override
		public @NotNull Optional<ClickEvent> getClickEvent()
		{
			return Optional.of(new ClickEvent(ClickEvent.Action.RUN_COMMAND, getText()));
		}

	}

	private static final class ActionDataClickShowCmd extends ActionData
	{

		private ActionDataClickShowCmd(@NotNull final String text)
		{
			super(text);
		}


		@Override
		public @NotNull Optional<ClickEvent> getClickEvent()
		{
			return Optional.of(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, getText()));
		}

	}

	private static final class ActionDataClickOpenUrl extends ActionData
	{

		private ActionDataClickOpenUrl(@NotNull final String text)
		{
			super(text);
		}

		@Override
		public @NotNull Optional<ClickEvent> getClickEvent()
		{
			return Optional.of(new ClickEvent(ClickEvent.Action.OPEN_URL, getText()));
		}

	}


	@Contract("_ -> new")
	public static @NotNull ActionData hover(@NotNull final String text)
	{
		return new ActionDataHover(text);
	}

	@Contract("_ -> new")
	public static @NotNull ActionData clickExecCmd(@NotNull final String text)
	{
		return new ActionDataClickExecCmd(text);
	}

	@Contract("_ -> new")
	public static @NotNull ActionData clickShowCmd(@NotNull final String text)
	{
		return new ActionDataClickShowCmd(text);
	}

	@Contract("_ -> new")
	public static @NotNull ActionData clickOpenUrl(@NotNull final String text)
	{
		return new ActionDataClickOpenUrl(text);
	}

}
