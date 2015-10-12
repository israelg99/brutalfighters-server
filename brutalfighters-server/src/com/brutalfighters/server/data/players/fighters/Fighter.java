package com.brutalfighters.server.data.players.fighters;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.maps.GameMap;
import com.brutalfighters.server.data.maps.MapManager;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.PlayerMap;
import com.brutalfighters.server.matches.GameMatch;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.tiled.Tile;
import com.brutalfighters.server.tiled.Tileset;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.MathUtil;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

abstract public class Fighter {
	
	// Finals
	protected static final int SKILLS = 4;
	protected static final float AIR_FORCE = 1.4f;
	protected static final int GRAVITY_FORCE = 27;
	protected static final int FALLING_MOMENTUM = 9;
	
	// Connection
	protected Connection connection;
	
	// Mana
	protected int manaRegen;
	
	// Skills
	protected final int[] max_skillCD;
	protected int[] skillMana;
	
	// Match ID
	protected String m_id;
	
	// Connection
	protected boolean isConnected;
	
	// AA
	protected int AA_dmg;
	protected Vec2 AA_range;
	protected Vec2 AA_CD;
	
	// Movement and Jumping speed
	protected Vec2 walking_speed;
	protected Vec2 running_speed;
	protected Vec2 jump_height;
	
	// Max Size
	protected Vec2 max_size;
	
	protected List<Buff> buffs;
	
	// The PlayerData
	protected PlayerData player;
	
	// Jump
	protected boolean jumpSwitch;
	
	// Bounds
	protected Rectangle bounds;
	
	protected Fighter(Connection connection, int team, Base base, String m_id, String name, int maxhp, int maxmana, Vec2 max_size, int walking_speed,
				int running_speed, int jump_height, int AA_CD,
				Vec2 AA_range, int AA_DMG, int manaRegen,
				int[] skillMana, int[] max_skillCD) {
		
		// Setting connection
		setConnection(connection);
		
		// Max Size
		setMaxSize(max_size);	
		
		// Skill Temp CD
		this.max_skillCD = max_skillCD.clone();
		
		// Constructing a new fighter/player data
		setPlayer(new PlayerData(team, base.getPos(), base.getFlip(), name, maxhp, maxmana, getMaxSize(), getMaxSkillCD(), GameMatch.getDefaultRespawn()));

		// Mana
		setManaRegen(manaRegen);
		
		// Movement Speed
		setWalkingSpeed(new Vec2(walking_speed));
		setRunningSpeed(new Vec2(running_speed));
		setJumpHeight(new Vec2(jump_height));

		// Match ID
		setMatchID(m_id);
		
		// Connection
		setConnected(true);
		
		// Auto Attack
		setAA_CD(new Vec2(AA_CD, AA_CD));
		setAA_Range(AA_range);
		setAA_Dmg(AA_DMG);
		
		// Skill Mana
		setSkillMana(skillMana.clone());
		
		// Bounds
		setBounds(new Rectangle());
		
		// Buffs
		resetBuffs();
		
		// Jump
		resetJumpSwitch();

	}
	
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public static int getSkills() {
		return SKILLS;
	}
	public static float getAirForce() {
		return AIR_FORCE;
	}
	public static int getGravityForce() {
		return GRAVITY_FORCE;
	}
	public static int getFallingMomentum() {
		return FALLING_MOMENTUM;
	}
	
	public Vec2 getMaxSize() {
		return max_size;
	}
	public void setMaxSize(Vec2 max_size) {
		this.max_size = new Vec2(max_size);
	}
	
	public List<Buff> getBuffs() {
		return buffs;
	}
	public void setBuffs(List<Buff> buffs) {
		this.buffs = buffs;
	}
	public void resetBuffs() {
		this.buffs = new ArrayList<Buff>();
	}

	public int getManaRegen() {
		return manaRegen;
	}
	public void setManaRegen(int manaRegen) {
		this.manaRegen = manaRegen;
	}
	
	private void setBounds(Rectangle rect) {
		this.bounds = rect;
	}

	public int[] getSkillMana() {
		return skillMana;
	}
	public void setSkillMana(int[] skillMana) {
		this.skillMana = skillMana;
	}

	public String getMatchID() {
		return m_id;
	}
	public void setMatchID(String m_id) {
		this.m_id = m_id;
	}

