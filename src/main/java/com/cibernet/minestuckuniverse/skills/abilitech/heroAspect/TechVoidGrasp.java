package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.game.GameData;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TechVoidGrasp extends TechHeroAspect
{
	public TechVoidGrasp(String name) {
		super(name, EnumAspect.VOID, EnumTechType.UTILITY);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time)
	{
		if(state != SkillKeyStates.KeyState.PRESS)
			return false;

		player.openGui(MinestuckUniverse.instance, MSUUtils.ITEM_VOID_UI, world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.VOID, 20);

		return true;
	}

	@SubscribeEvent
	public static void onItemExpire(ItemExpireEvent event)
	{
		if (event.getEntity().world.isRemote)
			return;

		GameData.addItemToVoid(event.getEntityItem().getItem());
	}

	private static final HashMap<World, List<EntityItem>> prevItems = new HashMap<>();

	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent event)
	{
		if(event.world.isRemote || event.phase == TickEvent.Phase.END)
			return;

		if(!prevItems.containsKey(event.world))
			prevItems.put(event.world, new ArrayList<>());

		List<EntityItem> items = prevItems.get(event.world);

		for(EntityItem item : items)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			item.writeEntityToNBT(nbt);
			int health = nbt.getInteger("Health");

			if(health <= 0 || (item.posY <= -64 && !item.isDead))
			{
				GameData.addItemToVoid(item.getItem().copy());
				item.setDead();
			}
		}

		items.clear();
		items.addAll(event.world.getEntities(EntityItem.class, e -> true));
	}
}
