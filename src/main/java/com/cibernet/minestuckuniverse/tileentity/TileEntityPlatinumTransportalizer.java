package com.cibernet.minestuckuniverse.tileentity;

import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.util.Teleport;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class TileEntityPlatinumTransportalizer extends TileEntityTransportalizer
{
    private static Random rand = new Random();
    private boolean enabled = true;
    private boolean active = true;
    String id = "";
    private String destId = "";

    public void validate() {
        super.validate();
        if (!this.world.isRemote && this.active) {
            if (this.id.isEmpty()) {
                this.id = this.getUnusedId();
            }

            put(this.id, new Location(this.pos, this.world.provider.getDimension()));
        }

    }

    public void invalidate() {
        super.invalidate();
        if (!this.world.isRemote && this.active) {
            Location location = transportalizers.get(this.id);
            if (location != null && location.equals(new Location(this.pos, this.world.provider.getDimension()))) {
                transportalizers.remove(this.id);
            }
        }

    }

    public void update() {
        if (!this.world.isRemote) {
            if (this.world.isBlockPowered(this.getPos())) {
                if (this.enabled) {
                    this.setEnabled(false);
                }
            } else if (!this.enabled) {
                this.setEnabled(true);
            }

        }
    }

    public String getUnusedId() {
        String unusedId = "";

        do {
            for(int i = 0; i < 4; ++i) {
                unusedId = unusedId + (char)(rand.nextInt(26) + 65);
            }
        } while(transportalizers.containsKey(unusedId));

        return unusedId;
    }

    public static void put(String key, Location location) {
        transportalizers.put(key, location);
    }

    public void teleport(Entity entity) {
        Location location = (Location)transportalizers.get(this.destId);
        if (!this.enabled) {
            entity.timeUntilPortal = entity.getPortalCooldown();
            if (entity instanceof EntityPlayerMP) {
                entity.sendMessage(new TextComponentTranslation("message.transportalizer.transportalizerDisabled", new Object[0]));
            }

        } else {
            if (location != null && location.pos.getY() != -1) {
                WorldServer world = entity.getServer().getWorld(location.dim);
                TileEntityTransportalizer destTransportalizer = (TileEntityTransportalizer)world.getTileEntity(location.pos);
                if (destTransportalizer == null) {
                    Debug.warn("Invalid transportalizer in map: " + this.destId + " at " + location);
                    transportalizers.remove(this.destId);
                    this.destId = "";
                    return;
                }

                if (!destTransportalizer.getEnabled()) {
                    return;
                }

                while(true)
                {
                    IBlockState block0 = this.world.getBlockState(this.pos.up());
                    IBlockState block1 = this.world.getBlockState(this.pos.up(2));
                    if (!block0.getMaterial().blocksMovement() && !block1.getMaterial().blocksMovement()) {
                        block0 = world.getBlockState(location.pos.up());
                        block1 = world.getBlockState(location.pos.up(2));
                        if (!block0.getMaterial().blocksMovement() && !block1.getMaterial().blocksMovement()) {
                            Teleport.teleportEntity(entity, location.dim, (Teleport.ITeleporter)null, (double)destTransportalizer.getPos().getX() + 0.5D, (double)destTransportalizer.getPos().getY() + 0.6D, (double)destTransportalizer.getPos().getZ() + 0.5D);
                            entity.timeUntilPortal = entity.getPortalCooldown();
                            break;
                        }

                        entity.timeUntilPortal = entity.getPortalCooldown();
                        if (entity instanceof EntityPlayerMP) {
                            entity.sendMessage(new TextComponentTranslation("message.transportalizer.destinationBlocked", new Object[0]));
                        }

                        return;
                    }

                    entity.timeUntilPortal = entity.getPortalCooldown();
                    if (entity instanceof EntityPlayerMP) {
                        entity.sendMessage(new TextComponentTranslation("message.transportalizer.blocked", new Object[0]));
                    }

                    return;
                }
            }

        }
    }

    public static void saveTransportalizers(NBTTagCompound tagCompound) {
        NBTTagCompound transportalizerTagCompound = new NBTTagCompound();
        Iterator it = transportalizers.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<String, Location> entry = (Map.Entry)it.next();
            Location location = (Location)entry.getValue();
            NBTTagCompound locationTag = new NBTTagCompound();
            locationTag.setInteger("x", location.pos.getX());
            locationTag.setInteger("y", location.pos.getY());
            locationTag.setInteger("z", location.pos.getZ());
            locationTag.setInteger("dim", location.dim);
            transportalizerTagCompound.setTag((String)entry.getKey(), locationTag);
        }

        tagCompound.setTag("transportalizers", transportalizerTagCompound);
    }

    public static void loadTransportalizers(NBTTagCompound tagCompound) {
        Iterator var1 = tagCompound.getKeySet().iterator();

        while(var1.hasNext()) {
            Object id = var1.next();
            NBTTagCompound locationTag = tagCompound.getCompoundTag((String)id);
            put((String)id, new Location(locationTag.getInteger("x"), locationTag.getInteger("y"), locationTag.getInteger("z"), locationTag.getInteger("dim")));
        }

    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        if (this.active && !this.id.isEmpty()) {
            transportalizers.remove(this.id);
        }

        Location location = (Location)transportalizers.get(id);
        this.id = id;
        if (location != null && (!this.hasWorld() || location.dim != this.getWorld().provider.getDimension() || !location.pos.equals(this.getPos()))) {
            this.active = false;
        } else {
            transportalizers.put(id, new Location(this.getPos(), this.getWorld().provider.getDimension()));
        }

    }

    public String getDestId() {
        return this.destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
        IBlockState state = this.world.getBlockState(this.pos);
        this.markDirty();
        this.world.notifyBlockUpdate(this.pos, state, state, 0);
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        IBlockState state = this.world.getBlockState(this.pos);
        this.markDirty();
        this.world.notifyBlockUpdate(this.pos, state, state, 0);
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.destId = tagCompound.getString("destId");
        this.id = tagCompound.getString("idString");
        if (tagCompound.hasKey("active")) {
            this.active = tagCompound.getBoolean("active");
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setString("idString", this.id);
        tagCompound.setString("destId", this.destId);
        tagCompound.setBoolean("active", this.active);
        return tagCompound;
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 2, this.writeToNBT(new NBTTagCompound()));
    }

    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }
}
