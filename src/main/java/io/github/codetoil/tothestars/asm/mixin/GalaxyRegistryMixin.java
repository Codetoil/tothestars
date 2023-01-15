package io.github.codetoil.tothestars.asm.mixin;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(GalaxyRegistry.class)
public class GalaxyRegistryMixin {
    @Shadow(remap = false)
    static Map<String, SolarSystem> solarSystems;

    @Inject(method = "getCelestialBodyFromDimensionID(I)Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;", at = @At("RETURN"), remap = false)
    private static void getCelestialBodyFromDimensionID(int dimensionID, CallbackInfoReturnable<CelestialBody> cir){
        if (cir.getReturnValue() != null)
        {
            return;
        }
        for (SolarSystem solarSystem : solarSystems.values())
        {
            if (solarSystem.getMainStar() != null && solarSystem.getMainStar().getDimensionID() == dimensionID)
            {
                cir.setReturnValue(solarSystem.getMainStar());
            }
        }
    }
    
    @Inject(method = "getCelestialBodyFromUnlocalizedName(Ljava/lang/String;)Lmicdoodle8/mods/galacticraft/api/galaxies/CelestialBody;", at = @At("RETURN"), remap = false)
    private static void getCelestialBodyFromUnlocalizedName(String unlocalizedName, CallbackInfoReturnable<CelestialBody> cir)
    {
        if (cir.getReturnValue() != null)
        {
            return;
        }
        for (SolarSystem solarSystem : solarSystems.values())
        {
            if (solarSystem.getMainStar() != null && solarSystem.getMainStar().getUnlocalizedName().equals(unlocalizedName))
            {
                cir.setReturnValue(solarSystem.getMainStar());
            }
        }
    }
}
