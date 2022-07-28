package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.BaseModusGuiHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.cibernet.minestuckuniverse.captchalogue.WalletModus.CARD_LIMIT;

public class CrystalBallModus extends EightBallModus
{
    @Override
    @SideOnly(Side.CLIENT)
    public SylladexGuiHandler getGuiHandler()
    {
        if(gui == null)
            gui = new BaseModusGuiHandler(this, 50) {};
        return super.getGuiHandler();
    }

    @Override
    public boolean canSwitchFrom(Modus modus) {
        return modus instanceof WalletModus || modus instanceof CrystalBallModus;
    }

    @Override
    protected Item getEightBallItem() {
        return MinestuckUniverseItems.walletBall;
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
