package com.cibernet.minestuckuniverse.modSupport.thaumcraft;

import static com.mraof.minestuck.block.MinestuckBlocks.*;
import static com.mraof.minestuck.item.MinestuckItems.*;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectEventProxy;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

public class TCAspects
{
    private final AspectEventProxy proxy;

    public TCAspects(AspectEventProxy proxy) {this.proxy = proxy;}

    public static void regiterAspects(AspectRegistryEvent event)
    {
        TCAspects handler = new TCAspects(event.register);
        handler.registerItems();
        handler.registerBlocks();
    }

    private void registerItems()
    {
        //Armor TODO
        //register(crumplyHat, (new AspectList()).add(Aspect.PROTECT, 10).add(Aspect.CRAFT, 5).add(Aspect.DESIRE, 5));


        //Food
        register(candy, new AspectList().add(Aspect.DESIRE, 1).add(Aspect.ALCHEMY, 1));
        register(beverage, new AspectList().add(Aspect.DESIRE, 2));
        register(bugOnAStick, new AspectList().add(Aspect.PLANT, 1).add(Aspect.LIFE, 1));
        register(chocolateBeetle, new AspectList().add(Aspect.PLANT, 1).add(Aspect.LIFE, 1));
        register(coneOfFlies, new AspectList().add(Aspect.PLANT, 1).add(Aspect.LIFE, 1));
        register(grasshopper, new AspectList().add(Aspect.PLANT, 1).add(Aspect.LIFE, 1));
        register(jarOfBugs, new AspectList().add(Aspect.PLANT, 1).add(Aspect.LIFE, 1));
        register(onion, new AspectList().add(Aspect.PLANT, 5).add(Aspect.LIFE, 1));
        register(desertFruit, new AspectList().add(Aspect.PLANT, 4).add(Aspect.LIFE, 4));
        register(irradiatedSteak, (new AspectList()).add(Aspect.CRAFT, 1).add(Aspect.BEAST, 5).add(Aspect.DEATH, 5));
        register(rockCookie, new AspectList().add(Aspect.EARTH, 5));
        register(fungalSpore, new AspectList().add(Aspect.PLANT, 1).add(Aspect.DARKNESS, 1).add(Aspect.EARTH, 1));
        register(sporeo, new AspectList().add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 2).add(Aspect.DESIRE, 1));
        register(morelMushroom, new AspectList().add(Aspect.PLANT, 5).add(Aspect.DESIRE, 2).add(Aspect.EARTH, 2));
        register(frenchFry, new AspectList().add(Aspect.PLANT, 5).add(Aspect.LIFE, 5).add(Aspect.CRAFT, 2));
        register(strawberryChunk, new AspectList().add(Aspect.PLANT, 5).add(Aspect.LIFE, 5));
        register(surpriseEmbryo, new AspectList().add(Aspect.EXCHANGE, 5).add(Aspect.DARKNESS, 1));

        //Frogs
        register(goldenGrasshopper, new AspectList().add(Aspect.DESIRE, 50).add(Aspect.LIFE, 10).add(Aspect.PLANT, 5));
        register(bugNet, 2, new AspectList().add(Aspect.TOOL, 5).add(Aspect.TRAP, 2));
        register(itemFrog, 0, new AspectList().add(Aspect.BEAST, 5).add(Aspect.LIFE, 5).add(Aspect.VOID, 5));
        register(itemFrog, 1, new AspectList().add(Aspect.BEAST, 5).add(Aspect.LIFE, 5));
        register(itemFrog, 2, new AspectList().add(Aspect.BEAST, 5).add(Aspect.LIFE, 5).add(Aspect.VOID, 5).add(Aspect.FIRE, 4));
        register(itemFrog, 3, new AspectList().add(Aspect.BEAST, 5).add(Aspect.LIFE, 50).add(Aspect.VOID, 500));
        register(itemFrog, 5, new AspectList().add(Aspect.BEAST, 5).add(Aspect.LIFE, 5).add(Aspect.VOID, 5).add(Aspect.DESIRE, 4));
        register(itemFrog, 6, new AspectList().add(Aspect.BEAST, 5).add(Aspect.LIFE, 5).add(Aspect.VOID, 5).add(Aspect.AURA, 4));

