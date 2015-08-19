package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.esotericsoftware.kryonet.Connection;

public class Chip extends Fighter {
	
	public Chip(Base base, String m_id) {
		super(base, m_id, "chip", 600, 1000, 90,100, 12, 21, 44, 500, 110, 10, 68, //$NON-NLS-1$
				9, new int[] {300,200,300,600}, new int[] {900,820,560,440});
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
		
		if(getPlayer().skillCD[0] > 0) {
				if(getPlayer().skillCD[0] == skillTempCD[0] - GameServer.getDelay() * 14) {
					float xstart = getPlayer().posx + convertSpeed(getPlayer().width/2);
					Projectiles.addProjectile(cnct, getPlayer().team, "Chip_RPG", xstart, getPlayer().posy-3, getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			getPlayer().skillCD[0] -= GameServer.getDelay();
		} else {
			endSkill1(cnct);
		}
	}
	
	@Override
	public void endSkill1(Connection cnct) {
		getPlayer().skillCD[0] = skillTempCD[0];
		getPlayer().isSkill1 = false;
		getPlayer().isSkilling = false;
	}

	
	// Skill 2

	// Variables
	public final int S2_Velocity = 5;
	
	@Override
	public void startSkill2(Connection cnct) {
		if(!getPlayer().collidesTop && applySkillMana(1)) {
			getPlayer().vely = JUMP_HEIGHT;
			
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
		
		if(getPlayer().skillCD[1] > 0) {
			if(getPlayer().skillCD[1] >= skillTempCD[1] - GameServer.getDelay() * 10) {
				getPlayer().vely += S2_Velocity;
			}
			getPlayer().skillCD[1] -= GameServer.getDelay();
		} else {
			endSkill2(cnct);
		}
	}
	
	@Override
	public void endSkill2(Connection cnct) {
		getPlayer().skillCD[1] = skillTempCD[1];
		getPlayer().isSkill2 = false;
		getPlayer().isSkilling = false;
	}
	
	
	// Skill 3
	
	@Override
	public void skill3(Connection cnct) {
		updateSkill3(cnct);
		
		if(getPlayer().skillCD[2] > 0) {
			if(getPlayer().skillCD[2] == skillTempCD[2] - GameServer.getDelay() * 9) {
				float xstart = getPlayer().posx + convertSpeed(getPlayer().width-10);
				Projectiles.addProjectile(cnct, getPlayer().team, "Chip_TNT", xstart, getPlayer().posy-8, getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			getPlayer().skillCD[2] -= GameServer.getDelay();
		} else {
			endSkill3(cnct);
		}
		
	}
	
	@Override
	public void endSkill3(Connection cnct) {
		getPlayer().skillCD[2] = skillTempCD[2];
		getPlayer().isSkill3 = false;
		getPlayer().isSkilling = false;
	}
	
	
	// Skill 4
	
	@Override
	public void skill4(Connection cnct) {
		updateSkill4(cnct);
		
		if(getPlayer().skillCD[3] > 0) {
			if(getPlayer().skillCD[3] == skillTempCD[3] - GameServer.getDelay() * 5) {
				float xstart = getPlayer().posx + convertSpeed(getPlayer().width/2+5);
				Projectiles.addProjectile(cnct, getPlayer().team, "Chip_MINE", xstart, getPlayer().posy-getPlayer().height/2-10, getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			getPlayer().skillCD[3] -= GameServer.getDelay();
		} else {
			endSkill4(cnct);
		}
		
	}
	
	@Override
	public void endSkill4(Connection cnct) {
		getPlayer().skillCD[3] = skillTempCD[3];
		getPlayer().isSkill4 = false;
		getPlayer().isSkilling = false;
	}
}
