package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.entity.models.armor.ModelDiverHelmet;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
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

    //Tool Classes
    public static MSUToolClass toolSword = new MSUToolClass(Material.WEB).addEnchantments(EnumEnchantmentType.WEAPON);
    public static MSUToolClass toolGauntlet = new MSUToolClass(Material.GLASS, Material.ICE, Material.PACKED_ICE);

    //GT Armor
    public static ItemArmor.ArmorMaterial[][] GTArmorMaterial = new ItemArmor.ArmorMaterial[classes.length][aspects.length];
    public static Item[][][] GTArmor = new Item[classes.length][aspects.length][4];

    //Items
    public static Item spaceSalt = new ItemSpaceSalt();
    public static Item moonstone = new MSUItemBase("moonstone");
    public static Item moonstoneChisel = new ItemChisel("moonstone", 31);
    public static Item zillystoneShard = new MSUItemBase("zillystone_shard", "zillystoneShard");

    //Weapons
    public static Item trueUnbreakableKatana = new MSUWeaponBase(7, -2.4D, 20, "true_unbreakable_katana", "unbreakableKatana").setTool(toolSword, 0, 15.0F);

    public static Item fancyGlove = new MSUWeaponBase(50, 0D, 0, 5, "fancy_glove", "fancyGlove").setTool(toolGauntlet, 0, 0);
    public static Item spikedGlove = new MSUWeaponBase(95, 3.5D, 0.25D, 8, "spiked_glove", "spikedGlove").setTool(toolGauntlet, 0, 0.5F);
    public static Item cobbleBasher = new MSUWeaponBase(175, 4D, -1.8D, 4, "cobble_basher","cobbleBasher").setTool(toolGauntlet, 0, 1.2F);
    public static Item fluoriteGauntlet = new ItemRandomWeapon(980, 7D, -0.3D,  8, "fluorite_gauntlet", "fluoriteGauntlet").setTool(toolGauntlet, 0, 2.4F);
    public static Item goldenGenesisGauntlet = new MSUWeaponBase(1256, 11D, -0.25D, 15, "golden_genesis_gauntlet","goldenGenesisGauntlet").setTool(toolGauntlet, 0, 3F);
    public static Item pogoFist = new ItemPogoWeapon(700, 7.0D, -0.3, 8, "pogo_fist", "pogoFist", 0.55D).setTool(toolGauntlet, 0, 1.4F);
    public static Item rocketFist = new MSUWeaponBase(124, 3D, 0.4D, 6, "rocket_powered_fist", "rocketFist").setTool(toolGauntlet, 0, 0.5F);
    public static Item ctd = new Item().setRegistryName("ctd").setUnlocalizedName("ctd").setCreativeTab(TabMinestuckUniverse.instance);
    public static Item cht = new Item().setRegistryName("cht").setUnlocalizedName("cht").setCreativeTab(TabMinestuckUniverse.instance);
    public static Item chc = new Item().setRegistryName("chc").setUnlocalizedName("chc").setCreativeTab(TabMinestuckUniverse.instance);
    public static Item plm = new Item().setRegistryName("plm").setUnlocalizedName("plm").setCreativeTab(TabMinestuckUniverse.instance);

    //Armor

    //Overrides

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        registerItem(registry, spaceSalt);
        registerItem(registry, moonstone);
        registerItem(registry, moonstoneChisel);
        registerItem(registry, zillystoneShard);

        registerItem(registry, trueUnbreakableKatana);

        registerItem(registry, fancyGlove);
        registerItem(registry, spikedGlove);
        registerItem(registry, cobbleBasher);
        registerItem(registry, fluoriteGauntlet);
        registerItem(registry, goldenGenesisGauntlet);
        registerItem(registry, pogoFist);
        registerItem(registry, rocketFist);

        registerItem(registry, ctd);
        registerItem(registry, cht);
        registerItem(registry, chc);
        registerItem(registry, plm);

        //registerGTArmor(registry);

        //Blocks
        registerItemBlocks(registry);

        //Item Mods


    }

    @SideOnly(Side.CLIENT)
    public static void setClientsideVariables()
    {
    }

    public static void registerGTArmor(IForgeRegistry<Item> registry)
    {
        /*
        ModelBiped[] models = new ModelBiped[] {new ModelGTKnight()};
        EntityEquipmentSlot[] slots = new EntityEquipmentSlot[] {EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD};
        for(int cls = 0; cls < classes.length; cls++)
            for(int asp = 0; asp < aspects.length; asp++)
            {
                String name = classes[cls].toString()+"_"+aspects[asp].toString();
                String unlocName = classes[cls].toString() + "." + aspects[asp].toString();
                GTArmorMaterial[cls][asp] = EnumHelper.addArmorMaterial(name.toUpperCase(), MinestuckUniverse.MODID + ":godtier/" + name, 1,
                        new int[] {4,4,4,4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f);
                for(int piece = 0; piece < 4; piece++)
                {
                    EntityEquipmentSlot slot = slots[piece];
                    GTArmor[cls][asp][piece] = new MSUArmorBase(GTArmorMaterial[cls][asp],0,
                            slot, unlocName + "." + slot.toString().toLowerCase(),"gt_" + name  + "_" + slot.toString().toLowerCase()
                            );//.setCreativeTab(TabMinestuckUniverse.GTArmor);
                    registerItem(registry, GTArmor[cls][asp][piece]);
                }
            }
            */
    }

    private static Item registerItem(IForgeRegistry<Item> registry, Item item)
    {
        registry.register(item);
        MSUModelManager.items.add(item);
        return item;
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry)
    {
        for(Block block : itemBlocks)
        {
            ItemBlock item = new ItemBlock(block);
            registerItem(registry, item.setRegistryName(item.getBlock().getRegistryName()));
        }
    }
}
