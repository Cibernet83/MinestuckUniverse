package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.blocks.BlockHeroStone;
import com.cibernet.minestuckuniverse.blocks.IGodTierBlock;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.GodTierData;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.damage.CritDamageSource;
import com.cibernet.minestuckuniverse.damage.EntityCritDamageSource;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.world.gen.structure.StructureQuestBed;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GTEventHandler
{

	public static Potion[] aspectEffects = { MobEffects.ABSORPTION, MobEffects.SPEED, MobEffects.RESISTANCE, MobEffects.ABSORPTION, MobEffects.FIRE_RESISTANCE, MobEffects.REGENERATION, MobEffects.LUCK, MobEffects.NIGHT_VISION, MobEffects.STRENGTH, MobEffects.JUMP_BOOST, MobEffects.HASTE, MobEffects.INVISIBILITY }; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	// Increase the starting rungs
	public static float[] aspectStrength = new float[] {1.0F/12, 1.0F/15, 1.0F/28, 1.0F/25, 1.0F/18, 1.0F/20, 1.0F/10, 1.0F/12, 1.0F/25, 1.0F/10, 1.0F/13, 1.0F/12}; //Absorption, Speed, Resistance, Saturation, Fire Resistance, Regeneration, Luck, Night Vision, Strength, Jump Boost, Haste, Invisibility

	private static final UUID GOD_TIER_SPEED_UUID = MathHelper.getRandomUUID(ThreadLocalRandom.current());
	private static final UUID GOD_TIER_ATTACK_UUID = MathHelper.getRandomUUID(ThreadLocalRandom.current());
	private static final UUID GOD_TIER_DEFENSE_UUID = MathHelper.getRandomUUID(ThreadLocalRandom.current());
	private static final UUID GOD_TIER_LUCK_UUID = MathHelper.getRandomUUID(ThreadLocalRandom.current());
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if (event.getEntity().world.isRemote)
			return;

		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.encode(player);
			SburbConnection c = SkaianetHandler.getMainConnection(identifier, true);
			MinestuckPlayerData.PlayerData data = MinestuckPlayerData.getData(identifier);
			
			if(c != null && c.enteredGame() && player.world.getBlockState(new BlockPos(player.posX, player.posY - 0.1, player.posZ)).getBlock() instanceof IGodTierBlock)
			{
				IGodTierBlock block = (IGodTierBlock) player.world.getBlockState(new BlockPos(player.posX, player.posY - 0.1, player.posZ)).getBlock();
				
				if(block.canGodTier() && (block.getAspect() == null || block.getAspect().equals(data.title.getHeroAspect())))
				{
					IGodTierData gtData = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
					if(!player.isCreative() && !gtData.canGodTier(player.world) && !gtData.isGodTier())
						player.sendStatusMessage(new TextComponentTranslation("status.godTierReject").setStyle(new Style().setColor(TextFormatting.DARK_GREEN)), false);
					else
					{
						if(!gtData.isGodTier())
						{
							MinestuckPlayerData.getData(player).echeladder.setProgressEnabled(false);
							gtData.setToBaseGodTier(true);
							player.world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentTranslation("status.godTier", player.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GREEN)));
							player.sendStatusMessage(new TextComponentTranslation("status.godTierMeditation.unlock").setStyle(new Style().setColor(TextFormatting.GOLD)), false);

							for(EntityEquipmentSlot slot : EntityEquipmentSlot.values())
							{
								if(slot.getSlotType().equals(EntityEquipmentSlot.Type.ARMOR))
									CaptchaDeckHandler.launchAnyItem(player, player.getItemStackFromSlot(slot));
							}
							
							
							NBTTagCompound armorNbt = new NBTTagCompound();
							armorNbt.setInteger("class", data.title.getHeroClass().ordinal());
							armorNbt.setInteger("aspect", data.title.getHeroAspect().ordinal());
							Item[] armor = new Item[]{MinestuckUniverseItems.gtShoes, MinestuckUniverseItems.gtPants, MinestuckUniverseItems.gtShirt, MinestuckUniverseItems.gtHood};
							for(int i = 0; i < armor.length; i++)
							{
								ItemStack armorStack = new ItemStack(armor[i]);
								armorStack.setTagCompound(armorNbt);
								player.setItemStackToSlot(EntityEquipmentSlot.values()[i + 2], armorStack);
							}
							

							for(Potion potionEffect : getAspectEffects(player).keySet())
								player.removeActivePotionEffect(potionEffect);

							player.motionY = 0.8;
							player.capabilities.allowFlying = true;
							player.capabilities.isFlying = true;
							player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 40, 19));
						}
						else player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));
						
						for(int i = 0; i < 30; i++)
							player.world.spawnParticle(EnumParticleTypes.TOTEM, player.posX, player.posY, player.posZ, 0.0D, 0.0D, 0.0D);
						player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_TOTEM_USE, player.getSoundCategory(), 1.0F, 1.0F);
						
						player.setHealth(2);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public static void playerDeathPost(LivingDeathEvent event)
	{
		if(event.isCanceled() && event.getEntityLiving().world.getWorldInfo().isHardcoreModeEnabled() && event.getEntityLiving() instanceof EntityPlayer && ((EntityPlayer) event.getEntityLiving()).isSpectator()
				&& event.getEntityLiving().hasCapability(MSUCapabilities.GOD_TIER_DATA, null) &&
				event.getEntityLiving().getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier())
				((EntityPlayer) event.getEntityLiving()).setGameType(event.getEntityLiving().world.getWorldInfo().getGameType());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(Minecraft.getMinecraft().player == null)
			return;

		IGodTierData data = Minecraft.getMinecraft().player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		boolean canFly = !Minecraft.getMinecraft().player.isPotionActive(MSUPotions.EARTHBOUND);

		if(event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().player == null)
			return;
		if(data.isGodTier())
			Minecraft.getMinecraft().player.capabilities.allowFlying = canFly;

		applyModifiers(Minecraft.getMinecraft().player, data);
	}

	public static final List<Potion> REFRESH_POTIONS = Arrays.asList(MobEffects.ABSORPTION, MobEffects.REGENERATION, MobEffects.WITHER, MobEffects.POISON, MSUPotions.GOD_TIER_COMEBACK);

	@SubscribeEvent
	public static void onTick(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.encode(event.player);
		SburbConnection c = SkaianetHandler.getMainConnection(identifier, true);
		int rung = MinestuckPlayerData.getData(identifier).echeladder.getRung();
		EnumAspect aspect = null;
		if(c != null && c.enteredGame())
			aspect = MinestuckPlayerData.getTitle(identifier).getHeroAspect();

		IGodTierData gtData = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);

		if(!player.world.isRemote)
			applyModifiers(player, gtData);

		if(gtData.isGodTier())
		{
			boolean canFly = !event.player.isPotionActive(MSUPotions.EARTHBOUND);

			player.capabilities.allowFlying = canFly;

			if (player.world.getBlockState(new BlockPos(player.posX, player.posY - 0.1, player.posZ)).getBlock() instanceof BlockHeroStone) {
				BlockHeroStone block = (BlockHeroStone) player.world.getBlockState(new BlockPos(player.posX, player.posY - 0.1, player.posZ)).getBlock();

				if (block.getAspect() != aspect && block.getAspect() != null && block instanceof BlockHeroStone && block.isChiseled())
				{
					int potionLevel = (int) (aspectStrength[block.getAspect().ordinal()] * (float) 40);
					if (block.getAspect() == EnumAspect.HOPE && !player.getActivePotionEffects().contains(MobEffects.WATER_BREATHING))
						player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 600, 0, true, false));
					if (potionLevel > 0 && !player.isPotionActive(aspectEffects[block.getAspect().ordinal()]))
						player.addPotionEffect(new PotionEffect(aspectEffects[block.getAspect().ordinal()], 600, potionLevel - 1));
				}
			}
		}

		SburbConnection worlcC = SburbHandler.getConnectionForDimension(player.world.provider.getDimension());
		if(worlcC != null)
		{
			boolean isLandOwner = player.equals(worlcC.getClientIdentifier().getPlayer());
			BlockPos questBedPos = StructureQuestBed.getQuestBedPos(player.world);

			//Climbed the Spire check
			if(isLandOwner && !gtData.climbedTheSpire() && player.onGround && player.posY >= StructureQuestBed.top
					&& player.posX > questBedPos.getX()-StructureQuestBed.radius && player.posX < questBedPos.getX()+StructureQuestBed.radius
					&& player.posZ > questBedPos.getZ()-StructureQuestBed.radius && player.posZ < questBedPos.getZ()+StructureQuestBed.radius)
				gtData.setClimbedTheSpire(true);


			//Quest Bed Debuffs
			if(!player.isCreative())
			{
				final int radius = 250;
				if(player.posX > questBedPos.getX()-radius && player.posX < questBedPos.getX()+radius
						&& player.posZ > questBedPos.getZ()-radius && player.posZ < questBedPos.getZ()+radius)
				{
					if(!gtData.isGodTier())
					{
						if(!gtData.climbedTheSpire() && (!player.isPotionActive(MSUPotions.EARTHBOUND) || player.getActivePotionEffect(MSUPotions.EARTHBOUND).getDuration() < 20))
							player.addPotionEffect(new PotionEffect(MSUPotions.EARTHBOUND, 40, 0, true, true));
						if(!player.isPotionActive(MSUPotions.CREATIVE_SHOCK) || player.getActivePotionEffect(MSUPotions.CREATIVE_SHOCK).getDuration() < 20)
							player.addPotionEffect(new PotionEffect(MSUPotions.CREATIVE_SHOCK, 40, 0, true, true));
					}
				}

			}
		}

		//Aspect Effects
		if(MinestuckConfig.aspectEffects && c != null && c.enteredGame())
		{
			HashMap<Potion, PotionEffect> appliedPotions = getAspectEffects(player);

			if (MinestuckPlayerData.getEffectToggle(identifier) && !player.isPotionActive(MSUPotions.GOD_TIER_LOCK))
				for (PotionEffect effect : appliedPotions.values())
				{
					PotionEffect currentPotionEffect = player.getActivePotionEffect(effect.getPotion());
					if (currentPotionEffect == null || (currentPotionEffect.getDuration() <= 200 && currentPotionEffect.getAmplifier() <= effect.getAmplifier() && !REFRESH_POTIONS.contains(effect.getPotion())))
						player.addPotionEffect(effect);
				}

			else if(gtData.isGodTier() || player.isPotionActive(MSUPotions.GOD_TIER_LOCK))
				for (Potion key : appliedPotions.keySet())
					if (player.isPotionActive(key))
						player.removePotionEffect(key);
		}
	}

	public static HashMap<Potion, PotionEffect> getAspectEffects(EntityPlayer player)
	{
		IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.encode(player);
		IGodTierData gtData = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		SburbConnection c = SkaianetHandler.getMainConnection(identifier, true);
		int rung = MinestuckPlayerData.getData(identifier).echeladder.getRung();
		EnumAspect aspect = null;
		HashMap<Potion, PotionEffect> appliedPotions = new HashMap<>();
		if(c != null && c.enteredGame())
			aspect = MinestuckPlayerData.getTitle(identifier).getHeroAspect();
		
		if(aspect == null)
			return appliedPotions;

		int potionLevel = (int)(aspectStrength[aspect.ordinal()] * (float)(gtData.isGodTier() ? 60 : rung)) + (gtData.isBadgeActive(MSUSkills.BADGE_PAGE) ? 2 : 0);
		
		if (gtData.isBadgeActive(MSUSkills.EFFECT_BUFF))
			switch (aspect)
			{
				case DOOM:
					appliedPotions.put(MobEffects.ABSORPTION, new PotionEffect(MobEffects.ABSORPTION, 600, 2, true, false));
					break;
				case HOPE:
					appliedPotions.put(MSUPotions.DECAYPROOF, new PotionEffect(MSUPotions.DECAYPROOF, 600, 0, true, false));
					break;
				case MIND:
					if (!(player.getActivePotionEffect(MobEffects.GLOWING) != null && player.getActivePotionEffect(MobEffects.GLOWING).getAmplifier() >= 2))
						appliedPotions.put(MSUPotions.MIND_FORTITUDE, new PotionEffect(MSUPotions.MIND_FORTITUDE, 600, 0, true, false));
					break;
				case VOID:
					appliedPotions.put(MSUPotions.VOID_CONCEAL, new PotionEffect(MSUPotions.VOID_CONCEAL, 600, 0, true, false));
					break;
				default:
					potionLevel *= 2;
					break;
			}

		Potion effect = aspectEffects[aspect.ordinal()];
		if(potionLevel > 0)
			appliedPotions.put(effect, new PotionEffect(effect, 600, potionLevel - 1, true, false));

		if ((gtData.isGodTier() || rung > 18) && aspect == EnumAspect.HOPE)
			appliedPotions.put(MobEffects.WATER_BREATHING, new PotionEffect(MobEffects.WATER_BREATHING, 600, 0, true, false));

		return appliedPotions;
	}

	public static final ArrayList<DamageSource> BLOCKABLE_UNBLOCKABLES = new ArrayList<DamageSource>()
	{{
		add(DamageSource.FLY_INTO_WALL);
	}};

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onPlayerHurt(LivingHurtEvent event)
	{
		if(!(event.getEntity() instanceof EntityPlayer) ||
				(event.getSource().isUnblockable() && !event.getSource().isFireDamage() &&
				!(event.getSource() instanceof CritDamageSource) && !event.getSource().isMagicDamage() && !BLOCKABLE_UNBLOCKABLES.contains(event.getSource())))
			return;

		IGodTierData data = event.getEntityLiving().getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		float dmgReduction = data.getSkillLevel(GodTierData.StatType.DEFENSE) * 0.002f * (data.isBadgeActive(MSUSkills.BADGE_PAGE) ? 2 : 1);

		if(event.getSource() instanceof CritDamageSource || event.getSource() instanceof EntityCritDamageSource)
			dmgReduction *= 0.4;
		if(event.getSource().isMagicDamage() || event.getSource().equals(DamageSource.DRAGON_BREATH))
			dmgReduction *= 0.5;
		if(event.getSource().equals(DamageSource.CACTUS) || event.getSource().isFireDamage())
			dmgReduction *= 0.75;

		event.setAmount(event.getAmount()*Math.max(0, 1-dmgReduction));

	}

	protected static void applyModifiers(EntityPlayer player, IGodTierData data)
	{

		IAttributeInstance attackAtr = player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		IAttributeInstance defenseAtr = player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
		IAttributeInstance speedAtr = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		IAttributeInstance luckAtr = player.getEntityAttribute(SharedMonsterAttributes.LUCK);


		AttributeModifier attackMod = new AttributeModifier(GOD_TIER_ATTACK_UUID, "God Tier Attack", data.getSkillAttributeLevel(GodTierData.StatType.ATTACK), 2).setSaved(false);
		AttributeModifier defenseMod = new AttributeModifier(GOD_TIER_DEFENSE_UUID, "God Tier Defense", data.getSkillAttributeLevel(GodTierData.StatType.DEFENSE), 0).setSaved(false);
		AttributeModifier speedMod = new AttributeModifier(GOD_TIER_SPEED_UUID, "God Tier Athletics", data.getSkillAttributeLevel(GodTierData.StatType.SPEED), 2).setSaved(false);
		AttributeModifier luckMod = new AttributeModifier(GOD_TIER_LUCK_UUID, "God Tier Luck", data.getSkillAttributeLevel(GodTierData.StatType.LUCK), 0).setSaved(false);

		NBTTagCompound playerCaps = new NBTTagCompound();
		player.capabilities.writeCapabilitiesToNBT(playerCaps);
		NBTTagCompound playerAbs = playerCaps.getCompoundTag("abilities");
		playerAbs.setFloat("flySpeed", player.getAIMoveSpeed() / 2f);
		playerCaps.setTag("abilities", playerAbs);
		player.capabilities.readCapabilitiesFromNBT(playerCaps);



		if(shouldRefreshAttribute(attackAtr, attackMod))
			attackAtr.removeModifier(attackMod);
		if(shouldRefreshAttribute(defenseAtr, defenseMod))
			defenseAtr.removeModifier(defenseMod);
		if((speedAtr.hasModifier(speedMod) && !player.isSprinting()) || shouldRefreshAttribute(speedAtr, speedMod))
			speedAtr.removeModifier(speedMod);
		if(shouldRefreshAttribute(luckAtr, luckMod))
			luckAtr.removeModifier(luckMod);

		if(data.isGodTier())
		{
			if(!attackAtr.hasModifier(attackMod))
				attackAtr.applyModifier(attackMod);
			if(!defenseAtr.hasModifier(defenseMod))
				defenseAtr.applyModifier(defenseMod);
			if(!luckAtr.hasModifier(luckMod))
			luckAtr.applyModifier(luckMod);
			if(player.isSprinting() && !speedAtr.hasModifier(speedMod))
				speedAtr.applyModifier(speedMod);
		}
	}

	public static boolean shouldRefreshAttribute(IAttributeInstance instance, AttributeModifier modifier)
	{
		if(instance.hasModifier(modifier))
		{
			AttributeModifier existingModifier = instance.getModifier(modifier.getID());
			return existingModifier.getAmount() != modifier.getAmount() || existingModifier.getOperation() != modifier.getOperation();
		}
		return false;
	}

	@SubscribeEvent
	public static void onPlayerJoinWorld(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityPlayerMP)
		{
			SburbConnection c = SkaianetHandler.getMainConnection(IdentifierHandler.encode((EntityPlayer)event.getEntity()), true);
			IGodTierData data = event.getEntity().getCapability(MSUCapabilities.GOD_TIER_DATA, null);

			if(data.getConsortType() == null && c != null)
			{
				LandAspectRegistry.AspectCombination landspects = MinestuckDimensionHandler.getAspects(c.getClientDimension());
				if(landspects != null)
					data.setConsortType(landspects.aspectTerrain.getConsortType());
			}

			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_DATA_SERVER, ((EntityPlayer)event.getEntity())), ((EntityPlayer)event.getEntity()));
		}
	}
}
