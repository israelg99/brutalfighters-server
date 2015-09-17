package com.brutalfighters.server.data.projectiles.types;

import java.util.Iterator;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.data.projectiles.Projectile;
import com.brutalfighters.server.util.Vec2;

public class TNT extends Projectile {

	public TNT(Fighter fighter, String flip, Vec2 pos, float speed, float dmg, Buff[] buffs) {
		super("TNT", fighter, flip, pos, new Vec2(60,67), speed, dmg, buffs); //$NON-NLS-1$
	}
	
	@Override
	public void update(Iterator<Projectile> iterator) {
		getProjectile().addTime(GameServer.getDelay());
		if(!getProjectile().isExplode()) {
			if(getProjectile().getTime() >= 500) {
				getProjectile().setExplode();
				dealDamage();
			} else if(!isColliding()) {
				getProjectile().getPos().addX(getProjectile().getVel().getX());
			} else {
				getProjectile().setExplode();
				return;
			}
		} else {
			iterator.remove();
		}
	}
}
