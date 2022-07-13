package com.cibernet.minestuckuniverse.network;

import com.cibernet.minestuckuniverse.network.captchalogue.*;
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
        STONE_TABLET_REQUEST(StoneTabletRequestPacket.class),
        UPDATE_HATS(UpdateHatsPacket.class),


        CHAT_MODUS_EJECT(ChatModusEjectPacket.class),
        UPDATE_MODUS(ModusUpdatePacket.class),
        BOOK_UPDATE_PAGE(BookModusPagePacket.class),
        REQUEST_UPDATE_MODUS(RequestModusUpdatePacket.class),
        JUJU_UPDATE(JujuModusPacket.class),
        COM_UPDATE(CommunistUpdatePacket.class),
        REQUEST_COM_UPDATE(RequestCommunistUpdatePacket.class),
        ALCHEM_WILDCARD(AlchemyWildcardPacket.class),
        BOOK_PUBLISH(BookPublishPacket.class),
        WALLET_CAPTCHA(WalletCaptchaloguePacket.class),


        UPDATE_DATA_CLIENT(PacketUpdateGTDataFromClient.class),
        UPDATE_DATA_SERVER(PacketUpdateGTDataFromServer.class),
        INCREASE_XP(PacketAddSkillXp.class),
        ATTEMPT_BADGE_UNLOCK(PacketAttemptBadgeUnlock.class),
        TOGGLE_BADGE(PacketToggleBadge.class),
        ADD_PLAYER_XP(PacketChangePlayerXp.class),
        REQUEST_GRIST_HOARD(PacketRequestGristHoard.class),
        SEND_GRIST_HOARD(PacketSendGristHoard.class),
        UPDATE_ALL_BADGE_EFFECTS(PacketUpdateAllBadgeEffects.class),
        SET_MOUSE_SENSITIVITY(PacketSetMouseSensitivity.class),
        SET_FOV(PacketSetFOV.class),
        SEND_PARTICLE(PacketSendParticle.class),
        UPDATE_BADGE_EFFECT(PacketUpdateBadgeEffect.class),
        KEY_INPUT(PacketKeyInput.class),
        SEND_POWER_PARTICLES(PacketSendPowerParticlesState.class),
        MINDFLAYER_MOVEMENT_INPUT(PacketMindflayerMovementInput.class),
        SET_CURRENT_ITEM(PacketSetCurrentItem.class),
        EDIT_FILL_BLOCKS(PacketPlaceBlockArea.class),
        UPDATE_CONFIG(PacketUpdateConfig.class),
        OPEN_GUI(PacketOpenGui.class),
        PERFORM_DASH(PacketDash.class),
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
