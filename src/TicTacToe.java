import java.util.Random;

public class TicTacToe {
/*	private Random random;*/
	private final int NORMAL = 0;
	private final int LOSE = -1;
	private final int WIN = 1;
	private final int DOUBLE_LOSE = -2;
	private final int DOUBLE_WIN = 2;
	private final int DIRECT_DOUBLE_WIN = -3;
	private final int BLOCK = 3;

	private int[][] board = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

	public TicTacToe() {
/*		random = new Random();*/
	}

	public boolean tick(int row, int column, int player) {

		if(board[row][column] != 0)
			return false;

		board[row][column] = player;
		return true;
	}

	public int win() {
		// check column and row
		for(int x = 0; x < board.length; x++) {
			if(board[0][x] == board[1][x] && board[0][x] == board[2][x] && board[0][x] != 0)
				return board[0][x];
			if(board[x][0] == board[x][1] && board[x][0] == board[x][2] && board[x][0] != 0)
				return board[x][0];
		}

		// check diagonal
		if(board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != 0)
			return board[0][0];

		if(board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != 0)
			return board[0][2];
		return 0;
	}

	public boolean full() {
		for (int[] xBoard : board) {
			for (int element : xBoard) {
				if (element == 0)
					return false;
			}
		}
		return true;
	}
	public void printBoard() {
		for (int[] xBoard : board) {
			for (int element : xBoard) {
				System.out.print(element + " ");
			}
			System.out.print("\n");
		}
	}

	public int[] choose(int turn) {
		return choose(turn, 0);
	}
	/**
	 *
	 * @param turn          current turn
	 * @return array coord  int[0] row, int[1] column, int[3] draw : 0, win : 1, lose : -1
	 */
	public int[] choose(int turn, int depth) {

		// coordinate of best move
		int[] coord = {-1, -1, 0};

		boolean firstWin = false;
		for(int x = 0; x < board.length; x++) {
			for(int y = 0; y < board.length; y++) {
				if(board[x][y] == 0) {
					board[x][y] = turn;

					// Initialize coord if it's not initialized
					if (coord[0] == -1 || coord[2] == DOUBLE_LOSE) {
						coord[0] = x;
						coord[1] = y;
						// coord[2] will be updated later
					}

					if (win() == turn) {
						if (firstWin) {
							coord[2] = DIRECT_DOUBLE_WIN;
							board[x][y] = 0;
							return coord;
						}
						coord[2] = WIN;
						firstWin = true;
					}

					// change turn
					int tmpTurn;
					if (turn == 1)
						tmpTurn = 2;
					else
						tmpTurn = 1;


					// if turn are winning, then check another place if there's double win
					// if turn are not winning then continue the recursion
					// min max here
					if (!firstWin) {
						int[] result = choose(tmpTurn, depth + 1);

						// update status of the current best move
						// check if current x, y is the best move
						if (coord[0] == x && coord[1] == y) {
							switch (result[2]) {
								case LOSE:
									coord[2] = WIN;
									break;

								case NORMAL:
									coord[2] = NORMAL;
									break;

								case WIN:
									coord[2] = LOSE;
									break;

								case DOUBLE_LOSE:
									coord[2] = DOUBLE_WIN;
									board[x][y] = 0;
									return coord;

								case DOUBLE_WIN:
									coord[2] = DOUBLE_LOSE;
									break;

								case BLOCK:
									coord[2] = NORMAL;
									break;

								// when it's too late to block it. return to outer method
								case DIRECT_DOUBLE_WIN:
									coord[2] = DOUBLE_LOSE;
									board[x][y] = 0;
									return coord;
							}
						}

						// if the opponent wins, then block it
						if (coord[2] == LOSE) {
							coord[0] = result[0];
							coord[1] = result[1];
							coord[2] = BLOCK;
						}

						// if this move allow us to win which means result[2] (the opponent status) is lose
						if (result[2] == LOSE && coord[2] != BLOCK && coord[2] != DOUBLE_WIN) {
							coord[0] = x;
							coord[1] = y;
							coord[2] = WIN;
						}
					}
					board[x][y] = 0;
				}
			}
		}
		return coord;
	}


}
