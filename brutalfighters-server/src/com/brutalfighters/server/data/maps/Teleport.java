package com.brutalfighters.server.data.maps;

import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.tiled.Tileset;

public class Teleport {
	
	public static boolean applyTeleporting(GameMap map, PlayerData p) {
		
		// Resetting the teleporting state, so the player won't have this on true all the time.
		// Note that we do not use the release packet for teleporting, because we can easily just reset it here.
		p.disableTeleporting();
		
		// Get the tileset, not the tile itself just tileset to get properties.
		Tileset teleport = map.getTileset(0, p.getPos().getX(), p.getPos().getY()-p.getSize().getY()/3);
		
		if(teleport.hasProperty(Tileset.TELEPORT())) { // Are we standing on a teleport?
			// Decoding the coordinates, parsing them, and multiplying them to real game pixel coordinates.
			String[] target = ((String) teleport.getProperty(Tileset.TELEPORT())).split(","); //$NON-NLS-1$
			
			// Setting the coordinates to the player.
			p.getPos().setX(map.toPixelX(Integer.parseInt(target[0])));
			
			// The coordinates in Tiled and the map file are flipped,
			// the (0,0) block tile, is in the top left,
			// but we are going to flip the Y so it will be in the bottom left.
			// We add half of the tile height, so the player won't be stuck in the middle of the block tile.
			p.getPos().setY((map.getHeightPixels() - map.toPixelY(Integer.parseInt(target[1]))) + map.getTileHeight()/2);
			
			// Returns true, as the player teleported successfully.
			return true;
		}
		// Not the teleport, displays an error and returns false.
		//System.err.println("Not a teleport!");
					
		// Returns false as the player didn't teleport successfully.
		return false;
	}
}
