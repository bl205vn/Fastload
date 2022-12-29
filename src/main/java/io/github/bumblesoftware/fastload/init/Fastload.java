package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;


import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.propertyKeys.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

public class Fastload implements ModInitializer {
	public static final String NAMESPACE = "Fastload";
	public static final InternalLogger LOGGER = Log4J2LoggerFactory.getInstance(NAMESPACE);
	private static String loggableString(String key) {
		return key.toUpperCase() + ": ";
	}
	private static String loggableString(String key, String extra) {
		return key.toUpperCase() + "_" + extra.toUpperCase() + ": ";
	}

	/**
	 * Logs config at start
	 */
	@Override
	public void onInitialize() {
		FLConfig.init();
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			LOGGER.info(loggableString(tryLimit()) + getChunkTryLimit());
			LOGGER.info(loggableString(unsafeClose()) + getCloseUnsafe().toString().toUpperCase());
			LOGGER.info(loggableString(render(true), "radius") + "" + getPreRenderRadius());
			LOGGER.info(loggableString(render(true), "area") + getPreRenderArea());
		}
		LOGGER.info(loggableString(debug()) + getDebug().toString().toUpperCase());
		LOGGER.info(loggableString(pregen(true), "radius") + getPregenRadius(true));
		LOGGER.info(loggableString(pregen(true), "area") + getPregenArea());
	}
}
