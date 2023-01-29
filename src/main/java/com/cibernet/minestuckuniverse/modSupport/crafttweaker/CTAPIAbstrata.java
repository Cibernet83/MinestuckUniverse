package com.cibernet.minestuckuniverse.modSupport.crafttweaker;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.strife.KindAbstratus;
import com.mraof.minestuck.modSupport.crafttweaker.Alchemy;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass("minestuckuniverse.KindAbstrata")
@ZenRegister
public class CTAPIAbstrata
{
	protected static List<IAction> actions = new ArrayList<>();

	public CTAPIAbstrata()
	{
	}

	@ZenMethod
	public static void addItemToAbstratus(String abstratus, IItemStack ingredient)
	{
		actions.add(new AddItem(abstratus, Mode.ADD, ingredient));
	}

	@ZenMethod
	public static void removeItemFromAbstratus(String abstratus, IItemStack ingredient)
	{
		actions.add(new AddItem(abstratus, Mode.REMOVE, ingredient));
	}

	@ZenMethod
	public static void addKeywordToAbstratus(String abstratus, String keyword)
	{
		actions.add(new AddKeyword(abstratus, Mode.ADD, keyword));
	}

	@ZenMethod
	public static void removeKeywordFromAbstratus(String abstratus, String keyword)
	{
		actions.add(new AddKeyword(abstratus, Mode.REMOVE, keyword));
	}

	@ZenMethod
	public static void makeAbstratusHidden(String abstratus, boolean hidden)
	{
		actions.add(new HideAbstratus(abstratus, hidden));
	}

	@ZenMethod
	public static void preventUnallocatedInteraction(String abstratus, boolean hidden)
	{
		actions.add(new DisableInteraction(abstratus, hidden));
	}

	@ZenMethod
	public static void addAbstratus(String abstratus)
	{
		actions.add(new AddAbstratus(abstratus));
	}

	@ZenMethod
	public static void printAbstrata()
	{
		actions.add(new PrintIds());
	}

	private static KindAbstratus getAbstratus(String id)
	{
		if(!id.contains(":"))
			id = MinestuckUniverse.MODID + ":" + id;

		KindAbstratus res = KindAbstratus.REGISTRY.getValue(new ResourceLocation(id));
		if(res == null)
			CraftTweakerAPI.logError("Kind Abstratus " + id + " does not exist. Use the printAbstrata() function to get a list of all ids");
		return res;
	}

	private static class AddItem implements IAction
	{
		final String abstratus;
		final Mode mode;
		final IIngredient items;

		public AddItem(String abstratus, Mode mode, IIngredient items)
		{
			this.abstratus = abstratus;
			this.mode = mode;
			this.items = items;
		}

		@Override
		public void apply()
		{
			KindAbstratus abstratus = getAbstratus(this.abstratus);
			if(abstratus == null)
				return;

			if(mode == Mode.ADD)
				for(IItemStack iStack : items.getItemArray())
					abstratus.addItemTools(((ItemStack)iStack.getInternal()).getItem());
			else for(IItemStack iStack : items.getItemArray())
			{
				abstratus.getToolItems().remove(((ItemStack)iStack.getInternal()).getItem());
				abstratus.getItemBlacklist().add(((ItemStack)iStack.getInternal()).getItem());
			}
		}

		@Override
		public String describe() {
			return mode == Mode.ADD ? ("adding " + items.getItemArray().length + " item entries to " + abstratus) : ("removing " + items.getItemArray().length + " item entries from " + abstratus);
		}
	}

	private static class AddKeyword implements IAction
	{
		final String abstratus;
		final Mode mode;
		final String key;

		public AddKeyword(String abstratus, Mode mode, String key)
		{
			this.abstratus = abstratus;
			this.mode = mode;
			this.key = key;
		}

		@Override
		public void apply()
		{
			KindAbstratus abstratus = getAbstratus(this.abstratus);
			if(abstratus == null)
				return;

			if(mode == Mode.ADD)
				abstratus.addKeywords(key);
			else abstratus.getKeywords().remove(key);
		}

		@Override
		public String describe() {
			return mode == Mode.ADD ? ("adding keyword " + key + " to " + abstratus) : ("removing keyword" + key + " from " + abstratus);
		}
	}

	private static class HideAbstratus implements IAction
	{
		final String abstratus;
		final boolean hide;

		public HideAbstratus(String abstratus, boolean hide)
		{
			this.abstratus = abstratus;
			this.hide = hide;
		}

		@Override
		public void apply()
		{
			KindAbstratus abstratus = getAbstratus(this.abstratus);
			if(abstratus == null)
				return;

			abstratus.setHidden(hide);
		}

		@Override
		public String describe() {
			return hide ? ("hiding " + abstratus + " from the strife card selection list") : ("adding " + abstratus + " to the strife card selection list");
		}
	}

	private static class DisableInteraction implements IAction
	{
		final String abstratus;
		final boolean disable;

		public DisableInteraction(String abstratus, boolean disable)
		{
			this.abstratus = abstratus;
			this.disable = disable;
		}

		@Override
		public void apply()
		{
			KindAbstratus abstratus = getAbstratus(this.abstratus);
			if(abstratus == null)
				return;

			abstratus.setPreventRightClick(disable);
		}

		@Override
		public String describe() {
			return "unallocated weapons under " + abstratus + " can no" + (disable ? " longer be" : "w be") + " ineracted with when restricted strife is enabled";
		}
	}

	private static class AddAbstratus implements IAction
	{
		final String key;

		private AddAbstratus(String key)
		{
			this.key = key;
		}

		@Override
		public void apply()
		{
			KindAbstratus res = new KindAbstratus(key);
			res.setRegistryName(new ResourceLocation(MinestuckUniverse.MODID, key));

			if(KindAbstratus.REGISTRY.getKeys().contains(res.getRegistryName()))
				CraftTweakerAPI.logError("abstratus " + res.getRegistryName() + " already exists, please pick a different key");
			else KindAbstratus.REGISTRY.register(res);
		}

		@Override
		public String describe() {
			return "implementing abstratus " + key;
		}
	}

	private enum Mode
	{
		ADD,
		REMOVE
	}

	private static class PrintIds implements IAction
	{
		@Override
		public void apply()
		{
			for(KindAbstratus k : KindAbstratus.REGISTRY.getValuesCollection())
				CraftTweakerAPI.logInfo(k.getLocalizedName() + " - " + k.getRegistryName());
		}

		@Override
		public String describe()
		{
			return null;
		}
	}
}
