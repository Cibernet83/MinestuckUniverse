package com.cibernet.minestuckuniverse.util;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.entity.EntityBubble;
import com.cibernet.minestuckuniverse.items.ItemAbilitechnosyth;
import com.cibernet.minestuckuniverse.items.godtier.ItemGodTierKit;
import com.google.common.base.Predicates;
import com.mojang.authlib.GameProfile;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.item.ItemBoondollars;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.PlayerDataPacket;
import com.mraof.minestuck.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class MSUUtils
{
	
    public static final int MACHINE_CHASIS_GUI = 0;
    public static final int AUTO_CAPTCHA_GUI = 1;
    public static final int CERAMIC_PORKHOLLOW_GUI = 2;
    public static final int BOONDOLLAR_REGISTER_GUI = 3;
    public static final int STRIFE_CARD_GUI = 4;
    public static final int STONE_TABLET_GUI = 5;
    public static final int GOD_TIER_MEDITATE_UI = 6;
    public static final int GOD_TIER_SASH_UI = 7;
    public static final int ITEM_VOID_UI = 8;
    public static final int GOD_TIER_HOARD_SELECTOR_UI = 9;
    public static final int FRAYMACHINE_UI = 10;
    public static final int SKILL_SHOP_UI = 11;

    public static void registerDeployList()
    {
        DeployList.registerItem("holopad", new ItemStack(MinestuckUniverseBlocks.holopad), new GristSet(GristType.Build, 1000), 2);
        DeployList.registerItem("gt_kit", new GristSet(GristType.Zillium, 20), 0, ItemGodTierKit::isAvailable, ItemGodTierKit::generateKit);
        DeployList.registerItem("fraymachine", new GristSet(GristType.Build, 5000), 0, ItemAbilitechnosyth::isAvailable, sburbConnection -> new ItemStack(MinestuckUniverseBlocks.abilitechnosynth[0]));
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

    public static final int TARGET_REACH = 10;

    public static RayTraceResult rayTraceBlocks(Entity entity, double blockReachDistance)
    {
        Vec3d vec3d = entity.getPositionEyes(1);
        Vec3d vec3d1 = entity.getLook(1);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
        return entity.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
    }

    public static RayTraceResult getMouseOver(EntityPlayer player, double blockReachDistance)
    {
        return getMouseOver(player.world, player, blockReachDistance);
    }
    
    public static RayTraceResult getMouseOver(World world, EntityPlayer player, double blockReachDistance)
    {
    	return getMouseOver(world, player, blockReachDistance, false);
    }

    public static RayTraceResult getMouseOver(World world, EntityPlayer player, double blockReachDistance, boolean getNonCollidables)
    {
    	if(blockReachDistance == player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue())
    		blockReachDistance *= 2;
    	
        if (world != null)
        {
            Entity pointedEntity = null;
            RayTraceResult objectMouseOver = rayTraceBlocks(player, blockReachDistance);
            Vec3d eyePos = player.getPositionEyes(1);
            double blockHitDistance = blockReachDistance;

            if (objectMouseOver != null)
                blockHitDistance = objectMouseOver.hitVec.distanceTo(eyePos);

            Vec3d look = player.getLook(1.0F);
            Vec3d lookPos = eyePos.addVector(look.x * blockReachDistance, look.y * blockReachDistance, look.z * blockReachDistance);
            Vec3d hitPos = null;
            List<Entity> entities = world.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().expand(look.x * blockReachDistance, look.y * blockReachDistance, look.z * blockReachDistance).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, p_apply_1_ -> p_apply_1_ != null && (getNonCollidables || p_apply_1_.canBeCollidedWith())));
            double entityHitDistance = blockHitDistance;

            for (Entity entity : entities) {
                AxisAlignedBB entityAABB = entity.getEntityBoundingBox().grow((double) entity.getCollisionBorderSize());
                RayTraceResult entityResult = entityAABB.calculateIntercept(eyePos, lookPos);

                if (entityAABB.contains(eyePos)) {
                    if (entityHitDistance >= 0.0D) {
                        pointedEntity = entity;
                        hitPos = entityResult == null ? eyePos : entityResult.hitVec;
                        entityHitDistance = 0.0D;
                    }
                }
                else if (entityResult != null) {
                    double eyeToHitDistance = eyePos.distanceTo(entityResult.hitVec);

                    if (eyeToHitDistance < entityHitDistance || entityHitDistance == 0.0D) {
                        if (entity.getLowestRidingEntity() == player.getLowestRidingEntity() && !entity.canRiderInteract()) {
                            if (entityHitDistance == 0.0D) {
                                pointedEntity = entity;
                                hitPos = entityResult.hitVec;
                            }
                        } else {
                            pointedEntity = entity;
                            hitPos = entityResult.hitVec;
                            entityHitDistance = eyeToHitDistance;
                        }
                    }
                }
            }

            if (pointedEntity != null && (entityHitDistance < blockHitDistance || objectMouseOver == null))
            {
                objectMouseOver = new RayTraceResult(pointedEntity, hitPos);
            }

            return objectMouseOver;
        }
        return null;
    }

    public static EntityLivingBase getTargetEntity(EntityPlayer player)
    {
        return getTargetEntity(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());
    }

    public static EntityLivingBase getTargetEntity(EntityPlayer player, double blockReachDistance)
    {
        RayTraceResult rayTraceResult = getMouseOver(player, blockReachDistance);
        return (rayTraceResult != null && rayTraceResult.entityHit instanceof EntityLivingBase) ? (EntityLivingBase) rayTraceResult.entityHit : null;
    }

    public static BlockPos getTargetBlock(EntityPlayer player)
    {
        return getTargetBlock(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());
    }

    public static BlockPos getTargetBlock(EntityPlayer player, double blockReachDistance)
    {
        RayTraceResult rayTraceResult = getMouseOver(player, blockReachDistance);
        return (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) ? rayTraceResult.getBlockPos() : null;
    }


    @SideOnly(Side.CLIENT)
    public static boolean isClientPlayer(Entity entity)
    {
        return Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.equals(entity);
    }

    public static boolean resetGodTier(EntityPlayer player)
    {
        if(!(player instanceof FakePlayer))
        {
            if(!player.world.isRemote)
            {
                IGodTierData data = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
                data.resetBadges();
                data.resetStats(true);
                MinestuckPlayerData.getData(player).echeladder.setProgressEnabled(true);
                data.markForReset();
                data.update();
            }
        }
        return false;
    }

    public static boolean changeTitle(EntityPlayer player, EnumClass heroClass, EnumAspect aspect)
    {
        if(!(player instanceof FakePlayer))
        {
            MinestuckPlayerData.getData(player).title = new Title(heroClass, aspect);
            MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(MinestuckPacket.Type.PLAYER_DATA, PlayerDataPacket.TITLE, heroClass, aspect), player);
            IGodTierData data = player.getCapability(MSUCapabilities.GOD_TIER_DATA, null);
            if(!data.hasMasterControl())
                data.resetTitleBadges(true);
            return true;
        }
        return false;
    }

    public static void onResetGodTier(EntityPlayer player)
    {
        player.capabilities.allowFlying = player.isCreative() || player.isSpectator();
        if(!player.capabilities.allowFlying)
            player.capabilities.isFlying = false;
    }

    public static EntityPlayer getOfflinePlayer(WorldServer world, IdentifierHandler.PlayerIdentifier identifier)
    {
        if(identifier.getPlayer() != null)
            return identifier.getPlayer();

        NBTTagCompound identifierNBT = ((NBTTagCompound) identifier.saveToNBT(new NBTTagCompound(), "id"));
        GameProfile profile;

        if(MinestuckConfig.useUUID)
            profile = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().getProfileByUUID(identifierNBT.getUniqueId("id"));
        else profile = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().getGameProfileForUsername(identifier.getUsername());

        EntityPlayerMP player = new EntityPlayerMP(FMLCommonHandler.instance().getMinecraftServerInstance(), world, profile , new PlayerInteractionManager(world));
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().setPlayerManager(new WorldServer[] {world});
        player.deserializeNBT(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerNBT(player));
        player.mountEntityAndWakeUp();

        return player;
    }
    
    public static boolean isTrulyOnGround(Entity entity)
    {
    	double y = entity.motionY;
    	double oldY = y;
    	if(y != 0)
    	{
            List<AxisAlignedBB> list1 = entity.world.getCollisionBoxes(entity, entity.getEntityBoundingBox().expand(0, y, 0));
    		for(int i = 0; i < list1.size(); i++)
    			y = ((AxisAlignedBB)list1.get(i)).calculateYOffset(entity.getEntityBoundingBox(), y);
    		
    	}
    	return oldY != y && oldY < 0;
    }
}
