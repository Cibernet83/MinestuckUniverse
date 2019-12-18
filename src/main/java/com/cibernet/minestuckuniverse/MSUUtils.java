package com.cibernet.minestuckuniverse;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.editmode.DeployList;
import net.minecraft.item.ItemStack;

public class MSUUtils
{
    public static final int MACHINE_CHASIS_GUI = 0;
    
    public static void registerDeployList()
    {
        DeployList.registerItem("holopad", new ItemStack(MinestuckUniverseBlocks.holopad), new GristSet(GristType.Build, MinestuckUniverse.isArsenalLoaded ? 10000 : 1000), 2);
    }
}
