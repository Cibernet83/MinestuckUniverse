package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.BlockCustomTransportalizer;
import com.cibernet.minestuckuniverse.client.models.armor.*;
import com.cibernet.minestuckuniverse.client.render.RenderThrowable;
import com.cibernet.minestuckuniverse.enchantments.MSUEnchantments;
import com.cibernet.minestuckuniverse.items.armor.*;
import com.cibernet.minestuckuniverse.items.properties.*;
import com.cibernet.minestuckuniverse.items.properties.beams.PropertyMagicBeam;
import com.cibernet.minestuckuniverse.items.properties.beams.PropertyPotionBeam;
import com.cibernet.minestuckuniverse.items.properties.bowkind.*;
import com.cibernet.minestuckuniverse.items.properties.clawkind.PropertyActionBuff;
import com.cibernet.minestuckuniverse.items.properties.throwkind.PropertyMagicDamagePrjctle;
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
import com.mraof.minestuck.item.block.ItemTransportalizer;
import com.mraof.minestuck.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
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
    public static MSUToolClass toolClaws = new MSUToolClass().addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolCane = new MSUToolClass().addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolSickle = new MSUToolClass().addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolSpoon = new MSUToolClass().addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolFork = new MSUToolClass().addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolSpork = new MSUToolClass(toolSpoon, toolFork);

    public static MSUToolClass toolShovel = new MSUToolClass("shovel").addEnchantments(EnumEnchantmentType.DIGGER);
    public static MSUToolClass toolAxe = new MSUToolClass("axe").addEnchantments(EnumEnchantmentType.WEAPON, EnumEnchantmentType.DIGGER);
    public static MSUToolClass toolPickaxe = new MSUToolClass("pickaxe").addEnchantments(EnumEnchantmentType.DIGGER);


    //Block Swap Property Maps
    public static final BlockMetaPair.Map overgrowthTransforms = new BlockMetaPair.Map();

    //Items
    public static Item spaceSalt = new ItemSpaceSalt();
    public static Item moonstone = new MSUItemBase("moonstone");
    public static Item moonstoneChisel = new ItemChisel("moonstone", 31);
    public static Item zillystoneShard = new MSUItemBase("zillystone_shard", "zillystoneShard");
    public static Item fluorite = new MSUItemBase("fluorite");
    public static Item battery = new MSUItemBase("battery", "battery");
    public static Item strifeCard = new ItemStrifeCard("strife_card", "strifeCard");
    public static Item dungeonKey = new MSUItemBase("dungeon_key", "dungeonKey");
    public static Item laserPointer = new ItemBeamWeapon(-1, 0, 0, 0.01f, 0, 1, 1, "laser_pointer", "laserPointer").addProperties(new PropertyPotionBeam(new PotionEffect(MobEffects.BLINDNESS, 1, 0, false, false)));
    public static Item whip = new ItemSound("whip", "whip", MSUSoundHandler.whipCrack);
    public static Item sbahjWhip = new ItemSound("whip_sbahj", "whipSbahj", MSUSoundHandler.whipCrock).setSecret();
    public static Item unrealAir = new ItemUnrealAir("unreal_air", "unrealAir");

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
    public static Item lightbringer = new MSUWeaponBase(803, 10, -2.3, 32, "lightbringer", "lightbringer").setTool(toolSword, 4, 10F).addProperties(new PropertySweep(), new PropertyFire(4, 0.8f, true), new PropertyLuckBasedDamage(0.5f), new PropertyMobTypeDamage(EnumCreatureAttribute.UNDEAD, 3));
    public static Item cybersword = new MSUWeaponBase(803, 10, -2.3, 32, "cybersword", "cybersword").setTool(toolSword, 5, 8F).addProperties(new PropertySweep(), new PropertyShock(15, 8, 0.5f, false), new PropertyLightning(15, 1, true, false), new PropertyLuckBasedDamage(0.1f));
    public static Item crystallineRibbitar = new MSUWeaponBase(584, 14, -2.3, 24, "crystalline_ribbitar", "crystallineRibbitar").setTool(toolSword, 4, 6F).addProperties(new PropertySweep());
    public static Item quantumEntangloporter = new MSUWeaponBase(325, 10, -2.3, 9, "quantum_entangloporter", "quantumEntangloporter").setTool(toolSword, 3, 2F).addProperties(new PropertySweep());
    public static Item valorsEdge = new MSUWeaponBase(640, 18, -2.3, 28, "valor_edge", "calamitySword").setTool(toolSword, 4, 4F).addProperties(new PropertySweep());

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
    public static Item gauntletOfZillywenn = new MSUWeaponBase(732, 12D, -1.8D, 14, "gauntlet_of_zillywenn","gauntletOfZillywenn").setTool(toolGauntlet, 3, 4F);
    public static Item gasterBlaster = new ItemWindUpBeam(832, 10D, -1.8D, 0.05f, 10, 1, 16, 0, 16, "gaster_blaster","gasterBlaster").setSounds(MSUSoundHandler.gasterBlasterCharge, MSUSoundHandler.gasterBlasterRelease).setTool(toolGauntlet, 6, 4F).addProperties(new PropertyPotionBeam(new PotionEffect(MobEffects.WITHER, 100, 0)));
    public static Item midasGlove = new MSUWeaponBase(632, 8D, -1.8D, 14, "midas_glove","midasGlove").setTool(toolGauntlet, 3, 5F).addProperties(new PropertyGristSetter(GristType.Gold));

    //Needlekind
    public static Item knittingNeedles = new ItemKnittingNeedles(32,2, 1, 1, "knitting_needle", "knittingNeedle").setTool(toolNeedles, 2, 1f);
    public static Item pointySticks = new MSUWeaponBase(50,2, 1, 1, "pointy_stick", "pointyStick").setTool(toolNeedles, 1, 1f).addProperties(new PropertyDualWield()).addProperties(new PropertyMobTypeDamage(EnumCreatureAttribute.UNDEAD, 2)).setRepairMaterial("plankWood");
    public static Item boneNeedles = new MSUWeaponBase(100,4, 0, 10, "bone_needle", "boneNeedle").setTool(toolNeedles, 1, 1f).addProperties(new PropertyDualWield()).setRepairMaterial("bone");
    public static Item needlewands = new ItemBeamWeapon(250,4, 0.5, 0.05f, 10, 1, 60, "needlewand", "needlewand").setTool(toolNeedles, 3, 2f).addProperties(new PropertyDualWield(), new PropertyMagicBeam());
    public static Item oglogothThorn = new ItemBeamWeapon(666,5.6, -0.5, 0.1f, 30, 0.7f, 5, 20, 80, "thorn_of_oglogoth", "oglogothThorn").setTool(toolNeedles, 4, 3f).addProperties(new PropertyDualWield());
    public static Item echidnaQuills = new MSUWeaponBase(5, 1, 100, "quill_of_echidna", "echidnaQuill").setTool(toolNeedles, 5, 5f).addProperties(new PropertyDualWield());
    public static Item thistlesOfZillywitch = new MSUWeaponBase(640, 7, 0.5, 14, "thistles_of_zillywitch", "thistlesOfZillywitch").setTool(toolNeedles, 3, 4f).addProperties(new PropertyDualWield());

    //Shieldkind
    public static Item woodenDoorshield = new MSUShieldBase(340, 12, 0.3f, 5, "wooden_doorshield", "woodenDoorshield").setRepairMaterial("plankWood");
    public static Item ironDoorshield = new MSUShieldBase(540, 8, 0.4f, 7, "iron_doorshield", "ironDoorshield").setRepairMaterial("ingotIron");
    public static Item clearShield = new MSUShieldBase(180, 20, 0.25f, 5, "clear_shield", "clearShield");
    public static Item bladedShield = new MSUShieldBase(300, 7, 1.2, 10, 0.32f, 6, "bladed_shield", "bladedShield");
    public static Item shockerShell = new MSUShieldBase(480, 14, 0.3f, 8, "shocker_shell", "shockerShell").setRepairMaterials(new ItemStack(battery)).addProperties(new PropertyElectric(10, 2, 0, false), new PropertyShieldShock(5, 2, 0.1f, 10, 4, 0.8f));
    public static Item rocketRiotShield = new MSUShieldBase(450, 6, 0.35f, 7, "rocket_riot_shield", "rocketRiotShield").addProperties(new PropertyRocketShieldDash(0.4f));
    public static Item ejectorShield = new MSUShieldBase(320, 7, 0.3f, 7, "ejector_shield", "ejectorShield").addProperties(new PropertyShieldEject(4f, 15));
    public static Item firewall = new MSUShieldBase(320, 7, 0.3f, 7, "firewall", "firewall").addProperties(new PropertyShieldFire(10, 1000, 0.7f, 1f, true));
    public static Item clarityWard = new MSUShieldBase(410, 8, 0.25f, 12, "clarity_ward", "christopherShield");
    public static Item obsidianShield = new MSUShieldBase(2000, 0, -3, 12, 0.6f, 10, "obsidian_shield", "obsidianShield");
    public static Item windshield = new MSUShieldBase(355, 18, 0.1f, 7, "windshield", "windshield");
    public static Item wallOfThorns = new MSUShieldBase(440, 10, 0.3f, 7, "wall_of_thorns", "wallOfThorns");
    public static Item nuclearNeglector = new MSUShieldBase(480, 8, 0.15f, 8, "nuclear_neglector", "nuclearNeglector");
    public static Item livingShield = new MSUShieldBase(465, 6, 0.0f, 8, "living_shield", "livingShield");
    public static Item perfectAegis = new MSUShieldBase(800, 3, 1f, 12, "perfect_aegis", "perfectAegis");

    //Bowkind
    public static final MSUBowBase.IIsArrow REGULAR_ARROWS = stack -> stack.getItem() == Items.ARROW;

    public static MSUBowBase flimsyBow = new MSUBowBase(210, 3, -1.8, 1.8f, 25, 1.8f,1f, 1, false, "flimsy_bow", "flimsyBow");
    public static MSUBowBase energyBow = (MSUBowBase) new MSUBowBase(330, 2.1f, 18, 2.1f, 1.1f, 1, true, "energy_bow", "energyBow").requireNoAmmo().addProperties(new PropertyLaserArrow());
    public static MSUBowBase infernoShot = (MSUBowBase) new MSUBowBase(385, 2, 24, 2, 0.9f, 1, true, "inferno_shot", "infernoShot").addProperties(new PropertyFlamingArrow(10, 0.9f));
    public static MSUBowBase icicleBow = (MSUBowBase) new MSUBowBase(230, 2, 30, 2, 0.7f, 1, true, "icicle_bow", "icicleBow").setArrowCheck(REGULAR_ARROWS).addProperties(new PropertyPotionArrow(new PotionEffect(MobEffects.SLOWNESS, 200, 2), 0.8f));
    public static MSUBowBase tempestBow = (MSUBowBase) new MSUBowBase(540, 2.3f, 16, 3.1f, 1.2f, 1, true, "tempest_bow", "tempestBow").addProperties(new PropertyHookshot(0.8f, 16, true, true, true), new PropertyLaserArrow());
    public static MSUBowBase shiverburnWing = (MSUBowBase) new MSUBowBase(390, 2.2f, 27, 2.2f, 0.8f, 1, true, "shiverburn_wing", "shiverburnWing").setArrowCheck(REGULAR_ARROWS).addProperties(new PropertyPotionArrow(new PotionEffect(MobEffects.SLOWNESS, 140, 2), 0.8f), new PropertyFlamingArrow(7, 0.9f));
    public static MSUBowBase magneticHookshot = (MSUBowBase) new MSUBowBase(680, 1f, 32, 4f, 0.8f, 1, true, "magnetic_hookshot", "magneticHookshot").setArrowCheck(REGULAR_ARROWS).addProperties(new PropertyHookshot(1, 64));
    public static MSUBowBase wormholePiercer = (MSUBowBase) new MSUBowBase(640, 1, 35, 3.5f, 0.8f, 1, true, "wormhole_piercer", "wormholePiercer").setArrowCheck(REGULAR_ARROWS).addProperties(new PropertyTeleArrows());
    public static MSUBowBase telegravitationalWarper = (MSUBowBase) new MSUBowBase(640, 3, 28, 2.9f, 0.8f, 1, true, "telegravitational_warper", "telegravitationalWarper").setArrowCheck(REGULAR_ARROWS).addProperties(new PropertyHookshot(0.4f, 16, false, false, true), new PropertyGhostArrow());
    public static MSUBowBase crabbow = new MSUBowBase(2048, 7, -1.8, 2.3f, 27, 1.95f, 1, 1, false, "crabbow", "crabbow");
    public static ItemMechanicalCrossbow mechanicalCrossbow = new ItemMechanicalCrossbow(385, 1,"mechanical_crossbow", "mechanicalCrossbow");
    public static MSUBowBase sweetBow = (MSUBowBase) new MSUBowBase(450, 1.8f, 20, 2.1f, 0.9f, 1, true, "sweet_bow", "sweetBow").addProperties(new PropertyCandyWeapon());
    public static MSUBowBase kingOfThePond = (MSUBowBase) new MSUBowBase(890, 2, 10, 5, 1.2f, 1, true, "king_of_the_pond", "kingOfThePond").addProperties(new PropertyFlamingArrow(20, 0.65f));
    public static MSUBowBase gildedGuidance = (MSUBowBase) new MSUBowBase(1210, 3.2f, 30, 0.0f, 0, 2, true, "gilded_guidance", "gildedGuidance").requireNoAmmo().addProperties(new PropertyLaserArrow(), new PropertyGuidedArrow());
    public static MSUBowBase bowOfLight = (MSUBowBase) new MSUBowBase(2050, 4f, 24, 5, 0.3f, 3, true, "bow_of_light", "bowOfLight").requireNoAmmo().addProperties(new PropertyLaserArrow(), new PropertyArrowNoGravity(), new PropertyFlamingArrow(6, 0.9f), new PropertyPierce(0.1f));
    public static MSUBowBase theChancemaker = (MSUBowBase) new MSUBowBase(1280, 1f, 16, 3, 1.3f, 2, false, "the_chancemaker", "theChancemaker").addProperties(new PropertyRandomDamage());
    public static Item wisdomsPierce = new MSUItemBase("wisdom_pierce", "calamityBow").setCreativeTab(TabMinestuckUniverse.weapons);
    public static Item wisdomsHookshot = new MSUItemBase("wisdom_hookshot", "calamityHookshot").setCreativeTab(null);


    //Hammerkind
    public static Item loghammer = new MSUWeaponBase(355, 7, -2.8, 7, "loghammer", "loghammer").setTool(toolHammer, 0, 3.0f).setRepairMaterial("logWood");
    public static Item overgrownLoghammer = new MSUWeaponBase(210, 7, -2.8, 7, "overgrown_loghammer", "overgrownLoghammer").setTool(toolHammer, 0, 3.0f).setRepairMaterial("logWood").addProperties(new PropertyPlantMend());
    public static Item glowingLoghammer = new MSUWeaponBase(310, 7, -2.8, 7, "glowing_loghammer", "glowingLoghammer").setTool(toolHammer, 0, 3.0f).setRepairMaterials(new ItemStack(MinestuckBlocks.glowingLog)).addProperties(new PropertyPotion(new PotionEffect(MobEffects.GLOWING, 200, 0), false, 1));
    public static Item midasMallet = new MSUWeaponBase(415, 6.5D, -2.5D, 15, "midas_mallet", "midasMallet").setTool(toolHammer, 3, 2f).addProperties(new PropertyGristSetter(GristType.Gold));
    public static Item aaaNailShocker = new MSUWeaponBase(325, 7, -2.4, 10,"aaa_nail_shocker", "aaaNailShocker").setTool(toolHammer, 2, 3f).setRepairMaterials(new ItemStack(battery)).addProperties(new PropertyElectric(20, 0, 0.7f, true));
    public static Item highVoltageStormCrusher = new MSUWeaponBase(580, 10, -2.4, 18, "high_voltage_storm_crusher", "highVoltageStormCrusher").setTool(toolHammer, 4, 3.0f).addProperties(new PropertyLightning(8, 1, true, false), new PropertyElectric(60, 8, -1, false));
    public static Item barrelsWarhammer = new MSUWeaponBase(640, 22, -2.4, 18, "barrel_warhammer", "calamityHammer").setTool(toolHammer, 4, 4.0f);
    public static Item stardustSmasher = new MSUWeaponBase(1600, 16, -2.4, 20, "stardust_smasher", "stardustSmasher").setTool(toolHammer, 20, 8.0f).addProperties(new PropertyMobTypeDamage(EnumCreatureAttribute.ARTHROPOD, 1000));

    //Clawkind
    public static Item makeshiftClaws = new MSUWeaponBase(80, 2D, -0.5, 8, "makeshift_claws", "makeshiftClaws").setTool(toolClaws, 1, 5).addProperties(new PropertySweep(), new PropertyDualWield());
    public static Item katars = new MSUWeaponBase(124, 1.5D, -0.65, 8, "katars", "katars").setTool(toolClaws, 2, 3).addProperties(new PropertySweep(), new PropertyDualWield());
    public static Item diamondKatars = new MSUWeaponBase(624, 2.5D, -0.65, 8, "diamond_katars", "diamondKatars").setTool(toolClaws, 2, 3).addProperties(new PropertySweep(), new PropertyDualWield());
    public static Item actionClaws = new ItemDualClaw(280, 3.0D, 0.0D, -1.5D, -1.0D, 6, "actionClaws","action_claws").setTool(toolClaws, 2, 3).addProperties(new PropertyActionBuff(200, 2.5));
    public static Item candyCornClaws = new ItemDualClaw(310, 4.0D, 0.0D, -1.5D, -1.0D, 6, "candyCornClaws","candy_corn_claws").setTool(toolClaws, 2, 3).addProperties(new PropertyCandyWeapon());
    public static Item sneakyDaggers = new ItemDualClaw(322, 3.0D, 0.0D, -1.5D, -1.0D, 7, "sneakyDaggers","sneaky_daggers").setTool(toolClaws, 2, 3);
    public static Item rocketKatars = new MSUWeaponBase(195, 3, -0.5, 8, "rocket_katars", "rocketKatars").setTool(toolClaws, 2, 5).addProperties(new PropertySweep(), new PropertyDualWield(), new PropertyRocketDash(3, 20, 0.3f, 2.5f));
    public static Item blizzardCutters = new ItemDualClaw(325, 4, 0,-0.5, -1, 8, "blizzardCutters", "blizzard_cutters").setTool(toolClaws, 3, 3).addProperties(new PropertyPotion(new PotionEffect(MobEffects.SLOWNESS, 400, 0), false, 0.4f), new PropertyKnockback(2.2f));
    public static Item thunderbirdTalons = new MSUWeaponBase(445, 5,-0.5, 18, "thunderbird_talons", "thunderbirdTalons").setTool(toolClaws, 5, 3).setRepairMaterials(new ItemStack(MinestuckUniverseItems.battery)).addProperties(new PropertySweep(), new PropertyDualWield(), new PropertyShock(10, 3, 0.4f, true), new PropertyKnockback(1.8f));
    public static Item archmageDaggers = new ItemBeamWeapon(830, 6,-0.5, 0.05f, 15, 1, 60, 10, 18, "archmage_daggers", "archmageDaggers").setTool(toolClaws, 5, 3).addProperties(new PropertySweep(), new PropertyDualWield(), new PropertyMagicBeam());
    public static Item katarsOfZillywhomst = new MSUWeaponBase(750, 8,-0.5, 24, "katars_of_zillywhomst", "katarsOfZillywhomst").setTool(toolClaws, 4, 3).addProperties(new PropertySweep(), new PropertyDualWield());

    //Canekind
    public static Item staffOfOvergrowth = new MSUWeaponBase(455, 6.0f, -1.2, 20, "staff_of_overgrowth", "staffOfOvergrowth").setTool(toolCane, 3, 2).addProperties(new PropertyBlockSwap(overgrowthTransforms, 1), new PropertyPotion(new PotionEffect(MobEffects.POISON, 400, 1), false, 0.4f));
    public static Item atomicIrradiator = new MSUWeaponBase(455, 6.0f, -1.2, 20, "atomic_irradiator", "atomicIrradiator").setTool(toolCane,3, 3).addProperties(new PropertyPotion(new PotionEffect(MobEffects.WITHER, 400, 1), true, 0.6f), new PropertyGristSetter(GristType.Uranium));
    public static Item goldCane = new MSUWeaponBase(340, 6.0f, -1.2, 18, "gold_cane", "goldCane").setTool(toolCane, 2, 5).setRepairMaterial("ingotGold");
    public static Item goldenCuestaff = new MSUWeaponBase(780, 10f, -1.2, 32, "golden_cuestaff", "goldenCuestaff").setTool(toolCane, 2, 6);
    public static Item scepterOfZillywuud = new MSUWeaponBase(780, 14f, -1.2, 32, "scepter_of_zillywuud", "scepterOfZillywuud").setTool(toolCane, 4, 3);

    //Clubkind
    public static Item ironClub = new MSUWeaponBase(305, 5, -2.4, 22, "iron_club", "ironClub").setTool(toolClub, 3, 2.0f).addProperties((new PropertySweep()));
    public static Item diamondClub = new MSUWeaponBase(1861, 6, -2.4, 22, "diamond_club", "diamondClub").setTool(toolClub, 3, 2.0f).addProperties((new PropertySweep()));
    public static Item rubyContrabat = new MSUWeaponBase(185, 6.5, -2.2, 22, "ruby_contrabat", "rubyContrabat").setTool(toolClub, 3, 4.0f).addProperties(new PropertySweep(), new PropertyGristSetter(GristType.Ruby));
    public static Item homeRunBat = new MSUWeaponBase(500, 5, -3.9, 10, "home_run_bat", "homeRunBat").setTool(toolClub, 5, 2.0f).addProperties(new PropertySweep(), new PropertyKnockback(15), new PropertySoundOnHit(MSUSoundHandler.homeRunBat, 1, 1.2f));
    public static Item dynamiteStick = new MSUWeaponBase(110, 8, -2.2, 8, "dynamite_stick", "dynamiteStick").setTool(toolClub, 1, 2.0f).addProperties(new PropertySweep(), new PropertyExplode(2.5f, 1, true));
    public static Item nightmareMace = new MSUWeaponBase(295, 6, -2.2, 8, "nightmare_mace", "nightmareMace").setTool(toolClub, 3, 3.0f).addProperties(new PropertySweep(), new PropertyHungry(3, 4, true),
            new PropertyPotion(new PotionEffect(MobEffects.BLINDNESS, 200, 0), false, 0.2f), new PropertyPotion(new PotionEffect(MobEffects.NAUSEA, 200, 0), false, 0.2f), new PropertyPotion(new PotionEffect(MobEffects.WITHER, 100, 1), true, 0.2f));
    public static Item cranialEnder = new MSUWeaponBase(345, 9, -2.2, 8, "cranial_ender", "cranialEnder").setTool(toolClub, 5, 2.0f).addProperties(new PropertySweep(), new PropertyExplode(0.5f, 0.2f, true), new PropertyPotion(new PotionEffect(MobEffects.NAUSEA, 100, 0), true, 0.7f));

    //Dicekind

    //Throwkind
    public static MSUThrowableBase yarnBall = new ItemYarnBall("yarn_ball", "yarnBall");
    public static MSUThrowableBase wizardbeardYarn = new MSUThrowableBase("wizardbeard_yarn", "wizardbeardYarn").addProperties(new PropertyMagicDamagePrjctle(6));
    public static Item dragonCharge = new ItemDragonCharge("dragon_charge", "dragonCharge");
    public static MSUThrowableBase throwingStar = new MSUThrowableBase(8, 0, 16, "throwing_star", "throwingStar");
    public static MSUThrowableBase goldenStar = new MSUThrowableBase(4, 0, 32, "golden_star", "goldenStar");
    public static MSUThrowableBase suitarang = new MSUThrowableBase(8, 0, 16, "suitarang", "suitarang");
    public static MSUThrowableBase psionicStar = new MSUThrowableBase(10, 0, 8, "psionic_star", "psionicStar").setSize(3);
    public static MSUThrowableBase boomerang = new MSUThrowableBase(10, 0, 1, "boomerang", "boomerang");
    public static MSUThrowableBase markedBoomerang = new MSUThrowableBase(10, 0, 1, "marked_boomerang", "markedBoomerang");
    public static MSUThrowableBase redHotRang = new MSUThrowableBase(12, 0, 1, "red_hot_rang", "redHotRang");
    public static MSUThrowableBase tornadoGlaive = new MSUThrowableBase(8, 0, 1, "tornado_glaive", "tornadoGlaive");
    public static MSUThrowableBase hotPotato = new MSUThrowableBase(0, 5, 16, "hot_potato", "hotPotato");

    //Rockkind
    public static MSUThrowableBase pebble = new MSUThrowableBase(0, 0, 64, "pebble", "pebble");
    public static MSUThrowableBase rock = new MSUThrowableBase(10, 5, 16, 5, -2.7, "rock", "rock");
    public static Item bigRock = new ItemBigRock("big_rock", "bigRock");

    //Misc.
    public static Item hereticusAurum = new MSUWeaponBase(620, 7, -2.2, 32, "hereticus_aurum", "hereticusAurum").setTool(toolSickle, 5, 4).addProperties(new PropertySweep(3));
    public static Item gravediggerShovel = new MSUWeaponBase(185, 6, -3, 8, "gravedigger_shovel", "gravediggerShovel").setTool(toolShovel, 3, 4).addProperties(new PropertyMobTypeDamage(EnumCreatureAttribute.UNDEAD, 3));
    public static Item battlesporkOfZillywut = new MSUWeaponBase(769, 12, -2.3, 24, "battlespork_of_zillywut", "battlesporkOfZillywut").setTool(toolSpork, 10, 3);
    public static Item battleaxeOfZillywahoo = new MSUWeaponBase(780, 17, -3, 24, "battleaxe_of_zillywahoo", "battleaxeOfZillywahoo").setTool(toolAxe, 10, 3);
    public static Item battlepickOfZillydew = new MSUWeaponBase(780, 16, -2.8, 24, "battlepick_of_zillydew", "battlepickOfZillydew").setTool(toolPickaxe, 10, 3);
    public static Item rolledUpPaper = new MSUWeaponBase(200, 3, 0, 1, "rolled_up_paper", "rolledUpPaper");
    public static Item yesterdaysNews = new MSUWeaponBase(350, 7, 0, 1, "yesterdays_news", "yesterdaysNews").addProperties(new PropertyPotion(new PotionEffect(MobEffects.MINING_FATIGUE, 200, 1), false, 0.5f), new PropertyPotion(new PotionEffect(MobEffects.SLOWNESS, 200, 1), false, 0.5f), new PropertyFire(3, 0.7f, true));

    //Armor
    public static MSUArmorBase diverHelmet = new ItemDiverHelmet(materialDiverHelmet,0,EntityEquipmentSlot.HEAD,"diverHelmet", "diver_helmet");
    public static MSUArmorBase spikedHelmet = new MSUArmorBase(materialSpikedHelmet,0,EntityEquipmentSlot.HEAD,"spikedDiverHelmet", "spiked_diver_helmet");
    public static MSUArmorBase cruxtruderHat = new MSUArmorBase(materialMetal,0,EntityEquipmentSlot.HEAD,"cruxtruderHelmet", "cruxtruder_helmet");
    public static MSUArmorBase frogHat = new MSUArmorBase(materialCloth,0,EntityEquipmentSlot.HEAD,"frogHat", "frog_hat");
    public static MSUArmorBase wizardHat = new MSUArmorBase(40, materialCloth,0,EntityEquipmentSlot.HEAD,"wizardHat", "wizard_hat");
    public static MSUArmorBase archmageHat = new MSUArmorBase(500, materialCloth,0,EntityEquipmentSlot.HEAD,"archmageHat", "archmage_hat");
    public static MSUArmorBase cozySweater = new ItemWitherproofArmor(60, materialCloth,0,EntityEquipmentSlot.CHEST,"cozySweater", "cozy_sweater");
    public static MSUArmorBase scarf = new ItemScarf(materialCloth,0,EntityEquipmentSlot.HEAD,"scarf", "scarf");
    //public static Item rocketWings = new MSUItemBase("rocket_wings", "rocketWings");
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
    public static Item catclaws = new ItemDualClaw(500, 4.0D, 1.0D, -1.5D, -1.0D, 6, "catClaws",Minestuck.MOD_ID+ ":catclaws").setTool(toolClaws, 2, 1);
    public static Item unbreakableKatana = new MSUWeaponBase(2200, 7, -2.4D, 20, Minestuck.MOD_ID +":unbreakable_katana", "katana")
            .setTool(toolSword, 0, 15.0F).addProperties(new PropertySweep());
    public static Item dice = new MSUItemBase("dice", "dice");
    public static Item fluoriteOctet = new MSUItemBase("fluorite_octet", "fluoriteOctet");

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(catclaws.setRegistryName(Minestuck.MOD_ID, "catclaws"));
        registry.register(unbreakableKatana.setRegistryName(Minestuck.MOD_ID, "unbreakable_katana"));
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
        registerItem(registry, shockerShell);
        registerItem(registry, windshield);
        registerItem(registry, rocketRiotShield);
        registerItem(registry, ejectorShield);
        registerItem(registry, firewall);
        registerItem(registry, obsidianShield);
        registerItem(registry, wallOfThorns);
        registerItem(registry, livingShield);
        registerItem(registry, nuclearNeglector);
        registerItem(registry, perfectAegis);

        registerItem(registry, makeshiftClaws);
        registerItem(registry, katars);
        registerItem(registry, diamondKatars);
        registerCustomRenderedItem(registry, actionClaws);
        registerCustomRenderedItem(registry, candyCornClaws);
        registerCustomRenderedItem(registry, sneakyDaggers);
        registerItem(registry, rocketKatars);
        registerCustomRenderedItem(registry, blizzardCutters);
        registerItem(registry, thunderbirdTalons);
        registerItem(registry, archmageDaggers);
        registerItem(registry, katarsOfZillywhomst);

        registerItem(registry, knittingNeedles);
        registerItem(registry, pointySticks);
        registerItem(registry, boneNeedles);
        registerItem(registry, needlewands);
        registerItem(registry, oglogothThorn);
        registerItem(registry, echidnaQuills);
        registerItem(registry, thistlesOfZillywitch);

        registerItem(registry, flimsyBow);
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

        registerItem(registry, loghammer);
        registerItem(registry, overgrownLoghammer);
        registerItem(registry, glowingLoghammer);
        registerItem(registry, midasMallet);
        registerItem(registry, aaaNailShocker);
        registerItem(registry, highVoltageStormCrusher);
        registerItem(registry, barrelsWarhammer);
        registerItem(registry, stardustSmasher);

        registerItem(registry, gravediggerShovel);
        registerItem(registry, hereticusAurum);
        registerItem(registry, battlesporkOfZillywut);
        registerItem(registry, battleaxeOfZillywahoo);
        registerItem(registry, battlepickOfZillydew);

        registerItem(registry, diamondClub);
        registerItem(registry, ironClub);
        registerItem(registry, rubyContrabat);
        registerItem(registry, homeRunBat);
        registerItem(registry, dynamiteStick);
        registerItem(registry, nightmareMace);
        registerItem(registry, cranialEnder);

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

        registerItem(registry, quantumEntangloporter);
        registerItem(registry, bloodKatana);
        registerItem(registry, trueUnbreakableKatana);
        registerCustomRenderedItem(registry, batteryBeamBlade);
        for(ItemBeamBlade blade : dyedBeamBlade)
            registerCustomRenderedItem(registry, blade);
        registerItem(registry, lightbringer);
        registerItem(registry, cybersword);
        registerItem(registry, crystallineRibbitar);
        registerItem(registry, valorsEdge);

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

        ((IBeamStats)needlewands).setCustomBeamTexture();
        ((IBeamStats)oglogothThorn).setBeamTexture("eldrich_beam");
        ((IBeamStats) gasterBlaster).setBeamTexture("clear_beam");
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
        registerItemCustomRender(blizzardCutters, new MSUModelManager.DualWeaponDefinition("blizzard_cutters_drawn", "blizzard_cutters_sheathed"));

        RenderThrowable.IRenderProperties THROW_STAR_ROTATION = ((entity, partialTicks) ->
        {
            GlStateManager.rotate(90, 1, 0, 0);
            GlStateManager.rotate((entity.ticksExisted+partialTicks) * -(float)Math.PI*10f, 0, 0, 1);
        });

        throwingStar.setRenderProperties(THROW_STAR_ROTATION);
        goldenStar.setRenderProperties(THROW_STAR_ROTATION);
        psionicStar.setRenderProperties(THROW_STAR_ROTATION);
        boomerang.setRenderProperties(THROW_STAR_ROTATION);
        markedBoomerang.setRenderProperties(THROW_STAR_ROTATION);
        redHotRang.setRenderProperties(THROW_STAR_ROTATION);
        tornadoGlaive.setRenderProperties(THROW_STAR_ROTATION);
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
