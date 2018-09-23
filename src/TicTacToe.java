import java.util.Random;

public class TicTacToe {
	Random random;
	private int[][] board = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

	public TicTacToe() {
		random = new Random();
	}

	public boolean check(int row, int column, int player) {

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
	 * @return array coord  int[0] row, int[1] column, int[2] pointMin, int[3] draw : 0, win: 1
	 */
	public int[] choose(int player, int turn, int point, int pointMin, int currentDepth, int depthMax) {
		if(currentDepth == depthMax) {
			System.out.println("depth reached");
			return new int[]{-1, -1, -1, -1}; // stop the method without any result
		}

		if(point > pointMin && pointMin != -1)
			return new int[]{-1, -1, -1, -1}; // stop the method without any result

		int[] coord = {-1, -1, -1, 0}; // coord[2] is pointMin

		for(int x = 0; x < board.length; x++) {
			for(int y = 0; y < board.length; y++) {
				if(board[x][y] == 0) {
					board[x][y] = turn;

					// initialize coord if it's uninitialized
					if(coord[0] == -1) {
						coord[0] = x;
						coord[1] = y;
						coord[2] = pointMin;
						coord[3] = 0;
					}

					int winner = win();
					if(winner == player) {
						board[x][y] = 0;
						return new int[]{x, y, point, 1};
					} else if(winner != 0) {
						board[x][y] = 0;
						return new int[]{-1, -1, point, -1};
					}

					// change turn
					int tempTurn;
					if(turn == 1)
						tempTurn = 2;
					else
						tempTurn = 1;

					// min max here
					int[] result = choose(player, tempTurn, point+1, pointMin, currentDepth + 1, depthMax);
					if(result[0] != -1) { // if it actually played
						if((result[2] < pointMin && result[3] == 1) || pointMin == -1) { // check if it's a better solution
							pointMin = result[2];
							coord[0] = x;
							coord[1] = y;
							coord[2] = result[2];
							coord[3] = result[3];
						}


					}
					board[x][y] = 0;
				}
			}
		}
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
		for(int x = 0; x < board.length; x++) {
			for(int y = 0; y < board.length; y++) {
				if(board[x][y] == 0)
					return false;
			}
		}
		return true;
	}
	public void printBoard() {
		for(int x = 0; x < board.length; x++){
			for (int y = 0; y < board.length; y++) {
				System.out.print(board[x][y] + " ");
			}
			System.out.print("\n");
		}
	}
}