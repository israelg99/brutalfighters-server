package com.brutalfighters.server.data.projectiles.types;

import java.util.Iterator;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.data.projectiles.Projectile;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.Vec2;

public class Mine extends Projectile {

	private static final int vely = -9;
	
	public Mine(Fighter fighter, String flip, Vec2 pos, float dmg, Buff[] buffs) {
		super("Mine", fighter, flip, pos, new Vec2(60,57), 0, dmg, buffs); //$NON-NLS-1$
	}
	
	@Override
	public void initialize() {
		if(!GameMatchManager.getCurrentMap().checkBoundaries(getProjectile().getPos())) {
			getProjectile().setExplode();
		} else {
			getProjectile().getVel().setX(convertSpeed(getSpeed()));
		}
	}
	
	@Override
	public void update(Iterator<Projectile> iterator) {
		getProjectile().addTime(GameServer.getDelay());
		if(!getProjectile().isExplode()) {
			if(getProjectile().getTime() >= 1000 && dealDamage()) {
				getProjectile().setExplode();
			} else if(!GameMatchManager.getCurrentMap().intersectsSurroundX(getProjectile().getPos().getX(), getProjectile().getPos().getY()-getProjectile().getSize().getY()/2, getBounds())) {
				getProjectile().getVel().setY(vely);
				getProjectile().getPos().addY(getProjectile().getVel().getY());
			} else {
				getProjectile().setExplode();
				return;
			}
		} else {
			iterator.remove();
		}
	}
}