        //SBURB utils
        register(boondollars, (new AspectList()).add(Aspect.DESIRE, 1));
        register(rawCruxite, (new AspectList()).add(Aspect.METAL, 5).add(Aspect.ALCHEMY, 15).add(Aspect.MECHANISM, 4));
        register(rawUranium, (new AspectList()).add(Aspect.METAL, 10).add(Aspect.DEATH, 5).add(Aspect.ENERGY, 10));
        register(cruxiteDowel, (new AspectList()).add(Aspect.METAL, 5).add(Aspect.ALCHEMY, 5).add(Aspect.TOOL, 4));
        register(captchaCard, (new AspectList()).add(Aspect.TRAP, 1).add(Aspect.ALCHEMY, 1).add(Aspect.VOID, 1));
        register(disk, (new AspectList().add(Aspect.SENSES, 15).add(Aspect.MECHANISM, 5).add(Aspect.ALCHEMY, 1)));
        register(shunt, new AspectList().add(Aspect.ALCHEMY, 5).add(Aspect.MECHANISM, 5));
        register(captcharoidCamera, new AspectList().add(Aspect.SENSES, 10).add(Aspect.TRAP, 5));

        //Modi TODO
        AspectList modusAspects = new AspectList().add(Aspect.VOID, 15).add(Aspect.TRAP, 5);
        register(modusCard, 0, modusAspects);

        //Records
        AspectList recordAspects = new AspectList().add(Aspect.SENSES, 15).add(Aspect.AIR, 5).add(Aspect.DESIRE, 10);
        register(recordDanceStab, recordAspects.add(Aspect.ENTROPY, 5));
        register(recordEmissaryOfDance, recordAspects.add(Aspect.ORDER, 5));
        register(recordRetroBattle, recordAspects.add(Aspect.AVERSION, 5));

        //Misc.
        register(threshDvd, new AspectList().add(Aspect.SENSES, 5).add(Aspect.DESIRE, 10));
        register(crewPoster, (new AspectList()).add(Aspect.SENSES, 10).add(Aspect.CRAFT, 5).add(Aspect.DARKNESS, 5));
        register(sbahjPoster, (new AspectList()).add(Aspect.SENSES, 1).add(Aspect.CRAFT, 1).add(Aspect.ENTROPY, 10));
        register(shopPoster, (new AspectList()).add(Aspect.SENSES, 10).add(Aspect.CRAFT, 5).add(Aspect.DESIRE, 5));
        register(carvingTool, (new AspectList()).add(Aspect.TOOL, 5));
        register(stoneEyeballs, (new AspectList()).add(Aspect.EARTH, 8));
        register(stoneSlab, (new AspectList()).add(Aspect.EARTH, 5).add(Aspect.MIND, 2));
        register(glowystoneDust, (new AspectList()).add(Aspect.LIGHT, 10).add(Aspect.ENERGY, 5));
        register(fakeArms, (new AspectList()).add(Aspect.MAN, 5));
    }

    private void registerBlocks()
    {
        register(oreUranium, (new AspectList()).add(Aspect.METAL, 10).add(Aspect.DEATH, 5).add(Aspect.ENERGY, 10));
        register(oreCruxite, (new AspectList()).add(Aspect.METAL, 5).add(Aspect.ALCHEMY, 15).add(Aspect.MECHANISM, 4));

    }

    private void register(ItemStack stack, AspectList aspects)       {proxy.registerObjectTag(stack, aspects);}
    private void register(Item item, AspectList aspects)             {register(new ItemStack(item), aspects);}
    private void register(Item item, int meta, AspectList aspects)   {register(new ItemStack(item, 1, meta), aspects);}
    private void register(Block block, AspectList aspects)           {register(new ItemStack(block), aspects);}
    private void register(Block block, int meta, AspectList aspects) {register(new ItemStack(block, 1, meta), aspects);}

}
