import java.util.Random;

public class TicTacToe {
	private Random random;
	private int[][] board = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

	public TicTacToe() {
		random = new Random();
	}

	public boolean tick(int row, int column, int player) {

		if(board[row][column] != 0)
			return false;

		board[row][column] = player;
		return true;
	}

	/**
	 *
	 * @param player        who is supposed to win, either 1 or 2
	 * @param turn          current turn
	 * @param point
	 * @param pointMin      most optimal point that can get
	 * @param currentDepth  level of difficulty
	 * @param depthMax
	 * @return array coord  int[0] row, int[1] column, int[2] pointMin, int[3] draw : 0, win : 1, lose : -1
	 */
	public int[] choose(int player, int turn, int point, int pointMin, int currentDepth, int depthMax) {
		if(currentDepth == depthMax)
			return new int[]{-1, -1, -1, 0}; // stop the method without any result

		if(point > pointMin && pointMin != -1)
			return new int[]{-1, -1, -1, 0}; // stop the method without any result

		int[] coord = {-1, -1, -1, 0}; // coord[2] is pointMin

		/*
		 * firstLost set to true when we can lose
		 * and if firstLost is true and we lose in another coord
		 * then there's no way to block it
		 * choose another move
		 */
		boolean firstLost = false;
		for(int x = 0; x < board.length; x++) {
			for(int y = 0; y < board.length; y++) {
				if(board[x][y] == 0) {

					board[x][y] = turn;

					// initialize coord if it's uninitialized or the previous move is bad

					if(coord[0] == -1 || coord[3] == -1 || coord[3] == -2) {
						coord[0] = x;
						coord[1] = y;
						coord[2] = pointMin;
						if(coord[3] == -2)
							coord[3] = 2;
						else
							coord[3] = 0;
					}

					int winner = win();
					if(winner == player) {
						board[x][y] = 0;
						return new int[]{x, y, point, 1};
					} else if(winner != 0) {
						board[x][y] = 0;
						/*return new int[]{-1, -1, point, -1};*/
						if(!firstLost)
							firstLost = true;
						else
							return new int[]{-1, -1, point, -2};
					}

					// change turn
					int tempTurn;
					if(turn == 1)
						tempTurn = 2;
					else
						tempTurn = 1;

					/*
					 * when firstLost is true, we will only focus on finding another possibility that we can lose
					 */

					if(!firstLost) {
						// min max here
						int[] result = choose(player, tempTurn, point + 1, pointMin, currentDepth + 1, depthMax);
						if (coord[0] == x && coord[1] == y)
							coord[3] = result[3]; // update status win or lose for the current coord


						// if it's impossible to block which means result[3] == -2, and the turn is not player, return
						if(result[3] == -2 && turn != player) {
							board[x][y] = 0;
							return new int[]{-1, -1, point, -2};
						}

						if (result[0] != -1) { // if it actually played
							// random choice if the next move is not worse than the current move
							boolean choice = result[2] == pointMin && random.nextInt(2) == 1 && result[3] == 1;

							if ((result[2] < pointMin && result[3] == 1) || pointMin == -1 || choice) { // check if it's a better solution
								pointMin = result[2];
								coord[0] = x;
								coord[1] = y;
								coord[2] = result[2];
								coord[3] = result[3];
							}


						}
					}
					board[x][y] = 0;
				}
			}
		}
		if(firstLost)
			return new int[]{-1, -1, point, -1};
		return coord;
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
}
