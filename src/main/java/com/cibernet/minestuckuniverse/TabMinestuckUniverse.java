package com.cibernet.minestuckuniverse;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.ItemModus;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.item.ItemMod;

public class TabMinestuckUniverse
{
    public static final CreativeTabs main = new CreativeTabs("minestuckUniverse")
    {
        @Override
        public ItemStack getTabIconItem() {
            return  new ItemStack(MinestuckUniverseBlocks.gristBlockBuild);
        }
    };
    public static final CreativeTabs weapons = new CreativeTabs("minestuckUniverseWeapons")
    {
        @Override
        public String getTabLabel() {
            return MSUConfig.combatOverhaul ? "minestuckWeapons" : super.getTabLabel();
        }

        @Override
        public ItemStack getTabIconItem() {
            return  new ItemStack(MinestuckUniverseItems.batteryBeamBlade);
        }
    };
    public static final CreativeTabs fetchModi = new CreativeTabs("minestuckFetchModi")
    {
        @Override
        public ItemStack getIconItemStack()
        {
            if (Minecraft.getMinecraft().player == null)
                return getTabIconItem();

            NonNullList<ItemStack> modi = NonNullList.create();

            for(ItemStack modus : OreDictionary.getOres("modus"))
                modus.getItem().getSubItems(this, modi);


            if(modi.isEmpty())
                return super.getIconItemStack();

            return modi.get((int) Math.abs((System.currentTimeMillis() / 1000d) % modi.size()));
        }

        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(MinestuckItems.modusCard);
        }

    };
    public static final CreativeTabs godTier = new CreativeTabs("minestuckGodTier")
    {
        @Override
        public ItemStack getTabIconItem() {
            return  new ItemStack(MinestuckUniverseBlocks.gristBlockBuild);
        }
    };
}
