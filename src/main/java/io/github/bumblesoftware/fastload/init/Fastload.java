package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.util.log.FastloadLogger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.propertyKeys.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

public class Fastload implements ModInitializer {
	public static final String NAMESPACE = "Fastload";

	@Override
	public void onInitialize() {
		FLConfig.init();
		FastloadLogger logger = FastloadLogger.DEFAULT_INSTANCE;
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			logger.log(tryLimit(), key -> key.toVar().addSuffix(Integer.toString(getChunkTryLimit()), "").getString().toUpperCase());
			logger.log(unsafeClose(), key -> key.toVar().addSuffix(Boolean.toString(getCloseUnsafe()), "").getString().toUpperCase());
			logger.log(render(true), key -> key.addSuffix("radius").toVar().addSuffix(Integer.toString(getPreRenderRadius()), "").getString().toUpperCase());
			logger.log(render(true), key -> key.addSuffix("area").toVar().addSuffix(Integer.toString(getPreRenderArea()), "").getString().toUpperCase());

		}
		logger.log(debug(), key -> key.toVar().addSuffix(Boolean.toString(getDebug()), "").getString().toUpperCase());
		logger.log(pregen(true), key -> key.addSuffix("radius").toVar().addSuffix(Integer.toString(getPregenRadius(true)), "").getString().toUpperCase());
		logger.log(pregen(true), key -> key.addSuffix("area").toVar().addSuffix(Integer.toString(getPregenArea()), "").getString().toUpperCase());
	}
}
