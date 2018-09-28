import java.util.Scanner;

public class Program {
	static TicTacToe ticTacToe = new TicTacToe();

	public static void main(String[] args) {
		humanvsbot();
	}

	public static void humanvsbot() {
		int winner = 0;
		int turn = 1;

		Scanner input = new Scanner(System.in);
		while (winner == 0 && !ticTacToe.full()) {
			if (turn == 1) {
				boolean valid = true;
				// to be sure that the player choose the valid case
				while (valid) {
					ticTacToe.printBoard();
					System.out.print("Choose row: ");
					int y = input.nextInt();

					System.out.print("Choose column: ");
					int x = input.nextInt();

					valid = !(ticTacToe.tick(y, x, turn));
					turn = 2;
				}
			} else if (turn == 2) {
				int[] coord = ticTacToe.choose(/*turn, */turn/*, 0, -1, 0, 10*/);
				ticTacToe.tick(coord[0], coord[1], turn);

				turn = 1;
			}

			winner = ticTacToe.win();

		}
		System.out.println("Winner " + winner);
	}

	public static void botvsbot() {
		int winner = 0;
		int turn = 1;

		Scanner input = new Scanner(System.in);
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
		System.out.print("Winner: " + winner);

	}
}
