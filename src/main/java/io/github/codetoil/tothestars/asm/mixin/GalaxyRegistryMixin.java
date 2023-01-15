package io.github.codetoil.tothestars.asm.mixin;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.core.util.list.CelestialList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.codetoil.tothestars.api.LandableStar;
import io.github.codetoil.tothestars.api.StarRegistry;

import java.util.Map;

@Mixin(GalaxyRegistry.class)
public class GalaxyRegistryMixin {
    @Shadow(remap = false)
    static CelestialList<SolarSystem> solarSystems;

    @SuppressWarnings("deprecation")
    @Inject(method = "refreshGalaxies()V", at = @At("RETURN"), remap = false)
    private static void refreshGalaxies(CallbackInfo ci) {
        StarRegistry.refreshGalaxies();
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "getCelestialBodyFromDimensionID(I)Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;", at = @At("RETURN"), remap = false)
    private static void getCelestialBodyFromDimensionID(int dimensionID, CallbackInfoReturnable<CelestialBody> cir) {
        if (cir.getReturnValue() != null) {
            return;
        }

        cir.setReturnValue(StarRegistry.getCelestialBodyFromDimensionID(dimensionID));
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "register(Ljava/lang/Object;)V", at = @At("RETURN"), remap = false)
    private static <T> void register(T object, CallbackInfo cir) {
        if (object instanceof LandableStar)
        {
            StarRegistry.registerLandableStar((LandableStar) object);
        }
    }
}
