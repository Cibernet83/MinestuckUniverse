package com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.heart;

import com.cibernet.minestuckuniverse.skills.abilitech.heroAspect.TechHeroAspect;
import com.cibernet.minestuckuniverse.util.EnumTechType;
import com.mraof.minestuck.util.EnumAspect;

public class TechHeartLink extends TechHeroAspect
{
	public TechHeartLink(String name, long cost) {
		super(name, EnumAspect.HEART, cost, EnumTechType.OFFENSE);
	}
}
