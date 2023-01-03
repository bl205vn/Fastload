package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.config.init.FLConfig;
import io.github.bumblesoftware.fastload.util.log.MessageContent;
import io.github.bumblesoftware.fastload.util.log.SingleLineLogger;
import io.github.bumblesoftware.fastload.util.log.Types;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import static io.github.bumblesoftware.fastload.config.init.DefaultConfig.propertyKeys.*;
import static io.github.bumblesoftware.fastload.config.init.FLMath.*;
import static io.github.bumblesoftware.fastload.util.ExtendedString.of;

public class Fastload implements ModInitializer {
	public static final String NAMESPACE = "Fastload";

	@Override
	public void onInitialize() {
		FLConfig.init();
		MessageContent info = SingleLineLogger.DEFAULT_INSTANCE.ofType(Types.INFO);
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			info	.withMessage(of(tryLimit())
							.toVar()
							.addSuffix("" + getChunkTryLimit())
							.toUpperCase()
					)
					.withMessage(of(unsafeClose())
							.toVar()
							.addSuffix("" + getCloseUnsafe())
							.toUpperCase()
					)
					.withMessage(of(render(true))
							.addSuffix("radius", "_")
							.toVar()
							.addSuffix("" + getPreRenderRadius())
							.toUpperCase()
					)
					.withMessage(of(render(true))
							.addSuffix("area", "_")
							.toVar()
							.addSuffix("" + getPreRenderArea())
							.toUpperCase()
					);

		}
		info	.withMessage(of(debug())
						.toVar()
						.addSuffix("" + getDebug())
						.toUpperCase()
				)
				.withMessage(of(pregen())
						.addSuffix("radius", "_")
						.toVar()
						.addSuffix("" + getPregenRadius(true))
						.toUpperCase()
				)
				.withMessage(of(pregen())
						.addSuffix("area", "_")
						.toVar()
						.addSuffix("" + getPreRenderArea())
						.toUpperCase()
				);
	}
}
