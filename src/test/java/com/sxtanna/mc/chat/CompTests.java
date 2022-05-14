package com.sxtanna.mc.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import com.sxtanna.mc.chat.util.Comp;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CompTests
{

    private static final String HEX_COLORED_TEXT = "&#000000Black Text &#FFFFFFWhite Text";


    @Test
    void test_hex_color_pattern()
    {
        final Matcher matcherBlack = Comp.HEX_COLOR.matcher("&#000000");
        assertTrue(matcherBlack.find());
        assertEquals("000000", matcherBlack.group(1));

        final Matcher matcherWhite = Comp.HEX_COLOR.matcher("&#FFFFFF");
        assertTrue(matcherWhite.find());
        assertEquals("FFFFFF", matcherWhite.group(1));
    }

    @Test
    void test_comp_of_basic_hello_world()
    {
        final BaseComponent[] of = Comp.of("hello world");
        assertEquals(1, of.length);
        assertInstanceOf(TextComponent.class, of[0]);

        final TextComponent text = (TextComponent) of[0];
        assertEquals("hello world", text.getText());
        assertEquals(ChatColor.WHITE, text.getColor());

        assertFalse(text.isBold());
        assertFalse(text.isItalic());
        assertFalse(text.isUnderlined());
        assertFalse(text.isStrikethrough());
        assertFalse(text.isObfuscated());

        assertNull(text.getFont());
        assertNull(text.getInsertion());
        assertNull(text.getClickEvent());
        assertNull(text.getHoverEvent());
    }

    @Test
    void test_comp_of_colored()
    {
        final BaseComponent[] of = Comp.of(HEX_COLORED_TEXT);
        assertEquals(2, of.length);
        assertInstanceOf(TextComponent.class, of[0]);
        assertInstanceOf(TextComponent.class, of[1]);

        final TextComponent blackText = (TextComponent) of[0];
        final TextComponent whiteText = (TextComponent) of[1];

        assertEquals("Black Text ", blackText.getText());
        assertEquals(ChatColor.of("#000000"), blackText.getColor());

        assertFalse(blackText.isBold());
        assertFalse(blackText.isItalic());
        assertFalse(blackText.isUnderlined());
        assertFalse(blackText.isStrikethrough());
        assertFalse(blackText.isObfuscated());

        assertNull(blackText.getFont());
        assertNull(blackText.getInsertion());
        assertNull(blackText.getClickEvent());
        assertNull(blackText.getHoverEvent());

        assertEquals("White Text", whiteText.getText());
        assertEquals(ChatColor.of("#FFFFFF"), whiteText.getColor());

        assertFalse(whiteText.isBold());
        assertFalse(whiteText.isItalic());
        assertFalse(whiteText.isUnderlined());
        assertFalse(whiteText.isStrikethrough());
        assertFalse(whiteText.isObfuscated());

        assertNull(whiteText.getFont());
        assertNull(whiteText.getInsertion());
        assertNull(whiteText.getClickEvent());
        assertNull(whiteText.getHoverEvent());
    }

    @Test
    void test_chat_color_translate_hex_colors()
    {
        final String text = Comp.preprocessHexColors(HEX_COLORED_TEXT);
        assertEquals("§x§0§0§0§0§0§0Black Text §x§F§F§F§F§F§FWhite Text", text);

        final String translated = ChatColor.translateAlternateColorCodes('&', text);
        assertEquals("§x§0§0§0§0§0§0Black Text §x§F§F§F§F§F§FWhite Text", translated);
    }

}
