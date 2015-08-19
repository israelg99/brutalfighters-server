package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.esotericsoftware.kryonet.Connection;

public class Lust extends Fighter {

	public Lust(Base base, String m_id) {
		super(base, m_id, "lust", 800, 1000, 90,100, 14, 26, 48, 500, 150, 10, 75, //$NON-NLS-1$
				9, new int[] {250,200,400,300}, new int[] {600,380,0,450});
	}
	
	// SKILLS

	// Skill 1
	
	// Variables
	public final int S1_DMG = 200, S1_HEIGHT = HEIGHT*2, S1_WIDTH = 100;
	
	@Override
	public void startSkill1(Connection cnct) {
		if(!getPlayer().collidesTop && applySkillMana(0)) {
			getPlayer().vely = JUMP_HEIGHT;
		} else {
			endSkill1(cnct);
		}
	}
	
	@Override
	public void updateSkill1(Connection cnct) {
		applyVelocity();
	}

	@Override
	public void skill1(Connection cnct) {
		
		updateSkill1(cnct);
		
		if(getPlayer().skillCD[0] > 0) {
			if(getPlayer().skillCD[0] == skillTempCD[0] - GameServer.getDelay() * 3) {
				AOE.dealAOE_enemy(getPlayer().team, CollisionDetection.getBounds(getPlayer().flip, getPlayer().posx, getPlayer().posy, S1_WIDTH, S1_HEIGHT), -S1_DMG);
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
	
	@Override
	public void updateSkill2(Connection cnct) {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill2(Connection cnct) {
		
		updateSkill2(cnct);
		
		if(getPlayer().skillCD[1] > 0) {
			if(getPlayer().skillCD[1] == skillTempCD[1] - GameServer.getDelay() * 3) {
				float xstart = getPlayer().posx + convertSpeed(10);
				Projectiles.addProjectile(cnct, getPlayer().team, "Lust_EnergyBall", xstart, getPlayer().posy-3, getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
	
	public final int S3_HP = 200;
	
	@Override
	public void startSkill3(Connection cnct) {
		if(getPlayer().hp < MAXHP && applySkillMana(2)) {
			applyHP(S3_HP);
			endSkill3(cnct);
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
	public void startSkill4(Connection cnct) {
		if(applySkillMana(3)) {
			getPlayer().isVulnerable = false;
		} else {
			endSkill4(cnct);
		}
	}
	
	@Override
	public void updateSkill4(Connection cnct) {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill4(Connection cnct) {
		updateSkill4(cnct);
		
		getPlayer().skillCD[3] -= GameServer.getDelay();
		if(getPlayer().skillCD[3] <= 0) {
			endSkill4(cnct);
		}
	}
	
	@Override
	public void endSkill4(Connection cnct) {
		getPlayer().skillCD[3] = skillTempCD[3];
		getPlayer().isSkill4 = false;
		getPlayer().isSkilling = false;
		getPlayer().isVulnerable = true;
	}
}
