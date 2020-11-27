package com.cibernet.minestuckuniverse.items;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockTransportalizer;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWarpMedallion extends MSUItemBase
{
    protected EnumTeleportType teleportType;

    public ItemWarpMedallion(String unlocName, String registryName, EnumTeleportType type, int uses)
    {
        super(registryName, unlocName);
        this.teleportType = type;

        if(uses >= 0)
            setMaxDamage(uses);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if(teleportType == EnumTeleportType.TRANSPORTALIZER && stack.hasTagCompound() && stack.getTagCompound().hasKey("Code"))
            tooltip.add("[" + stack.getTagCompound().getString("Code") + "]");
        else super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        if(teleportType == EnumTeleportType.RETURN)
        {
            if(stack.getTagCompound() == null)
                stack.setTagCompound(new NBTTagCompound());
            if(!stack.getTagCompound().hasKey("Player"))
            stack.getTagCompound().setInteger("Player", IdentifierHandler.encode((EntityPlayer)entityIn).getId());
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(teleportType == EnumTeleportType.TRANSPORTALIZER)
        {
            if(worldIn.getTileEntity(pos) instanceof TileEntityTransportalizer)
            {
                TileEntityTransportalizer te = (TileEntityTransportalizer) worldIn.getTileEntity(pos);
                ItemStack stack = player.getHeldItem(hand);
                if(stack.getTagCompound() == null)
                    stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setString("Code", te.getId());
                return EnumActionResult.SUCCESS;
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    public static int getColor(ItemStack stack)
    {
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("Player"))
            return -1;

        int colorIndex = MinestuckPlayerData.getData(IdentifierHandler.getById(stack.getTagCompound().getInteger("Player"))).color;

       return colorIndex <= -1 || colorIndex >= ColorCollector.getColorSize() ? 0x99D9EA : ColorCollector.getColor(colorIndex);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if(teleport(stack, worldIn, entityLiving))
        {
            stack.damageItem(1, entityLiving);
            entityLiving.motionX = 0;
            entityLiving.motionY = 0;
            entityLiving.motionZ = 0;
            entityLiving.fallDistance = 0;
        } else entityLiving.sendMessage(new TextComponentTranslation("message.medallion.error"));

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    private boolean teleport(ItemStack medallion, World worldIn, EntityLivingBase entityLiving)
    {
        BlockPos pos;

        switch (teleportType)
        {
            case SKAIA:

                int dimId = MinestuckDimensionHandler.skaiaDimensionId == worldIn.provider.getDimension() ? 0 : MinestuckDimensionHandler.skaiaDimensionId;
                WorldServer dimWorld = entityLiving.getServer().getWorld(dimId);
                pos = dimWorld.getTopSolidOrLiquidBlock(new BlockPos(entityLiving)).up(5);

            return Teleport.teleportEntity(entityLiving, dimId, null, pos);

            case TRANSPORTALIZER:

                if(!medallion.hasTagCompound() || !medallion.getTagCompound().hasKey("Code"))
                    return false;
            return tpToTransportalizer(medallion.getTagCompound().getString("Code"), worldIn, entityLiving);

            case RETURN:

                if(!medallion.hasTagCompound() || !medallion.getTagCompound().hasKey("Player"))
                    return false;
                IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.getById(medallion.getTagCompound().getInteger("Player"));
                if(identifier == null)
                    return false;

                SburbConnection c = SkaianetHandler.getMainConnection(identifier, false);
                if(c == null)
                    return false;

                WorldServer world = entityLiving.getServer().getWorld(c.getClientDimension());
                pos = world.provider.getRandomizedSpawnPoint();
                Teleport.teleportEntity(entityLiving, c.getClientDimension(), null, pos);
                return true;
        }

        return false;
    }


    public boolean tpToTransportalizer(String destId, World world, Entity entity)
    {
        Location location = (Location)TileEntityTransportalizer.transportalizers.get(destId);
        if (location != null && location.pos.getY() != -1) {
            WorldServer destWorld = entity.getServer().getWorld(location.dim);
            TileEntityTransportalizer destTransportalizer = (TileEntityTransportalizer)destWorld.getTileEntity(location.pos);
            if (destTransportalizer == null) {
                Debug.warn("Invalid transportalizer in map: " + destId + " at " + location);
                TileEntityTransportalizer.transportalizers.remove(destId);
                destId = "";
                return false;
            }

            if (!destTransportalizer.getEnabled()) {
                return false;
            }

            int[] var5 = MinestuckConfig.forbiddenDimensionsTpz;
            int var6 = var5.length;
            int var7 = 0;

            while(true) {
                if (var7 >= var6) {
                       IBlockState block0 = world.getBlockState(location.pos.up());
                       IBlockState block1 = world.getBlockState(location.pos.up(2));
                        if (!block0.getMaterial().blocksMovement() && !block1.getMaterial().blocksMovement()) {
                            Teleport.teleportEntity(entity, location.dim, (Teleport.ITeleporter)null, (double)destTransportalizer.getPos().getX() + 0.5D, (double)destTransportalizer.getPos().getY() + 0.6D, (double)destTransportalizer.getPos().getZ() + 0.5D);
                            entity.timeUntilPortal = entity.getPortalCooldown();
                            break;
                        }

                        entity.timeUntilPortal = entity.getPortalCooldown();
                        if (entity instanceof EntityPlayerMP) {
                            entity.sendMessage(new TextComponentTranslation("message.transportalizer.destinationBlocked", new Object[0]));
                        }

                        return false;
                }

                int id = var5[var7];
                if (world.provider.getDimension() == id || location.dim == id) {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                    if (entity instanceof EntityPlayerMP) {
                        entity.sendMessage(new TextComponentTranslation(world.provider.getDimension() == id ? "message.transportalizer.forbidden" : "message.transportalizer.forbiddenDest", new Object[0]));
                    }

                    return false;
                }

                ++var7;
            }
        } else entity.sendMessage(new TextComponentTranslation("message.transportalizer.destinationInvalid", new Object[0]));
        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == MinestuckItems.rawUranium;
    }

    @Override
    public boolean isRepairable() {
        return true;
    }

    public enum EnumTeleportType
    {
        RETURN,
        TRANSPORTALIZER,
        SKAIA,
        NONE
    }
}
