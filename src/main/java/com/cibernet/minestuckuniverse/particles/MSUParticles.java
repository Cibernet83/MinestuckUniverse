package com.cibernet.minestuckuniverse.particles;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	
}
