package com.cibernet.minestuckuniverse.tileentity;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.block.BlockHolopad;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityHolopad extends TileEntity implements ITickable
{
	public int innerRotation = 0;
	protected ItemStack card;
	
	public TileEntityHolopad() {
		this.card = ItemStack.EMPTY;
	}
	
	public void onRightClick(EntityPlayer player) {
		if (!this.world.isRemote) {
			new AxisAlignedBB(this.pos);
		}
		
		if (!this.card.isEmpty()) {
			if (player.getHeldItemMainhand().isEmpty()) {
				player.setHeldItem(EnumHand.MAIN_HAND, this.card);
			} else if (!player.inventory.addItemStackToInventory(this.card)) {
				this.dropItem(false, this.world, this.pos, this.card);
			} else {
				player.inventoryContainer.detectAndSendChanges();
			}
			
			this.setCard(ItemStack.EMPTY);
			//this.destroyHologram(this.pos);
		} else {
			ItemStack heldStack = player.getHeldItemMainhand();
			if (this.card.isEmpty() && !heldStack.isEmpty() && AlchemyRecipes.isPunchedCard(heldStack)) {
				this.setCard(heldStack.splitStack(1));
				ItemStack in = this.getCard();
				ItemStack item = new ItemStack(MinestuckBlocks.genericObject);
				if (in.hasTagCompound() && in.getTagCompound().hasKey("contentID")) {
					item = AlchemyRecipes.getDecodedItem(in);
				}
				
				//this.spawnHologram(this.pos, item);
			}
			
		}
	}
	
	
	public void dropItem(boolean inBlock, World worldIn, BlockPos pos, ItemStack item) {
		BlockPos dropPos;
		if (inBlock) {
			dropPos = pos;
		} else if (!worldIn.getBlockState(pos).isBlockNormalCube()) {
			dropPos = pos;
		} else if (!worldIn.getBlockState(pos.up()).isBlockNormalCube()) {
			dropPos = pos.up();
		} else {
			dropPos = pos;
		}
		
		InventoryHelper.spawnItemStack(worldIn, (double)dropPos.getX(), (double)dropPos.getY(), (double)dropPos.getZ(), item);
	}
	
	public boolean hasCard() {
		return !this.getCard().isEmpty();
	}
	
	public void setCard(ItemStack card) {
		if (card.getItem() == MinestuckItems.captchaCard || card.isEmpty()) {
			this.card = card;
			if (this.world != null) {
				IBlockState state = this.world.getBlockState(this.pos);
				this.world.notifyBlockUpdate(this.pos, state, state, 2);
			}
		}
		
	}
	
	public ItemStack getCard() {
		return this.card;
	}
	
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.setCard(new ItemStack(tagCompound.getCompoundTag("card")));
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setTag("card", this.card.writeToNBT(new NBTTagCompound()));
		return tagCompound;
	}
	
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setTag("card", this.card.writeToNBT(new NBTTagCompound()));
		return nbt;
	}
	
	public SPacketUpdateTileEntity getUpdatePacket() {
		SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
		return packet;
	}
	
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.handleUpdateTag(pkt.getNbtCompound());
	}
	
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock() || oldState.getValue(BlockHolopad.CARD) != newSate.getValue(BlockHolopad.CARD);
	}
	
	@Override
	public void update()
	{
		++innerRotation;
	}
}
