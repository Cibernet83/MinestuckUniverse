package com.cibernet.minestuckuniverse.util;

public enum EnumTechType
{
	OFFENSE("offense", 0xFFAA66), DEFENSE("defense", 0x66FF6D), UTILITY("utility", 0x66FF6D), PASSIVE("passive", 0x66FFE8);

	public final String unloc;
	public final int color;
	EnumTechType(String unloc, int color)
	{
		this.unloc = unloc;
		this.color = color;
	}
}
