import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.util.Scanner;
import java.util.Random;


public class Program {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;
	private static final int FPS = 60;
	private static final String WINDOW_TITLE = "Tic Tac Toe";

	private static TicTacToe ticTacToe = new TicTacToe();
	private static Random random = new Random();

	private static void printWinner(int winner) {
		if(winner == 0)
			System.out.print("Draw");
		else
			System.out.print("Winner is " + winner);
	}

	private static void initDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle(WINDOW_TITLE);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static void initGL() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	private static void drawX(int x, int y) {
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x + 50, y + 50);

			GL11.glVertex2d(x + 50, y);
			GL11.glVertex2d(x, y + 50);
		GL11.glEnd();
	}

	private static void drawO(int x, int y) {
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x + 50, y);

			GL11.glVertex2d(x + 50, y);
			GL11.glVertex2d(x + 50, y + 50);

			GL11.glVertex2d(x + 50, y + 50);
			GL11.glVertex2d(x, y + 50);

			GL11.glVertex2d(x, y + 50);
			GL11.glVertex2d(x, y);
		GL11.glEnd();
	}

	private static void drawBoard() {
		int[][] myBoard = ticTacToe.getBoard();

		// Draw border
		final int START_X = 200;
		final int START_Y = 100;

		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(START_X, START_Y);
			GL11.glVertex2d(START_X, START_Y + 300);

			GL11.glVertex2d(START_X + 100, START_Y);
			GL11.glVertex2d(START_X + 100, START_Y + 300);

			GL11.glVertex2d(START_X - 100, START_Y + 100);
			GL11.glVertex2d(START_X + 200, START_Y + 100);

			GL11.glVertex2d(START_X - 100, START_Y + 200);
			GL11.glVertex2d(START_X + 200, START_Y + 200);
		GL11.glEnd();

		// Draw x and o
		for (int row = 0; row < myBoard.length; row++) {
			for (int column = 0; column < myBoard.length; column++) {
				int y = (row + 1) * 100 + 25;
				int x = (column + 1) * 100 + 25;

				if(myBoard[row][column] == 1)
					drawX(x, y);
				else if (myBoard[row][column] == 2)
					drawO(x, y);
			}
		}
	}

	private static int[] getInput() {
		int x = -1;
		int y = -1;
		while (x < 0 || y < 0 || x > 0 || y > 0) {

			while (!Mouse.isButtonDown(0)) {
				System.out.println(Mouse.getX());
				System.out.println(Mouse.getY());
			}

			x = Mouse.getX() / 100;
			y = Mouse.getY() / 100;
		}

		return new int[]{x, y};
	}

	private static void humanvsbot() {
		int winner = 0;
		int turn = random.nextInt(2) + 1;

		while (winner == 0 && !ticTacToe.full() && !Display.isCloseRequested()) {
			drawBoard();
			Display.update();

			if (turn == 1) {
				boolean valid = true;
				// to be sure that the player choose the valid case
				while (valid) {
					ticTacToe.printBoard();
					int[] coord = getInput();

					valid = !(ticTacToe.tick(coord[1], coord[0], turn));
					turn = 2;
				}
			} else if (turn == 2) {
				int[] coord = ticTacToe.choose(/*turn, */turn/*, 0, -1, 0, 10*/);
				ticTacToe.tick(coord[0], coord[1], turn);

				turn = 1;
			}

			winner = ticTacToe.win();

			Display.sync(FPS);

		}
		ticTacToe.printBoard();
		printWinner(winner);
		Display.destroy();
	}

	private static void botvsbot() {
		int winner = 0;
		int turn = 1;

		while(winner == 0 && !ticTacToe.full()) {

			if(turn == 1)
				turn = 2;
			else
				turn = 1;
			int[] coord = ticTacToe.choose(/*turn , */turn/*, 0, -1, 0, 10*/);
			ticTacToe.tick(coord[0], coord[1], turn);
			ticTacToe.printBoard();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			winner = ticTacToe.win();
		}
		printWinner(winner);

	}


	public static void main(String[] args) {
		initDisplay();
		initGL();
		humanvsbot();
	}

}
