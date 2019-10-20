package com.ducksofts.ducky.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PFont {

	public static class PCharacterData {
		public char id;
		public int x, y, w, h, xadv, xoff, yoff;
	}
	
	public final List<PCharacterData> characters = new ArrayList<PCharacterData>();
	public PImage font;
	
	public PFont(String fnt, String image) {
		InputStream is = PFont.class.getResourceAsStream(fnt);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			String line;
			while((line = br.readLine()) != null) {
				String[] words = line.split("\\s+");
				if(!words[0].equals("char"))
					continue;
				PCharacterData data = new PCharacterData();
				data.id = (char)Integer.parseInt(words[1].substring(3, words[1].length()));
				data.x = Integer.parseInt(words[2].substring(2, words[2].length()));
				data.y = Integer.parseInt(words[3].substring(2, words[3].length()));
				data.w = Integer.parseInt(words[4].substring(6, words[4].length()));
				data.h = Integer.parseInt(words[5].substring(7, words[5].length()));
				data.xoff = Integer.parseInt(words[6].substring(8, words[6].length()));
				data.yoff = Integer.parseInt(words[7].substring(8, words[7].length()));
				data.xadv = Integer.parseInt(words[8].substring(9, words[8].length()));
				characters.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		font = new PImage(image);
	}
	
}