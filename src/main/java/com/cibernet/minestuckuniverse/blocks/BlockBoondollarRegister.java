package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.cibernet.minestuckuniverse.tileentity.TileEntityBoondollarRegister;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.item.ItemBoondollars;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockBoondollarRegister extends BlockContainer implements IRegistryItem
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    protected BlockBoondollarRegister()
    {
        super(Material.IRON);
        setUnlocalizedName("boondollarRegister");
        setHarvestLevel("pickaxe", 0);
        setHardness(3.0F);
        setCreativeTab(TabMinestuckUniverse.main);

        setDefaultState(getDefaultState().withProperty(POWERED, false));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        String key = getUnlocalizedName()+".tooltip";
        if(!I18n.translateToLocal(key).equals(key))
            tooltip.add(I18n.translateToLocal(key));
        if(stack.hasTagCompound())
        {
            NBTTagCompound nbt = stack.getTagCompound();
            if(nbt.hasKey("OwnerName"))
                tooltip.add(I18n.translateToLocalFormatted("tooltip.machineOwner", nbt.getString("OwnerName")));
        }
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[]{FACING, POWERED});
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex() + (state.getValue(POWERED).booleanValue() ? 4 : 0 );
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(POWERED, meta >= 4);
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getStateFromMeta(meta).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return (blockState.getValue(POWERED)).booleanValue() ? 15 : 0;
    }

    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (!(blockState.getValue(POWERED)).booleanValue())
        {
            return 0;
        }
        else
        {
            return blockState.getValue(FACING) == side ? 15 : 0;
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);
        if ((state.getValue(POWERED)).booleanValue() && !worldIn.isRemote)
        {
            {
                worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)));
                this.notifyNeighbors(worldIn, pos, state.getValue(FACING));
                worldIn.markBlockRangeForRenderUpdate(pos, pos);
            }
        }
    }

    private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing facing)
    {
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        worldIn.notifyNeighborsOfStateChange(pos.offset(facing.getOpposite()), this, false);
    }

    @Override
    public int tickRate(World worldIn) {
        return 5;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntityBoondollarRegister te = (TileEntityBoondollarRegister) worldIn.getTileEntity(pos);

        if(stack.hasTagCompound())
        {
            NBTTagCompound nbt = stack.getTagCompound();
            if(worldIn.getTileEntity(pos) instanceof TileEntityBoondollarRegister)
            {
                TileEntityBoondollarRegister vault = (TileEntityBoondollarRegister) worldIn.getTileEntity(pos);
                if(stack.hasDisplayName())
                    vault.setName(stack.getDisplayName());
                if (nbt.hasUniqueId("owner"))
                {
                    vault.loadFromNBT(nbt);
                    return;
                }
            }
        }

        if(te != null && placer instanceof EntityPlayer)
        {
            te.owner = IdentifierHandler.encode((EntityPlayer) placer);
            te.ownerName = ((EntityPlayer) placer).getDisplayNameString();
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    public void onVaultBroken(TileEntityBoondollarRegister vault)
    {
        if(vault.getStoredBoons() > 0)
            spawnAsEntity(vault.getWorld(), vault.getPos(), ItemBoondollars.setCount(new ItemStack(MinestuckItems.boondollars), vault.getStoredBoons()));
        if(!vault.customMessage.isEmpty())
        {
            ItemStack paper = new ItemStack(Items.PAPER);
            paper.setStackDisplayName(vault.customMessage);
            spawnAsEntity(vault.getWorld(), vault.getPos(), paper);
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if(!(te instanceof TileEntityBoondollarRegister))
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
            return;
        }

        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);

        TileEntityBoondollarRegister vault = (TileEntityBoondollarRegister) te;
        if(vault.owner != null && IdentifierHandler.encode(player).equals(vault.owner) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) <= 0)
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
            onVaultBroken(vault);
        }
        else
        {
            spawnAsEntity(vault.getWorld(), vault.getPos(), getItem(vault));
        }
    }

    public ItemStack getItem(TileEntityBoondollarRegister vault)
    {
        ItemStack stack = new ItemStack(MinestuckUniverseBlocks.boondollarRegister);

        if(vault.owner != null)
            stack.setTagCompound(vault.saveToNBT(new NBTTagCompound()));

        if(vault.hasCustomName())
            stack.setStackDisplayName(vault.getName());

        return stack;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!(worldIn.getTileEntity(pos) instanceof TileEntityBoondollarRegister))
            return false;
        TileEntityBoondollarRegister te = (TileEntityBoondollarRegister) worldIn.getTileEntity(pos);
        ItemStack stack = playerIn.getHeldItem(hand);

        IdentifierHandler.PlayerIdentifier playerIdentifier = IdentifierHandler.encode(playerIn);

        if(!worldIn.isRemote)
        {
            if (stack.getItem().equals(MinestuckItems.boondollars))
            {
                int boonValue = ItemBoondollars.getCount(stack);
                if(te.isFull(boonValue))
                    playerIn.sendStatusMessage(new TextComponentTranslation("message.vault.full"), true);
                else if(boonValue >= te.mav) {
                    te.addBoondollars(boonValue);
                    stack.shrink(1);

                    worldIn.setBlockState(pos, state.withProperty(POWERED, true), 3);
                    worldIn.markBlockRangeForRenderUpdate(pos, pos);
                    this.notifyNeighbors(worldIn, pos, state.getValue(FACING));
                    worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));

                    if (!te.customMessage.isEmpty())
                        playerIn.sendStatusMessage(new TextComponentString("[" + te.getName() + "] " + te.customMessage), false);
                } else playerIn.sendStatusMessage(new TextComponentTranslation("message.vault.notEnough", te.mav - boonValue), true);

            } else if (playerIdentifier.equals(te.owner)) {
                if (stack.getItem().equals(Items.PAPER)) {
                    if (stack.hasDisplayName() && te.customMessage.isEmpty()) {
                        te.customMessage = stack.getDisplayName();
                        stack.shrink(1);
                        playerIn.sendStatusMessage(new TextComponentTranslation("message.vault.msgSet"), true);
                    } else if (!te.customMessage.isEmpty()) {
                        ItemStack paper = new ItemStack(Items.PAPER);
                        paper.setStackDisplayName(te.customMessage);
                        spawnAsEntity(worldIn, pos, paper);
                        te.customMessage = "";
                        playerIn.sendStatusMessage(new TextComponentTranslation("message.vault.msgRemoved"), true);
                    }
                } else {
                    return true;
                }
            }
            return false;
        }
        else if(playerIdentifier.equals(te.owner) && !stack.getItem().equals(Items.PAPER) && !stack.getItem().equals(MinestuckItems.boondollars))
                playerIn.openGui(MinestuckUniverse.instance, MSUUtils.BOONDOLLAR_REGISTER_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        if(worldIn.getTileEntity(pos) instanceof TileEntityBoondollarRegister && worldIn.getBlockState(pos).getValue(POWERED))
            return ((TileEntityBoondollarRegister) worldIn.getTileEntity(pos)).getComparatorOutput();
        return super.getComparatorInputOverride(blockState, worldIn, pos);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBoondollarRegister();
    }

    public boolean isFullCube(IBlockState state) {
        return true;
    }
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    @Override
    public void setRegistryName() {
        setRegistryName("boondollar_register");
    }
}
