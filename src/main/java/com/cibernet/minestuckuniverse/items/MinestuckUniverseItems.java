package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.BlockCustomTransportalizer;
import com.cibernet.minestuckuniverse.client.models.armor.*;
import com.cibernet.minestuckuniverse.client.render.RenderThrowable;
import com.cibernet.minestuckuniverse.enchantments.MSUEnchantments;
import com.cibernet.minestuckuniverse.items.armor.*;
import com.cibernet.minestuckuniverse.items.properties.*;
import com.cibernet.minestuckuniverse.items.properties.beams.PropertyBeamDeathMessage;
import com.cibernet.minestuckuniverse.items.properties.beams.PropertyMagicBeam;
import com.cibernet.minestuckuniverse.items.properties.beams.PropertyPotionBeam;
import com.cibernet.minestuckuniverse.items.properties.beams.PropertyRainbowBeam;
import com.cibernet.minestuckuniverse.items.properties.bowkind.*;
import com.cibernet.minestuckuniverse.items.properties.clawkind.PropertyActionBuff;
import com.cibernet.minestuckuniverse.items.properties.shieldkind.*;
import com.cibernet.minestuckuniverse.items.properties.throwkind.*;
import com.cibernet.minestuckuniverse.items.properties.PropertyRocketBoost;
import com.cibernet.minestuckuniverse.items.weapons.*;
import com.cibernet.minestuckuniverse.util.BlockMetaPair;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.cibernet.splatcraft.items.ItemFilter;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.block.BlockTransportalizer;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.item.block.ItemTransportalizer;
import com.mraof.minestuck.util.MinestuckSoundHandler;
import com.mraof.minestuck.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.botania.common.block.ModBlocks;

import java.util.ArrayList;

public class MinestuckUniverseItems
{
    private static final PropertySoundOnHit.Value PITCH_NOTE = ((stack, target, player) -> (-player.rotationPitch + 90) / 90f);
    public static ArrayList<Block> itemBlocks = new ArrayList<>();

