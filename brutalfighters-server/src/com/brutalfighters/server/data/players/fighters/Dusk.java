package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.brutalfighters.server.util.MathUtil;
import com.esotericsoftware.kryonet.Connection;

public class Dusk extends Fighter {
	
	public Dusk(Base base, String m_id) {
		super(base, m_id, "dusk", 900, 1000, 90,100, 10, 21, 44, 500, 110, 10, 68, //$NON-NLS-1$
			9, new int[] {300,300,300,500}, new int[] {740,820,580,500});
	}
	
	@Override
	public void updateSkill1(Connection cnct) {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill1(Connection cnct) {
		
		updateSkill1(cnct);
		
		if(getPlayer().skillCD[0] > 0) {
				if(getPlayer().skillCD[0] == skillTempCD[0] - GameServer.getDelay() * 12) {
					float xstart = getPlayer().posx + convertSpeed(getPlayer().width*2);
					Projectiles.addProjectile(cnct, getPlayer().team, "Dusk_BATZ", xstart, getPlayer().posy, getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
			if(getPlayer().skillCD[1] == skillTempCD[1] - GameServer.getDelay() * 13) {
				float xstart = getPlayer().posx + convertSpeed(getPlayer().width);
				Projectiles.addProjectile(cnct, getPlayer().team, "Dusk_BATS", xstart, getPlayer().posy, getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
	
	@Override
	public void skill3(Connection cnct) {
		updateSkill3(cnct);
		
		if(getPlayer().skillCD[2] > 0) {
			if(getPlayer().skillCD[2] == skillTempCD[2] - GameServer.getDelay() * 10) {
				float xstart = getPlayer().posx + convertSpeed(getPlayer().width*2);
				Projectiles.addProjectile(cnct, getPlayer().team, "Dusk_LASER", xstart, getPlayer().posy+8, getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
			float xstart = getPlayer().posx + convertSpeed(MathUtil.nextInt(20,80));
			Projectiles.addProjectile(cnct, getPlayer().team, "Dusk_PurpleBat", xstart, getPlayer().posy+MathUtil.nextInt(-40, +30), getPlayer().flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
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
