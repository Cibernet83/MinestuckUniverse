package com.cibernet.minestuckuniverse.util;

import com.mraof.minestuck.util.EnumClass;

import static com.mraof.minestuck.util.EnumClass.*;

public enum EnumRoles
{
	ACTIVE, PASSIVE, NEUTRAL;

	public static EnumRoles getRoleFromClass(EnumClass heroClass)
	{
		if(     heroClass.equals(THIEF)     || heroClass.equals(PRINCE) || heroClass.equals(WITCH)  ||
				heroClass.equals(KNIGHT)    || heroClass.equals(MAGE)   || heroClass.equals(SYLPH)   ||
				heroClass.equals(LORD))
			return ACTIVE;
		else if(heroClass.equals(ROGUE)     || heroClass.equals(BARD)   || heroClass.equals(HEIR)   ||
				heroClass.equals(PAGE)      || heroClass.equals(SEER)   || heroClass.equals(MAID)  ||
				heroClass.equals(MUSE))
			return PASSIVE;
		else
			return NEUTRAL;
	}
}
