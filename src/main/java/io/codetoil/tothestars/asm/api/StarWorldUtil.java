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

package io.codetoil.tothestars.asm.api;

import io.codetoil.tothestars.asm.TTSCoremod;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;

public class StarWorldUtil
{
	public static List<Integer> registeredStars;

	public static boolean registerStar(int id, boolean initialiseDimensionAtServerInit, int defaultID)
	{
		if (StarWorldUtil.registeredStars == null)
		{
			StarWorldUtil.registeredStars = new ArrayList<>();
		}

		if (initialiseDimensionAtServerInit)
		{
			if (!DimensionManager.isDimensionRegistered(id))
			{
				DimensionManager.registerDimension(id, WorldUtil.getDimensionTypeById(id));
				TTSCoremod.logger.info("Registered Dimension: " + id);
				StarWorldUtil.registeredStars.add(id);
			} else if (DimensionManager.getProviderType(id).getId() == id && GalacticraftRegistry.isDimensionTypeIDRegistered(id))
			{
				TTSCoremod.logger.info("Re-registered dimension: " + id);
				StarWorldUtil.registeredStars.add(id);
			} else
			{
				TTSCoremod.logger.error("Dimension already registered: unable to register planet dimension " + id);
				// Add 0 to the list to preserve the correct order of the
				// other planets (e.g. if server/client initialise with
				// different dimension IDs in configs, the order becomes
				// important for figuring out what is going on)
				registeredStars.add(defaultID);
				return false;
			}
			DimensionType dt = WorldUtil.getDimensionTypeById(id);
			WorldProvider wp = dt.createDimension();
			WorldUtil.dimNames.put(id, WorldUtil.getDimensionName(wp));
			return true;
		}

		// Not to be initialised - still add to the registered stars list (for
		// hotloading later?)
		StarWorldUtil.registeredStars.add(id);
		return true;
	}

	public static void unregisterStars()
	{
		if (StarWorldUtil.registeredStars != null)
		{
			for (Integer var1 : StarWorldUtil.registeredStars)
			{
				DimensionManager.unregisterDimension(var1);
				TTSCoremod.logger.info("Unregistered Dimension: " + var1);
			}

			StarWorldUtil.registeredStars = null;
		}
		WorldUtil.dimNames.clear();
	}

	public static void registerStarClient(Integer dimID, int providerIndex)
	{
		int typeID = GalacticraftRegistry.getDimensionTypeID(providerIndex);

		if (typeID == 0)
		{
			TTSCoremod.logger.error("Server dimension " + dimID + " has no match on client due to earlier registration problem.");
		} else if (dimID == 0)
		{
			TTSCoremod.logger.error("Client dimension " + providerIndex + " has no match on server - probably a server dimension ID conflict problem.");
		} else if (!StarWorldUtil.registeredStars.contains(dimID))
		{
			StarWorldUtil.registeredStars.add(dimID);
			DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(typeID));
		} else
		{
			TTSCoremod.logger.error("Dimension already registered to another mod: unable to register planet dimension " + dimID);
		}
	}
}
