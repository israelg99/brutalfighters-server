package com.brutalfighters.server.data.projectiles;

import java.util.ArrayList;
import java.util.Iterator;

public class Projectiles {
	private ArrayList<Projectile> projectiles;
	
	public Projectiles() {
		projectiles = new ArrayList<Projectile>();
	}
	
	public void add(Projectile p) {
		projectiles.add(p);
		p.initialize();
	}
	
	public ProjectileData getProjectile(int i) {
		return projectiles.get(i).getProjectile();
	}
	public Projectile get(int i) {
		return projectiles.get(i);
	}
	public ArrayList<Projectile> getAll() {
		return projectiles;
	}
	
	public void update() {
		for (Iterator<Projectile> iterator = projectiles.iterator(); iterator.hasNext();) {
			Projectile projectile = iterator.next();
			if(projectile.getProjectile().isExplode()) {
				iterator.remove();
			} else {
				projectile.update(iterator);
			}
		}
	}

	public void remove(int i) {
		projectiles.remove(i);
	}
}
