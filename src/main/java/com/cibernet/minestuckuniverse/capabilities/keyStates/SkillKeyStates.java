package com.cibernet.minestuckuniverse.capabilities.keyStates;

import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.Abilitech;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

public class SkillKeyStates implements ISkillKeyStates
{
	private KeyState[] keyStates;
	private int[] keyTimes;

	public SkillKeyStates()
	{
		resetKeyStates();
	}

	@Override
	public void updateKeyState(Key key, boolean pressed)
	{
		if (pressed && keyStates[key.ordinal()] != KeyState.HELD)
		{
			keyStates[key.ordinal()] = KeyState.PRESS;
			keyTimes[key.ordinal()] = 0;
		}
		else if (!pressed && keyStates[key.ordinal()] != KeyState.NONE)
			keyStates[key.ordinal()] = KeyState.RELEASED;
	}

	@Override
	public int getKeyTime(Key key)
	{
		return keyTimes[key.ordinal()];
	}

	@Override
	public KeyState getKeyState(Key key)
	{
		return keyStates[key.ordinal()];
	}

	@Override
	public void tickKeyStates()
	{
		for (int i = 0; i < keyStates.length; i++)
		{
			if(keyStates[i] == KeyState.PRESS)
				keyStates[i] = KeyState.HELD;
			else if(keyStates[i] == KeyState.RELEASED)
				keyStates[i] = KeyState.NONE;

			keyTimes[i]++;
		}
	}

	@Override
	public void resetKeyStates() {
		keyStates = new KeyState[Key.values().length];
		keyTimes  = new int[Key.values().length];

		for (int i = 0; i < keyStates.length; i++)
			keyStates[i] = KeyState.NONE;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < keyStates.length; i++)
		{
			keyStates[i] = KeyState.values()[nbt.getInteger(i + "State")];
			keyTimes[i] = nbt.getInteger(i + "Time");
		}
	}

	@Override
	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		for (int i = 0; i < keyStates.length; i++)
		{
			nbt.setInteger(i + "State", keyStates[i].ordinal());
			nbt.setInteger(i + "Time", keyTimes[i]);
		}
		return nbt;
	}

	public enum Key {
		PRIMARY,
		SECONDARY,
		TERTIARY
	}

	public enum KeyState
	{
		PRESS,
		HELD,
		RELEASED,
		NONE
	}

	@SubscribeEvent
	public static void onWorldJoin(EntityJoinWorldEvent event)
	{
		if(event.getEntity().hasCapability(MSUCapabilities.SKILL_KEY_STATES, null))
			event.getEntity().getCapability(MSUCapabilities.SKILL_KEY_STATES, null).resetKeyStates();
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.player.world.isRemote || event.phase == TickEvent.Phase.END)
			return;

		IGodTierData data = event.player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
		//List<Badge> badgeList = data.getAllBadges();

		IBadgeEffects badgeEffects = event.player.getCapability(MSUCapabilities.BADGE_EFFECTS, null);
		ISkillKeyStates keyStates = event.player.getCapability(MSUCapabilities.SKILL_KEY_STATES, null);

		List<Abilitech> passives = new ArrayList<>();

		if(!event.player.isSpectator())
		{
			for(Key key : Key.values())
			{
				Abilitech abilitech = data.getTech(key.ordinal());
				boolean isActive = false;

				if(abilitech == null) continue;

				if(abilitech.canUse(event.player.world, event.player))
				{
					isActive = abilitech.onUseTick(event.player.world, event.player, badgeEffects, keyStates.getKeyState(key), keyStates.getKeyTime(key));

					if(!passives.contains(abilitech) && data.isTechPassiveEnabled(abilitech))
					{
						isActive = abilitech.onEquippedTick(event.player.world, event.player, badgeEffects) || isActive;
						passives.add(abilitech);
					}
				}

				if(!isActive)
					badgeEffects.stopPowerParticles(abilitech.getClass());
			}
		}

		keyStates.tickKeyStates();
	}
}
