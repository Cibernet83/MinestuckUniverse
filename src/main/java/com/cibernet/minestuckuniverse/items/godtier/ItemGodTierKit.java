package com.cibernet.minestuckuniverse.items.godtier;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.util.AspectColorHandler;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGodTierKit extends MSUItemBase
{
	
	public ItemGodTierKit()
	{
		super("god_tier_kit", "gtArmorKit");
		setMaxStackSize(1);
		setCreativeTab(TabMinestuckUniverse.godTier);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(isInCreativeTab(tab))
		{
			for(EnumAspect aspect : EnumAspect.values())
				for(EnumClass heroClass : EnumClass.values())
				{
					items.add(generateKit(heroClass, aspect));
				}
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		String heroClass = TextFormatting.OBFUSCATED + "Class" + TextFormatting.RESET;
		String heroAspect = TextFormatting.OBFUSCATED + "Thing" + TextFormatting.RESET;
		
		if(stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			
			if(nbt.hasKey("class"))
			{
				int c = nbt.getInteger("class");
				if(c >= 0 && c < EnumClass.values().length)
					heroClass = EnumClass.getClassFromInt(c).getDisplayName();
			}
			if(nbt.hasKey("aspect"))
			{
				int a = nbt.getInteger("aspect");
				if(a >= 0 && a < EnumAspect.values().length)
					heroAspect = EnumAspect.getAspectFromInt(a).getDisplayName();
			}
			
		}
		
		tooltip.add(I18n.format("title.format", heroClass, heroAspect));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
	{
		IdentifierHandler.PlayerIdentifier id = IdentifierHandler.encode(playerIn);
		SburbConnection c = SkaianetHandler.getMainConnection(id, true);
		ItemStack stack = playerIn.getHeldItem(handIn);
		NBTTagCompound nbt = stack.getTagCompound();
		if(/*c == null || !c.enteredGame() || */ nbt == null) return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
		
		
		NBTTagCompound armorNbt = new NBTTagCompound();
		armorNbt.setInteger("class", nbt.getInteger("class"));
		armorNbt.setInteger("aspect", nbt.getInteger("aspect"));
		Item[] armor = new Item[] {MinestuckUniverseItems.gtHood, MinestuckUniverseItems.gtShirt, MinestuckUniverseItems.gtPants, MinestuckUniverseItems.gtShoes};
		for(Item i : armor)
		{
			ItemStack armorStack = new ItemStack(i);
			armorStack.setTagCompound(armorNbt);
			giveStackToPlayer(armorStack, playerIn, worldIn);
		}
		stack.shrink(1);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		
		if(entityIn instanceof EntityPlayer)
		{
			NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
			
			EntityPlayer player = (EntityPlayer) entityIn;
			IdentifierHandler.PlayerIdentifier id = IdentifierHandler.encode(player);
			SburbConnection c = SkaianetHandler.getMainConnection(id, true);
			if(c == null || !c.enteredGame()) return;

			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());

			if(!stack.getTagCompound().hasKey("aspect"))
				nbt.setInteger("aspect", MinestuckPlayerData.getTitle(id).getHeroAspect().ordinal());
			if(!stack.getTagCompound().hasKey("class"))
				nbt.setInteger("class", MinestuckPlayerData.getTitle(id).getHeroClass().ordinal());
			
			stack.setTagCompound(nbt);
		}
	}
	
	public static int getColor(ItemStack stack, AspectColorHandler.EnumColor color)
	{	
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) return color == AspectColorHandler.EnumColor.SYMBOL ? 8355711 : 0;
		
		return AspectColorHandler.getAspectColor(nbt.getInteger("aspect"), color);
	}
	
	public void giveStackToPlayer(ItemStack stack, EntityPlayer player, World worldIn)
	{
		if(!player.inventory.addItemStackToInventory(stack))
		{
			BlockPos pos = player.getPosition();
			BlockPos dropPos;
			if(!worldIn.getBlockState(pos).isBlockNormalCube())
				dropPos = pos;
			else if(!worldIn.getBlockState(pos.up()).isBlockNormalCube())
				dropPos = pos.up();
			else dropPos = pos;
			InventoryHelper.spawnItemStack(worldIn, dropPos.getX(), dropPos.getY(), dropPos.getZ(), stack);
		}
	}
	
	public static ItemStack generateKit(EnumClass heroClass, EnumAspect aspect)
	{
		ItemStack stack = new ItemStack(MinestuckUniverseItems.gtArmorKit);
		
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("aspect", aspect == null ? 12 : aspect.ordinal());
		nbt.setInteger("class", heroClass.ordinal());
		stack.setTagCompound(nbt);
		
		return stack;
	}
	
	public static boolean isAvailable(SburbConnection sburbConnection)
	{
		return MinestuckPlayerData.getTitle(sburbConnection.getClientIdentifier()) != null && sburbConnection.getClientIdentifier().getPlayer() != null ;//&& sburbConnection.getClientIdentifier().getPlayer().getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier();
	}
	
	public static ItemStack generateKit(SburbConnection sburbConnection)
	{
		Title clientTitle = MinestuckPlayerData.getTitle(sburbConnection.getClientIdentifier());
		return generateKit(clientTitle == null ? EnumClass.KNIGHT : clientTitle.getHeroClass(), clientTitle == null ? null : clientTitle.getHeroAspect());
	}
	
}
