package com.brutalfighters.server.data.objects;

import java.awt.Rectangle;

public class Collidable {
	protected Rectangle bounds;
	
	public void Rectangle(int x, int y, int width, int height) {
		Rectangle(new Rectangle(x,y,width,height));
	}
	public void Rectangle(Rectangle rectangle) {
		bounds = new Rectangle(rectangle);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds.setBounds(bounds);
	}
	public void setBounds(int x, int y, int width, int height) {
		setBounds(new Rectangle(x, y, width, height));
	}
	
	public boolean intersects(Rectangle bounds) {
		return getBounds().intersects(bounds);
	}
}
