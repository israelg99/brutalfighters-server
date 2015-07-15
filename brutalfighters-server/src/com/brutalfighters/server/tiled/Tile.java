package com.brutalfighters.server.tiled;

import com.brutalfighters.server.data.objects.Collidable;




public class Tile extends Collidable {
	private int id;
	private boolean isBlocked;
	private String kind;
	
	public Tile(int id, int y, int x, int height, int width, boolean isBlocked, String kind) {
		this.id = id;
		this.isBlocked = isBlocked;
		this.kind = kind;
		Rectangle(x,y,width,height);
	}
	
	public boolean isBlocked() {
		return isBlocked;
	}
	public String getKind() {
		return kind;
	}
	public int getID() {
		return id;
	}
}
