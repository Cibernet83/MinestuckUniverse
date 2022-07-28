package com.cibernet.minestuckuniverse.capabilities.keyStates;

import com.cibernet.minestuckuniverse.capabilities.IMSUCapabilityBase;
import net.minecraft.entity.player.EntityPlayer;

public interface ISkillKeyStates extends IMSUCapabilityBase<EntityPlayer>
{
	void updateKeyState(SkillKeyStates.Key key, boolean pressed);
	int getKeyTime(SkillKeyStates.Key key);
	SkillKeyStates.KeyState getKeyState(SkillKeyStates.Key key);
	void tickKeyStates();
	void resetKeyStates();
}
