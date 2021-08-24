package com.cibernet.minestuckuniverse.util;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.item.ItemBoondollars;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MSUUtils
{
    public static final int MACHINE_CHASIS_GUI = 0;
    public static final int AUTO_CAPTCHA_GUI = 1;
    public static final int PORKHOLLOW_ATM_GUI = 2;
    public static final int BOONDOLLAR_REGISTER_GUI = 3;
    public static final int STRIFE_CARD_GUI = 4;
    
    public static void registerDeployList()
    {
        DeployList.registerItem("holopad", new ItemStack(MinestuckUniverseBlocks.holopad), new GristSet(GristType.Build, MinestuckUniverse.isArsenalLoaded ? 10000 : 1000), 2);
    }
    
    public static boolean compareCards(ItemStack card1, ItemStack card2, boolean ignoreStacksize)
    {
        ItemStack stack1 = AlchemyRecipes.getDecodedItem(card1);
        ItemStack stack2 = AlchemyRecipes.getDecodedItem(card2);
        if(!card1.isItemEqual(card2))
            return false;
        if(!card1.hasTagCompound() || !card2.hasTagCompound())
            return true;
        if(card1.getTagCompound().getBoolean("punched") != card2.getTagCompound().getBoolean("punched"))
            return false;
        if(!ignoreStacksize && stack1.getCount() != stack2.getCount())
            return false;
        if(stack1.hasTagCompound() != stack2.hasTagCompound())
            return false;
        if(stack1.hasTagCompound() && stack2.hasTagCompound())
            return stack1.getTagCompound().equals(stack2.getTagCompound());
        else return true;
    }
    
    public static void giveBoonItem(EntityPlayer reciever, int value)
    {
        if(value == 0)
            return;
        ItemStack stack = ItemBoondollars.setCount(new ItemStack(MinestuckItems.boondollars), value);
        if(!reciever.addItemStackToInventory(stack))
        {
            EntityItem entity = reciever.dropItem(stack, false);
            if(entity != null)
                entity.setNoPickupDelay();
        } else reciever.inventoryContainer.detectAndSendChanges();
    }

    public static GameType getPlayerGameType(EntityPlayer player)
    {
        if(player.world.isRemote)
            return getPlayerGameTypeClient((EntityPlayerSP) player);
        return ((EntityPlayerMP) player).interactionManager.getGameType();
    }

    @SideOnly(Side.CLIENT)
    private static GameType getPlayerGameTypeClient(EntityPlayerSP player)
    {
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(player.getGameProfile().getId());
        return networkplayerinfo.getGameType();
    }
}
