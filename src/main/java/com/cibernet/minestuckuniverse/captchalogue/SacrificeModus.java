package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.BaseModusGuiHandler;
import com.mraof.minestuck.alchemy.GristRegistry;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;

public class SacrificeModus extends BaseModus
{
	@Override
	protected boolean getSort()
	{
		return false;
	}

	@Override
	public boolean putItemStack(ItemStack stack)
	{
		if(!stack.isEmpty())
			player.heal(0.5f);
		return super.putItemStack(stack);
	}

	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		ItemStack stack = super.getItem(id, asCard);

		GristSet gristConversion = GristRegistry.getGristConversion(stack);

		int gristCost = 0;
		if(gristConversion != null)
			for(Map.Entry<GristType, Integer> grist : gristConversion.gristTypes.entrySet())
				gristCost += Math.abs(grist.getValue()*grist.getKey().getValue());

		player.attackEntityFrom(new DamageSource("fetchmodiplus:sacrifice").setDamageBypassesArmor(), Math.max(1, stack.getCount()/(float)stack.getMaxStackSize() * gristCost/player.getMaxHealth()));
		if(player.getHealth() <= 0)
		{
			CaptchaDeckHandler.launchAnyItem(player, stack);
			return ItemStack.EMPTY;
		}

		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new BaseModusGuiHandler(this, 52) {};
		return gui;
	}
}
