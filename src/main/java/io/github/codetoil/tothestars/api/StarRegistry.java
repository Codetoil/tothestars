package io.github.codetoil.tothestars.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.codetoil.tothestars.asm.TTSCoremod;

import java.util.HashMap;

import micdoodle8.mods.galacticraft.annotations.ReplaceWith;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.core.util.list.ImmutableCelestialList;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.util.list.CelestialList;

public class StarRegistry
{
    static CelestialList<LandableStar> landableStars = CelestialList.create();

    static Map<SolarSystem, CelestialList<LandableStar>> solarSystemLandableStarList = new HashMap<>();

    /**
     * @ReplaceWith {@link GalaxyRegistry#refreshGalaxies()}
     */
    @Deprecated
    @ReplaceWith("GalaxyRegistry.refreshGalaxies()")
    public static void refreshGalaxies()
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

    /**
     * @ReplaceWith {@link GalaxyRegistry#register(T object)}
     */
    @Deprecated
    @ReplaceWith("GalaxyRegistry.register(T object)")
    public static void registerLandableStar(LandableStar star)
    {
        landableStars.add(star);
    }

    /**
     * @ReplaceWith {@link GalaxyRegistry#getCelestialBodyFromDimensionID(int dimensionID)}
     */
    @Deprecated
    @ReplaceWith("GalaxyRegistry.getCelestialBodyFromDimensionID(int dimensionID)")
    public static CelestialBody getCelestialBodyFromDimensionID(int dimensionID)
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
        return landableStars.toImmutableList();
    }
}