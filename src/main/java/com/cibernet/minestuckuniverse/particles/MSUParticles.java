package com.cibernet.minestuckuniverse.particles;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MSUParticles
{
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static Particle spawnInkParticle(double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color)
	{
		return spawnInkParticle(xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, color, 1);
	}
	
	public static Particle spawnInkParticle(double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color, float size) {
		if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null) {
			int partSetting = mc.gameSettings.particleSetting;
			
			if (partSetting == 1 && mc.world.rand.nextInt(3) == 0) {
				partSetting = 2;
			}
			
			double xCheck = mc.getRenderViewEntity().posX - xCoordIn;
			double yCheck = mc.getRenderViewEntity().posY - yCoordIn;
			double zCheck = mc.getRenderViewEntity().posZ - zCoordIn;
			Particle particle = null;
			double max = 16.0D;
			
			if (xCheck * xCheck + yCheck * yCheck + zCheck * zCheck > max * max) {
				return null;
			} else if (partSetting > 1) {
				return null;
			} else {
				
				particle = new ParticleInk(mc.world, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, color, size);
				mc.effectRenderer.addEffect(particle);
				return particle;
			}
		}
		return null;
	}
	
	public static class ParticleInk extends Particle
	{
		public int color;
		
		public ParticleInk(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color, float size) {
			super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
			this.color = color;
			
			double r = Math.floor(color / (256*256));
			double g = Math.floor(color / 256) % 256;
			double b = color % 256;
			
			this.particleRed = Math.max(5/255f,(float) (r/255) - 5/255f);
			this.particleGreen = Math.max(5/255f,(float) (g/255) - 5/255f);
			this.particleBlue = Math.max(5/255f,(float) (b/255) - 5/255f);
			
			
			this.particleScale = Math.min(1, Math.max(0, rand.nextFloat()))*5 * size;
			
			this.particleGravity = 0.1f;
		}
		
		public void onUpdate()
		{
			super.onUpdate();
			if(particleGravity > 0)
				this.motionY -= 0.004D + 0.04D * (double)this.particleGravity;

			if (this.world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).getMaterial() == Material.WATER)
			{
				this.setExpired();
			}
		}
		
	}

	@SideOnly(Side.CLIENT)
	public static void spawnPowerParticle(World world, double xPos, double yPos, double zPos, double xVel, double yVel, double zVel, int maxAge, int hexColor)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticlePower(world, xPos, yPos, zPos, xVel, yVel, zVel, maxAge, hexColor));
	}

	public static void spawnAuraParticles(EntityLivingBase entity, int color, int count)
	{
		spawnAuraParticles(entity.world, entity.posX, entity.posY, entity.posZ, color, count);
	}

	public static void spawnAuraParticles(World world, double x, double y, double z, int color, int count)
	{
		if(world.isRemote)
			for (int i = 0; i < count; i++)
			{
				Vec3d vel = new Vec3d(Math.random()-0.5, Math.random()-0.25, Math.random()-0.5)
						.normalize().scale(((Math.random()*8+1)*0.02));
				Vec3d off = new Vec3d(Math.random()-0.5, Math.random(), Math.random()-0.5)
						.normalize().scale(0.4);

				MSUParticles.spawnPowerParticle(world, x+off.x, y+off.y, z+off.z, vel.x, vel.y, vel.z, world.rand.nextInt(10)+10, color);
			}
	}

	public static void spawnBurstParticles(EntityLivingBase entity, int color, int count)
	{
		spawnBurstParticles(entity.world, entity.posX, entity.posY, entity.posZ, color, count);
	}

	public static void spawnBurstParticles(World world, double x, double y, double z, int color, int count)
	{
		if(world.isRemote)
			for (int i = 0; i < count; i++)
			{
				Vec3d vel = new Vec3d(Math.random()-0.5, 0, Math.random()-0.5)
						.normalize().scale(((Math.random()*8+1)*0.05));
				Vec3d off = new Vec3d(Math.random()-0.5, 1.5, Math.random()-0.5)
						.normalize().scale(0.4);

				MSUParticles.spawnPowerParticle(world, x+off.x, y+off.y, z+off.z, vel.x, vel.y, vel.z, world.rand.nextInt(10)+10, color);
			}
	}

	public enum ParticleType
	{
		AURA,
		BURST
	}

	public static class PowerParticleState
	{
		public final ParticleType type;
		public final int[] colors;
		public final int count;

		public PowerParticleState(ParticleType type, int count, int... colors)
		{
			this.type = type;
			this.colors = colors;
			this.count = count;
		}


		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof PowerParticleState))
				return false;
			PowerParticleState state = (PowerParticleState) obj;
			return this.type == state.type && this.colors.equals(state.colors) && this.count == state.count;
		}
	}
}
