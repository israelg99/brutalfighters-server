package com.brutalfighters.server.util;

import java.awt.Rectangle;

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
	
	public static Rectangle getBounds(String flip, float x, float y, float width, float height) {
		return getBounds(flip, (int)x, (int)y, (int)width, (int)height);
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
	public static void setBounds(Rectangle rectangle, String flip, float x, float y, float width, float height) {
		setBounds(rectangle, flip, (int)x, (int)y, (int)width, (int)height);
	}
}
