package com.cibernet.minestuckuniverse.items.godtier;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.entity.EntityLocatorEye;
import com.cibernet.minestuckuniverse.items.MSUItemBase;
import com.cibernet.minestuckuniverse.world.gen.structure.StructureQuestBed;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLocatorEye extends MSUItemBase
{
	public ItemLocatorEye(String unlocName, String name)
	{
		super(name, unlocName);
		setMaxStackSize(1);
		setCreativeTab(TabMinestuckUniverse.godTier);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, world, tooltip, flagIn);

		/*
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Dimension") && MinestuckDimensionHandler.isLandDimension(stack.getTagCompound().getInteger("Dimension")))
		{
			World worldBound = Minecraft.getMinecraft().getIntegratedServer().getWorld(stack.getTagCompound().getInteger("Dimension"));

			LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(worldBound.provider.getDimension());
			ChunkProviderLands chunkProvider = (ChunkProviderLands) worldBound.provider.createChunkGenerator();


			String aspect1 = I18n.format("land." + aspects.aspectTerrain.getNames()[chunkProvider.nameIndex1], new Object[0]);
			String aspect2 = I18n.format("land." + aspects.aspectTitle.getNames()[chunkProvider.nameIndex2], new Object[0]);
			tooltip.add(I18n.format("item.denizenEye.tooltip", I18n.format("land.format", aspect1, aspect2)));
		}
		*/
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

		if(worldIn.isRemote || !(entityIn instanceof EntityPlayer))
			return;

		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());



		if(!(worldIn.provider.createChunkGenerator() instanceof ChunkProviderLands))
		{
			((EntityPlayer) entityIn).renderBrokenItemStack(stack.copy());
			stack.setCount(0);
		}
		else if(!stack.getTagCompound().hasKey("Dimension"))
			stack.getTagCompound().setInteger("Dimension", worldIn.provider.getDimension());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);

		if (!worldIn.isRemote)
		{
			BlockPos blockpos = StructureQuestBed.getQuestBedPos(worldIn);

			if (blockpos != null && stack.hasTagCompound()/* && stack.getTagCompound().getInteger("Dimension") == worldIn.provider.getDimension()*/)
			{
				float chance = 0.95f;

				SburbConnection c = SburbHandler.getConnectionForDimension(stack.getTagCompound().getInteger("Dimension"));
				if(c != null && !c.getClientIdentifier().equals(c.getServerIdentifier()))
					chance = Math.min(0.95f, Math.max(0.0f, 0.95f- SburbHandler.availableTier(c.getClientIdentifier())*0.05f));

				EntityLocatorEye entityEye = new EntityLocatorEye(worldIn, playerIn.posX, playerIn.posY + (double)(playerIn.height / 2.0F), playerIn.posZ);
				entityEye.moveTowards(blockpos, chance);
				worldIn.spawnEntity(entityEye);

				worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDEREYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
				worldIn.playEvent(null, 1003, new BlockPos(playerIn), 0);

				if (!playerIn.capabilities.isCreativeMode)
					stack.shrink(1);

				playerIn.addStat(StatList.getObjectUseStats(this));
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, stack);

	}
}
