package com.cibernet.minestuckuniverse.powers;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;

public class MSUClasspectPowers
{
	public static final Table<EnumAspect, EnumClass, MSUPowerBase> powers = HashBasedTable.create();
	
	public static MSUPowerBase machineResize = new PowerMachineResize();
}
