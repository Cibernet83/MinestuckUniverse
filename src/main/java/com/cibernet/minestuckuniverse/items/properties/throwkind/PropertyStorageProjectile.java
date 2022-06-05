package com.cibernet.minestuckuniverse.items.properties.throwkind;

import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class PropertyStorageProjectile extends WeaponProperty implements IPropertyThrowable
{
	boolean contentTooltip;
	boolean destroyProjectileItem;

	public PropertyStorageProjectile(boolean contentTooltip, boolean destroyProjectileItem)
	{
		this.contentTooltip = contentTooltip;
		this.destroyProjectileItem = destroyProjectileItem;
	}

	@Override
	public void addTooltip(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if(contentTooltip && stack.hasTagCompound())
		{
			ItemStack storedStack = MSUItemBase.getStoredItem(stack);

			if(!storedStack.isEmpty())
			{
				String stackSize = storedStack.getCount() > 0 ? storedStack.getCount() + "x" : "";
				tooltip.add("(" + stackSize + MSUItemBase.getStoredItem(stack).getDisplayName() + ")");
			}
		}
	}

	@Override
	public void getDroppedItems(EntityMSUThrowable projectile, List<ItemStack> itemList)
	{
		if(destroyProjectileItem && itemList.size() > 0 && itemList.get(0) == projectile.getStack())
			itemList.remove(0);

		itemList.add(MSUItemBase.getStoredItem(projectile.getStack()));
	}
}
