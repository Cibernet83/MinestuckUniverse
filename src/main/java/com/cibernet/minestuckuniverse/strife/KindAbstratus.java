package com.cibernet.minestuckuniverse.strife;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IClassedTool;
import com.cibernet.minestuckuniverse.items.weapons.MSUToolClass;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = MinestuckUniverse.MODID)
public class KindAbstratus extends IForgeRegistryEntry.Impl<KindAbstratus> implements Comparable<KindAbstratus>
{
	public static ForgeRegistry<KindAbstratus> REGISTRY;

	private static int idAt = 0;
	protected final int ID;
	protected String unlocName;
	protected boolean hidden = false;
	protected boolean isFist = false;

	protected final ArrayList<MSUToolClass> toolClasses = new ArrayList<>();
	protected final ArrayList<Class<? extends Item>> itemClasses = new ArrayList<>();
	protected final ArrayList<Item> toolItems = new ArrayList<>();

	protected IAbstratusConditional conditional;

	public KindAbstratus(String unlocName, MSUToolClass... toolClasses)
	{
		ID = idAt++;

		setUnlocalizedName(unlocName);
		addToolClasses(toolClasses);
	}

	public boolean isHidden() {
		return hidden;
	}

	public KindAbstratus setHidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}
	public boolean isFist() {
		return isFist;
	}

	public KindAbstratus setFist(boolean hidden) {
		this.isFist = hidden;
		return this;
	}

	public KindAbstratus setConditional(IAbstratusConditional condition)
	{
		this.conditional = condition;
		return this;
	}

	public IAbstratusConditional getConditional()
	{
		return conditional;
	}

	public KindAbstratus addToolClasses(MSUToolClass... toolClasses)
	{
		for(MSUToolClass tc : toolClasses)
			if(tc != null && !this.toolClasses.contains(tc))
				this.toolClasses.add(tc);
		return this;
	}

	public KindAbstratus addItemClasses(Class<? extends Item>... itemClasses)
	{
		for(Class<? extends Item> ic : itemClasses)
			if(ic != null && !this.itemClasses.contains(ic))
				this.itemClasses.add(ic);
		return this;
	}

	public KindAbstratus addItemTools(Item... items)
	{
		for(Item i : items)
			if(i != null && !this.toolItems.contains(i))
				this.toolItems.add(i);
		return this;
	}

	public ArrayList<Class<? extends Item>> getItemClasses() {
		return itemClasses;
	}

	public ArrayList<Item> getToolItems() {
		return toolItems;
	}

	public ArrayList<MSUToolClass> getToolClasses() {
		return toolClasses;
	}

	public boolean isStackCompatible(ItemStack stack)
	{
		if(stack.isEmpty())
			return false;

		Item item = stack.getItem();

		if(item instanceof IClassedTool)
			for(MSUToolClass tc : toolClasses)
				if(tc.isCompatibleWith(((IClassedTool) item).getToolClass()))
					return true;
		for(Class<? extends Item> clzz : itemClasses)
			if(clzz.isInstance(item))
				return true;
		for(Item i : toolItems)
			if(net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(i), stack, false))
				return true;

		if(conditional != null && conditional.consume(stack.getItem(), stack))
			return true;

		return false;
	}

	public String getUnlocalizedName() {
		return "kindAbstratus."+unlocName;
	}

	public String getLocalizedName()
	{
		return I18n.translateToLocal(I18n.translateToLocal(this.getUnlocalizedName()) + ".name").trim();
	}

	public String getDisplayName()
	{
		String name = getLocalizedName().toLowerCase();

		if(name.length() > 12)
			name = name.substring(0, 9) + "...";

		return name;
	}

	public KindAbstratus setUnlocalizedName(String unlocName) {
		this.unlocName = unlocName;
		return this;
	}

	@Override
	public int compareTo(KindAbstratus o) {
		return this.ID - o.ID;
	}

	public boolean canSelect()
	{
		return !isHidden() && (conditional != null || isFist() || !toolItems.isEmpty() || !toolClasses.isEmpty() || !itemClasses.isEmpty());
	}

	@SubscribeEvent
	public static void registerRegistry(RegistryEvent.NewRegistry event)
	{
		KindAbstratus.REGISTRY = (ForgeRegistry)(new RegistryBuilder()).setName(new ResourceLocation(MinestuckUniverse.MODID, "kind_abstrata"))
				.setType(KindAbstratus.class).setDefaultKey(new ResourceLocation(MinestuckUniverse.MODID)).create();
	}

	@Override
	public String toString() {
		return getRegistryName().toString();
	}

	public static interface IAbstratusConditional
	{
		boolean consume(Item item, ItemStack stack);
	}
}
