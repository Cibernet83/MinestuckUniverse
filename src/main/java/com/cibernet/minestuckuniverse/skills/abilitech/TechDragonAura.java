package com.cibernet.minestuckuniverse.skills.abilitech;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.skills.TechBoondollarCost;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TechDragonAura extends TechBoondollarCost
{
	public TechDragonAura(String name, long cost)
	{
		super(name, cost, EnumTechType.DEFENSE);
		requiredStacks.add(new ItemStack(MinestuckUniverseItems.dragonGel));
	}

	@Override
	public void onUnequipped(World world, EntityPlayer player, int techSlot) {
		super.onUnequipped(world, player, techSlot);
		player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setHasDragonAura(false);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
		{
			badgeEffects.setHasDragonAura(false);
			return false;
		}

		if(player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			badgeEffects.setHasDragonAura(false);
			return false;
		}

		badgeEffects.setHasDragonAura(true);

		if(time % 10 == 0)
			player.heal(1);
		if(time % 20 == 0)
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);

		badgeEffects.oneshotPowerParticles(MSUParticles.ParticleType.AURA, 10, Integer.parseInt(String. format("%02X%02X%02X",
				(int) (MathHelper.nextFloat(player.world.rand, 0.7176471F, 0.8745098F)*255), 0,
				(int) (MathHelper.nextFloat(player.world.rand, 0.8235294F, 0.9764706F)*255)), 16));

		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 1 && super.isUsableExternally(world, player);
	}

	@SubscribeEvent
	public static void onLivingAttack(LivingAttackEvent event)
	{
		if(event.getEntityLiving().world.isRemote)
			return;

		EntityLivingBase player = event.getEntityLiving();

		if(player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).hasDragonAura())
		{

			((WorldServer)player.world).spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, player.posX, player.posY+0.5, player.posZ, 1, 0, 0, 0, 0d);
			for(EntityLivingBase target : player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(8, 3, 8), (e) -> e != player))
				target.attackEntityFrom(DamageSource.causeExplosionDamage(player), 10);

			player.addPotionEffect(new PotionEffect(MSUPotions.GOD_TIER_LOCK, 600, 3, false, false));
			player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setHasDragonAura(false);
		}
	}
}
