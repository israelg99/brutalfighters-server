package com.brutalfighters.server.data.players;

import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.players.fighters.Blaze;
import com.brutalfighters.server.data.players.fighters.Chip;
import com.brutalfighters.server.data.players.fighters.Dusk;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.data.players.fighters.Lust;
import com.brutalfighters.server.data.players.fighters.Surge;


public enum Champion {
	Blaze {
		@Override
		public Fighter getNew(Base base, String m_id) {
			return new Blaze(base, m_id);
		}
		
	},
	
	Dusk {
		@Override
		public Fighter getNew(Base base, String m_id) {
			return new Dusk(base, m_id);
		}
		
	},
	
	Chip {
		@Override
		public Fighter getNew(Base base, String m_id) {
			return new Chip(base, m_id);
		}
		
	},
	
	Surge {
		@Override
		public Fighter getNew(Base base, String m_id) {
			return new Surge(base, m_id);
		}
		
	},
	
	Lust {
		@Override
		public Fighter getNew(Base base, String m_id) {
			return new Lust(base, m_id);
		}
	};
	
	public abstract Fighter getNew(Base base, String m_id);
	
	public static boolean contains(String fighter) {
	    for (Champion c : Champion.values()) {
	        if (c.name().equals(fighter)) {
	            return true;
	        }
	    }

	    return false;
	}
}
