package com.brutalfighters.server.data.projectiles.types;

import java.util.Iterator;

import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.data.projectiles.Projectile;
import com.brutalfighters.server.util.Vec2;

public class Pheonix extends Projectile {

	private final static int vely = 5;
	private final static float pull = 0.2f;
	private final static float max = 33.2f;
	
	public Pheonix(Fighter fighter, Vec2 pos) {
		super("Pheonix", fighter, "right", pos, new Vec2(280,500), 0, 0, new Buff[0]); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public void initialize() {
		getProjectile().getVel().setY(vely);
	}
	
	@Override
	public void update(Iterator<Projectile> iterator) {
		getProjectile().getVel().addY(pull);
		if(getProjectile().getVel().getY() >= max) {
			iterator.remove();
			return;
		}
		getProjectile().getPos().addY(getProjectile().getVel().getY());
	}
}
