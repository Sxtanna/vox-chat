package com.sxtanna.mc.chat.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Comp
{

	@NotNull
	public static final Pattern HEX_COLOR = Pattern.compile("&#(?<hex>[0-9a-fA-F]{6})");

	private Comp()
	{}


	public static @NotNull String preprocessHexColors(@NotNull final String text)
	{
		final List<MatchResult> results = new ArrayList<>();
		final Matcher           matcher = HEX_COLOR.matcher(text);
		final StringBuilder     builder = new StringBuilder(text);

		while (matcher.find())
		{
			results.add(matcher.toMatchResult());
		}

		Collections.reverse(results);

		for (final MatchResult result : results)
		{
			builder.replace(result.start(), result.end(), transformHexColor(result.group(1)));
		}

		return builder.toString();
	}

	public static @NotNull String transformHexColor(@NotNull final String hex) {
		//noinspection SuspiciousRegexArgument
		return String.format("%sx%s", ChatColor.COLOR_CHAR, hex.replaceAll(".", String.format("%s$0", ChatColor.COLOR_CHAR)));
	}

	public static @NotNull BaseComponent[] of(@NotNull final String text)
	{
		return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', preprocessHexColors(text)));
	}

	public static @NotNull TextComponent ofText(@NotNull final String text)
	{
		return ofBase(of(text));
	}

	@NotNull
	public static TextComponent ofBase(@NotNull final BaseComponent[] components)
	{
		return new TextComponent(components);
	}


	@Contract(value = " -> new", pure = true)
	public static @NotNull ComponentBuilder build()
	{
		return new ComponentBuilder();
	}


	@Contract("_ -> param1")
	public static @NotNull ComponentBuilder clean(@NotNull final ComponentBuilder builder)
	{
		final Iterator<BaseComponent> iterator = builder.getParts().iterator();
		while (iterator.hasNext())
		{
			final BaseComponent next = iterator.next();
			if (!(next instanceof TextComponent))
			{
				continue;
			}

			final TextComponent text = (TextComponent) next;
			if ((text.getText() == null || text.getText().isEmpty()) && (text.getExtra() == null || text.getExtra().isEmpty()) && (text.getClickEvent() == null && text.getHoverEvent() == null))
			{
				iterator.remove();
			}
		}

		return builder;
	}


	public static void walk(@NotNull final BaseComponent[] components, @NotNull final ComponentVisitor visitor)
	{
		Arrays.stream(components).forEach(component -> walk(component, visitor));
	}

	private static void walk(@NotNull final BaseComponent component, @NotNull final ComponentVisitor visitor)
	{
		visitor.visit(component);

		final List<BaseComponent> extra = component.getExtra();
		if (extra == null || extra.isEmpty())
		{
			return;
		}

		extra.forEach(other -> walk(other, visitor));
	}


	public static @NotNull BaseComponent[] filter(@NotNull final BaseComponent[] components, @NotNull final Predicate<BaseComponent> predicate)
	{
		final List<BaseComponent> matches = new ArrayList<>();

		walk(components, component ->
		{
			if (predicate.test(component))
			{
				matches.add(component);
			}
		});

		return matches.toArray(new BaseComponent[0]);
	}


	@FunctionalInterface
	public interface ComponentVisitor
	{

		void visit(@NotNull final BaseComponent component);

	}

}