	public boolean isConnected() {
		return isConnected;
	}
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public int getAA_Dmg() {
		return AA_dmg;
	}
	public void setAA_Dmg(int aA_dmg) {
		AA_dmg = aA_dmg;
	}

	public Vec2 getAA_Range() {
		return AA_range;
	}
	public void setAA_Range(Vec2 aA_range) {
		AA_range = new Vec2(aA_range);
	}

	public Vec2 getAA_CD() {
		return AA_CD;
	}
	public void setAA_CD(Vec2 aA_CD) {
		AA_CD = new Vec2(aA_CD);
	}
	public void resetAA_CD() {
		getAA_CD().setX(getAA_CD().getY());
	}

	public Vec2 getWalkingSpeed() {
		return walking_speed;
	}
	public void setWalkingSpeed(Vec2 walking_speed) {
		this.walking_speed = new Vec2(walking_speed);
	}

	public Vec2 getRunningSpeed() {
		return running_speed;
	}
	public void setRunningSpeed(Vec2 running_speed) {
		this.running_speed = new Vec2(running_speed);
	}

	public Vec2 getJumpHeight() {
		return jump_height;
	}
	public void setJumpHeight(Vec2 jump_height) {
		this.jump_height = new Vec2(jump_height);
	}

	public int[] getMaxSkillCD() {
		return max_skillCD;
	}
	
	protected boolean isJumpSwitched() {
		return jumpSwitch;
	}
	public void jumpSwitch() {
		setJumpSwitch(true);
	}
	protected void resetJumpSwitch() {
		setJumpSwitch(false);
	}
	protected void setJumpSwitch(boolean jumpSwitch) {
		this.jumpSwitch = jumpSwitch;
	}
	
	public final PlayerData getPlayer() {
		return player;
	}
	protected final void setPlayer(PlayerData pdata) {
		this.player = pdata;
	}
	
	public final void update() {
		
		resetExtrapolation();
		
		if(!applyDeath()) {
			
			if(getPlayer().isSkilling()) {
				applySkill();
			} else if(getPlayer().hasControl()) {
				applyVelocity();
				applyAA();
				applyTeleport();
			}
					
			applyFlag();
			applyRegen();
			updateBuffs();
			applyPosition();

		} else {
			applyBodyGravity();
			applyAlive();
		}
	}
	
	protected final void resetExtrapolation() {
		getPlayer().enableExtrapolating();
	}
	protected final void disableExtrapolation() {
		getPlayer().disableExtrapolating();
	}

	protected final void updateBuffs() {
		Iterator<Buff> iterator = getBuffs().iterator();
		while(iterator.hasNext()) {
			Buff buff = iterator.next();
			buff.tick(this, iterator);
		}
		
		convertBuffs();
	}
	
	protected final void convertBuffs() {
		getPlayer().resetBuffs(getBuffs().size());
		for(int i = 0; i < getBuffs().size(); i++) {
			getPlayer().getBuffs()[i] = getBuffs().get(i).getBuff();
		}
	}

	protected final void applyTeleport() {
		if(getPlayer().isTeleporting()) {
			disableExtrapolation();
			applyTeleporting();
		}
	}
	protected final boolean applyTeleporting() {
		
		// Resetting the teleporting state, so the player won't have this on true all the time.
		// Note that we do not use the release packet for teleporting, because we can easily just reset it here.
		getPlayer().disableTeleporting();
		
		// Get the tileset, not the tile itself just tileset to get properties.
		Tileset teleport = GameMatchManager.getCurrentMap().getTileset(0, getPlayer().getPos().getX(), getPlayer().getPos().getY()-getPlayer().getSize().getY()/3);
		
		if(teleport.hasProperty(Tileset.TELEPORT())) { // Are we standing on a teleport?
			// Decoding the coordinates, parsing them, and multiplying them to real game pixel coordinates.
			String[] target = ((String) teleport.getProperty(Tileset.TELEPORT())).split(","); //$NON-NLS-1$
			
			// Setting the coordinates to the player.
			getPlayer().getPos().setX(GameMatchManager.getCurrentMap().toPixelX(Integer.parseInt(target[0])));
			
			// The coordinates in Tiled and the map file are flipped,
			// the (0,0) block tile, is in the top left,
			// but we are going to flip the Y so it will be in the bottom left.
			// We add half of the tile height, so the player won't be stuck in the middle of the block tile.
			getPlayer().getPos().setY((GameMatchManager.getCurrentMap().getHeightPixels() - GameMatchManager.getCurrentMap().toPixelY(Integer.parseInt(target[1]))) + GameMatchManager.getCurrentMap().getTileHeight()/2);
			
			// Returns true, as the player teleported successfully.
			return true;
		}
		// Not the teleport, displays an error and returns false.
		//System.err.println("Not a teleport!");
					
		// Returns false as the player didn't teleport successfully.
		return false;
	}

