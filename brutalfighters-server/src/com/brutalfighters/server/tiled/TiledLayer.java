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
	
	public void addTile(int id, int tileWidth, int tileHeight, float ratio, String blocked, String step) {
		tiles.add(new Tile(id, (getHeight() - tiles.size() / getWidth()) * tileHeight, tiles.size() % getWidth() * tileWidth, tileHeight, tileWidth, ratio, blocked, step));
		
	}
	public Tile getTile(int x, int y) {
		int i = (getHeight()-y) * width + x;
		return i < tiles.size() ? tiles.get(i) : getTile(0,0);
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}
