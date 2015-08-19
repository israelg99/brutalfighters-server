package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.BuffData;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.esotericsoftware.kryonet.Connection;

public class Surge extends Fighter {

	public Surge(Base base, String m_id) {
		super(base, m_id, "surge", 1000, 1000, 90,100, 8, 16, 44, 500, 110, 10, 68, //$NON-NLS-1$
				9, new int[] {250,400,200,650}, new int[] {500,900,1200,1300});
	}
	
	// SKILLS
	
	// Skill 1
	final int DMG = 20;
	
	@Override
	public void updateSkill1(Connection cnct) {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill1(Connection cnct) {
		
		updateSkill1(cnct);
		
		if(getPlayer().skillCD[0] > 0) {
				if(getPlayer().skillCD[0] == skillTempCD[0] - GameServer.getDelay() * 5) {
					float xstart = getPlayer().posx + convertSpeed(5);
					Projectiles.addProjectile(cnct, getPlayer().team, "Surge_DashBall", xstart, getPlayer().posy, getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				if(getPlayer().skillCD[0] > skillTempCD[0] - GameServer.getDelay() * 5) {
					AOE.dealAOE_enemy(getPlayer().team, CollisionDetection.getBounds(getPlayer().flip, getPlayer().posx, getPlayer().posy, 150, 30), -DMG, new BuffData[] {(Buff.getBuff("BIT_SLOW"))}); //$NON-NLS-1$
					getPlayer().velx = convertSpeed(20);
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
	}
	
	@Override
	public void skill2(Connection cnct) {
		
		updateSkill2(cnct);
		
		if(getPlayer().skillCD[1] > 0) {
				if(getPlayer().skillCD[1] == skillTempCD[1] - GameServer.getDelay() * 17) {
					float xstart = getPlayer().posx + convertSpeed(getPlayer().width*2);
					Projectiles.addProjectile(cnct, getPlayer().team, "Surge_EnergyWave", xstart, getPlayer().posy, getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
	public void updateSkill3(Connection cnct) {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill3(Connection cnct) {
		updateSkill3(cnct);
		
		if(getPlayer().skillCD[2] > 0) {
			if(getPlayer().skillCD[2] == skillTempCD[2] - GameServer.getDelay() * 12) {
				getPlayer().isVulnerable = false;
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
		getPlayer().isVulnerable = true;
	}
	
	// Skill 4
	
	final int S4_dmg = 300, S4_X_RANGE = 150, S4_Y_RANGE = 350, DISTANCE = 230;
	
	@Override
	public void skill4(Connection cnct) {
		updateSkill4(cnct);
		
		if(getPlayer().skillCD[3] > 0) {
			if(getPlayer().skillCD[3] == skillTempCD[3] - GameServer.getDelay() * 14) {
//						dealAOE_enemy(getPlayer().team,"both", getPlayer().posx-getPlayer().width-DISTANCE+S4_X_RANGE/2, getPlayer().posy-getPlayer().height/2+S4_Y_RANGE, S4_X_RANGE, S4_Y_RANGE, -S4_dmg);
				AOE.dealAOE_enemy(getPlayer().team, CollisionDetection.getBounds("both", getPlayer().posx-getPlayer().width-DISTANCE, getPlayer().posy, S4_X_RANGE, S4_Y_RANGE), -S4_dmg); //$NON-NLS-1$
//						dealAOE_enemy(getPlayer().team,"both", getPlayer().posx+getPlayer().width+DISTANCE-S4_X_RANGE/2, getPlayer().posy-getPlayer().height/2+S4_Y_RANGE, S4_X_RANGE, S4_Y_RANGE, -S4_dmg);
				AOE.dealAOE_enemy(getPlayer().team, CollisionDetection.getBounds("both", getPlayer().posx+getPlayer().width+DISTANCE, getPlayer().posy, S4_X_RANGE, S4_Y_RANGE), -S4_dmg); //$NON-NLS-1$
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
