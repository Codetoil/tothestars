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

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.codetoil.tothestars.asm.api.LandableStar;
import io.codetoil.tothestars.asm.api.StarRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;

import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.util.stream.CelestialCollector;
import micdoodle8.mods.galacticraft.core.util.list.CelestialList;
import micdoodle8.mods.galacticraft.core.util.list.ImmutableCelestialList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GalaxyRegistry.class)
public abstract class GalaxyRegistryMixin {
    @Inject(method = "refreshGalaxies()V", at = @At("HEAD"), remap = false)
    private static void refreshGalaxies(CallbackInfo ci) {
        StarRegistry.refreshLandableStarsInGalaxies();
    }

    @WrapMethod(method = "getCelestialBodyFromDimensionID(I)Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;",
            remap = false)
    private static CelestialBody getCelestialBodyFromDimensionID(int dimensionID, Operation<CelestialBody> operation) {
        CelestialBody result = operation.call(dimensionID);
        if (result != null) {
            return result;
        }

        return StarRegistry.getLandableStarFromDimensionID(dimensionID);
    }

    @WrapMethod(method = "register(Ljava/lang/Object;)V", remap = false)
    private static <T> void register(Object object, Operation<Void> operation) {
        operation.call(object);

        if (object instanceof LandableStar)
        {
            StarRegistry.registerLandableStar((LandableStar) object);
        }
    }

    @ModifyReturnValue(method = "getAllReachableBodies", at = @At("RETURN"), remap = false)
    private static ImmutableCelestialList<CelestialBody>
    getAllReachableBodies(ImmutableCelestialList<CelestialBody> original)
    {
        List<CelestialBody> list = Lists.newArrayList();
        list.addAll(original);
        list.addAll(StarRegistry.getLandableStars()
                .stream()
                .filter(CelestialBody.filterReachable())
                .collect(CelestialCollector.toList()));
        return ImmutableCelestialList.of(list);
    }
}
