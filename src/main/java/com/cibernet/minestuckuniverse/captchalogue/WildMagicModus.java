package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.BaseModusGuiHandler;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class WildMagicModus extends BaseModus
{
	private static final int manaCost = 3;
	
	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		if(asCard)
			return super.getItem(id, asCard);
		
		if(list.isEmpty() || (id >= 0 && list.get(id).isEmpty()))
			return ItemStack.EMPTY;
		
		if(player.getFoodStats().getFoodLevel() <= manaCost)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.exhausted"), false);
			return ItemStack.EMPTY;
		}
		
		float luck = player.getLuck();
		
		int roll = Math.max(0,Math.min(20,player.world.rand.nextInt(20 - (int)Math.abs(luck))+1 + (int)(luck > 0 ? luck : 0)));
		
		player.addExhaustion(manaCost*3);
		player.sendStatusMessage(new TextComponentTranslation("status.wildMagicRoll", roll), false);
		
		Potion effect = null;
		int amp = 1;
		boolean giveItem = false;
		boolean clearEffects = false;
		
		switch(roll)
		{
			case 1: case 2:
				effect = MobEffects.WITHER;
			break;
			case 3:
				effect = MobEffects.POISON;
				amp = 0;
				break;
			case 4:
				effect = MobEffects.SLOWNESS;
			break;
			case 5:	case 6:
				effect = MobEffects.SLOWNESS;
				giveItem = true;
			break;
			case 9:
				giveItem = true;
			case 7: case 8:
				clearEffects = true;
				break;
			case 10:case 11:case 12:case 13:case 14:
				giveItem = true;
				break;
			case 15:
				effect = MobEffects.WATER_BREATHING;
				amp = 0;
				giveItem = true;
				break;
			case 16: case 17:
				effect = MobEffects.SPEED;
				giveItem = true;
			break;
			case 18: case 19:
				effect = MobEffects.REGENERATION;
				amp = 2;
				giveItem = true;
			break;
			case 20:
				effect = MobEffects.ABSORPTION;
				amp = 3;
				giveItem = true;
				break;
				
		}
		
		if(clearEffects)
			player.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
		
		if(effect != null)
		{
			int effectTime = player.getActivePotionEffect(effect) != null ? player.getActivePotionEffect(effect).getDuration() : 0;
			player.addPotionEffect(new PotionEffect(effect, effectTime+100, amp));
		}
		return giveItem ? super.getItem(id, asCard) : ItemStack.EMPTY;
		
	}
	
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
			gui = new BaseModusGuiHandler(this, 0) {};
		
		return gui;
	}
}
