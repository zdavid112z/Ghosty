package com.ducksofts.ghosty;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.ducksofts.ducky.gamework.LevelManager;
import com.ducksofts.ducky.graphics.GameRenderer;
import com.ducksofts.ducky.graphics.PImage;
import com.ducksofts.ducky.utils.Input;
import com.ducksofts.ghosty.entity.Furniture;
import com.ducksofts.ghosty.entity.Music;
import com.ducksofts.ghosty.entity.NPC;
import com.ducksofts.ghosty.entity.Player;
import com.ducksofts.ghosty.levels.DefaultLevel;
import com.ducksofts.ghosty.levels.ImageLevel;

public class MainGame extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static MainGame mainGame;
	
	public static final String TITLE = "Ghosty";
	public static final int WIDTH = 640;
	public static final int HEIGHT = 400;
	public static final int SCALE = 2;
	public static final int UPS = 60;
	
	private JFrame mFrame;
	private Thread mThread;
	private Input mInput;
	private LevelManager mLevelManager;
	private GameRenderer mRenderer;
	private boolean mRunning = false;
	
	public static void main(String[] args) {
		mainGame = new MainGame();
		mainGame.mFrame.add(mainGame);
		mainGame.mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ImageIcon i = new ImageIcon(MainGame.class.getResource("/misc/icon.png"));
		mainGame.mFrame.setIconImage(i.getImage());
		
		mainGame.mFrame.pack();
		mainGame.mFrame.setLocationRelativeTo(null);
		mainGame.mFrame.setTitle(TITLE);
		mainGame.mFrame.setVisible(true);
		mainGame.mFrame.setEnabled(true);
		mainGame.start();
	}

	public MainGame() {
		mFrame = new JFrame();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		mInput = new Input();
		addKeyListener(mInput);
		addMouseListener(mInput);
		addMouseMotionListener(mInput);
		addMouseWheelListener(mInput);
		mRenderer = new GameRenderer(WIDTH, HEIGHT);
		mRenderer.setBlending(true);
		mLevelManager = new LevelManager();
	}
	
	public synchronized void start() {
		if(mRunning)
			return;
		mRunning = true;
		mThread = new Thread(this, "Display");
		mThread.start();
	}
	
	public synchronized void stop() {
		if(!mRunning)
			return;
		mRunning = false;
		try {
			mThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	ImageLevel menu;
	ImageLevel help;
	DefaultLevel level;
	DefaultLevel level2;
	ImageLevel thx;
	
	public void run() {
		Music.loadMusic();
		Furniture.loadImages();
		NPC.loadImages();
		Player.loadImages();
		menu = new ImageLevel(mLevelManager, new PImage("/misc/menu.png"), KeyEvent.VK_SPACE);
		help = new ImageLevel(mLevelManager, new PImage("/misc/help.png"), KeyEvent.VK_SPACE);
		level = new DefaultLevel(mLevelManager, new PImage("/level/map.png"), new PImage("/level/gmap.png"), mRenderer);
		level.customize("/level/map.data");
		level.customize(new PImage("/level/entity.png"));
		level2 = new DefaultLevel(mLevelManager, new PImage("/level/map2.png"), new PImage("/level/gmap2.png"), mRenderer);
		level2.customize("/level/map2.data");
		level2.customize(new PImage("/level/entity2.png"));
		thx = new ImageLevel(mLevelManager, new PImage("/misc/thx.png"), 0);
		mLevelManager.setLevel(menu);
		long interval = 1000000000 / UPS;
		long timer = System.nanoTime();
		while(mRunning) {
			while(System.nanoTime() - timer >= interval) {
				update();
				timer += interval;
			}
			render();
		}
	}
	
	private void update() {
		Input.update((float)WIDTH / (float)getWidth(), (float)HEIGHT / (float)getHeight());
		mLevelManager.update();
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		mRenderer.clearAll();
		mLevelManager.render(mRenderer);
		g.drawImage(mRenderer.getImage(), 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	public void nextLevel() {
		mLevelManager.nextLevel();
	}
}