    //Armor Materials
    public static ItemArmor.ArmorMaterial materialDiverHelmet = EnumHelper.addArmorMaterial("DIVER_HELMET", MinestuckUniverse.MODID+":diver_helmet", 120, new int[] {0, 0, 0, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static ItemArmor.ArmorMaterial materialSpikedHelmet = EnumHelper.addArmorMaterial("SPIKED_HELMET", MinestuckUniverse.MODID+":spiked_diver_helmet", 230, new int[] {0, 0, 0, 6}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static ItemArmor.ArmorMaterial materialMetal = EnumHelper.addArmorMaterial("METAL", MinestuckUniverse.MODID+":metal", 200, new int[] {2, 0, 0, 4}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static ItemArmor.ArmorMaterial materialRubber = EnumHelper.addArmorMaterial("RUBBER", MinestuckUniverse.MODID+":rubber", 240, new int[] {1, 2, 3, 1}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static ItemArmor.ArmorMaterial materialSunShoes = EnumHelper.addArmorMaterial("SOLAR", MinestuckUniverse.MODID+":solar", 240, new int[] {3, 0, 0, 3}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F);
    public static ItemArmor.ArmorMaterial materialWindWalkers = EnumHelper.addArmorMaterial("WIND_WALKERS", MinestuckUniverse.MODID+":sun_shoes", 240, new int[] {3, 0, 0, 3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static ItemArmor.ArmorMaterial materialCobalt = EnumHelper.addArmorMaterial("COBALT", MinestuckUniverse.MODID+":cobalt", 640, new int[] {1, 0, 0, 2}, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.1F);
    public static ItemArmor.ArmorMaterial materialCloth = EnumHelper.addArmorMaterial("CLOTH", MinestuckUniverse.MODID+":cloth", -1, new int[] {0, 0, 0, 0}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);

    //Tool Classes
    public static MSUToolClass toolSword = new MSUToolClass("sword", Material.WEB).addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolGauntlet = new MSUToolClass("gauntlet", Material.GLASS, Material.ICE, Material.PACKED_ICE).addEnchantments(Enchantments.SILK_TOUCH, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, MSUEnchantments.SUPERPUNCH);
    public static MSUToolClass toolNeedles = new MSUToolClass("needle", Material.CLOTH).addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolHammer = new MSUToolClass("pickaxe", "pickaxe").addEnchantments(EnumEnchantmentType.WEAPON, EnumEnchantmentType.DIGGER);
    public static MSUToolClass toolClub = new MSUToolClass("club").addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolClaws = new MSUToolClass("claws", Material.PLANTS, Material.WEB).addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolCane = new MSUToolClass("cane").addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolSickle = new MSUToolClass("sickle", Material.GRASS, Material.PLANTS, Material.LEAVES).addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolSpoon = new MSUToolClass("spoon", Material.GOURD).addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolFork = new MSUToolClass("fork", Material.GRASS).addEnchantments(EnumEnchantmentType.WEAPON);

    public static MSUToolClass toolShovel = new MSUToolClass("shovel", "shovel").addEnchantments(EnumEnchantmentType.DIGGER);
    public static MSUToolClass toolAxe = new MSUToolClass("axe", "axe").addEnchantments(EnumEnchantmentType.WEAPON, EnumEnchantmentType.DIGGER).setDisablesShield();
    public static MSUToolClass toolPickaxe = new MSUToolClass("pickaxe", "pickaxe").addEnchantments(EnumEnchantmentType.DIGGER);

    public static MSUToolClass toolSpork = new MSUToolClass("spork", toolSpoon, toolFork);
    public static MSUToolClass toolHammaxe = new MSUToolClass("hammaxe", toolHammer, toolAxe);


    //Block Swap Property Maps
    public static final BlockMetaPair.Map overgrowthTransforms = new BlockMetaPair.Map();

    //Items
    public static Item spaceSalt = new ItemSpaceSalt();
    public static Item timetable = new ItemTimetable();
    public static Item moonstone = new MSUItemBase("moonstone");
    public static Item moonstoneChisel = new ItemChisel("moonstone", 31);
    public static Item zillystoneShard = new MSUItemBase("zillystone_shard", "zillystoneShard");
    public static Item fluorite = new MSUItemBase("fluorite");
    public static Item battery = new MSUItemBase("battery", "battery");
    public static Item strifeCard = new ItemStrifeCard("strife_card", "strifeCard");
    public static Item dungeonKey = new MSUItemBase("dungeon_key", "dungeonKey");
    public static Item laserPointer = new ItemBeamWeapon(-1, 0, 0, 0.01f, 0, 1, 1, "laser_pointer", "laserPointer").addProperties(new PropertyPotionBeam(new PotionEffect(MobEffects.BLINDNESS, 30, 0, false, false))).setRepairMaterials(new ItemStack(battery)).setCreativeTab(TabMinestuckUniverse.main);
    public static Item whip = new ItemSound("whip", "whip", MSUSoundHandler.whipCrack);
    public static Item sbahjWhip = new ItemSound("whip_sbahj", "whipSbahj", MSUSoundHandler.whipCrock).setSecret();
    public static Item unrealAir = new ItemUnrealAir("unreal_air", "unrealAir");

    public static Item tickingStopwatch = new MSUItemBase("ticking_stopwatch", "tickingStopwatch"){{
        addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "time"), ((stack, worldIn, entityIn) -> ((System.currentTimeMillis() - Minestuck.startTime)/1000f) % 60));
    }}.setMaxStackSize(1);
    public static Item cueBall = new MSUItemBase("cue_ball", "cueBall")
    {
        @Override
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {}

        @Override
        public boolean onEntityItemUpdate(EntityItem entityItem)
        {
            entityItem.setEntityInvulnerable(true);
            entityItem.setGlowing(true);
            entityItem.setNoDespawn();

            if(entityItem.posY < 1)
            {
                entityItem.setPosition(entityItem.getPosition().getX(), 260, entityItem.getPosition().getZ());
                entityItem.motionY = entityItem.world.rand.nextDouble()*1.2;
            }

            return super.onEntityItemUpdate(entityItem);
        }

    }.setSecret().setMaxStackSize(1);

    //Ghost Items
    public static Item returnNode = new ItemGhost("return_node_ghost_item", MinestuckBlocks.returnNode);
    public static Item travelGate = new ItemGhost("travel_gate_ghost_item", MinestuckBlocks.gate);
    public static Item netherPortal = new ItemGhost("nether_portal_ghost_item", Blocks.PORTAL);
    public static Item endPortal = new ItemGhost("end_portal_ghost_item", Blocks.END_PORTAL);
    public static Item endGateway = new ItemGhost("end_gateway_ghost_item", Blocks.END_GATEWAY);
    public static Item sun = new ItemGhost("sun_ghost_item");
    public static Item greenSun = new ItemGhost("green_sun_ghost_item");
    public static Item moon = new ItemGhost("moon_ghost_item");
    public static Item skaia = new ItemGhost("skaia_ghost_item");
    public static Item lightning = new ItemGhost("lightning_ghost_item");

    //Medallions
    public static Item ironMedallion = new MSUItemBase("iron_medallion", "ironMedallion").setMaxStackSize(1);
    public static Item returnMedallion = new ItemWarpMedallion("returnMedallion", "return_medallion", ItemWarpMedallion.EnumTeleportType.RETURN, 80);
    public static Item teleportMedallion = new ItemWarpMedallion("teleportMedallion", "teleport_medallion", ItemWarpMedallion.EnumTeleportType.TRANSPORTALIZER, 80);
    public static Item skaianMedallion = new ItemWarpMedallion("skaianMedallion", "skaian_medallion", ItemWarpMedallion.EnumTeleportType.SKAIA, 80);

    //Weapons

    //Bladekind
    public static Item trueUnbreakableKatana = (new MSUWeaponBase(-1, 24.0D, -2.07D, 20, "true_unbreakable_katana", "unbreakableKatana")).addProperties(new PropertySweep()).setTool(toolSword, 0, 15.0F);
    public static Item bloodKatana = (new MSUWeaponBase(880, 16.0D, -2.07D, 10, "blood_katana", "bloodKatana")).addProperties(new PropertySweep(), new PropertyBloodBound()).setTool(toolSword, 0, 15.0F);
    public static ItemBeamBlade batteryBeamBlade = new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade", "batteryBeamBlade").setTool(toolSword, 0, 15.0F);
    public static ItemBeamBlade[] dyedBeamBlade = new ItemBeamBlade[] {
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_white", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.WHITE),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_orange", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.ORANGE),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_magenta", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.MAGENTA),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_light_blue", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.LIGHT_BLUE),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_yellow", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.YELLOW),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_lime", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.LIME),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_pink", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.PINK),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_gray", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.GRAY),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_silver", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.SILVER),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_cyan", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.CYAN),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_purple", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.PURPLE),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_blue", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.BLUE),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_brown", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.BROWN),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_green", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.GREEN),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_red", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.RED),
            new ItemBeamBlade(385, 8, -2.3, 30, "battery_beam_blade_black", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.BLACK)};
    public static Item lightbringer = new MSUWeaponBase(1375, 20, -2.07, 32, "lightbringer", "lightbringer").setTool(toolSword, 4, 10F).addProperties(new PropertySweep(), new PropertyFire(4, 0.8f, true), new PropertyLuckBasedDamage(0.5f), new PropertyMobTypeDamage(EnumCreatureAttribute.UNDEAD, 3));
    public static Item cybersword = new MSUWeaponBase(1650, 28.8, -2.07, 32, "cybersword", "cybersword").setTool(toolSword, 5, 8F).addProperties(new PropertySweep(), new PropertyShock(15, 8, 0.5f, false), new PropertyLightning(15, 1, true, false), new PropertyLuckBasedDamage(0.1f)).setCreativeTab(null);
    public static Item crystallineRibbitar = new MSUWeaponBase(3300, 16.4, -2.3, 24, "crystalline_ribbitar", "crystallineRibbitar").setTool(toolSword, 4, 6F).addProperties(new PropertySweep());
    public static Item quantumEntangloporter = new MSUWeaponBase(1430, 19.2, -2.3, 9, "quantum_entangloporter", "quantumEntangloporter").setTool(toolSword, 3, 2F).addProperties(new PropertySweep(), new PropertyTeleBlock(Blocks.CACTUS, 20));
    public static Item valorsEdge = new MSUWeaponBase(1650, 26.4, -2.07, 28, "valor_edge", "calamitySword").setTool(toolSword, 4, 4F).addProperties(new PropertySweep(), new PropertyCrowdDamage(0.3f, 3f, 20),
            new PropertyLowHealthBoost(SharedMonsterAttributes.MOVEMENT_SPEED, "Calamity Boost", 1.2, 0.4, 0.4f, 2), new PropertyLowHealthBoost(SharedMonsterAttributes.ATTACK_SPEED, "Calamity Boost", 0.4, 0.2, 0.4f, 2));

    //Gauntletkind
    public static Item fancyGlove = new MSUWeaponBase(200, 1D, 0, 5, "fancy_glove", "fancyGlove").setTool(toolGauntlet, 0, 1);
    public static Item spikedGlove = new MSUWeaponBase(289, 5.7, -0.48, 8, "spiked_glove", "spikedGlove").setTool(toolGauntlet, 1, 1.4F);
    public static Item cobbleBasher = new MSUWeaponBase(360, 7.2, -0.66, 4, "cobble_basher","cobbleBasher").setTool(toolGauntlet, 1, 4F);
    public static Item fluoriteGauntlet = new MSUWeaponBase(1000, 9, -0.54,  8, "fluorite_gauntlet", "fluoriteGauntlet").setTool(toolGauntlet, 4, 8).addProperties(new PropertyRandomDamage());
    public static Item goldenGenesisGauntlet = new MSUWeaponBase(1440, 23.1, -0.48D, 15, "golden_genesis_gauntlet","goldenGenesisGauntlet").setTool(toolGauntlet, 2, 3F);
    public static Item pogoFist = new MSUWeaponBase(600, 7.2D, -0.6, 8, "pogo_fist", "pogoFist").setTool(toolGauntlet, 2, 4F).addProperties(new PropertyPogo(0.55D));
    public static Item rocketFist = new MSUWeaponBase(360, 7.2D, -0.54, 6, "rocket_powered_fist", "rocketFist").setTool(toolGauntlet, 2, 4F).addProperties(new PropertyRocketDash(10, 15, 0.4f, 3));
    public static Item jawbreaker = new MSUWeaponBase(660, 6.5, -0.6D, 6, "jawbreaker", "jawbreaker").setTool(toolGauntlet, 3, 1.6F).addProperties(new PropertyCandyWeapon());
    public static Item eldrichGauntlet = new MSUWeaponBase(1200, 9.6, -0.72, 6, "eldritch_gauntlet", "eldrichGauntlet").setTool(toolGauntlet, 3, 6).addProperties(new PropertyEldrichBoost());
    public static Item gauntletOfZillywenn = new MSUWeaponBase(2400, 24.5, -0.78, 14, "gauntlet_of_zillywenn","gauntletOfZillywenn").setTool(toolGauntlet, 5, 8F);
    public static Item gasterBlaster = new ItemWindUpBeam(640, 7.7, -0.66D, 0.05f, 10, 1.3f, 16, 0, 16, "gaster_blaster","gasterBlaster").setSounds(MSUSoundHandler.gasterBlasterCharge, MSUSoundHandler.gasterBlasterRelease).setTool(toolGauntlet, 6, 4F)
            .setTool(toolGauntlet, 4, 8).addProperties(new PropertyPotionBeam(new PotionEffect(MobEffects.WITHER, 100, 0)), new PropertyBeamDeathMessage("sans"));
    public static Item midasGlove = new MSUWeaponBase(800, 8.7D, -0.57D, 14, "midas_glove","midasGlove").setTool(toolGauntlet, 3, 10F).addProperties(new PropertyGristSetter(GristType.Gold));

    //Needlekind
    public static Item knittingNeedles = new ItemKnittingNeedles(128,1.6, -0.24, 1, "knitting_needle", "knittingNeedle").setTool(toolNeedles, 2, 1f);
    public static Item pointySticks = new MSUWeaponBase(300,3.2, -0.3, 10, "pointy_stick", "pointyStick").setTool(toolNeedles, 1, 1f).addProperties(new PropertyDualWield()).addProperties(new PropertyTipperDamage(0.9f, 1.1f, 0), new PropertyMobTypeDamage(EnumCreatureAttribute.UNDEAD, 2)).setRepairMaterial("plankWood");
    public static Item boneNeedles = new MSUWeaponBase(225,3.6, -0.3, 5, "bone_needle", "boneNeedle").setTool(toolNeedles, 1, 1f).addProperties(new PropertyTipperDamage(0.7f, 1.25f, 0), new PropertyDualWield()).setRepairMaterial("bone");
    public static Item drumstickNeedles = new MSUWeaponBase(500,4, -0.3, 5, "drumstick_needles", "drumstickNeedles").setTool(toolNeedles, 2, 1f).addProperties(new PropertyTipperDamage(0.7f, 1.25f, 0), new PropertyDualWield(), new PropertySoundOnHit(SoundEvents.BLOCK_NOTE_BASEDRUM, PITCH_NOTE, ((stack, target, player) -> 3f)), new PropertySoundOnClick(new SoundEvent[] {SoundEvents.BLOCK_NOTE_BASEDRUM}, PITCH_NOTE,  ((stack, target, player) -> 3f), true));
    public static Item dragonBlades = new MSUWeaponBase(500, 7.9, -0.36, 4, "dragon_blade", "dragonBlades").setTool(toolNeedles, 2, 4).addProperties(new PropertyDualWield(), new PropertyTipperDamage(0.8f, 1.3f, 0.1f)).setCreativeTab(null);
    public static Item litGlitterBeamTransistor = new ItemBeamWeapon(700,5.5, -0.3, 0.1f, 20, 1, 72000, 40, 30, "lit_glitter_beam_transistor", "litGlitterBeamTransistor").setTool(toolNeedles, 2, 4f).addProperties(new PropertyDualWield(), new PropertyTipperDamage(0.9f, 1.1f, 0), new PropertyRainbowBeam(), new PropertyBeamDeathMessage("rainbow"));
    public static Item needlewands = new ItemBeamWeapon(488,6.5, -0.3, 0.05f, 10, 1, 60, "needlewand", "needlewand").setTool(toolNeedles, 3, 2f).addProperties(new PropertyDualWield(), new PropertyTipperDamage(0.95f, 1.15f, 0), new PropertyMagicBeam(), new PropertyBeamDeathMessage("magic"));
    public static Item oglogothThorn = new ItemBeamWeapon(666,7.2, -0.3, 0.1f, 30, 0.7f, 5, 20, 80, "thorn_of_oglogoth", "oglogothThorn").setTool(toolNeedles, 4, 3f).addProperties(new PropertyDualWield(), new PropertyTipperDamage(0.6f, 1.6f, 0.12f));
    public static Item echidnaQuills = new MSUWeaponBase(17.3, -0.3, 100, "quill_of_echidna", "echidnaQuill").setTool(toolNeedles, 10, 5f).addProperties(new PropertyDualWield(), new PropertyTipperDamage(1f, 1.2f, 0));
    public static Item thistlesOfZillywitch = new MSUWeaponBase(800, 9.1,  -0.3, 40, "thistles_of_zillywitch", "thistlesOfZillywitch").setTool(toolNeedles, 5, 10f).addProperties(new PropertyDualWield(), new PropertyTipperDamage(0.9f, 1.3f, 0));

    //Shieldkind
    public static Item woodenDoorshield = new MSUShieldBase(340, 12, 0.3f, 5, "wooden_doorshield", "woodenDoorshield").setRepairMaterial("plankWood");
    public static Item ironDoorshield = new MSUShieldBase(540, 8, 0.4f, 7, "iron_doorshield", "ironDoorshield").setRepairMaterial("ingotIron");
    public static Item clearShield = new MSUShieldBase(180, 20, 0.25f, 5, "clear_shield", "clearShield");
    public static Item bladedShield = new MSUShieldBase(300, 7, -1.2, 10, 0.32f, 6, "bladed_shield", "bladedShield");
    public static Item shockerShell = new MSUShieldBase(480, 14, 0.3f, 8, "shocker_shell", "shockerShell").setRepairMaterials(new ItemStack(battery)).addProperties(new PropertyElectric(10, 2, 0, false), new PropertyShieldShock(5, 2, 0.1f, 10, 4, 0.8f));
    public static Item rocketRiotShield = new MSUShieldBase(450, 6, 0.35f, 7, "rocket_riot_shield", "rocketRiotShield").setRepairMaterial("gunpowder").addProperties(new PropertyRocketBoost(0.4f));
    public static Item ejectorShield = new MSUShieldBase(320, 7, 0.3f, 7, "ejector_shield", "ejectorShield").addProperties(new PropertyShieldEject(4f, 15));
    public static Item firewall = new MSUShieldBase(320, 7, 0.3f, 7, "firewall", "firewall").addProperties(new PropertyShieldFire(10, 1000, 0.7f, 1f, true));
    public static Item clarityWard = new MSUShieldBase(410, 8, 0.25f, 12, "clarity_ward", "christopherShield");
    public static Item obsidianShield = new MSUShieldBase(2000, 0, -3, 12, 0.6f, 10, "obsidian_shield", "obsidianShield").addProperties(new PropertyUseOnCooled(), new PropertyVisualParry());
    public static Item windshield = new MSUShieldBase(355, 18, 0.1f, 7, "windshield", "windshield").addProperties(new PropertyShieldKnockback(2f, true), new PropertyShieldKnockback(0.3f, false));
    public static Item wallOfThorns = new MSUShieldBase(440, 10, 0.5f, 7, "wall_of_thorns", "wallOfThorns").addProperties(new PropertyShieldPotion(true, 1f, new PotionEffect(MobEffects.POISON, 400, 1)), new PropertyShieldPotion(false, 0.1f, new PotionEffect(MobEffects.POISON, 100, 0)));
    public static Item hardRindHarvest = new MSUShieldBase(320, 7, 0.4f, 6, "hard_rind_harvest", "hardRindHarvest").addProperties(new PropertyEdible(2, 0.4f, 5).setPotionEffect(0.1f, new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 0)));
    public static Item nuclearNeglector = new MSUShieldBase(480, 8, 0.15f, 8, "nuclear_neglector", "nuclearNeglector").addProperties(new PropertyShieldPotionNegative(new PotionEffect(MobEffects.WITHER, 60, 2)), new PropertyShieldPotion(true, 1, new PotionEffect(MobEffects.WITHER, 600, 2)));
    public static Item livingShield = new MSUShieldBase(465, 6, 0.0f, 8, "living_shield", "livingShield").addProperties(new PropertyShieldHeal(0.6f, true));
    public static Item perfectAegis = new MSUShieldBase(800, 3, 1f, 12, "perfect_aegis", "perfectAegis").addProperties(new PropertyShieldDeflect(1, 5), new PropertyVisualParry());

    //Bowkind
    public static final MSUBowBase.IIsArrow REGULAR_ARROWS = stack -> stack.getItem() == Items.ARROW;

    public static MSUBowBase energyBow = (MSUBowBase) new MSUBowBase(330, 2.1f, 18, 2.1f, 1.1f, 1, true, "energy_bow", "energyBow").requireNoAmmo().addProperties(new PropertyLaserArrow());
    public static MSUBowBase infernoShot = (MSUBowBase) new MSUBowBase(385, 2, 24, 2, 0.9f, 1, true, "inferno_shot", "infernoShot").addProperties(new PropertyFlamingArrow(10, 0.9f));
    public static MSUBowBase icicleBow = (MSUBowBase) new MSUBowBase(230, 2, 30, 2, 0.7f, 1, true, "icicle_bow", "icicleBow").setArrowCheck(REGULAR_ARROWS).addProperties(new PropertyPotionArrow(new PotionEffect(MobEffects.SLOWNESS, 200, 2), 0.8f));
    public static MSUBowBase tempestBow = (MSUBowBase) new MSUBowBase(540, 2.3f, 16, 3.1f, 1.2f, 1, true, "tempest_bow", "tempestBow").addProperties(new PropertyHookshot(0.8f, 16, true, true, true), new PropertyLaserArrow());
    public static MSUBowBase shiverburnWing = (MSUBowBase) new MSUBowBase(390, 2.2f, 27, 2.2f, 0.8f, 1, true, "shiverburn_wing", "shiverburnWing").setArrowCheck(REGULAR_ARROWS).addProperties(new PropertyPotionArrow(new PotionEffect(MobEffects.SLOWNESS, 140, 2), 0.8f), new PropertyFlamingArrow(7, 0.9f));
    public static MSUBowBase magneticHookshot = (MSUBowBase) new MSUBowBase(680, 1f, 32, 4f, 0.8f, 1, true, "magnetic_hookshot", "magneticHookshot").setArrowCheck(REGULAR_ARROWS).addProperties(new PropertyHookshot(1, 64));
    public static MSUBowBase wormholePiercer = (MSUBowBase) new MSUBowBase(640, 1, 35, 3.5f, 0.8f, 1, true, "wormhole_piercer", "wormholePiercer").setArrowCheck(REGULAR_ARROWS).addProperties(new PropertyTeleArrows(), new PropertyLaserArrow());
    public static MSUBowBase telegravitationalWarper = (MSUBowBase) new MSUBowBase(640, 3, 28, 2.9f, 0.8f, 1, true, "telegravitational_warper", "telegravitationalWarper").setArrowCheck(REGULAR_ARROWS).addProperties(new PropertyHookshot(0.4f, 16, false, false, true), new PropertyLaserArrow(), new PropertyGhostArrow());
    public static MSUBowBase crabbow = new MSUBowBase(2048, 7, -1.8, 2.3f, 27, 1.95f, 1, 1, false, "crabbow", "crabbow");
    public static ItemMechanicalCrossbow mechanicalCrossbow = new ItemMechanicalCrossbow(385, 1,"mechanical_crossbow", "mechanicalCrossbow");
    public static MSUBowBase sweetBow = (MSUBowBase) new MSUBowBase(450, 1.8f, 20, 2.1f, 0.9f, 1, true, "sweet_bow", "sweetBow").addProperties(new PropertyCandyWeapon());
    public static MSUBowBase kingOfThePond = (MSUBowBase) new MSUBowBase(890, 2, 10, 5, 1.2f, 1, true, "king_of_the_pond", "kingOfThePond").addProperties(new PropertyFlamingArrow(20, 0.65f));
    public static MSUBowBase gildedGuidance = (MSUBowBase) new MSUBowBase(1210, 3.2f, 30, 0.0f, 0, 2, true, "gilded_guidance", "gildedGuidance").requireNoAmmo().addProperties(new PropertyLaserArrow(), new PropertyGuidedArrow());
    public static MSUBowBase bowOfLight = (MSUBowBase) new MSUBowBase(2050, 4f, 24, 5, 0.3f, 3, true, "bow_of_light", "bowOfLight").requireNoAmmo().addProperties(new PropertyLaserArrow(), new PropertyArrowNoGravity(), new PropertyFlamingArrow(6, 0.9f), new PropertyPierce(0.1f));
    public static MSUBowBase theChancemaker = (MSUBowBase) new MSUBowBase(1280, 1f, 16, 3, 1.3f, 2, true, "the_chancemaker", "theChancemaker").addProperties(new PropertyRandomDamage());
    public static MSUBowBase wisdomsPierce = (MSUBowBase) new MSUBowBase(1560, 3, 22, 4, 0.3f, 2, false, "wisdom_pierce", "calamityBow").addProperties(new PropertyPierce(0.4f), new PropertyLowHealthBoost(SharedMonsterAttributes.LUCK, "Calamity Boost", 2, 0.4, 0.5f, 1), new PropertyLowHealthDrawSpeed(0.5f, 0.2f));
    public static MSUBowBase wisdomsHookshot = (MSUBowBase) new MSUBowBase(1560, 0.5f, 22, 3, 0.3f, 2, true, "wisdom_hookshot", "calamityHookshot").requireNoAmmo().addProperties(new PropertyHookshot(1, 128), new PropertyArrowNoGravity(), new PropertyLowHealthBoost(SharedMonsterAttributes.LUCK, "Calamity Boost", 2, 0.4, 0.5f, 1)).setCreativeTab(null);


    //Hammerkind
    public static Item loghammer = new MSUWeaponBase(776, 12.8, -2.8, 7, "loghammer", "loghammer").setTool(toolHammer, 0, 3.0f).setRepairMaterial("logWood");
    public static Item overgrownLoghammer = new MSUWeaponBase(575, 19.2, -2.8, 7, "overgrown_loghammer", "overgrownLoghammer").setTool(toolHammer, 0, 3.0f).setRepairMaterial("logWood").addProperties(new PropertyPlantMend());
    public static Item glowingLoghammer = new MSUWeaponBase(906, 19.2, -2.8, 7, "glowing_loghammer", "glowingLoghammer").setTool(toolHammer, 0, 3.0f).setRepairMaterials(new ItemStack(MinestuckBlocks.glowingLog)).addProperties(new PropertyPotion(new PotionEffect(MobEffects.GLOWING, 200, 0), false, 1));
    public static Item midasMallet = new MSUWeaponBase(1150, 26.9D, -2.94D, 15, "midas_mallet", "midasMallet").setTool(toolHammer, 3, 2f).addProperties(new PropertyGristSetter(GristType.Gold), new PropertyVMotionDamage(1.6f, 3));
    public static Item aaaNailShocker = new MSUWeaponBase(776, 19.2, -2.52, 10,"aaa_nail_shocker", "aaaNailShocker").setTool(toolHammer, 2, 3f).setRepairMaterials(new ItemStack(battery)).addProperties(new PropertyElectric(20, 0, 0.7f, true));
    public static Item highVoltageStormCrusher = new MSUWeaponBase(1150, 30.7, -2.8, 18, "high_voltage_storm_crusher", "highVoltageStormCrusher").setTool(toolHammer, 4, 3.0f).addProperties(new PropertyLightning(8, 1, true, false), new PropertyElectric(60, 8, -1, false), new PropertyVMotionDamage(1.6f, 3));
    public static Item barrelsWarhammer = new MSUWeaponBase(1438, 64, -2.8, 18, "barrel_warhammer", "calamityHammer").setTool(toolHammer, 4, 4.0f).addProperties(new PropertyRocketBoost(0.6f), new PropertyLowHealthBoost(SharedMonsterAttributes.ATTACK_DAMAGE, "Calamity Boost", 0.6, 0.1, 0.3f, 2), new PropertyVMotionDamage(1.6f, 3), new PropertyVMotionDamage(1.6f, 3));
    public static Item stardustSmasher = new MSUWeaponBase(1725, 44.8, -2.8, 20, "stardust_smasher", "stardustSmasher").setTool(toolHammer, 20, 8.0f).addProperties(new PropertyMobTypeDamage(EnumCreatureAttribute.ARTHROPOD, 1000), new PropertyVMotionDamage(1.6f, 3));

    //Clawkind
    public static Item katars = new MSUWeaponBase(248, 1.6, -0.65, 2, "katars", "katars").setTool(toolClaws, 2, 3).addProperties(new PropertySweep(), new PropertyDualWield());
    public static Item diamondKatars = new MSUWeaponBase(900, 2.5, -0.65, 6, "diamond_katars", "diamondKatars").setTool(toolClaws, 2, 3).addProperties(new PropertySweep(), new PropertyDualWield());
    public static Item actionClaws = new ItemDualClaw(608, 3.9D, 0.0D, -0.55D, -0.0D, 6, "actionClaws","action_claws").setTool(toolClaws, 2, 3).addProperties(new PropertyActionBuff(200, 2.5));
    public static Item candyCornClaws = new ItemDualClaw(743, 4.8, 0.0D, -0.65D, -0.0D, 6, "candyCornClaws","candy_corn_claws").setTool(toolClaws, 2, 3).addProperties(new PropertyCandyWeapon());
    public static Item sneakyDaggers = new ItemDualClaw(900, 5.5, 0.0D, -0.61D, -0.0D, 7, "sneakyDaggers","sneaky_daggers").setTool(toolClaws, 2, 3).addProperties(new PropertySneaky(1.2f, 1.1f, 1.6f));
    public static Item rocketKatars = new MSUWeaponBase(585, 6.4, -0.6, 8, "rocket_katars", "rocketKatars").setTool(toolClaws, 2, 5).setRepairMaterial("gunpowder").addProperties(new PropertySweep(), new PropertyDualWield(), new PropertyRocketDash(3, 20, 0.3f, 2.5f));
    public static Item blizzardCutters = new ItemDualClaw(810, 6.4, 0,-0.65, 0, 8, "blizzardCutters", "blizzard_cutters").setTool(toolClaws, 3, 3).addProperties(new PropertyPotion(new PotionEffect(MobEffects.SLOWNESS, 400, 0), false, 0.4f), new PropertyKnockback(2.2f));
    public static Item thunderbirdTalons = new MSUWeaponBase(1125, 8.8,-0.65, 18, "thunderbird_talons", "thunderbirdTalons").setTool(toolClaws, 5, 3).setRepairMaterials(new ItemStack(MinestuckUniverseItems.battery)).addProperties(new PropertySweep(), new PropertyDualWield(), new PropertyShock(10, 3, 0.4f, true), new PropertyKnockback(1.8f));
    public static Item archmageDaggers = new ItemBeamWeapon(1350, 9.6,-0.68, 0.05f, 15, 1, 60, 10, 18, "archmage_daggers", "archmageDaggers").setTool(toolClaws, 5, 3).addProperties(new PropertySweep(), new PropertyDualWield(), new PropertyMagicBeam(), new PropertyBeamDeathMessage("magic"));
    public static Item katarsOfZillywhomst = new ItemDualClaw(2700, 15.3, 0,-0.65, 0, 40, "katarsOfZillywhomst", "katars_of_zillywhomst").setTool(toolClaws, 5, 10).addProperties(new PropertySweep(), new PropertyDualWield());
    public static Item bladesOfTheWarrior = new MSUWeaponBase(1080, 6.4,-0.65, 24, "blades_of_the_warrior", "christopherClaws").setTool(toolClaws, 4, 3).addProperties(new PropertySweep(), new PropertyDualWield()).setCreativeTab(null);

    //Canekind
    public static Item staffOfOvergrowth = new MSUWeaponBase(1040, 14.4, -2, 20, "staff_of_overgrowth", "staffOfOvergrowth").setTool(toolCane, 3, 2).addProperties(new PropertyBlockSwap(overgrowthTransforms, 1), new PropertyPotion(new PotionEffect(MobEffects.POISON, 400, 1), false, 0.4f));
    public static Item atomicIrradiator = new MSUWeaponBase(900, 18, -2, 20, "atomic_irradiator", "atomicIrradiator").setTool(toolCane,3, 3).addProperties(new PropertyPotion(new PotionEffect(MobEffects.WITHER, 400, 1), true, 0.6f), new PropertyGristSetter(GristType.Uranium));
    public static Item goldCane = new MSUWeaponBase(600, 12, -2, 18, "gold_cane", "goldCane").setTool(toolCane, 2, 5).setRepairMaterial("ingotGold");
    public static Item goldenCuestaff = new MSUWeaponBase(1000, 20, -2, 32, "golden_cuestaff", "goldenCuestaff").setTool(toolCane, 2, 6);
    public static Item scepterOfZillywuud = new MSUWeaponBase(2400, 43.2, -2, 32, "scepter_of_zillywuud", "scepterOfZillywuud").setTool(toolCane, 5, 10);

    //Clubkind
    public static Item rubyContrabat = new MSUWeaponBase(1200, 14.1, -1.98, 22, "ruby_contrabat", "rubyContrabat").setTool(toolClub, 3, 4.0f).addProperties(new PropertySweep(), new PropertyGristSetter(GristType.Ruby), new PropertyProjectileDeflect(0.5f, 4));
    public static Item homeRunBat = new MSUWeaponBase(3240, 16.9, -3.9, 10, "home_run_bat", "homeRunBat").setTool(toolClub, 5, 2.0f).addProperties(new PropertySweep(), new PropertyKnockback(15), new PropertySoundOnHit(MSUSoundHandler.homeRunBat, 1, 1.2f), new PropertyProjectileDeflect(1, 10));
    public static Item dynamiteStick = new MSUWeaponBase(1050, 17.6, -2.2, 8, "dynamite_stick", "dynamiteStick").setTool(toolClub, 1, 2.0f).addProperties(new PropertySweep(), new PropertyExplode(2.5f, 1, true), new PropertyProjectileDeflect(0.6f, 4));
    public static Item nightmareMace = new MSUWeaponBase(1200, 14.1, -2.09, 8, "nightmare_mace", "nightmareMace").setTool(toolClub, 3, 3.0f).addProperties(new PropertySweep(), new PropertyHungry(3, 4, true), new PropertyProjectileDeflect(0.3f, 8),
            new PropertyPotion(new PotionEffect(MobEffects.BLINDNESS, 200, 0), false, 0.2f), new PropertyPotion(new PotionEffect(MobEffects.NAUSEA, 200, 0), false, 0.2f), new PropertyPotion(new PotionEffect(MobEffects.WITHER, 100, 1), true, 0.2f));
    public static Item cranialEnder = new MSUWeaponBase(1800, 17.6, -2.2, 8, "cranial_ender", "cranialEnder").setTool(toolClub, 5, 2.0f).addProperties(new PropertySweep(), new PropertyExplode(0.5f, 0.2f, true), new PropertyPotion(new PotionEffect(MobEffects.NAUSEA, 100, 0), true, 0.7f), new PropertyProjectileDeflect(0.6f, 3));
    public static Item badaBat = new MSUWeaponBase(8035, 15.9, -3.9, 10, "bada_bat", "badaBat").setTool(toolClub, 5, 14.0f).addProperties(new PropertySweep(), new PropertyKnockback(15), new PropertySoundOnHit(MSUSoundHandler.homeRunBat, 1, 1.2f), new PropertySoundOnClick(MSUSoundHandler.bada, 1, 1.2f), new PropertyProjectileDeflect(1, 10)).setCreativeTab(null);

    //Dicekind TODO
    public static Item dice = new MSUItemBase("dice", "dice").setCreativeTab(TabMinestuck.instance);
    public static Item fluoriteOctet = new MSUItemBase("fluorite_octet", "fluoriteOctet").setCreativeTab(TabMinestuck.instance);

    //Throwkind
    public static MSUThrowableBase yarnBall = new ItemYarnBall("yarn_ball", "yarnBall");
    public static MSUThrowableBase wizardbeardYarn = new MSUThrowableBase("wizardbeard_yarn", "wizardbeardYarn").addProperties(new PropertyMagicDamagePrjctle(6));
    public static Item dragonCharge = new ItemDragonCharge("dragon_charge", "dragonCharge");
    public static MSUThrowableBase throwingStar = new MSUThrowableBase(8, 0, 32, "throwing_star", "throwingStar").addProperties(new PropertyDamagePrjctle(4), new PropertyThrowGravity(0.7f), new PropertyBreakableProjectile(0.7f));
    public static MSUThrowableBase goldenStar = new MSUThrowableBase(4, 0, 64, "golden_star", "goldenStar").addProperties(new PropertyDamagePrjctle(2), new PropertyThrowGravity(0.4f), new PropertyBreakableProjectile(0.9f));
    public static MSUThrowableBase suitarang = new MSUThrowableBase(8, 0, 32, "suitarang", "suitarang").addProperties(new PropertyDamagePrjctle(6), new PropertyThrowGravity(0.7f), new PropertyBreakableProjectile(0.5f), new PropertyVariableItem(4));
    public static MSUThrowableBase psionicStar = new MSUThrowableBase(10, 0, 16, "psionic_star", "psionicStar").setSize(3).addProperties(new PropertyDamagePrjctle(12, true), new PropertyThrowGravity(0.7f), new PropertyBreakableProjectile(0.2f));
    public static MSUThrowableBase boomerang = new MSUThrowableBase(10, 0, 1, 1f, 2, -0.5, "boomerang", "boomerang"){{setMaxDamage(64);}}.addProperties(new PropertyDamagePrjctle(5), new PropertyThrowGravity(0.6f), new PropertyBoomerang());
    public static MSUThrowableBase markedBoomerang = new MSUThrowableBase(10, 0, 1, 1f, 2, -0.5, "marked_boomerang", "markedBoomerang"){{setMaxDamage(64);}}.addProperties(new PropertyDamagePrjctle(5), new PropertyThrowGravity(0.6f), new PropertyBoomerang());
    public static MSUThrowableBase redHotRang = new MSUThrowableBase(12, 0, 1, 1f, 4, -0.5, "red_hot_rang", "redHotRang"){{setMaxDamage(80);}}.addProperties(new PropertyDamagePrjctle(7), new PropertyThrowGravity(0.6f), new PropertyBoomerang(), new PropertyFirePrjctle(5, false));
    public static MSUThrowableBase tornadoGlaive = new MSUThrowableBase(8, 0, 1, 1f, 6, -1f, "tornado_glaive", "tornadoGlaive"){{setMaxDamage(550);}}.setSize(2).addProperties(new PropertyDamagePrjctle(8), new PropertyPrjctleItemPull(16, 0.5f), new PropertyBoomerang(), new PropertyThrowGravity(0.4f));
    public static MSUThrowableBase hotPotato = new MSUThrowableBase(0, 5, 16, "hot_potato", "hotPotato").addProperties(new PropertyDamagePrjctle(10), new PropertyFirePrjctle(10, true));

    //Rockkind
    public static MSUThrowableBase pebble = new MSUThrowableBase(0, 0, 16, 1.4f, 0, 0, "pebble", "pebble").addProperties(new PropertyDamagePrjctle(2), new PropertyThrowGravity(1.5f));
    public static MSUThrowableBase rock = new MSUThrowableBase(10, 5, 16, 1.2f, 5, -2.7, "rock", "rock").addProperties(new PropertyDamagePrjctle(8), new PropertyThrowGravity(2.5f));
    public static Item bigRock = new ItemBigRock("big_rock", "bigRock");

    //Misc.
    public static Item diamondSickle = new MSUWeaponBase(1650, 5.5, -2.4, 32, "diamond_sickle", "diamondSickle").setTool(toolSickle, 3, 4).addProperties(new PropertySweep(3), new PropertyFarmine(2, 500));
    public static Item hereticusAurum = new MSUWeaponBase(110, 15.9, -2.16, 32, "hereticus_aurum", "hereticusAurum").setTool(toolSickle, 5, 4).addProperties(new PropertySweep(3), new PropertyFarmine(5, 500));
    public static Item gravediggerShovel = new MSUWeaponBase(720, 16, -3, 8, "gravedigger_shovel", "gravediggerShovel").setTool(toolShovel, 3, 4).addProperties(new PropertyMobTypeDamage(EnumCreatureAttribute.UNDEAD, 3));
    public static Item fancySpoon = new MSUWeaponBase(1200, 4.5, -2, 4, "fancy_spoon", "fancySpoon").setTool(toolSpoon, 3, 3).addProperties(new PropertyHungerSpeed(1.2f));
    public static Item quartzFork = new MSUWeaponBase(405, 9.1, -2, 4, "quartz_fork", "quartzFork").setTool(toolFork, 3, 3).addProperties(new PropertyTipperDamage(0.6f, 1.3f, 0.8f), new PropertyHungerSpeed(1.2f));
    public static Item crockerFork = new MSUWeaponBase(600, 11.5, -2, 6, "crocker_fork", "crockerFork").setTool(toolFork, 4, 8).addProperties(new PropertyTipperDamage(0.6f, 1.3f, 0.8f), new PropertyHungerSpeed(1.2f)).setCreativeTab(null);
    public static Item battlesporkOfZillywut = new MSUWeaponBase(3150, 37.9, -2, 40, "battlespork_of_zillywut", "battlesporkOfZillywut").setTool(toolSpork, 5, 10).addProperties(new PropertyHungerSpeed(1.2f));
    public static Item battleaxeOfZillywahoo = new MSUWeaponBase(3000, 86.4, -3, 40, "battleaxe_of_zillywahoo", "battleaxeOfZillywahoo").setTool(toolAxe, 5, 10);
    public static Item battlepickOfZillydew = new MSUWeaponBase(780, 16, -2.8, 40, "battlepick_of_zillydew", "battlepickOfZillydew").setTool(toolPickaxe, 5, 10);
    public static Item rolledUpPaper = new MSUWeaponBase(200, 3, 0, 1, "rolled_up_paper", "rolledUpPaper");
    public static Item yesterdaysNews = new MSUWeaponBase(450, 7, 0, 1, "yesterdays_news", "yesterdaysNews").addProperties(new PropertyPotion(new PotionEffect(MobEffects.MINING_FATIGUE, 200, 1), false, 0.5f), new PropertyPotion(new PotionEffect(MobEffects.SLOWNESS, 200, 1), false, 0.5f), new PropertyFire(3, 0.7f, true));

    //Armor
    public static MSUArmorBase diverHelmet = new ItemDiverHelmet(materialDiverHelmet,0,EntityEquipmentSlot.HEAD,"diverHelmet", "diver_helmet");
    public static MSUArmorBase spikedHelmet = new MSUArmorBase(materialSpikedHelmet,0,EntityEquipmentSlot.HEAD,"spikedDiverHelmet", "spiked_diver_helmet");
    public static MSUArmorBase cruxtruderHat = new MSUArmorBase(materialMetal,0,EntityEquipmentSlot.HEAD,"cruxtruderHelmet", "cruxtruder_helmet");
    public static MSUArmorBase frogHat = new MSUArmorBase(materialCloth,0,EntityEquipmentSlot.HEAD,"frogHat", "frog_hat");
    public static MSUArmorBase wizardHat = new MSUArmorBase(40, materialCloth,0,EntityEquipmentSlot.HEAD,"wizardHat", "wizard_hat");
    public static MSUArmorBase archmageHat = new MSUArmorBase(500, materialCloth,0,EntityEquipmentSlot.HEAD,"archmageHat", "archmage_hat");
    public static MSUArmorBase cozySweater = new ItemWitherproofArmor(60, materialCloth,0,EntityEquipmentSlot.CHEST,"cozySweater", "cozy_sweater");
    public static MSUArmorBase scarf = new ItemScarf(materialCloth,0,EntityEquipmentSlot.HEAD,"scarf", "scarf");
    public static MSUArmorBase rubberBoots = new MSUArmorBase(materialRubber,0,EntityEquipmentSlot.FEET,"rubberBoots", "rubber_boots");
    public static MSUArmorBase bunnySlippers = new MSUArmorBase(materialCloth,0,EntityEquipmentSlot.FEET,"bunnySlippers", "bunny_slippers");
    public static MSUArmorBase moonShoes = new ItemPogoBoots(1.1f, materialRubber,0,"moonShoes", "moon_shoes");
    public static MSUArmorBase sunShoes = new ItemPogoBoots(1.6f, materialSunShoes,0,"solarShoes", "solar_shoes").setSolar();
    public static MSUArmorBase rocketBoots = new MSUArmorBase(850, materialSunShoes,0,EntityEquipmentSlot.FEET,"rocketBoots", "rocket_boots").setRepairMaterial("gunpowder");
    public static MSUArmorBase windWalkers = new MSUArmorBase(283, materialWindWalkers,0,EntityEquipmentSlot.FEET,"windWalkers", "wind_walkers");
    public static MSUArmorBase airJordans = new MSUArmorBase(230, materialRubber,0,EntityEquipmentSlot.FEET,"airJordans", "air_jordans");
    public static MSUArmorBase cobaltJordans = new MSUArmorBase(480, materialCobalt,0,EntityEquipmentSlot.FEET,"airJordansCobalt", "air_jordans_cobalt");

    //Overrides
    public static MSUArmorBase crumplyHat = new MSUArmorBase(materialCloth, 0, EntityEquipmentSlot.HEAD, "crumplyHat", Minestuck.MOD_ID+":crumply_hat");
    public static Item catclaws = new ItemDualClaw(450, 2.9, -0.65, -1.5D, -1.0D, 6, "catClaws",Minestuck.MOD_ID+ ":catclaws").setTool(toolClaws, 2, 1);

    //Support
    public static Item splatcraftCruxiteFilter = new MSUItemBase("cruxite_filter", "cruxiteFilter"){
        @Override
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        }
    };

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(crumplyHat.setRegistryName(Minestuck.MOD_ID, "crumply_hat"));
	    registry.register(dice.setRegistryName(Minestuck.MOD_ID, "dice"));
	    registry.register(fluoriteOctet.setRegistryName(Minestuck.MOD_ID, "fluorite_octet"));

        registerItem(registry, moonstone);
        registerItem(registry, moonstoneChisel);
        registerItem(registry, zillystoneShard);
        registerItem(registry, fluorite);
        registerItem(registry, battery);
        registerItem(registry, yarnBall);
        registerItem(registry, wizardbeardYarn);
        registerItem(registry, spaceSalt);
        registerItem(registry, cueBall);
        registerItem(registry, tickingStopwatch);
        registerItem(registry, timetable);
        registerItem(registry, strifeCard);

        registerItem(registry, laserPointer);
        registerItem(registry, whip);
        registerItem(registry, sbahjWhip);
        registerItem(registry, unrealAir);

        registerItem(registry, returnNode);
        registerItem(registry, travelGate);
        registerItem(registry, endPortal);
        registerItem(registry, netherPortal);
        registerItem(registry, endGateway);
        registerItem(registry, sun);
        registerItem(registry, moon);
        registerItem(registry, skaia);
        registerItem(registry, greenSun);
        registerItem(registry, lightning);

        registerItem(registry, diverHelmet);
        registerItem(registry, spikedHelmet);
        registerItem(registry, frogHat);
        registerItem(registry, cruxtruderHat);
        registerItem(registry, wizardHat);
        registerItem(registry, archmageHat);
        registerItem(registry, cozySweater);
        registerItem(registry, scarf);

        registerItem(registry, bunnySlippers);
        registerItem(registry, rubberBoots);
        registerItem(registry, moonShoes);
        registerItem(registry, sunShoes);
        registerItem(registry, airJordans);
        registerItem(registry, cobaltJordans);
        registerItem(registry, rocketBoots);
        registerItem(registry, windWalkers);

        registerItem(registry, ironMedallion);
        registerItem(registry, returnMedallion);
        registerItem(registry, teleportMedallion);
        registerItem(registry, skaianMedallion);

        if(MSUConfig.combatOverhaul)
        {
            MinestuckItems.clawHammer = registerCustomRenderedItem(registry, new MSUWeaponBase(259, 6.4, -2.52, 1, MinestuckItems.clawHammer).setTool(toolHammer, 1, 2).addProperties(new PropertyVMotionDamage(1.6f, 3)));
            MinestuckItems.sledgeHammer = registerItem(registry, new MSUWeaponBase(575, 12.8, -2.8, 3, MinestuckItems.sledgeHammer).setTool(toolHammer, 2, 4));
            MinestuckItems.blacksmithHammer = registerItem(registry, new MSUWeaponBase(575, 12.8, -2.8, 2, MinestuckItems.blacksmithHammer).setTool(toolHammer, 1, 6));
            MinestuckItems.pogoHammer = registerItem(registry, new MSUWeaponBase(863, 19.2, -2.8, 4, MinestuckItems.pogoHammer).setTool(toolHammer, 2, 4).addProperties(new PropertyPogo(0.7), new PropertyVMotionDamage(2f, 4)));
            MinestuckItems.telescopicSassacrusher = registerItem(registry, new MSUWeaponBase(1610, 51.2, -3.64, 4, MinestuckItems.telescopicSassacrusher).setTool(toolHammer, 8, 2).addProperties(new PropertyFarmine(100, 128), new PropertyVMotionDamage(1.8f, 10)));
            MinestuckItems.regiHammer = registerItem(registry, new MSUWeaponBase(776, 19.2, -2.52, 4, MinestuckItems.regiHammer).setTool(toolHammer, 3, 6));
            MinestuckItems.fearNoAnvil = registerCustomRenderedItem(registry, new MSUWeaponBase(1725, 32, -2.66, 8, MinestuckItems.fearNoAnvil).setTool(toolHammer, 3, 10).addProperties(new PropertyPotion(false, 0.5f, new PotionEffect(MobEffects.SLOWNESS, 400, 1), new PotionEffect(MobEffects.MINING_FATIGUE, 400, 2)), new PropertyVMotionDamage(1.6f, 3)));
            MinestuckItems.meltMasher = registerItem(registry, new MSUWeaponBase(1265, 25.6, -2.8, 4, MinestuckItems.meltMasher).setTool(toolHammer, 4, 8).addProperties(new PropertyAutoSmelt(), new PropertyFarmine(8, 5), new PropertyFire(4, 1, false)));
            MinestuckItems.zillyhooHammer = registerItem(registry, new MSUWeaponBase(3450, 69.1, -2.94, 40, MinestuckItems.zillyhooHammer).setTool(toolHammer, 5, 10));
            MinestuckItems.scarletZillyhoo = registerItem(registry, new MSUWeaponBase(2588, 49.9, -2.8, 60, MinestuckItems.scarletZillyhoo).setTool(toolHammer, 5, 10).addProperties(new PropertyFire(10, 0.8f, true)));
            MinestuckItems.popamaticVrillyhoo = registerItem(registry, new MSUWeaponBase(3019, 20, -2.66, 20, MinestuckItems.popamaticVrillyhoo).setTool(toolHammer, 5, 12).addProperties(new PropertyRandomDamage(0, 7, 4)));
            MinestuckItems.mwrthwl = registerItem(registry, new MSUWeaponBase(1725, 32, -2.8, 8, MinestuckItems.mwrthwl).setTool(toolHammer, 4, 8).addProperties(new PropertyTrueDamage()));
            MinestuckItems.qPHammerAxe = registerItem(registry, new MSUWeaponBase(1290, 28.8, -2.9, 4, MinestuckItems.qPHammerAxe).setTool(toolHammaxe, 4, 6).addProperties(new PropertyPogo(0.6), new PropertyFarmine(25, 64)));
            MinestuckItems.qFHammerAxe = registerItem(registry, new MSUWeaponBase(1209, 43.2, -3, 8, MinestuckItems.qFHammerAxe).setTool(toolHammaxe, 4, 8).setRepairMaterials(new ItemStack(MinestuckItems.rawUranium)).addProperties(new PropertyPogo(0.7), new PropertyFarmine(25, 32), new PropertyPotion(true, 0.5f, new PotionEffect(MobEffects.WITHER, 200, 2))));
            MinestuckItems.qEHammerAxe = registerItem(registry, new MSUWeaponBase(1725, 38.4, -2.8, 12, MinestuckItems.qEHammerAxe).setTool(toolHammer, 4, 6).addProperties(new PropertyPogo(0.8), new PropertyFarmine(25, 8)));
            MinestuckItems.dDEHammerAxe = registerCustomRenderedItem(registry, new MSUWeaponBase(1724, 38.4, -2.8, 12, MinestuckItems.dDEHammerAxe).setTool(toolHammer, 0, 6).addProperties(new PropertyPogo(1), new PropertySoundOnHit(MinestuckSoundHandler.soundScreech, 1F, 1.5F)));
        }

        registerItem(registry, loghammer);
        registerItem(registry, overgrownLoghammer);
        registerItem(registry, glowingLoghammer);
        registerItem(registry, midasMallet);
        registerItem(registry, aaaNailShocker);
        registerItem(registry, highVoltageStormCrusher);
        registerItem(registry, barrelsWarhammer);
        registerItem(registry, stardustSmasher);

        if(MSUConfig.combatOverhaul)
        {
            MinestuckItems.sord = registerItem(registry, new MSUWeaponBase(250, 3, -2.3, 1, MinestuckItems.sord).setTool(toolSword, 0, 0).addProperties(new PropertySlippery()));
            MinestuckItems.cactusCutlass = registerItem(registry, new MSUWeaponBase(746, 12.7, -2.3, 5, MinestuckItems.cactusCutlass).setTool(toolSword, 1, 4));
            MinestuckItems.beefSword = registerItem(registry, new MSUWeaponBase(550, 7.2, -2.3, 4, MinestuckItems.beefSword).setTool(toolSword, 1, 2).addProperties(new PropertyEdible(3, 0.3F, 75)));
            MinestuckItems.steakSword = registerItem(registry, new MSUWeaponBase(550, 8, -2.3, 4, MinestuckItems.steakSword).setTool(toolSword, 1, 2).addProperties(new PropertyEdible(8, 0.8F, 50)));
            MinestuckItems.irradiatedSteakSword = registerItem(registry, new MSUWeaponBase(550, 8, -2.3, 3, MinestuckItems.irradiatedSteakSword).setTool(toolSword, 1, 3).addProperties(new PropertyEdible(4, 0.4F, 25).setPotionEffect(0.9f, new PotionEffect(MobEffects.WITHER, 100, 1))));
            MinestuckItems.firePoker = registerItem(registry, new MSUWeaponBase(825, 10.5, -2.07, 10, MinestuckItems.firePoker).setTool(toolSword, 2, 4).addProperties(new PropertyFire(30, 0.3f, false), new PropertyTipperDamage(0.8f, 1.2f, 1)));
            MinestuckItems.hotHandle = registerItem(registry, new MSUWeaponBase(825, 12.0, -2.3, 10, MinestuckItems.hotHandle).setTool(toolSword, 3, 3).addProperties(new PropertyFire(15, 1f, true)));
            MinestuckItems.royalDeringer = registerCustomRenderedItem(registry, new MSUWeaponBase(908, 13.2, -2.3, 14, MinestuckItems.royalDeringer){{addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "broken"), PropertyBreakableItem.getPropertyOverride());}}.setTool(toolSword, 3, 6).addProperties(new PropertyBreakableItem()));
            MinestuckItems.caledfwlch = registerCustomRenderedItem(registry, new MSUWeaponBase(1100, 16.0, -2.3, 16, MinestuckItems.caledfwlch){{addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "broken"), PropertyBreakableItem.getPropertyOverride());}}.setTool(toolSword, 4, 8).addProperties(new PropertyTrueDamage(), new PropertyBreakableItem()));
            MinestuckItems.caledscratch = registerCustomRenderedItem(registry, new MSUWeaponBase(1375, 20.0, -2.07, 20, MinestuckItems.caledscratch){{addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "broken"), PropertyBreakableItem.getPropertyOverride());}}.setTool(toolSword, 4, 10).addProperties(new PropertyXpMend(), new PropertyBreakableItem()));
            MinestuckItems.doggMachete = registerItem(registry, new MSUWeaponBase(1513, 20.0, -2.3, 10, MinestuckItems.doggMachete).setTool(toolSword, 4, 10).addProperties(new PropertyPotion(new PotionEffect(MobEffects.SLOWNESS, 200, 0), false, 0.4f), new PropertyKnockback(0.65f)));
            MinestuckItems.scarletRibbitar = registerCustomRenderedItem(registry, new MSUWeaponBase(1375, 22.0, -2.415, 18, MinestuckItems.scarletRibbitar){{addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "broken"), PropertyBreakableItem.getPropertyOverride());}}.setTool(toolSword, 4, 10).addProperties(new PropertyFire(30, 0.5f, true), new PropertyBreakableItem()));
            MinestuckItems.cobaltSabre = registerItem(registry, new MSUWeaponBase(1210, 16.0, -2.07, 20, MinestuckItems.cobaltSabre).setTool(toolSword, 4, 8).addProperties(new PropertyFire(8, 0.8f, true), new PropertyGristSetter(GristType.Cobalt)));
            MinestuckItems.zillywairCutlass = registerItem(registry, new MSUWeaponBase(3300, 33.6, -2.3, 40, MinestuckItems.zillywairCutlass).setTool(toolSword, 5, 10));
            MinestuckItems.regisword = registerItem(registry, new MSUWeaponBase(743, 12.0, -2.07, 8, MinestuckItems.regisword).setTool(toolSword, 3, 6));
            MinestuckItems.quantumSabre = registerItem(registry, new MSUWeaponBase(880, 14.4, -2.3, 10, MinestuckItems.quantumSabre).setTool(toolSword, 3, 6).addProperties(new PropertyPotion(new PotionEffect(MobEffects.WITHER, 100, 1), false, 0.6f)));

            MinestuckItems.shatterBeacon = registerItem(registry, new MSUWeaponBase(1100, 34.0, -2.3, 14, MinestuckItems.shatterBeacon).setTool(toolSword, 3, 8).addProperties(new PropertyPotion(false, 0.6f,
                    new PotionEffect(MobEffects.SPEED, 300, 0),
                    new PotionEffect(MobEffects.HASTE, 300, 0),
                    new PotionEffect(MobEffects.RESISTANCE, 300, 0),
                    new PotionEffect(MobEffects.JUMP_BOOST, 300, 0),
                    new PotionEffect(MobEffects.STRENGTH, 300, 0),
                    new PotionEffect(MobEffects.REGENERATION, 300, 1)
            )));

            MinestuckItems.claymore = registerItem(registry, new MSUWeaponBase(660, 18.4, -2.76, 5, MinestuckItems.claymore).setTool(toolSword, 3, 4));
            MinestuckItems.katana = registerItem(registry, new MSUWeaponBase(650, 8, -2.3, 6, MinestuckItems.katana).setTool(toolSword, 1, 2));
        }

        registerCustomRenderedItem(registry, new MSUWeaponBase(5500, 16, -2.3, 12, MinestuckItems.unbreakableKatana).setTool(toolSword, 4, 6));
        registerItem(registry, bloodKatana);
        registerItem(registry, trueUnbreakableKatana);
        registerCustomRenderedItem(registry, batteryBeamBlade);
        for(ItemBeamBlade blade : dyedBeamBlade)
            registerCustomRenderedItem(registry, blade);
        registerItem(registry, quantumEntangloporter);
        registerItem(registry, lightbringer);
        registerItem(registry, cybersword);
        registerItem(registry, crystallineRibbitar);
        registerItem(registry, valorsEdge);

        registerItem(registry, fancyGlove);
        registerItem(registry, spikedGlove);
        registerItem(registry, cobbleBasher);
        registerItem(registry, fluoriteGauntlet);
        registerItem(registry, goldenGenesisGauntlet);
        registerItem(registry, pogoFist);
        registerItem(registry, rocketFist);
        registerItem(registry, jawbreaker);
        registerItem(registry, eldrichGauntlet);
        registerItem(registry, gasterBlaster);
        registerItem(registry, gauntletOfZillywenn);
        registerItem(registry, midasGlove);

        registerItem(registry, clearShield);
        registerItem(registry, woodenDoorshield);
        registerItem(registry, ironDoorshield);
        registerItem(registry, bladedShield);
        registerItem(registry, clarityWard);
        registerItem(registry, bladesOfTheWarrior);
        registerItem(registry, shockerShell);
        registerItem(registry, windshield);
        registerItem(registry, rocketRiotShield);
        registerItem(registry, ejectorShield);
        registerItem(registry, firewall);
        registerItem(registry, obsidianShield);
        registerItem(registry, wallOfThorns);
        registerItem(registry, hardRindHarvest);
        registerItem(registry, livingShield);
        registerItem(registry, nuclearNeglector);
        registerItem(registry, perfectAegis);

        registerItem(registry, katars);
        registerItem(registry, diamondKatars);
        registerCustomRenderedItem(registry, catclaws);
        registerCustomRenderedItem(registry, actionClaws);
        registerCustomRenderedItem(registry, candyCornClaws);
        registerCustomRenderedItem(registry, sneakyDaggers);
        registerItem(registry, rocketKatars);
        registerCustomRenderedItem(registry, blizzardCutters);
        registerItem(registry, thunderbirdTalons);
        registerItem(registry, archmageDaggers);
        registerCustomRenderedItem(registry, katarsOfZillywhomst);

        registerItem(registry, knittingNeedles);
        registerItem(registry, pointySticks);
        registerItem(registry, boneNeedles);
        registerItem(registry, drumstickNeedles);
        registerItem(registry, litGlitterBeamTransistor);
        registerItem(registry, needlewands);
        registerItem(registry, oglogothThorn);
        registerItem(registry, echidnaQuills);
        registerItem(registry, thistlesOfZillywitch);

        registerItem(registry, energyBow);
        registerItem(registry, infernoShot);
        registerItem(registry, sweetBow);
        registerItem(registry, icicleBow);
        registerItem(registry, tempestBow);
        registerItem(registry, shiverburnWing);
        registerItem(registry, magneticHookshot);
        registerItem(registry, wormholePiercer);
        registerItem(registry, telegravitationalWarper);
        registerItem(registry, mechanicalCrossbow);
        registerItem(registry, crabbow);
        registerItem(registry, kingOfThePond);
        registerItem(registry, gildedGuidance);
        registerItem(registry, bowOfLight);
        registerItem(registry, theChancemaker);
        registerItem(registry, wisdomsPierce);
        registerItem(registry, wisdomsHookshot);

        if(MSUConfig.combatOverhaul)
        {
            MinestuckItems.sickle = registerItem(registry, new MSUWeaponBase(275, 3.6, -2.4, 1, MinestuckItems.sickle).setTool(toolSickle, 1, 2).addProperties(new PropertySweep(2), new PropertyFarmine(1, 500)));
            MinestuckItems.homesSmellYaLater = registerItem(registry, new MSUWeaponBase(990, 11.9, -2.4, 10, MinestuckItems.homesSmellYaLater).setTool(toolSickle, 2, 3).addProperties(new PropertySweep(3f), new PropertyFarmine(2, 500)));
            MinestuckItems.fudgeSickle = registerItem(registry, new MSUWeaponBase(880, 17.3, -2.64, 6, MinestuckItems.fudgeSickle).setTool(toolSickle, 2, 2).addProperties(new PropertySweep(2.5f), new PropertyEdible(6, 1, 10)));
            MinestuckItems.candySickle = registerItem(registry, new MSUWeaponBase(908, 10.8, -2.4, 5, MinestuckItems.candySickle).setTool(toolSickle, 3, 4).addProperties(new PropertySweep(3), new PropertyFarmine(2, 500), new PropertyCandyWeapon()));
            MinestuckItems.regiSickle = registerItem(registry, new MSUWeaponBase(743, 10.8, -2.16, 8, MinestuckItems.regiSickle).setTool(toolSickle, 3, 4).addProperties(new PropertySweep(3), new PropertyFarmine(3, 500)));
            MinestuckItems.clawSickle = registerItem(registry, new MSUWeaponBase(1375, 23.5, -2.64, 8, MinestuckItems.clawSickle).setTool(toolSickle, 3, 3).addProperties(new PropertySweep(5), new PropertyFarmine(2, 500)));
            MinestuckItems.clawOfNrubyiglith = registerItem(registry, new MSUWeaponBase(1650, 23.5, -2.4, 12, MinestuckItems.clawOfNrubyiglith).setTool(toolSickle, 4, 3).addProperties(new PropertySweep(3.5f), new PropertyWhisperingTerror(0.15f)));
        }
        registerItem(registry, diamondSickle);
        registerItem(registry, hereticusAurum);

        if(MSUConfig.combatOverhaul)
        {
            MinestuckItems.woodenSpoon = registerItem(registry, new MSUWeaponBase(300, 3.2, -2, 1, MinestuckItems.woodenSpoon).setTool(toolSpoon, 0, 2).addProperties(new PropertyHungerSpeed(1.2f)));
            MinestuckItems.silverSpoon = registerItem(registry, new MSUWeaponBase(600, 6.4, -1.88, 8, MinestuckItems.silverSpoon).setTool(toolSpoon, 2, 4).addProperties(new PropertyHungerSpeed(1.1f)));
            registerCustomRenderedItem(registry, new MSUWeaponBase(900, 9.6, -2.2, 6, MinestuckItems.crockerSpork.getRegistryName().toString(), "crockerSpoon").setTool(toolSpoon, 4, 8).addProperties(new PropertyHungerSpeed(1.2f)));
        }
        registerCustomRenderedItem(registry, crockerFork);
        registerItem(registry, fancySpoon);
        if(MSUConfig.combatOverhaul)
        {
            MinestuckItems.fork = registerItem(registry, new MSUWeaponBase(225, 3.9, -2.2, 1, MinestuckItems.fork).setTool(toolFork, 0, 2).addProperties(new PropertyHungerSpeed(1.2f)));
            MinestuckItems.skaiaFork = registerItem(registry, new MSUWeaponBase(1080, 18.3, -2.42, 10, MinestuckItems.skaiaFork).setTool(toolFork, 3, 6).addProperties(new PropertyTipperDamage(0.6f, 1.3f, 0.8f)).addProperties(new PropertyHungerSpeed(1.25f)));
        }
        registerItem(registry, quartzFork);
        if(MSUConfig.combatOverhaul)
        {
            MinestuckItems.spork = registerItem(registry, new MSUWeaponBase(525, 7.1, -2.2, 8, MinestuckItems.spork).setTool(toolSpork, 2, 4).addProperties(new PropertyHungerSpeed(1.2f)));
            MinestuckItems.goldenSpork = registerItem(registry, new MSUWeaponBase(788, 9.5, -1.98, 8, MinestuckItems.goldenSpork).setTool(toolSpork, 1, 8).addProperties(new PropertyHungerSpeed(1.3f)));
        }
        registerItem(registry, battlesporkOfZillywut);

        if(MSUConfig.combatOverhaul)
        {
            MinestuckItems.copseCrusher = registerItem(registry, new MSUWeaponBase(500, 16, -2.85, 2, MinestuckItems.copseCrusher).setTool(toolAxe, 4, 4).addProperties(new PropertyFarmine(100, 64)));
            MinestuckItems.battleaxe = registerItem(registry, new MSUWeaponBase(600, 32, -3.15, 5, MinestuckItems.battleaxe).setTool(toolAxe, 3, 2));
            MinestuckItems.batleacks = registerItem(registry, new MSUWeaponBase(750, 24, -3, 1, MinestuckItems.batleacks).setTool(toolAxe, 2, 2).addProperties(new PropertySlippery()));
            MinestuckItems.blacksmithBane = registerItem(registry, new MSUWeaponBase(750, 26.4, -3.03, 8, MinestuckItems.blacksmithBane).setTool(toolAxe, 3, 3));
            MinestuckItems.scraxe = registerItem(registry, new MSUWeaponBase(675, 24, -3, 7, MinestuckItems.scraxe).setTool(toolAxe, 3, 5).addProperties(new PropertySoundOnHit(SoundEvents.BLOCK_NOTE_GUITAR, PITCH_NOTE, ((stack, target, player) -> 3f)), new PropertySoundOnClick(new SoundEvent[] {SoundEvents.BLOCK_NOTE_GUITAR}, PITCH_NOTE,  ((stack, target, player) -> 3f), false)));
            MinestuckItems.hephaestusLumber = registerItem(registry, new MSUWeaponBase(750, 28, -3, 9, MinestuckItems.hephaestusLumber).setTool(toolAxe, 4, 3).addProperties(new PropertyAutoSmelt(), new PropertyFarmine(100, 32), new PropertyFire(20, 1, true), new PropertyFire(2, 1, false)));
            MinestuckItems.rubyCroak = registerItem(registry, new MSUWeaponBase(1000, 32, -2.7, 10, MinestuckItems.rubyCroak).setTool(toolAxe, 3, 3).addProperties(new PropertyFire(10, 0.8f, false)));
        }

        registerItem(registry, battleaxeOfZillywahoo);
        registerItem(registry, battlepickOfZillydew);
        registerItem(registry, gravediggerShovel);

        if(MSUConfig.combatOverhaul)
        {
            MinestuckItems.deuceClub = registerItem(registry, new MSUWeaponBase(270, 3.5, -2.2, 1, MinestuckItems.deuceClub).setTool(toolClub, 0, 1).addProperties(new PropertyProjectileDeflect(0.1f, 1)));
            MinestuckItems.metalBat = registerItem(registry, new MSUWeaponBase(720, 7.1, -2.42, 4, MinestuckItems.metalBat).setTool(toolClub, 2, 4).addProperties(new PropertyProjectileDeflect(0.25f, 2)));
            MinestuckItems.pogoClub = registerItem(registry, new MSUWeaponBase(900, 10, -2.2, 6, MinestuckItems.pogoClub).setTool(toolClub, 2, 6).addProperties(new PropertyProjectileDeflect(0.4f, 1.5f), new PropertyPogo(0.5f)));
            MinestuckItems.spikedClub = registerItem(registry, new MSUWeaponBase(900, 11.1, -2.2, 6, MinestuckItems.spikedClub).setTool(toolClub, 3, 3).addProperties(new PropertyProjectileDeflect(0.15f, 1.5f)));
            MinestuckItems.nightClub = registerItem(registry, new MSUWeaponBase(1320, 11.2, -2.2, 6, MinestuckItems.nightClub).setTool(toolClub, 3, 3).addProperties(new PropertyProjectileDeflect(0.2f, 3), new PropertyDaytimeDamage(false, 1.4f)));
        }
        registerItem(registry, rubyContrabat);
        registerItem(registry, dynamiteStick);
        registerItem(registry, nightmareMace);
        registerItem(registry, cranialEnder);
        registerItem(registry, homeRunBat);
        registerItem(registry, badaBat);

        if(MSUConfig.combatOverhaul)
        {
            MinestuckItems.cane = registerItem(registry, new MSUWeaponBase(200, 4, -2, 1, MinestuckItems.cane).setTool(toolCane, 0, 1));
            MinestuckItems.ironCane = registerItem(registry, new MSUWeaponBase(480, 8, -2, 3, MinestuckItems.ironCane).setTool(toolCane, 1, 2));
            MinestuckItems.spearCane = registerItem(registry, new MSUWeaponBase(660, 13.2, -2, 3, MinestuckItems.spearCane).setTool(toolCane, 2, 3).addProperties(new PropertyTipperDamage(0.9f, 1.1f, 1)));
            MinestuckItems.paradisesPortabello = registerItem(registry, new MSUWeaponBase(540, 12, -2, 2, MinestuckItems.paradisesPortabello).setTool(toolCane, 2, 2).addProperties(new PropertyEdible(4, 2, 5)));
            MinestuckItems.regiCane = registerItem(registry, new MSUWeaponBase(540, 12, -1.8, 5, MinestuckItems.regiCane).setTool(toolCane, 3, 4).addProperties(new PropertyTipperDamage(0.8f, 1.1f, 1)));
            MinestuckItems.dragonCane = registerItem(registry, new MSUWeaponBase(1000, 24, -2, 7, MinestuckItems.dragonCane).setTool(toolCane, 4, 3));
        }
        registerItem(registry, dragonBlades);
        if(MSUConfig.combatOverhaul)
        {
            MinestuckItems.pogoCane = registerItem(registry, new MSUWeaponBase(600, 12, -2, 5, MinestuckItems.pogoCane).setTool(toolCane, 2, 4).addProperties(new PropertyPogo(0.6)));
            MinestuckItems.upStick = registerItem(registry, new MSUWeaponBase(-1, 6.4, -2, 5, MinestuckItems.upStick).setTool(toolCane, 1, 3).addProperties(new PropertyPotion(true, 1, new PotionEffect(MobEffects.WITHER, 20, 2))));
        }

        registerItem(registry, staffOfOvergrowth);
        registerItem(registry, atomicIrradiator);
        registerItem(registry, goldCane);
        registerItem(registry, goldenCuestaff);
        registerItem(registry, scepterOfZillywuud);

        registerItem(registry, throwingStar);
        registerItem(registry, goldenStar);
        registerItem(registry, suitarang);
        registerItem(registry, psionicStar);
        registerItem(registry, boomerang);
        registerItem(registry, markedBoomerang);
        registerItem(registry, redHotRang);
        registerItem(registry, hotPotato);
        registerItem(registry, dragonCharge);
        registerItem(registry, tornadoGlaive);

        registerItem(registry, pebble);
        registerItem(registry, rock);
        registerItem(registry, bigRock);
        registerItem(registry, rolledUpPaper);
        registerItem(registry, yesterdaysNews);

        registerItem(registry, dungeonKey);

        //Blocks
        registerItemBlocks(registry);

        //Support
        if(MinestuckUniverse.isSplatcraftLodaded)
            splatcraftCruxiteFilter = new ItemFilter("cruxiteFilter", "cruxite_filter", false).setCreativeTab(TabMinestuckUniverse.main);
        registerItem(registry, splatcraftCruxiteFilter);
    }

    public static void setPostInitVariables()
    {
        overgrowthTransforms.put(Blocks.DIRT, 0, Blocks.GRASS, 0);
        overgrowthTransforms.put(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE);
        overgrowthTransforms.put(Blocks.STONEBRICK, 0, Blocks.STONEBRICK, 1);
        overgrowthTransforms.put(Blocks.MONSTER_EGG, 2, Blocks.MONSTER_EGG, 3);
        overgrowthTransforms.put(Blocks.COBBLESTONE_WALL, 0, Blocks.COBBLESTONE_WALL, 1);
        overgrowthTransforms.put(Blocks.END_STONE, MinestuckBlocks.endGrass);
        overgrowthTransforms.put(MinestuckBlocks.pinkStoneBricks, MinestuckBlocks.pinkStoneMossy);
        overgrowthTransforms.put(Blocks.LOG, 0, MinestuckBlocks.log, 4);
        overgrowthTransforms. put(Blocks.LOG, 4, MinestuckBlocks.log, 0);
        overgrowthTransforms.put(Blocks.LOG, 8, MinestuckBlocks.log, 8);
        overgrowthTransforms.put(Blocks.LOG, 12, MinestuckBlocks.log, 12);


        if(MinestuckUniverse.isMSGTLoaded && MinestuckItems.fearNoAnvil instanceof MSUWeaponBase)
            ((MSUWeaponBase)MinestuckItems.fearNoAnvil)
                    .addProperties(new PropertyPotion(true, 0.1f, new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("minestuckgodtier","time_stop")), 20, 0)));

        wisdomsPierce.addProperties(new PropertyInnocuousDouble(wisdomsHookshot, true, false, false));
        wisdomsHookshot.addProperties(new PropertyInnocuousDouble(wisdomsPierce, true, false, false));
        ((MSUWeaponBase)clarityWard).addProperties(new PropertyInnocuousDouble(bladesOfTheWarrior, true, false, true));
        ((MSUWeaponBase)bladesOfTheWarrior).addProperties(new PropertyInnocuousDouble(clarityWard, true, true, false));
        ((MSUWeaponBase)dragonBlades).addProperties(new PropertyInnocuousDouble(Item.REGISTRY.getObject(new ResourceLocation(Minestuck.MOD_ID, "dragon_cane")), false, true, false));
        ((MSUWeaponBase)crockerFork).addProperties(new PropertyInnocuousDouble(Item.REGISTRY.getObject(new ResourceLocation(Minestuck.MOD_ID, "crocker_spork")), false, false, false));

        if(MSUConfig.combatOverhaul)
        {
            ((MSUWeaponBase)Item.REGISTRY.getObject(new ResourceLocation(Minestuck.MOD_ID, "dragon_cane"))).addProperties(new PropertyInnocuousDouble(dragonBlades, false, false, true));
            ((MSUWeaponBase)Item.REGISTRY.getObject(new ResourceLocation(Minestuck.MOD_ID, "crocker_spork"))).addProperties(new PropertyInnocuousDouble(crockerFork, false, false, false));
        }

        if(MinestuckUniverse.isBotaniaLoaded)
        {
            overgrowthTransforms.put(ModBlocks.livingrock, 1, ModBlocks.livingrock, 2);
            overgrowthTransforms.put(ModBlocks.livingwood, 1, ModBlocks.livingwood, 2);
            overgrowthTransforms.put(ModBlocks.dreamwood, 1, ModBlocks.dreamwood, 2);
        }

        suitarang.addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "variant"),
                ((stack, worldIn, entityIn) -> ((PropertyVariableItem) suitarang.getProperty(PropertyVariableItem.class, stack)).getPropertyOverride().apply(stack, worldIn, entityIn)));
        valorsEdge.addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "awakened"),
                ((stack, worldIn, entityIn) -> ((PropertyLowHealthBoost)((MSUWeaponBase)valorsEdge).getProperty(PropertyLowHealthBoost.class, stack)).getPropertyOverride().apply(stack, worldIn, entityIn)));
        barrelsWarhammer.addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "awakened"),
                ((stack, worldIn, entityIn) -> ((PropertyLowHealthBoost)((MSUWeaponBase)barrelsWarhammer).getProperty(PropertyLowHealthBoost.class, stack)).getPropertyOverride().apply(stack, worldIn, entityIn)));
        wisdomsPierce.addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "awakened"),
                ((stack, worldIn, entityIn) -> ((PropertyLowHealthBoost) wisdomsPierce.getProperty(PropertyLowHealthBoost.class, stack)).getPropertyOverride().apply(stack, worldIn, entityIn)));
        wisdomsHookshot.addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "awakened"),
                ((stack, worldIn, entityIn) -> ((PropertyLowHealthBoost) wisdomsHookshot.getProperty(PropertyLowHealthBoost.class, stack)).getPropertyOverride().apply(stack, worldIn, entityIn)));
        dragonBlades.addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "offhand"), ((stack, worldIn, entityIn) -> entityIn != null && entityIn.getHeldItemOffhand().equals(stack) ? 1 : 0));
        bladesOfTheWarrior.addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID, "left"), ((stack, worldIn, entityIn) -> entityIn != null &&
                entityIn.getHeldItem(entityIn.getPrimaryHand() == EnumHandSide.LEFT ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND).equals(stack) ? 1 : 0));

        energyBow.setArrowTexture("energy_arrow");
        sweetBow.setCustomArrowTexture();
        icicleBow.setCustomArrowTexture();
        shiverburnWing.setCustomArrowTexture();
        magneticHookshot.setCustomArrowTexture();
        tempestBow.setCustomArrowTexture();
        wormholePiercer.setCustomArrowTexture();
        telegravitationalWarper.setArrowTexture("gravity_arrow");
        gildedGuidance.setCustomArrowTexture();
        bowOfLight.setArrowTexture("light_arrow");
        wisdomsHookshot.setArrowTexture("energy_arrow");
        theChancemaker.setArrowTexture("the_chancemaker");

        ((IBeamStats)needlewands).setCustomBeamTexture();
        ((IBeamStats)oglogothThorn).setBeamTexture("eldrich_beam");
        ((IBeamStats) gasterBlaster).setBeamTexture("clear_beam");
        ((IBeamStats) litGlitterBeamTransistor).setBeamTexture("clear_beam");
        ((IBeamStats) laserPointer).setBeamTexture("laser_beam");
    }

    @SideOnly(Side.CLIENT)
    public static void setClientsideVariables()
    {
        diverHelmet.setArmorModel(new ModelDiverHelmet());
        spikedHelmet.setArmorModel(new ModelSpikedHelmet());
        cruxtruderHat.setArmorModel(new ModelCruxtruderHat());
        frogHat.setArmorModel(new ModelFrogHat());
        wizardHat.setArmorModel(new ModelWizardHat());
        archmageHat.setArmorModel(new ModelArchmageHat());
        scarf.setArmorModel(new ModelScarf());
        crumplyHat.setArmorModel(new ModelCrumplyHat());

        for(ItemBeamBlade blade : dyedBeamBlade)
            registerItemCustomRender(blade, new MSUModelManager.DualWeaponDefinition("dyed_battery_beam_blade", "dyed_battery_beam_blade_off"));
        registerItemCustomRender(batteryBeamBlade, new MSUModelManager.DualWeaponDefinition("battery_beam_blade", "battery_beam_blade_off"));
        registerItemCustomRender(yarnBall, new MSUModelManager.DyedItemDefinition("yarn_ball"));
        registerItemCustomRender(scarf, new MSUModelManager.DyedItemDefinition("scarf"));
        registerItemCustomRender(actionClaws, new MSUModelManager.DualWeaponDefinition("action_claws_drawn", "action_claws_sheathed"));
        registerItemCustomRender(candyCornClaws, new MSUModelManager.DualWeaponDefinition("candy_corn_claws_drawn", "candy_corn_claws_sheathed"));
        registerItemCustomRender(sneakyDaggers, new MSUModelManager.DualWeaponDefinition("sneaky_daggers_drawn", "sneaky_daggers_sheathed"));
        registerItemCustomRender(blizzardCutters, new MSUModelManager.DualWeaponDefinition("blizzard_cutters_drawn", "blizzard_cutters_sheathed"));
        registerItemCustomRender(katarsOfZillywhomst, new MSUModelManager.DualWeaponDefinition("katars_of_zillywhomst_drawn", "katars_of_zillywhomst_sheathed"));

        RenderThrowable.IRenderProperties THROW_STAR_ROTATION = ((entity, partialTicks) ->
        {
            GlStateManager.rotate(90, 1, 0, 0);
            GlStateManager.rotate((entity.ticksExisted+partialTicks) * -(float)Math.PI*10f, 0, 0, 1);
        });

        throwingStar.setRenderProperties(THROW_STAR_ROTATION);
        goldenStar.setRenderProperties(THROW_STAR_ROTATION);
        psionicStar.setRenderProperties(THROW_STAR_ROTATION);
        suitarang.setRenderProperties(THROW_STAR_ROTATION);
        boomerang.setRenderProperties(THROW_STAR_ROTATION);
        markedBoomerang.setRenderProperties(THROW_STAR_ROTATION);
        redHotRang.setRenderProperties(THROW_STAR_ROTATION);
        tornadoGlaive.setRenderProperties(THROW_STAR_ROTATION);
    }

    private static Item registerItem(IForgeRegistry<Item> registry, Item item)
    {
        registerCustomRenderedItem(registry, item);
        MSUModelManager.items.add(item);
        return item;
    }

    private static Item registerCustomRenderedItem(IForgeRegistry<Item> registry, Item item)
    {
        if(item instanceof ISortedTabItem && item.getCreativeTab() == TabMinestuckUniverse.weapons)
            ((ISortedTabItem) item).setTabSlot();
        if(item instanceof IRegistryItem)
            ((IRegistryItem)item).setRegistryName();
        registry.register(item);
        return item;
    }

    @SideOnly(Side.CLIENT)
    private static Item registerItemCustomRender(Item item, MSUModelManager.CustomItemMeshDefinition customMesh)
    {
        MSUModelManager.customItemModels.add(new Pair<>(item, customMesh));
        return item;
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry)
    {
        for(Block block : itemBlocks)
        {
            ItemBlock item = (block instanceof BlockCustomTransportalizer || block instanceof BlockTransportalizer)
                    ? new ItemTransportalizer(block) : new ItemBlock(block);
            registerItem(registry, item.setRegistryName(item.getBlock().getRegistryName()));
        }
    }
}
