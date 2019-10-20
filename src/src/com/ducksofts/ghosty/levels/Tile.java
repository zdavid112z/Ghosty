package com.ducksofts.ghosty.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ducksofts.ducky.graphics.GameRenderer;
import com.ducksofts.ducky.graphics.PImage;

public class Tile {

	public static class PlankTile extends Tile {

		private Random mRandom;
		private PImage[] mPlanks;
		
		public PlankTile(int code) {
			super(null, code);
			mPlanks = new PImage[20];
			mPlanks[0] = new PImage("/level/planks/planks.png");
			mPlanks[1] = mPlanks[0];
			mPlanks[2] = mPlanks[0];
			mPlanks[3] = mPlanks[0];
			mPlanks[5] = mPlanks[0];
			mPlanks[6] = new PImage("/level/planks/planks1.png");
			mPlanks[14] = mPlanks[6];
			mPlanks[7] = mPlanks[0];
			//mPlanks[7] = new PImage("/level/planks/planks1.png");
			mPlanks[8] = new PImage("/level/planks/planks3.png");
			mPlanks[9] = mPlanks[8];
			mPlanks[10] = mPlanks[8];
			mPlanks[11] = new PImage("/level/planks/planks4.png");
			mPlanks[12] = mPlanks[11];
			mPlanks[13] = mPlanks[11];
			mPlanks[4] = new PImage("/level/planks/planks5.png");
			mPlanks[15] = new PImage("/level/planks/planks6.png");
			mPlanks[16] = new PImage("/level/planks/planks7.png");
			mPlanks[17] = new PImage("/level/planks/planks8.png");
			mPlanks[18] = new PImage("/level/planks/planks9.png");
			mPlanks[19] = new PImage("/level/planks/planks10.png");
			mRandom = new Random();
		}
		
		public void renderAt(GameRenderer r, int x, int y, int layer) {
			mRandom.setSeed(x * 60 + y * 15 - 20);
			r.render(mPlanks[mRandom.nextInt(mPlanks.length)], x, y, layer, 1, true);
		}
		
	}
	
	public static final int tileSize = 64;
	public static List<Tile> tiles;
	
	static {
		tiles = new ArrayList<Tile>();
		tiles.add(new Tile(new PImage("/level/planks/black.png"), 0));
		//tiles.add(new Tile(new PImage("/level/planks/planks.png"), 0xC46800));
		tiles.add(new PlankTile(0xC46800));
		tiles.add(new Tile(new PImage("/level/planks/woodTop.png"), 0xDB7100));
		tiles.add(new Tile(new PImage("/level/planks/woodBottom.png"), 0x7A5135));
		tiles.add(new Tile(new PImage("/level/planks/woodLeft.png"), 0xFF6600));
		tiles.add(new Tile(new PImage("/level/planks/woodRight.png"), 0xFFA970));
		tiles.add(new Tile(new PImage("/level/planks/woodcotr.png"), 0xE56A19));
		tiles.add(new Tile(new PImage("/level/planks/woodcotl.png"), 0xE5AB85));
		tiles.add(new Tile(new PImage("/level/planks/woodcobr.png"), 0xE58A4E));
		tiles.add(new Tile(new PImage("/level/planks/woodcobl.png"), 0xAF693A));
		tiles.add(new Tile(new PImage("/level/planks/woodcitr.png"), 0xAF691D));
		tiles.add(new Tile(new PImage("/level/planks/woodcitl.png"), 0xAF7638));
		tiles.add(new Tile(new PImage("/level/planks/woodcibr.png"), 0x825729));
		tiles.add(new Tile(new PImage("/level/planks/woodcibl.png"), 0xDB9346));
	}
	
	public static Tile findByCode(int code) {
		for(Tile t : tiles) {
			if(t.mCode == code)
				return t;
		}
		return null;
	}
	
	protected int mCode;
	protected PImage mImage;
	
	public Tile(PImage image, int code) {
		mImage = image;
		mCode = code;
		tiles.add(this);
	}
	
	public void update() {
		
	}
	
	public void renderAt(GameRenderer r, int x, int y, int layer) {
		r.render(mImage, x, y, layer, 1, true);
	}
	
}
