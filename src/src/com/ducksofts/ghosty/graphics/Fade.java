package com.ducksofts.ghosty.graphics;

import com.ducksofts.ducky.graphics.GameRenderer;

public class Fade {

	private GameRenderer mRenderer;
	
	public Fade(GameRenderer renderer) {
		mRenderer = renderer;
	}
	
	public void fade(int color, float alpha) {
		int s = mRenderer.getWidth() * mRenderer.getHeight();
		for(int i = 0; i < s; i++) {
			int src = mRenderer.getPixels()[i];
			short r = (short) ((color & 0xff) * alpha + (src & 0xff) * (1.0f - alpha));
			short g = (short) ((((color & 0xff00) >> 8) & 0xff) * alpha + (((src & 0xff00) >> 8) & 0xff) * (1.0f - alpha));
			short b = (short) ((((color & 0xff0000) >> 16) & 0xff) * alpha + (((src & 0xff0000) >> 16) & 0xff) * (1.0f - alpha));
			mRenderer.getPixels()[i] = r | (g << 8) | (b << 16);
		}
	}
	
}
