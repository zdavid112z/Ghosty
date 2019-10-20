package com.ducksofts.ghosty.levels;

import java.util.List;

import com.ducksofts.ducky.gamework.CollisionBox;
import com.ducksofts.ducky.gamework.Level;
import com.ducksofts.ducky.gamework.LevelManager;
import com.ducksofts.ducky.graphics.GameRenderer;
import com.ducksofts.ducky.graphics.PImage;
import com.ducksofts.ghosty.DataFile;
import com.ducksofts.ghosty.DataFile.Variable;
import com.ducksofts.ghosty.entity.Fireplace;
import com.ducksofts.ghosty.entity.Furniture;
import com.ducksofts.ghosty.entity.Music;
import com.ducksofts.ghosty.entity.NPC;
import com.ducksofts.ghosty.entity.Player;
import com.ducksofts.ghosty.entity.PlayerUI;
import com.ducksofts.ghosty.graphics.Fade;
import com.ducksofts.ghosty.graphics.LightData;
import com.ducksofts.ghosty.graphics.Lighting;

public class DefaultLevel extends Level {
	
	public Fade mFade;
	public Lighting mLighting;
	protected int mOffsetX = 0, mOffsetY = 0, mWidth, mHeight;
	protected GameRenderer mRenderer;
	protected byte[] mMap;
	protected Player mPlayer;
	protected PlayerUI mPlayerUI;
	public boolean ending = false;
	
	public DefaultLevel(LevelManager lm, PImage map, PImage gmap, GameRenderer renderer) {
		super(lm);
		mRenderer = renderer;
		mWidth = map.getWidth();
		mHeight = map.getHeight();
		mLighting = new Lighting(renderer.getPixels(), renderer.getWidth(), renderer.getHeight(), 4, 4);
		mFade = new Fade(renderer);
		mMap = new byte[map.getWidth() * map.getHeight()];
		for(int j = 0; j < map.getHeight(); j++) {
			for(int i = 0; i < map.getWidth(); i++) {
				for(int k = 0; k < Tile.tiles.size(); k++) {
					if(Tile.tiles.get(k).mCode == (map.pixels[i + j * map.getWidth()] & 0xffffff))
						mMap[i + j * map.getWidth()] = (byte) k;
				}
			}
		}
		mPlayer = new Player(this, mRenderer.getWidth(), mRenderer.getHeight(), mEntities);
		mPlayerUI = new PlayerUI(this, mPlayer, gmap);
	}
	
	public int getOffsetX() {
		return mOffsetX;
	}

	public int getOffsetY() {
		return mOffsetY;
	}
	
	public int getWindowWidth() {
		return mRenderer.getWidth();
	}
	
	public int getWindowHeight() {
		return mRenderer.getHeight();
	}
	
	public int getLevelWidth() {
		return mWidth;
	}
	
	public int getLevelHeight() {
		return mHeight;
	}
	
	public void customize(PImage entityMap) {
		for(int y = 0; y < entityMap.getHeight(); y++) {
			for(int x = 0; x < entityMap.getWidth(); x++) {
				switch(entityMap.pixels[x + y * entityMap.getWidth()] & 0xffffff) {
				case 0xFF0C00:
					mPlayer.positionX = x * Tile.tileSize + 32;
					mPlayer.positionY = y * Tile.tileSize + 32;
					break;
				case 0x00FF48:
					NPC npc = new NPC(this);
					npc.positionX = x * Tile.tileSize + 32;
					npc.positionY = y * Tile.tileSize + 32;
					break;
				case 0xFFFA00:
					Furniture f = new Furniture(this, null, Furniture.Lamp, mLighting, mPlayer);
					f.positionX = x * Tile.tileSize + 24;
					f.positionY = y * Tile.tileSize + 40;
					break;
				case 0xFF9000:
					Furniture f1 = new Furniture(this, null, Furniture.LightCupboard, mLighting, mPlayer);
					f1.positionX = x * Tile.tileSize + 32;
					f1.positionY = y * Tile.tileSize - 8;
					break;
				case 0xFF6A00:
					Furniture f2 = new Furniture(this, Furniture.Cupboard, null, mLighting, mPlayer);
					f2.positionX = x * Tile.tileSize + 32;
					f2.positionY = y * Tile.tileSize - 16;
					break;
				case 0xFFD666:
					Furniture f3 = new Furniture(this, Furniture.Chair, null, mLighting, mPlayer);
					f3.positionX = x * Tile.tileSize + 32;
					f3.positionY = y * Tile.tileSize;
					break;
				case 0xFFF1AD:
					Furniture f4 = new Furniture(this, Furniture.Couch, null, mLighting, mPlayer);
					f4.positionX = x * Tile.tileSize + 64;
					f4.positionY = y * Tile.tileSize;
					break;
				case 0xFF8C72:
					Fireplace fp = new Fireplace(this, mLighting, mPlayer);
					fp.positionX = x * Tile.tileSize + 32;
					fp.positionY = y * Tile.tileSize + 32;
					break;
				default:
					break;
				}
			}
		}
	}
	
