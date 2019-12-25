package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.entity.models.armor.ModelDiverHelmet;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.weapon.ItemWeapon;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.block.Block;
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
    //Armor Materials
    public static ItemArmor.ArmorMaterial materialDiverHelmet = EnumHelper.addArmorMaterial("DIVER_HELMET", MinestuckUniverse.MODID+":diver_helmet", 50, new int[] {0, 0, 0, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);

    //GT Armor
    public static ItemArmor.ArmorMaterial[][] GTArmorMaterial = new ItemArmor.ArmorMaterial[classes.length][aspects.length];
    public static Item[][][] GTArmor = new Item[classes.length][aspects.length][4];

    //Items
    public static Item spaceSalt = new ItemSpaceSalt();
    public static Item moonstone = new MSUItemBase("moonstone");
    public static Item moonstoneChisel = new ItemChisel("moonstone", 31);
    public static Item zillystoneShard = new MSUItemBase("zillystone_shard", "zillystoneShard");

    public static Item murica = new ItemSound("murica", "murica", MSUSoundHandler.murica).setCreativeTab(null);
    public static Item muricaSouth = new ItemSound("murica_south", "muricaSouth", MSUSoundHandler.murica_south).setCreativeTab(null);

    //Weapons
    public static Item trueUnbreakableKatana = (new MSUWeaponBase(-1, 7.0D, -2.35D, 20, "true_unbreakable_katana", "unbreakableKatana")).setTool("sword", 0, 15.0F);

    //Armor
    public static MSUArmorBase diverHelmet = new MSUArmorBase(materialDiverHelmet,0,EntityEquipmentSlot.HEAD,"diverHelmet", "diver_helmet");

    //Overrides
    public static Item unbreakableKatana = new ItemWeapon(2200, 7, -2.4D, 20, "katana").setTool("sword", 0, 15.0F).setRegistryName(Minestuck.MOD_ID, "unbreakable_katana");

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        registerItem(registry, spaceSalt);
        registerItem(registry, moonstone);
        registerItem(registry, moonstoneChisel);
        registerItem(registry, zillystoneShard);

        registerItem(registry, trueUnbreakableKatana);

        registerItem(registry, diverHelmet);

        registry.register(unbreakableKatana);

        registerItem(registry, murica);
        registerItem(registry, muricaSouth);

        //registerGTArmor(registry);

        //Blocks
        registerItemBlocks(registry);

        //Item Mods


    }

    @SideOnly(Side.CLIENT)
    public static void setClientsideVariables()
    {
        diverHelmet.setArmorModel(new ModelDiverHelmet());
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
