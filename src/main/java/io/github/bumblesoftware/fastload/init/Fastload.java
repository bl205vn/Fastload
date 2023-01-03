package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.util.ExtendedString;
import io.github.bumblesoftware.fastload.util.log.MessageContent;
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
		MessageContent info = FL_Logger.DEFAULT_INSTANCE.INFO;
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			info.withMessage(ExtendedString.of(
					ExtendedString.of(tryLimit())
							.toVar()
							.addSuffix("" + getChunkTryLimit())
							.toUpperCase(),
					ExtendedString.of(unsafeClose())
							.toVar()
							.addSuffix("" + getCloseUnsafe())
							.toUpperCase(),
					ExtendedString.of(render(true))
							.addSuffix("radius", "_")
							.toVar()
							.addSuffix("" + getPreRenderRadius())
							.toUpperCase(),
					ExtendedString.of(render(true))
							.addSuffix("area", "_")
							.toVar()
							.addSuffix("" + getPreRenderArea())
							.toUpperCase()
			));

		}
		info.withMessage(ExtendedString.of(
				ExtendedString.of(debug())
						.toVar()
						.addSuffix("" + getDebug())
						.toUpperCase(),
				ExtendedString.of(pregen())
						.addSuffix("radius", "_")
						.toVar()
						.addSuffix("" + getPregenRadius(true))
						.toUpperCase(),
				ExtendedString.of(pregen())
						.addSuffix("area", "_")
						.toVar()
						.addSuffix("" + getPreRenderArea())
						.toUpperCase()
		));
	}
}
