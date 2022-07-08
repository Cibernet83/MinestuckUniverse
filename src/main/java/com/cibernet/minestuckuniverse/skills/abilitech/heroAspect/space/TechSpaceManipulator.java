package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.space;

import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.godTier.IGodTierData;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.items.ItemManipulatedMatter;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.skills.MSUSkills;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TechSpaceManipulator extends TechHeroAspect
{
	public TechSpaceManipulator(String name) {
		super(name, EnumAspect.SPACE, EnumTechType.UTILITY);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if (state == SkillKeyStates.KeyState.NONE)
			return false;

		BlockPos pos1 = badgeEffects.getManipulatedPos1(), pos2 = badgeEffects.getManipulatedPos2();
		if (state == SkillKeyStates.KeyState.RELEASED)
		{
			if (time < 20 || pos1 == null || pos2 == null)
			{
				BlockPos pos = MSUUtils.getTargetBlock(player);

				if(pos == null || player.isSneaking())
				{
					badgeEffects.setManipulatedPos1(null, player.dimension);
					badgeEffects.setManipulatedPos2(null, player.dimension);
					player.sendStatusMessage(new TextComponentTranslation("item.manipulatedMatter.posReset"), true);
				}
				else
				{
					boolean manipulatingPos2 = badgeEffects.isManipulatingPos2() ;//&& badgeEffects.getManipulatedPos1Dim() == badgeEffects.getManipulatedPos2Dim();

					if (manipulatingPos2)
						badgeEffects.setManipulatedPos2(pos, player.dimension);
					else
						badgeEffects.setManipulatedPos1(pos, player.dimension);
					player.sendStatusMessage(new TextComponentTranslation("item.manipulatedMatter.posSet" + (badgeEffects.isManipulatingPos2() ? "A" : "B"), pos.getX() + ", " + pos.getY() + ", " + pos.getZ()), true);
				}
			}
			else if (player.capabilities.allowEdit)
			{
				if (badgeEffects.getManipulatedPos1Dim() != badgeEffects.getManipulatedPos2Dim() ||
						Math.abs(pos1.getX() - pos2.getX()) >= 8 ||
						Math.abs(pos1.getY() - pos2.getY()) >= 8 ||
						Math.abs(pos1.getZ() - pos2.getZ()) >= 8)
					player.sendStatusMessage(new TextComponentTranslation("item.manipulatedMatter.tooBig"), true);
				else
				{
					int energyRequired = (int) (((Math.abs(pos1.getX() - pos2.getX()))*(Math.abs(pos1.getY() - pos2.getY()))*(Math.abs(pos1.getZ() - pos2.getZ())))/512f * 18);

					if(!player.isCreative())
					{
						if(player.getFoodStats().getFoodLevel() < energyRequired)
						{
							player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
							return false;
						}
						player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()-energyRequired);
					}

					ItemStack manipulatedMatter = new ItemStack(MinestuckUniverseItems.manipulatedMatter);
					ItemManipulatedMatter.storeStructure(manipulatedMatter, player, world, badgeEffects.getManipulatedPos1(), badgeEffects.getManipulatedPos2());
					if (!player.addItemStackToInventory(manipulatedMatter))
						player.dropItem(manipulatedMatter, true, false);
				}
			}
			else
				player.sendStatusMessage(new TextComponentTranslation("item.manipulatedMatter.cantEdit"), true);

			if (time >= 20)
				badgeEffects.startPowerParticles(TechSpaceManipulator.class, MSUParticles.ParticleType.BURST, EnumAspect.SPACE, 10);
			else badgeEffects.startPowerParticles(TechSpaceManipulator.class, MSUParticles.ParticleType.AURA, EnumAspect.SPACE, 5);
		}
		else badgeEffects.startPowerParticles(TechSpaceManipulator.class, MSUParticles.ParticleType.AURA, EnumAspect.SPACE, (time >= 20 && pos1 != null && pos2 != null) ? 6 : 1);

		return true;
	}

	public static class PosA {}
	public static class PosB {}
}
