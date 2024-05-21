package io.codetoil.tothestars.test;

import micdoodle8.mods.galacticraft.api.world.ChunkProviderBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;

import java.util.List;
import java.util.Random;

public class ChunkProviderTestSun extends ChunkProviderBase {

    private final World world;
    private final WorldType worldType;
    private final Random rand;
    private static final FlatGeneratorInfo flatGeneratorInfo;
    private final ChunkGeneratorFlat flat;

    static {
        flatGeneratorInfo = new FlatGeneratorInfo();
        flatGeneratorInfo.setBiome(Biome.getIdForBiome(Biomes.VOID));
        List<FlatLayerInfo> info = flatGeneratorInfo.getFlatLayers();
        info.add(new FlatLayerInfo(1, Blocks.BEDROCK));
        flatGeneratorInfo.updateLayers();
    }

    public ChunkProviderTestSun(World worldIn, long seed, boolean mapFeaturesEnabled)
    {
        this.world = worldIn;
        this.worldType = worldIn.getWorldInfo().getTerrainType();
        this.rand = new Random(seed);
        this.flat = new ChunkGeneratorFlat(worldIn, seed, mapFeaturesEnabled, flatGeneratorInfo.toString());
    }

    @Override
    public Chunk generateChunk(int x, int z) {
        return flat.generateChunk(x, z);
    }

    @Override
    public void populate(int x, int z) {
        flat.populate(x, z);
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return flat.getPossibleCreatures(creatureType, pos);
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {
        flat.recreateStructures(chunkIn, x, z);
    }
}
