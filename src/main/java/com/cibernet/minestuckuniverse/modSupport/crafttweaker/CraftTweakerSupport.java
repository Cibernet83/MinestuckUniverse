package com.cibernet.minestuckuniverse.modSupport.crafttweaker;

import crafttweaker.IAction;


public class CraftTweakerSupport
{
	public static void applyRecipes()
	{
		CTAPIAbstrata.actions.forEach(IAction::apply);
		CTAPIAssembly.actions.forEach(IAction::apply);
		CTAPIGrist.actions.forEach(IAction::apply);
		//MinestuckUniverseBlocks.registerCustomGristBlocks();
		//MinestuckUniverseItems.registerCustomGristBlocks();

	}

}
