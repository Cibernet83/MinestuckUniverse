package com.cibernet.minestuckuniverse.skills.abilitech;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates.KeyState;
import com.cibernet.minestuckuniverse.entity.EntityMSUThrowable;
import com.cibernet.minestuckuniverse.items.MSUThrowableBase;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.cibernet.minestuckuniverse.items.properties.throwkind.IPropertyThrowable;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.skills.TechBoondollarCost;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TechSling extends TechBoondollarCost
{

	public TechSling(String name, long cost)
	{
		super(name, cost, EnumTechType.OFFENSE);
	}
	
	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == KeyState.NONE)
			return false;
		
		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 2)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}
		
		time = Math.min(20, time);
		if(time < 20)
			badgeEffects.setFOV(1 + badgeEffects.getFOV());
		
		if(state == KeyState.RELEASED)
		{
			badgeEffects.setFOV(badgeEffects.getFOV() - time);
			
			Modus modus = MinestuckPlayerData.getData(player).modus;
			ItemStack stack = modus.getItem(world.rand.nextInt(modus.getSize()), false);
			if(stack == ItemStack.EMPTY)
				return false;
			
			EntityMSUThrowable proj = new EntityMSUThrowable(world, player, stack);
			proj.setPlainHit(true);
			if(stack.getItem() instanceof MSUThrowableBase)
			{
				for(WeaponProperty p : ((MSUThrowableBase) stack.getItem()).getProperties(stack))
					if(p instanceof IPropertyThrowable)
						((IPropertyThrowable) p).onProjectileThrow(proj, player, stack);
			}
			
			if (!world.isRemote)
			{
				proj.shoot(player, player.rotationPitch, player.rotationYaw, 0, time * .075F, 0);
				world.spawnEntity(proj);
				MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_MODUS, CaptchaDeckHandler.writeToNBT(modus)), player);
			}
			
			if (!player.isCreative())
				player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 2);
		}
		return true;
	}
	
	@Override
	public boolean isUsableExternally(World world, EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() >= 2 && super.isUsableExternally(world, player);
	}
}
