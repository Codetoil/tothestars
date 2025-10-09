/*
 *  Copyright (c) 2020, 2023-2025 Anthony Michalek (Codetoil)
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

package io.codetoil.tothestars.asm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import io.codetoil.tothestars.asm.api.LandableStar;
import io.codetoil.tothestars.asm.api.StarRegistry;
import io.codetoil.tothestars.asm.api.StarWorldUtil;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;

@Mixin(WorldUtil.class)
public abstract class WorldUtilMixin {
    @WrapMethod(method =
            "getReachableCelestialBodiesForDimensionID(I)Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;",
            remap = false)
    private static CelestialBody getReachableCelestialBodiesForDimensionID(int id, Operation<CelestialBody> operation) {
        CelestialBody result = operation.call(id);

        for (LandableStar star : StarRegistry.getLandableStars()) {
            if (star.isReachable()) {
                if (star.getDimensionID() == id) {
                    return star;
                }
            }
        }
        return result;
    }

    @WrapMethod(method =
            "getReachableCelestialBodiesForName(Ljava/lang/String;)" +
                    "Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;",
            remap = false)
    private static CelestialBody getReachableCelestialBodiesForName(String name, Operation<CelestialBody> operation) {
        CelestialBody result = operation.call(name);

        for (CelestialBody star : StarRegistry.getLandableStars()) {
            if (star.isReachable()) {
                if (star.getName().equals(name)) {
                    return star;
                }
            }
        }
        return result;
    }

    @WrapMethod(method = "getArrayOfPossibleDimensions(ILnet/minecraft/entity/player/EntityPlayerMP;)" +
            "Ljava/util/HashMap;", remap = false)
    private static HashMap<String, Integer> getArrayOfPossibleDimensions(int tier, EntityPlayerMP playerBase,
                                                                         Operation<HashMap<String, Integer>> operation) {
        HashMap<String, Integer> result = operation.call(tier, playerBase);

        for (CelestialBody body : StarRegistry.getLandableStars()) {
            if (!body.isReachable()) {
                result.put(body.getTranslatedName() + "*", body.getDimensionID());
            }
        }
        return result;
    }

    @WrapMethod(method="getPossibleDimensionsForSpaceshipTier", remap = false)
    private static List<Integer> getPossibleDimensionsForSpaceshipTier(int tier, EntityPlayerMP playerBase,
                                                                       Operation<List<Integer>> operation) {
        List<Integer> result = operation.call(tier, playerBase);

        if (StarWorldUtil.registeredStars == null) return result;

        for (Integer element : StarWorldUtil.registeredStars)
        {
            WorldProvider provider = WorldUtil.getProviderForDimensionServer(element);

            if (provider != null)
            {
                if (provider instanceof IGalacticraftWorldProvider)
                {
                    if (((IGalacticraftWorldProvider) provider).canSpaceshipTierPass(tier))
                    {
                        result.add(element);
                    }
                } else
                {
                    result.add(element);
                }
            }
        }
        return result;
    }

    @Shadow(remap = false)
    private static void insertChecklistEntries(CelestialBody body, List<CelestialBody> bodiesDone,
                                               List<List<String>> checklistValues)
    {
    }

    @ModifyReturnValue(method = "getAllChecklistKeys()Ljava/util/List;", at = @At("RETURN"), remap = false)
    private static List<List<String>> getAllChecklistKeys(List<List<String>> result,
                                                          @Local List<CelestialBody> bodiesDone)
    {
        for (LandableStar star : StarRegistry.getLandableStars())
        {
            insertChecklistEntries(star, bodiesDone, result);
        }
        return result;
    }
}
