package birdyGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class BirdyGame implements ActionListener, MouseListener, KeyListener
{
	public static BirdyGame birdyGame;

	public final int WIDTH = 800, HEIGHT = 800;
	public Renderer renderer;
	public Rectangle birdy;
	public int ticks, yMotion, score;
	public ArrayList<Rectangle> columns;
	public Random random;
	public boolean gameOver, gameStart;

	public BirdyGame()
	{
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		random = new Random();

		jframe.add(renderer);
		jframe.setTitle("Birdy Game");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		birdy = new Rectangle(WIDTH / 2 - 10, WIDTH / 2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);

		timer.start();
	}

	public void addColumn(boolean start)
	{
		int space = 300;
		int width = 100;
		int height = 50 + random.nextInt(300);

		if (start)
		{
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
		} else
		{
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}

	}

	public void paintColumn(Graphics g, Rectangle column)
	{
		g.setColor(Color.gray.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}

	public void jump()
	{
		if (gameOver)
		{
			birdy = new Rectangle(WIDTH / 2 - 10, WIDTH / 2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;

			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			gameOver = false;
		}

		if (!gameStart)
		{
			gameStart = true;
		} else if (!gameOver)
		{
			if (yMotion > 0)
			{
				yMotion = 0;
			}

			yMotion -= 10;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		int speed = 10;

		ticks++;

		if (gameStart)
		{
			for (int i = 0; i < columns.size(); i++)
			{
				Rectangle column = columns.get(i);

				column.x -= speed;
			}

			if (ticks % 2 == 0 && yMotion < 15)
			{
				yMotion += 2;
			}

			for (int i = 0; i < columns.size(); i++)
			{
				Rectangle column = columns.get(i);

				if (column.x + column.width < 0)
				{
					columns.remove(column);

					if (column.y == 0)
					{
						addColumn(false);
					}
				}
			}

			birdy.y += yMotion;

			for (Rectangle column : columns)
			{
				if (column.y == 0 && birdy.x + birdy.width / 2 > column.x + column.width / 2 - 10
						&& birdy.x + birdy.width / 2 < column.x + column.width / 2 + 10)
				{
					score++;
				}
				if (column.intersects(birdy))
				{
					gameOver = true;

					birdy.x = column.x - birdy.width;
				}
			}

			if (birdy.y > HEIGHT - 120 || birdy.y < 0)
			{
				gameOver = true;
			}

			if (birdy.y + yMotion >= HEIGHT - 120)
			{
				birdy.y = HEIGHT - 120 - birdy.height;
			}
		}

		renderer.repaint();
	}

	public void repaint(Graphics g)
	{
		g.setColor(Color.cyan.darker());
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.orange.darker());
		g.fillRect(0, HEIGHT - 120, WIDTH, 150);

		g.setColor(Color.green.darker());
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);

		g.setColor(Color.red.darker());
		g.fillRect(birdy.x, birdy.y, birdy.width, birdy.height);

		for (Rectangle column : columns)
		{
			paintColumn(g, column);
		}

		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));

		if (!gameStart)
		{
			g.drawString("Click to start", 75, HEIGHT / 2 - 50);
		}

		if (gameOver)
		{
			g.drawString("Game Over", 100, HEIGHT / 2 - 50);
		}

		if (!gameOver && gameStart)
		{
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
	}

	public static void main(String[] args)
	{
		birdyGame = new BirdyGame();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		jump();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			jump();
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

}
