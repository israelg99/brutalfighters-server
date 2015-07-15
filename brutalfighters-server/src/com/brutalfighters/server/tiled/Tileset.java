package com.brutalfighters.server.tiled;

import java.util.HashMap;
import java.util.Map;

import com.brutalfighters.server.data.maps.GameMap;

public class Tileset {
	private Map<String, Object> properties;
	
	public Tileset() {
		this.properties = new HashMap<String, Object>();
	}
	
	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	public Object getProperty(String key) {
		return properties.get(key);
	}
	public boolean hasProperty(String key) {
		return properties.containsKey(key);
	}
	
	public boolean isBlocked() {
		return hasProperty("blocked") ? GameMap.BLOCKED().equalsIgnoreCase((String) getProperty("blocked")) : false; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getKind() {
		return hasProperty("kind") ? ((String) getProperty("kind")) : "none"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
