package com.example.xrayfree;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.text.Text;

public final class Ghost {
	/** Camera distance from the player. 1.0 = vanilla (4 blocks). */
	public static final float CAMERA_DISTANCE_MULT = 2.5f;

	public static volatile boolean freelook = false;
	public static volatile boolean seeThrough = false;
	public static float camYaw, camPitch;

	private static Perspective prevPerspective = Perspective.FIRST_PERSON;
	private static double prevGamma = 1.0;

	private Ghost() {}

	/** Left Alt: whole thing on/off. */
	public static void toggleFreelook(MinecraftClient mc) {
		if (!freelook) {
			freelook = true;
			camYaw = mc.player.getYaw();
			camPitch = mc.player.getPitch();
			prevPerspective = mc.options.getPerspective();
			mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
			setSeeThrough(mc, true);
			mc.player.sendMessage(Text.literal("Freelook: ON"), true);
		} else {
			setSeeThrough(mc, false);
			freelook = false;
			mc.options.setPerspective(prevPerspective);
			mc.player.sendMessage(Text.literal("Freelook: OFF"), true);
		}
	}

	/** V: see-through view on/off (only while freelook is on). */
	public static void toggleSeeThrough(MinecraftClient mc) {
		if (!freelook) return;
		setSeeThrough(mc, !seeThrough);
		mc.player.sendMessage(Text.literal("See-through: " + (seeThrough ? "ON" : "OFF")), true);
	}

	private static void setSeeThrough(MinecraftClient mc, boolean on) {
		if (on == seeThrough) return;
		seeThrough = on;
		if (on) {
			prevGamma = mc.options.getGamma().getValue();
			mc.options.getGamma().setValue(15.0); // fullbright
		} else {
			mc.options.getGamma().setValue(prevGamma);
		}
		mc.chunkCullingEnabled = !on; // off = caves behind rock still render
		mc.worldRenderer.scheduleTerrainUpdate();
	}
  }
