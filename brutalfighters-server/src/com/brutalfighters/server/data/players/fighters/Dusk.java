package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.Buff_RedBats;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.types.BigBats;
import com.brutalfighters.server.data.projectiles.types.PurpleLaser;
import com.brutalfighters.server.data.projectiles.types.PurpleBat;
import com.brutalfighters.server.data.projectiles.types.SmallBats;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.MathUtil;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

public class Dusk extends Fighter {
	
	public Dusk(Connection connection, Base base, String m_id) {
		super(connection, base, m_id, "dusk", 900, 1000, new Vec2(90,100), 10, //$NON-NLS-1$
				21, 44, 500, new Vec2(110,10), 68, 9,
				new int[] {300,300,300,500}, new int[] {740,820,580,500});
	}
	
	@Override
	public void updateSkill1() {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill1() {
		
		updateSkill1();
		
		if(getPlayer().getSkillCD()[0] > 0) {
				if(getPlayer().getSkillCD()[0] == max_skillCD[0] - GameServer.getDelay() * 12) {
					float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()*2);
					GameMatchManager.getCurrentProjectiles().add(new BigBats(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()), 17, 0, new Buff[] {new Buff_RedBats()})); 
				}
			getPlayer().getSkillCD()[0] -= GameServer.getDelay();
		} else {
			endSkill1();
		}
	}
	
	@Override
	public void endSkill1() {
		getPlayer().getSkillCD()[0] = max_skillCD[0];
		getPlayer().setSkill1(false);
		getPlayer().disableSkilling();
	}

	
	@Override
	public void skill2() {
		updateSkill2();
		
		if(getPlayer().getSkillCD()[1] > 0) {
			if(getPlayer().getSkillCD()[1] == max_skillCD[1] - GameServer.getDelay() * 13) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX());
				GameMatchManager.getCurrentProjectiles().add(new SmallBats(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()), 45, 100, new Buff[0])); 
			}
			getPlayer().getSkillCD()[1] -= GameServer.getDelay();
		} else {
			endSkill2();
		}
		
	}
	
	@Override
	public void endSkill2() {
		getPlayer().getSkillCD()[1] = max_skillCD[1];
		getPlayer().setSkill2(false);
		getPlayer().disableSkilling();
	}
	
	@Override
	public void skill3() {
		updateSkill3();
		
		if(getPlayer().getSkillCD()[2] > 0) {
			if(getPlayer().getSkillCD()[2] == max_skillCD[2] - GameServer.getDelay() * 10) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()*2);
				GameMatchManager.getCurrentProjectiles().add(new PurpleLaser(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()+8), 30, 110, new Buff[0])); 
			}
			getPlayer().getSkillCD()[2] -= GameServer.getDelay();
		} else {
			endSkill3();
		}
		
	}
	
	@Override
	public void endSkill3() {
		getPlayer().getSkillCD()[2] = max_skillCD[2];
		getPlayer().setSkill3(false);
		getPlayer().disableSkilling();
	}
	
	
	@Override
	public void skill4() {
		updateSkill4();
		
		if(getPlayer().getSkillCD()[3] > 0) {
			float xstart = getPlayer().getPos().getX() + convertSpeed(MathUtil.nextInt(20,80));
			GameMatchManager.getCurrentProjectiles().add(new PurpleBat(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()+MathUtil.nextInt(-40, +30)), 30, 25, new Buff[0])); 
			getPlayer().getSkillCD()[3] -= GameServer.getDelay();
		} else {
			endSkill4();
		}
	}
	
	@Override
	public void endSkill4() {
		getPlayer().getSkillCD()[3] = max_skillCD[3];
		getPlayer().setSkill4(false);
		getPlayer().disableSkilling();
	}
}
