package com.brutalfighters.server.tiled;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TMXHandler extends DefaultHandler {
	private TiledMap map;
	private int tilesetID;
	
	@Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
		if(qName.equalsIgnoreCase("map")) { // Creates the map //$NON-NLS-1$
			map = new TiledMap(Integer.parseInt(attributes.getValue("width")) - 1, Integer.parseInt(attributes.getValue("height")) - 1, //$NON-NLS-1$ //$NON-NLS-2$
								Integer.parseInt(attributes.getValue("tilewidth")), Integer.parseInt(attributes.getValue("tileheight"))); //$NON-NLS-1$ //$NON-NLS-2$
			map.addTileset(0); // Adding the initial tileset, must have, because all `gid`'s start with 1, 0 being the initial tileset.
		} else if(qName.equalsIgnoreCase("stile")) { // Adds tileset, will add properties later //$NON-NLS-1$
			tilesetID = Integer.parseInt(attributes.getValue("id")) + 1; //$NON-NLS-1$
			map.addTileset(tilesetID);
		} else if(qName.equalsIgnoreCase("property")) { // Adds property to the last tileset, because the latest is the current. //$NON-NLS-1$
			map.editTileset(tilesetID, attributes.getValue("name"), attributes.getValue("value")); //$NON-NLS-1$ //$NON-NLS-2$
		} else if(qName.equalsIgnoreCase("layer")) { // Adds layer to the map, will add tiles later //$NON-NLS-1$
			map.addTiledLayer(Integer.parseInt(attributes.getValue("width")), Integer.parseInt(attributes.getValue("height"))); //$NON-NLS-1$ //$NON-NLS-2$
		} else if(qName.equalsIgnoreCase("tile")) { // Adds tiles into the map, into the last layer, because the latest is the current. //$NON-NLS-1$
			map.addTile(map.getTiledLayersLength() - 1, Integer.parseInt(attributes.getValue("gid"))); //$NON-NLS-1$
		}
	}
	
	@Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
		// Nothing here for now
	}
	
	@Override
    public void characters(char ch[], int start, int length) throws SAXException {
		// Nothing here for now
	}
	
	public TiledMap getMap() {
		return map;
	}
}