	protected final void applyFlag() {
		GameMatch match = GameMatchManager.getCurrentMatch();
		String mapName = match.getMapName();
		Flag teamFlag = match.getFlags().getFlag(getPlayer().getTeam());
		Flag enemyFlag = match.getFlags().getFlag(GameMatch.getEnemyTeamID(getPlayer().getTeam()));
		
		if(!teamFlag.getFlag().isTaken() && !teamFlag.inBase(mapName, getPlayer().getTeam()) && collidesFlag(teamFlag)) {
			match.getFlags().setFlag(getPlayer().getTeam(), Flag.getFlag(mapName, getPlayer().getTeam()));
		}
		
		if(collidesFlag(enemyFlag) && !enemyFlag.getFlag().isTaken() && !getPlayer().isHoldingFlag()) {
			enemyFlag.getFlag().gotStolen();
			getPlayer().stoleFlag();
		}
		
		if(getPlayer().isHoldingFlag() && enemyFlag.getFlag().isTaken() && collidesFlagBase(mapName, getPlayer().getTeam()) && teamFlag.inBase(mapName, getPlayer().getTeam())) {
			GameMatchManager.getCurrentMatch().addFlag(getPlayer().getTeam());
			enemyFlag.getFlag().gotDropped();
			getPlayer().droppedFlag();
			match.getFlags().setFlag(GameMatch.getEnemyTeamID(getPlayer().getTeam()), Flag.getFlag(mapName, GameMatch.getEnemyTeamID(getPlayer().getTeam())));
		}
	}
	protected final boolean collidesFlag(Flag flag) {
		return intersects(flag.getBounds());
	}
	protected final boolean collidesFlagBase(String mapName, int team) {
		return collidesFlag(MapManager.getMap(mapName).getFlag(team));
	}

	protected final void applyFlip() {
		if(!getPlayer().isRight() == getPlayer().isLeft()) {
			if(getPlayer().isRight()) {
				getPlayer().flipRight();
			} else {
				getPlayer().flipLeft();
			}
		}
	}
	public final boolean applyGravity() {
		if(!getPlayer().isMidAir()) {
			gravityVelocityReset();
			return false; // Unable to apply gravity, is on ground or collides ground.
		}
		applyGravitation();
		return true; // Able to apply gravity, is mid air.
	}
	protected final void gravityVelocityReset() {
		if(getPlayer().onGround() && getPlayer().isCollidingBot() && getPlayer().getVel().getY() < 0) {
			getPlayer().getVel().resetY();
		}
	}
	protected final void applyWalking(int speed) {
		getPlayer().getVel().resetX(); // We must have it here, no worries, we should not apply walking when the X velocity is modified anyway.
		if((getPlayer().isLeft() != getPlayer().isRight())) {
			if(getPlayer().isRight()) {
				getPlayer().getVel().setX(speed);
				getPlayer().flipRight();
			} else if(getPlayer().isLeft()) {
				getPlayer().getVel().setX(-speed);
				getPlayer().flipLeft();
			}
		}
	}
	
	
	protected final void applyBodyGravity() {
		if(collidesBot()) {
			getPlayer().getVel().resetY();
			alignGround(GameMatchManager.getCurrentMap().getTileHeight());
		} else {
			applyGravitation();
			getPlayer().getPos().addY(getPlayer().getVel().getY());
		}
		
	}

	protected final void applySkill() {
		if(getPlayer().isSkill1()) {
			skill1();
		} else if(getPlayer().isSkill2()) {
			skill2();
		} else if(getPlayer().isSkill3()) {
			skill3();
		} else if(getPlayer().isSkill4()) {
			skill4();
		}	
	}

	protected final void applyAlive() {
		if(getPlayer().isDCD()) {
			getPlayer().subDCD(GameServer.getDelay());
		} else {
			reset();
		}
	}
	
