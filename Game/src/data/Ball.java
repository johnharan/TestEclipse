package data;

import static helpers.Artist.drawCircle;


public class Ball {

	private int radius,sides;
	private float speed,x,y;
	private boolean alive;
	private int[] direction;
	
	
	public Ball(int radius, int sides, float speed, float x, float y) {
		this.radius = radius;
		this.sides = sides;
		this.speed = speed;
		this.x = x;
		this.y = y;
		this.direction = new int[2];
		this.direction[0] = 1;
		this.direction[1] = 0;
	}

	public void update(){
		findDirection();
		x += Boot.getDelta() * direction[0] * speed;
		y += Boot.getDelta() * direction[1] * speed;
		
	}
	
	public void draw(){
		drawCircle(x,y,radius,sides);
	}
	
	public void findDirection(){
		if(x >= Boot.WIDTH){
			direction[0] = -1;
		}else if(x <= 0){
			direction[0] = 1;
		}
		
		if(y >= Boot.HEIGHT){
			direction[1] = -1;
		}else if(y <= 0){
			direction[1] = 1;
		}
		
		
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public void die(){
		alive = false;
	}

	public int getRadius() {
		return radius;
	}


	public void setRadius(int radius) {
		this.radius = radius;
	}


	public float getSpeed() {
		return speed;
	}


	public void setSpeed(float speed) {
		this.speed = speed;
	}


	public float getX() {
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}
	
	

}