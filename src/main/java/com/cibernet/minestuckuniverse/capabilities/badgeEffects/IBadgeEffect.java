package com.cibernet.minestuckuniverse.capabilities.badgeEffects;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

public interface IBadgeEffect
{
	void serialize(NBTTagCompound tag);
	void serialize(ByteBuf data);
	default IBadgeEffect initialize(World world)
	{
		return this;
	}

	enum BadgeEffectType
	{
		INT, BOOLEAN, VEC4, MOVEMENT_INPUT, ENTITY, NBT, STRING
	}

	static IBadgeEffect deserialize(NBTTagCompound tag)
	{
		BadgeEffectType type = BadgeEffectType.values()[tag.getByte("BadgeEffectType")];
		switch (type)
		{
			case INT:
				return new IntEffect(tag);
			case BOOLEAN:
				return new BooleanEffect(tag);
			case VEC4:
				return new Vec4Effect(tag);
			case MOVEMENT_INPUT:
				return new MovementInputEffect(tag);
			case ENTITY:
				return new EntityIDEffect(tag);
			case NBT:
				return new NBTEffect(tag, true);
			case STRING:
				return new StringEffect(tag);
			default:
					throw new IllegalArgumentException("Unknown BadgeEffectType " + type);
		}
	}

	static IBadgeEffect deserialize(ByteBuf data)
	{
		BadgeEffectType type = BadgeEffectType.values()[data.readByte()];
		switch (type)
		{
			case INT:
				return new IntEffect(data);
			case BOOLEAN:
				return new BooleanEffect(data);
			case VEC4:
				return new Vec4Effect(data);
			case MOVEMENT_INPUT:
				return new MovementInputEffect(data);
			case ENTITY:
				return new EntityIDEffect(data);
			case NBT:
				return new NBTEffect(data);
			case STRING:
				return new StringEffect(data);
			default:
				throw new IllegalArgumentException("Unknown BadgeEffectType " + type);
		}
	}


	class IntEffect implements IBadgeEffect
	{
		public final int value;

		public IntEffect(int value)
		{
			this.value = value;
		}

		private IntEffect(NBTTagCompound tag)
		{
			this.value = tag.getInteger("Value");
		}

		private IntEffect(ByteBuf data)
		{
			this.value = data.readInt();
		}

		@Override
		public void serialize(NBTTagCompound tag)
		{
			tag.setByte("BadgeEffectType", (byte) BadgeEffectType.INT.ordinal());
			tag.setInteger("Value", value);
		}

