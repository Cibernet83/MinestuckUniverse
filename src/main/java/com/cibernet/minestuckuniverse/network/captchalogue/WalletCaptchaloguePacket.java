package com.cibernet.minestuckuniverse.network.captchalogue;

import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.captchalogue.WalletEntityItem;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.block.*;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityCruxtruder;
import com.mraof.minestuck.tileentity.TileEntityPunchDesignix;
import com.mraof.minestuck.tileentity.TileEntityTotemLathe;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class WalletCaptchaloguePacket extends MSUPacket
{
    int entity;
    BlockPos pos;

    @Override
    public MSUPacket generatePacket(Object... args)
    {
        RayTraceResult lookingAt = ((RayTraceResult)args[0]);

        data.writeBoolean(lookingAt.entityHit != null);

        if(lookingAt.entityHit != null)
            data.writeInt(lookingAt.entityHit.getEntityId());

        data.writeBoolean(lookingAt.getBlockPos() != null);
        if(lookingAt.getBlockPos() != null)
        {
            data.writeInt(lookingAt.getBlockPos().getX());
            data.writeInt(lookingAt.getBlockPos().getY());
            data.writeInt(lookingAt.getBlockPos().getZ());
        }
        return this;
    }

    @Override
    public MSUPacket consumePacket(ByteBuf buffer)
    {
        if(buffer.readBoolean())
            entity = buffer.readInt();
        if(buffer.readBoolean())
            pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());

        return this;
    }

    @Override
    public void execute(EntityPlayer player)
    {
        if(!player.getHeldItemMainhand().isEmpty())
            return;
        Entity entity = player.world.getEntityByID(this.entity);
        if(entity != null)
        {
            ItemStack stack = new ItemStack(MinestuckUniverseItems.walletEntityItem);
            WalletEntityItem.storeEntity(entity, stack);
            player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
            entity.setDead();
        }
        else if(pos != null)
            player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, captchalogueTileEntity(player.world, pos));
        CaptchaDeckHandler.captchalougeItem((EntityPlayerMP) player);
    }

    @Override
    public EnumSet<Side> getSenderSide()
    {
        return EnumSet.of(Side.CLIENT);
    }

    protected static ItemStack captchalogueTileEntity(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        BlockPos mainPos;
        EnumFacing mchnFacing;

        if(block instanceof BlockLargeMachine)
        {
            if(block instanceof BlockAlchemiter)
            {
                mainPos = ((BlockAlchemiter)block).getMainPos(state, pos, worldIn);
                TileEntity te = worldIn.getTileEntity(mainPos);
                if(!(te instanceof TileEntityAlchemiter))
                    return ItemStack.EMPTY;
                else if(((TileEntityAlchemiter)te).isBroken())
                    return ItemStack.EMPTY;
                mchnFacing = ((TileEntityAlchemiter) te).getFacing();

                mainPos = mainPos.down();

                if(!worldIn.isRemote)
                {
                    for(int x = 0; x < 4; x++)
                        for(int z = 0; z < 4; z++)
                            worldIn.destroyBlock(mainPos.offset(mchnFacing, x).offset(mchnFacing.rotateY(), z), true);
                    for(int y = 0; y < 4; y++)
                        worldIn.destroyBlock(mainPos.up(y), true);
                }

                return new ItemStack(MinestuckBlocks.alchemiter[0]);
            }
            else if(block instanceof BlockTotemLathe)
            {
                mainPos = ((BlockTotemLathe)block).getMainPos(state, pos);
                TileEntity te = worldIn.getTileEntity(mainPos);
                if(!(te instanceof TileEntityTotemLathe))
                    return ItemStack.EMPTY;
                else if(((TileEntityTotemLathe)te).isBroken())
                    return ItemStack.EMPTY;
                mchnFacing = ((TileEntityTotemLathe) worldIn.getTileEntity(mainPos)).getFacing();
                if(!worldIn.isRemote)
                {
                    TileEntity teLathe = worldIn.getTileEntity(mainPos);
                    for(int x = 0; x < 4; x++)
                        for(int y = 0; y < 3; y++)
                        {
                            //TODO lathe dowel not dropping for some reason
                            BlockPos pos1 = mainPos.up(y).offset(mchnFacing.rotateYCCW(), x);
                            if(x == 2 && y == 1)
                                if(!((TileEntityTotemLathe) teLathe).getDowel().isEmpty())
                                    InventoryHelper.spawnItemStack(worldIn, pos1.getX(), pos1.getY(), pos1.getZ(), ((TileEntityTotemLathe) teLathe).getDowel());
                            if(x == 0 && y == 0)
                            {
                                if(!((TileEntityTotemLathe) teLathe).getCard1().isEmpty())
                                    InventoryHelper.spawnItemStack(worldIn, pos1.getX(), pos1.getY(), pos1.getZ(), ((TileEntityTotemLathe) teLathe).getCard1());
                                if(!((TileEntityTotemLathe) teLathe).getCard2().isEmpty())
                                    InventoryHelper.spawnItemStack(worldIn, pos1.getX(), pos1.getY(), pos1.getZ(), ((TileEntityTotemLathe) teLathe).getCard2());
                            }
                            if(worldIn.getBlockState(pos1).getBlock() instanceof BlockTotemLathe)
                                worldIn.destroyBlock(pos1, true);
                        }
                }
                return new ItemStack(MinestuckBlocks.totemlathe[0]);
            }else if(block instanceof BlockPunchDesignix)
            {
                mainPos = ((BlockPunchDesignix)block).getMainPos(state, pos);
                mchnFacing = (EnumFacing)state.getValue(BlockPunchDesignix.DIRECTION);

                TileEntity te = worldIn.getTileEntity(mainPos);
                if(!(te instanceof TileEntityPunchDesignix))
                    return ItemStack.EMPTY;
                else if(((TileEntityPunchDesignix)te).broken)
                    return ItemStack.EMPTY;
                if(!worldIn.isRemote)
                {
                    for(int x = 0; x < 2; x++)
                        for(int y = 0; y < 2; y++)
                            worldIn.destroyBlock(mainPos.down(y).offset(mchnFacing.rotateYCCW(), x), true);
                }
                return new ItemStack(MinestuckBlocks.punchDesignix);
            }else if(block instanceof BlockCruxtruder)
            {
                mchnFacing = worldIn.getBlockState(((BlockCruxtruder)state.getBlock()).getMainPos(state, pos)).getValue(BlockCruxtruder.DIRECTION);
                mainPos = ((BlockCruxtruder)block).getMainPos(state, pos);
                TileEntity te = worldIn.getTileEntity(mainPos);
                if(!(te instanceof TileEntityCruxtruder))
                    return ItemStack.EMPTY;
                else if(((TileEntityCruxtruder)te).isBroken())
                    return ItemStack.EMPTY;

                if(!worldIn.isRemote)
                {
                    for(int x = 0; x < 3; x++)
                        for(int z = 0; z < 3; z++)
                            for(int y = 1; y < 2; y++)
                                worldIn.destroyBlock(mainPos.north(x - 1).east(z - 1).down(y), true);

                    worldIn.destroyBlock(mainPos, true);
                    if(worldIn.getBlockState(mainPos.up()).getBlock() instanceof BlockCruxtiteDowel)
                        worldIn.destroyBlock(mainPos.up(), true);

                }
                return new ItemStack(MinestuckBlocks.cruxtruder);
            }
        } else
        {
            float hardness = state.getBlockHardness(worldIn, pos);
            if (Item.getItemFromBlock(block) != Items.AIR && hardness < 50F && hardness >= 0 && worldIn.getTileEntity(pos) != null)
            {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setTag("BlockEntityTag", worldIn.getTileEntity(pos).writeToNBT(new NBTTagCompound()));

                if (!nbt.getCompoundTag("BlockEntityTag").hasNoTags())
                {
                    NBTTagCompound displayTag = new NBTTagCompound();
                    NBTTagList tooltipList = new NBTTagList();
                    tooltipList.appendTag(new NBTTagString("(+NBT)"));
                    displayTag.setTag("Lore", tooltipList);
                    nbt.setTag("display", displayTag);
                }

                ItemStack result = new ItemStack(Item.getItemFromBlock(block));
                if(nbt.getCompoundTag("BlockEntityTag").hasKey("CustomName"))
                    result.setStackDisplayName(nbt.getCompoundTag("BlockEntityTag").getString("CustomName"));

                result.setTagCompound(nbt);

                worldIn.removeTileEntity(pos);
                worldIn.destroyBlock(pos, false);

                return result;
            }
        }
        return ItemStack.EMPTY;
    }
}
