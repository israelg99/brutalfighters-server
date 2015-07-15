package com.brutalfighters.server.tiled;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class TMXMapLoader {
	private static SAXParserFactory factory;
	private static SAXParser saxParser;
	private static TMXHandler tmxhandler;
	
	public static void Load() {
		try {
			factory = SAXParserFactory.newInstance();
			tmxhandler = new TMXHandler();
			saxParser = factory.newSAXParser();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static TiledMap readMap(String path) {
		try {
			saxParser.parse(new File(path), tmxhandler);
			return tmxhandler.getMap();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Couldn't read the map"); //$NON-NLS-1$
		return null;
	}
}
