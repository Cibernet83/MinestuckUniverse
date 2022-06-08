package com.cibernet.minestuckuniverse.gui;

import static com.cibernet.minestuckuniverse.util.MSUUtils.*;

import com.cibernet.minestuckuniverse.gui.container.ContainerAutoCaptcha;
import com.cibernet.minestuckuniverse.gui.container.ContainerMachineChasis;
import com.cibernet.minestuckuniverse.tileentity.TileEntityAutoCaptcha;
import com.cibernet.minestuckuniverse.tileentity.TileEntityBoondollarRegister;
import com.cibernet.minestuckuniverse.tileentity.TileEntityMachineChasis;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class MSUGuiHandler implements IGuiHandler
{
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
        switch(ID)
        {
            case MACHINE_CHASIS_GUI:
                return new ContainerMachineChasis(player.inventory, (TileEntityMachineChasis) te);
            case AUTO_CAPTCHA_GUI:
                return new ContainerAutoCaptcha(player.inventory, (TileEntityAutoCaptcha) te);

        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
        switch(ID)
        {
            case MACHINE_CHASIS_GUI:
                return new GuiMachineChasis(player.inventory, (TileEntityMachineChasis) te);
            case AUTO_CAPTCHA_GUI:
                return new GuiAutoCaptcha(player.inventory, (TileEntityAutoCaptcha) te);
            case CERAMIC_PORKHOLLOW_GUI:
                return new GuiCeramicPorkhollow(player);
            case BOONDOLLAR_REGISTER_GUI:
                return new GuiBoondollarRegister(player, (TileEntityBoondollarRegister) te);
            case STRIFE_CARD_GUI:
                return new GuiStrifeCard(player);
            case STONE_TABLET_GUI:
                EnumHand hand = EnumHand.OFF_HAND;
                ItemStack stack = player.getHeldItemMainhand();
                ItemStack tablet = new ItemStack(MinestuckItems.stoneSlab);
                String text = "";
                if (!stack.isItemEqual(tablet))
                {
                    hand = EnumHand.MAIN_HAND;
                    if (!(stack = player.getHeldItemOffhand()).isItemEqual(tablet))
                        return null;
                }

                if (stack.hasTagCompound())
                {
                    text = stack.getTagCompound().getString("text");
                }

                boolean canEdit = player.getHeldItem(hand).isItemEqual(new ItemStack(MinestuckItems.carvingTool));
                return new GuiStoneTablet(player, player.getHeldItemMainhand(), text, canEdit);

        }
        return null;
    }
}
