import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Random;

class Surface extends JPanel {
	public static final int WIDTH = 30;
	public static final int HEIGHT = 16;
	public static final int MINES = 99;
	private int[][] blocks;
	private int[][] mines;
	private String state;
	private boolean ifFirst;
	
	public Surface() {
		addMouseListener(new MinesweeperMouseAdapter());
		init();
	}
	
	private void init() {
		blocks = new int[HEIGHT][WIDTH];
		mines = new int[HEIGHT][WIDTH];
		state = "play";
		ifFirst = true;
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				blocks[i][j] = 0;
			}
		}
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				mines[i][j] = 0;
			}
		}
		repaint();
	}
	
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (state == "play") {
			for (int i = 0; i < HEIGHT; i++) {
				for (int j = 0; j < WIDTH; j++) {
					switch (blocks[i][j]) {
						case 0:
							g2d.setPaint(new Color(200, 200, 200));
							break;
						case 10:
							g2d.setPaint(new Color(0, 0, 255));
							break;
						default:
							g2d.setPaint(new Color(0, 255, 0));
							break;
					}
					g2d.fillRect(j*30, i*30, 30, 30);
					g2d.setPaint(new Color(0, 0, 0));
					g2d.setFont(new Font("Dialog", Font.PLAIN, 15));
					if (blocks[i][j] > 0 && blocks[i][j] < 9) {
						g2d.drawString(Integer.toString(blocks[i][j]), j*30+10, i*30+20);
					}
					g2d.setPaint(new Color(128, 128, 128));
					g2d.drawRect(j*30, i*30, 30, 30);
				}
			}
		} else if (state == "game over") {
			for (int i = 0; i < HEIGHT; i++) {
				for (int j = 0; j < WIDTH; j++) {
					if (mines[i][j] == 0) {
						g2d.setPaint(new Color(0, 255, 0));
					} else {
						g2d.setPaint(new Color(255, 0, 0));
					}
					g2d.fillRect(j*30, i*30, 30, 30);
					g2d.setPaint(new Color(128, 128, 128));
					g2d.drawRect(j*30, i*30, 30, 30);
				}
			}
			g2d.setPaint(new Color(0, 0, 0, 175));
			g2d.fillRect((WIDTH*30-200) / 2, (HEIGHT*30-100) / 2, 200, 100);
			g2d.setPaint(new Color(255, 255, 255));
			g2d.drawRect((WIDTH*30-200) / 2, (HEIGHT*30-100) / 2, 200, 100);
			g2d.setFont(new Font("Dialog", Font.PLAIN, 30));
			g2d.drawString("Game Over!", (WIDTH*30-g2d.getFontMetrics().stringWidth("Game Over!")) / 2, (HEIGHT*30-30) / 2);
			g2d.drawRect((WIDTH*30-130) / 2, (HEIGHT*30-6) / 2, 130, 40);
			g2d.setFont(new Font("Dialog", Font.PLAIN, 25));
			g2d.drawString("Restart", (WIDTH*30-g2d.getFontMetrics().stringWidth("Restart")) / 2, (HEIGHT*30+52) / 2);
		} else {
			for (int i = 0; i < HEIGHT; i++) {
				for (int j = 0; j < WIDTH; j++) {
					if (mines[i][j] == 0) {
						g2d.setPaint(new Color(0, 255, 0));
					} else {
						g2d.setPaint(new Color(255, 0, 0));
					}
					g2d.fillRect(j*30, i*30, 30, 30);
					g2d.setPaint(new Color(128, 128, 128));
					g2d.drawRect(j*30, i*30, 30, 30);
				}
			}
			g2d.setPaint(new Color(0, 0, 0, 175));
			g2d.fillRect((WIDTH*30-200) / 2, (HEIGHT*30-100) / 2, 200, 100);
			g2d.setPaint(new Color(255, 255, 255));
			g2d.drawRect((WIDTH*30-200) / 2, (HEIGHT*30-100) / 2, 200, 100);
			g2d.setFont(new Font("Dialog", Font.PLAIN, 30));
			g2d.drawString("You Win!", (WIDTH*30-g2d.getFontMetrics().stringWidth("You Win!")) / 2, (HEIGHT*30-30) / 2);
			g2d.drawRect((WIDTH*30-130) / 2, (HEIGHT*30-6) / 2, 130, 40);
			g2d.setFont(new Font("Dialog", Font.PLAIN, 25));
			g2d.drawString("Restart", (WIDTH*30-g2d.getFontMetrics().stringWidth("Restart")) / 2, (HEIGHT*30+52) / 2);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	private class MinesweeperMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			Point mouseCoords = e.getPoint();
			int x = (int) (mouseCoords.getX()/30);
			int y = (int) (mouseCoords.getY()/30);
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (state == "play") {
					if (ifFirst) {
						initMines(y, x);
						ifFirst = false;
					}
					click(y, x);
				} else {
					if (mouseCoords.getX() > (WIDTH*30-130)/2 && mouseCoords.getX() < (WIDTH*30+130)/2 && mouseCoords.getY() > (HEIGHT*30-6)/2 && mouseCoords.getY() < (HEIGHT*30+74)/2) {
						init();
					}
				}
			} else {
				if (state == "play") {
					if (blocks[y][x] == 0) {
						blocks[y][x] = 10;
					} else if (blocks[y][x] == 10) {
						blocks[y][x] = 0;
					}
				}
			}
			repaint();
		}
	}
	
	private void initMines(int x, int y) {
		for (int i = 0; i < MINES; i++) {
			Random rand = new Random();
			int xRand = rand.nextInt(HEIGHT);
			int yRand = rand.nextInt(WIDTH);
			boolean canMine = true;
			for (int j = -1; j < 2; j++) {
				for (int k = -1; k < 2; k++) {
					if (xRand == x+j && yRand == y+k) {
						canMine = false;
						break;
					}
				}
			}
			if (mines[xRand][yRand] == 1) {
				canMine = false;
			}
			if (canMine) {
				mines[xRand][yRand] = 1;
			} else {
				i--;
			}
		}
	}
	
	private void click(int x, int y) {
		if (blocks[x][y] == 0) {
			if (mines[x][y] == 0) {
				int count = 0;
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if (x+i > -1 && x+i < HEIGHT && y+j > -1 && y+j < WIDTH) {
							if (mines[x+i][y+j] == 1) {
								count++;
							}
						}
					}
				}
				if (count == 0) {
					blocks[x][y] = 9;
					for (int i = -1; i < 2; i++) {
						for (int j = -1; j < 2; j++) {
							if (x+i > -1 && x+i < HEIGHT && y+j > -1 && y+j < WIDTH) {
								click(x+i, y+j);
							}
						}
					}
				} else {
					blocks[x][y] = count;
				}
				checkWin();
			} else {
				state = "game over";
			}
		}
	}
	
	private void checkWin() {
		int unexplored = 0;
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if (blocks[i][j] == 0 && mines[i][j] == 0) {
					unexplored++;
					break;
				}
			}
		}
		if (unexplored == 0) {
			state = "win";
		}
	}
}

public class Minesweeper extends JFrame {
	
	public Minesweeper() {
		initUI();
	}
	
	private void initUI() {
		add(new Surface());
		setTitle("Minesweeper");
		setSize(Surface.WIDTH*30 + 1, Surface.HEIGHT*30 + 31);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Minesweeper minesweeper = new Minesweeper();
				minesweeper.setVisible(true);
			}
		});
	}
}
