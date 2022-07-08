package com.cibernet.minestuckuniverse.skills;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Skill extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<Skill> implements Comparable<Skill>
{
	protected String unlocalizedName;
	int sortIndex;
	static int sort = 0;

	public Skill()
	{
		this.sortIndex = sort++;
	}

	public abstract String getUnlocalizedName();

	public String getDisplayName()
	{
		return getDisplayComponent().getFormattedText().trim();
	}

	public TextComponentTranslation getDisplayComponent()
	{
		return new TextComponentTranslation(getUnlocalizedName()+".name");
	}

	@SideOnly(Side.CLIENT)
	public String getDisplayTooltip()
	{
		return I18n.format(getUnlocalizedName() + ".tooltip");
	}

	public String getReadRequirements()
	{
		return new TextComponentTranslation(getUnlocalizedName()+".read").getFormattedText();
	}

	public String getUnlockRequirements()
	{
		return new TextComponentTranslation(getUnlocalizedName()+".unlock").getFormattedText();
	}

	public Skill setUnlocalizedName(String unlocalizedName)
	{
		this.unlocalizedName = unlocalizedName;
		return this;
	}

	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		return true;
	}

	public boolean isReadable(World world, EntityPlayer player)
	{
		return true;
	}

	public boolean canUnlock(World world, EntityPlayer player)
	{
		return true;
	}

	public void onUnlock(World world, EntityPlayer player)
	{
	}

	public boolean canUse(World world, EntityPlayer player) { return true; }

	public boolean canDisable() { return true; }

	public int getSortIndex()
	{
		return sortIndex;
	}

	@Override
	public int compareTo(Skill o) {
		return this.sortIndex - o.sortIndex;
	}

	@Override
	public String toString() {
		return getRegistryName() == null ? "MISSING REGISTRY for " + super.toString() : getRegistryName().toString();
	}

	public int getColor() { return 0x77FFEC; }

	public boolean isObtainable()
	{
		return true;
	}
}
