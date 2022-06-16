package com.cibernet.minestuckuniverse.badges;

import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.network.MSUPacket;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BadgeBuilder extends BadgeLevel
{
	public BadgeBuilder() {
		super("builderBadge", 7);
	}

	@Override
	public boolean canUnlock(World world, EntityPlayer player)
	{
		GristSet cost = new GristSet(GristType.Build, 20000);
		if(Badge.findItem(player, new ItemStack(MinestuckUniverseItems.battlepickOfZillydew, 1), false) && GristHelper.canAfford(MinestuckPlayerData.getGristSet(player), cost))
		{
			Badge.findItem(player, new ItemStack(MinestuckUniverseItems.battlepickOfZillydew, 1), true);

			IdentifierHandler.PlayerIdentifier pid = IdentifierHandler.encode(player);
			GristHelper.decrease(pid, cost);
			MinestuckPlayerTracker.updateGristCache(pid);

			return true;
		}
		return false;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		if(canEditDrag(event.getEntityPlayer()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void baba(ClientChatEvent event)
	{
		MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.UPDATE_CONFIG));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.player == null || event.phase == TickEvent.Phase.END)
			return;

		EntityPlayer player = mc.player;
		IBadgeEffects cap = player.getCapability(MSUCapabilities.BADGE_EFFECTS, null);
		boolean isDragging = cap.isEditDragging();
		boolean isDown = mc.gameSettings.keyBindUseItem.isKeyDown();

		if (isDown)
		{
			if(!canEditDrag(player))
			{
				cap.setEditDragging(false);
				cap.setEditPos1(null);
				cap.setEditPos2(null);
				return;
			}

			if (!isDragging)
			{
				RayTraceResult rayTrace = player.rayTrace(mc.playerController.getBlockReachDistance(), mc.getRenderPartialTicks());
				if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
				{
					cap.setEditPos1(rayTrace.getBlockPos().offset(rayTrace.sideHit, player.world.getBlockState(rayTrace.getBlockPos()).getBlock().isReplaceable(player.world, rayTrace.getBlockPos()) ? 0 : 1));
					cap.setEditTraceHit(rayTrace.hitVec);
					cap.setEditTraceFacing(rayTrace.sideHit);
				}
			}

			if (cap.getEditPos1() != null) {

				RayTraceResult rayTrace = player.rayTrace(mc.playerController.getBlockReachDistance(), mc.getRenderPartialTicks());
				BlockPos pos2;
				if (rayTrace.getBlockPos() == null) {
					Vec3d vec3d = player.getPositionEyes(mc.getRenderPartialTicks());
					Vec3d vec3d1 = player.getLook(mc.getRenderPartialTicks());
					Vec3d vec3d2 = vec3d.addVector(vec3d1.x * mc.playerController.getBlockReachDistance(), vec3d1.y * mc.playerController.getBlockReachDistance(), vec3d1.z * mc.playerController.getBlockReachDistance());
					pos2 = new BlockPos(vec3d2.x, vec3d2.y, vec3d2.z);
				} else pos2 = rayTrace.getBlockPos().offset(rayTrace.sideHit, player.world.getBlockState(rayTrace.getBlockPos()).getBlock().isReplaceable(player.world, rayTrace.getBlockPos()) ? 0 : 1);

				BlockPos pos1 = cap.getEditPos1();
				ItemStack stack = ((player.getHeldItemMainhand().getItem() instanceof ItemBlock) ? player.getHeldItemMainhand() : player.getHeldItemOffhand());

				if((Math.max(pos1.getX(),pos2.getX())-Math.min(pos1.getX(), pos2.getX())+1)*
						(Math.max(pos1.getY(),pos2.getY())-Math.min(pos1.getY(), pos2.getY())+1)*
						(Math.max(pos1.getZ(),pos2.getZ())-Math.min(pos1.getZ(), pos2.getZ())+1) <= (player.isCreative() ? 256 : stack.getCount()))
					cap.setEditPos2(pos2);
			}
		} else if (isDragging)
		{
			if (cap.getEditPos1() != null)
			{
				MSUChannelHandler.sendToServer(MSUPacket.makePacket(MSUPacket.Type.EDIT_FILL_BLOCKS, cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceFacing()));
			}
			cap.setEditPos1(null);
			cap.setEditPos2(null);
		}

		cap.setEditDragging(isDown);
	}

	private static boolean canEditDrag(EntityPlayer player)
	{
		return ((player.world.isRemote ? ClientEditHandler.isActive() : ServerEditHandler.getData(player) != null)
				|| (player.getCapability(MSUCapabilities.GOD_TIER_DATA, null) != null && player.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isBadgeActive(MSUBadges.BUILDER_BADGE)))
				&& ((player.getHeldItemMainhand().getItem() instanceof ItemBlock) || (player.getHeldItemOffhand().getItem() instanceof ItemBlock));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void renderOutline(RenderWorldLastEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();

		if (mc.player != null && mc.getRenderViewEntity() == mc.player) {
			RayTraceResult rayTraceResult = mc.objectMouseOver;

			EntityPlayerSP player = mc.player;
			IBadgeEffects cap = player.getCapability(MSUCapabilities.BADGE_EFFECTS, null);
			float partialTicks = event.getPartialTicks();
			double d1 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
			double d2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
			double d3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;


			if (cap.isEditDragging() && cap.getEditPos1() != null)
			{
				BlockPos posA = cap.getEditPos1();
				BlockPos posB = cap.getEditPos2();

				if(posB == null && rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK)
					posB = rayTraceResult.getBlockPos();

				if(posB != null)
				{
					AxisAlignedBB boundingBox = new AxisAlignedBB(Math.min(posA.getX(), posB.getX()), Math.min(posA.getY(), posB.getY()), Math.min(posA.getZ(), posB.getZ()),
							Math.max(posA.getX(), posB.getX())+1, Math.max(posA.getY(), posB.getY())+1, Math.max(posA.getZ(), posB.getZ())+1).offset(-d1, -d2, -d3).grow(0.002);

					GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
					GlStateManager.glLineWidth(2.0F);
					GlStateManager.disableTexture2D();
					GlStateManager.depthMask(false);

					RenderGlobal.drawSelectionBoundingBox(boundingBox, 0, 1, 0, 0.5F);

					GlStateManager.depthMask(true);
					GlStateManager.enableTexture2D();
					GlStateManager.disableBlend();
				}

			}
		}
	}
}
