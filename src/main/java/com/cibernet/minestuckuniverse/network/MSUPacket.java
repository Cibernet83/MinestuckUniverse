package com.cibernet.minestuckuniverse.network;

import com.mraof.minestuck.network.*;
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

        public abstract MSUPacket generatePacket(Object... var1);

        public abstract MSUPacket consumePacket(ByteBuf var1);

        public abstract void execute(EntityPlayer var1);

        public abstract EnumSet<Side> getSenderSide();

    public static enum Type
    {
        MACHINE_CHASSIS(MachineChassisPacket.class),
        ATM(PorkhollowAtmPacket.class),
        HERO_POWER(HeroPowerPacket.class),
        ;

        Class<? extends MSUPacket> packetType;

        Type(Class<? extends MSUPacket> packetClass) {
            this.packetType = packetClass;
        }

        MSUPacket make() {
            try {
                return this.packetType.newInstance();
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }
}
