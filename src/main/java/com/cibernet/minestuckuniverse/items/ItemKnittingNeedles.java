package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.BlockWoolTransportalizer;
import com.cibernet.minestuckuniverse.items.properties.PropertyDualWield;
import com.cibernet.minestuckuniverse.items.weapons.MSUWeaponBase;
import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemKnittingNeedles extends MSUWeaponBase
{
    public ItemKnittingNeedles(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName)
    {
        super(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
        setMaxStackSize(2);

        this.addPropertyOverride(new ResourceLocation(MinestuckUniverse.MODID,"plural"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if(stack.hasTagCompound() && stack.getTagCompound().hasKey("JEIDisplay"))
                    return 1;
                return (entityIn != null && stack.getCount() >= 2) ? 1 : 0;
            }
        });
        addProperties(new PropertyDualWield());
        setContainerItem(this);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return stack.getCount() >= 2 ? I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name.plural").trim() : super.getItemStackDisplayName(stack);
    }

    @Override
    public double getAttackDamage(ItemStack stack) {
        return stack.getCount() >= 2 ? 0 : super.getAttackDamage(stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);

        if(stack.getCount() >= 2 && worldIn.getBlockState(pos).getBlock() instanceof BlockWoolTransportalizer)
        {
            EnumDyeColor color = ((BlockWoolTransportalizer)worldIn.getBlockState(pos).getBlock()).color;
            NBTTagCompound nbt = worldIn.getTileEntity(pos).writeToNBT(new NBTTagCompound());
            TileEntity te = MinestuckBlocks.transportalizer.createTileEntity(worldIn, MinestuckBlocks.transportalizer.getDefaultState());
            te.readFromNBT(nbt);
            worldIn.setBlockState(pos, MinestuckBlocks.transportalizer.getDefaultState());
            worldIn.setTileEntity(pos, te);


            stack.damageItem(1, player);
            if(!worldIn.isRemote)
                InventoryHelper.spawnItemStack(worldIn,pos.getX(),pos.getY(),pos.getZ(), new ItemStack(MinestuckUniverseItems.yarnBall, 1, color.getDyeDamage()));
            worldIn.playSound(null, pos, SoundEvents.BLOCK_CLOTH_BREAK, SoundCategory.BLOCKS, 0.5f, 1);

            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void setDamage(ItemStack stack, int damage)
    {
        super.setDamage(stack, damage);
        if(stack.getItemDamage() > stack.getMaxDamage())
            stack.setCount(1);
    }
}
