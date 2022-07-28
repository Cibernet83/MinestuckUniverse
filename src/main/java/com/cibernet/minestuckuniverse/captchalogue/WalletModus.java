package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.BaseModusGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WalletModus extends BaseModus
{
    public static final int CARD_LIMIT = 10;

    @Override
    protected boolean getSort()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public SylladexGuiHandler getGuiHandler()
    {
        if(gui == null)
            gui = new BaseModusGuiHandler(this, 49) {};
        return gui;
    }

    @Override
    public boolean canSwitchFrom(Modus modus) {
        return modus instanceof WalletModus || modus instanceof CrystalBallModus;
    }

    @Override
    public boolean increaseSize() {
        return this.size >= CARD_LIMIT ? false : super.increaseSize();
    }

    @Override
    public void initModus(NonNullList<ItemStack> prev, int size)
    {
        super.initModus(prev, size);

        if(this.size > CARD_LIMIT)
        {
            CaptchaDeckHandler.launchAnyItem(player, new ItemStack(MinestuckItems.captchaCard, this.size-CARD_LIMIT));
            this.size = CARD_LIMIT;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.size = Math.min(CARD_LIMIT, nbt.getInteger("size"));
        this.list = NonNullList.create();

        for(int i = 0; i < this.size && nbt.hasKey("item" + i); ++i) {
            this.list.add(new ItemStack(nbt.getCompoundTag("item" + i)));
        }

        if (this.side.isClient()) {
            this.items = NonNullList.create();
            this.changed = true;
        }

    }
}
