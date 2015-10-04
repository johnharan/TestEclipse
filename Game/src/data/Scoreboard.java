package data;

import static helpers.Artist.drawNumbers;
import static helpers.Artist.loadTexture;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;


public class Scoreboard {
	private int currentRound,leftPaddlePoints,rightPaddlePoints,leftPaddleRounds,rightPaddleRounds;
	private static boolean gameOver = false;
	private Texture numbers;
	private int pointsPerGame,totalRounds;
	

	public int getLeftPaddleRounds() {
		return leftPaddleRounds;
	}

	public int getRightPaddleRounds() {
		return rightPaddleRounds;
	}

	public int getLeftPaddlePoints() {
		return leftPaddlePoints;
	}

	public int getRightPaddlePoints() {
		return rightPaddlePoints;
	}

	public Scoreboard(int pointsPerGame, int totalRounds) {
		this.pointsPerGame = pointsPerGame;
		this.totalRounds = totalRounds;
		numbers = loadTexture("res/numbers.png","PNG");
		
	}

	public void update(){
		drawNumbers(numbers,this.getLeftPaddlePoints(),Display.getWidth()/2 - 420,50);
		drawNumbers(numbers,this.getLeftPaddleRounds(),Display.getWidth()/2 - 300,50);
		
		drawNumbers(numbers,this.getRightPaddlePoints(),Display.getWidth()/2 + 180,50);
		drawNumbers(numbers,this.getRightPaddleRounds(),Display.getWidth()/2 + 300,50);
	}
	
	public void newRound(){
		leftPaddlePoints = 0;
		rightPaddlePoints = 0;
		if(currentRound == totalRounds){
			gameOver = true;
			Boot.setGameState(2);
		}else{
			currentRound++;
		}	
	}

	public void addPoint(String paddle) {
		if(paddle.equals("PaddleLeft")){
			leftPaddlePoints++;
			if(leftPaddlePoints >= pointsPerGame){
				leftPaddleRounds++;
				newRound();
			}
		}else{
			rightPaddlePoints++;
			if(rightPaddlePoints >= pointsPerGame){
				rightPaddleRounds++;
				newRound();
			}
		}
		System.out.println("Human: Points = " + leftPaddlePoints + ",Rounds = " + leftPaddleRounds);
		System.out.println("AI: Points = " + rightPaddlePoints + ",Rounds = " + rightPaddleRounds);
		System.out.println();
	}
	
	public static boolean isGameOver() {
		return gameOver;
	}
	
	public void reset(){
		leftPaddlePoints = 0;
		rightPaddlePoints = 0;
		leftPaddleRounds = 0;
		rightPaddleRounds = 0;
		currentRound = 0;
	}
}
