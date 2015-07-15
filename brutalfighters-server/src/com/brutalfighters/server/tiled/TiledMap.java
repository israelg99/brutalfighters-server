package com.brutalfighters.server.tiled;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class TiledMap {
	protected int width, height, tileWidth, tileHeight;
	protected List<Tileset> tilesets = new ArrayList<Tileset>();
	protected List<TiledLayer> tiledlayers = new ArrayList<TiledLayer>();
	
	public TiledMap(int width, int height, int tileWidth, int tileHeight) {
		this.width = width;
		this.height = height;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}
	
	//   TILESETS
	
	// Add Tileset
	public void addTileset() {
		tilesets.add(new Tileset());
	}
	
	// Edit Tileset
	public void editTileset(int id, String key, Object value) {
		tilesets.get(id).setProperty(key, value);
	}
	
	// Get Tileset
	public List<Tileset> getTilesets() {
		return tilesets;
	}
	public Tileset getTileset(int id) {
		return tilesets.get(id);
	}
	public boolean isBlocked(int id) {
		return tilesets.get(id).isBlocked();
	}
	public String getKind(int id) {
		return tilesets.get(id).getKind();
	}
	
	// More Tileset
	public int getTilesetsLength() {
		return tilesets.size();
	}
	
	
	// TILED LAYERS
	
	// Add Tiledlayer
	public void addTiledLayer() {
		tiledlayers.add(new TiledLayer());	
	}
	public void addTiledLayer(int width, int height) {
		tiledlayers.add(new TiledLayer(width, height));	
	}
	
	// Get Tiledlayer
	public List<TiledLayer> getTiledLayers() {
		return tiledlayers;
	}
	public TiledLayer getTiledLayer(int i) {
		return tiledlayers.get(i);
	}
	
	// More TiledLayers
	public int getTiledLayersLength() {
		return tiledlayers.size();
	}
	
	
	
	// Tiles (Into the last TiledLayer)
	
	// Add Tiles
	public void addTile(int i) {
		tiledlayers.get(i).addTile(getTileWidth(), getTileHeight(), getTileset(0).isBlocked(), getTileset(0).getKind());
	}
	public void addTile(int i, int id) {
		tiledlayers.get(i).addTile(id, getTileWidth(), getTileHeight(), getTileset(id).isBlocked(), getTileset(id).getKind());
	}
	
	// Get Tiles
	public Tile getTile(int i, int x, int y) {
		return tiledlayers.get(i).getTile(x,y);
	}
	public int getID(int i, int x, int y) {
		return getTile(i,x,y).getID();
	}
	
	// Other map
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	public int getWidthPixels() {
		return width * getTileWidth();
	}
	
	public int getHeightPixels() {
		return height * getTileHeight();
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}
	
	// Collision Detection
	public Rectangle getBounds(int layer, int x, int y) {
		return getTile(layer,x,y).getBounds();
	}
	public boolean intersect(int layer, int x, int y, Rectangle bounds) {
		return getBounds(layer,x,y).intersects(bounds);
	}
}
