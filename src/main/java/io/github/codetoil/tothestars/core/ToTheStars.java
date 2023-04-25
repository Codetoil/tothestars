package io.github.codetoil.tothestars.core;

import io.github.codetoil.tothestars.asm.api.StarRegistry;
import io.github.codetoil.tothestars.asm.api.StarWorldUtil;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@Mod(modid = "tothestars", dependencies = "required-after:galacticraftcore")
public class ToTheStars
{
	public static Logger logger;

	@Mod.Instance("tothestars")
	public static ToTheStars instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		for (CelestialBody body : StarRegistry.getLandableStars())
		{
			if (body.shouldAutoRegister())
			{
				int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, body.getDimensionID());
				DimensionType type = GalacticraftRegistry.registerDimension(body.getTranslationKey(), body.getDimensionSuffix(), body.getDimensionID(), body.getWorldProvider(), body.getForceStaticLoad() || id < 0);
				if (type != null)
				{
					body.initialiseMobSpawns();
				}
				else
				{
					body.setUnreachable();
					ToTheStars.logger.error("Tried to register dimension for body: " + body.getTranslationKey() + " hit conflict with ID " + body.getDimensionID());
				}
			}

			if (body.getSurfaceBlocks() != null)
			{
				TransformerHooks.spawnListAE2_GC.addAll(body.getSurfaceBlocks());
			}
		}
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		for (CelestialBody body : StarRegistry.getLandableStars())
		{
			if (body.shouldAutoRegister())
			{
				if (!StarWorldUtil.registerStar(body.getDimensionID(), body.isReachable(), 0))
				{
					body.setUnreachable();
				}
			}
		}
	}
}
