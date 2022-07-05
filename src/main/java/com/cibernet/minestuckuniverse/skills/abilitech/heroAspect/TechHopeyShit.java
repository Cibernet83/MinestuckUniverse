package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TechHopeyShit extends TechHeroAspect
{
	private static final String[] STATUS_OPTIONS = new String[] {"tallyHo", "gadzooks", "boyHowdy", "holyToledo", "landSakesAlive", "helloNurse", "byGum", "ayChihuahua", "bobUncle", "sockItToMe", "shiverMeTimbers", "winOneForTheGipper", "jumpyJehosaPhat", "shuckyDarn", "fiddleFaddle", ""};

	public TechHopeyShit(String name)
	{
		super(name, EnumAspect.HOPE, 10000, EnumTechType.OFFENSE);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
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


		float range = Math.min(time/30f, 16);

		DamageSource damage = new EntityDamageSource("hopefulOutburst", player).setDamageBypassesArmor();
		for(Entity target : player.world.getEntitiesWithinAABB(Entity.class, player.getEntityBoundingBox().grow(range)))
		{
			if(target != player && target.getDistance(player) < range)
			{
				if(target instanceof EntityPlayerMP && doTitle)
				{
					SPacketTitle spackettitle1 = new SPacketTitle(SPacketTitle.Type.TITLE, title);
					((EntityPlayerMP)target).connection.sendPacket(spackettitle1);
				}

				if(time % 5 == 0)
				{
					target.hurtResistantTime = 0;
					target.attackEntityFrom(damage, 1);
				}

				if(target instanceof IProjectile)
				{
					target.motionX *= 0.3f;
					target.motionY *= 0.3f;
					target.motionZ *= 0.3f;
				}
			}
		}

		if(!player.isCreative() && time % Math.max(5, 20-time/20f) == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

		if(player.getName().equals("aridThought"))
			badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, 10, 0x534B60, 0xF3296F, 0x96DFF3);
		else badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.BURST, EnumAspect.HOPE, 10);

		return true;
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
}
