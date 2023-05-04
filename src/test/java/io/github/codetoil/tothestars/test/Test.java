package io.github.codetoil.tothestars.test;

import io.github.codetoil.tothestars.asm.api.LandableStar;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

// Took some boilerplate from HaveASolTime
@Mod(modid = "test", dependencies = "required-after:galacticraftcore;required-after:tothestars")
public class Test
{
	public static Logger logger;

	@Mod.Instance("test")
	public static Test instance;

	public static LandableStar starSol;

	public static int dimSolId = -5430;
	public static DimensionType dimSol;
	public static Biome biomeSolFlat;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();

		Test.starSol = new LandableStar("sol").setParentSolarSystem(GalacticraftCore.solarSystemSol);

		biomeSolFlat = new BiomeSunGenBaseGC(new Biome.BiomeProperties("sol")
				.setBaseHeight(1.5F)
				.setHeightVariation(0.4F)
				.setRainfall(0.0F)
				.setRainDisabled()
				.setWaterColor(0xFFFF00)
				.setTemperature(1.0F));

		Test.starSol.setBiomeInfo(biomeSolFlat);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Test.starSol.setDimensionInfo(dimSolId, WorldProviderSol.class).setTierRequired(3);

		GalaxyRegistry.register(Test.starSol);
		GalacticraftRegistry.registerTeleportType(WorldProviderSol.class, new TeleportTypeSol());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		Test.dimSol = WorldUtil.getDimensionTypeById(dimSolId);
		GalacticraftCore.solarSystemSol.setMainStar(Test.starSol);
	}

}
