import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class GameFrame extends JFrame{

	public GamePanel snakePanel;
	
	public GameFrame(){
		super("Snake");
		snakePanel = new GamePanel();
		snakePanel.setBackground(Color.blue);
		add(snakePanel, SwingConstants.CENTER);
		
		
		this.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent evt) {
	            formKeyPressed(evt);
	        }});
		
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		this.setBounds(200, 200, snakePanel.width + 10, snakePanel.height + 10);
	}

	private void formKeyPressed(KeyEvent evt){
	    snakePanel.KeyPressed(evt);
	}
}
