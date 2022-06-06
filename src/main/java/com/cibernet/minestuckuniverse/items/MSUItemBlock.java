package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.mraof.minestuck.Minestuck;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MSUItemBlock extends ItemBlock implements IRegistryItem
{
    final String registryName;
    protected boolean isSecret = false;

    public MSUItemBlock(Block block, String name, String unlocName)
    {
        super(block);
        this.setUnlocalizedName(unlocName);
        this.setCreativeTab(TabMinestuckUniverse.main);
        registryName = name;
    }

    public MSUItemBlock setSecret()
    {
        isSecret = true;
        return this;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if(!isSecret)
            super.getSubItems(tab, items);
    }

    public MSUItemBlock(Block block, String name)
    {
        this(block, name, name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        String key = getUnlocalizedName()+".tooltip";
        String playerName = Minecraft.getMinecraft().player == null ? "" : Minecraft.getMinecraft().player.getName();
        String str = "";

        if(MSUItemBase.DEDICATED_TOOLTIPS.contains(playerName) && net.minecraft.client.resources.I18n.hasKey(key+"."+playerName))
                str = (I18n.translateToLocal(key+"."+playerName));
        else if(net.minecraft.client.resources.I18n.hasKey(key) && !stack.getItem().getRegistryName().getResourceDomain().equals(Minestuck.MOD_ID))
            str = (I18n.translateToLocal(key));

        if(!str.isEmpty())
            tooltip.add(str);

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void setRegistryName() {
    }

    public static ItemStack getStoredItem(ItemStack stack)
    {
        NBTTagCompound nbt = getOrCreateTag(stack);
        if(!nbt.hasKey("StoredItem") || !(nbt.getTag("StoredItem") instanceof NBTTagCompound))
            return ItemStack.EMPTY;

        return new ItemStack((NBTTagCompound) nbt.getTag("StoredItem"));
    }

    public static ItemStack storeItem(ItemStack stack, ItemStack store)
    {
        NBTTagCompound itemNbt = new NBTTagCompound();
        store.writeToNBT(itemNbt);
        getOrCreateTag(stack).setTag("StoredItem", itemNbt);

        return stack;
    }

    public static NBTTagCompound getOrCreateTag(ItemStack stack)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        if(!stack.hasTagCompound())
            stack.setTagCompound(nbt);
        else nbt = stack.getTagCompound();
        return nbt;
    }

}
