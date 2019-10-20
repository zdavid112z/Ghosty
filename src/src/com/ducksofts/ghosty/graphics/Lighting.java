package com.ducksofts.ghosty.graphics;

import java.util.ArrayList;
import java.util.List;

public class Lighting {

	public float ambient = 0.03f;
	protected int[] mPixels;
	protected int mWidth, mHeight;
	protected int mTileX, mTileY;
	protected List<LightData> mLights = new ArrayList<LightData>();
	
	public Lighting(int[] pixels, int w, int h, int tx, int ty) {
		mPixels = pixels;
		mWidth = w;
		mHeight = h;
		mTileX = tx;
		mTileY = ty;
	}
	
	public void add(LightData l) {
		mLights.add(l);
	}
	
	public void remove(LightData l) {
		mLights.remove(l);
	}
	
	public void render(int ox, int oy) {
		for(int y = 0; y < mHeight; y += mTileY) {
			for(int x = 0; x < mWidth; x += mTileX) {
				float cr = 0, cg = 0, cb = 0;
				for(int i = 0; i < mLights.size(); i++) {
					int dx = x - mLights.get(i).positionX - ox;
					int dy = y - mLights.get(i).positionY - oy;
					double dist = Math.sqrt(dx * dx + dy * dy);
					dist /= (float)mLights.get(i).radius;
					if(dist >= 1)
						continue;
					else {
						double f = 1.0 - dist;
						cr += mLights.get(i).colorR * f;
						cg += mLights.get(i).colorG * f;
						cb += mLights.get(i).colorB * f;
					}
				}
				for(int yy = 0; yy < mTileY; yy++) {
					for(int xx = 0; xx < mTileX; xx++) {						
						int col = mPixels[x + xx + (y + yy) * mWidth];
						if(cb > 1)
							cb = 1;
						if(cg > 1)
							cg = 1;
						if(cr > 1)
							cr = 1;
						if(cb < ambient)
							cb = ambient;
						if(cr < ambient)
							cr = ambient;
						if(cg < ambient)
							cg = ambient;
						short b = (short) ((float) (col & 0xff) * cb);
						short g = (short) ((float) ((col & 0xff00) >> 8) * cg);
						short r = (short) ((float) ((col & 0xff0000) >> 16) * cr);
						mPixels[(x + xx) + (y + yy) * mWidth] = r << 16 | g << 8 | b;
					}
				}
			}
		}
	}
	
}
