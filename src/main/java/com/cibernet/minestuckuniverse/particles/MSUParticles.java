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

	private static int[][] particleColors = new int[][] {
			new int[] {0xB71015, 0x3E1601}, // BLOOD
			new int[] {0x47E2FA, 0x4379E6}, // BREATH
			new int[] {0x306800, 0x111111}, // DOOM
			new int[] {0xBD1864, 0x55142A}, // HEART
			new int[] {0xFFDE55, 0xFDFEFF}, // HOPE
			new int[] {0x72EB34, 0xA49787}, // LIFE
			new int[] {0xF6FA4E, 0xF0840C}, // LIGHT
			new int[] {0x06FFC9, 0x00923D}, // MIND
			new int[] {0x9C4DAC, 0x520C61}, // RAGE
			new int[] {0x4BEC13}, // SPACE
			new int[] {0xFF2106, 0xB70D0E}, // TIME
			new int[] {0x104EA2, 0x001856}, // VOID

			new int[] {0xDB5397}, // BARD
			new int[] {0x6D9EEB}, // HEIR
			new int[] {0xEF7F34}, // KNIGHT
			new int[] {0xB55BFF}, // MAGE
			new int[] {0x31E0AB}, // MAID
			new int[] {}, // PAGE
			new int[] {0x7C1D1D}, // PRINCE
			new int[] {0x39C4C6}, // ROGUE
			new int[] {0xD670FF}, // SEER
			new int[] {0xFF8377}, // SYLPH
			new int[] {0x996543}, // THIEF
			new int[] {0x7F7F7F}, // WITCH
			new int[] {0xFF0000}, // LORD
			new int[] {0x00FF00}, // MUSE
	};
	public static int[] getAspectParticleColors(EnumAspect aspect)
	{
		return particleColors[aspect.ordinal()];
	}
	public static int[] getClassParticleColors(EnumClass aspect)
	{
		return particleColors[EnumAspect.values().length + aspect.ordinal()];
	}

	public static class PowerParticleState
	{
		public final ParticleType type;
		public final EnumAspect aspect;
		public final EnumClass clazz;
		public final int count;

		public PowerParticleState(ParticleType type, EnumAspect aspect, int count)
		{
			this.type = type;
			this.aspect = aspect;
			this.clazz = null;
			this.count = count;
		}
		public PowerParticleState(ParticleType type, EnumClass clazz, int count)
		{
			this.type = type;
			this.aspect = null;
			this.clazz = clazz;
			this.count = count;
		}


		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof PowerParticleState))
				return false;
			PowerParticleState state = (PowerParticleState) obj;
			return this.type == state.type && this.aspect == state.aspect && this.clazz == state.clazz && this.count == state.count;
		}
	}
}
