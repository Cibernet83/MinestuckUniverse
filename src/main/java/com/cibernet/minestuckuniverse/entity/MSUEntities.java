package com.cibernet.minestuckuniverse.entity;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.entity.classes.EntityAcheron;
import com.cibernet.minestuckuniverse.entity.classes.EntityMSUThrowable;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class MSUEntities
{
	public static int currentEntityIdOffset = 0;
	
	public static void registerEntities()
	{
		registerEntity(EntityAcheron.class, "Acheron");
		registerEntity(EntityMSUThrowable.class, "YarnBall");
	}
	
	
	public static void registerEntity(Class<? extends Entity> entityClass, String name) {
		registerEntity(entityClass, name, name, 80, 3, true);
	}
	
	public static void registerEntity(Class<? extends Entity> entityClass, String name, int eggPrimary, int eggSecondary) {
		registerEntity(entityClass, name, name, 80, 3, true, eggPrimary, eggSecondary);
	}
	
	public static void registerEntity(Class<? extends Entity> entityClass, String name, String registryName) {
		registerEntity(entityClass, name, registryName, 80, 3, true);
	}
	
	public static void registerEntity(Class<? extends Entity> entityClass, String name, String registryName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(new ResourceLocation(MinestuckUniverse.MODID, registryName), entityClass, MinestuckUniverse.MODID + name, currentEntityIdOffset, MinestuckUniverse.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		++currentEntityIdOffset;
	}
	
	public static void registerEntity(Class<? extends Entity> entityClass, String name, String registryName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggPrimary, int eggSecondary) {
		EntityRegistry.registerModEntity(new ResourceLocation(MinestuckUniverse.MODID, registryName), entityClass, MinestuckUniverse.MODID + name, currentEntityIdOffset, MinestuckUniverse.instance, trackingRange, updateFrequency, sendsVelocityUpdates, eggPrimary, eggSecondary);
		++currentEntityIdOffset;
	}
}
