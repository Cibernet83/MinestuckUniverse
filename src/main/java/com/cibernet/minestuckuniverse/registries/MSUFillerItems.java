package com.cibernet.minestuckuniverse.registries;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.entity.models.armor.ModelDiverHelmet;
import com.cibernet.minestuckuniverse.items.ItemSound;
import com.cibernet.minestuckuniverse.items.MSUArmorBase;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.mraof.minestuck.item.weapon.ItemWeapon;
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

public class MSUFillerItems
{
	//Armor Materials
	public static ItemArmor.ArmorMaterial materialDiverHelmet = EnumHelper.addArmorMaterial("DIVER_HELMET", MinestuckUniverse.MODID+":diver_helmet", 50, new int[] {0, 0, 0, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	public static ItemArmor.ArmorMaterial materialSpikedHelmet = EnumHelper.addArmorMaterial("SPIKED_DIVER_HELMET", MinestuckUniverse.MODID+":spiked_diver_helmet", 70, new int[] {0, 0, 0, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.5F);
	public static ItemArmor.ArmorMaterial materialFroghat = EnumHelper.addArmorMaterial("FROG_HAT", MinestuckUniverse.MODID+":froghat", 5, new int[] {0,0,0,2}, 8, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
	
	//Items
	public static Item murica = new ItemSound("murica", "murica", MSUSoundHandler.murica);
	public static Item muricaSouth = new ItemSound("murica_south", "muricaSouth", MSUSoundHandler.murica_south);
	public static Item CHS = new MSUItemBase("cruxaclysmic_heartstopper", "cruxaclysmicHeartStopper");
	public static Item cruxatonicDerringer = new MSUItemBase("cruxatonic_derringer,", "cruxatonicDerringer");

	//Armor
	public static MSUArmorBase diverHelmet = new MSUArmorBase(materialDiverHelmet,0, EntityEquipmentSlot.HEAD,"diverHelmet", "diver_helmet");
	public static MSUArmorBase spikedDiverHelmet = new MSUArmorBase(materialSpikedHelmet, 0, EntityEquipmentSlot.HEAD, "spikedHelmet", "spiked_diver_helmet");
	public static MSUArmorBase froghat = new MSUArmorBase(materialFroghat, 0, EntityEquipmentSlot.HEAD, "froghat", "froghat");
	
	//Weapons
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		TabMinestuckUniverse.fillerItems = new TabMinestuckUniverse("tabMSUFillerItems");
		IForgeRegistry<Item> registry = event.getRegistry();
		
		registerItem(registry, diverHelmet);
		registerItem(registry, spikedDiverHelmet);
		registerItem(registry, froghat);
		
		
		registerItem(registry, murica);
		registerItem(registry, muricaSouth);
		registerItem(registry, CHS);
	}
	
	@SideOnly(Side.CLIENT)
	public static void setClientsideVariables()
	{
		diverHelmet.setArmorModel(new ModelDiverHelmet());
		//spikedDiverHelmet.setArmorModel(new ModelSpikedHelmet());
		//froghat.setArmorModel(new ModelFroghat());
	}
	
	private static Item registerItem(IForgeRegistry<Item> registry, Item item)
	{
		item.setCreativeTab(TabMinestuckUniverse.fillerItems);
		registry.register(item);
		MSUModelManager.items.add(item);
		return item;
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry)
	{
		for(Block block : MinestuckUniverseItems.itemBlocks)
		{
			ItemBlock item = new ItemBlock(block);
			registerItem(registry, item.setRegistryName(item.getBlock().getRegistryName()));
		}
	}
}
