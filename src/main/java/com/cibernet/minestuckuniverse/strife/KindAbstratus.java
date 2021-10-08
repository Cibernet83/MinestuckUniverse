package com.cibernet.minestuckuniverse.strife;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IClassedTool;
import com.cibernet.minestuckuniverse.items.weapons.MSUToolClass;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
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
	protected boolean preventRightClick = false;

	protected final ArrayList<MSUToolClass> toolClasses = new ArrayList<>();
	protected final ArrayList<Class<? extends Item>> itemClasses = new ArrayList<>();
	protected final ArrayList<Item> toolItems = new ArrayList<>();
	protected final ArrayList<String> keywords = new ArrayList<>();

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

	public KindAbstratus setFist(boolean isFist) {
		this.isFist = isFist;
		return this;
	}

	public boolean preventsRightClick()
	{
		return preventRightClick;
	}

	public KindAbstratus setPreventRightClick(boolean prevents)
	{
		this.preventRightClick = prevents;
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

	public KindAbstratus addKeywords(String... keys)
	{
		for(String i : keys)
			if(i != null && !i.isEmpty() && !this.keywords.contains(i))
				this.keywords.add(i);
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
		boolean result = false;

		if(item instanceof IClassedTool || item instanceof ItemTool)
		for(MSUToolClass tc : toolClasses)
		{
			if((item instanceof IClassedTool && tc.isCompatibleWith(((IClassedTool) item).getToolClass())) || (item instanceof ItemTool && tc.getBaseTools().contains(((ItemTool) item).getToolMaterialName())))
			{
				result = true;
				break;
			}
		}
		if(!result)
		for(Class<? extends Item> clzz : itemClasses)
			if(clzz.isInstance(item))
			{
				result = true;
				break;
			}
		if(!result)
		for(Item i : toolItems)
			if(net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(i), stack, false))
			{
				result = true;
				break;
			}
		if(!result)
			for(String str : keywords)
				if(stack.getItem().getRegistryName().getResourcePath().contains(str))
				{
					result = true;
					break;
				}

		if(conditional != null)
			return conditional.consume(stack.getItem(), stack, result);

		return result;
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

	public boolean isEmpty()
	{
		return !isFist() && conditional == null && toolItems.isEmpty() && toolClasses.isEmpty() && itemClasses.isEmpty();
	}

	public boolean canSelect()
	{
		return !isHidden() && !isEmpty();
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
		boolean consume(Item item, ItemStack stack, boolean originalResult);
	}
}
