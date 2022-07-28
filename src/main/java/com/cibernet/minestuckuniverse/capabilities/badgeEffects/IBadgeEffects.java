package com.cibernet.minestuckuniverse.capabilities.badgeEffects;

import com.cibernet.minestuckuniverse.capabilities.IMSUCapabilityBase;
import com.cibernet.minestuckuniverse.entity.EntityBubble;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.util.SoulData;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import javax.annotation.Nullable;

public interface IBadgeEffects extends IMSUCapabilityBase<EntityLivingBase>
{
	// ----- Effects
	int getDecayTime();
	void setDecayTime(int decayTime);

	boolean isConcealed();
	void setConcealed(boolean concealed);

	boolean isTimeStopped();
	void setTimeStopped(boolean timeStopped);

	Vec3d getWarpPoint();
	int getWarpPointDim();
	boolean hasWarpPoint();
	void setWarpPoint(Vec3d warpPoint, int warpPointDim);
	void unsetWarpPoint();

	boolean isRageShifted();
	void setRageShifted(boolean rageShifted);
	boolean isFrenzied();
	void setFrenzied(boolean frenzied);

	NBTTagCompound getCloakData();
	boolean isCloaked();
	void setCloakData(NBTTagCompound nbtTagCompound);
	void clearCloakData();

	Entity getTether(int slot);
	void setTether(@Nullable Entity entity, int slot);
	void clearTether(int slot);
	
	//tethers start

	EntityLivingBase getMindflayerEntity();
	void setMindflayerEntity(EntityLivingBase entity);
	void setMindflayedBy(EntityLivingBase entity);
	EntityLivingBase getMindflayedBy();
	boolean isMindflayed();

	boolean isSoulShocked();
	void setSoulShocked(boolean v);

	void setSoulLinkedBy(@Nullable EntityLivingBase target);
	EntityLivingBase getSoulLinkedBy();
	void setSoulLinkInt(int v);
	int getSoulLinkInt();
	
	//tethers end
	
	void setExternalTech(int slot, String id);
	String getExternalTech(int slot);
	
	void setProjectionTime(int time);
	int getProjectionTime();
	
	void setFOV(int fov);
	int getFOV();

	void setTickUpStacks(int v);
	void increaseTickUpStacks(int v);
	int getTickUpStacks();

	void setCalculating(int calc);
	int getCalculating();
	
	void setHoping(boolean v);
	boolean isHoping();

	float getMoveStrafe();
	float getMoveForward();
	boolean getJump();
	boolean getSneak();
	boolean hasMovement();
	void setMovement(float moveStrafe, float moveForward, boolean jump, boolean sneak);
	void unsetMovement();

	ArrayList<UUID> getSavingGraceTargets();
	boolean isSavingGraced();
	void setSavingGraced(boolean v);

	boolean isWindFormed();
	void setWindFormed(boolean doingWimdyThing);

	Queue<SoulData> getTimeSoulData();

	Vec3d getPrevPos();
	void setPrevPos(Vec3d pos);

	BlockPos getManipulatedPos1();
	int getManipulatedPos1Dim();
	void setManipulatedPos1(BlockPos pos, int dim);
	BlockPos getManipulatedPos2();
	int getManipulatedPos2Dim();
	void setManipulatedPos2(BlockPos pos, int dim);
	boolean isManipulatingPos2();

	BlockPos getEditPos1();
	BlockPos getEditPos2();
	Vec3d getEditTraceHit();
	EnumFacing getEditTraceFacing();
	void setEditPos1(BlockPos pos);
	void setEditPos2(BlockPos pos);
	void setEditTraceHit(Vec3d hit);
	void setEditTraceFacing(EnumFacing facing);

	boolean isEditDragging();
	void setEditDragging(boolean v);

	int getLastSeerDodge();
	void setLastSeerDodge(int v);

	int getLastPageAwakening();
	void setLastPageAwakening(int v);

	boolean hasDragonAura();
	void setHasDragonAura(boolean v);

	// ----- Particles
	Map<Class, MSUParticles.PowerParticleState> getPowerParticles();

	default void startPowerParticles(Class badge, MSUParticles.ParticleType particleType, EnumAspect aspect, int count) {
		startPowerParticles(badge, new MSUParticles.PowerParticleState(particleType, count, BadgeEffects.getAspectParticleColors(aspect)));
	}
	default void startPowerParticles(Class badge, MSUParticles.ParticleType particleType, EnumClass clazz, int count) {
		startPowerParticles(badge, new MSUParticles.PowerParticleState(particleType, count, BadgeEffects.getClassParticleColors(clazz)));
	}
	default void startPowerParticles(Class badge, MSUParticles.ParticleType particleType, int count, int... colors) {
		startPowerParticles(badge, new MSUParticles.PowerParticleState(particleType, count, colors));
	}
	void startPowerParticles(Class badge, MSUParticles.PowerParticleState state);

	void stopPowerParticles(Class badge);

	default void oneshotPowerParticles(MSUParticles.ParticleType particleType, EnumAspect aspect, int count) {
		oneshotPowerParticles(new MSUParticles.PowerParticleState(particleType, count, BadgeEffects.getAspectParticleColors(aspect)));
	}
	default void oneshotPowerParticles(MSUParticles.ParticleType particleType, EnumClass clazz, int count) {
		oneshotPowerParticles(new MSUParticles.PowerParticleState(particleType, count, BadgeEffects.getClassParticleColors(clazz)));
	}
	default void oneshotPowerParticles(MSUParticles.ParticleType particleType, int count, int... colors)
	{
		oneshotPowerParticles(new MSUParticles.PowerParticleState(particleType, count, colors));
	}
	void oneshotPowerParticles(MSUParticles.PowerParticleState state);

	// ----- Data
	void setOwner(EntityLivingBase entity);

	void receive(String key, IBadgeEffect value);
}
