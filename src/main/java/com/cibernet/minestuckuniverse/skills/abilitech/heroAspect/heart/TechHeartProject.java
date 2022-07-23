package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.heart;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates.KeyState;
import com.cibernet.minestuckuniverse.entity.EntityHeartDecoy;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.Teleport;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TechHeartProject extends TechHeroAspect
{
	public TechHeartProject(String name, long cost) {
		super(name, EnumAspect.HEART, cost, EnumTechType.UTILITY);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state != KeyState.HELD || time > 60)
		{
			if(badgeEffects.getTether(techSlot) != null)
				if(badgeEffects.getTether(techSlot) instanceof EntityHeartDecoy)
					((EntityHeartDecoy) badgeEffects.getTether(techSlot)).returnToSender(null, 0);
				else
					badgeEffects.setTether(null, techSlot);
			return false;
		}
		
		if(!player.isCreative() && player.getFoodStats().needFood())
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}
		
		if (time < 60)
		{
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HEART, 2);
			return true;
		}
		
		if (!player.isCreative())
			player.getFoodStats().setFoodLevel(0);
		
		EntityHeartDecoy decoy = new EntityHeartDecoy(world.getMinecraftServer().getWorld(world.provider.getDimension()), (EntityPlayerMP) player);

		badgeEffects.setTether(decoy, techSlot);
		badgeEffects.setProjectionTime((int) player.world.getTotalWorldTime());
		
		player.setGameType(GameType.SPECTATOR);
		
		world.spawnEntity(decoy);
		
		return true;
	}	
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return !player.getFoodStats().needFood() && super.isUsableExternally(world, player);
	}
	
	@SubscribeEvent
	public static void update(LivingEvent.LivingUpdateEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			IBadgeEffects BE = event.getEntityLiving().getCapability(MSUCapabilities.BADGE_EFFECTS, null);
			for(int i = 0; i < SkillKeyStates.Key.values().length; i++)
				if(BE.getTether(i) instanceof EntityHeartDecoy && (((EntityPlayerMP) event.getEntityLiving()).interactionManager.getGameType() != GameType.SPECTATOR || Math.abs(event.getEntityLiving().world.getTotalWorldTime() - BE.getProjectionTime()) >= 1200))
					((EntityHeartDecoy) BE.getTether(i)).returnToSender(null, 0);
		}
	}
}
