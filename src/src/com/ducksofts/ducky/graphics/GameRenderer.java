package com.ducksofts.ducky.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameRenderer {
	
	public static enum RenderCallType {
		RENDER_CALL_IMAGE,
		RENDER_CALL_FONT,
		RENDER_CALL_DYNAMIC_IMAGE
	}
	
	public static class RenderCall {
		public RenderCallType type;
		public PImage image;
		public PFont font;
		public String text;
		public int[] org;
		public int[] other;
		public int depth, x, y, color;
		public float transparency;
		public boolean applyOffset;
	}
	
	private int mWidth;
	private int mHeight;
	public int offsetX = 0, offsetY = 0;
	private BufferedImage mImage;
	private int[] mPixels;
	private byte[] mDepthBuffer;
	private boolean mBlending = false;
	private List<RenderCall> mRenderQueue = new ArrayList<RenderCall>();
	
	public GameRenderer(int w, int h) {
		mWidth = w;
		mHeight = h;
		mImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		mPixels = ((DataBufferInt)(mImage.getRaster().getDataBuffer())).getData();
		mDepthBuffer = new byte[w * h];
	}
	
	public int getWidth() {
		return mWidth;
	}
	
	public int getHeight() {
		return mHeight;
	}
	
	public int[] getPixels() {
		return mPixels;
	}
	
	public BufferedImage getImage() {
		return mImage;
	}
	
	public void clearAll() {
		clearScreen();
		clearQueue();
	}
	
	public void clearQueue() {		
		mRenderQueue.clear();
	}
	
	public void clearScreen() {
		for(int i = 0; i < mPixels.length; i++) {
			mPixels[i] = 0;
			mDepthBuffer[i] = 127;
		}
	}
	
	public void putPixel(int color, int x, int y) {
		mPixels[x + y * mWidth] = color;
	}
	
	public void blendPixel(int color, int x, int y, float alpha) {
		int src = mPixels[x + y * mWidth];
		//float alpha = (float) ((byte)((color & 0xff000000) >> 24) & 0xff) / 255.0f;
		//alpha *= transparency;
		short r = (short) ((color & 0xff) * alpha + (src & 0xff) * (1.0f - alpha));
		short g = (short) ((((color & 0xff00) >> 8) & 0xff) * alpha + (((src & 0xff00) >> 8) & 0xff) * (1.0f - alpha));
		short b = (short) ((((color & 0xff0000) >> 16) & 0xff) * alpha + (((src & 0xff0000) >> 16) & 0xff) * (1.0f - alpha));
		mPixels[x + y * mWidth] = r | (g << 8) | (b << 16);
	}
	
	public RenderCall render(PImage image, int[] orgCol, int[] otherCol, int x, int y, int depth, float transparency, boolean applyOffset) {
		RenderCall rc = new RenderCall();
		rc.type = RenderCallType.RENDER_CALL_DYNAMIC_IMAGE;
		rc.org = orgCol;
		rc.other = otherCol;
		rc.image = image;
		rc.x = x;
		rc.y = y;
		rc.depth = depth;
		rc.transparency = transparency;
		rc.applyOffset = applyOffset;
		mRenderQueue.add(rc);
		return rc;
	}
	
	public RenderCall render(PImage image, int x, int y, int depth, float transparency, boolean applyOffset) {
		RenderCall rc = new RenderCall();
		rc.type = RenderCallType.RENDER_CALL_IMAGE;
		rc.image = image;
		rc.x = x;
		rc.y = y;
		rc.depth = depth;
		rc.transparency = transparency;
		rc.applyOffset = applyOffset;
		mRenderQueue.add(rc);
		return rc;
	}
	
	public RenderCall render(PFont font, int x, int y, String text, int color, int depth, float transparency, boolean applyOffset) {
		RenderCall rc = new RenderCall();
		rc.type = RenderCallType.RENDER_CALL_FONT;
		rc.font = font;
		rc.x = x;
		rc.y = y;
		rc.text = text;
		rc.color = color;
		rc.depth = depth;
		rc.transparency = transparency;
		rc.applyOffset = applyOffset;
		mRenderQueue.add(rc);
		return rc;
	}
	
	public void renderAll() {
		mRenderQueue.sort(new Comparator<RenderCall>() {
			
			public int compare(RenderCall r1, RenderCall r2) {
				return r2.depth - r1.depth;
			}
			
		});
		for(RenderCall c : mRenderQueue) {
			switch (c.type) {
			case RENDER_CALL_IMAGE:
				renderImageNow(c.image, c.x, c.y, c.depth, c.transparency, c.applyOffset);
				break;
			case RENDER_CALL_FONT:
				renderTextNow(c.font, c.x, c.y, c.text, c.color, c.depth, c.transparency, c.applyOffset);
				break;
			case RENDER_CALL_DYNAMIC_IMAGE:
				renderDynamicImageNow(c.image, c.org, c.other, c.x, c.y, c.depth, c.transparency, c.applyOffset);
				break;
			default:
				break;
			}
		}
	}
	
	public void renderImageNow(PImage image, int x, int y, int depth, float transparency, boolean applyOffset) {
		if(applyOffset) {
			x += offsetX;
			y += offsetY;
		}
		if(x >= mWidth || y >= mHeight || x + image.getWidth() < 0 || y + image.getHeight() < 0)
			return;
		for(int yy = 0; yy < image.getHeight(); yy++) {
			if(yy + y < 0 || yy + y >= mHeight)
				continue;
			for(int xx = 0; xx < image.getWidth(); xx++) {
				if(xx + x < 0 || xx + x >= mWidth)
					continue;
				int color = image.pixels[xx + yy * image.getWidth()];
				if((color & 0xff000000) == 0)
					continue;
				if(mDepthBuffer[(xx + x) + (yy + y) * mWidth] < depth)
					continue;
				if(((color & 0xff000000) == 0xff000000 && transparency == 1) || !mBlending)
					mPixels[(xx + x) + (yy + y) * mWidth] = color;
				else {
					int src = mPixels[(xx + x) + (yy + y) * mWidth];
					float alpha = (float) ((byte)((color & 0xff000000) >> 24) & 0xff) / 255.0f;
					alpha *= transparency;
					short r = (short) ((color & 0xff) * alpha + (src & 0xff) * (1.0f - alpha));
					short g = (short) ((((color & 0xff00) >> 8) & 0xff) * alpha + (((src & 0xff00) >> 8) & 0xff) * (1.0f - alpha));
					short b = (short) ((((color & 0xff0000) >> 16) & 0xff) * alpha + (((src & 0xff0000) >> 16) & 0xff) * (1.0f - alpha));
					mPixels[(xx + x) + (yy + y) * mWidth] = r | (g << 8) | (b << 16);
				}
				mDepthBuffer[(xx + x) + (yy + y) * mWidth] = (byte)depth;
			} 
		}
	}
	
	public void renderDynamicImageNow(PImage image, int[] org, int[] other, int x, int y, int depth, float transparency, boolean applyOffset) {
		if(applyOffset) {
			x += offsetX;
			y += offsetY;
		}
		if(x >= mWidth || y >= mHeight || x + image.getWidth() < 0 || y + image.getHeight() < 0)
			return;
		for(int yy = 0; yy < image.getHeight(); yy++) {
			if(yy + y < 0 || yy + y >= mHeight)
				continue;
			for(int xx = 0; xx < image.getWidth(); xx++) {
				if(xx + x < 0 || xx + x >= mWidth)
					continue;
				int color = image.pixels[xx + yy * image.getWidth()];
				for(int i = 0; i < org.length; i++) {
					if((color & 0xffffff) == org[i]) {
						color = other[i];
						color |= 0xff000000;
						break;
					}
				}
				if((color & 0xff000000) == 0)
					continue;
				if(mDepthBuffer[(xx + x) + (yy + y) * mWidth] < depth)
					continue;
				if(((color & 0xff000000) == 0xff000000 && transparency == 1) || !mBlending)
					mPixels[(xx + x) + (yy + y) * mWidth] = color;
				else {
					int src = mPixels[(xx + x) + (yy + y) * mWidth];
					float alpha = (float) ((byte)((color & 0xff000000) >> 24) & 0xff) / 255.0f;
					alpha *= transparency;
					short r = (short) ((color & 0xff) * alpha + (src & 0xff) * (1.0f - alpha));
					short g = (short) ((((color & 0xff00) >> 8) & 0xff) * alpha + (((src & 0xff00) >> 8) & 0xff) * (1.0f - alpha));
					short b = (short) ((((color & 0xff0000) >> 16) & 0xff) * alpha + (((src & 0xff0000) >> 16) & 0xff) * (1.0f - alpha));
					mPixels[(xx + x) + (yy + y) * mWidth] = r | (g << 8) | (b << 16);
				}
				mDepthBuffer[(xx + x) + (yy + y) * mWidth] = (byte)depth;
			} 
		}
	}
	
	public void renderTextNow(PFont font, int x, int y, String text, int color, int depth, float transparency, boolean applyOffset) {
		if(applyOffset) {
			x += offsetX;
			y += offsetY;
		}
		int cx = x;
		for(int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			for(PFont.PCharacterData d : font.characters) {
				if(c == d.id) {
					renderCharacterNow(font, cx + d.xoff, y + d.yoff, d, color, depth, transparency);
					cx += d.xadv;
					break;
				}
			}
		}
	}
	
	public void renderCharacterNow(PFont font, int x, int y, PFont.PCharacterData c, int color, int depth, float transparency) {
		for(int yy = c.y; yy < c.h + c.y; yy++) {
			if(yy + y - c.y < 0 || yy + y - c.y >= mHeight)
				continue;
			for(int xx = c.x; xx < c.w + c.x; xx++) {
				if(xx + x - c.x < 0 || xx + x - c.x >= mWidth)
					continue;
				if((font.font.pixels[xx + yy * font.font.getWidth()] & 0xff) == 0)
					continue;
				if(mDepthBuffer[(xx + x - c.x) + (yy + y - c.y) * mWidth] < depth)
					continue;
				if(((font.font.pixels[xx + yy * font.font.getWidth()] & 0xff) == 0xff && transparency == 1) || !mBlending)
					mPixels[(xx + x - c.x) + (yy + y - c.y) * mWidth] = color;
				else {
					int src = mPixels[(xx + x - c.x) + (yy + y - c.y) * mWidth];
					int a = (font.font.pixels[xx + yy * font.font.getWidth()] & 0xff);
					float alpha = (float)a / 255.0f;
					alpha *= transparency;
					short r = (short) ((color & 0xff) * alpha + (src & 0xff) * (1.0f - alpha));
					short g = (short) ((((color & 0xff00) >> 8) & 0xff) * alpha + (((src & 0xff00) >> 8) & 0xff) * (1.0f - alpha));
					short b = (short) ((((color & 0xff0000) >> 16) & 0xff) * alpha + (((src & 0xff0000) >> 16) & 0xff) * (1.0f - alpha));
					mPixels[(xx + x - c.x) + (yy + y - c.y) * mWidth] = r | (g << 8) | (b << 16);
				}
				mDepthBuffer[(xx + x - c.x) + (yy + y - c.y) * mWidth] = (byte)depth;
			}
		}
	}

	public boolean getBlending() {
		return mBlending;
	}

	public void setBlending(boolean mBlending) {
		this.mBlending = mBlending;
	}
	
}
