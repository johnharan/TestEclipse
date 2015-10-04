package data;
import java.lang.management.ManagementFactory;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Timer;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;
import static helpers.Artist.*;
import static data.Ball.*;

public class Boot {
    
	static final int WIDTH = Display.getWidth(),HEIGHT = Display.getHeight(), MAX_FRAMES_SKIPPED = 5;
	static final float MS_PER_UPDATE = 16.6f;
	
	private long currentTime,lastTime;
	private static long delta;
	private int framesInLastSecond = 0,framesInCurrentSecond = 0;
    private static Paddle paddleLeft;
    private static Paddle paddleRight;
    private static boolean shutdown = false;
	private static Ball pong;
	private static int game_state;
	private int plX;
	private int plY;
	private int prX;
	private int prY;
	private int paddleWidth;
	private int paddleHeight;
	private int ballRadius;
	private int ballSides;
	private int ballX;
	private int ballY;
	private float ballSpeed;
	private static Scoreboard scores;
	private static Splashscreen splash;

	public Boot() {
		beginSession();
		
		game_state = 0; // 0 is start, 1 is play, 2 is end;
		
		paddleWidth = 64;
		paddleHeight = 128;
		plX = 150; 
		plY = 278;
		prX = 1700; 
		prY = 277;
		
		ballRadius = 20;
		ballSides = 15;
		ballX = Display.getWidth()/2;
		ballY = 360;
		ballSpeed = 1.5f;
		
		Texture paddle = loadTexture("res/paddle.png","PNG");
		
		splash = new Splashscreen();
		scores = new Scoreboard(3, 2); // pointsPerGame, rounds
		
		paddleLeft = new Paddle(paddle, plX, plY, paddleWidth, paddleHeight);
		paddleRight = new Paddle(paddle, prX, prY, paddleWidth, paddleHeight);
		pong = new Ball(ballRadius, ballSides, ballSpeed, ballX, ballY);


		long nextSecond = System.currentTimeMillis() + 1000;
		lastTime = System.nanoTime();

		while(!shutdown){
			currentTime = System.nanoTime();
			delta = (currentTime - lastTime) / 1000000; // delta is the duration of time since last frame/iteration of loop
			lastTime = currentTime;	
	
			if(delta >= 20){ // restricting delta time to 20ms
				delta = 20;
			}
		    
			update(); // checks for escape key
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clears screen each time.. don't need this if drawing background
			
			drawNet();
			
			
			
			if(game_state == 1){ // play state
				scores.update();
				
				paddleLeft.update();
				paddleLeft.draw();
				paddleRight.updateAI(pong, delta);
				paddleRight.draw();
			
			
				if (pong.isAlive()) {
					pong.update();
					pong.draw();
					// the below if block uses a new thread to decay the ai's
					// performance over time, the performance resets after a
					// score
					if (Thread.activeCount() <= 2) { // this allows the main thread plus max of one timer thread
						Thread timedMissAdjustment = new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(2000); // wait 2 seconds
									paddleRight.setLow(paddleRight.getLow() + 15);
									paddleRight.setMax(paddleRight.getMax() + 30);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						});
						timedMissAdjustment.start();
					}
				} else {
					pong.update();
					if (Thread.activeCount() <= 2) { // this allows the main thread plus max of one timer thread																											
						Thread timedRespawn = new Thread(new Runnable() {
							public void run() {
								try {
									Thread.sleep(1000); // wait 1 second then respawn ball
									pong.respawn();
									paddleRight.setLow(0);
									paddleRight.setMax(0);
									paddleRight.setOffset(0); // resets the ai's offset
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						});
						timedRespawn.start();
					}
				}
			//System.out.println("Low: " + paddleRight.getLow() + ",High: " + paddleRight.getMax());
			
			}else if(game_state == 2){ // end game state
				scores.update();
				paddleLeft.draw();
				paddleRight.draw();
				splash.update();
				splash.detectClick();
				
				// if reset button clicked , reset game and go to state 0
				if(splash.getPlay() == true){
					scores.reset();
					paddleLeft.resetPaddle(plX, plY, paddleWidth, paddleHeight);
					paddleRight.resetPaddle(prX, prY, paddleWidth, paddleHeight);
					game_state = 1;
					splash.setPlay(false);
				}
			}else{ // start state
				splash.update();
				splash.detectClick();
				
				
				if(splash.getPlay() == true){
					game_state = 1;
					splash.setPlay(false);
				}
				
				
			}
		    //////////////////////////
			
			Display.update();
			Display.sync(60);
		
			/////////////////////FPS///////////////////////// 
			long currentTimeT = System.currentTimeMillis();
			if (currentTimeT > nextSecond) {
				nextSecond += 1000;
				framesInLastSecond = framesInCurrentSecond;
				framesInCurrentSecond = 0;
			}
			framesInCurrentSecond++;
			////////////////////////////////////////////////
			
			//System.out.println("Delta: " + delta + ",FPS: " + framesInLastSecond);
		}
		
		Display.destroy();
		
	}


	public static Scoreboard getScores() {
		return scores;
	}


	public static int getGameState() {
		return game_state;
	}


	public static void setGameState(int state) {
		game_state = state;
	}


	public static Paddle getPaddleLeft() {
		return paddleLeft;
	}

	public static Paddle getPaddleRight() {
		return paddleRight;
	}

	public static void main(String[] args) {
		new Boot();
        
	}
	
	public void update(){
		while(Keyboard.next()){
			if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
				shutdown = true;
			}
		}	
	}

	public static Long getDelta(){
		return delta;
	}
	
}
