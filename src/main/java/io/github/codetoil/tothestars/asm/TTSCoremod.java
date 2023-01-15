package io.github.codetoil.tothestars.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;
import micdoodle8.mods.galacticraft.core.util.GalacticLog;

@IFMLLoadingPlugin.SortingIndex(TTSCoremod.SORTINGINDEX)
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class TTSCoremod implements IFMLLoadingPlugin {
	public static final int SORTINGINDEX = 3;
	public static final GalacticLog logger;

	public TTSCoremod() {
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.tothestars.json");
		logger = new GalacticLog(this);
	}
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}
	
	@Override
	public String getModContainerClass() {
		return "io.github.codetoil.tothestars.asm.TTSModContainer";
	}
	
	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {}
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}