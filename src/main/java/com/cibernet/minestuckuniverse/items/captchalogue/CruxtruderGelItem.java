package com.cibernet.minestuckuniverse.items.captchalogue;

import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.MSUSoundHandler;
import com.mraof.minestuck.block.BlockCruxtiteDowel;
import com.mraof.minestuck.block.BlockCruxtruder;
import com.mraof.minestuck.block.BlockSburbMachine;
import com.mraof.minestuck.tileentity.TileEntityCruxtruder;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.tileentity.TileEntitySburbMachine;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class CruxtruderGelItem extends CruxiteItem
{
	public CruxtruderGelItem(String name)
	{
		super(name);
		setMaxStackSize(16);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".name", stack.getMetadata() == 0 ? "" : (I18n.translateToLocal("cruxite_color." + (stack.getMetadata() - 1))+" ")).trim();
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		int color = stack.getMetadata()-1;
		
		if(worldIn.getTileEntity(pos) instanceof TileEntitySburbMachine)
		{
			TileEntitySburbMachine te = (TileEntitySburbMachine) worldIn.getTileEntity(pos);
			if(te.getMachineType() == BlockSburbMachine.MachineType.CRUXTRUDER)
			{
				te.color = color;
				
				if (player instanceof EntityPlayerMP)
					CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)player, stack);
				worldIn.playSound(null, pos, MSUSoundHandler.cruxtruderGelFill, SoundCategory.BLOCKS, 0.8f, 1);
				if(worldIn.isRemote) for (int i = 0; i < 16; ++i)
					MSUParticles.spawnInkParticle(pos.getX()+0.5f, pos.getY()+1, pos.getZ()+0.5, 0, 0.15f, 0, color == -1 ? 0x99D9EA : ColorCollector.getColor(color));
				stack.shrink(1);
				player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
				return EnumActionResult.SUCCESS;
			}
		}
		else if(worldIn.getBlockState(pos).getBlock() instanceof BlockCruxtruder)
		{
			BlockPos mainPos = ((BlockCruxtruder) worldIn.getBlockState(pos).getBlock()).getMainPos(worldIn.getBlockState(pos), pos);
			TileEntity te = worldIn.getTileEntity(mainPos);
			
			if(te instanceof TileEntityCruxtruder && worldIn.getBlockState(mainPos.up()).getBlock().isReplaceable(worldIn, mainPos.up()))
			{
				((TileEntityCruxtruder) te).setColor(color);
				
				if(worldIn.getBlockState(mainPos.up()).getBlock() instanceof BlockCruxtiteDowel && worldIn.getBlockState(mainPos.up()).getValue(BlockCruxtiteDowel.TYPE).equals(BlockCruxtiteDowel.Type.CRUXTRUDER))
				{
					ItemStack dowel = ((TileEntityItemStack)worldIn.getTileEntity(mainPos.up())).getStack();
					dowel.setItemDamage(stack.getMetadata());
					((TileEntityItemStack)worldIn.getTileEntity(mainPos.up())).setStack(dowel);
				}
				
				if(player instanceof EntityPlayerMP)
					CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) player, stack);
				worldIn.playSound(null, pos, MSUSoundHandler.cruxtruderGelFill, SoundCategory.BLOCKS, 0.8f, 1);
				if(worldIn.isRemote) for(int i = 0; i < 16; ++i)
					MSUParticles.spawnInkParticle(mainPos.getX() + 0.5f, mainPos.getY() + 1, mainPos.getZ() + 0.5, 0, 0.15f, 0, color == -1 ? 0x99D9EA : ColorCollector.getColor(color));
				if(!player.isCreative())
				{
					stack.shrink(1);
					player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
				}
				return EnumActionResult.SUCCESS;
			}
		}
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
