package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.time;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.events.TimetableEffectEvent;
import com.cibernet.minestuckuniverse.items.ItemTimetable;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.skills.badges.Badge;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TechTimeTables extends TechHeroAspect
{
	public TechTimeTables(String name)
	{
		super(name, EnumAspect.TIME, 50000, EnumTechType.UTILITY);
	}

	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == SkillKeyStates.KeyState.NONE)
			return false;


		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}

		if(!(time % 10 == 0))
			return true;

		Entity target = MSUUtils.getMouseOver(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue()).entityHit;

		badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, EnumAspect.TIME, target == null ? 1 : 5);

		TimetableEffectEvent event = new TimetableEffectEvent(player, target);
		MinecraftForge.EVENT_BUS.post(event);

		if (!event.isCanceled())
		{
			if (target instanceof EntityZombie && !((EntityZombie) target).isChild())
				((EntityZombie) target).setChild(true);
			else if (target instanceof EntityAgeable)
			{
				if(target instanceof EntityChicken && ((EntityChicken) target).getGrowingAge() < -600)
				{
					world.spawnEntity(new EntityItem(world, target.posX, target.posY, target.posZ, new ItemStack(Items.EGG)));
					target.setDead();
				}
				else ((EntityAgeable) target).ageUp(-60, false);
			}
			else if (target instanceof EntityItem)
			{
				ItemStack targetStack = ((EntityItem) target).getItem();
				if (targetStack.isItemDamaged())
					targetStack.setItemDamage(Math.max(0, targetStack.getItemDamage() - 3));
				else if(targetStack.getItem() == Items.CHICKEN)
				{
					EntityChicken chicken = new EntityChicken(world);
					chicken.setPosition(target.posX, target.posY, target.posZ);
					world.spawnEntity(chicken);
					targetStack.shrink(1);
				}
				else
				{
					boolean deconstruct = false;
					IRecipe recipe = ItemTimetable.getItemRecipe(targetStack);

					if (recipe != null)
						for (Ingredient ingredient : recipe.getIngredients())
						{
							ItemStack[] res = ingredient.getMatchingStacks();

							if (res.length > 0 && !res[0].getItem().hasContainerItem(res[0]))
								world.spawnEntity(new EntityItem(world, target.posX, target.posY, target.posZ, res[0].copy()));
							deconstruct = true;
						}
					if (deconstruct)
						targetStack.shrink(recipe.getRecipeOutput().getCount());
				}
			} else return true;
		}

		if((event.isCanceled() || target != null))
			player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() - 1);
		return true;
	}

	@Override
	public boolean canAppearOnList(World world, EntityPlayer player)
	{
		return super.canAppearOnList(world, player) && Badge.findItem(player, new ItemStack(MinestuckUniverseItems.timetable), false, true);
	}
}
