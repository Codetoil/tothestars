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

package io.codetoil.tothestars.asm.mixin;

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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(WorldUtil.class)
public abstract class WorldUtilMixin {
    @Inject(method = "getReachableCelestialBodiesForDimensionID(I)Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;", at = @At("RETURN"), remap = false, cancellable = true)
    private static void getReachableCelestialBodiesForDimensionID(int id, CallbackInfoReturnable<CelestialBody> cir) {
        for (LandableStar cBody : StarRegistry.getLandableStars()) {
            if (cBody.isReachable()) {
                if (cBody.getDimensionID() == id) {
                    cir.setReturnValue(cBody);
                }
            }
        }
    }

    @Inject(method = "getReachableCelestialBodiesForName(Ljava/lang/String;)Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;", at = @At("RETURN"), remap = false, cancellable = true)
    private static void getReachableCelestialBodiesForName(String name, CallbackInfoReturnable<CelestialBody> cir) {
        for (CelestialBody cBody : StarRegistry.getLandableStars()) {
            if (cBody.isReachable()) {
                if (cBody.getName().equals(name)) {
                    cir.setReturnValue(cBody);
                }
            }
        }
    }

    @Inject(method = "getArrayOfPossibleDimensions(ILnet/minecraft/entity/player/EntityPlayerMP;)Ljava/util/HashMap;", at = @At("RETURN"), remap = false)
    private static void getArrayOfPossibleDimensions(int tier, EntityPlayerMP playerBase, CallbackInfoReturnable<HashMap<String, Integer>> cir) {
        for (CelestialBody body : StarRegistry.getLandableStars()) {
            if (!body.isReachable()) {
                cir.getReturnValue().put(body.getTranslatedName() + "*", body.getDimensionID());
            }
        }
    }

    @Inject(method="getPossibleDimensionsForSpaceshipTier", at = @At("RETURN"), remap = false)
    private static void getPossibleDimensionsForSpaceshipTier(int tier, EntityPlayerMP playerBase, CallbackInfoReturnable<List<Integer>> cir) {
        List<Integer> temp = cir.getReturnValue();
        if (StarWorldUtil.registeredStars == null) return;

        for (Integer element : StarWorldUtil.registeredStars)
        {
            WorldProvider provider = WorldUtil.getProviderForDimensionServer(element);

            if (provider != null)
            {
                if (provider instanceof IGalacticraftWorldProvider)
                {
                    if (((IGalacticraftWorldProvider) provider).canSpaceshipTierPass(tier))
                    {
                        temp.add(element);
                    }
                } else
                {
                    temp.add(element);
                }
            }
        }
    }

    @Shadow(remap = false)
    private static void insertChecklistEntries(CelestialBody body, List<CelestialBody> bodiesDone, List<List<String>> checklistValues)
    {
    }

    @Inject(method="getAllChecklistKeys()Ljava/util/List;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private static void getAllChecklistKeys(CallbackInfoReturnable<List<List<String>>> cir, List<CelestialBody> bodiesDone)
    {
        for (LandableStar star : StarRegistry.getLandableStars())
        {
            insertChecklistEntries(star, bodiesDone, cir.getReturnValue());
        }
    }
}
