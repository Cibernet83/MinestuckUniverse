package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.BlockCustomTransportalizer;
import com.cibernet.minestuckuniverse.client.models.armor.*;
import com.cibernet.minestuckuniverse.enchantments.MSUEnchantments;
import com.cibernet.minestuckuniverse.items.armor.*;
import com.cibernet.minestuckuniverse.items.properties.*;
import com.cibernet.minestuckuniverse.items.properties.clawkind.PropertyActionBuff;
import com.cibernet.minestuckuniverse.items.properties.prjctilekind.PropertyMagicDamagePrjctle;
import com.cibernet.minestuckuniverse.items.properties.shieldkind.PropertyRocketShieldDash;
import com.cibernet.minestuckuniverse.items.properties.shieldkind.PropertyShieldEject;
import com.cibernet.minestuckuniverse.items.properties.shieldkind.PropertyShieldFire;
import com.cibernet.minestuckuniverse.items.properties.shieldkind.PropertyShieldShock;
import com.cibernet.minestuckuniverse.items.weapons.*;
import com.cibernet.minestuckuniverse.util.BlockMetaPair;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.block.BlockTransportalizer;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.item.block.ItemTransportalizer;
import com.mraof.minestuck.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
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
    public static MSUToolClass toolSword = new MSUToolClass(Material.WEB).addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolGauntlet = new MSUToolClass(Material.GLASS, Material.ICE, Material.PACKED_ICE).addEnchantments(Enchantments.SILK_TOUCH, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, MSUEnchantments.SUPERPUNCH);
    public static MSUToolClass toolNeedles = new MSUToolClass(Material.CLOTH).addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolHammer = new MSUToolClass("pickaxe").addEnchantments(EnumEnchantmentType.WEAPON, EnumEnchantmentType.DIGGER);
    public static MSUToolClass toolClub = new MSUToolClass().addEnchantments(EnumEnchantmentType.WEAPON);


    //Block Swap Property Maps
    public static final BlockMetaPair.Map overgrowthTransforms = new BlockMetaPair.Map();

    //Items
    public static Item spaceSalt = new ItemSpaceSalt();
    public static Item moonstone = new MSUItemBase("moonstone");
    public static Item moonstoneChisel = new ItemChisel("moonstone", 31);
    public static Item zillystoneShard = new MSUItemBase("zillystone_shard", "zillystoneShard");
    public static Item fluorite = new MSUItemBase("fluorite");
    public static Item battery = new MSUItemBase("battery", "battery");
    public static Item dungeonKey = new MSUItemBase("dungeon_key", "dungeonKey");
    public static Item yarnBall = new ItemYarnBall("yarn_ball", "yarnBall");
    public static Item wizardbeardYarn = new MSUThrowableBase("wizardbeard_yarn", "wizardbeardYarn").addProperties(new PropertyMagicDamagePrjctle(6));
    public static Item whip = new ItemSound("whip", "whip", MSUSoundHandler.whipCrack);
    public static Item sbahjWhip = new ItemSound("whip_sbahj", "whipSbahj", MSUSoundHandler.whipCrock).setSecret();

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

    //Medallions
    public static Item ironMedallion = new MSUItemBase("iron_medallion", "ironMedallion").setMaxStackSize(1);
    public static Item returnMedallion = new ItemWarpMedallion("returnMedallion", "return_medallion", ItemWarpMedallion.EnumTeleportType.RETURN, 80);
    public static Item teleportMedallion = new ItemWarpMedallion("teleportMedallion", "teleport_medallion", ItemWarpMedallion.EnumTeleportType.TRANSPORTALIZER, 80);
    public static Item skaianMedallion = new ItemWarpMedallion("skaianMedallion", "skaian_medallion", ItemWarpMedallion.EnumTeleportType.SKAIA, 80);

    //Weapons

    //Bladekind
    public static Item trueUnbreakableKatana = (new MSUWeaponBase(-1, 7.0D, -2.35D, 20, "true_unbreakable_katana", "unbreakableKatana")).addProperties(new PropertySweep()).setTool(toolSword, 0, 15.0F);
    public static Item bloodKatana = (new ItemBloodWeapon(450, 9.0D, -2.3D, 10, "blood_katana", "bloodKatana")).addProperties(new PropertySweep()).setTool(toolSword, 0, 15.0F);
    public static ItemBeamBlade batteryBeamBlade = new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade", "batteryBeamBlade").setTool(toolSword, 0, 15.0F);
    public static ItemBeamBlade[] dyedBeamBlade = new ItemBeamBlade[] {
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_white", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.WHITE),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_orange", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.ORANGE),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_magenta", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.MAGENTA),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_light_blue", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.LIGHT_BLUE),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_yellow", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.YELLOW),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_lime", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.LIME),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_pink", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.PINK),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_gray", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.GRAY),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_silver", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.SILVER),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_cyan", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.CYAN),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_purple", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.PURPLE),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_blue", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.BLUE),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_brown", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.BROWN),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_green", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.GREEN),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_red", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.RED),
            new ItemBeamBlade(345, 6, -2.3, 30, "battery_beam_blade_black", "batteryBeamBlade").setTool(toolSword, 0, 15.0F).setColor(EnumDyeColor.BLACK)};
    public static Item lightbringer = new MSUWeaponBase(803, 10, -2.3, 32, "lightbringer", "lightbringer").setTool(toolSword, 3, 10F).addProperties(new PropertySweep(), new PropertyFire(80, 0.8f, true), new PropertyLuckBasedDamage(0.5f), new PropertyMobTypeDamage(EnumCreatureAttribute.UNDEAD, 3));

    //Gauntletkind
    public static Item fancyGlove = new MSUWeaponBase(50, 0D, 0, 5, "fancy_glove", "fancyGlove").setTool(toolGauntlet, 0, 1);
    public static Item spikedGlove = new MSUWeaponBase(95, 3.5D, 0.25D, 8, "spiked_glove", "spikedGlove").setTool(toolGauntlet, 1, 1.4F);
    public static Item cobbleBasher = new MSUWeaponBase(175, 4D, -1.8D, 4, "cobble_basher","cobbleBasher").setTool(toolGauntlet, 1, 4F);
    public static Item fluoriteGauntlet = new MSUWeaponBase(980, 7D, -0.3D,  8, "fluorite_gauntlet", "fluoriteGauntlet").setTool(toolGauntlet, 1, 2.4F);
    public static Item goldenGenesisGauntlet = new MSUWeaponBase(1256, 11D, -0.25D, 15, "golden_genesis_gauntlet","goldenGenesisGauntlet").setTool(toolGauntlet, 1, 3F);
    public static Item pogoFist = new MSUWeaponBase(700, 7.0D, -0.3, 8, "pogo_fist", "pogoFist").setTool(toolGauntlet, 1, 2F).addProperties(new PropertyPogo(0.55D));
    public static Item rocketFist = new MSUWeaponBase(124, 5D, -1, 6, "rocket_powered_fist", "rocketFist").setTool(toolGauntlet, 1, 1.6F).addProperties(new PropertyRocketDash(10, 15, 0.4f, 3));
    public static Item jawbreaker = new MSUWeaponBase(124, 3D, 0.4D, 6, "jawbreaker", "jawbreaker").setTool(toolGauntlet, 1, 1.6F).addProperties(new PropertyCandyWeapon());
    public static Item eldrichGauntlet = new MSUWeaponBase(124, 3D, 0.4D, 6, "eldritch_gauntlet", "eldrichGauntlet").setTool(toolGauntlet, 1, 1.6F).addProperties(new PropertyEldrichBoost());

    //Needlekind
    public static Item knittingNeedles = new ItemKnittingNeedles(32,2, 1, 1, "knitting_needle", "knittingNeedle").setTool(toolNeedles, 2, 1f);
    public static Item pointySticks = new MSUWeaponBase(50,2, 1, 1, "pointy_stick", "pointyStick").setTool(toolNeedles, 1, 1f).addProperties(new PropertyDualWield()).addProperties(new PropertyMobTypeDamage(EnumCreatureAttribute.UNDEAD, 2)).setRepairMaterial("plankWood");
    public static Item boneNeedles = new MSUWeaponBase(100,4, 0, 10, "bone_needle", "boneNeedle").setTool(toolNeedles, 1, 1f).addProperties(new PropertyDualWield()).setRepairMaterial("bone");
    public static Item needlewands = new MSUWeaponBase(250,4, 0.5, 60, "needlewand", "needlewand").setTool(toolNeedles, 3, 2f).addProperties(new PropertyDualWield());
    public static Item oglogothThorn = new MSUWeaponBase(366,5.6, -0.5, 80, "thorn_of_oglogoth", "oglogothThorn").setTool(toolNeedles, 4, 3f).addProperties(new PropertyDualWield());
    public static Item echidnaQuills = new MSUWeaponBase(5, 1, 100, "quill_of_echidna", "echidnaQuill").setTool(toolNeedles, 5, 5f).addProperties(new PropertyDualWield());

    //Shieldkind
    public static Item woodenDoorshield = new MSUShieldBase(340, 12, 0.3f, 5, "wooden_doorshield", "woodenDoorshield").setRepairMaterial("plankWood");
    public static Item ironDoorshield = new MSUShieldBase(540, 8, 0.4f, 7, "iron_doorshield", "ironDoorshield").setRepairMaterial("ingotIron");
    public static Item clearShield = new MSUShieldBase(180, 20, 0.25f, 5, "clear_shield", "clearShield");
    public static Item shockerShell = new MSUShieldBase(480, 14, 0.3f, 8, "shocker_shell", "shockerShell").setRepairMaterials(new ItemStack(battery)).addProperties(new PropertyElectric(10, 2, 0, false), new PropertyShieldShock(5, 2, 0.1f, 10, 4, 0.8f));
    public static Item rocketRiotShield = new MSUShieldBase(450, 6, 0.35f, 7, "rocket_riot_shield", "rocketRiotShield").addProperties(new PropertyRocketShieldDash(0.4f));
    public static Item ejectorShield = new MSUShieldBase(320, 7, 0.3f, 7, "ejector_shield", "ejectorShield").addProperties(new PropertyShieldEject(4f, 15));
    public static Item firewall = new MSUShieldBase(320, 7, 0.3f, 7, "firewall", "firewall").addProperties(new PropertyShieldFire(10, 1000, 0.7f, 1f, true));


    //Hammerkind
    public static Item loghammer = new MSUWeaponBase(355, 7, -2.8, 7, "loghammer", "loghammer").setTool(toolHammer, 0, 3.0f).setRepairMaterial("logWood");
    public static Item overgrownLoghammer = new MSUWeaponBase(210, 7, -2.8, 7, "overgrown_loghammer", "overgrownLoghammer").setTool(toolHammer, 0, 3.0f).setRepairMaterial("logWood").addProperties(new PropertyPlantMend());
    public static Item glowingLoghammer = new MSUWeaponBase(310, 7, -2.8, 7, "glowing_loghammer", "glowingLoghammer").setTool(toolHammer, 0, 3.0f).setRepairMaterials(new ItemStack(MinestuckBlocks.glowingLog)).addProperties(new PropertyPotion(new PotionEffect(MobEffects.GLOWING, 200, 0), false, 1));
    public static Item midasMallet = new MSUWeaponBase(415, 6.5D, -2.5D, 15, "midas_mallet", "midasMallet").setTool(toolHammer, 3, 2f).addProperties(new PropertyGristSetter(GristType.Gold));
    public static Item aaaNailShocker = new MSUWeaponBase(325, 7, -2.4, 10,"aaa_nail_shocker", "aaaNailShocker").setTool(toolHammer, 2, 3f).setRepairMaterials(new ItemStack(battery)).addProperties(new PropertyElectric(20, 0, 0.7f, true));
    public static Item highVoltageStormCrusher = new MSUWeaponBase(580, 10, -2.4, 18, "high_voltage_storm_crusher", "highVoltageStormCrusher").setTool(toolHammer, 4, 3.0f).addProperties(new PropertyLightning(8, 1, true, false), new PropertyElectric(60, 8, -1, false));

    //Sicklekind
    public static Item hereticusAurum = new MSUWeaponBase(620, 7, -2.2, 32, "hereticus_aurum", "hereticusAurum").addProperties(new PropertySweep(3));

    //Clawkind
    public static Item actionClaws = new ItemDualClaw(280, 3.0D, 0.0D, -1.5D, -1.0D, 6, "actionClaws","action_claws").addProperties(new PropertyActionBuff(200, 2.5));
    public static Item candyCornClaws = new ItemDualClaw(310, 4.0D, 0.0D, -1.5D, -1.0D, 6, "candyCornClaws","candy_corn_claws").addProperties(true, new PropertyCandyWeapon());
    public static Item rocketKatars = new MSUWeaponBase(195, 3, -0.5, 8, "rocket_katars", "rocketKatars").addProperties(new PropertySweep(), new PropertyDualWield(), new PropertyRocketDash(3, 20, 0.3f, 2.5f));

    //Canekind
    public static Item staffOfOvergrowth = new MSUWeaponBase(455, 6.0f, -1.2, 20, "staff_of_overgrowth", "staffOfOvergrowth").addProperties(new PropertyBlockSwap(overgrowthTransforms, 1), new PropertyPotion(new PotionEffect(MobEffects.POISON, 400, 1), false, 0.4f));
    public static Item atomicIrradiator = new MSUWeaponBase(455, 6.0f, -1.2, 20, "atomic_irradiator", "atomicIrradiator").addProperties(new PropertyPotion(new PotionEffect(MobEffects.WITHER, 400, 1), true, 0.6f), new PropertyGristSetter(GristType.Uranium));
    public static Item goldCane = new MSUWeaponBase(340, 6.0f, -1.2, 18, "gold_cane", "goldCane").setRepairMaterial("ingotGold");
    public static Item goldenCuestaff = new MSUWeaponBase(780, 10f, 32, "golden_cuestaff", "goldenCuestaff");

    //Clubkind
    public static Item rubyContrabat = new MSUWeaponBase(185, 6.5, -2.2, 22, "ruby_contrabat", "rubyContrabat").setTool(toolClub, 3, 4.0f).addProperties(new PropertySweep(), new PropertyGristSetter(GristType.Ruby));
    public static Item homeRunBat = new MSUWeaponBase(500, 5, -3.9, 10, "home_run_bat", "homeRunBat").setTool(toolClub, 5, 2.0f).addProperties(new PropertySweep(), new PropertyKnockback(15), new PropertySoundOnHit(MSUSoundHandler.homeRunBat, 1, 1.2f));
    public static Item dynamiteStick = new MSUWeaponBase(110, 8, -2.2, 8, "dynamite_stick", "dynamiteStick").setTool(toolClub, 1, 2.0f).addProperties(new PropertySweep(), new PropertyExplode(2.5f, 1, true));

    //Prjctilekind
    public static Item dragonCharge = new ItemDragonCharge("dragon_charge", "dragonCharge");

    //Spadekind
    public static Item gravediggerShovel = new MSUWeaponBase(185, 6, -3, 8, "gravedigger_shovel", "gravediggerShovel").addProperties(new PropertyMobTypeDamage(EnumCreatureAttribute.UNDEAD, 3));

    //Armor
    public static MSUArmorBase diverHelmet = new ItemDiverHelmet(materialDiverHelmet,0,EntityEquipmentSlot.HEAD,"diverHelmet", "diver_helmet");
    public static MSUArmorBase spikedHelmet = new MSUArmorBase(materialSpikedHelmet,0,EntityEquipmentSlot.HEAD,"spikedDiverHelmet", "spiked_diver_helmet");
    public static MSUArmorBase cruxtruderHat = new MSUArmorBase(materialMetal,0,EntityEquipmentSlot.HEAD,"cruxtruderHelmet", "cruxtruder_helmet");
    public static MSUArmorBase frogHat = new MSUArmorBase(materialCloth,0,EntityEquipmentSlot.HEAD,"frogHat", "frog_hat");
    public static MSUArmorBase wizardHat = new MSUArmorBase(40, materialCloth,0,EntityEquipmentSlot.HEAD,"wizardHat", "wizard_hat");
    public static MSUArmorBase archmageHat = new MSUArmorBase(500, materialCloth,0,EntityEquipmentSlot.HEAD,"archmageHat", "archmage_hat");
    public static MSUArmorBase cozySweater = new ItemWitherproofArmor(60, materialCloth,0,EntityEquipmentSlot.CHEST,"cozySweater", "cozy_sweater");
    public static MSUArmorBase scarf = new ItemScarf(materialCloth,0,EntityEquipmentSlot.HEAD,"scarf", "scarf");
    public static Item rocketWings = new MSUItemBase("rocket_wings", "rocketWings");
    public static MSUArmorBase rubberBoots = new MSUArmorBase(materialRubber,0,EntityEquipmentSlot.FEET,"rubberBoots", "rubber_boots");
    public static MSUArmorBase bunnySlippers = new MSUArmorBase(materialCloth,0,EntityEquipmentSlot.FEET,"bunnySlippers", "bunny_slippers");
    public static MSUArmorBase moonShoes = new ItemPogoBoots(1.1f, materialRubber,0,"moonShoes", "moon_shoes");
    public static MSUArmorBase sunShoes = new ItemPogoBoots(1.6f, materialSunShoes,0,"solarShoes", "solar_shoes").setSolar();
    public static MSUArmorBase rocketBoots = new MSUArmorBase(350, materialSunShoes,0,EntityEquipmentSlot.FEET,"rocketBoots", "rocket_boots");
    public static MSUArmorBase windWalkers = new MSUArmorBase(283, materialWindWalkers,0,EntityEquipmentSlot.FEET,"windWalkers", "wind_walkers");
    public static MSUArmorBase airJordans = new MSUArmorBase(230, materialRubber,0,EntityEquipmentSlot.FEET,"airJordans", "air_jordans");
    public static MSUArmorBase cobaltJordans = new MSUArmorBase(480, materialCobalt,0,EntityEquipmentSlot.FEET,"airJordansCobalt", "air_jordans_cobalt");

    //Overrides
    public static MSUArmorBase crumplyHat = new MSUArmorBase(materialCloth, 0, EntityEquipmentSlot.HEAD, "crumplyHat", Minestuck.MOD_ID+":crumply_hat");
    public static Item catclaws = new ItemDualClaw(500, 4.0D, 1.0D, -1.5D, -1.0D, 6, "catClaws",Minestuck.MOD_ID+ ":catclaws");
    public static Item unbreakableKatana = new MSUWeaponBase(2200, 7, -2.4D, 20, Minestuck.MOD_ID +":unbreakable_katana", "katana")
            .setTool(toolSword, 0, 15.0F).addProperties(new PropertySweep()).setCreativeTab(TabMinestuck.instance);

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(catclaws.setRegistryName(Minestuck.MOD_ID, "catclaws"));
        registry.register(unbreakableKatana.setRegistryName(Minestuck.MOD_ID, "unbreakable_katana"));
	    registry.register(crumplyHat.setRegistryName(Minestuck.MOD_ID, "crumply_hat"));

        registerItem(registry, moonstone);
        registerItem(registry, moonstoneChisel);
        registerItem(registry, zillystoneShard);
        registerItem(registry, fluorite);
        registerItem(registry, battery);
        registerItem(registry, yarnBall);
        registerItem(registry, wizardbeardYarn);
        registerItem(registry, spaceSalt);

        registerItem(registry, whip);
        registerItem(registry, sbahjWhip);

        registerItem(registry, returnNode);
        registerItem(registry, travelGate);
        registerItem(registry, endPortal);
        registerItem(registry, netherPortal);
        registerItem(registry, endGateway);
        registerItem(registry, sun);
        registerItem(registry, moon);
        registerItem(registry, skaia);
        registerItem(registry, greenSun);

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

        registerItem(registry, rocketWings);

        registerItem(registry, ironMedallion);
        registerItem(registry, returnMedallion);
        registerItem(registry, teleportMedallion);
        registerItem(registry, skaianMedallion);

        registerItem(registry, fancyGlove);
        registerItem(registry, spikedGlove);
        registerItem(registry, cobbleBasher);
        registerItem(registry, fluoriteGauntlet);
        registerItem(registry, goldenGenesisGauntlet);
        registerItem(registry, pogoFist);
        registerItem(registry, rocketFist);
        registerItem(registry, jawbreaker);
        registerItem(registry, eldrichGauntlet);

        registerItem(registry, clearShield);
        registerItem(registry, woodenDoorshield);
        registerItem(registry, ironDoorshield);
        registerItem(registry, shockerShell);
        registerItem(registry, rocketRiotShield);
        registerItem(registry, ejectorShield);
        registerItem(registry, firewall);

        registerCustomRenderedItem(registry, actionClaws);
        registerCustomRenderedItem(registry, candyCornClaws);
        registerItem(registry, rocketKatars);

        registerItem(registry, knittingNeedles);
        registerItem(registry, pointySticks);
        registerItem(registry, boneNeedles);
        registerItem(registry, needlewands);
        registerItem(registry, oglogothThorn);
        registerItem(registry, echidnaQuills);

        registerItem(registry, loghammer);
        registerItem(registry, overgrownLoghammer);
        registerItem(registry, glowingLoghammer);
        registerItem(registry, midasMallet);
        registerItem(registry, aaaNailShocker);
        registerItem(registry, highVoltageStormCrusher);

        registerItem(registry, hereticusAurum);

        registerItem(registry, rubyContrabat);
        registerItem(registry, homeRunBat);
        registerItem(registry, dynamiteStick);

        registerItem(registry, staffOfOvergrowth);
        registerItem(registry, atomicIrradiator);
        registerItem(registry, goldCane);
        registerItem(registry, goldenCuestaff);

        registerItem(registry, dragonCharge);

        registerItem(registry, bloodKatana);
        registerItem(registry, trueUnbreakableKatana);
        registerCustomRenderedItem(registry, batteryBeamBlade);
        for(ItemBeamBlade blade : dyedBeamBlade)
            registerCustomRenderedItem(registry, blade);
        registerItem(registry, lightbringer);

        registerItem(registry, gravediggerShovel);

        registerItem(registry, dungeonKey);

        //Blocks
        registerItemBlocks(registry);

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

        if(MinestuckUniverse.isBotaniaLoaded)
        {
            overgrowthTransforms.put(ModBlocks.livingrock, 1, ModBlocks.livingrock, 2);
            overgrowthTransforms.put(ModBlocks.livingwood, 1, ModBlocks.livingwood, 2);
            overgrowthTransforms.put(ModBlocks.dreamwood, 1, ModBlocks.dreamwood, 2);
        }
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
    }

    private static Item registerItem(IForgeRegistry<Item> registry, Item item)
    {
    	if(item instanceof IRegistryItem)
            ((IRegistryItem)item).setRegistryName();
        registry.register(item);
        MSUModelManager.items.add(item);
        return item;
    }

    private static Item registerCustomRenderedItem(IForgeRegistry<Item> registry, Item item)
    {
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
