package io.github.bumblesoftware.fastload.mixin.mixins.client;

import io.github.bumblesoftware.fastload.api.events.SetScreenEvent;
import io.github.bumblesoftware.fastload.config.init.FLMath;
import io.github.bumblesoftware.fastload.config.screen.BuildingTerrainScreen;
import io.github.bumblesoftware.fastload.mixin.intercomm.client.MinecraftClientMixinInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.bumblesoftware.fastload.config.init.FLMath.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements MinecraftClientMixinInterface {
    //Original code is from 'kennytv, forceloadingscreen' under the 'MIT' License.
    //Code is heavily modified to suit Fastload's needs

    @Shadow public void setScreen(@Nullable Screen screen) {}
    @Shadow private boolean windowFocused;
    @Shadow private volatile boolean running;
    @Shadow @Nullable public ClientWorld world;
    @Shadow @Final public WorldRenderer worldRenderer;
    @Shadow @Final public GameOptions options;

    @Shadow @Final public GameRenderer gameRenderer;
    @Shadow @Nullable public ClientPlayerEntity player;
    @Shadow @Final private static Logger LOGGER;
    //Checkers
    private boolean justLoaded = false;
    private boolean shouldLoad = false;
    private boolean playerJoined = false;
    private boolean showRDDOnce = false;
    //Boolean  Pre-render
    private boolean isBuilding = false;
    private boolean closeBuild = false;
    //Pre Renderer Log Constants
    @SuppressWarnings("FieldCanBeLocal")
    private final int chunkTryLimit = getChunkTryLimit();
    //Storage
    private Float oldPitch = null;
    private Integer oldChunkLoadedCountStorage = null;
    private Integer oldChunkBuildCountStorage = null;
    //Warning Constants
    private int preparationWarnings = 0;
    private int buildingWarnings = 0;
    //Ticks until Pause Menu is Active again
    private final int timeDownGoal = 10;
    // Set this to 0 to start timer for Pause Menu Cancellation
    private int timeDown = timeDownGoal;

    /**
     * External interface setter for @field shouldLoad
     */
    @Override
    public void setShouldLoad(boolean toValue) {
        shouldLoad = toValue;
    }
    /**
     * External interface setter for @field playerJoined
     */
    @Override
    public void setPlayerJoined(boolean toValue) {
        playerJoined = toValue;
    }
    private Camera getCamera() {
        return gameRenderer.getCamera();
    }
    //Basic Logger
    private static void log(String toLog) {
        LOGGER.info(toLog);
    }
    //Logs Difference in Render and Pre-render distances
    private static void logRenderDistanceDifference() {
        if (!getPreRenderRadius().equals(getPreRenderRadius(true)))
            log("Pre-rendering radius changed to "
                    + getPreRenderRadius() + " from " + getPreRenderRadius(true)
                    + " to protect from chunks not loading past your given render distance. " +
                    "To resolve this, please adjust your render distance accordingly");
    }
    //Logs Goal Versus amount Pre-renderer could load
    private void logPreRendering(int chunkLoadedCount) {
        log("Goal (Loaded Chunks): " + getPreRenderArea());
        log("Loaded Chunks: " + chunkLoadedCount);
    }
    private void logBuilding(int chunkBuildCount, int chunkBuildCountGoal) {
        log("Goal (Built Chunks): " + chunkBuildCountGoal);
        log("Chunk Build Count: " + chunkBuildCount);
    }
    private void stopBuilding(int chunkLoadedCount, int chunkBuildCount, int chunkBuildCountGoal) {
        if (playerJoined) {
            closeBuild = true;
            if (getDebug()) {
                logBuilding(chunkBuildCount, chunkBuildCountGoal);
                logPreRendering(chunkLoadedCount);
            }
            isBuilding = false;
            if (!windowFocused) {
                timeDown = 0;
                if (getDebug()) log("Temporarily Cancelling Pause Menu to enable Renderer");
            }
            assert this.player != null;
            if (oldPitch != null) {
                getCamera().setRotation(this.player.getYaw(), oldPitch);
                if (this.player.getPitch() != oldPitch) this.player.setPitch(oldPitch);
                oldPitch = null;
            }
            playerJoined = false;
            oldChunkLoadedCountStorage = 0;
            oldChunkBuildCountStorage = 0;
            setScreen(null);
        }
    }
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreen(final Screen screen, final CallbackInfo ci) {
        SetScreenEvent.onSetScreen(screen, ci);
        //Failsafe
        if (screen == null) {
            isBuilding = false;
            shouldLoad = false;
            justLoaded = false;
            showRDDOnce = false;
            oldPitch = null;
        }
        //Stop Pause Menu interfering with rendering
        if (timeDown < timeDownGoal && screen instanceof GameMenuScreen && !windowFocused) {
            ci.cancel();
            setScreen(null);
        }
        //Log Pre-Render Initiation
        if (screen instanceof BuildingTerrainScreen) {
            if (getDebug()) {
                log("Successfully Initiated Building Terrain");
            }
        }
        //Close Progress Screen
        if (screen instanceof ProgressScreen && getCloseUnsafe()) {
            ci.cancel();
            if (getDebug()) log("Progress Screen Successfully Cancelled");
        }
        //Close Downloading Terrain Screen ASAP
        if (screen instanceof DownloadingTerrainScreen && shouldLoad && playerJoined && running) {
            if (getDebug()) log("Downloading Terrain Accessed!");
            shouldLoad = false;
            justLoaded = true;
            showRDDOnce = true;
            // Switch to Pre-render Phase
            if (getCloseSafe()) {
                ci.cancel();
                if (getDebug()) log("Preparing to replace Download Terrain with Building Terrain");
                if (getDebug()) log("Goal (Loaded Chunks): " + getPreRenderArea());
                justLoaded = true;
                isBuilding = true;
                setScreen(new BuildingTerrainScreen());
            //Skip Downloading Terrain Screen
            } else if (getCloseUnsafe()) {
                playerJoined = false;
                ci.cancel();
                if (getDebug()) log("Successfully Skipped Downloading Terrain Screen!");
                timeDown = 0;
                setScreen(null);
            }
        }
    }
    @Inject(method = "openPauseMenu", at = @At("HEAD"), cancellable = true)
    private void cancelOpenPauseMenu(boolean pause, CallbackInfo ci) {
        //Stop Pause for Downloading Terrain Skip (Failsafe)
        if (justLoaded) {
            if (windowFocused) justLoaded = false;
            else if (running) {
                justLoaded = false;
                ci.cancel();
                if (getDebug()) log("Pause Menu Cancelled");
            }
        }
    }
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(boolean tick, CallbackInfo ci) {
        //Log differences differences
        if (showRDDOnce) {
            logRenderDistanceDifference();
            showRDDOnce = false;
        }
        //Pre-rendering Engine
        if (isBuilding) {
            if (this.world != null) {
                //Sets player to face horizontally to prioritise chunk loading
                {
                    assert player != null;
                    if (oldPitch == null) {
                        oldPitch = this.player.getPitch();
                    }
                    this.player.setPitch(0);
                    if (getDebug()) {
                        log("Pitch:" + oldPitch);
                    }
                }

                int chunkLoadedCount = this.world.getChunkManager().getLoadedChunkCount();
                int chunkBuildCount = this.worldRenderer.getCompletedChunkCount();
                double FOV = this.options.getFov().getValue();
                double chunkBuildCountGoal = (FOV/360) * getPreRenderArea().doubleValue();
                final int oldPreparationWarningCache = preparationWarnings;
                final int oldBuildingWarningCache = buildingWarnings;

                if (getDebug()) {
                    logPreRendering(chunkLoadedCount);
                    logBuilding(chunkBuildCount, (int) chunkBuildCountGoal);
                }
                //The warning system
                if (oldChunkLoadedCountStorage != null && oldChunkBuildCountStorage != null) {
                    if (oldChunkLoadedCountStorage == chunkLoadedCount)
                        preparationWarnings++;
                    if (oldChunkBuildCountStorage == chunkBuildCount)
                        buildingWarnings++;

                    if ((buildingWarnings >= chunkTryLimit || preparationWarnings >= chunkTryLimit) && !FLMath.getForceBuild()) {
                        buildingWarnings = 0;
                        preparationWarnings = 0;
                        log("Pre-loading is taking too long! Stopping...");
                        stopBuilding(chunkLoadedCount, chunkBuildCount, (int) chunkBuildCountGoal);
                    }

                    if (!closeBuild) {
                        //Log Warnings
                        final int spamLimit = 2;
                        if (preparationWarnings > 0) {
                            if (oldPreparationWarningCache == preparationWarnings && preparationWarnings > spamLimit) {
                                log("FL_WARN# Same prepared chunk count returned " + preparationWarnings + " time(s) in a row!");
                                if (!getForceBuild()) {
                                    log("Had it be " + chunkTryLimit + " time(s) in a row, pre-loading would've stopped");
                                }
                                if (getDebug()) logPreRendering(chunkLoadedCount);
                            }
                            if (chunkLoadedCount > oldChunkLoadedCountStorage) {
                                preparationWarnings = 0;
                            }
                        }
                        if (buildingWarnings > 0) {
                            if (oldBuildingWarningCache == buildingWarnings && buildingWarnings > spamLimit) {
                                log("FL_WARN# Same built chunk count returned " + buildingWarnings + " time(s) in a row");
                                if (!getForceBuild()) {
                                    log("Had it be " + chunkTryLimit + " time(s) in a row, pre-loading would've stopped");
                                }
                                if (getDebug()) logPreRendering(chunkLoadedCount);
                            }
                            if (chunkBuildCount > oldChunkBuildCountStorage) {
                                buildingWarnings = 0;
                            }
                        }
                    }
                }

                //Stops when completed

                oldChunkLoadedCountStorage = chunkLoadedCount;
                oldChunkBuildCountStorage = chunkBuildCount;

                if (chunkLoadedCount >= getPreRenderArea() && chunkBuildCount >= chunkBuildCountGoal) {
                    stopBuilding(chunkLoadedCount, chunkBuildCount, (int) chunkBuildCountGoal);
                    log("Successfully pre-loaded the world! Stopping...");
                }
            }
        // Tick Timer for Pause Menu Cancellation
        } else if (timeDown < timeDownGoal) {
            timeDown++;
            if (getDebug()) log("" + timeDown);
        }
    }
}