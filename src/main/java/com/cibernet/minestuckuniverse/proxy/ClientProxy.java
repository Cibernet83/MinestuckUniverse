package com.cibernet.minestuckuniverse.proxy;

import com.cibernet.minestuckuniverse.client.MSUFontRenderer;
import com.cibernet.minestuckuniverse.client.MSUKeys;
import com.cibernet.minestuckuniverse.client.render.RenderHologram;
import com.cibernet.minestuckuniverse.client.render.RenderUnrealAir;
import com.cibernet.minestuckuniverse.client.render.ThrowableRenderFactory;
import com.cibernet.minestuckuniverse.entity.EntityUnrealAir;
import com.cibernet.minestuckuniverse.items.weapons.ItemBeamBlade;
import com.cibernet.minestuckuniverse.items.ItemWarpMedallion;
import com.cibernet.minestuckuniverse.tileentity.TileEntityHolopad;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.cibernet.minestuckuniverse.client.MSURenderMachineOutline;
import com.cibernet.minestuckuniverse.entity.EntityAcheron;
import com.cibernet.minestuckuniverse.client.models.ModelAcheron;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.mraof.minestuck.client.renderer.BlockColorCruxite;
import com.mraof.minestuck.client.renderer.entity.RenderEntityMinestuck;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import static com.cibernet.minestuckuniverse.items.MinestuckUniverseItems.dyedBeamBlade;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        super.preInit();

        MinestuckUniverseItems.setClientsideVariables();

        RenderingRegistry.registerEntityRenderingHandler(EntityAcheron.class, RenderEntityMinestuck.getFactory(new ModelAcheron(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityThrowable.class, new ThrowableRenderFactory(MinestuckUniverseItems.yarnBall));
        RenderingRegistry.registerEntityRenderingHandler(EntityUnrealAir.class, RenderUnrealAir::new);
        MinecraftForge.EVENT_BUS.register(MSUModelManager.class);
        MinecraftForge.EVENT_BUS.register(MSURenderMachineOutline.class);
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
                new Item[]{MinestuckUniverseItems.returnMedallion});
    }

}
