package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.client.FLClientHandler;
import net.fabricmc.api.ClientModInitializer;

public class FastloadClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FLClientEvents.init();
        FLClientHandler.init();
    }
}
