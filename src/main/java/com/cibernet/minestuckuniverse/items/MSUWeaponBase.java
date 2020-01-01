package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.item.weapon.ItemWeapon;
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
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.annotation.Nullable;
import java.util.List;

public class MSUWeaponBase extends MSUItemBase
{
    protected boolean unbreakable;
    protected double weaponDamage;
    protected int enchantability;
    protected double weaponSpeed;
    
    protected MSUToolClass tool = null;
    protected float harvestSpeed = 0;
    private int harvestLevel = 0;
    
    public MSUWeaponBase(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName)
    {
        super(name, unlocName);
    
        this.unbreakable = maxUses <= 0;
        this.maxStackSize = 1;
        this.setMaxDamage(maxUses);
        this.weaponDamage = damageVsEntity;
        this.enchantability = enchantability;
        this.weaponSpeed = weaponSpeed;
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
    
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player) {
        if (!this.unbreakable) {
            stack.damageItem(1, player);
        }
        
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
    
    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        if(tool == null)
            return super.getDestroySpeed(stack, state);
        if(tool.canHarvest(state))
            return harvestSpeed;
        return super.getDestroySpeed(stack, state);
    }
    
    @Override
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        if(tool == null)
            return super.canHarvestBlock(blockIn);
        return tool.canHarvest(blockIn);
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        if(tool == null)
            return super.isEnchantable(stack);
        return !tool.getEnchantments().isEmpty();
    }
    
    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState)
    {
        if(tool == null)
            return super.getHarvestLevel(stack, toolClass, player, blockState);
        return tool.canHarvest(blockState) ? harvestLevel : -1;
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        if(enchantment.type.equals(EnumEnchantmentType.BREAKABLE))
            return !unbreakable;
        
        if(tool == null)
            return super.canApplyAtEnchantingTable(stack, enchantment);
        
        return tool.enchantments.contains(enchantment);
    }
}
