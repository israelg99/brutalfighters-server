package com.brutalfighters.server.data.maps;

import java.util.HashMap;

import com.brutalfighters.server.tiled.TMXMapLoader;

public class MapManager {
	private static HashMap<String, CTFMap> maps = new HashMap<String, CTFMap>();
	private static final String DEFAULT_MAP = "green_field_tp"; //$NON-NLS-1$
	
	public static void registerMaps() {
		TMXMapLoader.Load();
		
		maps.put("green_field", new CTFMap(TMXMapLoader.readMap("assets/maps/green_field/green_field.tmx"))); //$NON-NLS-1$ //$NON-NLS-2$
		maps.put("green_field_short", new CTFMap(TMXMapLoader.readMap("assets/maps/green_field_short/green_field_short.tmx"))); //$NON-NLS-1$ //$NON-NLS-2$
		maps.put("green_field_original", new CTFMap(TMXMapLoader.readMap("assets/maps/green_field_original/green_field_original.tmx"))); //$NON-NLS-1$ //$NON-NLS-2$
		maps.put("green_field_tp", new CTFMap(TMXMapLoader.readMap("assets/maps/green_field_tp/green_field_tp.tmx"))); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public static CTFMap getMap(String map) {
		return maps.get(map);
	}
	
	public static int getMapsLength() {
		return maps.size();
	}
	
	public static String getDefaultMap() {
		return DEFAULT_MAP;
	}
}
