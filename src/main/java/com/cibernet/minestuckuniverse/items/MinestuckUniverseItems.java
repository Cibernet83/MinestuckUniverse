package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MSUModelManager;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.entity.models.armor.ModelDiverHelmet;
import com.cibernet.minestuckuniverse.entity.models.armor.ModelFroghat;
import com.cibernet.minestuckuniverse.entity.models.armor.ModelGTKnight;
import com.cibernet.minestuckuniverse.entity.models.armor.ModelSpikedHelmet;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.block.ItemAlchemiter;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Iterator;

import static com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks.*;

public class MinestuckUniverseItems
{
    public static ArrayList<Block> itemBlocks = new ArrayList<>();
    
    private static final EnumClass[] classes = EnumClass.values();
    private static final EnumAspect[] aspects = EnumAspect.values();
    //Armor Materials
    public static ItemArmor.ArmorMaterial materialDiverHelmet = EnumHelper.addArmorMaterial("DIVER_HELMET", MinestuckUniverse.MODID+":diver_helmet", 50, new int[] {0, 0, 0, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
    public static ItemArmor.ArmorMaterial materialSpikedHelmet = EnumHelper.addArmorMaterial("SPIKED_DIVER_HELMET", MinestuckUniverse.MODID+":spiked_diver_helmet", 70, new int[] {0, 0, 0, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.5F);
    public static ItemArmor.ArmorMaterial materialFroghat = EnumHelper.addArmorMaterial("FROG_HAT", MinestuckUniverse.MODID+":froghat", 5, new int[] {0,0,0,2}, 8, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);


    //GT Armor
    public static ItemArmor.ArmorMaterial[][] GTArmorMaterial = new ItemArmor.ArmorMaterial[classes.length][aspects.length];
    public static Item[][][] GTArmor = new Item[classes.length][aspects.length][4];

    //Items
    public static Item spaceSalt = new ItemSpaceSalt();
    public static Item moonstone = new MSUItemBase("moonstone");
    public static Item moonstoneChisel = new ItemChisel("moonstone", 31);
    public static Item zillystoneShard = new MSUItemBase("zillystone_shard", "zillystoneShard");

    //Armor
    public static MSUArmorBase diverHelmet = new MSUArmorBase(materialDiverHelmet,0,EntityEquipmentSlot.HEAD,"diverHelmet", "diver_helmet");
    public static MSUArmorBase spikedDiverHelmet = new MSUArmorBase(materialSpikedHelmet, 0, EntityEquipmentSlot.HEAD, "spikedHelmet", "spiked_diver_helmet");
    public static MSUArmorBase froghat = new MSUArmorBase(materialFroghat, 0, EntityEquipmentSlot.HEAD, "froghat", "froghat");

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        registerItem(registry, spaceSalt);
        registerItem(registry, moonstone);
        registerItem(registry, moonstoneChisel);
        registerItem(registry, zillystoneShard);

        registerItem(registry, diverHelmet);
        registerItem(registry, spikedDiverHelmet);
        registerItem(registry, froghat);

        //registerGTArmor(registry);

        //Blocks
        registerItemBlocks(registry);
        
        //Item Mods
        
        
    }

    public static void setClientsideVariables()
    {
        diverHelmet.setArmorModel(new ModelDiverHelmet());
        spikedDiverHelmet.setArmorModel(new ModelSpikedHelmet());
        froghat.setArmorModel(new ModelFroghat());
    }
    
    public static void registerGTArmor(IForgeRegistry<Item> registry)
    {
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
                            slot, unlocName + "." + slot.toString().toLowerCase(),"gt_" + name  + "_" + slot.toString().toLowerCase(),
                            models[cls % models.length]);//.setCreativeTab(TabMinestuckUniverse.GTArmor);
                    registerItem(registry, GTArmor[cls][asp][piece]);
                }
            }
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
