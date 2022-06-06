package com.cibernet.minestuckuniverse.proxy;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.client.MSUFontRenderer;
import com.cibernet.minestuckuniverse.client.MSUKeys;
import com.cibernet.minestuckuniverse.client.RenderBeams;
import com.cibernet.minestuckuniverse.client.render.*;
import com.cibernet.minestuckuniverse.entity.*;
import com.cibernet.minestuckuniverse.events.handlers.CaptchalogueEventHandler;
import com.cibernet.minestuckuniverse.gui.GuiStrifeSwitcher;
import com.cibernet.minestuckuniverse.items.weapons.ItemBeamBlade;
import com.cibernet.minestuckuniverse.items.ItemWarpMedallion;
import com.cibernet.minestuckuniverse.tileentity.TileEntityHolopad;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.cibernet.minestuckuniverse.client.MSURenderMachineOutline;
import com.cibernet.minestuckuniverse.client.models.ModelAcheron;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.renderer.BlockColorCruxite;
import com.mraof.minestuck.client.renderer.entity.RenderEntityMinestuck;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import javax.annotation.Nullable;

import static com.cibernet.minestuckuniverse.items.MinestuckUniverseItems.dyedBeamBlade;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        super.preInit();

        MinestuckUniverseItems.setClientsideVariables();

        RenderingRegistry.registerEntityRenderingHandler(EntityAcheron.class, RenderEntityMinestuck.getFactory(new ModelAcheron(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityMSUThrowable.class, RenderThrowable::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMSUArrow.class, RenderArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityUnrealAir.class, RenderUnrealAir::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRock.class, RenderRock::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCruxiteSlime.class, RenderCruxiteSlime::new);

        MinecraftForge.EVENT_BUS.register(MSUModelManager.class);
        MinecraftForge.EVENT_BUS.register(MSURenderMachineOutline.class);
        MinecraftForge.EVENT_BUS.register(RenderBeams.class);
        MinecraftForge.EVENT_BUS.register(GuiStrifeSwitcher.class);
        MinecraftForge.EVENT_BUS.register(CaptchalogueEventHandler.Client.class);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHolopad.class, new RenderHologram());
    }

    @Override
    public void init() {
        super.init();

        MinecraftForge.EVENT_BUS.register(MSUChannelHandler.instance);
        registerRenderers();
        MSUKeys.register();
        MSUFontRenderer.registerFonts();
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    protected static void registerRenderers()
    {
        Minecraft mc = Minecraft.getMinecraft();

        mc.getItemColors().registerItemColorHandler(new ItemBeamBlade.BladeColorHandler(), dyedBeamBlade);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) ->
                        BlockColorCruxite.handleColorTint(ItemWarpMedallion.getColor(stack), tintIndex),
                MinestuckUniverseItems.returnMedallion);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) ->
                        BlockColorCruxite.handleColorTint(stack.getMetadata() == 0 ? 0x99D9EA : ColorCollector.getColor(stack.getMetadata() - 1), tintIndex),
                MinestuckUniverseItems.cruxiteGel, MinestuckUniverseItems.cruxtruderGel, MinestuckUniverseItems.captchalogueBook, MinestuckUniverseItems.chasityKey,
                Item.getItemFromBlock(MinestuckUniverseBlocks.ceramicPorkhollow));


        mc.getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityItemStack)
                return BlockColorCruxite.handleColorTint(((TileEntityItemStack)tileEntity).getStack().getMetadata() == 0 ? 0x99D9EA : ColorCollector.getColor(((TileEntityItemStack)tileEntity).getStack().getMetadata()-1), tintIndex);
            return -1;
        }, MinestuckUniverseBlocks.ceramicPorkhollow);
    }


}
