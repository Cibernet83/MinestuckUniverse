package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.blocks.BlockCustomTransportalizer;
import com.cibernet.minestuckuniverse.enchantments.MSUEnchantments;
import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import com.cibernet.minestuckuniverse.client.models.armor.*;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockTransportalizer;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.item.block.ItemTransportalizer;
import com.mraof.minestuck.item.weapon.ItemWeapon;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

public class MinestuckUniverseItems
{
    public static ArrayList<Block> itemBlocks = new ArrayList<>();

    private static final EnumClass[] classes = EnumClass.values();
    private static final EnumAspect[] aspects = EnumAspect.values();
    //Armor Materials
    public static ItemArmor.ArmorMaterial materialDiverHelmet = EnumHelper.addArmorMaterial("DIVER_HELMET", MinestuckUniverse.MODID+":diver_helmet", 120, new int[] {0, 0, 0, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static ItemArmor.ArmorMaterial materialSpikedHelmet = EnumHelper.addArmorMaterial("SPIKED_HELMET", MinestuckUniverse.MODID+":spiked_diver_helmet", 230, new int[] {0, 0, 0, 6}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static ItemArmor.ArmorMaterial materialCloth = EnumHelper.addArmorMaterial("CLOTH", MinestuckUniverse.MODID+":cloth", -1, new int[] {0, 0, 0, 0}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);

    //GT Armor
    public static ItemArmor.ArmorMaterial[][] GTArmorMaterial = new ItemArmor.ArmorMaterial[classes.length][aspects.length];
    public static Item[][][] GTArmor = new Item[classes.length][aspects.length][4];

    public static MSUToolClass toolSword = new MSUToolClass(Material.WEB).addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolGauntlet = new MSUToolClass(Material.GLASS, Material.ICE, Material.PACKED_ICE).addEnchantments(Enchantments.SILK_TOUCH, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, MSUEnchantments.SUPERPUNCH);
    public static MSUToolClass toolNeedles = new MSUToolClass(Material.CLOTH).addEnchantments(EnumEnchantmentType.WEAPON);

    //Items
    public static Item spaceSalt = new ItemSpaceSalt();
    public static Item moonstone = new MSUItemBase("moonstone");
    public static Item moonstoneChisel = new ItemChisel("moonstone", 31);
    public static Item zillystoneShard = new MSUItemBase("zillystone_shard", "zillystoneShard");
    public static Item dungeonKey = new MSUItemBase("dungeon_key", "dungeonKey");
    public static Item yarnBall = new ItemYarnBall("yarn_ball", "yarnBall");

    //Ghost Items
    public static Item returnNode = new ItemGhostBlock("return_node_ghost_item", MinestuckBlocks.returnNode);
    public static Item travelGate = new ItemGhostBlock("travel_gate_ghost_item", MinestuckBlocks.gate);
    public static Item netherPortal = new ItemGhostBlock("nether_portal_ghost_item", Blocks.PORTAL);
    public static Item endPortal = new ItemGhostBlock("end_portal_ghost_item", Blocks.END_PORTAL);
    public static Item endGateway = new ItemGhostBlock("end_gateway_ghost_item", Blocks.END_GATEWAY);

    //Medallions
    public static Item ironMedallion = new MSUItemBase("iron_medallion", "ironMedallion").setMaxStackSize(1);
    public static Item returnMedallion = new ItemWarpMedallion("returnMedallion", "return_medallion", ItemWarpMedallion.EnumTeleportType.RETURN, 80);
    public static Item teleportMedallion = new ItemWarpMedallion("teleportMedallion", "teleport_medallion", ItemWarpMedallion.EnumTeleportType.TRANSPORTALIZER, 80);
    public static Item skaianMedallion = new ItemWarpMedallion("skaianMedallion", "skaian_medallion", ItemWarpMedallion.EnumTeleportType.SKAIA, 80);

    //Weapons
    public static Item trueUnbreakableKatana = (new MSUWeaponBaseSweep(-1, 7.0D, -2.35D, 20, "true_unbreakable_katana", "unbreakableKatana", Item.ToolMaterial.IRON.toString())).setTool(toolSword, 0, 15.0F);
    public static Item battery = new MSUItemBase("battery", "battery");
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

    public static Item fancyGlove = new MSUWeaponBase(50, 0D, 0, 5, "fancy_glove", "fancyGlove").setTool(toolGauntlet, 0, 1);
    public static Item spikedGlove = new MSUWeaponBase(95, 3.5D, 0.25D, 8, "spiked_glove", "spikedGlove").setTool(toolGauntlet, 1, 1.4F);
    public static Item cobbleBasher = new MSUWeaponBase(175, 4D, -1.8D, 4, "cobble_basher","cobbleBasher").setTool(toolGauntlet, 1, 4F);
    public static Item fluoriteGauntlet = new ItemRandomWeapon(980, 7D, -0.3D,  8, "fluorite_gauntlet", "fluoriteGauntlet").setTool(toolGauntlet, 1, 2.4F);
    public static Item goldenGenesisGauntlet = new MSUWeaponBase(1256, 11D, -0.25D, 15, "golden_genesis_gauntlet","goldenGenesisGauntlet").setTool(toolGauntlet, 1, 3F);
    public static Item pogoFist = new ItemPogoWeapon(700, 7.0D, -0.3, 8, "pogo_fist", "pogoFist", 0.55D).setTool(toolGauntlet, 1, 2F);
    public static Item rocketFist = new MSUWeaponBase(124, 3D, 0.4D, 6, "rocket_powered_fist", "rocketFist").setTool(toolGauntlet, 1, 1.6F);

    public static Item knittingNeedles = new ItemKnittingNeedles(32,2, 1, 1, "knitting_needle", "knittingNeedle").setTool(toolNeedles, 2, 1f);
    public static Item pointySticks = new ItemPluralWeapon(50,2, 1, 1, "pointy_stick", "pointyStick").setTool(toolNeedles, 1, 1f);
    public static Item boneNeedles = new ItemPluralWeapon(100,4, 0, 10, "bone_needle", "boneNeedle").setTool(toolNeedles, 1, 1f);
    public static Item needlewands = new ItemPluralWeapon(250,4, 0.5, 60, "needlewand", "needlewand").setTool(toolNeedles, 3, 2f);
    public static Item oglogothThorn = new ItemPluralWeapon(366,5.6, -0.5, 80, "thorn_of_oglogoth", "oglogothThorn").setTool(toolNeedles, 4, 3f);
    public static Item echidnaQuills = new ItemPluralWeapon(5, 1, 100, "quill_of_echidna", "echidnaQuill").setTool(toolNeedles, 5, 5f);

    //Armor
    public static MSUArmorBase diverHelmet = new ItemDiverHelmet(materialDiverHelmet,0,EntityEquipmentSlot.HEAD,"diverHelmet", "diver_helmet");
    public static MSUArmorBase spikedHelmet = new MSUArmorBase(materialSpikedHelmet,0,EntityEquipmentSlot.HEAD,"spikedDiverHelmet", "spiked_diver_helmet");
    public static MSUArmorBase frogHat = new MSUArmorBase(materialCloth,0,EntityEquipmentSlot.HEAD,"frogHat", "frog_hat");
    public static MSUArmorBase wizardHat = new MSUArmorBase(40, materialCloth,0,EntityEquipmentSlot.HEAD,"wizardHat", "wizard_hat");
    public static MSUArmorBase cozySweater = new ItemWitherproofArmor(60, materialCloth,0,EntityEquipmentSlot.CHEST,"cozySweater", "cozy_sweater");
    //public static MSUArmorBase scarf = new ItemDiverHelmet(materialCloth,0,EntityEquipmentSlot.HEAD,"scarf", "scarf");

    //Overrides
    public static MSUArmorBase crumplyHat = new MSUArmorBase(materialCloth, 0, EntityEquipmentSlot.HEAD, "crumplyHat", Minestuck.MOD_ID+":crumply_hat");
    public static Item catclaws = new ItemDualClaw(500, 4.0D, 1.0D, -1.5D, -1.0D, 6, "catclaws",Minestuck.MOD_ID+ ":catclaws");
    public static Item unbreakableKatana = new MSUWeaponBaseSweep(2200, 7, -2.4D, 20, Minestuck.MOD_ID +":unbreakable_katana", "katana", Item.ToolMaterial.IRON.toString())
            .setTool(toolSword, 0, 15.0F).setCreativeTab(TabMinestuck.instance);
    public static Item captcharoidCamera = new ItemCaptcharoidOverride();



    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(catclaws.setRegistryName(Minestuck.MOD_ID, "catclaws"));
        registry.register(unbreakableKatana.setRegistryName(Minestuck.MOD_ID, "unbreakable_katana"));
	    registry.register(captcharoidCamera.setRegistryName(Minestuck.MOD_ID, "captcharoid_camera"));
	    registry.register(crumplyHat.setRegistryName(Minestuck.MOD_ID, "crumply_hat"));

        registerItem(registry, moonstone);
        registerItem(registry, moonstoneChisel);
        registerItem(registry, zillystoneShard);
        registerItem(registry, battery);
        registerItem(registry, yarnBall);
        registerItem(registry, spaceSalt);

        registerItem(registry, returnNode);
        registerItem(registry, travelGate);
        registerItem(registry, endPortal);
        registerItem(registry, netherPortal);
        registerItem(registry, endGateway);

        registerItem(registry, diverHelmet);
        registerItem(registry, spikedHelmet);
        registerItem(registry, frogHat);
        registerItem(registry, wizardHat);
        registerItem(registry, cozySweater);

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

        registerItem(registry, knittingNeedles);
        registerItem(registry, pointySticks);
        registerItem(registry, boneNeedles);
        registerItem(registry, needlewands);
        registerItem(registry, oglogothThorn);
        registerItem(registry, echidnaQuills);

        registerItem(registry, trueUnbreakableKatana);
        registerCustomRenderedItem(registry, batteryBeamBlade);
        for(ItemBeamBlade blade : dyedBeamBlade)
            registerCustomRenderedItem(registry, blade);

        registerItem(registry, dungeonKey);

        //Blocks
        registerItemBlocks(registry);

        //Item Mods

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(yarnBall, new BehaviorProjectileDispense()
        {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                ItemStack thrownStack = stackIn.copy();
                thrownStack.setCount(1);
                return new EntityMSUThrowable(worldIn, position.getX(), position.getY(), position.getZ(), thrownStack);
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public static void setClientsideVariables()
    {
        diverHelmet.setArmorModel(new ModelDiverHelmet());
        spikedHelmet.setArmorModel(new ModelSpikedHelmet());
        frogHat.setArmorModel(new ModelFrogHat());
        wizardHat.setArmorModel(new ModelWizardHat());
        crumplyHat.setArmorModel(new ModelCrumplyHat());

        for(ItemBeamBlade blade : dyedBeamBlade)
            registerItemCustomRender(blade, new MSUModelManager.DualWeaponDefinition("dyed_battery_beam_blade", "dyed_battery_beam_blade_off"));
        registerItemCustomRender(batteryBeamBlade, new MSUModelManager.DualWeaponDefinition("battery_beam_blade", "battery_beam_blade_off"));
        registerItemCustomRender(yarnBall, new MSUModelManager.DyedItemDefinition("yarn_ball"));
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
