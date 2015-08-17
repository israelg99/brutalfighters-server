package com.brutalfighters.server.tiled;

import java.util.HashMap;
import java.util.Map;

public class Tileset {
	
	private static final String BLOCKED = "blocked"; //$NON-NLS-1$
	private static final String BLOCKED_TOP = "top"; //$NON-NLS-1$	
	private static final String STEP = "step"; //$NON-NLS-1$
	private static final String TELEPORT = "teleport"; //$NON-NLS-1$
	private static final String RATIO = "ratio"; //$NON-NLS-1$
	
	private Map<String, Object> properties;
	
	public Tileset() {
		this.properties = new HashMap<String, Object>();
	}
	
	public static String BLOCKED() {
		return BLOCKED;
	}
	public static String BLOCKED_TOP() {
		return BLOCKED_TOP;
	}
	public static String STEP() {
		return STEP;
	}
	public static String TELEPORT() {
		return TELEPORT;
	}
	public static String RATIO() {
		return RATIO;
	}

	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	public Object getProperty(String key) {
		return hasProperty(key) ? properties.get(key) : "none"; //$NON-NLS-1$
	}
	public boolean hasProperty(String key) {
		return properties.containsKey(key);
	}
	
	public boolean isBlocked() {
		return BLOCKED().equalsIgnoreCase(getBlocked());
	}
	public String getBlocked() {
		return getProperty(BLOCKED()).toString();
	}
	public String getStep() {
		return getProperty(STEP()).toString();
	}
	
	public float getRatio() {
		String ratio = getProperty(RATIO()).toString();
		return !ratio.equals("none") ? Float.parseFloat(ratio) : 1;  //$NON-NLS-1$
	}
}
