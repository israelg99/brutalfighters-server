package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.data.maps.Base;
import com.esotericsoftware.kryonet.Connection;


public enum FighterFactory {
	Blaze {
		@Override
		public Fighter getNew(Connection connection, Base base, String m_id) {
			return new Blaze(connection, base, m_id);
		}
		
	},
	
	Dusk {
		@Override
		public Fighter getNew(Connection connection, Base base, String m_id) {
			return new Dusk(connection, base, m_id);
		}
		
	},
	
	Chip {
		@Override
		public Fighter getNew(Connection connection, Base base, String m_id) {
			return new Chip(connection, base, m_id);
		}
		
	},
	
	Surge {
		@Override
		public Fighter getNew(Connection connection, Base base, String m_id) {
			return new Surge(connection, base, m_id);
		}
		
	},
	
	Lust {
		@Override
		public Fighter getNew(Connection connection, Base base, String m_id) {
			return new Lust(connection, base, m_id);
		}
	};
	
	public abstract Fighter getNew(Connection connection, Base base, String m_id);
	
	public static boolean contains(String fighter) {
	    for (FighterFactory c : FighterFactory.values()) {
	        if (c.name().equals(fighter)) {
	            return true;
	        }
	    }

	    return false;
	}
}
