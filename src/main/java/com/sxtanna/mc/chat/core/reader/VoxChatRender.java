package com.sxtanna.mc.chat.core.reader;

import com.google.common.collect.ImmutableSet;
import com.sxtanna.mc.chat.VoxChat;
import com.sxtanna.mc.chat.core.data.ActionType;
import com.sxtanna.mc.chat.util.Comp;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Document;
import org.commonmark.node.Emphasis;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.node.Paragraph;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.commonmark.renderer.NodeRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.sxtanna.mc.chat.util.Comp.build;
import static com.sxtanna.mc.chat.util.Comp.clean;
import static com.sxtanna.mc.chat.util.Comp.ofText;

public final class VoxChatRender extends AbstractVisitor implements NodeRenderer
{

	@NotNull
	private final ComponentBuilder builder = build();

	private boolean ital;
	private boolean bold;

	@NotNull
	public ComponentBuilder getBuilder()
	{
		return clean(builder);
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
		ital = true;
		visitChildren(emphasis);
		ital = false;
	}

	@Override
	public void visit(final StrongEmphasis strongEmphasis)
	{
		bold = true;
		visitChildren(strongEmphasis);
		bold = false;
	}

	@Override
	public void visit(final Text text)
	{
		builder.append(ofText(text.getLiteral()), currentHasText() ?
												  ComponentBuilder.FormatRetention.NONE :
												  ComponentBuilder.FormatRetention.FORMATTING);

		if (ital)
		{
			builder.italic(true);
		}
		if (bold)
		{
			builder.bold(true);
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
							  .ifPresent(builder::event);
						  data.getHoverEvent()
							  .ifPresent(builder::event);
					  });
		}
	}


	private boolean currentHasText()
	{
		final BaseComponent current = builder.getCurrentComponent();
		return current instanceof TextComponent && !((TextComponent) current).getText().isEmpty();
	}

	private void cleanseEmpty()
	{
		while (!builder.getParts().isEmpty() && !currentHasText())
		{
			if (builder.getCurrentComponent().getExtra() != null && !builder.getCurrentComponent().getExtra().isEmpty())
			{
				break;
			}

			builder.resetCursor().removeComponent(builder.getCursor());
		}
	}

}
