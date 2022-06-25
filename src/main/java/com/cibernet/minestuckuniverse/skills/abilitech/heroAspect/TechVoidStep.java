package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.potions.MSUPotions;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TechVoidStep extends TechHeroAspect
{
    public TechVoidStep(String name)
    {
        super(name, EnumAspect.VOID, EnumTechType.OFFENSE, EnumAspect.BREATH);
    }

    @Override
    public void onPassiveToggle(World world, EntityPlayer player, boolean active)
    {
        super.onEquipped(world, player);
        player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).setVoidstepping(active);
    }

    @Override
    public void onEquipped(World world, EntityPlayer player) {
        onPassiveToggle(world, player, player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isTechPassiveEnabled(this));
    }

    @Override
    public void onUnequipped(World world, EntityPlayer player) {
        onPassiveToggle(world, player, false);
    }

    @Override
    public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
    {
        if (state == SkillKeyStates.KeyState.PRESS)
        {
            badgeEffects.setVoidstepping(!badgeEffects.isVoidstepping());
            player.sendStatusMessage(new TextComponentTranslation(badgeEffects.isVoidstepping() ? "status.badgeEnabled" : "status.badgeDisabled", getDisplayComponent()), true);
        }

        if(!(badgeEffects.isVoidstepping() && (player.capabilities.isFlying || badgeEffects.isDoingWimdyThing())))
            return false;

        if(!player.isCreative() && player.ticksExisted % 40 == 1)
        {
            player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-1);
            if(player.getFoodStats().getFoodLevel() < 1)
            {
                player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
                return false;
            }
        }

        if(!player.isPotionActive(MSUPotions.VOID_CONCEAL))
            MSUChannelHandler.sendToTrackingAndSelf(MSUPacket.makePacket(MSUPacket.Type.SEND_PARTICLE, MSUParticles.ParticleType.AURA, (player.ticksExisted % 2) == 0 ? 0x104EA2 : 0x001856, 1, player), player);

        return true;
    }

    @Override
    public boolean canUse(World world, EntityPlayer player) {
        return player.getFoodStats().getFoodLevel() > 0 && super.canUse(world, player);
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (!(event.getEntity() instanceof EntityPlayer) || event.getEntity().world.isRemote)
            return;

        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        player.noClip = player.noClip || (player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).isVoidstepping() &&
                                                  (player.capabilities.isFlying || player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).isDoingWimdyThing()));
    }

    @SubscribeEvent
    public static void onPlayerCollision(GetCollisionBoxesEvent event)
    {
        if (!(event.getEntity() instanceof EntityPlayer))
            return;

        if(event.getEntity().getCapability(MSUCapabilities.BADGE_EFFECTS, null).isVoidstepping()
                   && (((EntityPlayer) event.getEntity()).capabilities.isFlying || event.getEntity().getCapability(MSUCapabilities.BADGE_EFFECTS, null).isDoingWimdyThing()))
            event.getCollisionBoxesList().clear();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SideOnly(Side.CLIENT)
    public static void onPlayerPushOutOfBlocks(PlayerSPPushOutOfBlocksEvent event)
    {
        if(event.getEntity() != null && event.getEntityPlayer().isUser() && event.getEntity().getCapability(MSUCapabilities.BADGE_EFFECTS, null).isVoidstepping())
            event.setCanceled(true);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void renderBlockOverlay(RenderBlockOverlayEvent event)
    {
        if(Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.getCapability(MSUCapabilities.BADGE_EFFECTS, null).isVoidstepping())
            event.setCanceled(true);
    }
}
