package com.cibernet.minestuckuniverse.skills.abilitech;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.skills.Skill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Abilitech extends Skill
{
	public static final List<Abilitech> ABILITECHS = new ArrayList<>();

	protected long cost;
	String name;

	public Abilitech(String name, long cost)
	{
		super();
		setUnlocalizedName(name);

		ABILITECHS.add(this);
		this.cost = cost;
		this.name = name;
	}

	@Override
	public String getUnlocalizedName() {
		return "tech." + unlocalizedName;
	}
	public List<String> getTags() { return new ArrayList<>(); }


	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, SkillKeyStates.KeyState state, int time) { return false; }
	public boolean onEquippedTick(World world, EntityPlayer player, IBadgeEffects badgeEffects) { return false; }

	public void onEquipped(World world, EntityPlayer player) {}
	public void onUnequipped(World world, EntityPlayer player) {}
	public void onPassiveToggle(World world, EntityPlayer player, boolean active) {}

	public Skill setRegistryName()
	{
		return setRegistryName(MinestuckUniverse.MODID, name);
	}
}
