package com.sxtanna.mc.chat.core.reader;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;

public final class VoxChatReader
{

	@NotNull
	private final Parser parser = Parser.builder()
										.enabledBlockTypes(Collections.emptySet())
										.build();


	public @NotNull Optional<Node> read(@NotNull @Language("Markdown") final String text)
	{
		try
		{
			return Optional.ofNullable(parser.parse(text));
		}
		catch (final Exception ex)
		{
			return Optional.empty();
		}
	}

}
