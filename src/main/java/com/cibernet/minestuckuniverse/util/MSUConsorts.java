package com.cibernet.minestuckuniverse.util;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.consort.MessageType;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MSUConsorts
{
	public static EnumConsort.MerchantType SHOP_SKILLS = EnumHelper.addEnum(EnumConsort.MerchantType.class, "SKILLS", new Class[0]);;

	public static void setupCustomConsortAttributes()
	{
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.helloWorld"))).type(SHOP_SKILLS);
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.hero", "playerTitle"))).type(SHOP_SKILLS);
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new GodTierMessage("skillShop.godTier"))).type(SHOP_SKILLS);
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.zillystone"))).type(SHOP_SKILLS);
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.abilitechnosynth"))).type(SHOP_SKILLS);
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.skaia"))).type(SHOP_SKILLS).consortReq(entityConsort -> MinestuckDimensionHandler.isSkaia(entityConsort.dimension));
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.skaiaBattle"))).type(SHOP_SKILLS).consortReq(entityConsort -> MinestuckDimensionHandler.isSkaia(entityConsort.dimension));
		//ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.skaiaReckoning"))).type(SHOP_SKILLS).consortReq(entityConsort -> MinestuckDimensionHandler.isSkaia(entityConsort.dimension));
		//ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.ghostCard"))).type(SHOP_SKILLS);
		//ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.innateTransformations"))).type(SHOP_SKILLS);
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.dragonGel"))).type(SHOP_SKILLS);
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.returnMedallion"))).type(SHOP_SKILLS);
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.ghostCard"))).type(SHOP_SKILLS);
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.susanFrog"))).type(SHOP_SKILLS).landTerrain(LandAspectRegistry.fromNameTerrain("frogs"));
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new MessageType.SingleMessage("skillShop.tickingStopwatch"))).type(SHOP_SKILLS).landTerrain(LandAspectRegistry.fromNameTerrain("clockwork"));
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new HeroClassMessage("skillShop.class"))).type(SHOP_SKILLS);
		ConsortDialogue.addMessage(new SkillShopGuiMessage(new PlayerCustomMessage("skillShop.player"))).type(SHOP_SKILLS);
	}

	public static class SkillShopGuiMessage extends MessageType
	{
		protected String nbtName;
		protected MessageType initMessage;

		public SkillShopGuiMessage(MessageType message) {
			this(message.getString(), message);
		}

		public SkillShopGuiMessage(String name, MessageType message) {
			this.nbtName = name;
			this.initMessage = message;
		}

		public String getString() {
			return this.nbtName;
		}

		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			MSUChannelHandler.sendToPlayer(MSUPacket.makePacket(MSUPacket.Type.OPEN_GUI, MSUUtils.SKILL_SHOP_UI, (int) consort.posX,(int) consort.posY,(int) consort.posZ), player);
			return this.initMessage.getMessage(consort, player, chainIdentifier);
		}

		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String chainIdentifier, String fromChain) {
			return null;
		}
	}

	public static class PlayerCustomMessage extends MessageType.SingleMessage
	{

		protected String nbtName;

		public PlayerCustomMessage(String message, String... args)
		{
			super(message, args);
			nbtName = message;
		}

		@Override
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			String name = player.getName();

			if(!I18n.hasKey("consort."+unlocalizedMessage + "." + name))
				return super.getMessage(consort, player, chainIdentifier);

			unlocalizedMessage += "." + name;
			ITextComponent result = super.getMessage(consort, player, chainIdentifier);
			unlocalizedMessage = nbtName;

			return result;
		}
	}

	public static class HeroClassMessage extends MessageType.SingleMessage
	{
		protected String nbtName;

		public HeroClassMessage(String message, String... args)
		{
			super(message, args);
			nbtName = message;
		}

		@Override
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			Title title = MinestuckPlayerData.getTitle(IdentifierHandler.encode(player));

			if(title == null)
				return super.getMessage(consort, player, chainIdentifier);

			unlocalizedMessage += "." + title.getHeroClass().name();
			ITextComponent result = super.getMessage(consort, player, chainIdentifier);
			unlocalizedMessage = nbtName;

			return result;
		}
	}

	public static class GodTierMessage extends MessageType.SingleMessage
	{
		protected String nbtName;

		public GodTierMessage(String message, String... args)
		{
			super(message, args);
			nbtName = message;
		}

		@Override
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			unlocalizedMessage += "." + (player.hasCapability(MSUCapabilities.GOD_TIER_DATA, null) && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier() ? "true" : "false");
			ITextComponent result = super.getMessage(consort, player, chainIdentifier);
			unlocalizedMessage = nbtName;

			return result;
		}
	}

	@SubscribeEvent
	public static void onConsortSpawn(LivingSpawnEvent event)
	{
		if(event.getEntity() instanceof EntityConsort)
		{
			EntityConsort consort = (EntityConsort) event.getEntity();
			if(consort.merchantType == EnumConsort.MerchantType.GENERAL && event.getWorld().rand.nextFloat() < .05f)
			{
				consort.merchantType = MSUConsorts.SHOP_SKILLS;
				consort.getCapability(MSUCapabilities.CONSORT_HATS_DATA, null).setHeadStack(new ItemStack(MinestuckUniverseItems.archmageHat));
			}
		}
	}
}
