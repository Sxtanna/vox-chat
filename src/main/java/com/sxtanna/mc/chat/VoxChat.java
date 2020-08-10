package com.sxtanna.mc.chat;

import com.sxtanna.mc.chat.core.ActionManager;
import com.sxtanna.mc.chat.core.FormatManager;
import com.sxtanna.mc.chat.hook.Placeholders;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public final class VoxChat
{

	static VoxChat INSTANCE;

	@NotNull
	private static VoxChat getInstance()
	{
		return Objects.requireNonNull(INSTANCE, "VoxChat has not be enabled yet!");
	}

	@NotNull
	public static Optional<String> find(@NotNull final Player player)
	{
		return getFormatManager().list().stream().filter(name -> player.hasPermission("voxchat.format." + name)).findFirst();
	}

	public static boolean send(@NotNull final String format, @NotNull final Player player, @NotNull final String message, @NotNull final Collection<? extends CommandSender> recipients)
	{
		final Optional<BaseComponent[]> components = getFormatManager().prepare(format, player, message);
		if (!components.isPresent())
		{
			return false;
		}

		recipients.forEach(other -> other.spigot().sendMessage(components.get()));
		return true;
	}


	@NotNull
	public static ActionManager getActionManager()
	{
		return getInstance().getPlugin().getActionManager();
	}

	@NotNull
	public static FormatManager getFormatManager()
	{
		return getInstance().getPlugin().getFormatManager();
	}

	@NotNull
	public static Placeholders getReplacer()
	{
		return getInstance().getPlugin().getReplacer();
	}


	@NotNull
	private final VoxChatPlugin plugin;


	VoxChat(@NotNull final VoxChatPlugin plugin)
	{
		this.plugin = plugin;
	}


	@NotNull
	public VoxChatPlugin getPlugin()
	{
		return plugin;
	}

}
