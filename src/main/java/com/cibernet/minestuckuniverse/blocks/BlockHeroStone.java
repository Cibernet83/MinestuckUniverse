package com.cibernet.minestuckuniverse.blocks;

import com.cibernet.minestuckuniverse.MinestuckUniverse;
import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.capabilities.MSUCapabilities;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.util.MSUUtils;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.List;

public class BlockHeroStone extends MSUBlockBase implements IGodTierBlock
{
	protected EnumAspect aspect;
	protected boolean isChiseled;
	
	public BlockHeroStone(EnumAspect aspect, boolean isChiseled)
	{
		super(Material.ROCK, getAspectMapColor(aspect), ((isChiseled ? "chiseled_" : "") + "hero_stone" + (aspect == null ? "" : ("_" + aspect.toString()))), ("heroStone" + (isChiseled ? "Chiseled" : "")));
		
		this.aspect = aspect;
		this.isChiseled = isChiseled;

		setHarvestLevel("pickaxe", 3);
		setBlockUnbreakable();
		setResistance(2000.0F);
		setCreativeTab(TabMinestuckUniverse.godTier);
		
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		String heroAspect = TextFormatting.OBFUSCATED + "Thing" + TextFormatting.RESET;
		
		if(getAspect() != null)
			heroAspect = getAspect().getDisplayName();
			
		
		tooltip.add(heroAspect);
	}
	
	protected static MapColor getAspectMapColor(EnumAspect aspect)
	{
		if(aspect == null)
			return MapColor.SILVER;
		switch(aspect)
		{
			case DOOM: return MapColor.GREEN_STAINED_HARDENED_CLAY;
			case HOPE: return MapColor.SAND;
			case LIFE: return MapColor.SILVER_STAINED_HARDENED_CLAY;
			case MIND: return MapColor.GRASS;
			case RAGE: return MapColor.PURPLE;
			case TIME: return MapColor.RED;
			case VOID: return MapColor.BLUE_STAINED_HARDENED_CLAY;
			case BLOOD: return MapColor.NETHERRACK;
			case HEART: return MapColor.MAGENTA_STAINED_HARDENED_CLAY;
			case LIGHT: return MapColor.ADOBE;
			case SPACE: return MapColor.BLACK;
			case BREATH: return MapColor.LAPIS;
			default: return MapColor.SILVER;
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);

		if(stack.getItem().equals(MinestuckUniverseItems.moonstoneChisel))
		{
			if(!worldIn.isRemote)
			{
				float luck = playerIn.getLuck();
				int val = 1;
				val += Math.signum(luck)*((worldIn.rand.nextInt(10) < Math.abs(luck) ? 1 : 0));

				EnumAspect aspect = getAspect();
				if(aspect == null)
					aspect = EnumAspect.values()[worldIn.rand.nextInt(EnumAspect.values().length)];

				InventoryHelper.spawnItemStack(worldIn,pos.getX(),pos.getY(),pos.getZ(), new ItemStack(MinestuckUniverseItems.heroStoneShards.get(aspect), val));
				stack.damageItem(1, playerIn);
				return true;
			}
		}
		else if(!(playerIn instanceof FakePlayer))
		{
			Title title = worldIn.isRemote ? MinestuckPlayerData.title : MinestuckPlayerData.getTitle(IdentifierHandler.encode(playerIn));
			EnumAspect aspect = getAspect();

			if(!playerIn.isSneaking() && playerIn.getCapability(MSUCapabilities.GOD_TIER_DATA, null).isGodTier() && (aspect == null || aspect == title.getHeroAspect()) && facing == EnumFacing.UP)
			{
				if(worldIn.isRemote)
					playerIn.openGui(MinestuckUniverse.instance, MSUUtils.GOD_TIER_MEDITATE_UI, worldIn, pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
		}

		return false;
	}

	@Override
	public EnumAspect getAspect()
	{
		return aspect;
	}
	
	public boolean isChiseled() {return isChiseled;}
}
