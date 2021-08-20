package com.cibernet.minestuckuniverse.items;

import com.cibernet.minestuckuniverse.TabMinestuckUniverse;
import com.cibernet.minestuckuniverse.items.properties.WeaponProperty;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MSUWeaponBaseSweep extends ItemSword implements IClassedTool, IPropertyWeapon<MSUWeaponBaseSweep> {
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

    protected final ArrayList<WeaponProperty> properties = new ArrayList<>();

    public MSUWeaponBaseSweep(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName)
    {
        this(maxUses, damageVsEntity, weaponSpeed, enchantability, name, unlocName, Item.ToolMaterial.IRON);
    }
    public MSUWeaponBaseSweep(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName, ToolMaterial material)
    {
        super(material);
        registryName = name;
        this.setUnlocalizedName(unlocName);
        this.setCreativeTab(registryName.split(":")[0].equals(Minestuck.MOD_ID) ? TabMinestuck.instance : TabMinestuckUniverse.instance);

        this.unbreakable = maxUses <= 0;
        this.maxStackSize = 1;
        this.setMaxDamage(maxUses);
        this.weaponDamage = damageVsEntity;
        this.enchantability = enchantability;
        this.weaponSpeed = weaponSpeed;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String key = getUnlocalizedName() + ".tooltip";
        if (!I18n.translateToLocal(key).equals(key))
            tooltip.add(I18n.translateToLocal(key));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public MSUWeaponBaseSweep setMaterial(Item.ToolMaterial material) {
        this.material = material;
        return this;
    }

    public MSUWeaponBaseSweep setRepairMaterial(ItemStack stack) {
        this.repairMaterial = stack;
        return this;
    }

    public String getToolMaterialName() {
        return this.material.toString();
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = repairMaterial;

        if (!mat.isEmpty() && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;

        for (WeaponProperty p : getProperties(toRepair))
            if (p.getIsRepairable(toRepair, repair))
                return true;

        return super.getIsRepairable(toRepair, repair);
    }

    public MSUWeaponBaseSweep(double damageVsEntity, double weaponSpeed, int enchantability, String name, String unlocName) {
        this(-1, damageVsEntity, weaponSpeed, enchantability, name, unlocName);
        unbreakable = true;
    }

    public double getAttackDamage(ItemStack stack)
    {
        double dmg = weaponDamage;

        for (WeaponProperty p : getProperties(stack))
            dmg = p.getAttackDamage(stack, dmg);

        return dmg;
    }

    public double getAttackSpeed(ItemStack stack) {
        double spd = weaponSpeed;

        for (WeaponProperty p : getProperties(stack))
            spd = p.getAttackSpeed(stack, spd);

        return spd;
    }

    @Override
    public int getItemEnchantability() {
        return enchantability;
    }

    @Override
    public boolean isDamageable() {
        return !unbreakable;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        getProperties(entityItem.getItem()).forEach(p -> p.onEntityItemUpdate(entityItem));
        return super.onEntityItemUpdate(entityItem);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        getProperties(stack).forEach(p -> p.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected));
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player) {
        if (!this.unbreakable) {
            stack.damageItem(1, player);
        }

        getProperties(stack).forEach(p -> p.onEntityHit(stack, target, player));

        return true;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        for (WeaponProperty p : getProperties(player.getHeldItem(hand))) {
            EnumActionResult actionResult = p.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            if (actionResult != EnumActionResult.PASS)
                return actionResult;
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        for (WeaponProperty p : getProperties(player.getHeldItem(hand))) {
            EnumActionResult actionResult = p.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
            if (actionResult != EnumActionResult.PASS)
                return actionResult;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        for (WeaponProperty p : getProperties(stack))
            stack = p.onItemUseFinish(stack, worldIn, entityLiving);
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        int result = super.getMaxItemUseDuration(stack);
        for (WeaponProperty p : getProperties(stack))
            result = p.getMaxItemUseDuration(stack, result);
        return result;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        for (WeaponProperty p : getProperties(playerIn.getHeldItem(handIn))) {
            EnumActionResult actionResult = p.onItemRightClick(worldIn, playerIn, handIn);
            if (actionResult != EnumActionResult.PASS)
                return ActionResult.newResult(actionResult, playerIn.getHeldItem(handIn));
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        for (WeaponProperty p : getProperties(oldStack))
            if (!p.shouldCauseBlockBreakReset(oldStack, newStack))
                return false;

        return super.shouldCauseBlockBreakReset(oldStack, newStack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        for (WeaponProperty p : getProperties(oldStack))
            if (!p.shouldCauseReequipAnimation(oldStack, newStack, slotChanged))
                return false;

        return super.shouldCauseBlockBreakReset(oldStack, newStack);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        int dmg = 1;
        if (!canHarvestBlock(state))
            dmg = 2;
        if ((double) state.getBlockHardness(worldIn, pos) == 0.0D)
            dmg = 0;

        stack.damageItem(dmg, entityLiving);

        getProperties(stack).forEach(p -> p.onBlockDestroyed(stack, worldIn, state, pos, entityLiving));

        return true;
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (slot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.getAttackDamage(stack), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", this.getAttackSpeed(stack), 0));
        }

        return multimap;
    }

    public MSUWeaponBaseSweep setTool(MSUToolClass cls, int harvestLevel, float harvestSpeed) {
        tool = cls;
        this.harvestLevel = harvestLevel;
        this.harvestSpeed = harvestSpeed;
        return this;
    }

    public MSUToolClass getTool() {
        return tool;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (getTool() == null)
            return super.getDestroySpeed(stack, state);
        if (getTool().canHarvest(state))
            return harvestSpeed;
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        if (getTool() == null)
            return super.canHarvestBlock(blockIn);
        return tool.canHarvest(blockIn);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        if (getTool() == null)
            return super.isEnchantable(stack);
        return !getTool().getEnchantments().isEmpty() || isDamageable();
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        if (getTool() == null)
            return super.getHarvestLevel(stack, toolClass, player, blockState);
        return getTool().canHarvest(blockState) ? harvestLevel : -1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment.type.equals(EnumEnchantmentType.BREAKABLE))
            return !unbreakable;

        if (getTool() == null)
            return super.canApplyAtEnchantingTable(stack, enchantment);

        return getTool().enchantments.contains(enchantment);
    }

    @Override
    public MSUToolClass getToolClass() {
        return tool;
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack)
    {
        return oldStack.isItemEqualIgnoreDurability(newStack);
    }

    @Override
    public void setRegistryName() {
        setRegistryName(registryName);
    }

    @Override
    public List<WeaponProperty> getProperties() {
        return properties;
    }
}
