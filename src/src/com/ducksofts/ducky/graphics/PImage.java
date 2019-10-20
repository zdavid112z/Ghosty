package com.ducksofts.ducky.graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class PImage {

	private int mWidth;
	private int mHeight;
	public int[] pixels;
	
	public PImage(String path) {
		try {
			BufferedImage image = ImageIO.read(PImage.class.getResourceAsStream(path));
			mWidth = image.getWidth();
			mHeight = image.getHeight();
			pixels = image.getRGB(0, 0, mWidth, mHeight, null, 0, mWidth);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PImage(int w, int h, int[] p) {
		mWidth = w;
		mHeight = h;
		pixels = p;
	}
	
	public PImage(int w, int h) {
		mWidth = w;
		mHeight = h;
		pixels = new int[w * h];
	}
	
	public PImage resize(int nw, int nh) {
		PImage image = new PImage(nw, nh);
		resize(nw, nh, image);
		return image;
	}
	
	public void resize(int nw, int nh, PImage image) {
		float ax = (float)mWidth / nw;
		float ay = (float)mHeight / nh;
		for(int j = 0; j < nh; j++) {
			for(int i = 0; i < nw; i++) {
				int color = pixels[(int)((float)i * ax) + ((int)((float)j * ay)) * mWidth];
				image.pixels[i + j * nw] = color;
			}
		}
	}
	
	public int getWidth() {
		return mWidth;
	}
	
	public int getHeight() {
		return mHeight;
	}	
}
