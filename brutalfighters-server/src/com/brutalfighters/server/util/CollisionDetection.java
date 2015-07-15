package com.brutalfighters.server.util;

import java.awt.Rectangle;

import com.brutalfighters.server.data.maps.GameMap;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.tiled.Tile;

public class CollisionDetection {
	
	public static Rectangle getBounds(String flip, int x, int y, int width, int height) {
		if(flip.equals("left")) { //$NON-NLS-1$
			return new Rectangle(x-width,y-height/2,width,height);
		} else if(flip.equals("right")) { //$NON-NLS-1$
			return new Rectangle(x,y-height/2,width,height);
		} else if(flip.equals("both")) { //$NON-NLS-1$
			return new Rectangle(x-width/2,y-height/2,width,height);
		} else {
			return null;
		}
	}
	
	public static Rectangle getBounds(String flip, float x, float y, int width, int height) {
		return getBounds(flip, (int)x, (int)y, width, height);
	}
	
	public static void setBounds(Rectangle rectangle, String flip, int x, int y, int width, int height) {
		if(flip.equals("left")) { //$NON-NLS-1$
			rectangle.setBounds(x-width,y-height/2,width,height);
		} else if(flip.equals("right")) { //$NON-NLS-1$
			rectangle.setBounds(x,y-height/2,width,height);
		} else if(flip.equals("both")) { //$NON-NLS-1$
			rectangle.setBounds(x-width/2,y-height/2,width,height);
		}
	}
	public static void setBounds(Rectangle rectangle, String flip, float x, float y, int width, int height) {
		setBounds(rectangle, flip, (int)x, (int)y, width, height);
	}
	
	// Boundary Methods
	public static float getLeft(PlayerData p) {
		return -p.width/2;
	}
	public static float getRight(PlayerData p) {
		return p.width/2;
	}
	public static float getTop(PlayerData p) {
		return p.height/2;
	}
	public static float getBot(PlayerData p) {
		return -p.height/2;
	}
	
	public static Rectangle getVelocityBounds(PlayerData p, boolean velx, boolean vely) {
		Rectangle bounds = getBounds(p);
		bounds.x += velx ? p.velx : 0;
		bounds.y += vely ? p.vely : 0;
		return bounds;
	}
	
	public static boolean collidesBot(PlayerData p, GameMap map) {
		// BOT!
		return map.intersectsSurroundX(p.posx, p.posy+getBot(p)+p.vely, getVelocityBounds(p, false, true)) || p.posy + p.vely + getBot(p) < map.getBotBoundary();
	}
	public static boolean collidesLeft(PlayerData p, GameMap map) {
		// LEFT!
		return map.intersectsSurroundY(p.posx+getLeft(p)+p.velx, p.posy, getVelocityBounds(p, true, false)) ||p.posx + p.velx + getLeft(p) < map.getLeftBoundary();
	}
	public static boolean collidesRight(PlayerData p, GameMap map) {
		// RIGHT!
		return map.intersectsSurroundY(p.posx+getRight(p)+p.velx, p.posy, getVelocityBounds(p, true, false)) || p.posx + p.velx + getRight(p) > map.getRightBoundary();
	}
	public static boolean collidesTop(PlayerData p, GameMap map) {
		// TOP!
		return map.intersectsSurroundX(p.posx, p.posy+getTop(p), getVelocityBounds(p, false, true)) || p.posy + p.vely + getTop(p) > map.getTopBoundary();
	}
	
	public static Tile getCellOn(PlayerData p, GameMap map) {
		return map.getTile(0, p.posx, p.posy + getBot(p));
	}
	
	public static boolean isFacingCollision(PlayerData p) {
		return (p.flip.equals("right") && p.collidesRight) || (p.flip.equals("left") && p.collidesLeft); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public static Rectangle getBounds(PlayerData p) {
		Rectangle bounds = getBounds("both", p.posx, p.posy, p.width, p.height); //$NON-NLS-1$
		return bounds;
	}
	public static boolean intersects(PlayerData p, Rectangle rect) {
		return getBounds(p).intersects(rect);
	}
	public static boolean intersects(PlayerData p, PlayerData p2) {
		return getBounds(p).intersects(getBounds(p2));
	}
}
