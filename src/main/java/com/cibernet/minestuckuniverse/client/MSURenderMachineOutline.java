package com.cibernet.minestuckuniverse.client;

import com.cibernet.minestuckuniverse.util.SpaceSaltUtils;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.block.BlockSburbMachine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MSURenderMachineOutline
{
	
	@SubscribeEvent
	public static void renderWorld(RenderWorldLastEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		if (mc.player != null && mc.getRenderViewEntity() == mc.player)
		{
			RayTraceResult rayTraceResult = mc.objectMouseOver;
			if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK)
				return;
			
			
			IBlockState state = mc.player.getEntityWorld().getBlockState(mc.objectMouseOver.getBlockPos());
			if (mc.player.getHeldItemMainhand().getItem().equals(MinestuckUniverseItems.spaceSalt) && state.getBlock() instanceof BlockSburbMachine)
			{
				BlockSburbMachine.MachineType type = state.getValue(BlockSburbMachine.MACHINE_TYPE);
				EnumFacing facing = state.getValue(BlockSburbMachine.FACING);
				//facing = mc.player.getHorizontalFacing().getOpposite();
				ItemStack stack = mc.player.getHeldItemMainhand();
				
				renderCheckItem(mc.player, stack, type, event.getContext(), rayTraceResult, event.getPartialTicks(), facing);
			}
		}
	}
	
	private static boolean renderCheckItem(EntityPlayerSP player, ItemStack stack, BlockSburbMachine.MachineType machineType, RenderGlobal render, RayTraceResult rayTraceResult, float partialTicks, EnumFacing placedFacing)
	{
			BlockPos pos = rayTraceResult.getBlockPos();
			
			Block block = player.world.getBlockState(pos).getBlock();
			boolean flag = block.isReplaceable(player.world, pos);
			
			//if (!flag)
			//	pos = pos.up();
			
			//EnumFacing placedFacing = player.getHorizontalFacing().getOpposite();
			double hitX = rayTraceResult.hitVec.x - pos.getX(), hitZ = rayTraceResult.hitVec.z - pos.getZ();
			boolean r = placedFacing.getAxis() == EnumFacing.Axis.Z;
			boolean f = placedFacing== EnumFacing.NORTH || placedFacing==EnumFacing.EAST;
			double d1 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
			double d2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
			double d3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
			
			boolean placeable;
			AxisAlignedBB boundingBox;
			
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.glLineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);	//GL stuff was copied from the standard mouseover bounding box drawing, which is likely why the alpha isn't working
			BlockPos mchnPos = pos;
			
			if(machineType == BlockSburbMachine.MachineType.PUNCH_DESIGNIX)
			{
				if (placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
						|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
					pos = pos.offset(placedFacing.rotateY());
				
				BlockPos placementPos = pos;
				if (placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.NORTH)
					pos = pos.offset(placedFacing.rotateYCCW());    //The bounding box is symmetrical, so doing this gets rid of some rendering cases
				
				boundingBox = new AxisAlignedBB(0, 0, 0, (r ? 2 : 1), 2, (r ? 1 : 2)).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = SpaceSaltUtils.canPlacePunchDesignix(stack, player, player.world, placementPos, placedFacing, mchnPos);
			} else if(machineType == BlockSburbMachine.MachineType.TOTEM_LATHE)
			{
				pos = pos.offset(placedFacing.rotateY());
				
				if (placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
						|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
					pos = pos.offset(placedFacing.rotateY());
				
				BlockPos placementPos = pos;
				if (placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.NORTH)
					pos = pos.offset(placedFacing.rotateYCCW(), 3);    //The bounding box is symmetrical, so doing this gets rid of some rendering cases
				
				boundingBox = new AxisAlignedBB(0, 0, 0, (r ? 4 : 1), 3, (r ? 1 : 4)).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = SpaceSaltUtils.canPlaceTotemLathe(stack, player, player.world, placementPos, placedFacing, mchnPos);
			} else if(machineType == BlockSburbMachine.MachineType.CRUXTRUDER)
			{
				BlockPos placementPos = pos.offset(placedFacing.rotateYCCW(), (placedFacing.equals(EnumFacing.SOUTH) || placedFacing.equals(EnumFacing.WEST) ? -1 : 1)).offset(placedFacing, (placedFacing.equals(EnumFacing.NORTH) || placedFacing.equals(EnumFacing.WEST) ? 2 : 0));
				
				boundingBox = new AxisAlignedBB(0,0,0, 3, 3, 3).offset(placementPos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = SpaceSaltUtils.canPlaceCruxtruder(stack, player, player.world, pos.offset(placedFacing.rotateY()).offset(placedFacing, 2), placedFacing, mchnPos);
			} else	//Alchemiter
			{
				pos = pos.offset(placedFacing.rotateY());
				
				if (placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
						|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
					pos = pos.offset(placedFacing.rotateY());
				
				BlockPos placementPos = pos;
				if (placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.NORTH)
					pos = pos.offset(placedFacing.rotateYCCW(), 3);
				if(placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.SOUTH)
					pos = pos.offset(placedFacing.getOpposite(), 3);
				
				boundingBox = new AxisAlignedBB(0, 0, 0, 4, 4, 4).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = SpaceSaltUtils.canPlaceAlchemiter(stack, player, player.world, placementPos, placedFacing, mchnPos);
				
				BlockPos rodOff = new BlockPos(0,0,0).offset(placedFacing, -3);
				
				//If you don't want the extra details to the alchemiter outline, comment out the following two lines
				RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(1F/4F + rodOff.getX(),1,1F/4F + rodOff.getZ(), 3F/4F + rodOff.getX(), 4, 3F/4F + rodOff.getZ()).offset(placementPos).offset(-d1, -d2, -d3).shrink(0.002), placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
				RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(0,0,0, 4, 1, 4).offset(pos).offset(-d1, -d2, -d3).shrink(0.002), placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			
			}
			
			RenderGlobal.drawSelectionBoundingBox(boundingBox, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		
		return true;
	}
}
