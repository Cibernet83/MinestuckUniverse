package com.cibernet.minestuckuniverse.entity;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.strife.IStrifeData;
import com.cibernet.minestuckuniverse.strife.MSUKindAbstrata;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

public class EntityRock extends EntityLivingBase
{
	public static final DamageSource ROCK_DAMAGE = new DamageSource(MinestuckUniverse.MODID+".rock");

	private double motionCalcX = 0;
	private double motionCalcZ = 0;

	public EntityRock(World worldIn) {
		super(worldIn);
		setSize(2, 2);
		stepHeight = 1;
	}

	@Override
	public double getMountedYOffset() {
		return height*0.9;
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return NonNullList.create();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {

	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return EnumHandSide.LEFT;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		if(!player.isSneaking())
		{
			player.startRiding(this);
			return true;
		}
		return super.processInitialInteract(player, hand);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if(canBeSteered())
		{
			Entity driver = getControllingPassenger();

			rotationYaw = driver.rotationYaw;

			double f = 0.3 * getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
			motionX = lerp(motionX, -f * MathHelper.sin(this.rotationYaw * 0.017453292F), 0.6);
			motionZ = lerp(motionZ, f * MathHelper.cos(this.rotationYaw * 0.017453292F), 0.6);
		}

		if(Math.abs(posX-motionCalcX) > 0.1 || Math.abs(motionY) > 0.1 || Math.abs(posZ-motionCalcZ) > 0.1)
		{
			for(Entity entity : world.getEntitiesWithinAABBExcludingEntity(this, getCollisionBoundingBox().contract(0 , 1, 0)))
				if(!getPassengers().contains(entity))
					entity.attackEntityFrom(ROCK_DAMAGE, 20);

			for(int x = (int)getCollisionBoundingBox().minX; x < getCollisionBoundingBox().maxX; x++)
				for(int z = (int)getCollisionBoundingBox().minZ; z < getCollisionBoundingBox().maxZ; z++)
				{
					BlockPos pos = new BlockPos(x, posY, z);
					if(world.getBlockState(pos).getBlockHardness(world, pos) == 0 && !(world.getBlockState(pos).getBlock() instanceof BlockLiquid))
						world.destroyBlock(pos, true);
				}
		}
		motionCalcX = this.posX;
		motionCalcZ = this.posZ;
	}

	private static double lerp(double a, double b, double slide)
	{
		double lowerBnd = Math.min(a, b);
		double upperBnd = Math.max(a, b);

		return lowerBnd + (upperBnd - lowerBnd) * slide;
	}

	@Override
	public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio)
	{
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(source.isFireDamage() || source == ROCK_DAMAGE)
			return false;

		if(source instanceof EntityDamageSource && source.getImmediateSource() instanceof EntityLivingBase)
		{
			ItemStack stack = ((EntityLivingBase) source.getImmediateSource()).getHeldItemMainhand();
			if(stack.canHarvestBlock(Blocks.STONE.getDefaultState()))
				amount *= 3;
		}

		boolean result = super.attackEntityFrom(source, amount);

		hurtResistantTime = 0;
		hurtTime = 0;
		maxHurtTime = 0;
		recentlyHit = 0;

		if(getHealth() <= 0)
			setDead();

		if(result)
			playParticles(5);

		return result;
	}

	@Override
	public void onDeath(DamageSource cause)
	{
		playParticles(getHealth() <= 0 ? 32 : 5);
		super.onDeath(cause);

	}

	@Override
	public void handleStatusUpdate(byte id) {
		if(id != 3)
			super.handleStatusUpdate(id);
	}

	private void playParticles(int particleCount)
	{
		if (this.world instanceof WorldServer)
		{
			((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + (double)this.height / 1.5D, this.posZ, particleCount, (double)(this.width / 4.0F), (double)(this.height / 4.0F), (double)(this.width / 4.0F), 0.05D, Block.getStateId(Blocks.STONE.getDefaultState()));
		}
	}

	protected SoundEvent getFallSound(int heightIn)
	{
		return SoundEvents.BLOCK_STONE_FALL;
	}

	@Nullable
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.BLOCK_STONE_HIT;
	}

	@Nullable
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.BLOCK_STONE_BREAK;
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return true;
	}

	@Nullable
	public Entity getControllingPassenger()
	{
		return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
	}

	public boolean canBeSteered()
	{
		Entity entity = this.getControllingPassenger();

		if (!(entity instanceof EntityPlayer))
		{
			return false;
		}
		else
		{
			EntityPlayer entityplayer = (EntityPlayer)entity;
			IStrifeData cap = entity.getCapability(MSUCapabilities.STRIFE_DATA, null);

			return entityplayer.isSprinting() && cap.getSelectedSpecibusIndex() >= 0 && cap.getPortfolio()[cap.getSelectedSpecibusIndex()] != null && cap.getPortfolio()[cap.getSelectedSpecibusIndex()].getKindAbstratus() == MSUKindAbstrata.rockkind;
		}
	}
}
