package com.cibernet.minestuckuniverse.commands;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.events.handlers.GTEventHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.util.EnumLunarSway;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.cibernet.minestuckuniverse.world.gen.structure.StructureQuestBed;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MSGTCommand extends CommandBase
{
	@Override
	public String getName() {
		return "godtier";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.godtier.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length < 1)
			throw new WrongUsageException("commands.godtier.usage", new Object[0]);

		EntityPlayer target;

		switch (args[0])
		{
			case "locate":
				if(!(sender.getEntityWorld().provider.createChunkGenerator() instanceof ChunkProviderLands))
					throw new WrongUsageException("commands.godtier.locate.wrongDim");
				BlockPos pos = StructureQuestBed.getQuestBedPos(sender.getEntityWorld());
				sender.sendMessage(new TextComponentTranslation("commands.locate.success", "Quest Bed", pos.getX(), pos.getZ()));
				break;

			case "title":

				if(args.length < 3)
					throw new WrongUsageException("commands.godtier.usage", new Object[0]);
				target = (args.length >= 4) ? getPlayer(server, sender, args[3]) : getCommandSenderAsPlayer(sender);

				String classStr = args[1], aspectStr = args[2];

				EnumClass titleClass = null;
				EnumAspect titleAspect = null;

				try	//Parse class
				{
					for(EnumClass c : EnumClass.values())
						if(c.name().equalsIgnoreCase(classStr))
						{
							titleClass = c;
							break;
						}

					if(titleClass == null)
					{
						int classIndex = Integer.parseInt(classStr);
						titleClass = EnumClass.getClassFromInt(classIndex);
					}
				} catch(Exception e)
				{
					throw new WrongUsageException("commands.sburbSession.notClass", classStr);
				}
				try	//Parse aspect
				{
					for(EnumAspect aspect : EnumAspect.values())
						if(aspect.name().equalsIgnoreCase(aspectStr))
						{
							titleAspect = aspect;
							break;
						}

					if(titleAspect == null)
					{
						int aspectIndex = Integer.parseInt(aspectStr);
						titleAspect = EnumAspect.getAspectFromInt(aspectIndex);
					}
				} catch(Exception e)
				{
					throw new WrongUsageException("commands.sburbSession.notAspect", aspectStr);
				}

				MSUUtils.changeTitle(target, titleClass, titleAspect);
				notifyCommandListener(sender, this, 0, "commands.godtier.title.success", new Object[] {target.getDisplayName(), titleClass.asTextComponent(), titleAspect.asTextComponent()});

				break;
			case "reset":

				target = (args.length >= 2) ? getPlayer(server, sender, args[1]) : getCommandSenderAsPlayer(sender);
				MSUUtils.resetGodTier(target);
				notifyCommandListener(sender, this, 0, "commands.godtier.reset.success", new Object[] {target.getDisplayName()});
				break;
			case "mastercontrol":

				if(args.length < 2 || (!args[1].equals("true") && !args[1].equals("false")))
					throw new WrongUsageException("commands.godtier.mastercontrol.usage", new Object[0]);

				boolean enable = args[1].equals("true");
				target = (args.length >= 3) ? getPlayer(server, sender, args[2]) : getCommandSenderAsPlayer(sender);
				target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).setMasterControl(enable);
				target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).update();
				notifyCommandListener(sender, this, 0, "commands.godtier.mastercontrol.success." + (enable ? "on" : "off"), new Object[] {target.getDisplayName()});
				break;
			case "maxbadges":

				if(args.length < 2)
					throw new WrongUsageException("commands.godtier.maxbadges.usage", new Object[0]);

				try
				{
					int count = Integer.parseInt(args[1]);
					target = (args.length >= 3) ? getPlayer(server, sender, args[2]) : getCommandSenderAsPlayer(sender);
					target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).setMaxBadges(count);
					target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).update();
					notifyCommandListener(sender, this, 0, "commands.godtier.maxbadges.success", new Object[] {target.getDisplayName(), count});
				}
				catch (NumberFormatException e) {throw new WrongUsageException("commands.generic.num.invalid", args[1]);}

				break;
			case "lunarsway":
				if(args.length < 2)
					throw new WrongUsageException("commands.godtier.maxbadges.usage", new Object[0]);

				EnumLunarSway sway = args[1].equals("prospit") ? EnumLunarSway.PROSPIT : args[1].equals("derse") ? EnumLunarSway.DERSE : null;
				if(sway == null)
					throw new WrongUsageException("commands.godtier.lunarsway.usage", new Object[0]);

				target = (args.length >= 3) ? getPlayer(server, sender, args[2]) : getCommandSenderAsPlayer(sender);
				target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).setLunarSway(sway);
				target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).update();
				notifyCommandListener(sender, this, 0, "commands.godtier.lunarsway."+args[1], new Object[] {target.getDisplayName()});
				break;
			default: throw new WrongUsageException("commands.godtier.usage", new Object[0]);
			case "ascend":
				target = (args.length >= 2) ? getPlayer(server, sender, args[1]) : getCommandSenderAsPlayer(sender);

				MinestuckPlayerData.PlayerData data = MinestuckPlayerData.getData(target);

				if(data.title == null)
					throw new WrongUsageException("commands.godtier.ascend.noClass", target);

				data.echeladder.setProgressEnabled(false);
				target.getCapability(MSUCapabilities.GOD_TIER_DATA, null).setToBaseGodTier(true);
				target.world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentTranslation("status.godTier", target.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GREEN)));
				target.sendStatusMessage(new TextComponentTranslation("status.godTierMeditation.unlock").setStyle(new Style().setColor(TextFormatting.GOLD)), false);

				for(EntityEquipmentSlot slot : EntityEquipmentSlot.values())
				{
					if(slot.getSlotType().equals(EntityEquipmentSlot.Type.ARMOR))
						CaptchaDeckHandler.launchAnyItem(target, target.getItemStackFromSlot(slot));
				}


				NBTTagCompound armorNbt = new NBTTagCompound();
				armorNbt.setInteger("class", data.title.getHeroClass().ordinal());
				armorNbt.setInteger("aspect", data.title.getHeroAspect().ordinal());
				Item[] armor = new Item[]{MinestuckUniverseItems.gtShoes, MinestuckUniverseItems.gtPants, MinestuckUniverseItems.gtShirt, MinestuckUniverseItems.gtHood};
				for(int i = 0; i < armor.length; i++)
				{
					ItemStack armorStack = new ItemStack(armor[i]);
					armorStack.setTagCompound(armorNbt);
					target.setItemStackToSlot(EntityEquipmentSlot.values()[i + 2], armorStack);
				}


				for(Potion potionEffect : GTEventHandler.getAspectEffects(target).keySet())
					target.removeActivePotionEffect(potionEffect);

				target.motionY = 0.8;
				target.capabilities.allowFlying = true;
				target.capabilities.isFlying = true;
				target.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 40, 19));
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{

		if(args.length == 1)
			return getListOfStringsMatchingLastWord(args, "title", "reset", "ascend", "mastercontrol", "maxbadges", "locate", "lunarsway");
		else switch(args[0])
		{
			case "reset":
				if(args.length == 2)
					return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
				break;
			case "ascend":
				if(args.length == 2)
					return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
				break;
			case "mastercontrol":
				if(args.length == 2)
					return getListOfStringsMatchingLastWord(args, "true", "false");
			case "maxbadges":
				if(args.length == 3)
					return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
				break;
			case "title":
				switch (args.length)
				{
					case 2: return getListOfStringsMatchingLastWord(args, Arrays.asList(EnumClass.values()));
					case 3: return getListOfStringsMatchingLastWord(args, Arrays.asList(EnumAspect.values()));
					case 4: return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
				}
				break;
			case "lunarsway":
				return getListOfStringsMatchingLastWord(args, "prospit", "derse");
		}

		return Collections.emptyList();
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 3;
	}
}
