package com.brutalfighters.server.data.projectiles;

import java.awt.Rectangle;
import java.util.Iterator;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.objects.Collidable;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

abstract public class Projectile extends Collidable {
	
	protected Fighter fighter;
	protected int team;
	
	protected float dmg;
	protected Buff[] buffs;
	protected float speed;
	
	protected ProjectileData projectile;
	
	protected Projectile(String name, Fighter fighter, String flip, Vec2 pos, Vec2 size, float speed, float dmg, Buff[] buffs) {
		setProjectile(new ProjectileData(name, flip, pos, size));
		setFighter(fighter);
		setTeam(fighter.getPlayer().getTeam());
		setSpeed(speed);
		setDMG(dmg);
		setBuffs(buffs);
		Rectangle(CollisionDetection.getBounds("both", getProjectile().getPos().getX(), getProjectile().getPos().getY(), getProjectile().getSize().getX(), getProjectile().getSize().getY())); //$NON-NLS-1$
	}
	
	public ProjectileData getProjectile() {
		return projectile;
	}
	public void setProjectile(ProjectileData projectile) {
		this.projectile = projectile;
	}

	public Fighter getFighter() {
		return fighter;
	}
	public void setFighter(Fighter fighter) {
		this.fighter = fighter;
	}

	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}

	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getDMG() {
		return dmg;
	}
	public void setDMG(float dmg) {
		this.dmg = dmg;
	}

	public Buff[] getBuffs() {
		return buffs;
	}
	public void setBuffs(Buff[] buffs) {
		this.buffs = buffs;
	}
	
	@Override
	public Rectangle getBounds() {
		setBounds();
		return bounds;
	}
	
	public void setBounds() {
		CollisionDetection.setBounds(bounds, "both", getProjectile().getPos().getX(), getProjectile().getPos().getY(), getProjectile().getSize().getX(), getProjectile().getSize().getY()); //$NON-NLS-1$
	}
	
	public boolean isOwner(Connection cnct) {
		return getFighter().getConnection().equals(cnct);
	}
	public boolean isOwner(Fighter fighter) {
		return getFighter().equals(fighter);
	}

	public void initialize() {
		if(isColliding()) {
			getProjectile().setExplode();
		} else {
			getProjectile().getVel().setX(convertSpeed(speed));
			getProjectile().getVel().setX(convertSpeed(speed));
		}
	}
	public void update(Iterator<Projectile> iterator) {
		getProjectile().addTime(GameServer.getDelay());
		if(!getProjectile().isExplode()) {
			if(dealDamage()) {
				getProjectile().setExplode();
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
	
	public boolean isColliding() { // We may modify it, so it's not static.
		return !GameMatchManager.getCurrentMap().checkBoundaries(getProjectile().getPos()) || GameMatchManager.getCurrentMap().intersects(getProjectile().getPos().getX(), getProjectile().getPos().getY(), getBounds());
	}
	
	public boolean dealDamage() {
		return AOE.dealAOE_enemy(getTeam(), getBounds(), -getDMG(), getBuffs());
	}
	
	public float convertSpeed(float speed) {
		return getProjectile().isRight() ? speed : -speed;
	}
}
