/*
 *  Copyright (c) 2020, 2023, 2024 Anthony Michalek (Codetoil)
 *	This file is part of ToTheStars.
 *
 * 	ToTheStars is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 *  Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 *  option) any later version.
 *
 * 	ToTheStars is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Lesser General Public License for more details.
 *
 * 	You should have received a copy of the GNU Lesser General Public License along with Foobar. If not, see
 *  <https://www.gnu.org/licenses/>.
 */

package io.codetoil.tothestars.asm.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.HashMap;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.core.util.list.ImmutableCelestialList;
import micdoodle8.mods.galacticraft.core.util.list.CelestialList;

public class StarRegistry
{
    private static final CelestialList<LandableStar> landableStars = CelestialList.create();

    private static final Map<SolarSystem, CelestialList<LandableStar>> solarSystemLandableStarList = new HashMap<>();

    public static void refreshLandableStarsInGalaxies()
    {
        solarSystemLandableStarList.clear();

        for (LandableStar landableStar : getLandableStars())
        {
            SolarSystem solarSystem = landableStar.getParentSolarSystem();
            CelestialList<LandableStar> list = solarSystemLandableStarList.get(solarSystem);
            if (list == null)
            {
                list = CelestialList.create();
            }
            list.add(landableStar);
            solarSystemLandableStarList.put(solarSystem, list);
        }
    }

    public static CelestialBody getLandableStarFromTranslationkey(String translationKey)
    {
        for (LandableStar landableStar : landableStars)
        {
            if (landableStar.getTranslationKey().equals(translationKey))

            {
                return landableStar;
            }
        }
        return null;
    }

    public static void registerLandableStar(LandableStar star)
    {
        landableStars.add(star);
    }

    public static CelestialBody getLandableStarFromDimensionID(int dimensionID)
    {
        for (LandableStar landableStar : landableStars) {
            if (landableStar.getDimensionID() == dimensionID)
            {
                return landableStar;
            }
        }

        return null;
    }

    public static List<LandableStar> getLandableStarsForSolarSystem(SolarSystem solarSystem)
    {
        if (solarSystemLandableStarList.get(solarSystem) == null)
        {
            return new ArrayList<>();
        }
        return solarSystemLandableStarList.get(solarSystem);
    }

    /**
     * Returns a read-only list containing all registered Landable Stars
     */
    public static ImmutableCelestialList<LandableStar> getLandableStars()
    {
        return ImmutableCelestialList.of(landableStars);
    }
}