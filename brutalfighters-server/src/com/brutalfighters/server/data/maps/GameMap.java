package com.brutalfighters.server.data.maps;

import java.awt.Rectangle;

import com.brutalfighters.server.tiled.Tile;
import com.brutalfighters.server.tiled.TiledMap;
import com.brutalfighters.server.tiled.Tileset;
import com.brutalfighters.server.util.Vec2;

public class GameMap extends TiledMap {

	private static final String BLOCKED = "blocked"; //$NON-NLS-1$
	private static final String TELEPORT = "teleport"; //$NON-NLS-1$
	
	private static final int BLOCK_SIZE = 96;
	
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
		return getTile(i,x,y).isBlocked();
	}
	public String getKind(int i, float x, float y) {
		return getTile(i,x,y).getKind();
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
	private int getWidthScaledPixels() { // WIDTH OF THE WHOLE MAP NOT TILES!!!
		return width * getTileWidth()+BLOCK_SIZE;
	}
	
	private int getHeightScaledPixels() { // HEIGHT OF THE WHOLE MAP NOT TILES!!!
		return height * getTileHeight()+BLOCK_SIZE;
	}
	
	// Collision Detection
	/**
	 * @param bounds Rectangle is used for the AABB which is currently disabled on tiles.
	 */
	public boolean intersects(float x, float y, Rectangle bounds) {
		if(isBlocked(0, x,y)) {
			//return intersect(0,toCellX(x),toCellY(y),bounds); //AABB Implemented but not needed :`(
			return true;
		}
		return false;
	}
	public boolean intersectsSurroundX(float x, float y, Rectangle bounds) {
		return intersects(x-bounds.width/2+1,y, bounds) || intersects(x,y, bounds) || intersects(x+bounds.width/2-1,y, bounds);
	}
	public boolean intersectsSurroundY(float x, float y, Rectangle bounds) {
		return intersects(x,y-bounds.height/2+1, bounds) || intersects(x,y, bounds) || intersects(x,y+bounds.height/2-1, bounds);
	}
	
	// Static
	public static String BLOCKED() {
		return BLOCKED;
	}
	public static int BLOCK_SIZE() {
		return BLOCK_SIZE;
	}
	public static String TELEPORT() {
		return TELEPORT;
	}
	
}
