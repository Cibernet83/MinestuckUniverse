package com.cibernet.minestuckuniverse.gui.captchalogue;

import com.cibernet.minestuckuniverse.captchalogue.JujuModus;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.network.captchalogue.JujuModusPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.io.IOException;

public class JujuGuiHandler extends BaseModusGuiHandler
{
	protected GuiButton guiButton;
	protected boolean link = true;
	
	public JujuGuiHandler(JujuModus modus)
	{
		super(modus, 16);
	}
	
	public void initGui()
	{
		super.initGui();
		this.guiButton = new GuiButton(0, (this.width - 256) / 2 + 15, (this.height - 202) / 2 + 175, 120, 20, "");
		this.buttonList.add(this.guiButton);
	}
	
	public void drawScreen(int xcor, int ycor, float f)
	{
		this.guiButton.x = (this.width - 256) / 2 + 15;
		this.guiButton.y = (this.height - 202) / 2 + 175;
		link = ((JujuModus)modus).partnerID == -1;
		this.guiButton.displayString = I18n.format(link ? "gui.jujuLink" : "gui.jujuUnlink", new Object[0]);
		super.drawScreen(xcor, ycor, f);
	}
	
	@Override
	public void updateContent()
	{
		if(!(modus instanceof JujuModus))
		{
			cards.clear();
			return;
		}
		
		NonNullList<ItemStack> stacks = ((JujuModus)modus).getPartnerItems();
		
		if(stacks == null)
			stacks = NonNullList.create();
		
		this.cards.clear();
		this.maxWidth = Math.max(this.mapWidth, 10 + stacks.size() * 21 + (stacks.size() - 1) * 5);
		this.maxHeight = this.mapHeight;
		updateContentSuper();
		int start = Math.max(5, (this.mapWidth - (stacks.size() * 21 + (stacks.size() - 1) * 5)) / 2);
		
		for(int i = 0; i < stacks.size(); ++i) {
			this.cards.add(new GuiCard(stacks.get(i), this, i, start + i * 26, (this.mapHeight - 26) / 2));
		}
	}
	
	@Override
	public int getTextureIndex(GuiCard card)
	{
		return super.getTextureIndex(card) + ((JujuModus)modus).cardTexIndex;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if (button == this.guiButton)
		{
			MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.JUJU_UPDATE, link ? JujuModusPacket.Type.LINK : JujuModusPacket.Type.UNLINK));
		}
		
	}
}
