package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

public class Chip extends Fighter {
	
	public Chip(Base base, String m_id) {
		super(base, m_id, "chip", 600, 1000, new Vec2(90,100), 12, //$NON-NLS-1$
				21, 44, 500, new Vec2(110,10), 68, 9,
				new int[] {300,200,300,600}, new int[] {900,820,560,440});
	}
	
	// SKILLS
	
	// Skill 1
	
	@Override
	public void updateSkill1(Connection cnct) {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill1(Connection cnct) {
		
		updateSkill1(cnct);
		
		if(getPlayer().getSkillCD()[0] > 0) {
				if(getPlayer().getSkillCD()[0] == max_skillCD[0] - GameServer.getDelay() * 14) {
					float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()/2);
					Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Chip_RPG", xstart, getPlayer().getPos().getY()-3, getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			getPlayer().getSkillCD()[0] -= GameServer.getDelay();
		} else {
			endSkill1(cnct);
		}
	}
	
	@Override
	public void endSkill1(Connection cnct) {
		getPlayer().getSkillCD()[0] = max_skillCD[0];
		getPlayer().setSkill1(false);
		getPlayer().disableSkilling();
	}

	
	// Skill 2

	// Variables
	public final int S2_Velocity = 5;
	
	@Override
	public void startSkill2(Connection cnct) {
		if(!getPlayer().isCollidingTop() && applySkillMana(1)) {
			getPlayer().getVel().setY(getJumpHeight().getX());
			
		} else {
			endSkill2(cnct);
		}
	}
	
	@Override
	public void updateSkill2(Connection cnct) {
		applyVelocity();
	}
	
	@Override
	public void skill2(Connection cnct) {
		
		updateSkill2(cnct);
		
		if(getPlayer().getSkillCD()[1] > 0) {
			if(getPlayer().getSkillCD()[1] >= max_skillCD[1] - GameServer.getDelay() * 10) {
				getPlayer().getVel().addY(S2_Velocity);
			}
			getPlayer().getSkillCD()[1] -= GameServer.getDelay();
		} else {
			endSkill2(cnct);
		}
	}
	
	@Override
	public void endSkill2(Connection cnct) {
		getPlayer().getSkillCD()[1] = max_skillCD[1];
		getPlayer().setSkill2(false);
		getPlayer().disableSkilling();
	}
	
	
	// Skill 3
	
	@Override
	public void skill3(Connection cnct) {
		updateSkill3(cnct);
		
		if(getPlayer().getSkillCD()[2] > 0) {
			if(getPlayer().getSkillCD()[2] == max_skillCD[2] - GameServer.getDelay() * 9) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()-10);
				Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Chip_TNT", xstart, getPlayer().getPos().getY()-8, getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			getPlayer().getSkillCD()[2] -= GameServer.getDelay();
		} else {
			endSkill3(cnct);
		}
		
	}
	
	@Override
	public void endSkill3(Connection cnct) {
		getPlayer().getSkillCD()[2] = max_skillCD[2];
		getPlayer().setSkill3(false);
		getPlayer().disableSkilling();
	}
	
	
	// Skill 4
	
	@Override
	public void skill4(Connection cnct) {
		updateSkill4(cnct);
		
		if(getPlayer().getSkillCD()[3] > 0) {
			if(getPlayer().getSkillCD()[3] == max_skillCD[3] - GameServer.getDelay() * 5) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()/2+5);
				Projectiles.addProjectile(cnct, getPlayer().getTeam(), "Chip_MINE", xstart, getPlayer().getPos().getY()-getPlayer().getSize().getY()/2-10, getPlayer().getFlip(), "init"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			getPlayer().getSkillCD()[3] -= GameServer.getDelay();
		} else {
			endSkill4(cnct);
		}
		
	}
	
	@Override
	public void endSkill4(Connection cnct) {
		getPlayer().getSkillCD()[3] = max_skillCD[3];
		getPlayer().setSkill4(false);
		getPlayer().disableSkilling();
	}
}
