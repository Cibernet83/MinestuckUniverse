package com.cibernet.minestuckuniverse.util;

public enum EnumTechType
{
	OFFENSE("techType.offense", 0xFFAA66), DEFENSE("techType.defense", 0xC3D3D8), UTILITY("techType.utility", 0x66FF6D), PASSIVE("techType.passive", 0x66FFE8);

	public final String unloc;
	public final int color;
	EnumTechType(String unloc, int color)
	{
		this.unloc = unloc;
		this.color = color;
	}
}
