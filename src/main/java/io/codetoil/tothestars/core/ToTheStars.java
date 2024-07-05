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

package io.codetoil.tothestars.core;

import io.codetoil.tothestars.asm.api.StarRegistry;
import io.codetoil.tothestars.asm.api.StarWorldUtil;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
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
