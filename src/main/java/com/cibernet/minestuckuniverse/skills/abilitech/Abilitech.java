package com.cibernet.minestuckuniverse.skills.abilitech;

import com.cibernet.minestuckuniverse.MSUConfig;
import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.skills.Skill;
import com.cibernet.minestuckuniverse.util.EnumTechType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Abilitech extends Skill
{
	public static final List<Abilitech> ABILITECHS = new ArrayList<>();
	protected final EnumTechType[] techTypes;

	String name;
	public boolean isSuper = false;

	public Abilitech(String name, EnumTechType... techTypes)
	{
		super();
		setUnlocalizedName(name);

		ABILITECHS.add(this);
		this.name = name;
		this.techTypes = techTypes;
	}

	@Override
	public String getUnlocalizedName() {
		return "tech." + unlocalizedName;
	}

	public List<String> getTags()
	{
		List<String > list = new ArrayList<>();
		for(EnumTechType type : techTypes)
			list.add("@"+type.name()+"@");
		return list;
	}


	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time) { return false; }
	public boolean onPassiveTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot) { return false; }

	public void onEquipped(World world, EntityPlayer player, int techSlot) {}
	public void onUnequipped(World world, EntityPlayer player, int techSlot) {}
	public void onPassiveToggle(World world, EntityPlayer player, boolean active) {}

	public Skill setRegistryName()
	{
		return setRegistryName(MinestuckUniverse.MODID, name);
	}
	
	public boolean isUsableExternally(World world, EntityPlayer player) {return canUse(world, player) && !Arrays.asList(MSUConfig.abilitechExternalUseBlacklist).contains(getRegistryName().toString());}

	@Override
	public boolean isObtainable() {
		return !isSuper;
	}

	public Abilitech setSuper(boolean v)
	{
		isSuper = v;
		return this;
	}
}
