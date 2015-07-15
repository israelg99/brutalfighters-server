package com.brutalfighters.server.tiled;

import java.util.ArrayList;
import java.util.List;

public class TiledLayer {
	private int width, height;
	private List<Tile> tiles = new ArrayList<Tile>();
	
	public TiledLayer() {
		this(0,0);
	}
	public TiledLayer(int width, int height) {
		this.width = width;
		this.height = height-1;
	}
	
	
	public void addTile(int tileWidth, int tileHeight, boolean isBlocked, String kind) {
		addTile(0, tileWidth, tileHeight, isBlocked, kind);
		
	}
	public void addTile(int id, int tileWidth, int tileHeight, boolean isBlocked, String kind) {
		tiles.add(new Tile(id, (getHeight() - tiles.size() / getWidth()) * tileHeight, tiles.size() % getWidth() * tileWidth, tileHeight, tileWidth, isBlocked, kind));
		
	}
	public Tile getTile(int x, int y) {
		return tiles.get((getHeight()-y) * width + x);
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}
