package io.github.codetoil.tothestars.api;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialType;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;

public class LandableStar extends CelestialBody {
	protected Star star = null;

	public LandableStar(String bodyName) {
		super(CelestialType.STAR, bodyName);
	}

	public Star getStar()
	{
		return this.star;
	}
	
	public LandableStar setStar(Star star)
	{
		this.star = star;
		return this;
	}

	public SolarSystem getParentSolarSystem()
    {
        return this.star.getParentSolarSystem();
    }
}
