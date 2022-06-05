package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.BaseModusGuiHandler;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class MonsterModus extends BaseModus
{
	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(id == CaptchaDeckHandler.EMPTY_CARD)
		{
			if(list.size() < size)
			{
				size--;
				return new ItemStack(MinestuckItems.captchaCard);
			} else return ItemStack.EMPTY;
		}
		
		if(list.isEmpty())
			return ItemStack.EMPTY;
		
		int color = MinestuckPlayerData.getData(player).color;
		
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			for(ItemStack item : list)
				createMonster(player, item, color);
			list.clear();
			return ItemStack.EMPTY;
		}
		
		if(id < 0 || id >= list.size())
			return ItemStack.EMPTY;
		
		ItemStack item = getSort() ? list.remove(id) : this.list.set(id, ItemStack.EMPTY);
		
		if(asCard)
		{
			size--;
			item = AlchemyRecipes.createCard(item, false);
			return item;
		}
		createMonster(player, item, color);
		return ItemStack.EMPTY;
	}
	
	public static void createMonster(EntityPlayer source, ItemStack stack, int color)
	{
		EntityMob entity = source.world.rand.nextBoolean() ? new EntityZombie(source.world) : new EntitySkeleton(source.world);
		
		EntityEquipmentSlot slot = EntityEquipmentSlot.MAINHAND;
		if(stack.getItem() instanceof ItemArmor)
			slot = ((ItemArmor) stack.getItem()).armorType;
		entity.setItemStackToSlot(slot, stack);
		entity.enablePersistence();
		entity.setDropChance(slot, 1);
		
		entity.motionX = CaptchaDeckHandler.rand.nextDouble() - 0.5D;
		entity.motionZ = CaptchaDeckHandler.rand.nextDouble() - 0.5D;
		entity.setPosition(source.posX, source.posY + 1.0D, source.posZ);
		source.world.spawnEntity(entity);
	}
	
	@Override
	protected boolean getSort()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new BaseModusGuiHandler(this, 47) {};
		return gui;
	}
}
