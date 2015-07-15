package com.brutalfighters.server.data.projectiles;

import java.awt.Rectangle;

import com.brutalfighters.server.data.objects.Collidable;
import com.brutalfighters.server.util.CollisionDetection;
import com.esotericsoftware.kryonet.Connection;

public class ActiveProjectile extends Collidable {
	private Connection connection;
	private int team;
	private ProjectileData projectile;
	
	public ActiveProjectile(Connection cnct, ProjectileData pd, int team) {
		this.setConnection(cnct);
		this.setTeam(team);
		this.setData(pd);
		Rectangle(CollisionDetection.getBounds("both", pd.x, pd.y, pd.width, pd.height)); //$NON-NLS-1$
	}
	
	public ProjectileData data() {
		return projectile;
	}
	public void setData(ProjectileData projectile) {
		this.projectile = projectile;
	}
	
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}
	
	@Override
	public Rectangle getBounds() {
		setBounds();
		return bounds;
	}
	
	public void setBounds() {
		CollisionDetection.setBounds(bounds, "both", data().x, data().y, data().width, data().height); //$NON-NLS-1$
	}
	
	public boolean isOwner(Connection cnct) {
		if(connection.equals(cnct)) {
			return true;
		}
		return false;
	}
}
