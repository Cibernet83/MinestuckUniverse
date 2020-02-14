package com.cibernet.minestuckuniverse.proxy;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.entity.models.ModelArmorOverride;
import com.cibernet.minestuckuniverse.entity.models.ModelPlayerOverride;
import com.cibernet.minestuckuniverse.fillerItems.MSUFillerItems;
import com.cibernet.minestuckuniverse.entity.render.RenderHologram;
import com.cibernet.minestuckuniverse.items.ItemBeamBlade;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.tileentity.TileEntityHolopad;
import com.cibernet.minestuckuniverse.util.MSUModelManager;
import com.cibernet.minestuckuniverse.client.MSURenderMachineOutline;
import com.cibernet.minestuckuniverse.entity.classes.EntityAcheron;
import com.cibernet.minestuckuniverse.entity.models.ModelAcheron;
import com.cibernet.minestuckuniverse.network.MSUChannelHandler;
import static com.cibernet.minestuckuniverse.items.MinestuckUniverseItems.*;
import com.mraof.minestuck.client.renderer.entity.RenderEntityMinestuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ClientProxy extends CommonProxy
{

    protected static void registerRenderers()
    {
        Minecraft mc = Minecraft.getMinecraft();

        mc.getItemColors().registerItemColorHandler(new ItemBeamBlade.BladeColorHandler(), dyedBeamBlade);
    }

    @Override
    public void preInit()
    {
        super.preInit();

        MinestuckUniverseItems.setClientsideVariables();

        RenderingRegistry.registerEntityRenderingHandler(EntityAcheron.class, RenderEntityMinestuck.getFactory(new ModelAcheron(), 0.5F));
        MinecraftForge.EVENT_BUS.register(MSUModelManager.class);
        MinecraftForge.EVENT_BUS.register(MSURenderMachineOutline.class);

        if(MinestuckUniverse.fillerItemsEnabled)
            MSUFillerItems.setClientsideVariables();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHolopad.class, new RenderHologram());
    }

    @Override
    public void init() {
        super.init();

        MinecraftForge.EVENT_BUS.register(MSUChannelHandler.instance);
        registerRenderers();
    }

    @Override
    public void postInit()
    {
        super.postInit();

        overridePlayerModels();
    }

    public void overridePlayerModels()
    {
        Map<String, RenderPlayer> skinMap = ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, Minecraft.getMinecraft().getRenderManager(), "field_178636_l");
        if(skinMap == null) return;

        RenderPlayer defaultRender = skinMap.get("default");
        overridePlayerModels(defaultRender, false);

        RenderPlayer slimRender = skinMap.get("slim");
        overridePlayerModels(slimRender, true);
    }

    public void overridePlayerModels(RenderPlayer renderPlayer, boolean slim)
    {
        ModelBase oldModel = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, renderPlayer, "field_77045_g");
        ObfuscationReflectionHelper.setPrivateValue(RenderLivingBase.class, renderPlayer, new ModelPlayerOverride(oldModel, 0.0F, slim), "field_77045_g");

        List<LayerRenderer<EntityLivingBase>> layers = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, renderPlayer, "field_177097_h");
        if(layers != null)
        {
            LayerRenderer<EntityLivingBase> armorLayer = layers.stream().filter(layer -> layer instanceof LayerBipedArmor).findFirst().orElse(null);
            if(armorLayer != null)
            {
                Field field = ReflectionHelper.<EntityLivingBase>findField(LayerArmorBase.class, ObfuscationReflectionHelper.remapFieldNames(LayerArmorBase.class.getName(), "field_177186_d"));
                field.setAccessible(true);
                try
                {
                    field.set(armorLayer, new ModelArmorOverride());
                } catch(IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
