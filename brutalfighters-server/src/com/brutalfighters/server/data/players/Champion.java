package com.brutalfighters.server.data.players;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.BuffData;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.MathUtil;
import com.esotericsoftware.kryonet.Connection;

public enum Champion {
	
	Blaze("blaze", 1000, 1000, 90,100, 10, 18, 44, 500, 110, 10, 75, //$NON-NLS-1$
			9, new int[] {400,200,950,500}, new int[] {980,580,820,820}) {

		// SKILL 1
		int s1_HEIGHT = 40, s1_WIDTH = 500, dmg = 40; // DEALS AROUND 4 times
		
		@Override
		public void Skill1(PlayerData p, Connection cnct) {
			updateSkill1(p, cnct);
			
			if(p.skillCD[0] > 0) {
				if(p.skillCD[0] <= skillTempCD[0] - GameServer.getDelay() * 12 && p.skillCD[0] >= skillTempCD[0] - GameServer.getDelay() * 15) {
					AOE.dealAOE_enemy(p.team, CollisionDetection.getBounds(p.flip, p.posx + StaticPlayer.convertSpeed(p, 20), p.posy, s1_WIDTH, s1_HEIGHT), -dmg, new BuffData[] {(Buff.getBuff("BIT_SLOW"))}); //$NON-NLS-1$
				}
				p.skillCD[0] -= GameServer.getDelay();
			} else {
				endSkill1(p, cnct);
			}
			
		}
		
		@Override
		public void endSkill1(PlayerData p, Connection cnct) {
			p.skillCD[0] = skillTempCD[0];
			p.isSkill1 = false;
			p.isSkilling = false;
		}
		
		@Override
		public void Skill2(PlayerData p, Connection cnct) {
			updateSkill2(p, cnct);
			
			if(p.skillCD[1] > 0) {
				if(p.skillCD[1] == skillTempCD[1] - GameServer.getDelay() * 7) {
					float xstart = p.posx + StaticPlayer.convertSpeed(p, 50);
					Projectiles.addProjectile(cnct, p.team, "Blaze_BloodBall", xstart, p.posy+5, p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				p.skillCD[1] -= GameServer.getDelay();
			} else {
				endSkill2(p, cnct);
			}
			
		}
		
		@Override
		public void endSkill2(PlayerData p, Connection cnct) {
			p.skillCD[1] = skillTempCD[1];
			p.isSkill2 = false;
			p.isSkilling = false;
		}
		
		// Skill 3
		private final int self_hp = 550;
		
		@Override
		public void Skill3(PlayerData p, Connection cnct) {
			updateSkill3(p, cnct);
			
			if(p.skillCD[2] > 0) {
				if(p.skillCD[2] == skillTempCD[2] - GameServer.getDelay() * 12) {
					Projectiles.addProjectile(cnct, p.team, "Blaze_PHEONIX", p.posx, p.posy+p.height*2, p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
					StaticPlayer.applyHP(p, self_hp);
					AOE.dealAOE_enemy(p.team, CollisionDetection.getBounds("both", p.posx, p.posy, 400, 400), 0, new BuffData[] {(Buff.getBuff("SLOW_HEALING"))}); //$NON-NLS-1$ //$NON-NLS-2$
				}
				p.skillCD[2] -= GameServer.getDelay();
			} else {
				endSkill3(p, cnct);
			}
			
		}
		
		@Override
		public void endSkill3(PlayerData p, Connection cnct) {
			p.skillCD[2] = skillTempCD[2];
			p.isSkill3 = false;
			p.isSkilling = false;
		}
		
		
		@Override
		public void Skill4(PlayerData p, Connection cnct) {
			updateSkill4(p, cnct);
			
			if(p.skillCD[3] > 0) {
				if(p.skillCD[3] / GameServer.getDelay() % 2 == 0) {
					float xstart = p.posx + StaticPlayer.convertSpeed(p, MathUtil.nextInt(20,80));
					Projectiles.addProjectile(cnct, p.team, "Blaze_SkullFire", xstart, p.posy+MathUtil.nextInt(-50, 10), p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				p.skillCD[3] -= GameServer.getDelay();
			} else {
				endSkill4(p, cnct);
			}
			
		}
		
		@Override
		public void endSkill4(PlayerData p, Connection cnct) {
			p.skillCD[3] = skillTempCD[3];
			p.isSkill4 = false;
			p.isSkilling = false;
		}
	},
	
	Dusk("dusk", 900, 1000, 90,100, 10, 21, 44, 500, 110, 10, 68, //$NON-NLS-1$
			9, new int[] {300,300,300,500}, new int[] {740,820,580,500}) {
		
		@Override
		public void updateSkill1(PlayerData p, Connection cnct) {
			defaultUpdate(p);
			StaticPlayer.applyFlip(p);
		}
		
		@Override
		public void Skill1(PlayerData p, Connection cnct) {
			
			updateSkill1(p, cnct);
			
			if(p.skillCD[0] > 0) {
					if(p.skillCD[0] == skillTempCD[0] - GameServer.getDelay() * 12) {
						float xstart = p.posx + StaticPlayer.convertSpeed(p, p.width*2);
						Projectiles.addProjectile(cnct, p.team, "Dusk_BATZ", xstart, p.posy, p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				p.skillCD[0] -= GameServer.getDelay();
			} else {
				endSkill1(p, cnct);
			}
		}
		
		@Override
		public void endSkill1(PlayerData p, Connection cnct) {
			p.skillCD[0] = skillTempCD[0];
			p.isSkill1 = false;
			p.isSkilling = false;
		}

		
		@Override
		public void Skill2(PlayerData p, Connection cnct) {
			updateSkill2(p, cnct);
			
			if(p.skillCD[1] > 0) {
				if(p.skillCD[1] == skillTempCD[1] - GameServer.getDelay() * 13) {
					float xstart = p.posx + StaticPlayer.convertSpeed(p, p.width);
					Projectiles.addProjectile(cnct, p.team, "Dusk_BATS", xstart, p.posy, p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				p.skillCD[1] -= GameServer.getDelay();
			} else {
				endSkill2(p, cnct);
			}
			
		}
		
		@Override
		public void endSkill2(PlayerData p, Connection cnct) {
			p.skillCD[1] = skillTempCD[1];
			p.isSkill2 = false;
			p.isSkilling = false;
		}
		
		@Override
		public void Skill3(PlayerData p, Connection cnct) {
			updateSkill3(p, cnct);
			
			if(p.skillCD[2] > 0) {
				if(p.skillCD[2] == skillTempCD[2] - GameServer.getDelay() * 10) {
					float xstart = p.posx + StaticPlayer.convertSpeed(p, p.width*2);
					Projectiles.addProjectile(cnct, p.team, "Dusk_LASER", xstart, p.posy+8, p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				p.skillCD[2] -= GameServer.getDelay();
			} else {
				endSkill3(p, cnct);
			}
			
		}
		
		@Override
		public void endSkill3(PlayerData p, Connection cnct) {
			p.skillCD[2] = skillTempCD[2];
			p.isSkill3 = false;
			p.isSkilling = false;
		}
		
		
		@Override
		public void Skill4(PlayerData p, Connection cnct) {
			updateSkill4(p, cnct);
			
			if(p.skillCD[3] > 0) {
				float xstart = p.posx + StaticPlayer.convertSpeed(p, MathUtil.nextInt(20,80));
				Projectiles.addProjectile(cnct, p.team, "Dusk_PurpleBat", xstart, p.posy+MathUtil.nextInt(-40, +30), p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
				p.skillCD[3] -= GameServer.getDelay();
			} else {
				endSkill4(p, cnct);
			}
		}
		
		@Override
		public void endSkill4(PlayerData p, Connection cnct) {
			p.skillCD[3] = skillTempCD[3];
			p.isSkill4 = false;
			p.isSkilling = false;
		}
		
	},
	
	Chip("chip", 600, 1000, 90,100, 12, 21, 44, 500, 110, 10, 68, //$NON-NLS-1$
			9, new int[] {300,200,300,600}, new int[] {900,820,560,440}) {

		// SKILLS
		
		// Skill 1
		
		@Override
		public void updateSkill1(PlayerData p, Connection cnct) {
			defaultUpdate(p);
			StaticPlayer.applyFlip(p);
		}
		
		@Override
		public void Skill1(PlayerData p, Connection cnct) {
			
			updateSkill1(p, cnct);
			
			if(p.skillCD[0] > 0) {
					if(p.skillCD[0] == skillTempCD[0] - GameServer.getDelay() * 14) {
						float xstart = p.posx + StaticPlayer.convertSpeed(p, p.width/2);
						Projectiles.addProjectile(cnct, p.team, "Chip_RPG", xstart, p.posy-3, p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				p.skillCD[0] -= GameServer.getDelay();
			} else {
				endSkill1(p, cnct);
			}
		}
		
		@Override
		public void endSkill1(PlayerData p, Connection cnct) {
			p.skillCD[0] = skillTempCD[0];
			p.isSkill1 = false;
			p.isSkilling = false;
		}
	
		
		// Skill 2

		// Variables
		public final int S2_Velocity = 5;
		
		@Override
		public void startSkill2(PlayerData p, Connection cnct) {
			if(!p.collidesTop && applySkillMana(p, 1)) {
				p.vely = JUMP_HEIGHT;
				
			} else {
				endSkill2(p, cnct);
			}
		}
		
		@Override
		public void updateSkill2(PlayerData p, Connection cnct) {
			StaticPlayer.applyVelocity(p);
		}
		
		@Override
		public void Skill2(PlayerData p, Connection cnct) {
			
			updateSkill2(p, cnct);
			
			if(p.skillCD[1] > 0) {
				if(p.skillCD[1] >= skillTempCD[1] - GameServer.getDelay() * 10) {
					p.vely += S2_Velocity;
				}
				p.skillCD[1] -= GameServer.getDelay();
			} else {
				endSkill2(p, cnct);
			}
		}
		
		@Override
		public void endSkill2(PlayerData p, Connection cnct) {
			p.skillCD[1] = skillTempCD[1];
			p.isSkill2 = false;
			p.isSkilling = false;
		}
		
		
		// Skill 3
		
		@Override
		public void Skill3(PlayerData p, Connection cnct) {
			updateSkill3(p, cnct);
			
			if(p.skillCD[2] > 0) {
				if(p.skillCD[2] == skillTempCD[2] - GameServer.getDelay() * 9) {
					float xstart = p.posx + StaticPlayer.convertSpeed(p, p.width-10);
					Projectiles.addProjectile(cnct, p.team, "Chip_TNT", xstart, p.posy-8, p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				p.skillCD[2] -= GameServer.getDelay();
			} else {
				endSkill3(p, cnct);
			}
			
		}
		
		@Override
		public void endSkill3(PlayerData p, Connection cnct) {
			p.skillCD[2] = skillTempCD[2];
			p.isSkill3 = false;
			p.isSkilling = false;
		}
		
		
		// Skill 4
		
		@Override
		public void Skill4(PlayerData p, Connection cnct) {
			updateSkill4(p, cnct);
			
			if(p.skillCD[3] > 0) {
				if(p.skillCD[3] == skillTempCD[3] - GameServer.getDelay() * 5) {
					float xstart = p.posx + StaticPlayer.convertSpeed(p, p.width/2+5);
					Projectiles.addProjectile(cnct, p.team, "Chip_MINE", xstart, p.posy-p.height/2-10, p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				p.skillCD[3] -= GameServer.getDelay();
			} else {
				endSkill4(p, cnct);
			}
			
		}
		
		@Override
		public void endSkill4(PlayerData p, Connection cnct) {
			p.skillCD[3] = skillTempCD[3];
			p.isSkill4 = false;
			p.isSkilling = false;
		}
		
	},
	
	Surge("surge", 1000, 1000, 90,100, 8, 16, 44, 500, 110, 10, 68, //$NON-NLS-1$
			9, new int[] {250,400,200,650}, new int[] {500,900,1200,1300}) {

		// SKILLS
		
		// Skill 1
		final int DMG = 20;
		
		@Override
		public void updateSkill1(PlayerData p, Connection cnct) {
			defaultUpdate(p);
			StaticPlayer.applyFlip(p);
		}
		
		@Override
		public void Skill1(PlayerData p, Connection cnct) {
			
			updateSkill1(p, cnct);
			
			if(p.skillCD[0] > 0) {
					if(p.skillCD[0] == skillTempCD[0] - GameServer.getDelay() * 5) {
						float xstart = p.posx + StaticPlayer.convertSpeed(p, 5);
						Projectiles.addProjectile(cnct, p.team, "Surge_DashBall", xstart, p.posy, p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					if(p.skillCD[0] > skillTempCD[0] - GameServer.getDelay() * 5) {
						AOE.dealAOE_enemy(p.team, CollisionDetection.getBounds(p.flip, p.posx, p.posy, 150, 30), -DMG, new BuffData[] {(Buff.getBuff("BIT_SLOW"))}); //$NON-NLS-1$
						p.velx = StaticPlayer.convertSpeed(p, 20);
					}
				p.skillCD[0] -= GameServer.getDelay();
			} else {
				endSkill1(p, cnct);
			}
		}
		
		@Override
		public void endSkill1(PlayerData p, Connection cnct) {
			p.skillCD[0] = skillTempCD[0];
			p.isSkill1 = false;
			p.isSkilling = false;
		}
	
		// Skill 2
		
		@Override
		public void updateSkill2(PlayerData p, Connection cnct) {
			defaultUpdate(p);
		}
		
		@Override
		public void Skill2(PlayerData p, Connection cnct) {
			
			updateSkill2(p, cnct);
			
			if(p.skillCD[1] > 0) {
					if(p.skillCD[1] == skillTempCD[1] - GameServer.getDelay() * 17) {
						float xstart = p.posx + StaticPlayer.convertSpeed(p, p.width*2);
						Projectiles.addProjectile(cnct, p.team, "Surge_EnergyWave", xstart, p.posy, p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				p.skillCD[1] -= GameServer.getDelay();
			} else {
				endSkill2(p, cnct);
			}
		}
		
		@Override
		public void endSkill2(PlayerData p, Connection cnct) {
			p.skillCD[1] = skillTempCD[1];
			p.isSkill2 = false;
			p.isSkilling = false;
		}
		
		// Skill 3
		
		@Override
		public void updateSkill3(PlayerData p, Connection cnct) {
			defaultUpdate(p);
			StaticPlayer.applyFlip(p);
		}
		
		@Override
		public void Skill3(PlayerData p, Connection cnct) {
			updateSkill3(p, cnct);
			
			if(p.skillCD[2] > 0) {
				if(p.skillCD[2] == skillTempCD[2] - GameServer.getDelay() * 12) {
					p.isVulnerable = false;
				}
				p.skillCD[2] -= GameServer.getDelay();
			} else {
				endSkill3(p, cnct);
			}
		}
		
		@Override
		public void endSkill3(PlayerData p, Connection cnct) {
			p.skillCD[2] = skillTempCD[2];
			p.isSkill3 = false;
			p.isSkilling = false;
			p.isVulnerable = true;
		}
		
		// Skill 4
		
		final int S4_dmg = 300, S4_X_RANGE = 150, S4_Y_RANGE = 350, DISTANCE = 230;
		
		@Override
		public void Skill4(PlayerData p, Connection cnct) {
			updateSkill4(p, cnct);
			
			if(p.skillCD[3] > 0) {
				if(p.skillCD[3] == skillTempCD[3] - GameServer.getDelay() * 14) {
//					StaticPlayer.dealAOE_enemy(p.team,"both", p.posx-p.width-DISTANCE+S4_X_RANGE/2, p.posy-p.height/2+S4_Y_RANGE, S4_X_RANGE, S4_Y_RANGE, -S4_dmg);
					AOE.dealAOE_enemy(p.team, CollisionDetection.getBounds("both", p.posx-p.width-DISTANCE, p.posy, S4_X_RANGE, S4_Y_RANGE), -S4_dmg); //$NON-NLS-1$
//					StaticPlayer.dealAOE_enemy(p.team,"both", p.posx+p.width+DISTANCE-S4_X_RANGE/2, p.posy-p.height/2+S4_Y_RANGE, S4_X_RANGE, S4_Y_RANGE, -S4_dmg);
					AOE.dealAOE_enemy(p.team, CollisionDetection.getBounds("both", p.posx+p.width+DISTANCE, p.posy, S4_X_RANGE, S4_Y_RANGE), -S4_dmg); //$NON-NLS-1$
				}
				p.skillCD[3] -= GameServer.getDelay();
			} else {
				endSkill4(p, cnct);
			}
			
		}
		
		@Override
		public void endSkill4(PlayerData p, Connection cnct) {
			p.skillCD[3] = skillTempCD[3];
			p.isSkill4 = false;
			p.isSkilling = false;
		}
		
	},
	
	Lust("lust", 800, 1000, 90,100, 14, 26, 48, 500, 150, 10, 75, //$NON-NLS-1$
			9, new int[] {250,200,400,300}, new int[] {600,380,0,450}) {
		
		// SKILLS
		
		// Skill 1
		
		// Variables
		public final int S1_DMG = 200, S1_HEIGHT = HEIGHT*2, S1_WIDTH = 100;
		
		@Override
		public void startSkill1(PlayerData p, Connection cnct) {
			if(!p.collidesTop && applySkillMana(p, 0)) {
				p.vely = JUMP_HEIGHT;
			} else {
				endSkill1(p, cnct);
			}
		}
		
		@Override
		public void updateSkill1(PlayerData p, Connection cnct) {
			StaticPlayer.applyVelocity(p);
		}

		@Override
		public void Skill1(PlayerData p, Connection cnct) {
			
			updateSkill1(p, cnct);
			
			if(p.skillCD[0] > 0) {
				if(p.skillCD[0] == skillTempCD[0] - GameServer.getDelay() * 3) {
					AOE.dealAOE_enemy(p.team, CollisionDetection.getBounds(p.flip, p.posx, p.posy, S1_WIDTH, S1_HEIGHT), -S1_DMG);
				}
				p.skillCD[0] -= GameServer.getDelay();
			} else {
				endSkill1(p, cnct);
			}
		}
		
		@Override
		public void endSkill1(PlayerData p, Connection cnct) {
			p.skillCD[0] = skillTempCD[0];
			p.isSkill1 = false;
			p.isSkilling = false;
		}
		
		
		
		
		// Skill 2
		
		@Override
		public void updateSkill2(PlayerData p, Connection cnct) {
			defaultUpdate(p);
			StaticPlayer.applyFlip(p);
		}
		
		@Override
		public void Skill2(PlayerData p, Connection cnct) {
			
			updateSkill2(p, cnct);
			
			if(p.skillCD[1] > 0) {
				if(p.skillCD[1] == skillTempCD[1] - GameServer.getDelay() * 3) {
					float xstart = p.posx + StaticPlayer.convertSpeed(p, 10);
					Projectiles.addProjectile(cnct, p.team, "Lust_EnergyBall", xstart, p.posy-3, p.flip, "init"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				p.skillCD[1] -= GameServer.getDelay();
			} else {
				endSkill2(p, cnct);
			}
		}
		
		@Override
		public void endSkill2(PlayerData p, Connection cnct) {
			p.skillCD[1] = skillTempCD[1];
			p.isSkill2 = false;
			p.isSkilling = false;
		}
		
		
		
		// Skill 3
		
		public final int S3_HP = 200;
		
		@Override
		public void startSkill3(PlayerData p, Connection cnct) {
			if(p.hp < MAXHP && applySkillMana(p, 2)) {
				StaticPlayer.applyHP(p, S3_HP);
				endSkill3(p, cnct);
			} else {
				endSkill3(p, cnct);
			}
		}
		
		@Override
		public void endSkill3(PlayerData p, Connection cnct) {
			p.skillCD[2] = skillTempCD[2];
			p.isSkill3 = false;
			p.isSkilling = false;
		}
		
		
		
		
		// Skill 4
		
		@Override
		public void startSkill4(PlayerData p, Connection cnct) {
			if(applySkillMana(p, 3)) {
				p.isVulnerable = false;
			} else {
				endSkill4(p, cnct);
			}
		}
		
		@Override
		public void updateSkill4(PlayerData p, Connection cnct) {
			defaultUpdate(p);
			StaticPlayer.applyFlip(p);
		}
		
		@Override
		public void Skill4(PlayerData p, Connection cnct) {
			updateSkill4(p, cnct);
			
			p.skillCD[3] -= GameServer.getDelay();
			if(p.skillCD[3] <= 0) {
				endSkill4(p, cnct);
			}
		}
		
		@Override
		public void endSkill4(PlayerData p, Connection cnct) {
			p.skillCD[3] = skillTempCD[3];
			p.isSkill4 = false;
			p.isSkilling = false;
			p.isVulnerable = true;
		}
		
	};
	
	public static final int skills = 4;
	
	public final String NAME;
	public final int MAXHP, MAXMANA, MANAREGEN;
	public final int WIDTH, HEIGHT;
	public final int WALKING_SPEED, RUNNING_SPEED, JUMP_HEIGHT;
	public final int AA_CD, AA_X_RANGE, AA_Y_RANGE, AA_DMG;
	
	public final int[] skillMana;
	
	public final int[] skillTempCD;
	
	private static void defaultUpdate(PlayerData p) {
		p.velx = 0;
		StaticPlayer.applyGravity(p);
	}
	
	Champion(String name, int maxhp, int maxmana, int width, int height, int walking_speed,
				int running_speed, int jump_height, int AACD,
				int AA_X_RANGE, int AA_Y_RANGE, int AA_DMG, int manaRegen,
				int[] skillMana, int[] skillTempCD) {
		
		this.NAME = name;
		
		this.MAXHP = maxhp;
		this.MAXMANA = maxmana;
		this.MANAREGEN = manaRegen;
		
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.WALKING_SPEED = walking_speed;
		this.RUNNING_SPEED = running_speed;
		this.JUMP_HEIGHT = jump_height;
		
		this.AA_CD = AACD;
		this.AA_X_RANGE = AA_X_RANGE;
		this.AA_Y_RANGE = AA_Y_RANGE;
		this.AA_DMG = AA_DMG;
		
		this.skillMana = skillMana;
		this.skillTempCD = skillTempCD;
	}

	public void startSkill1(PlayerData p, Connection cnct) {
		if(!applySkillMana(p, 0)) {
			endSkill1(p, cnct);
		}
	}
	public void updateSkill1(PlayerData p, Connection cnct) {
		defaultUpdate(p);
	}
	public void Skill1(PlayerData p, Connection cnct) {
		
	}
	public void endSkill1(PlayerData p, Connection cnct) {
		
	}

	public void startSkill2(PlayerData p, Connection cnct) {
		if(!applySkillMana(p, 1)) {
			endSkill2(p, cnct);
		}
	}
	public void updateSkill2(PlayerData p, Connection cnct) {
		defaultUpdate(p);
	}
	public void Skill2(PlayerData p, Connection cnct) {
		
	}
	public void endSkill2(PlayerData p, Connection cnct) {
		
	}

	public void startSkill3(PlayerData p, Connection cnct) {
		if(!applySkillMana(p, 2)) {
			endSkill3(p, cnct);
		}
	}
	public void updateSkill3(PlayerData p, Connection cnct) {
		defaultUpdate(p);
	}
	public void Skill3(PlayerData p, Connection cnct) {
		
	}
	public void endSkill3(PlayerData p, Connection cnct) {
		
	}
	
	public void startSkill4(PlayerData p, Connection cnct) {
		if(!applySkillMana(p, 3)) {
			endSkill4(p, cnct);
		}
	}
	public void updateSkill4(PlayerData p, Connection cnct) {
		defaultUpdate(p);
	}
	public void Skill4(PlayerData p, Connection cnct) {
		
	}
	public void endSkill4(PlayerData p, Connection cnct) {
		
	}
	
	public float getSpeed(PlayerData p) {
		return p.isRunning ? RUNNING_SPEED : WALKING_SPEED;
	}
	public void assignSpeed(PlayerData p) {
		p.walking_speed = WALKING_SPEED;
		p.running_speed = RUNNING_SPEED;
	}
	public void AAttack(PlayerData p, Connection cnct) {
		AOE.dealAOE_enemy(p.team, CollisionDetection.getBounds(p.flip, p.posx, p.posy, AA_X_RANGE, AA_Y_RANGE), -AA_DMG);
	}
	public boolean applySkillMana(PlayerData p, int index) {
		if(p.mana >= skillMana[index]) {
			StaticPlayer.applyMana(p, -skillMana[index]);
			return true;
		}
		return false;
	}
	
	public static boolean contains(String fighter) {
	    for (Champion c : Champion.values()) {
	        if (c.name().equals(fighter)) {
	            return true;
	        }
	    }

	    return false;
	}
	
	public static void init() {
		values();
	}
	
}
