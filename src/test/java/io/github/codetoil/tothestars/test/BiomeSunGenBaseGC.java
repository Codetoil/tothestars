package io.github.codetoil.tothestars.test;

import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeSunGenBaseGC extends BiomeGenBaseGC {
    protected BiomeSunGenBaseGC(BiomeProperties var1) {
        super(var1, true);
    }

    @Override
    public void registerTypes(Biome b) {
        BiomeDictionary.addTypes(b, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DEAD);
        super.registerTypes(b);
    }
}
