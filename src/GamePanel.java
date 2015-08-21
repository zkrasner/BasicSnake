import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{
	
	//Variables and other needed Constants//
	
	public int width = 400, height = 400; 	//Dimensions
	
	private ArrayList<Point> snakesChain; 	//Positions of the links of the snake
	private Point lead;						//Position of the head of the snake
	private int
	snakeDimension = 10,					//Size of the snake
	moveDist = 10,							//Distance moved per cycle
	startSpeed = 175;
	private boolean alive, gameOver,		//Game continuation booleans
	up, down, left, right;					//Directional booleans
	private int score, getApple = 10;		//Score
	private Random gen = new Random();
	private Point apple = new Point(gen.nextInt(width), gen.nextInt(height));
	private int threshhold = 10;
	private int onAppleWait = 0;
	
	//Constructor//
	public GamePanel(){
		alive = true;
		
		Thread game = new Thread(this);
		game.start();
		
		lead = new Point(width/2, height/2);
		snakesChain = new ArrayList<Point>();
		up = true;
		
		score = 0;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	//Methods//
	
	/////////////////////////////////////////////
	//Painting//
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//Apples
		g.setColor(Color.red);
		if (onApple()) updateApple();
		g.fillRect(apple.x, apple.y, 10, 10);
		
		//Leader
		g.setColor(Color.black);
		g.fillOval(lead.x, lead.y, snakeDimension, snakeDimension);
		//Tail
		if (snakesChain.size() != 0){
			for (Point s: snakesChain){
				g.fillOval(s.x, s.y, snakeDimension, snakeDimension);
			}
		}
		
		//Score
		g.drawString("Score:\t" + score, 5, 15);
		
		//If the game is over
		if (gameOver) g.drawString("GAME OVER!", width/2 - 40, height/2);
		
	}

	/////////////////////////////////////////////
	//Key methods
	public void KeyPressed(KeyEvent evt){
		switch (evt.getKeyCode()){
		case KeyEvent.VK_UP:
			if (down == true) break;
			up = true;
			down = false;
			left = false;
			right = false;
			break;
		case KeyEvent.VK_DOWN:
			if (up == true) break;
			up = false;
			down = true;
			left = false;
			right = false;
			break;
		case KeyEvent.VK_LEFT:
			if (right == true) break;
			up = false;
			down = false;
			left = true;
			right = false;
			break;
		case KeyEvent.VK_RIGHT:
			if (left == true) break;
			up = false;
			down = false;
			left = false;
			right = true;
			break;
		}
	}
	
	/////////////////////////////////////////////
	//Movement//
	public void moveSnake(){
		for (int i = snakesChain.size()-1; i >= 0; i--){
			if (i == 0) {
				snakesChain.set(0, new Point(lead.x, lead.y));
				break;
			}
			snakesChain.set(i, snakesChain.get(i-1));
		}	
		if (up) lead.y -= moveDist;
		if (down) lead.y += moveDist;
		if (left) lead.x -= moveDist;
		if (right) lead.x += moveDist;
		placeLead(lead);
		onApple();
	}
	public void placeLead(Point p){
		lead.y = p.y;
		lead.x = p.x;
		repaint();
	}
	
	/////////////////////////////////////////////
	//Speed//
	public int speedUp(){
		int speed = startSpeed;
		
		if (score >= 50) speed -= 25;
		if (score >= 100) speed -= 25;
		if (score >= 150) speed -= 25;
		if (score >= 250) speed -= 25;
		if (score >= 400) speed = 25;
		
		return speed;
	}
	
	/////////////////////////////////////////////
	//Snake Length//
	public void addLinks(int links){
		for (int i = links; i >= 0; i--){
			snakesChain.add(new Point(-20,0));
		}
	}

	/////////////////////////////////////////////
	//Apple//
	public double distance(Point pOne, Point pTwo){
		int a = pOne.x - pTwo.x;
		int b = pOne.y - pTwo.y;
		double c = Math.sqrt((a*a) + (b*b));
		return c;
	}
	public boolean onApple(){
		if (onAppleWait > 0){
			onAppleWait = onAppleWait - 1;
			return false;
		} else {
			if (distance(lead, apple) <= threshhold){
				score += getApple;
				addLinks(1);
				onAppleWait = onAppleWait + 7;
				updateApple();
				return true;
			} else return false;
		}
	}
	public void updateApple(){
		apple.x = gen.nextInt(width - 30) + 10;
		apple.y = gen.nextInt(height - 30) + 10;
		for (Point p: snakesChain){
			if (distance(apple, p) <= 10) updateApple();
		}
		repaint();
	}

	/////////////////////////////////////////////
	//Game Ending//
	public void gameOver(){
		//out of bounds
		if (lead.x >= width || lead.x < 0) gameOver = true;
		if (lead.y >= height || lead.y < 0) gameOver = true;
		
		//eating tail of snake
		for (int i = snakesChain.size() - 1; i >= 1; i--){
			if (eatTail(snakesChain.get(i)))
				break;
		}
	}
	public boolean eatTail(Point tail){
		if (distance(lead, tail) <= 9){
			gameOver = true;
			return true;
		} else return false;
		
	}

	/////////////////////////////////////////////
	//House Keeping//
	public Dimension getPreferredSize(){
		return new Dimension(width,height);
	}
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	
	/////////////////////////////////////////////
	//Runnable//
	public void run(){
		while (!gameOver){
			try {
				Thread.sleep(speedUp());
				gameOver();
				if (alive) moveSnake();
			} catch (Exception e){}
			
		}
	}
}
