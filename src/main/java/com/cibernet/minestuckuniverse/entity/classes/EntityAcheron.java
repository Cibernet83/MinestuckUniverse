package com.cibernet.minestuckuniverse.entity.classes;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import net.minecraft.world.World;

public class EntityAcheron extends EntityUnderling
{
	public EntityAcheron(World world)
	{
		super(world);
		this.setSize(2.0F, 2.5F);
		this.stepHeight = 1.0F;
	}
	
	protected void initEntityAI() {
		super.initEntityAI();
		EntityAIAttackOnCollideWithRate aiAttack = new EntityAIAttackOnCollideWithRate(this, 0.3F, 40, false);
		aiAttack.setDistanceMultiplier(1.2F);
		this.tasks.addTask(3, aiAttack);
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(this.type, 10.0F);
	}
	
	@Override
	protected float getKnockbackResistance()
	{
		return 0.8f;
	}
	
	@Override
	protected double getWanderSpeed()
	{
		return 0.2;
	}
	
	@Override
	protected double getAttackDamage()
	{
		return 25;
	}
	
	@Override
	protected int getVitalityGel()
	{
		return this.rand.nextInt(3) + 30;
	}
	
	@Override
	protected String getUnderlingName()
	{
		return "Acheron";
	}
	
	@Override
	protected float getMaximumHealth()
	{
		return this.type != null ? 15.0F * this.type.getPower() + 200.0F : 1.0F;
	}
}
