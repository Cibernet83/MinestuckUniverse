package com.cibernet.minestuckuniverse.events.handlers;

import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.damage.IGodTierDamage;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class KarmaEventHandler
{
	private static ArrayList<EntityPlayer> criticalKills = new ArrayList<>();
	private static final int KARMA_CAP = 200;
	
	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			
			IdentifierHandler.PlayerIdentifier targetIdentifier = IdentifierHandler.encode((EntityPlayer) event.getEntityLiving());
			SburbConnection tC = SkaianetHandler.getMainConnection(targetIdentifier, true);
			
			if(tC == null || !tC.enteredGame())
				return;
			
			IGodTierData targetData = event.getEntityLiving().getCapability(MSUCapabilities.GOD_TIER_DATA, null);
			
			int totalKarma = targetData.getTotalKarma();
			boolean pvpKill = event.getSource().getTrueSource() instanceof EntityPlayer;
			
			if(pvpKill)
			{
				IdentifierHandler.PlayerIdentifier sourceIdentifier = IdentifierHandler.encode((EntityPlayer) event.getSource().getTrueSource());
				SburbConnection sC = SkaianetHandler.getMainConnection(sourceIdentifier, true);
				IGodTierData sourceData = event.getSource().getTrueSource().getCapability(MSUCapabilities.GOD_TIER_DATA, null);
				if(sC != null && sC.enteredGame() && sourceData.isGodTier())
				{
					if(totalKarma >= -1 && totalKarma < 5)
						totalKarma = 5;
					else if(totalKarma > -5 && totalKarma < -1)
						totalKarma = -5;
					
					if(criticalKills.remove(event.getEntityLiving()))
					{
						totalKarma = (int) targetData.getTempKarma();
						sourceData.setStaticKarma(Math.max(Math.min(sourceData.getStaticKarma() - totalKarma * 2, KARMA_CAP), -KARMA_CAP));
					}
					else
						sourceData.setStaticKarma(Math.max(Math.min(sourceData.getStaticKarma() - totalKarma, KARMA_CAP), -KARMA_CAP));

					sourceData.update();
				}
			}

			if(targetData.isGodTier() && !(event.getSource() instanceof IGodTierDamage && ((IGodTierDamage) event.getSource()).isGodproof()))
			{
				int minKarma = targetData.isBadgeActive(MSUSkills.KARMA) ? 40 : 20;
				if(totalKarma >= minKarma || totalKarma <= -minKarma)
				{
					if(totalKarma >= minKarma)
						event.getEntityLiving().world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentTranslation("status.heroicDeath", event.getEntityLiving().getDisplayName()).setStyle(new Style().setColor(TextFormatting.GOLD)));
					else
						event.getEntityLiving().world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentTranslation("status.justDeath", event.getEntityLiving().getDisplayName()).setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
					targetData.setStaticKarma(0);
					targetData.setTempKarma(0);
				} else
				{
					EntityPlayer player = (EntityPlayer) event.getEntityLiving();
					boolean hasRevenantBadge = targetData.isBadgeActive(MSUSkills.REVENANTS_RETALIATION);

					player.addPotionEffect(new PotionEffect(MSUPotions.GOD_TIER_COMEBACK, hasRevenantBadge ? 500 : 200, targetData.isBadgeActive(MSUSkills.EFFECT_BUFF) ? 2 : 0));

					((WorldServer)player.world).spawnParticle(EnumParticleTypes.TOTEM, player.posX, player.posY+0.25, player.posZ, 30, 1, 0, 0, hasRevenantBadge ? 0.8 : 0.5);
					player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_TOTEM_USE, player.getSoundCategory(), 1.0F, 1.0F);

					if(hasRevenantBadge)
					{
						((WorldServer)player.world).spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, player.posX, player.posY+0.5, player.posZ, 1, 0, 0, 0, 0d);
						for(EntityLivingBase target : player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(8, 3, 8), (e) -> e != player && (e instanceof IMob || e instanceof EntityPlayer)))
							target.attackEntityFrom(DamageSource.causeExplosionDamage(player).setDamageBypassesArmor(), target == event.getSource().getTrueSource() ? 30 : 15);
					}

					player.setHealth(hasRevenantBadge ? 30 : 20);
					event.setCanceled(true);
					
					if(!pvpKill && MinestuckPlayerData.getData((EntityPlayer) event.getEntityLiving()).echeladder.getRung() >= 49)
						targetData.setTempKarma(Math.max(targetData.getTempKarma() - 15, -KARMA_CAP));
				}

				targetData.update();
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerDamage(LivingDamageEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer && event.getSource().getTrueSource() instanceof EntityPlayer)
		{
			IdentifierHandler.PlayerIdentifier sourceIdentifier = IdentifierHandler.encode((EntityPlayer) event.getSource().getTrueSource());
			SburbConnection sC = SkaianetHandler.getMainConnection(sourceIdentifier, true);
			
			if(sC != null && sC.enteredGame())
			{
				int targetKarma = event.getEntityLiving().getCapability(MSUCapabilities.GOD_TIER_DATA, null).getTotalKarma();
				IGodTierData sourceData = event.getSource().getTrueSource().getCapability(MSUCapabilities.GOD_TIER_DATA, null);
				
				if(targetKarma >= 0)
					sourceData.setTempKarma(Math.max(Math.min(sourceData.getTempKarma() -Math.max( 1, targetKarma / 5), KARMA_CAP), -KARMA_CAP));
				else
					sourceData.setTempKarma(Math.max(Math.min(sourceData.getTempKarma() -Math.min(-1, targetKarma / 5), KARMA_CAP), -KARMA_CAP));

				sourceData.update();

				if((event.getSource() instanceof IGodTierDamage && ((IGodTierDamage)event.getSource()).isCrit()) || (event.getEntityLiving().getHealth() - event.getAmount() <= 0 && event.getEntityLiving().getHealth() >= 10))
					criticalKills.add((EntityPlayer) event.getEntityLiving());
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.phase != TickEvent.Phase.START)
			return;
		IGodTierData data = event.player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		if(data.getTempKarma() != 0)
		{
			data.setTempKarma(data.getTempKarma() - (1f / 1200f * (data.isBadgeActive(MSUSkills.KARMA) ? 2 : 1) * (data.isTechPassiveEnabled(MSUSkills.MIND_GODHOODS_JUSTICE) ? 2 : 1)) * Math.signum(data.getTempKarma()));
			if(data.getTempKarma() > -0.001f && data.getTempKarma() < 0.001f)
				data.setTempKarma(0);
		}
	}
}