	protected final void reset() {
		resetPlayer();
		resetSpeeds();
		resetBuffs();
	}
	protected final void resetPlayer() {
		setPlayer(new PlayerData(getPlayer().getTeam(), GameMatchManager.getCurrentMap().getBase(getPlayer().getTeam()).getPos(), GameMatchManager.getCurrentMap().getBase(getPlayer().getTeam()).getFlip(), getPlayer().getName(), getPlayer().getHP().getY(), getPlayer().getMana().getY(), getMaxSize(), getMaxSkillCD(), GameMatch.getDefaultRespawn()));
	}

	protected final boolean applyDeath() {
		if(!getPlayer().isDead()) {
			if(getPlayer().hasNoHP()) {
				getPlayer().died();
				if(getPlayer().isHoldingFlag()) {
					getPlayer().droppedFlag();
					GameMatchManager.getCurrentMatch().getFlags().getFlag(GameMatch.getEnemyTeamID(getPlayer().getTeam())).getFlag().gotDropped();
				}
				GameMatchManager.getCurrentMatch().addKill(GameMatch.getEnemyTeamID(getPlayer().getTeam()));
				getPlayer().setDCD(GameMatchManager.getCurrentMatch().getRespawnTime());
				return true;
			}
			return false;
		}
		return true;
		
	}

	protected final void applyVelocity() {
		
		float speed = getSpeed();
		
		if(!getPlayer().isMidAir()) {
			
			getPlayer().getVel().resetY();
			
			if(getPlayer().onGround()) {
				/* JUMP */
				if(getPlayer().isJump() && hasFullControl()) {
					if(!getPlayer().isCollidingTop()) { // Should be here
						getPlayer().getVel().setY(getJumpHeight().getX());
					} else {
						getPlayer().getVel().resetY();
					}
				}
				
				if(getPlayer().isCollidingTop() && getPlayer().isCollidingBot()) {
					getPlayer().getVel().resetY();
					getPlayer().setJump(false);
				}
				/* END JUMP */
			}
			
		} else {
			
			//Jump Cut
			if(isJumpSwitched() && hasFullControl() && !getPlayer().isJump() && getPlayer().getVel().getY() > getJumpHeight().getX()/2) {
				getPlayer().getVel().setY(getJumpHeight().getX()/2);
			}
			resetJumpSwitch();
			
			// Air Momentum
			speed *= getAirForce();
			
			// Gravity
			applyGravitation();
			
		}
		
		//Walking (Velocity X reset inside ofc)
		applyWalking((int)speed);
	}
	
	protected final boolean hasFullControl() {
		return getPlayer().hasControl() && !getPlayer().isSkilling();
	}

	/* Collision Detection */
	protected final void applyCollision() {
		resetCollisions();
		
		collidesMap();

		collidesPlayer();
	}
	
	/* We can use Enum of sides(bot,left,right,top) and pass it as a parameter, thus combine those 4 functions into one. */
	protected final boolean collidesBot() {
		// BOT!
		return GameMatchManager.getCurrentMap().intersectsSurroundXBoth("top", getPlayer().getPos().getX(), getPlayer().getPos().getY()+getBot()+getPlayer().getVel().getY(), getVelocityBounds(false, true)) || getPlayer().getPos().getY() + getPlayer().getVel().getY() + getBot() < GameMatchManager.getCurrentMap().getBotBoundary(); //$NON-NLS-1$
	}
	protected final boolean collidesLeft() {
		// LEFT!
		return GameMatchManager.getCurrentMap().intersectsSurroundY(getPlayer().getPos().getX()+getLeft()+getPlayer().getVel().getX(), getPlayer().getPos().getY(), getVelocityBounds(true, false)) ||getPlayer().getPos().getX() + getPlayer().getVel().getX() + getLeft() < GameMatchManager.getCurrentMap().getLeftBoundary();
	}
	protected final boolean collidesRight() {
		// RIGHT!
		return GameMatchManager.getCurrentMap().intersectsSurroundY(getPlayer().getPos().getX()+getRight()+getPlayer().getVel().getX(), getPlayer().getPos().getY(), getVelocityBounds(true, false)) || getPlayer().getPos().getX() + getPlayer().getVel().getX() + getRight() > GameMatchManager.getCurrentMap().getRightBoundary();
	}
	protected final boolean collidesTop() {
		// TOP!
		return GameMatchManager.getCurrentMap().intersectsSurroundX(getPlayer().getPos().getX(), getPlayer().getPos().getY()+getTop()+getPlayer().getVel().getY(), getVelocityBounds(false, true)) || getPlayer().getPos().getY() + getPlayer().getVel().getY() + getTop() > GameMatchManager.getCurrentMap().getTopBoundary();
	}
	
