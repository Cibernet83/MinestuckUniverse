package com.cibernet.minestuckuniverse.items.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.OperandiModus;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

public class OperandiToolItem extends MSUItemBase
{
	protected float efficiency;
	/** Damage versus entities. */
	protected float attackDamage;
	protected float attackSpeed;
	/** The material this tool is made from. */
	//protected Item.ToolMaterial toolMaterial;
	protected String toolClass = "";
	
	public OperandiToolItem(String name, String toolClass, float attackDamageIn, float attackSpeedIn, float efficiency, int maxUses)
	{
		super(name);
		OperandiModus.itemPool.add(this);
		
		this.efficiency = 4.0F;
		this.maxStackSize = 1;
		this.setMaxDamage(maxUses);
		this.efficiency = efficiency;
		this.attackDamage = attackDamageIn;
		this.attackSpeed = attackSpeedIn;
		this.toolClass = toolClass;
		
		setCreativeTab(TabMinestuck.instance);
	}
	
	public OperandiToolItem(String name, String toolClass)
	{
		this(name, toolClass, 0.0F, 0.0F, 7.0f, 3);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
	}
	
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		if (state.getBlock().isToolEffective(toolClass, state))
			return efficiency;
		return 1.0F;
	}
	
	/**
	 * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
	 * the damage on the stack.
	 */
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		stack.damageItem(getMaxDamage(stack)+1, attacker);
		return true;
	}
	
	/**
	 * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
	 */
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if ((double)state.getBlockHardness(worldIn, pos) != 0.0D)
		{
			if(state.getBlock().isToolEffective(toolClass, state))
			{
				ItemStack storedStack = MSUItemBase.getStoredItem(stack);
				
				if(!worldIn.isRemote)
					stack.damageItem(1, entityLiving);
				if(stack.isEmpty())
				{
					worldIn.playSound(null, entityLiving.getPosition(), MSUSoundHandler.operandiTaskComplete, SoundCategory.PLAYERS, 1, 1);
					
					if((entityLiving instanceof EntityPlayer) && !((EntityPlayer)entityLiving).addItemStackToInventory(storedStack))
						((EntityPlayer) entityLiving).dropItem(storedStack, true);
				}
			}
			else if(!worldIn.isRemote) stack.damageItem(getMaxDamage(stack)+1, entityLiving);
		}
		
		return true;
	}
	
	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	/**
	 * Return the enchantability factor of the item, most of the time is based on material.
	 */
	public int getItemEnchantability()
	{
		return -1;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return false;
	}
	
	/**
	 * Return whether this item is repairable in an anvil.
	 *
	 * @param toRepair the {@code ItemStack} being repaired
	 * @param repair the {@code ItemStack} being used to perform the repair
	 */
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}
	
	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
	 */
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
		
		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double)this.attackDamage, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double)this.attackSpeed, 0));
		}
		
		return multimap;
	}
	
	@javax.annotation.Nullable
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, @javax.annotation.Nullable net.minecraft.entity.player.EntityPlayer player, @javax.annotation.Nullable IBlockState blockState)
	{
		int level = super.getHarvestLevel(stack, toolClass,  player, blockState);
		if (level == -1 && toolClass.equals(this.toolClass))
		{
			return 2;
		}
		else
		{
			return level;
		}
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return toolClass != null ? com.google.common.collect.ImmutableSet.of(toolClass) : super.getToolClasses(stack);
	}
}
