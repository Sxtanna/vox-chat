package com.sxtanna.mc.chat.core.data;

import com.sxtanna.mc.chat.util.Comp;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ActionData
{

	@NotNull
	private final String text;


	ActionData(@NotNull final String text)
	{
		this.text = text;
	}

	@NotNull
	@Contract(pure = true)
	public final String getText()
	{
		return text;
	}


	@NotNull
	public Optional<ClickEvent> getClickEvent()
	{
		return Optional.empty();
	}

	@NotNull
	public Optional<HoverEvent> getHoverEvent()
	{
		return Optional.empty();
	}


	public static final class ActionDataHover extends ActionData
	{

		private ActionDataHover(@NotNull final String text)
		{
			super(text);
		}


		@NotNull
		@Override
		public Optional<HoverEvent> getHoverEvent()
		{
			return Optional.of(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Comp.of(getText()))));
		}

	}

	public static final class ChatActionClickExecCmd extends ActionData
	{

		private ChatActionClickExecCmd(@NotNull final String text)
		{
			super(text);
		}

		@Override
		public @NotNull Optional<ClickEvent> getClickEvent()
		{
			return Optional.of(new ClickEvent(ClickEvent.Action.RUN_COMMAND, getText()));
		}

	}

	public static final class ChatActionClickShowCmd extends ActionData
	{

		private ChatActionClickShowCmd(@NotNull final String text)
		{
			super(text);
		}

		@Override
		public @NotNull Optional<ClickEvent> getClickEvent()
		{
			return Optional.of(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, getText()));
		}

	}

	public static final class ChatActionClickOpenUrl extends ActionData
	{

		private ChatActionClickOpenUrl(@NotNull final String text)
		{
			super(text);
		}

		@Override
		public @NotNull Optional<ClickEvent> getClickEvent()
		{
			return Optional.of(new ClickEvent(ClickEvent.Action.OPEN_URL, getText()));
		}

	}


	@NotNull
	public static ActionData hover(@NotNull final String text)
	{
		return new ActionDataHover(text);
	}

}
