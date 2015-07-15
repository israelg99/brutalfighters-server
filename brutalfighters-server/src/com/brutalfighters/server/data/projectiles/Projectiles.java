package com.brutalfighters.server.data.projectiles;

import java.util.ArrayList;
import java.util.Iterator;

import com.brutalfighters.server.matches.GameMatchManager;
import com.esotericsoftware.kryonet.Connection;

public class Projectiles {
	private ArrayList<ActiveProjectile> projectiles;
	
	public Projectiles() {
		projectiles = new ArrayList<ActiveProjectile>();
	}
	
	public void add(ActiveProjectile p) {
		projectiles.add(p);
		Projectile.valueOf(p.data().name).initialize(p);
	}
	public void add(Connection cnct, String name, int posx, int posy, int width, int height, String flip, String mode) {
		add(cnct, GameMatchManager.getClosedMatch(cnct).getPlayer(cnct).team, name, posx, posy, width, height, flip, mode);
	}
	public void add(Connection cnct, int team, String name, float posx, float posy, int width, int height, String flip, String mode) {
		ProjectileData pd = new ProjectileData();
		pd.name = name;
		pd.x = posx;
		pd.y = posy;
		pd.width = width;
		pd.height = height;
		pd.flip = flip;
		pd.mode = mode;
		
		add(new ActiveProjectile(cnct, pd, team));
	}
	
	public ProjectileData getData(int i) {
		return projectiles.get(i).data();
	}
	public ActiveProjectile get(int i) {
		return projectiles.get(i);
	}
	public ArrayList<ActiveProjectile> getAll() {
		return projectiles;
	}
	
	public void updateProjectiles() {
		for (Iterator<ActiveProjectile> iterator = projectiles.iterator(); iterator.hasNext();) {
			ActiveProjectile projectile = iterator.next();
			if(projectile.data().mode == Projectile.explode) {
				iterator.remove();
			} else {
				Projectile.valueOf(projectile.data().name).update(projectile, iterator);
			}
		}
	}
	
	public static void addProjectile(Connection cnct, int team, String name, float posx, float posy, String flip, String mode) {
		Projectile projectile = Projectile.valueOf(name);
		GameMatchManager.getCurrentMatch().getProjectiles().add(cnct, team, name, posx, posy, projectile.WIDTH, projectile.HEIGHT, flip, mode);
	}

	public void remove(int i) {
		projectiles.remove(i);
	}
}