	public void customize(String path) {
		List<Variable> list = DataFile.loadFile(path);
		for(Variable v : list) {
			if(v.name.equals("colBox")) {
				String[] d = v.var.split(",");
				CollisionBox cb = new CollisionBox(this, Integer.parseInt(d[2]), Integer.parseInt(d[3]));
				cb.positionX = Integer.parseInt(d[0]);
				cb.positionY = Integer.parseInt(d[1]);
			}
			else if(v.name.equals("colBoxt")) {
				String[] d = v.var.split(",");
				int cx = Integer.parseInt(d[0]) * Tile.tileSize;
				int cy = Integer.parseInt(d[1]) * Tile.tileSize;
				int sx = Integer.parseInt(d[2]) * Tile.tileSize;
				int sy = Integer.parseInt(d[3]) * Tile.tileSize;
				CollisionBox cb = new CollisionBox(this, sx / 2, sy / 2);
				cb.positionX = cx + sx / 2;
				cb.positionY = cy + sy / 2;
			}
			else if(v.name.equals("player")) {
				String[] d = v.var.split(",");
				mPlayer.positionX = Integer.parseInt(d[0]);
				mPlayer.positionY = Integer.parseInt(d[1]);
			}
			else if(v.name.equals("npc")) {
				String[] d = v.var.split(",");
				NPC npc = new NPC(this);
				npc.positionX = Integer.parseInt(d[0]);
				npc.positionY = Integer.parseInt(d[1]);
			}
			else if(v.name.equals("light")) {
				String[] d = v.var.split(",");
				LightData ld = new LightData();
				ld.positionX = Integer.parseInt(d[0]);
				ld.positionY = Integer.parseInt(d[1]);
				ld.radius = Integer.parseInt(d[2]);
				ld.colorR = Float.parseFloat(d[3]);
				ld.colorG = Float.parseFloat(d[4]);
				ld.colorB = Float.parseFloat(d[5]);
				mLighting.add(ld);
			}
			else if(v.name.equals("music")) {
				if(v.var.equals("true")) {
					new Music(this, mPlayer);
				}
			}
			else if(v.name.equals("furniture")) {
				String[] d = v.var.split(",");
				Furniture f = null;
				if(d[0].equals("single")) {
					PImage image = null;
					if(d[1].equals("chair")) 
						image = Furniture.Chair;
					else if(d[1].equals("couch")) 
						image = Furniture.Couch;
					else if(d[1].equals("cupboard"))
						image = Furniture.Cupboard;
					f = new Furniture(this, image, null, mLighting, mPlayer);
				} else if (d[0].equals("multiple")){
					PImage[] images = null;
					if(d[1].equals("cupboard")) 
						images = Furniture.LightCupboard;
					else if(d[1].equals("lamp")) 
						images = Furniture.Lamp;
					f = new Furniture(this, null, images, mLighting, mPlayer);
				} else if(d[0].equals("fireplace")) {
					f = new Fireplace(this, mLighting, mPlayer);
				}
				f.positionX = Integer.parseInt(d[2]);
				f.positionY = Integer.parseInt(d[3]);
			}
		}
	}
	
	public void update() {
		super.update();
	}
	
	
	public void render(GameRenderer renderer) { 
		renderer.offsetX = mPlayer.getOffsetX();
		renderer.offsetY = mPlayer.getOffsetY();
		super.render(renderer);
		for(int j = 0; j < mHeight; j++) {
			for(int i = 0; i < mWidth; i++) {
				Tile.tiles.get(mMap[i + j * mWidth]).renderAt(renderer, i * Tile.tileSize, j * Tile.tileSize, 126);
			}
		}
		renderer.renderAll();
		renderer.clearQueue();
		mLighting.render(renderer.offsetX, renderer.offsetY);
		mPlayerUI.renderNow(renderer);
		//renderer.renderAll();
		renderer.clearQueue();
	}
	
}
