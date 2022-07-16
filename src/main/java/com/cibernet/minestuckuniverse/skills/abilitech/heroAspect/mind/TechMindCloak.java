package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.mind;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.client.render.RenderPlayerCloak;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;

public class TechMindCloak extends TechHeroAspect
{

	public TechMindCloak(String name, long cost)
	{
		super(name, EnumAspect.MIND, cost, EnumTechType.UTILITY);
	}

	@Override
	public void onUnequipped(World world, EntityPlayer player, int techSlot)
	{
		super.onUnequipped(world, player, techSlot);
		player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).clearCloakData();
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(badgeEffects.isCloaked())
		{
			if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
			{
				player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
				badgeEffects.clearCloakData();
				return false;
			}

			if(!player.isCreative() && time % 60 == 0)
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);
		}

		if(state != SkillKeyStates.KeyState.PRESS)
			return false;

		RayTraceResult result = MSUUtils.getMouseOver(world, player, player.getEntityAttribute(EntityPlayerMP.REACH_DISTANCE).getAttributeValue(), false);
		NBTTagCompound cloakData = new NBTTagCompound();

		if(result == null || result.typeOfHit == RayTraceResult.Type.MISS)
		{
			badgeEffects.clearCloakData();
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.MIND, 5);
			player.sendStatusMessage(new TextComponentTranslation("status.tech.illusoryCloak.reset"), true);
		}
		else if(result.entityHit != null)
		{
			if(false)//result.entityHit instanceof EntityPlayer)
			{
				EntityPlayer cloakPlayer = (EntityPlayer) result.entityHit;
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, cloakPlayer, this, techSlot, false)))
					return false;
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setUniqueId("UUID", cloakPlayer.getUniqueID());
				cloakData.setTag("Player", nbt);
				badgeEffects.setCloakData(cloakData);
				badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.MIND, 5);
				player.sendStatusMessage(new TextComponentTranslation("status.tech.illusoryCloak.disguise", player.getDisplayName()), true);
			}
			else
			{
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, result.entityHit, this, techSlot, false)))
					return false;
				NBTTagCompound nbt = result.entityHit.writeToNBT(new NBTTagCompound());
				nbt.setString("id", EntityRegistry.getEntry(result.entityHit.getClass()).getRegistryName().toString());

				cloakData.setTag("Entity", nbt );
				badgeEffects.setCloakData(cloakData);
				badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.MIND, 5);
				player.sendStatusMessage(new TextComponentTranslation("status.tech.illusoryCloak.disguise", result.entityHit.getDisplayName()), true);
			}
		}
		else if(result.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			IBlockState blockState = world.getBlockState(result.getBlockPos());
			NBTTagCompound compound = new NBTTagCompound();

			ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(blockState.getBlock());
			compound.setString("Block", resourcelocation.toString());
			compound.setByte("Data", (byte)blockState.getBlock().getMetaFromState(blockState));

			cloakData.setTag("Block", compound);
			badgeEffects.setCloakData(cloakData);
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.MIND, 5);
			player.sendStatusMessage(new TextComponentTranslation("status.tech.illusoryCloak.disguise", new TextComponentTranslation(blockState.getBlock().getUnlocalizedName())), true);

		}


		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return false;
	}

	private static final HashMap<EntityLivingBase, Entity> cloakedCache = new HashMap<>();
	private static final HashMap<EntityLivingBase, IBlockState> cloakBlockCache = new HashMap<>();

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderLiving(RenderLivingEvent event)
	{
		event.setCanceled(renderCloak(event.getEntity()));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderPlayer(RenderPlayerEvent.Pre event)
	{
		event.setCanceled(renderCloak(event.getEntityLiving()));
	}

	@SideOnly(Side.CLIENT)
	protected static boolean renderCloak(EntityLivingBase entity)
	{
		if(Minecraft.getMinecraft().player.isPotionActive(MSUPotions.MIND_FORTITUDE) || !entity.getCapability(MSUCapabilities.BADGE_EFFECTS, null).isCloaked())
		{
			cloakedCache.remove(entity);
			return false;
		}


		NBTTagCompound cloakData = entity.getCapability(MSUCapabilities.BADGE_EFFECTS, null).getCloakData();

		if(cloakData.hasKey("Player"))
		{
			if(entity instanceof AbstractClientPlayer)
			{
				NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(cloakData.getCompoundTag("Player").getUniqueId("UUID"));

				if(playerInfo != null)
				new RenderPlayerCloak(Minecraft.getMinecraft().getRenderManager(), !playerInfo.getSkinType().equals("slim"), playerInfo.getLocationSkin())
						.doRender((AbstractClientPlayer) entity, entity.posX, entity.posY, entity.posZ, entity.rotationYawHead, Minecraft.getMinecraft().getRenderPartialTicks());
			}
		}
		else
		{
			if(cloakData.hasKey("Entity"))
				if(!cloakedCache.containsKey(entity) || !EntityRegistry.getEntry(cloakedCache.get(entity).getClass()).getRegistryName().toString().equals(cloakData.getCompoundTag("Entity").getString("id")) || !cloakedCache.get(entity).world.equals(entity.world))
					cloakedCache.put(entity, EntityList.createEntityFromNBT(cloakData.getCompoundTag("Entity"), entity.world));
			if(cloakData.hasKey("Block"))
			{


				IBlockState state;
				NBTTagCompound compound = cloakData.getCompoundTag("Block");
				int i = compound.getByte("Data") & 255;

				if (compound.hasKey("Block", 8))
					state = Block.getBlockFromName(compound.getString("Block")).getStateFromMeta(i);
				else if (compound.hasKey("TileID", 99))
					state = Block.getBlockById(compound.getInteger("TileID")).getStateFromMeta(i);
				else state = Block.getBlockById(compound.getByte("Tile") & 255).getStateFromMeta(i);

				if(!cloakedCache.containsKey(entity) || !(cloakedCache.get(entity) instanceof EntityFallingBlock) || !cloakedCache.get(entity).world.equals(entity.world) || (cloakBlockCache.containsKey(entity) && !state.equals(cloakBlockCache.get(entity))))
				{
					EntityFallingBlock blockEntity = new EntityFallingBlock(entity.world, entity.posX, entity.posY, entity.posZ, state);

					blockEntity.isDead = false;
					blockEntity.onGround = false;
					blockEntity.isAirBorne = true;
					blockEntity.fallTime = 0;
					blockEntity.fallDistance = 0;

					cloakedCache.put(entity, blockEntity);
					cloakBlockCache.put(entity, state);
				}
			}

			if(cloakedCache.containsKey(entity))
			{
				Entity cloak = cloakedCache.get(entity);
				cloak.setPosition(entity.posX, entity.posY, entity.posZ);
				cloak.motionX = entity.motionX;
				cloak.motionY = entity.motionY;
				cloak.motionZ = entity.motionZ;
				cloak.rotationPitch = entity.rotationPitch;
				cloak.rotationYaw = entity.rotationYaw;

				cloak.lastTickPosX = entity.lastTickPosX;
				cloak.lastTickPosY = entity.lastTickPosY;
				cloak.lastTickPosZ = entity.lastTickPosZ;
				cloak.ticksExisted = entity.ticksExisted;

				ObfuscationReflectionHelper.setPrivateValue(Entity.class, cloak, ObfuscationReflectionHelper.getPrivateValue(Entity.class, entity, "field_184239_as"), "field_184239_as");

				if(cloak instanceof EntityLivingBase)
				{
					((EntityLivingBase) cloak).rotationYawHead = entity.rotationYawHead;
					((EntityLivingBase) cloak).swingProgress = entity.swingProgress;
					((EntityLivingBase) cloak).swingProgressInt = entity.swingProgressInt;
					((EntityLivingBase) cloak).swingingHand = entity.swingingHand;
					((EntityLivingBase) cloak).limbSwing = entity.limbSwing;
					((EntityLivingBase) cloak).limbSwingAmount = entity.limbSwingAmount;
					((EntityLivingBase) cloak).prevLimbSwingAmount = entity.prevLimbSwingAmount;
					((EntityLivingBase) cloak).renderYawOffset = entity.renderYawOffset;
					((EntityLivingBase) cloak).prevRenderYawOffset = entity.prevRenderYawOffset;
				}

				Minecraft.getMinecraft().getRenderManager().renderEntityStatic(cloak, Minecraft.getMinecraft().getRenderPartialTicks(), false);

				cloak.prevPosX = cloak.posX;
				cloak.prevPosY = cloak.posY;
				cloak.prevPosZ = cloak.posZ;
				cloak.prevRotationPitch = cloak.rotationPitch;
				cloak.prevRotationYaw = cloak.rotationYaw;

				if(cloak instanceof EntityLivingBase)
				{
					((EntityLivingBase) cloak).prevRotationYawHead = ((EntityLivingBase) cloak).rotationYawHead;
				}
			}
		}

		return true;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(event.phase != TickEvent.Phase.START || player == null)
			return;

		if(player.isSneaking() && player.motionX == 0 && player.motionZ == 0 &&
				player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).isCloaked() && player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).getCloakData().hasKey("Block"))
			player.setPosition(Math.floor(player.posX) + .5f, player.posY, Math.floor(player.posZ)+0.5f);
	}

	@SubscribeEvent
	public static void canTargetPlayer(PlayerEvent.Visibility event)
	{
		NBTTagCompound cloakData = event.getEntityPlayer().getCapability(MSUCapabilities.BADGE_EFFECTS, null).getCloakData();

		if(cloakData != null && (cloakData.hasKey("Block") || cloakData.hasKey("Entity")))
			event.modifyVisibility(0);

	}

	@Override
	public List<String> getTags()
	{
		List<String> list = super.getTags();
		list.add("@VOID@");

		return list;
	}

	@Override
	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		Title title = MinestuckPlayerData.getTitle(IdentifierHandler.encode(player));
		return title != null && (title.getHeroAspect() == EnumAspect.VOID || title.getHeroAspect() == EnumAspect.MIND);
	}
}
