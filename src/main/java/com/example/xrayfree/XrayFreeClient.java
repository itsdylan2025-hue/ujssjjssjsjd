package com.example.xrayfree;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class XrayFreeClient implements ClientModInitializer {
	private static KeyBinding freelookKey;
	private static KeyBinding seeThroughKey;

	@Override
	public void onInitializeClient() {
		freelookKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Toggle Freelook", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, "XrayFree"));
		seeThroughKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Toggle See-Through", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "XrayFree"));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null || client.world == null) return;
			while (freelookKey.wasPressed()) {
				Ghost.toggleFreelook(client);
			}
			while (seeThroughKey.wasPressed()) {
				Ghost.toggleSeeThrough(client);
			}
		});
	}
}
