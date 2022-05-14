package com.sxtanna.mc.chat.core.reader;

import org.jetbrains.annotations.NotNull;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import com.sxtanna.mc.chat.VoxChat;
import com.sxtanna.mc.chat.core.data.ActionType;

import com.google.common.collect.ImmutableSet;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Document;
import org.commonmark.node.Emphasis;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.node.Paragraph;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.commonmark.renderer.NodeRenderer;

import java.util.Set;

import static com.sxtanna.mc.chat.util.Comp.build;
import static com.sxtanna.mc.chat.util.Comp.clean;
import static com.sxtanna.mc.chat.util.Comp.ofText;

public final class VoxChatRender extends AbstractVisitor implements NodeRenderer
{

    @NotNull
    private final Player           player;
    @NotNull
    private final ComponentBuilder builder = build();

    private boolean ital;
    private boolean bold;


    public VoxChatRender(@NotNull final Player player)
    {
        this.player = player;
    }


    public @NotNull ComponentBuilder getBuilder()
    {
        return clean(this.builder);
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes()
    {
        return ImmutableSet.of(Document.class, Paragraph.class);
    }

    @Override
    public void render(final Node node)
    {
        node.accept(this);
    }


    @Override
    public void visit(final Emphasis emphasis)
    {
        this.ital = true;
        visitChildren(emphasis);
        this.ital = false;
    }

    @Override
    public void visit(final StrongEmphasis strongEmphasis)
    {
        this.bold = true;
        visitChildren(strongEmphasis);
        this.bold = false;
    }

    @Override
    public void visit(final Text text)
    {
        this.builder.append(ofText(VoxChat.getReplacer().apply(this.player, text.getLiteral())),
                            currentHasText() ?
                            ComponentBuilder.FormatRetention.NONE :
                            ComponentBuilder.FormatRetention.FORMATTING);

        if (this.ital)
        {
            this.builder.italic(true);
        }
        if (this.bold)
        {
            this.builder.bold(true);
        }
    }

    @Override
    public void visit(final Link link)
    {
        visitChildren(link);

        cleanseEmpty();

        // registered actions
        for (final String dest : link.getDestination().replace(" ", "").split("[,|:/]"))
        {
            // [action_type.action_name]
            final String[] path = dest.split("\\.");
            if (path.length != 2)
            {
                continue; // invalid path, skip
            }

            ActionType.find(path[0])
                      .flatMap(type -> VoxChat.getActionManager().find(type, path[1]))
                      .ifPresent(data -> {
                          data.getClickEvent()
                              .ifPresent(this.builder::event);
                          data.getHoverEvent()
                              .ifPresent(this.builder::event);
                      });
        }
    }


    private boolean currentHasText()
    {
        final BaseComponent current = this.builder.getCurrentComponent();
        return current instanceof TextComponent && !((TextComponent) current).getText().isEmpty();
    }

    private void cleanseEmpty()
    {
        while (!this.builder.getParts().isEmpty() && !currentHasText())
        {
            if (this.builder.getCurrentComponent().getExtra() != null && !this.builder.getCurrentComponent().getExtra().isEmpty())
            {
                break;
            }

            this.builder.resetCursor().removeComponent(this.builder.getCursor());
        }
    }

}
