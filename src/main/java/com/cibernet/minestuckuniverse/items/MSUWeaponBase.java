package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.item.weapon.ItemWeapon;
import javafx.scene.paint.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.annotation.Nullable;
import java.util.List;

public class MSUWeaponBase extends Item implements IClassedTool
{
    private final String registryName;

    protected boolean unbreakable;
    protected double weaponDamage;
    protected int enchantability;
    protected double weaponSpeed;
    protected ToolMaterial material;
    ItemStack repairMaterial = ItemStack.EMPTY;

    protected MSUToolClass tool = null;
    protected float harvestSpeed = 0;
    private int harvestLevel = 0;

    public MSUWeaponBase(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName)
    {
        super();
        registryName = name;
        this.setUnlocalizedName(unlocName);
        this.setCreativeTab(TabMinestuckUniverse.instance);

        this.unbreakable = maxUses <= 0;
        this.maxStackSize = 1;
        this.setMaxDamage(maxUses);
        this.weaponDamage = damageVsEntity;
        this.enchantability = enchantability;
        this.weaponSpeed = weaponSpeed;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        String key = getUnlocalizedName()+".tooltip";
        if(!I18n.translateToLocal(key).equals(key))
            tooltip.add(I18n.translateToLocal(key));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public MSUWeaponBase setMaterial(Item.ToolMaterial material)
    {
        this.material = material;
        return this;
    }

    public MSUWeaponBase setRepairMaterial(ItemStack stack)
    {
        this.repairMaterial = stack;
        return this;
    }

    public String getToolMaterialName()
    {
        return this.material.toString();
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        ItemStack mat = repairMaterial;
        if (!mat.isEmpty() && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }

    public MSUWeaponBase(double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName)
    {
        this(-1, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
        unbreakable = true;
    }

    public double getAttackDamage(ItemStack stack)
    {
        return weaponDamage;
    }

    public double getAttackSpeed(ItemStack stack)
    {
        return weaponSpeed;
    }

    @Override
    public int getItemEnchantability()
    {
        return enchantability;
    }

    @Override
    public boolean isDamageable()
    {
        return !unbreakable;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player) {
        if (!this.unbreakable) {
            stack.damageItem(1, player);
        }

        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        int dmg = 1;
        if(!canHarvestBlock(state))
            dmg = 2;
        if((double)state.getBlockHardness(worldIn, pos) == 0.0D)
            dmg = 0;

        stack.damageItem(dmg, entityLiving);

        return true;
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.getAttackDamage(stack), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", this.getAttackSpeed(stack), 0));
        }

        return multimap;
    }

    public MSUWeaponBase setTool(MSUToolClass cls, int harvestLevel, float harvestSpeed)
    {
        tool = cls;
        this.harvestLevel = harvestLevel;
        this.harvestSpeed = harvestSpeed;
        return this;
    }

    public MSUToolClass getTool() {return tool;}

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        if(getTool() == null)
            return super.getDestroySpeed(stack, state);
        if(getTool().canHarvest(state))
            return harvestSpeed;
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        if(getTool() == null)
            return super.canHarvestBlock(blockIn);
        return tool.canHarvest(blockIn);
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        if(getTool() == null)
            return super.isEnchantable(stack);
        return !getTool().getEnchantments().isEmpty() || isDamageable();
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState)
    {
        if(getTool() == null)
            return super.getHarvestLevel(stack, toolClass, player, blockState);
        return getTool().canHarvest(blockState) ? harvestLevel : -1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        if(enchantment.type.equals(EnumEnchantmentType.BREAKABLE))
            return !unbreakable;

        if(getTool() == null)
            return super.canApplyAtEnchantingTable(stack, enchantment);

        return getTool().enchantments.contains(enchantment);
    }

    @Override
    public MSUToolClass getToolClass() {
        return tool;
    }

    @Override
    public void setRegistryName() {
        setRegistryName(registryName);
    }
}
