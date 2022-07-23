package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.gui.captchalogue.AlchemyGuiHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class AlchemyModus extends BaseModus
{
	
	public GristType wildcardGrist = GristType.Build;
	
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
		
		if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
		{
			list.clear();
			return ItemStack.EMPTY;
		}
		
		if(id < 0 || id >= list.size())
			return ItemStack.EMPTY;
		
		
		if(asCard)
		{
			size--;
			return AlchemyRecipes.createGhostCard(list.remove(id));
		}
		else return alchemize(list.get(id), player);
		
	}
	
	@Override
	public boolean putItemStack(ItemStack stack)
	{
		if (stack.isItemStackDamageable())
			stack.setItemDamage(0);
		
		if(stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			
			stack.setTagCompound(new NBTTagCompound());
			
			if(nbt.hasKey("Name", 8))
				stack.setStackDisplayName(nbt.getString("Name"));
			if(nbt.hasKey("LocName", 8))
				stack.setTranslatableName(I18n.translateToLocal(nbt.getString("LocName")));
		}
		
		return super.putItemStack(stack);
	}
	
	public ItemStack alchemize(ItemStack stack, EntityPlayer player)
	{
		ItemStack newItem = stack.copy();
		
		if (newItem.isItemStackDamageable())
			newItem.setItemDamage(0);
		
		GristSet cost = this.getGristCost(stack);
		boolean canAfford = GristHelper.canAfford(MinestuckPlayerData.getGristSet(player), cost);
		if (canAfford)
		{
			AlchemyRecipes.giveAlchemyExperience(newItem, player);
			IdentifierHandler.PlayerIdentifier pid = IdentifierHandler.encode(player);
			GristHelper.decrease(pid, cost);
			MinestuckPlayerTracker.updateGristCache(pid);

			if(stack.getItem().equals(Item.getItemFromBlock(MinestuckUniverseBlocks.artifact))) //TODO move this to MSU
			{
				player.sendStatusMessage(new TextComponentTranslation("status.alchemodus.alchemArtifact"), false);
				player.attackEntityFrom(DamageSource.GENERIC, Float.MAX_VALUE);
				return ItemStack.EMPTY;
			}

			return newItem;
		}
		return ItemStack.EMPTY;
	}
	
	public GristSet getGristCost(ItemStack stack)
	{
		GristSet set = GristRegistry.getGristConversion(stack);
		boolean useSelectedType = stack.getItem() == MinestuckItems.captchaCard;
		if (useSelectedType)
			set = new GristSet(getWildcardGrist(), side == Side.SERVER ? MinestuckConfig.cardCost : MinestuckConfig.clientCardCost);
		
		if(stack.getItem() == MinestuckUniverseItems.eightBall)
			set = new GristSet(new GristType[] {GristType.Build, GristType.Artifact, GristType.Cobalt, GristType.Zillium, GristType.Mercury}, new int[] {1000, 100, 800, 2, 800});
		
		if (set != null)
			set.scaleGrist((float)stack.getCount());
		
		return set;
	}
	
	public GristType getWildcardGrist()
	{
		return wildcardGrist;
	}
	
	public AlchemyModus setWildcardGrist(GristType grist)
	{
		this.wildcardGrist = grist;
		return this;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		wildcardGrist = GristType.REGISTRY.getValue(nbt.getInteger("WildcardGrist"));
		if(wildcardGrist == null)
			wildcardGrist = GristType.Build;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("WildcardGrist", GristType.REGISTRY.getID(wildcardGrist));
		return super.writeToNBT(nbt);
	}
	
	@Override
	protected boolean getSort()
	{
		return true;
	}
	
	@Override
	public NonNullList<ItemStack> getItems()
	{
		if (this.side.isServer())
		{
			NonNullList<ItemStack> items = NonNullList.create();
			this.fillGhostList(items);
			return items;
		} else
		{
			if (this.changed)
				this.fillGhostList(this.items);
			return this.items;
		}
	}
	
	private void fillGhostList(NonNullList<ItemStack> items)
	{
		items.clear();
		Iterator<ItemStack> iter = this.list.iterator();
		
		for(int i = 0; i < this.size; ++i) {
			/* if (iter.hasNext())
			{
				ItemStack stack = iter.next();
				stack.setCount(0);
				items.add(stack);
			} else */
				items.add(ItemStack.EMPTY);
		}
	}
	
	
	public NonNullList<ItemStack> getDisplayItems()
	{
		return super.getItems();
	}
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new AlchemyGuiHandler(this);
		return gui;
	}
}
