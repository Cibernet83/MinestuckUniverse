package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechLifeFertility;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockChloroball extends MSUBlockBase
{
    public static final AxisAlignedBB AABB = new AxisAlignedBB(5/16D,5/16D,5/16D,11/16D,11/16D,11/16D);

    public static final SoundType SOUND_TYPE = new SoundType(0.7F, 1.8F, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundEvents.BLOCK_FIRE_AMBIENT, SoundEvents.BLOCK_FIRE_AMBIENT, SoundEvents.BLOCK_FIRE_AMBIENT, SoundEvents.BLOCK_FIRE_AMBIENT);
    private static final int RADIUS = 5;

    public BlockChloroball()
    {
        super(Material.FIRE, "chloroball", "chloroball");

        setSoundType(SOUND_TYPE);
        setLightLevel(2/15f);
        setTickRandomly(true);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.playEvent(1009, pos, 0);
    }

    @Override
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) {
        return true;
    }

    @Override
    public boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
        return true;
    }

    @Override
    public boolean addRunningEffects(IBlockState state, World world, BlockPos pos, Entity entity) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
    {
        MSUParticles.spawnAuraParticles(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0x72EB34, 8);
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        if(world.isRemote) return;
        int count = 0;
        List<BlockPos> availablePos = new ArrayList<>();

        for(int x = -RADIUS; x <= RADIUS; x++)for(int y = RADIUS; y >= -RADIUS; y--)for(int z = -RADIUS; z <= RADIUS; z++)
        {
            BlockPos targetPos = new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z);
            if(targetPos != null && world.getBlockState(targetPos).getBlock() instanceof IGrowable)
                availablePos.add(targetPos);
        }

        while(availablePos.size() > 0 && count < 3 && rand.nextFloat() < 1f/(count+1f))
        {
            BlockPos targetPos = availablePos.get(rand.nextInt(availablePos.size()));
            if(((IGrowable) world.getBlockState(targetPos).getBlock()).canGrow(world, targetPos, world.getBlockState(targetPos), world.isRemote))
            {
                for(int i = 0; i < 3; i++)
                    if(world.getBlockState(pos).getBlock() instanceof IGrowable)
                        ((IGrowable) world.getBlockState(targetPos).getBlock()).grow(world, world.rand, targetPos, world.getBlockState(targetPos));
                    else break;

                world.playEvent(2005, targetPos, 0);
            }
            count++;

        }

        if(count > 0)
            MSUParticles.spawnAuraParticles(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0x72EB34, 8);
    }

    @Override
    public int tickRate(World worldIn)
    {
        return super.tickRate(worldIn);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if(rand.nextInt(4) == 0)
            MSUParticles.spawnAuraParticles(worldIn, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0x72EB34, Math.max(1, rand.nextInt(6)-3));
    }
}
