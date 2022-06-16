package com.cibernet.minestuckuniverse.potions;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MSUPotions
{

    public static final Potion CREATIVE_SHOCK = new PotionBuildInhibit( 0x993030, "disableBuilding");
    public static final Potion EARTHBOUND = new PotionFlight(true, 0xFFCD70, "disableFlight");
    public static final Potion SKYHBOUND = new PotionFlight(false, 0x70FFFF, "flight").setBeneficial();

    public static final Potion GOD_TIER_COMEBACK = new PotionComeback(false, 0x00FF00, "godtierComeback").setBeneficial();
    public static final Potion GOD_TIER_LOCK = new MSUPotionBase(true, 0x808080, "godtierLock");
    public static final Potion TIME_STOP = new PotionTimeStop(true, 0xFF2106, "timeStop");
    public static final Potion MIND_CONFUSION = new PotionConfusion(true, 0x3DA35A, "mindConfusion");
    public static final Potion MIND_FORTITUDE = new PotionCounter(false, 458697, "mindFortitude", MIND_CONFUSION, MobEffects.BLINDNESS, MobEffects.NAUSEA);
    public static final Potion DECAY = new PotionDecay(true, 0x204121, "decay");
    public static final Potion DECAYPROOF = new PotionCounter(false, 0xFEDA82, "decayproof", DECAY, MobEffects.WITHER, MobEffects.POISON);
    public static final Potion VOID_CONCEAL = new PotionConceal(false, 9062, "voidConceal");
    public static final Potion RAGE_BERSERK = new PotionBerserk(false, 0x442769, "rageBerserk");
    public static final Potion BLEEDING = new PotionBleeding(true, 0xB71015, "bleeding");

    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Potion> event)
    {
        IForgeRegistry<Potion> registry = event.getRegistry();

        registry.register(CREATIVE_SHOCK.setRegistryName("creative_shock"));
        registry.register(EARTHBOUND.setRegistryName("earthbound"));
        registry.register(SKYHBOUND.setRegistryName("skybound"));

        registry.register(GOD_TIER_COMEBACK.setRegistryName("god_tier_comeback"));
        registry.register(GOD_TIER_LOCK.setRegistryName("god_tier_lock"));
        registry.register(TIME_STOP.setRegistryName("time_stop"));
        registry.register(MIND_FORTITUDE.setRegistryName("mental_fortitude"));
        registry.register(MIND_CONFUSION.setRegistryName("mental_crash"));
        registry.register(DECAY.setRegistryName("decay"));
        registry.register(DECAYPROOF.setRegistryName("decayproof"));
        registry.register(VOID_CONCEAL.setRegistryName("true_concealment"));
        registry.register(RAGE_BERSERK.setRegistryName("berserk"));
        registry.register(BLEEDING.setRegistryName("bleeding"));
    }
}
