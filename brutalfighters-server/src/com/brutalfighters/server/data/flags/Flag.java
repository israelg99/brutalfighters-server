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


public class Flag {
	
	private FlagData flag;
	
	private Flag(Vec2 pos, Vec2 vel, boolean isTaken, String flip) {
		flag = new FlagData(pos, vel, isTaken, flip);
	}
	public Flag(Vec2 pos, String flip) {
		this(pos, new Vec2(), false, flip);
	}
	public Flag(FlagData flag) {
		this(flag.getPos(), flag.getVel(), flag.isTaken(), flag.getFlip());
	}
	
	/* Getters and Setters */
	public static Flag getFlag(String map, int team) {
		return MapManager.getMap(map).getFlag(team);
	}
	public FlagData getFlag() {
		return flag;
	}
	public void setFlag(FlagData flag) {
		this.flag = flag;
	}

	public boolean inBase(String mapName, int team) {
		Flag mflag = MapManager.getMap(mapName).getFlag(team);
		return collides(mflag);
	}
	
	public void updateFlag(int index) {
		// FLAG IS NOT TAKEN
		if(!getFlag().isTaken()) {
			getFlag().getVel().setX(0);
			if(!collidesBot()) {
				getFlag().getVel().setY(-FlagData.getGravity());
			} else if(getFlag().getVel().getY() != 0) {
				int tiles = GameMatchManager.getCurrentMap().getTileHeight();
				getFlag().getVel().setY(0);
				getFlag().getPos().setY((int)(getFlag().getPos().getY() / tiles) * tiles + FlagData.getSize().getY()/2 - 1);
			}
			
			getFlag().getPos().addY(getFlag().getVel().getY());
		
		// FLAG IS TAKEN
		} else {
			boolean isTaken = false;
			
			PlayerData p;
			
			for(Map.Entry<Connection, Fighter> entry : GameMatchManager.getCurrentMatch().getEnemyTeam(index).entrySet()) {
				
				p = entry.getValue().getPlayer();
				
				if(p.isHoldingFlag()) {
					
					float pad = p.getSize().getX()/3;
					
					if(p.getFlip().equals("left")) { //$NON-NLS-1$
						getFlag().flipRight();
					} else {
						pad = -pad;
						getFlag().flipLeft();
					}
					
					getFlag().getPos().setX(p.getPos().getX() + pad);
					getFlag().getPos().setY(p.getPos().getY() + p.getSize().getY()/3 + 15);
					
					getFlag().getVel().setX(p.getVel().getX());
					getFlag().getVel().setY(p.getVel().getY());
					
					isTaken = true;
					
					break;
				}
			}
			if(!isTaken) {
				getFlag().gotDropped();
			}
		}
	}
	
	public Rectangle getBounds() {
		return CollisionDetection.getBounds("both", getFlag().getPos().getX(), getFlag().getPos().getY(), FlagData.getSize().getX(), FlagData.getSize().getY()); //$NON-NLS-1$
	}
	
	public boolean collides(Flag flag) {
		return getBounds().intersects(flag.getBounds());
	}
	public boolean collidesBot() {
		return GameMatchManager.getCurrentMap().intersects(getFlag().getPos().getX(), getFlag().getPos().getY()-FlagData.getSize().getY()/2, getBounds());
	}
}
