package com.cibernet.minestuckuniverse.skills.abilitech;

import com.cibernet.minestuckuniverse.capabilities.badgeEffects.IBadgeEffects;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates;
import com.cibernet.minestuckuniverse.capabilities.keyStates.SkillKeyStates.KeyState;
import com.cibernet.minestuckuniverse.items.MinestuckUniverseItems;
import com.cibernet.minestuckuniverse.particles.MSUParticles;
import com.cibernet.minestuckuniverse.skills.TechBoondollarCost;
import com.cibernet.minestuckuniverse.skills.badges.Badge;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Teleport;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TechReturn extends TechBoondollarCost
{

	public TechReturn(String name) 
	{
		super(name, 100, EnumTechType.UTILITY);
		requiredStacks.add(new ItemStack(MinestuckUniverseItems.returnMedallion));
	}
	
	@Override
	public boolean onUseTick(World world, EntityPlayer player, IBadgeEffects badgeEffects, int techSlot, SkillKeyStates.KeyState state, int time)
	{
		if(state == KeyState.NONE)
			return false;
		
		if(time > 10)
			return false;
		
		if(!player.isCreative() && player.getFoodStats().getFoodLevel() < 1)
		{
			player.sendStatusMessage(new TextComponentTranslation("status.tooExhausted"), true);
			return false;
		}
		
		IdentifierHandler.PlayerIdentifier identifier = IdentifierHandler.encode(player);
		if(identifier == null)
		    return false;
		
		int color = ColorCollector.getColor(MinestuckPlayerData.getData(player).color);
        
        badgeEffects.startPowerParticles(getClass(), MSUParticles.ParticleType.AURA, 2, color);
        
		if(time < 10)
			return true;

		SburbConnection c = SkaianetHandler.getMainConnection(identifier, true);
		if(c == null)
		    return false;

		WorldServer worldd = player.getServer().getWorld(c.getClientDimension());
		BlockPos pos = worldd.provider.getRandomizedSpawnPoint();
		Teleport.teleportEntity(player, c.getClientDimension(), null, pos);
		return true;
	}
}