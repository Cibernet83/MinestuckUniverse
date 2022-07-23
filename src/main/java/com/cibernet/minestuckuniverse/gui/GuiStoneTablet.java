package com.cibernet.minestuckuniverse.gui;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public class GuiStoneTablet extends GuiScreen
{
	private static final ResourceLocation TABLET_GUI_TEXTURES = new ResourceLocation(MinestuckUniverse.MODID, "textures/gui/stone_tablet.png");
	private final boolean canEdit;
	//Player
	private final EntityPlayer editingPlayer;
	private final ItemStack tablet;
	//GUI size
	private final int guiWidth = 192;
	private final int guiHeight = 192;
	private boolean isModified;
	private NBTTagString text;
	private int updateCount;
	private List<ITextComponent> cachedComponents;
	//GUI buttons
	private GuiButton buttonDone;
	private GuiButton buttonCancel;

	public GuiStoneTablet(EntityPlayer playerIn, ItemStack tablet, String text, boolean canEdit)
	{
		this.editingPlayer = playerIn;
		this.tablet = tablet;
		this.canEdit = canEdit;
		setText(text, false);
	}

	/**
	 * Processes keystrokes when editing the text of a tablet
	 */
	private void keyTypedInTablet(char typedChar, int keyCode)
	{
		if (GuiScreen.isKeyComboCtrlV(keyCode))
		{
			this.insertText(GuiScreen.getClipboardString());
		}
		else
		{
			switch (keyCode)
			{
				case 14:
					String s = this.getText();

					if (!s.isEmpty())
					{
						this.setText(s.substring(0, s.length() - 1));
					}

					return;
				case 28:
				case 156:
					this.insertText("\n");
					return;
				default:

					if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
					{
						this.insertText(Character.toString(typedChar));
					}
			}
		}
	}

	private void insertText(String in)
	{
		String s = this.getText();
		String s1 = s + in;
		int i = this.fontRenderer.getWordWrappedHeight(s1 + "" + TextFormatting.BLACK + "_", 118);

		if (i <= 128 && s1.length() < 278)
		{
			this.setText(s1);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TABLET_GUI_TEXTURES);
		int i = (this.width - 192) / 2;
		int j = 2;
		this.drawTexturedModalRect(i, 2, 0, 0, 192, 192);

		String text = getText();

		if (this.canEdit)
		{
			if (this.fontRenderer.getBidiFlag())
			{
				text = text + "_";
			}
			else if (this.updateCount / 6 % 2 == 0)
			{
				text = text + "" + TextFormatting.BLACK + "_";
			}
			else
			{
				text = text + "" + TextFormatting.GRAY + "_";
			}
		}

		if (this.cachedComponents == null)
		{
			this.fontRenderer.drawSplitString(text, i + 33, 35, 110, 0);
		}
		else
		{
			int k1 = Math.min(128 / this.fontRenderer.FONT_HEIGHT, this.cachedComponents.size());

			for (int l1 = 0; l1 < k1; ++l1)
			{
				ITextComponent itextcomponent2 = this.cachedComponents.get(l1);
				this.fontRenderer.drawString(itextcomponent2.getUnformattedText(), i + 36, 34 + l1 * this.fontRenderer.FONT_HEIGHT, 0);
			}

			ITextComponent itextcomponent1 = this.getClickedComponentAt(mouseX, mouseY);

			if (itextcomponent1 != null)
			{
				this.handleComponentHover(itextcomponent1, mouseX, mouseY);
			}
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);

		if (canEdit)
			this.keyTypedInTablet(typedChar, keyCode);
	}

	public void setText(String in, boolean modify)
	{
		this.text = new NBTTagString(in);
		this.isModified = modify;
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (mouseButton == 0)
		{
			ITextComponent itextcomponent = this.getClickedComponentAt(mouseX, mouseY);

			if (itextcomponent != null && this.handleComponentClick(itextcomponent))
			{
				return;
			}
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.enabled)
		{
			if (button.id == 0)
			{
				this.mc.displayGuiScreen(null);
				this.sendTabletToServer();
			}
			else if (button.id == 4)
			{
				this.mc.displayGuiScreen(null);
			}
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui()
	{
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);

		if (this.canEdit)
		{
			this.buttonDone = this.addButton(new GuiButton(0, this.width / 2 - 100, 196, 98, 20, I18n.format("gui.done")));
			this.buttonCancel = this.addButton(new GuiButton(4, this.width / 2 + 2, 196, 98, 20, I18n.format("gui.cancel")));
		}
		else
		{
			this.buttonDone = this.addButton(new GuiButton(0, this.width / 2 - 100, 196, 200, 20, I18n.format("gui.done")));
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		super.updateScreen();
		++this.updateCount;
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	private void sendTabletToServer() throws IOException
	{
		if (this.isModified)
		{
			if (this.text != null)
			{
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.STONE_TABLET_REQUEST, this.text.getString()));
				this.mc.displayGuiScreen(null);
			}
		}
	}

	private String getText()
	{
		return this.text.getString();
	}

	public void setText(String in)
	{
		setText(in, true);
	}

	@Nullable
	public ITextComponent getClickedComponentAt(int p_175385_1_, int p_175385_2_)
	{
		if (this.cachedComponents == null)
		{
			return null;
		}
		else
		{
			int i = p_175385_1_ - (this.width - 192) / 2 - 36;
			int j = p_175385_2_ - 2 - 16 - 16;

			if (i >= 0 && j >= 0)
			{
				int k = Math.min(128 / this.fontRenderer.FONT_HEIGHT, this.cachedComponents.size());

				if (i <= 116 && j < this.mc.fontRenderer.FONT_HEIGHT * k + k)
				{
					int l = j / this.mc.fontRenderer.FONT_HEIGHT;

					if (l >= 0 && l < this.cachedComponents.size())
					{
						ITextComponent itextcomponent = this.cachedComponents.get(l);
						int i1 = 0;

						for (ITextComponent itextcomponent1 : itextcomponent)
						{
							if (itextcomponent1 instanceof TextComponentString)
							{
								i1 += this.mc.fontRenderer.getStringWidth(((TextComponentString) itextcomponent1).getText());

								if (i1 > i)
								{
									return itextcomponent1;
								}
							}
						}
					}

					return null;
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
	}

}








