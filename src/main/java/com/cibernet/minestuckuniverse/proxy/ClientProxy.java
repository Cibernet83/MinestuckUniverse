package com.cibernet.minestuckuniverse.proxy;

import com.cibernet.minestuckuniverse.blocks.MinestuckUniverseBlocks;
import com.cibernet.minestuckuniverse.client.MSUFontRenderer;
import com.cibernet.minestuckuniverse.client.MSUKeys;
import com.cibernet.minestuckuniverse.client.MSURenderMachineOutline;
import com.cibernet.minestuckuniverse.client.RenderBeams;
import com.cibernet.minestuckuniverse.client.layers.LayerConsortCosmetics;
import com.cibernet.minestuckuniverse.client.models.ModelAcheron;
import com.cibernet.minestuckuniverse.client.models.consort.ModelIguana;
import com.cibernet.minestuckuniverse.client.models.consort.ModelNakagator;
import com.cibernet.minestuckuniverse.client.models.consort.ModelSalamander;
import com.cibernet.minestuckuniverse.client.models.consort.ModelTurtle;
import com.cibernet.minestuckuniverse.client.render.*;
import com.cibernet.minestuckuniverse.entity.*;
import com.cibernet.minestuckuniverse.events.handlers.CaptchalogueEventHandler;
import com.cibernet.minestuckuniverse.gui.GuiStrifeSwitcher;
import com.cibernet.minestuckuniverse.items.ItemSkaianScroll;
import com.cibernet.minestuckuniverse.items.ItemWarpMedallion;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.items.godtier.ItemGodTierKit;
import com.cibernet.minestuckuniverse.items.weapons.ItemBeamBlade;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import com.cibernet.minestuckuniverse.tileentity.TileEntityHolopad;
import com.cibernet.minestuckuniverse.util.AspectColorHandler;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.mraof.minestuck.client.model.ModelFrog;
import com.mraof.minestuck.client.renderer.BlockColorCruxite;
import com.mraof.minestuck.client.renderer.entity.RenderDecoy;
import com.mraof.minestuck.client.renderer.entity.RenderEntityMinestuck;
import com.mraof.minestuck.client.renderer.entity.frog.RenderFrog;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.consort.EntityTurtle;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
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
        RenderingRegistry.registerEntityRenderingHandler(EntityMSUThrowable.class, RenderThrowable::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMSUArrow.class, RenderArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityUnrealAir.class, RenderUnrealAir::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRock.class, RenderRock::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCruxiteSlime.class, RenderCruxiteSlime::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBubble.class, RenderBubble::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHeartDecoy.class, RenderHeartDecoy::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHopeGolem.class, (manager) -> new RenderHopeGolem(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityLocatorEye.class, (manager) -> new RenderSnowball<>(manager, MinestuckUniverseItems.denizenEye, Minecraft.getMinecraft().getRenderItem()));

        RenderingRegistry.registerEntityRenderingHandler(EntityNakagator.class, manager -> new RenderConsort(manager, new ModelNakagator(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntitySalamander.class, manager -> new RenderConsort(manager, new ModelSalamander(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityIguana.class, manager -> new RenderConsort(manager, new ModelIguana(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityTurtle.class, manager -> new RenderConsort(manager, new ModelTurtle(), 0.5F));

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
                MinestuckUniverseItems.cruxiteGel, MinestuckUniverseItems.cruxtruderGel, MinestuckUniverseItems.captchalogueBook, MinestuckUniverseItems.chastityKey,
                Item.getItemFromBlock(MinestuckUniverseBlocks.ceramicPorkhollow));


        mc.getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityItemStack)
                return BlockColorCruxite.handleColorTint(((TileEntityItemStack)tileEntity).getStack().getMetadata() == 0 ? 0x99D9EA : ColorCollector.getColor(((TileEntityItemStack)tileEntity).getStack().getMetadata()-1), tintIndex);
            return -1;
        }, MinestuckUniverseBlocks.ceramicPorkhollow);

        mc.getItemColors().registerItemColorHandler((stack, tintIndex) ->
        {
            switch(tintIndex)
            {
                case 0: return ItemGodTierKit.getColor(stack, AspectColorHandler.EnumColor.SHIRT);
                case 1: return ItemGodTierKit.getColor(stack, AspectColorHandler.EnumColor.PRIMARY);
                case 2: return ItemGodTierKit.getColor(stack, AspectColorHandler.EnumColor.SECONDARY);
                case 3: return ItemGodTierKit.getColor(stack, AspectColorHandler.EnumColor.SHOES);
                case 4: case 7: return ItemGodTierKit.getColor(stack, AspectColorHandler.EnumColor.SYMBOL);
                case 5: return ItemGodTierKit.getColor(stack, AspectColorHandler.EnumColor.DETAIL_PRIMARY);
                case 6: return ItemGodTierKit.getColor(stack, AspectColorHandler.EnumColor.DETAIL_SECONDARY);
                default: return 0xFFFFFF;
            }
        }, MinestuckUniverseItems.gtArmorKit, MinestuckUniverseItems.gtHood, MinestuckUniverseItems.gtShirt, MinestuckUniverseItems.gtPants, MinestuckUniverseItems.gtShoes);

        mc.getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex == 1 ? ItemSkaianScroll.getColor(stack) : -1, MinestuckUniverseItems.skaianScroll);

        RenderFrog frog = ((RenderFrog)Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(EntityFrog.class));

        ModelRenderer frogHood = new ModelRenderer(frog.getMainModel());
        frogHood.setRotationPoint(0.0F, 4.0F, -3.0F);
        ((ModelFrog) frog.getMainModel()).head.addChild(frogHood);
        frogHood.cubeList.add(new ModelBox(frogHood, 0, 0, -4F, 0, 0, 8, 7, 7, 0.0F, false));

        ModelRenderer frogHelm = new ModelRenderer(frog.getMainModel());
        frogHelm.setRotationPoint(0.0F, 0.0F, -3.0F);
        ((ModelFrog) frog.getMainModel()).head.addChild(frogHelm);
        frogHelm.cubeList.add(new ModelBox(frogHelm, 0, 0, -3.5F, 0, 0, 7, 7, 7, 0.0F, false));

        frog.addLayer(new LayerConsortCosmetics(frog, frogHood, frogHelm, ((ModelFrog) frog.getMainModel()).head));
    }


}
