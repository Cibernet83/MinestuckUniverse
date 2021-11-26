package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.events.TimetableEffectEvent;
import com.cibernet.minestuckuniverse.items.properties.PropertyXpMend;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class ItemTimetable extends MSUItemBase
{
	public ItemTimetable()
	{
		super("timetable");
		setMaxStackSize(1);
		setMaxDamage(100);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BLOCK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if(!worldIn.isRemote && isSelected && entityIn instanceof EntityPlayer && ((EntityLivingBase) entityIn).getActiveItemStack().equals(stack))
		{
			EntityPlayer player = (EntityPlayer) entityIn;
			if((player.getItemInUseMaxCount()-player.getItemInUseCount()) % 20 != 0)
				return;

			Entity target = getHoveredEntity(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());

			TimetableEffectEvent event = new TimetableEffectEvent(player, target);
			MinecraftForge.EVENT_BUS.post(event);

			int xpUsed = 20;

			if(player.isCreative() || player.experienceTotal >= xpUsed)
			{
				if (!event.isCanceled())
				{
					if (target instanceof EntityZombie && !((EntityZombie) target).isChild())
						((EntityZombie) target).setChild(true);
					else if (target instanceof EntityAgeable)
					{
						if(target instanceof EntityChicken && ((EntityChicken) target).getGrowingAge() < -600)
						{
							worldIn.spawnEntity(new EntityItem(worldIn, target.posX, target.posY, target.posZ, new ItemStack(Items.EGG)));
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
							EntityChicken chicken = new EntityChicken(worldIn);
							chicken.setPosition(target.posX, target.posY, target.posZ);
							worldIn.spawnEntity(chicken);
							targetStack.shrink(1);
						}
						else
						{
							boolean deconstruct = false;
							IRecipe recipe = getItemRecipe(targetStack);

							if (recipe != null)
								for (Ingredient ingredient : recipe.getIngredients())
								{
									ItemStack[] res = ingredient.getMatchingStacks();

									if (res.length > 0 && !res[0].getItem().hasContainerItem(res[0]))
										worldIn.spawnEntity(new EntityItem(worldIn, target.posX, target.posY, target.posZ, res[0].copy()));
									deconstruct = true;
								}
							if (deconstruct)
								targetStack.shrink(recipe.getRecipeOutput().getCount());
						}
					} else return;
				}

				if((event.isCanceled() || target != null) && !player.isCreative())
				{
					decreaseExp(player, xpUsed);
					stack.damageItem(1, player);
				}
			}
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		entityLiving.resetActiveHand();
	}

	/** Decreases player's experience properly by unclecat_*/
	public static void decreaseExp(EntityPlayer player, float amount)
	{
		if (player.experienceTotal - amount <= 0) // If not enough levels or will be negative
		{
			player.experienceLevel = 0;
			player.experience = 0;
			player.experienceTotal = 0;
			return;
		}

		player.experienceTotal -= amount;

		if (player.experience * (float)player.xpBarCap() < amount) // Removing experience within current level to floor it to player.experience == 0.0f
		{
			amount -= player.experience * (float)player.xpBarCap();
			player.experience = 1.0f;
			player.experienceLevel--;
		}

		while (player.xpBarCap() < amount) // Removing whole levels
		{
			amount -= player.xpBarCap();
			player.experienceLevel--;
		}

		player.experience -= amount / (float)player.xpBarCap(); // Removing experience from remaining level
	}

	public static IRecipe getItemRecipe(ItemStack stack)
	{
		/*
		ArrayList<IRecipe> recipes = new ArrayList<>();
		for(IRecipe entry : CraftingManager.REGISTRY)
			recipes.add(entry);

		Collections.sort(recipes, (a, b) ->
		{
			ResourceLocation locA = a.getRegistryName().getResourceDomain().equals("minecraft") ?
					new ResourceLocation("___", a.getRegistryName().getResourcePath()) : a.getRegistryName();
			ResourceLocation locB = b.getRegistryName().getResourceDomain().equals("minecraft") ?
					new ResourceLocation("___", b.getRegistryName().getResourcePath()) : b.getRegistryName();

			return locA.compareTo(locB);
		});
		*/

		for(IRecipe recipe : CraftingManager.REGISTRY)
		{
			try
			{
				ItemStack res = recipe.getCraftingResult(null);
				if(stack.getCount() >= res.getCount() && stack.getItem() == res.getItem() && stack.getMetadata() == res.getMetadata())
					return recipe;
			} catch (NullPointerException e) {}
		}

		return null;
	}

	public static Entity getHoveredEntity(EntityLivingBase player, double entityHitDistance)
	{
		Vec3d eyePos = player.getPositionEyes(1);
		Vec3d look = player.getLook(1.0F);
		Vec3d lookPos = eyePos.addVector(look.x * entityHitDistance, look.y * entityHitDistance, look.z * entityHitDistance);
		Entity pointedEntity = null;

		List<Entity> entities = player.world.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().expand(look.x * entityHitDistance, look.y * entityHitDistance, look.z * entityHitDistance).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, p_apply_1_ -> p_apply_1_ != null));

		for (Entity entity : entities) {
			AxisAlignedBB entityAABB = entity.getEntityBoundingBox().grow(entity instanceof EntityItem ? 0.2 : (double) entity.getCollisionBorderSize());
			RayTraceResult entityResult = entityAABB.calculateIntercept(eyePos, lookPos);

			if (entityAABB.contains(eyePos)) {
				if (entityHitDistance >= 0.0D) {
					pointedEntity = entity;
					entityHitDistance = 0.0D;
				}
			}
			else if (entityResult != null) {
				double eyeToHitDistance = eyePos.distanceTo(entityResult.hitVec);

				if (eyeToHitDistance < entityHitDistance || entityHitDistance == 0.0D) {
					if (entity.getLowestRidingEntity() == player.getLowestRidingEntity() && !entity.canRiderInteract()) {
						if (entityHitDistance == 0.0D)
						{
							pointedEntity = entity;
						}
					} else
						{
						pointedEntity = entity;
						entityHitDistance = eyeToHitDistance;
					}
				}
			}
		}
		return pointedEntity;
	}
}
