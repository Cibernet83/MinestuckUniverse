package com.cibernet.minestuckuniverse.modSupport.crafttweaker;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristType;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass("minestuckuniverse.Grist")
@ZenRegister
public class CTAPIGrist
{
	public static final List<IAction> actions = new ArrayList<>();

	public CTAPIGrist() {}

	@ZenMethod
	public static void addGristType(String key, float rarity, float value)
	{
		actions.add(new AddGristType(key, rarity, value));
	}

	@ZenMethod
	public static void addSecondaryGrist(String grist, String secondary)
	{
		actions.add(new SecondaryGrist(grist, secondary, false));
	}

	@ZenMethod
	public static void removeSecondaryGrist(String grist, String secondary)
	{
		actions.add(new SecondaryGrist(grist, secondary, true));
	}

	private static class AddGristType implements IAction
	{

		final String key;
		final float rarity;
		final float value;

		private AddGristType(String key, float rarity, float value) {
			this.key = key;
			this.rarity = rarity;
			this.value = value;
		}

		@Override
		public void apply()
		{
			GristType grist = new GristType(key, rarity, value, new ResourceLocation(MinestuckUniverse.MODID, key));
			grist.setRegistryName(new ResourceLocation(MinestuckUniverse.MODID, key));
			MinestuckUniverseGrist.gristList.add(grist);
			MinestuckUniverseGrist.customGrist.add(grist);

			GristType.REGISTRY.register(grist);
		}

		@Override
		public String describe()
		{
			return "Adding Grist Type " + key;
		}
	}

	private static class SecondaryGrist implements IAction
	{
		final String grist;
		final String secondary;
		final boolean remove;

		private SecondaryGrist(String grist, String secondary, boolean remove)
		{
			this.grist = grist;
			this.secondary = secondary;
			this.remove = remove;
		}

		@Override
		public void apply()
		{
			if(remove)
				GristHelper.secondaryGristMap.get(GristType.getTypeFromString(grist)).remove(GristType.getTypeFromString(secondary));
			else GristHelper.secondaryGristMap.get(GristType.getTypeFromString(grist)).add(GristType.getTypeFromString(secondary));
		}

		@Override
		public String describe()
		{
			return (remove ? ("Removing " + secondary + " Grist from") : ("Adding " + secondary + " Grist to") + " Secondary " + grist + " Grist map");
		}
	}
}
