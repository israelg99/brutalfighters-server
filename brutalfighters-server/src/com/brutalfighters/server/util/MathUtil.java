package com.brutalfighters.server.util;

import java.util.Random;

public class MathUtil {
	
	public static Random random = new Random();
	
	public static int nextInt(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}
	public static float nextFloat(float min, float max) {
		return min + (int)(random.nextFloat() * ((max - min) + 1));
	}
}
