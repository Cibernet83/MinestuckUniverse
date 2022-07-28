package com.cibernet.minestuckuniverse.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.List;

public class GlobalSayCommand extends CommandBase
{
	@Override
	public String getName()
	{
		return "globalsay";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.globalsay.usage";
	}

	/**
	 * Callback for when the command is executed
	 */
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length <= 0)
		{
			throw new WrongUsageException("commands.globalsay.usage", new Object[0]);
		}
		else
		{
			ITextComponent itextcomponent = getChatComponentFromNthArg(sender, args, 0, !(sender instanceof EntityPlayer));
			server.getPlayerList().getPlayers().forEach((entityPlayerMP -> entityPlayerMP.sendStatusMessage(new TextComponentTranslation("chat.type.emote", new Object[] {sender.getDisplayName(), itextcomponent}), false)));
		}
	}

	/**
	 * Get a list of options for when the user presses the TAB key
	 */
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
	}
}
