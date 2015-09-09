package com.brutalfighters.server.data.maps;

import java.awt.Rectangle;

import com.brutalfighters.server.tiled.Tile;
import com.brutalfighters.server.tiled.TiledMap;
import com.brutalfighters.server.tiled.Tileset;
import com.brutalfighters.server.util.Vec2;

public class GameMap extends TiledMap {

	protected int leftBoundary;

	protected int rightBoundary;

	protected int topBoundary;

	protected int botBoundary;
	
	public GameMap(TiledMap map) {
		super(map.getWidth(), map.getHeight(), map.getTileWidth(), map.getTileHeight());
		
		this.tilesets = map.getTilesets();
		this.tiledlayers = map.getTiledLayers();
		
		this.setLeftBoundary(5);
		this.setRightBoundary(getWidthScaledPixels()-5);
		
		this.setBotBoundary(5);
		this.setTopBoundary(getHeightScaledPixels()-5);
	}	
	
	// Boundaries
	public int getRightBoundary() {
		return rightBoundary;
	}
	public void setRightBoundary(int rightBoundary) {
		this.rightBoundary = rightBoundary;
	}
	public int getLeftBoundary() {
		return leftBoundary;
	}
	public void setLeftBoundary(int leftBoundary) {
		this.leftBoundary = leftBoundary;
	}
	
	public int getTopBoundary() {
		return topBoundary;
	}
	public void setTopBoundary(int topBoundary) {
		this.topBoundary = topBoundary;
	}
	public int getBotBoundary() {
		return botBoundary;
	}
	public void setBotBoundary(int botBoundary) {
		this.botBoundary = botBoundary;
	}
	
	public boolean checkSideBoundaries(float x) {
		return (x > getLeftBoundary() && x < getRightBoundary());
	}
	public boolean checkVerticalBoundaries(float y) {
		return (y > getBotBoundary() && y < getTopBoundary());
	}
	public boolean checkBoundaries(Vec2 pos) {
		return checkSideBoundaries(pos.getX()) && checkVerticalBoundaries(pos.getY());
	}
	
	// Get Tiles
	@Override
	public Tile getTile(int i, int x, int y) {
		return tiledlayers.get(i).getTile(x,y);
	}
	public Tile getTile(int i, float x, float y) {
		return getTile(i, toCellX(x), toCellY(y));
	}
	
	public Tileset getTileset(int i, float x, float y) {
		return getTileset(getTile(i,x,y).getID());
	}
	
	private boolean isBlocked(int i, float x, float y) {
		return isBlocked(Tileset.BLOCKED(), getTile(i,x,y));
	}
	private boolean isBlocked(String blocked, int i, float x, float y) {
		return isBlocked(blocked, getTile(i,x,y));
	}
	private static boolean isBlocked(String blocked, Tile tile) {
		return tile.isBlocked(blocked);
	}
	
	public String getKind(int i, float x, float y) {
		return getTile(i,x,y).getStep();
	}
	
	public int getID(int i, float x, float y) {
		return getTile(i,x,y).getID();
	}
	
	// Get Info
	public int toCellX(float x) {
		return (int) (x/getTileWidth());
	}
	public int toCellY(float y) {
		return ((int) (y/getTileHeight()));
	}
	public int toPixelX(float x) {
		return (int) ((x) * getTileWidth() + getTileWidth()/2);
	}
	public int toPixelY(float y) {
		return (int) ((y) * getTileHeight() + getTileHeight()/2);
	}
	
	// Other map
	private int getWidthScaledPixels() { // width OF THE WHOLE MAP NOT TILES!!!
		return width * getTileWidth()+getTileWidth();
	}
	
	private int getHeightScaledPixels() { // height OF THE WHOLE MAP NOT TILES!!!
		return height * getTileHeight()+getTileHeight();
	}
	
	
	// Collision Detection
	
	/**
	 * @param bounds Rectangle is used for the AABB which is currently disabled on tiles.
	 */
	public boolean intersects(String blocked, float x, float y, Rectangle bounds) {
		
		Tile tile = getTile(0,x,y);
				
		if(isBlocked(blocked, 0, x,y) && (((getTileHeight()-y%getTileHeight()) / getTileHeight()) <= tile.getRatio())) {
			//return intersect(0,toCellX(x),toCellY(y),bounds); //AABB Implemented but not needed :`(
			return true;
		}
		return false;
	}
	public boolean intersectsSurroundX(String blocked, float x, float y, Rectangle bounds) {
		return intersects(blocked, x-bounds.width/2+1,y, bounds) || intersects(blocked, x,y, bounds) || intersects(blocked, x+bounds.width/2-1,y, bounds);
	}
	public boolean intersectsSurroundY(String blocked, float x, float y, Rectangle bounds) {
		return intersects(blocked, x,y-bounds.height/2+1, bounds) || intersects(blocked, x,y, bounds) || intersects(blocked, x,y+bounds.height/2-1, bounds);
	}
	
	/**
	 * @param bounds Rectangle is used for the AABB which is currently disabled on tiles.
	 */
	public boolean intersects(float x, float y, Rectangle bounds) {
		return intersects(Tileset.BLOCKED(), x, y, bounds);
	}
	public boolean intersectsSurroundX(float x, float y, Rectangle bounds) {
		return intersectsSurroundX(Tileset.BLOCKED(), x, y, bounds);
	}
	public boolean intersectsSurroundY(float x, float y, Rectangle bounds) {
		return intersectsSurroundY(Tileset.BLOCKED(), x, y, bounds);
	}
	
	public boolean intersectsSurroundXBoth(String blocked, float x, float y, Rectangle bounds) {
		return intersectsSurroundX(Tileset.BLOCKED(), x, y, bounds) || intersectsSurroundX(blocked, x, y, bounds);
	}
	public boolean intersectsSurroundYBoth(String blocked, float x, float y, Rectangle bounds) {
		return intersectsSurroundY(Tileset.BLOCKED(), x, y, bounds) || intersectsSurroundY(blocked, x, y, bounds);
	}
	
}
