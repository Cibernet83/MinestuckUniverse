package com.cibernet.minestuckuniverse.commands;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.Skill;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillCommand extends CommandBase
{
	@Override
	public String getName() {
		return "sburbSkills";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.sburbSkills.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{

		if (args.length < 2)
			throw new WrongUsageException("commands.sburbSkills.usage");

		EntityPlayer target = (args.length >= 3) ? getPlayer(server, sender, args[2]) : getCommandSenderAsPlayer(sender);
		IGodTierData data = target.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		Skill skill = MSUSkills.REGISTRY.getValue(new ResourceLocation(args[1]));

		switch (args[0])
		{
			case "grant":
				if(args[1].toLowerCase().equals("all"))
				{
					for(Skill s : Abilitech.ABILITECHS)
						data.addSkill(s, false);
					data.update();
					notifyCommandListener(sender, this, 0, "commands.sburbSkills.grant.all", target.getDisplayName());
				}
				else
				{
					data.addSkill(skill, true);
					notifyCommandListener(sender, this, 0, "commands.sburbSkills.grant.success", target.getDisplayName(), skill.getDisplayComponent());
				}
				break;
			case "revoke":
				if(args[1].toLowerCase().equals("all"))
				{
					for(Skill s : new ArrayList<>(data.getSkills()))
						if(skill instanceof Abilitech)
							data.revokeSkill(s, false);
					data.update();
					notifyCommandListener(sender, this, 0, "commands.sburbSkills.revoke.all", target.getDisplayName());
				}
				else
				{
					data.revokeSkill(skill, true);
					notifyCommandListener(sender, this, 0, "commands.sburbSkills.revoke.success", target.getDisplayName(), skill.getDisplayComponent());
				}
				break;
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if(args.length == 1)
			return getListOfStringsMatchingLastWord(args, "grant", "revoke");
		else if(args.length == 2)
			return getListOfStringsMatchingLastWord(args, MSUSkills.REGISTRY.getKeys());
		else if(args.length == 3)
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());

		return super.getTabCompletions(server, sender, args, targetPos);
	}
}
