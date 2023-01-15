package io.github.codetoil.tothestars.asm.mixin;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(WorldUtil.class)
public abstract class WorldUtilMixin {

    private static Set<Star> getStars()
    {
        return GalaxyRegistry.getSolarSystems()
                .stream()
                .map(SolarSystem::getMainStar)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Inject(method = "getPossibleDimensionsForSpaceshipTier(ILnet/minecraft/entity/player/EntityPlayerMP;)Ljava/util/List;", at = @At("RETURN"), remap=false)
    private static void getPossibleDimensionsForSpaceshipTier(int tier, EntityPlayerMP playerBase, CallbackInfoReturnable<List<Integer>> cir)
    {
        List<Integer> temp = cir.getReturnValue();
        for(Star star : getStars())
        {
            if (star.isReachable())
            {
                if (star.getDimensionID() != -1)
                {

                    WorldProvider provider = WorldUtil.getProviderForDimensionServer(star.getDimensionID());
                    if (provider != null)
                    {
                        if (provider instanceof IGalacticraftWorldProvider)
                        {
                            if (((IGalacticraftWorldProvider) provider).canSpaceshipTierPass(tier))
                            {
                                temp.add(star.getDimensionID());
                            }
                        }
                        else
                        {
                            temp.add(star.getDimensionID());
                        }
                    }
                }
            }
        }
    }

    /**
     * @author Codetoil & The original writer(s)
     */
    @Overwrite(remap=false)
    public static CelestialBody getReachableCelestialBodiesForDimensionID(int id)
    {
        /* OLD CODE*/
        List<CelestialBody> celestialBodyList = Lists.newArrayList();
        celestialBodyList.addAll(GalaxyRegistry.getMoons());
        celestialBodyList.addAll(GalaxyRegistry.getPlanets());
        celestialBodyList.addAll(GalaxyRegistry.getSatellites());
        /* NEW CODE*/
        celestialBodyList.addAll(getStars());
        /* OLD CODE*/
        for (CelestialBody cBody : celestialBodyList)
        {
            if (cBody.isReachable())
            {
                if (cBody.getDimensionID() == id)
                {
                    return cBody;
                }
            }
        }

        return null;
    }

    /**
     * @author Codetoil & The original writer
     */
    @Overwrite(remap=false)
    public static CelestialBody getReachableCelestialBodiesForName(String name)
    {
        /* OLD CODE*/
        List<CelestialBody> celestialBodyList = Lists.newArrayList();
        celestialBodyList.addAll(GalaxyRegistry.getMoons());
        celestialBodyList.addAll(GalaxyRegistry.getPlanets());
        celestialBodyList.addAll(GalaxyRegistry.getSatellites());
        /* NEW CODE*/
        celestialBodyList.addAll(getStars());

        /* OLD CODE*/
        for (CelestialBody cBody : celestialBodyList)
        {
            if (cBody.isReachable())
            {
                if (cBody.getName().equals(name))
                {
                    return cBody;
                }
            }
        }

        return null;
    }

    @Shadow
    static void insertChecklistEntries(CelestialBody body, List<CelestialBody> bodiesDone, List<List<String>> checklistValues) {
    }

    /**
     * @author Codetoil & The original writer
     */
    @Overwrite(remap=false)
    public static List<List<String>> getAllChecklistKeys()
    {
        /* OLD CODE */
        List<List<String>> checklistValues = Lists.newArrayList();
        List<CelestialBody> bodiesDone = Lists.newArrayList();

        for (Planet planet : GalaxyRegistry.getPlanets())
        {
            insertChecklistEntries(planet, bodiesDone, checklistValues);
        }

        for (Moon moon : GalaxyRegistry.getMoons())
        {
            insertChecklistEntries(moon, bodiesDone, checklistValues);
        }

        for (Satellite satellite : GalaxyRegistry.getSatellites())
        {
            insertChecklistEntries(satellite, bodiesDone, checklistValues);
        }

        /* NEW CODE */

        for (Star star : getStars())
        {
            insertChecklistEntries(star, bodiesDone, checklistValues);
        }

        /* OLD CODE */
        return checklistValues;
    }
}