		@Override
		public void serialize(ByteBuf data)
		{
			data.writeByte(BadgeEffectType.INT.ordinal());
			data.writeInt(value);
		}

		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof IntEffect))
				return false;
			IntEffect effect = (IntEffect) obj;

			return this.value == effect.value;
		}
	}

	class BooleanEffect implements IBadgeEffect
	{
		public final boolean value;

		public BooleanEffect(boolean value)
		{
			this.value = value;
		}

		private BooleanEffect(NBTTagCompound tag)
		{
			this.value = tag.getBoolean("Value");
		}

		private BooleanEffect(ByteBuf data)
		{
			this.value = data.readBoolean();
		}

		@Override
		public void serialize(NBTTagCompound tag)
		{
			tag.setByte("BadgeEffectType", (byte) BadgeEffectType.BOOLEAN.ordinal());
			tag.setBoolean("Value", value);
		}

		@Override
		public void serialize(ByteBuf data)
		{
			data.writeByte(BadgeEffectType.BOOLEAN.ordinal());
			data.writeBoolean(value);
		}

		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof BooleanEffect))
				return false;
			BooleanEffect effect = (BooleanEffect) obj;

			return this.value == effect.value;
		}
	}

	class Vec4Effect implements IBadgeEffect
	{
		public final Vec3d position;
		public final int dimension;

		public Vec4Effect(Vec3d position, int dimension)
		{
			this.position = position;
			this.dimension = dimension;
		}

		private Vec4Effect(NBTTagCompound tag)
		{
			this.position = new Vec3d(
					tag.getDouble("PositionX"),
					tag.getDouble("PositionY"),
					tag.getDouble("PositionZ")
			);
			this.dimension = tag.getInteger("Dimension");
		}

		private Vec4Effect(ByteBuf data)
		{
			this.position = new Vec3d(
					data.readDouble(),
					data.readDouble(),
					data.readDouble()
			);
			this.dimension = data.readInt();
		}

		@Override
		public void serialize(NBTTagCompound tag)
		{
			tag.setByte("BadgeEffectType", (byte) BadgeEffectType.VEC4.ordinal());
			tag.setDouble("PositionX", position.x);
			tag.setDouble("PositionY", position.y);
			tag.setDouble("PositionZ", position.z);
			tag.setInteger("Dimension", dimension);
		}

		@Override
		public void serialize(ByteBuf data)
		{
			data.writeByte(BadgeEffectType.VEC4.ordinal());
			data.writeDouble(position.x);
			data.writeDouble(position.y);
			data.writeDouble(position.z);
			data.writeInt(dimension);
		}

		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof Vec4Effect))
				return false;
			Vec4Effect effect = (Vec4Effect) obj;

			return this.position.equals(effect.position) && this.dimension == effect.dimension;
		}
	}

	class MovementInputEffect implements IBadgeEffect
	{
		public final float moveStrafe;
		public final float moveForward;
		public final boolean jump;
		public final boolean sneak;

		public MovementInputEffect(float moveStrafe, float moveForward, boolean jump, boolean sneak)
		{
			this.moveStrafe = moveStrafe;
			this.moveForward = moveForward;
			this.jump = jump;
			this.sneak = sneak;
		}

		private MovementInputEffect(NBTTagCompound tag)
		{
			this.moveStrafe = tag.getFloat("MoveStrafe");
			this.moveForward = tag.getFloat("MoveForward");
			this.jump = tag.getBoolean("Jump");
			this.sneak = tag.getBoolean("Sneak");
		}

		private MovementInputEffect(ByteBuf data)
		{
			this.moveStrafe = data.readFloat();
			this.moveForward = data.readFloat();
			this.jump = data.readBoolean();
			this.sneak = data.readBoolean();
		}

		@Override
		public void serialize(NBTTagCompound tag)
		{
			tag.setByte("BadgeEffectType", (byte) BadgeEffectType.MOVEMENT_INPUT.ordinal());
			tag.setFloat("MoveStrafe", moveStrafe);
			tag.setFloat("MoveForward", moveForward);
			tag.setBoolean("Jump", jump);
			tag.setBoolean("Sneak", sneak);
		}

		@Override
		public void serialize(ByteBuf data)
		{
			data.writeByte(BadgeEffectType.MOVEMENT_INPUT.ordinal());
			data.writeFloat(moveStrafe);
			data.writeFloat(moveForward);
			data.writeBoolean(jump);
			data.writeBoolean(sneak);
		}

		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof MovementInputEffect))
				return false;
			MovementInputEffect effect = (MovementInputEffect) obj;

			return this.moveStrafe == effect.moveStrafe && this.moveForward == effect.moveForward && this.jump == effect.jump && this.sneak == effect.sneak;
		}
	}

	class EntityIDEffect implements IBadgeEffect // Helper class to get around the consumePacket/execute limits
	{
		private final int id;
		private final UUID uuid;

		private EntityIDEffect(ByteBuf data)
		{
			this.id = data.readInt();
			this.uuid = null;
		}

		private EntityIDEffect(NBTTagCompound tag)
		{
			this.id = -1;
			this.uuid = UUID.fromString(tag.getString("UUID"));
		}

		@Override
		public void serialize(NBTTagCompound tag)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public void serialize(ByteBuf data)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public IBadgeEffect initialize(World world)
		{
			if (uuid == null)
				return new EntityEffect(world.getEntityByID(id));
			else
				return new EntityEffect(world.getEntities(Entity.class, (entity) -> entity.getUniqueID().equals(uuid)).get(0));
		}
	}

	class EntityEffect implements IBadgeEffect
	{
		public final Entity value;

		public EntityEffect(Entity value)
		{
			this.value = value;
		}

		@Override
		public void serialize(NBTTagCompound tag)
		{
			tag.setByte("BadgeEffectType", (byte) BadgeEffectType.ENTITY.ordinal());
			tag.setString("UUID", value.getUniqueID().toString());
		}

		@Override
		public void serialize(ByteBuf data)
		{
			data.writeByte(BadgeEffectType.ENTITY.ordinal());
			data.writeInt(value.getEntityId());
		}

		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof EntityEffect))
				return false;
			EntityEffect effect = (EntityEffect) obj;

			return this.value.equals(effect.value);
		}

		public EntityLivingBase getLiving()
		{
			return value instanceof EntityLivingBase ? (EntityLivingBase) value : null;
		}
	}

	class NBTEffect implements IBadgeEffect
	{
		public final NBTTagCompound value;

		protected NBTEffect(NBTTagCompound tag, boolean serialized)
		{
			if(serialized)
				value = tag.getCompoundTag("Value");
			else value = tag;
		}

		public NBTEffect(ByteBuf data)
		{
			value = ByteBufUtils.readTag(data);
		}

		@Override
		public void serialize(NBTTagCompound tag)
		{

			tag.setByte("BadgeEffectType", (byte) BadgeEffectType.NBT.ordinal());
			tag.setTag("Value", value);
		}

		@Override
		public void serialize(ByteBuf data)
		{
			data.writeByte(BadgeEffectType.NBT.ordinal());
			ByteBufUtils.writeTag(data, value);
		}
	}

	class StringEffect implements IBadgeEffect
	{
		public final String value;

		public StringEffect(String value)
		{
			this.value = value;
		}

		private StringEffect(NBTTagCompound tag)
		{
			this.value = tag.getString("Value");
		}

		private StringEffect(ByteBuf data)
		{
			this.value = ByteBufUtils.readUTF8String(data);
		}

		@Override
		public void serialize(NBTTagCompound tag)
		{
			tag.setByte("BadgeEffectType", (byte) BadgeEffectType.STRING.ordinal());
			tag.setString("Value", value);
		}

		@Override
		public void serialize(ByteBuf data)
		{
			data.writeByte(BadgeEffectType.STRING.ordinal());
			ByteBufUtils.writeUTF8String(data, value);
		}

		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof StringEffect))
				return false;
			StringEffect effect = (StringEffect) obj;

			return this.value == effect.value;
		}
	}
}
