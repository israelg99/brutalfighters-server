package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.BuffData;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.MathUtil;
import com.esotericsoftware.kryonet.Connection;

public class Blaze extends Fighter {
	
	public Blaze(Base base, String m_id) {
		super(base, m_id, "blaze", 1000, 1000, 90,100, 10, 18, 44, 500, 110, 10, 75, //$NON-NLS-1$
				9, new int[] {400,200,950,500}, new int[] {980,580,820,820});
	}
	
	// SKILL 1
	int s1_HEIGHT = 40, s1_WIDTH = 500, dmg = 40; // DEALS AROUND 4 times
	
	@Override
	public void skill1(Connection cnct) {
		updateSkill1(cnct);
		
		if(getPlayer().skillCD[0] > 0) {
			if(getPlayer().skillCD[0] <= skillTempCD[0] - GameServer.getDelay() * 12 && getPlayer().skillCD[0] >= skillTempCD[0] - GameServer.getDelay() * 15) {
				AOE.dealAOE_enemy(getPlayer().team, CollisionDetection.getBounds(getPlayer().flip, getPlayer().posx + convertSpeed(20), getPlayer().posy, s1_WIDTH, s1_HEIGHT), -dmg, new BuffData[] {(Buff.getBuff("BIT_SLOW"))}); //$NON-NLS-1$
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
	
	@Override
	public void skill2(Connection cnct) {
		updateSkill2(cnct);
		
		if(getPlayer().skillCD[1] > 0) {
			if(getPlayer().skillCD[1] == skillTempCD[1] - GameServer.getDelay() * 7) {
				float xstart = getPlayer().posx + convertSpeed(50);
				Projectiles.addProjectile(cnct, getPlayer().team, "Blaze_BloodBall", xstart, getPlayer().posy+5, getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
	private final int self_hp = 550;
	
	@Override
	public void skill3(Connection cnct) {
		updateSkill3(cnct);
		
		if(getPlayer().skillCD[2] > 0) {
			if(getPlayer().skillCD[2] == skillTempCD[2] - GameServer.getDelay() * 12) {
				Projectiles.addProjectile(cnct, getPlayer().team, "Blaze_PHEONIX", getPlayer().posx, getPlayer().posy+getPlayer().height*2, getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
				applyHP(self_hp);
				AOE.dealAOE_enemy(getPlayer().team, CollisionDetection.getBounds("both", getPlayer().posx, getPlayer().posy, 400, 400), 0, new BuffData[] {(Buff.getBuff("SLOW_HEALING"))}); //$NON-NLS-1$ //$NON-NLS-2$
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
	
	
	@Override
	public void skill4(Connection cnct) {
		updateSkill4(cnct);
		
		if(getPlayer().skillCD[3] > 0) {
			if(getPlayer().skillCD[3] / GameServer.getDelay() % 2 == 0) {
				float xstart = getPlayer().posx + convertSpeed(MathUtil.nextInt(20,80));
				Projectiles.addProjectile(cnct, getPlayer().team, "Blaze_SkullFire", xstart, getPlayer().posy+MathUtil.nextInt(-50, 10), getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
