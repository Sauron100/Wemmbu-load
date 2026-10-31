package de.characterloading;

import net.fabricmc.api.ClientModInitializer;

public class CharacterLoadingClient implements ClientModInitializer {
    public static final String MOD_ID = "characterloading";

    @Override
    public void onInitializeClient() {
        Config.load();
    }
}
