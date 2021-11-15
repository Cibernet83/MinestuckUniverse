package com.cibernet.minestuckuniverse.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public abstract class MSUPacket
{
    protected ByteBuf data = Unpooled.buffer();


        public MSUPacket() {
        }

        public static MSUPacket makePacket(MSUPacket.Type type, Object... dat) {
            return type.make().generatePacket(dat);
        }

        public static String readLine(ByteBuf data) {
            StringBuilder str = new StringBuilder();

            while(data.readableBytes() > 0) {
                char c = data.readChar();
                if (c == '\n') {
                    break;
                }

                str.append(c);
            }

            return str.toString();
        }

        public static void writeString(ByteBuf data, String str) {
            for(int i = 0; i < str.length(); ++i) {
                data.writeChar(str.charAt(i));
            }

        }

        public abstract MSUPacket generatePacket(Object... args);

        public abstract MSUPacket consumePacket(ByteBuf data);

        public abstract void execute(EntityPlayer player);

        public abstract EnumSet<Side> getSenderSide();

    public static enum Type
    {
        MACHINE_CHASSIS(MachineChassisPacket.class),
        ATM(PorkhollowAtmPacket.class),
        VAULT(BoondollarRegisterPacket.class),
        FLIGHT_EFFECT(StopFlightEffectPacket.class),
        BUILD_INHIBIT_EFFECT(StopBuildInhibitEffectPacket.class),
        RESET_COOLDOWN(ResetCooldownPacket.class),
        UPDATE_STRIFE(UpdateStrifeDataPacket.class),
        ASSIGN_STRIFE(AssignStrifePacket.class),
        RETRIEVE_STRIFE(RetrieveStrifeCardPacket.class),
        SET_ACTIVE_STRIFE(SetActiveStrifePacket.class),
        SWAP_OFFHAND_STRIFE(SwapOffhandStrifePacket.class),
        UPDATE_BEAMS(UpdateBeamDataPacket.class),
        LEFT_CLICK_EMPTY(LeftClickEmptyPacket.class),
        ROCKET_BOOTS(RocketBootsPacket.class),
        ;

        Class<? extends MSUPacket> packetType;

        private Type(Class<? extends MSUPacket> packetClass) {
            this.packetType = packetClass;
        }

        MSUPacket make() {
            try {
                return (MSUPacket)this.packetType.newInstance();
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }
}
