package com.cibernet.minestuckuniverse.tileentity;

import com.cibernet.minestuckuniverse.blocks.BlockEffectBeacon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityEffectBeacon extends TileEntity implements ITickable
{
    private static final int radius = 15;

    @Override
    public void update()
    {
        if(!(world.getBlockState(getPos()).getBlock() instanceof BlockEffectBeacon))
            return;
        PotionEffect effect = ((BlockEffectBeacon) world.getBlockState(getPos()).getBlock()).getEffect();

        for(EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(getPos()).grow(radius)))
            player.addPotionEffect(effect);
    }
}
