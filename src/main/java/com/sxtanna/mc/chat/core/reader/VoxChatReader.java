package com.sxtanna.mc.chat.core.reader;

import org.jetbrains.annotations.NotNull;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.intellij.lang.annotations.Language;

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
            return Optional.ofNullable(this.parser.parse(text));
        }
        catch (final Exception ex)
        {
            return Optional.empty();
        }
    }

}
