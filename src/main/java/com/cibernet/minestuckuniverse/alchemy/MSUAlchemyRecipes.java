package com.cibernet.minestuckuniverse.alchemy;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.GristRegistry;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftInvHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.items.ItemsTC;

import static com.mraof.minestuck.alchemy.GristType.*;
import static com.cibernet.minestuckuniverse.alchemy.MinestuckUniverseGrist.*;
import static com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks.*;

public class MSUAlchemyRecipes
{
    public static void registerRecipes()
    {
        registerVanilla();
        registerMSU();

        if(MinestuckUniverse.isThaumLoaded) registerThaumcraft();

        if(!MinestuckUniverse.isArsenalLoaded) registerArsenalFallback();
    }

    public static void registerVanilla()
    {

    }

    public static void registerMSU()
    {
        //Grist Conversions
        GristRegistry.addGristConversion(new ItemStack(magicBlock), new GristSet(new GristType[] {Flux}, new int[] {1}));

        //Alchemy
    }
    public static void registerThaumcraft()
    {
        //Grist Conversions
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.amber), new GristSet(Amber, 8));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.thaumonomicon), new GristSet(new GristType[] {Flux, Iodine, Chalk}, new int[] {24, 5, 2}));
        GristRegistry.addGristConversion(new ItemStack(ItemsTC.salisMundus), new GristSet(Flux, 8));

        //Alchemy
    }

    public static void registerArsenalFallback()
    {
        if(MinestuckUniverse.isBotaniaLoaded)
        {

        }
    }

    public static void addGristConversion(String modid, String itemid, GristSet grist)
    {
        Item i = Item.REGISTRY.getObject(new ResourceLocation(modid, itemid));
        GristRegistry.addGristConversion(new ItemStack(i), grist);
    }

}
