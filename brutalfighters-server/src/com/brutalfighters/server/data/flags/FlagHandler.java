package com.brutalfighters.server.data.flags;

import java.awt.Rectangle;
import java.util.Map;

import com.brutalfighters.server.data.maps.MapManager;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;


public class FlagHandler {
	
	public static final int WIDTH = 20, HEIGHT = 155; // Won't be in the Flag class as the client don't need it
	public static final int GRAVITY = 12;
	
	// Get flag accordingly to your parameters
	public static Flag getFlag(Vec2 pos, String flip) {
		Flag flag = new Flag();
		
		flag.isTaken = false;
		flag.vely = 0;
		flag.velx = 0;
		
		flag.posx = pos.getX();
		flag.posy = pos.getY();
		
		flag.flip = flip;
		
		return flag;
	}
	public static Flag getFlag(Flag flag) {
		Flag temp = getFlag(new Vec2(flag.posx, flag.posy), flag.flip);
		temp.isTaken = flag.isTaken;
		temp.vely = flag.vely;
		temp.velx = flag.velx;
		
		return temp;
	}
	
	public static void setFlag(Flag flag, Flag set) {
		flag.posx = set.posx;
		flag.posy = set.posy;
		flag.isTaken = set.isTaken;
		flag.vely = set.vely;
		flag.velx = set.velx;
		flag.flip = set.flip;
	}
	
	public static Flag getFlag(String map, int team) {
		return MapManager.getMap(map).getFlag(team);
	}

	public static boolean inBase(Flag flag, String mapName, int team) {
		Flag mflag = MapManager.getMap(mapName).getFlag(team);
		return flagsCollide(mflag, flag);
	}

	public static void toBase(Flag flag, String mapName, int team) {
		setFlag(flag, MapManager.getMap(mapName).getFlag(team));
	}
	
	public static void updateFlags(Flag[] flags) {
		for(int i = 0; i < flags.length; i++) {
			updateFlag(i, flags[i]);
		}
	}
	public static void updateFlag(int index, Flag flag) {
		// FLAG IS NOT TAKEN
		if(!flag.isTaken) {
			flag.velx = 0;
			if(!collidesBot(flag)) {
				flag.vely = -GRAVITY;
			} else if(flag.vely != 0) {
				int tiles = GameMatchManager.getCurrentMap().getTileHeight();
				flag.vely = 0;
				flag.posy = (int)(flag.posy / tiles) * tiles + HEIGHT/2 - 1;
			}
			
			flag.posy += flag.vely;
		
		// FLAG IS TAKEN
		} else {
			boolean isTaken = false;
			
			PlayerData p;
			
			for(Map.Entry<Connection, Fighter> entry : GameMatchManager.getCurrentMatch().getEnemyTeam(index).entrySet()) {
				
				p = entry.getValue().getPlayer();
				
				if(p.isFlagged) {
					
					int pad = p.width/3;
					
					if(p.flip.equals("left")) { //$NON-NLS-1$
						flag.flip = "right"; //$NON-NLS-1$
					} else {
						pad = -pad;
						flag.flip = "left"; //$NON-NLS-1$
					}
					
					flag.posx = p.posx + pad;
					flag.posy = p.posy + p.height/3 + 15;
					
					flag.velx = p.velx;
					flag.vely = p.vely;
					
					isTaken = true;
					
					break;
				}
			}
			if(!isTaken) {
				flag.isTaken = false;
			}
		}
	}
	
	public static Rectangle getBounds(Flag flag) {
		return CollisionDetection.getBounds("both", flag.posx, flag.posy, WIDTH, HEIGHT); //$NON-NLS-1$
	}
	
	public static boolean flagsCollide(Flag flag1, Flag flag2) {
		return getBounds(flag1).intersects(getBounds(flag2));
	}
	public static boolean collidesBot(Flag flag) {
		return GameMatchManager.getCurrentMap().intersects(flag.posx, flag.posy-HEIGHT/2, getBounds(flag));
	}
}
