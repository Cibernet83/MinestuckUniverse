package com.cibernet.minestuckuniverse.entity;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityUnrealAir extends Entity
{
	public static DamageSource UNREAL_DAMAGE = new DamageSource(MinestuckUniverse.MODID+".unrealDamage").setDamageBypassesArmor().setDamageAllowedInCreativeMode().setDamageIsAbsolute();

	public EntityUnrealAir(World worldIn)
	{
		super(worldIn);
		setSize(1.5f, 1f);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		Vec3d vec = getLookVec().scale(0.1f);

		motionX = vec.x;
		motionY = vec.y;
		motionZ = vec.z;

		posX += motionX;
		posY += motionY;
		posZ += motionZ;

		if(!isDead && posY > world.getActualHeight()*1.4f)
		{
			for(Entity passenger : getPassengers())
				passenger.attackEntityFrom(UNREAL_DAMAGE, Float.MAX_VALUE);
			world.setEntityState(this, (byte) 1);
			setDead();
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		player.startRiding(this);
		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		world.setEntityState(this, (byte) 1);
		setDead();
		return true;
	}

	@Override
	public void handleStatusUpdate(byte id)
	{
		for (int i = 0; i < 5; ++i)
		{
			Vec3d vec3d = new Vec3d(((double)this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
			vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
			vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
			double d0 = (double)(-this.rand.nextFloat()) * 0.6D - 0.3D;
			Vec3d vec3d1 = new Vec3d(((double)this.rand.nextFloat() - 0.5D) * width, d0, 0.6D);
			vec3d1 = vec3d1.addVector(this.posX+0.5f, this.posY + height/2f, this.posZ+0.5f);
			if (this.world instanceof WorldServer) //Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server specific variant
				((WorldServer)this.world).spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, 0,  vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D, Item.getIdFromItem(MinestuckUniverseItems.unrealAir));
			else //Fix the fact that spawning ItemCrack uses TWO arguments.
				this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(MinestuckUniverseItems.unrealAir));

		}
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return true;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}
}
