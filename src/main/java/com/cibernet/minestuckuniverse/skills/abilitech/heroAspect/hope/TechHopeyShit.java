package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.hope;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.damage.CritDamageSource;
import com.cibernet.minestuckuniverse.events.AbilitechTargetedEvent;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.breath.TechBreathKnockback;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TechHopeyShit extends TechHeroAspect
{
	private static final String[] STATUS_OPTIONS = new String[] {"tallyHo", "gadzooks", "boyHowdy", "holyToledo", "landSakesAlive", "helloNurse", "byGum", "ayChihuahua", "bobUncle", "sockItToMe", "shiverMeTimbers", "winOneForTheGipper", "jumpinJehosaPhat", "shuckyDarn", "fiddleFaddle"};

	public TechHopeyShit(String name, long cost)
	{
		super(name, EnumAspect.HOPE, cost, EnumTechType.OFFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(badgeEffects.isHoping() == (state != SkillKeyStates.KeyState.NONE))
			badgeEffects.setHoping(state != SkillKeyStates.KeyState.NONE);

		if(state == SkillKeyStates.KeyState.NONE)
			return false;

		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		boolean doTitle = world.rand.nextFloat() < 0.005f;
		ITextComponent title = new TextComponentTranslation("status.hopey." + STATUS_OPTIONS[world.rand.nextInt(STATUS_OPTIONS.length)]);

		if(player instanceof EntityPlayerMP && doTitle)
		{
			SPacketTitle spackettitle1 = new SPacketTitle(SPacketTitle.Type.TITLE, title);
			((EntityPlayerMP)player).connection.sendPacket(spackettitle1);
		}

		if(time % 5 == 0)
		{
			float range = Math.min(time/30f + 2, 12);
			DamageSource damage = new HopeDamageSource(player);
			if(doTitle)
				for(EntityPlayerMP target : player.world.getEntitiesWithinAABB(EntityPlayerMP.class, player.getEntityBoundingBox().grow(range*2)))
				{
					SPacketTitle spackettitle1 = new SPacketTitle(SPacketTitle.Type.TITLE, title);
					target.connection.sendPacket(spackettitle1);
				}

			for(EntityLivingBase target : player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(range)))
			{
				if(MinecraftForge.EVENT_BUS.post(new AbilitechTargetedEvent(world, target, this, techSlot, false)))
					continue;
				
				if(target != player && target.getDistance(player) < range)
				{
					//if(!(target instanceof EntityItem) && time % 5 == 0)
					{
						target.hurtResistantTime = 0;
						target.attackEntityFrom(damage, 1);
					}

					float strength = 1;
					Vec3d vec = new Vec3d(player.posX-target.posX, player.posY-target.posY, player.posZ-target.posZ).normalize();

					target.velocityChanged = true;
					strength *= 0.3f;
					target.isAirBorne = true;
					float f = MathHelper.sqrt(vec.x * vec.x + vec.z * vec.z);
					target.motionX /= 2.0D;
					target.motionZ /= 2.0D;
					target.motionX -= vec.x / (double)f * (double)strength;
					target.motionZ -= vec.z / (double)f * (double)strength;

					if (target.onGround)
					{
						target.motionY /= 2.0D;
						target.motionY += (double)strength;
						if (target.motionY > 0.4000000059604645D)
							target.motionY = 0.4000000059604645D;
					}

				}
			}
		}



		if(!player.isCreative() && time % Math.max(5, 20-time/20f) == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

		if(player.getName().equals("aridThought"))
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, 10, 0x534B60, 0xF3296F, 0x6CF4FC);
		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.HOPE, 10);

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 1 && super.isUsableExternally(world, player);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onMovementInput(InputUpdateEvent event)
	{
		if(event.getEntityPlayer() != null && event.getEntityPlayer().getCapability(MSUCapabilities.BADGE_EFFECTS, null).isHoping())
		{
			event.getMovementInput().moveForward *= 0.1f;
			event.getMovementInput().moveStrafe *= 0.1f;

			event.getEntityPlayer().motionY += 0.5f;
		}
	}

	@Override
	public boolean canAppearOnList(World world, EntityPlayer player) {
		return super.canAppearOnList(world, player) && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier();
	}

	public static class HopeDamageSource extends CritDamageSource
	{
		protected Entity damageSourceEntity;

		public HopeDamageSource(Entity damageSourceEntityIn)
		{
			super("hopefulOutburst");
			this.damageSourceEntity = damageSourceEntityIn;
			setDamageBypassesArmor();
		}

		public Entity getTrueSource()
		{
			return this.damageSourceEntity;
		}

		public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
		{
			return  new TextComponentTranslation("death.attack." + this.damageType, entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName());
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
}
