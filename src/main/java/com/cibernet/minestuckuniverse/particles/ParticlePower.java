package com.cibernet.minestuckuniverse.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticlePower extends Particle
{

    public ParticlePower(World world, double xPos, double yPos, double zPos, double xVel, double yVel, double zVel, int maxAge, int hexColor)
    {
        this(world, xPos, yPos, zPos, xVel, yVel, zVel, maxAge, (float) ((hexColor & 16711680) >> 16) / 255f, (float) ((hexColor & 65280) >> 8) / 255f, (float) ((hexColor & 255) >> 0) / 255f);
    }

    public ParticlePower(World world, double xPos, double yPos, double zPos, double xVel, double yVel, double zVel, int maxAge, float r, float g, float b)
    {
        super(world, xPos, yPos, zPos);

        motionX = xVel;
        motionY = yVel;
        motionZ = zVel;

        particleRed = r;
        particleGreen = g;
        particleBlue = b;

        particleMaxAge = maxAge;

        this.setParticleTextureIndex(160);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        setParticleTextureIndex(160 + (particleAge % 14)/2);
    }

    @Override
    public int getBrightnessForRender(float pTicks)
    {
        float f = 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender(pTicks);
        int j = i&255;
        int k = i>>16&255;
        j = j+(int) (f*15.0F*16.0F);

        if (j>240) {
            j = 240;
        }

        return j|k<<16;
    }
}
