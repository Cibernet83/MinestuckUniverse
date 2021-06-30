package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.tileentity.TileEntityGristHopper;
import com.cibernet.minestuckuniverse.tileentity.TileEntityRedTransportalizer;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockTransportalizer;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockRedTransportalizer extends BlockCustomTransportalizer
{

    public BlockRedTransportalizer() {
        super(MapColor.RED);
        this.setUnlocalizedName("redTransportalizer");
        this.setHardness(3.5F);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityRedTransportalizer();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntityRedTransportalizer tileEntity = (TileEntityRedTransportalizer)worldIn.getTileEntity(pos);
        IdentifierHandler.PlayerIdentifier id = IdentifierHandler.encode(playerIn);
        if(!id.equals(tileEntity.owner))
        {
            if (playerIn instanceof EntityPlayerMP)
                playerIn.sendStatusMessage(new TextComponentTranslation("message.transportalizer.notOwner"), true);
            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            List<ItemStack> items = new ArrayList();
            ItemStack itemstack = this.getSilkTouchDrop(state);
            if (!itemstack.isEmpty()) {
                if (te instanceof TileEntityRedTransportalizer)
                {
                    IdentifierHandler.PlayerIdentifier id = IdentifierHandler.encode(player);
                    if(((TileEntityRedTransportalizer) te).owner.equals(id))
                        itemstack.setStackDisplayName(((TileEntityRedTransportalizer)te).getId());
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

    @Nullable
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntityRedTransportalizer te = (TileEntityRedTransportalizer)worldIn.getTileEntity(pos);

        if(te != null && placer instanceof EntityPlayer)
            te.owner = IdentifierHandler.encode((EntityPlayer) placer);

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void setRegistryName() {
        this.setRegistryName("ruby_red_transportalizer");
    }
}
