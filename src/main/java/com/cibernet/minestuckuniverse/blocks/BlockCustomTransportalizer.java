package com.cibernet.minestuckuniverse.blocks;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.IRegistryItem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler.GuiId;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class BlockCustomTransportalizer extends BlockContainer implements IRegistryItem
{

    protected static final AxisAlignedBB TRANSPORTALIZER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public BlockCustomTransportalizer(MapColor color)
    {
        super(Material.IRON, color);
        this.setCreativeTab(TabMinestuckUniverse.main);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        String key = getUnlocalizedName()+".tooltip";
        if(!I18n.translateToLocal(key).equals(key))
            tooltip.add(I18n.translateToLocal(key));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
    
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return TRANSPORTALIZER_AABB;
    }

    public abstract TileEntity createNewTileEntity(World world, int metadata);

    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote && entity.getRidingEntity() == null && entity.getPassengers().isEmpty() && !world.isRemote) {
            if (entity.timeUntilPortal == 0) {
                ((TileEntityTransportalizer)world.getTileEntity(pos)).teleport(entity);
            } else {
                entity.timeUntilPortal = entity.getPortalCooldown();
            }
        }

    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntityTransportalizer tileEntity = (TileEntityTransportalizer)worldIn.getTileEntity(pos);
        if (tileEntity != null && !playerIn.isSneaking()) {
            if (worldIn.isRemote) {
                playerIn.openGui(Minestuck.instance, GuiId.TRANSPORTALIZER.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        } else {
            return false;
        }
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            List<ItemStack> items = new ArrayList();
            ItemStack itemstack = this.getSilkTouchDrop(state);
            if (!itemstack.isEmpty()) {
                if (te instanceof TileEntityTransportalizer) {
                    itemstack.setStackDisplayName(((TileEntityTransportalizer)te).getId());
                }

                items.add(itemstack);
            }

            ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0F, true, player);
            Iterator var9 = items.iterator();

            while(var9.hasNext()) {
                ItemStack item = (ItemStack)var9.next();
                spawnAsEntity(worldIn, pos, item);
            }
        } else {
            this.harvesters.set(player);
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            this.dropBlockAsItem(worldIn, pos, state, i);
            this.harvesters.set(null);
        }

    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
