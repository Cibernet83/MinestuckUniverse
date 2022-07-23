package com.cibernet.minestuckuniverse.captchalogue;

import com.cibernet.minestuckuniverse.gui.captchalogue.BookGuiHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.client.gui.captchalouge.SylladexGuiHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BookModus extends BaseModus
{
	public int page = 0;
	public String bookName = "";
	
	public ItemStack createBook()
	{
		ItemStack cards = new ItemStack(MinestuckItems.captchaCard);
		if(list.isEmpty() && size <= cards.getMaxStackSize())
		{
			cards.setCount(size);
			return cards;
		}
		
		ItemStack book = new ItemStack(MinestuckUniverseItems.captchalogueBook, 1, MinestuckPlayerData.getData(player).color+1);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("BookInfo", writeToNBT(new NBTTagCompound()));
		nbt.setString("Author", player.getDisplayNameString());
		book.setTagCompound(nbt);
		
		if(!bookName.isEmpty())
			book.setStackDisplayName(bookName);
		
		return book;
	}
	
	public boolean readBook(ItemStack book)
	{
		if(!book.hasTagCompound() || !book.getTagCompound().hasKey("BookInfo"))
			return false;
		readFromNBT(book.getTagCompound().getCompoundTag("BookInfo"));
		
		page = 0;
		if(book.hasDisplayName())
			bookName = book.getDisplayName();
		
		return true;
	}
	
	public void clear()
	{
		list.clear();
		size = 0;
		page = 0;
		bookName = "";
	}
	
	@Override
	public boolean putItemStack(ItemStack stack)
	{
		if(stack.getItem().equals(MinestuckUniverseItems.captchalogueBook))
		{
			ItemStack currBook = createBook();
			boolean result = readBook(stack);
			
			if(result)
			{
				if(!player.addItemStackToInventory(currBook))
					CaptchaDeckHandler.launchAnyItem(player, currBook);
			}
			return result;
		}
		
		return super.putItemStack(stack);
	}
	
	@Nonnull
	@Override
	public ItemStack getItem(int id, boolean asCard)
	{
		ItemStack result = super.getItem(id, asCard);
		if(id == CaptchaDeckHandler.EMPTY_CARD || asCard)
			page = (int) Math.max(0, Math.min(Math.floor((getSize()-1)/2.0), page));
		
		return result;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		page = nbt.getInteger("Page");
		bookName = nbt.getString("BookName");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Page", page);
		nbt.setString("BookName", bookName);
		
		return super.writeToNBT(nbt);
	}
	
	@Override
	protected boolean getSort()
	{
		return true;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public SylladexGuiHandler getGuiHandler()
	{
		if(gui == null)
			gui = new BookGuiHandler(this);
		return gui;
	}
}