	protected final void collidesMap() {
		collidesMapY();
		collidesMapX();
	}
	
	protected final void collidesPlayer() {
		PlayerMap players = GameMatchManager.getCurrentMatch().getEnemyTeam(getPlayer().getTeam());
		for(int i = 0; i < players.getPlayers().length; i++) {
			if(!players.getPlayers()[i].getPlayer().isDead()) {
				collidesPlayerY(players.getPlayers()[i]);
				collidesPlayerX(players.getPlayers()[i]);
			}
		}
	}
	
	protected final void resetCollisions() {
		resetCollisionsY();
		resetCollisionsX();
	}
	
	protected final boolean isFacingCollision() {
		return (getPlayer().getFlip().equals("right") && getPlayer().isCollidingRight()) || (getPlayer().getFlip().equals("left") && getPlayer().isCollidingLeft());  //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/* Collision Detection Y */
	protected final void applyCollisionY() {
		resetCollisionsY();
		collidesMapY();
		collidesPlayersY();
	}
	protected final void resetCollisionsY() {
		getPlayer().isCollidingTop(false);
		
		getPlayer().isCollidingBot(false);
		getPlayer().isOnGround(false);
	}
	protected final void collidesMapY() {
		if(collidesTop()) {
			getPlayer().isCollidingTop(true);
		}
		if(collidesBot()) {
			getPlayer().isCollidingBot(true);
			if(getPlayer().getVel().getY() <= 0) {
				getPlayer().isOnGround(true);
				alignGround(GameMatchManager.getCurrentMap().getTileHeight());
			}
		}
	}
	protected final void collidesPlayersY() {
		PlayerMap players = GameMatchManager.getCurrentMatch().getEnemyTeam(getPlayer().getTeam());
		for(int i = 0; i < players.getPlayers().length; i++) {
			if(!players.getPlayers()[i].getPlayer().isDead()) {
				collidesPlayerY(players.getPlayers()[i]);
			}
		}
	}
	protected final void collidesPlayerY(Fighter fighter) {
		if(getVelocityBounds(false, true).intersects(fighter.getBounds())) {
			if(getPlayer().getVel().getY() > 0) {
				getPlayer().isCollidingTop(true);
			} else {
				getPlayer().isCollidingBot(true);
				getPlayer().isOnGround(true);
				alignFighter(fighter);
			}
		}
	}
	
	/* Collision Detection X */
	protected final void applyCollisionX() {
		resetCollisionsX();
		collidesMapX();
		collidesPlayersX();
	}
	protected final void resetCollisionsX() {
		getPlayer().isCollidingLeft(false);
		getPlayer().isCollidingRight(false);
	}
	protected final void collidesMapX() {
		if(collidesLeft()) {
			getPlayer().isCollidingLeft(true);
		}
		
		if(collidesRight()) {
			getPlayer().isCollidingRight(true);
		}
	}
	protected final void collidesPlayersX() {
		PlayerMap players = GameMatchManager.getCurrentMatch().getEnemyTeam(getPlayer().getTeam());
		for(int i = 0; i < players.getPlayers().length; i++) {
			if(!players.getPlayers()[i].getPlayer().isDead()) {
				collidesPlayerX(players.getPlayers()[i]);
			}
		}
	}
	protected final void collidesPlayerX(Fighter fighter) {
		if(getVelocityBounds(true, false).intersects(fighter.getBounds())) {
			if(getPlayer().getVel().getX() > 0) {
				getPlayer().isCollidingRight(true);
			} else {
				getPlayer().isCollidingLeft(true);
			}
		}
	}
	
	// Boundary Methods - MUST NOT BE CHANGED!!!!!
	protected final float getLeft() {
		return -getPlayer().getSize().getX()/2;
	}
	protected final float getRight() {
		return getPlayer().getSize().getX()/2;
	}
	protected final float getTop() {
		return getPlayer().getSize().getY()/2;
	}
	protected final float getBot() {
		return -getPlayer().getSize().getY()/2;
	}
	
	protected final Rectangle getVelocityBounds(boolean velx, boolean vely) {
		Rectangle bounds = getBounds();
		bounds.x += velx ? getPlayer().getVel().getX() : 0;
		
		/* If vely is true, then we either add the velocityY to the Y coordinate of the bounds, otherwise if velocityY equals zero we add -1
		 * The reason we add -1, is simply becasue gravity has a constant force downwards, so that -1 represents that gravity, otherwise we would experience gravity and collision problems.
		 * For example, if we would remove that -1, then when the player will collide bot, and we will RESET the velocityY it will not collide bot, then it will collide bot again until we reset, and it will continue every tick.
		 * That's why we add -1 if vely is true and velocityY is empty. */
		bounds.y += vely ? getPlayer().getVel().getY() != 0 ? getPlayer().getVel().getY() : -1 : 0;
		
		return bounds;
	}
	
	protected final Tile getCellOn(GameMap map) {
		return map.getTile(0, getPlayer().getPos().getX(), getPlayer().getPos().getY() + getBot());
	}
	
	protected final Rectangle getBounds() {
		CollisionDetection.setBounds(this.bounds, "both", getPlayer().getPos().getX(), getPlayer().getPos().getY(), getPlayer().getSize().getX(), getPlayer().getSize().getY()); //$NON-NLS-1$
		return this.bounds;
	}
	public final boolean intersects(Rectangle rect) {
		return getBounds().intersects(rect);
	}
	public final boolean intersects(Fighter player) {
		return getBounds().intersects(player.getBounds());
	}
	
	protected final void applyAA() {
		if(getPlayer().isAAttack() && getPlayer().canAAttack()) {
			if(getAA_CD().getX() <= 0) {
				
				AAttack();
				
				resetAA_CD();
			} else {
				getAA_CD().subX(GameServer.getDelay());
			}
		} else {
			resetAA_CD();
		}
		
	}
	
	protected final void applyPosition() {
		
		// Apply velocity to position
		
		// Check collision Y
		applyCollisionY();
		
		if(getPlayer().movingY() && ((getPlayer().getVel().getY() > 0 && !getPlayer().isCollidingTop()) 
				|| (getPlayer().getVel().getY() < 0 && !getPlayer().isCollidingBot()))) {
			getPlayer().getPos().addY(getPlayer().getVel().getY());
		}
		
		
		// Check collision X
		applyCollisionX();
		
		if(getPlayer().movingX() && ((getPlayer().getVel().getX() > 0 && !getPlayer().isCollidingRight())
				|| (getPlayer().getVel().getX() < 0 && !getPlayer().isCollidingLeft()))) {
			getPlayer().getPos().addX(getPlayer().getVel().getX());
		}
	}
	
	protected final void applyRegen() {
		healMana(getManaRegen());
	}
	
	protected final void applyGravitation() {
		getPlayer().getVel().setY(getPlayer().getVel().getY() - getFallingMomentum() < -getGravityForce() ? -getGravityForce() : getPlayer().getVel().getY() - getFallingMomentum());	
	}
	protected final void alignGround(int ground) {
		getPlayer().getPos().setY((int)(getPlayer().getPos().getY() / ground) * ground + getPlayer().getSize().getY()/2 - 1);
	}
	protected final void alignFighter(Fighter fighter) {
		getPlayer().getPos().setY(fighter.getPlayer().getPos().getY() + fighter.getPlayer().getSize().getY());
	}
	
	// Buffs
	public final void applyBuffs(Buff[] buffs) {
		if(getPlayer().isVulnerable()) {
			for(int i = 0; i < buffs.length; i++) {
				// Add Buffs
				getBuffs().add(buffs[i].getNewBuff());
				
				// We need to get a new buff(), because buffs are passed by ref by value,
				// that's how are objects are treated in Java and we don't want
				// players to refer to the same buff object.
				
				// Start/Initiate the Buff - NOT HERE! We will do so in the updateBuffs(), because we don't want order to be relevant, IMPORTANT!
				//getBuffs().get(getBuffs().size()-1).start(this, getPlayer().getBuffs().length);
			}
		}
	}
	
	// HP
	protected final void applyHP(float hp) {
		if(hp > 0) { 
			healHP(hp);
			return;
		} else if(hp < 0) {
			dealHP(hp);
			return;
		}
	}
	public final void applyRandomHP(float hp) {
		applyHP(hp - MathUtil.nextInt(0, Math.abs((int)(hp*0.1))) + MathUtil.nextInt(0, Math.abs((int)(hp*0.1))));
	}
	protected final void dealHP(float dmg) {
		if(!getPlayer().isDead() && getPlayer().hasHP() && getPlayer().isVulnerable()) {
			float newhp = getPlayer().getHP().getX() + dmg;
			getPlayer().getHP().setX(newhp < 0 ? 0 : newhp); // the dmg itself is negative.
		}
	}
	protected final void healHP(float heal) {
		if(!getPlayer().isDead()) {
			float newhp = getPlayer().getHP().getX() + heal;
			getPlayer().getHP().setX(newhp > getPlayer().getHP().getY() ?
					getPlayer().getHP().getY() : newhp);
		}
	}
	
	// MANA
	protected final void applyMana(float mana) {
		if(mana > 0) { 
			healMana(mana);
			return;
		} else if(mana < 0) {
			dealMana(mana);
			return;
		}
	}
	protected final void applyRandomMana(float mana) {
		applyMana(mana - MathUtil.nextFloat(0, (int)(mana*0.1) + MathUtil.nextFloat(0, (mana))));
	}
	protected final void dealMana(float dmg) {
		if(!getPlayer().isDead() && getPlayer().hasMana() && getPlayer().isVulnerable()) {
			float newMana = getPlayer().getMana().getX() + dmg;
			getPlayer().getMana().setX((newMana < 0 ? 0 : newMana)); // the dmg itself is negative.
		}
	}
	protected final void healMana(float heal) {
		if(!getPlayer().isDead()) {
			float newMana = getPlayer().getMana().getX() + heal;
			getPlayer().getMana().setX(newMana > getPlayer().getMana().getY() ?
					getPlayer().getMana().getY() : newMana);
		}
	}
	
	// SPEED CONVERTION
	protected final float convertSpeed(float speed) {
		return getPlayer().getFlip().equals("right") ? speed : -speed;  //$NON-NLS-1$
	}
	
	protected void defaultUpdate() {
		getPlayer().getVel().resetX();
		applyGravity();
	}
	
	public void startSkill1() {
		if(!applySkillMana(0)) {
			endSkill1();
		}
	}
	protected void updateSkill1() {
		defaultUpdate();
	}
	protected void skill1() {
		
	}
	protected void endSkill1() {
		
	}

	public void startSkill2() {
		if(!applySkillMana(1)) {
			endSkill2();
		}
	}
	protected void updateSkill2() {
		defaultUpdate();
	}
	protected void skill2() {
		
	}
	protected void endSkill2() {
		
	}

	public void startSkill3() {
		if(!applySkillMana(2)) {
			endSkill3();
		}
	}
	protected void updateSkill3() {
		defaultUpdate();
	}
	protected void skill3() {
		
	}
	protected void endSkill3() {
		
	}
	
	public void startSkill4() {
		if(!applySkillMana(3)) {
			endSkill4();
		}
	}
	protected void updateSkill4() {
		defaultUpdate();
	}
	protected void skill4() {
		
	}
	protected void endSkill4() {
		
	}
	
	
	protected final float getSpeed() {
		return getPlayer().isRunning() ? getRunningSpeed().getX() : getWalkingSpeed().getX();
	}
	
	public final void resetSpeeds() {
		resetWalkingSpeed();
		resetRunningSpeed();
		resetJumpHeight();
	}
	
	protected final void resetWalkingSpeed() {
		getWalkingSpeed().YtoX();
	}
	protected final void resetRunningSpeed() {
		getRunningSpeed().YtoX();
	}
	protected final void resetJumpHeight() {
		getJumpHeight().YtoX();
	}
	
	protected final void AAttack() {
		AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds(getPlayer().getFlip(), getPlayer().getPos().getX()-convertSpeed(getPlayer().getSize().getX()/2), getPlayer().getPos().getY(), getAA_Range().getX(), getAA_Range().getY()), -getAA_Dmg());
	}
	
	protected final boolean applySkillMana(int index) {
		if(getPlayer().getMana().getX() >= skillMana[index]) {
			applyMana(-skillMana[index]);
			return true;
		}
		return false;
	}
}
