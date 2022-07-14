package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.heart;

import javax.annotation.Nullable;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates.Key;
import com.cibernet.minestuckuniverse.damage.CritDamageSource;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TechHeartBond extends TechHeroAspect
{
	
	public TechHeartBond(String name, long cost)
	{
		super(name, EnumAspect.HEART, cost, EnumTechType.OFFENSE, EnumTechType.DEFENSE);
	}
	//lower food, make particles, set *dead* 
	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		
		if(state == SkillKeyStates.KeyState.NONE)
			return false;
		
		boolean released = false;
		if(state == SkillKeyStates.KeyState.RELEASED)
			released = true;
		
		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}
		
		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HEART, 1);
		
		EntityLivingBase target = badgeEffects.getTether(techSlot) instanceof EntityLivingBase ? (EntityLivingBase) badgeEffects.getTether(techSlot) : null;

		if(target != null && MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(player, target, this, techSlot, null)))
			target = null;

		IBadgeEffects targetBadgeEffects = target != null ? target.getCapability(MSUCapabilities.BADGE_EFFECTS, null) : null;
		
		if(released || player.getHealth() <= 0 || (target != null && target.getHealth() <= 0))
		{
			if(targetBadgeEffects != null)
				targetBadgeEffects.setSoulLinkedBy(null);
			badgeEffects.clearTether(techSlot);
			badgeEffects.setSoulLinkInt(0);
			return false;
		}
		
		if(!released && target != null && targetBadgeEffects.getSoulLinkedBy() == player)
		{
			double linkedPercent = (player.getHealth()/player.getMaxHealth() + target.getHealth()/target.getMaxHealth())/2;
			//if(linkedPercent <= .005 || (linkedPercent*player.getMaxHealth() < 1 && linkedPercent*target.getMaxHealth() < 1))
			
			System.out.println("Linkedpercent: " + linkedPercent);
			if((player.getMaxHealth() * linkedPercent) != player.getHealth())
				player.setHealth((float) (player.getMaxHealth() * linkedPercent));
			if((target.getMaxHealth() * linkedPercent) != target.getHealth())
				target.setHealth((float) (target.getMaxHealth() * linkedPercent));
			
			
			if (!player.isCreative() && time % 20 == 0)
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);
			
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.HEART, 3);
			targetBadgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.HEART, 3);
		}
		
		
		
		EntityLivingBase targett = MSUUtils.getTargetEntity(player);
		targetBadgeEffects = targett == null ? null : targett.getCapability(MSUCapabilities.BADGE_EFFECTS, null);		
		if(target == null && targett != null && targetBadgeEffects.getSoulLinkedBy() == null && !hasAnySoulLink(targett) && !hasAnySoulLink(player))
		{
			if(badgeEffects.getSoulLinkInt() < 20)
				badgeEffects.setSoulLinkInt(badgeEffects.getSoulLinkInt() + 1);
			else
			{
				badgeEffects.setTether(targett, techSlot);
				targetBadgeEffects.setSoulLinkedBy(player);
				
				badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, EnumAspect.HEART, 8);
			}
		}
		else if(target == null)
			badgeEffects.setSoulLinkInt(0);
		
		return true;	
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 1 && super.isUsableExternally(world, player);
	}
	
	public static class HeartDamageSource extends CritDamageSource
	{
		protected Entity damageSourceEntity;

		public HeartDamageSource(Entity damageSourceEntityIn)
		{
			super("spiritBond");
			setDamageBypassesArmor();
			this.damageSourceEntity = damageSourceEntityIn;
			setGodproof();
		}

		public Entity getTrueSource()
		{
			return this.damageSourceEntity;
		}

		public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
		{
			String s = "death.attack." + this.damageType;
			String s1 = s + ".external";
			return entityLivingBaseIn != damageSourceEntity && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName()}) : new TextComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName()});
		}

		/**
		 * Gets the location from which the damage originates.
		 */
		@Nullable
		public Vec3d getDamageLocation()
		{
			return new Vec3d(this.damageSourceEntity.posX, this.damageSourceEntity.posY, this.damageSourceEntity.posZ);
		}
	}
	
	private static boolean hasAnySoulLink(EntityLivingBase entity)
	{
		int[] LnT = hasSoulLink(entity);
		return LnT[0] != 0 || LnT[1] != 0;
	}
	
	//2 ints, first is whether its being linked and the other is if it is linking
	private static int[] hasSoulLink(EntityLivingBase entity)
	{
		int[] linkedNTether = new int[2];
		IBadgeEffects BE = entity.getCapability(MSUCapabilities.BADGE_EFFECTS, null);
		if(BE != null && BE.getSoulLinkedBy() != null)
			linkedNTether[0] = 1;
		
		if(!(entity instanceof EntityPlayer))
			return linkedNTether;
		IGodTierData data = ((EntityPlayer)entity).getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		
		if(data != null)
			for(Key key : Key.values())
			{
				Abilitech abilitech = data.getTech(key.ordinal());
				if(abilitech == null || linkedNTether[1] > 0 || !(abilitech instanceof TechHeartBond) || BE.getTether(key.ordinal()) == null) 
					continue;
				linkedNTether[1] = key.ordinal() + 1;
			}
		return linkedNTether;
	}

	/*
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLivingDeathRender(RenderLivingEvent.Pre<EntityLivingBase> event)
	{
		int[] LnT = hasSoulLink(event.getEntity());
		if(LnT[0] != 0 || LnT[1] != 0)
		{
			event.getEntity().deathTime = 0;
			event.getEntity().isDead = false;
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if (event.getEntity().world.isRemote)
			return;
		
		boolean soulKill = event.getSource() instanceof HeartDamageSource;
		
		IBadgeEffects BE = event.getEntityLiving().getCapability(MSUCapabilities.BADGE_EFFECTS, null);
		int[] LnT = hasSoulLink(event.getEntityLiving());
		if(LnT[0] != 0)
		{
			if(soulKill)
				BE.setSoulLinkedBy(null);
			else
				event.setCanceled(true);
		}
		if(LnT[1] > 0 && LnT[1] <= Key.values().length)
		{
			if(soulKill)
				BE.setTether(null, LnT[1]-1);
			else
				event.setCanceled(true);
		}
	}
	*/
}
