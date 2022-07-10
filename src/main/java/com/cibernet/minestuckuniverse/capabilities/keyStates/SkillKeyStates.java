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
	private KeyState[] receivedKeyStates;
	private KeyState[] keyStates;
	private int[] keyTimes;

	public SkillKeyStates()
	{
		resetKeyStates();
	}

	@Override
	public void updateKeyState(Key key, boolean pressed)
	{
		if (pressed && receivedKeyStates[key.ordinal()] != KeyState.HELD)
			receivedKeyStates[key.ordinal()] = KeyState.PRESS;
		else if (!pressed && receivedKeyStates[key.ordinal()] != KeyState.NONE)
			receivedKeyStates[key.ordinal()] = KeyState.RELEASED;
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
		for (int i = 0; i < Key.values().length; i++)
		{
			if(receivedKeyStates[i] == KeyState.PRESS)
				receivedKeyStates[i] = KeyState.HELD;
			else if(receivedKeyStates[i] == KeyState.RELEASED)
				receivedKeyStates[i] = KeyState.NONE;
			
			if(keyStates[i] != receivedKeyStates[i])
				keyStates[i] = KeyState.values()[(keyStates[i].ordinal() + 1) % KeyState.values().length];
			
			if(keyStates[i] == KeyState.PRESS)
				keyTimes[i] = 0;
			else
				keyTimes[i]++;
		}
	}

	@Override
	public void resetKeyStates() {
		receivedKeyStates = new KeyState[Key.values().length];
		keyStates = new KeyState[Key.values().length];
		keyTimes  = new int[Key.values().length];

		for (int i = 0; i < Key.values().length; i++)
		{
			receivedKeyStates[i] = KeyState.NONE;
			keyStates[i] = KeyState.NONE;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		for (int i = 0; i < keyStates.length; i++)
		{
			receivedKeyStates[i] = KeyState.values()[nbt.getInteger(i + "Received")];
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
			nbt.setInteger(i + "Received", receivedKeyStates[i].ordinal());
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
		List<Abilitech> actions = new ArrayList<>();

		for(Key key : Key.values())
		{
			Abilitech abilitech = data.getTech(key.ordinal());
			if(abilitech == null) continue;
			
			boolean isActive = false;

			if(!event.player.isSpectator() && !event.player.isDead && !badgeEffects.isTimeStopped() && !badgeEffects.isSoulShocked())
			{
				if(abilitech.canUse(event.player.world, event.player))
				{
					isActive = abilitech.onUseTick(event.player.world, event.player, badgeEffects, key.ordinal(), keyStates.getKeyState(key), keyStates.getKeyTime(key));
	
					if(isActive)
						actions.add(abilitech);
					
					if(!passives.contains(abilitech) && data.isTechPassiveEnabled(abilitech))
					{
						isActive = abilitech.onPassiveTick(event.player.world, event.player, badgeEffects, key.ordinal()) || isActive;
						passives.add(abilitech);
					}
				}
			}

			if(!isActive && !actions.contains(abilitech) && badgeEffects != null)
				badgeEffects.stopPowerParticles(abilitech.getClass());
		}
		if(!event.player.isDead && !badgeEffects.isTimeStopped() && !badgeEffects.isSoulShocked())
			keyStates.tickKeyStates();
	}
}
