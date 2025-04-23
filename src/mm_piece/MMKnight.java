package mm_piece;

import board.ChessBoard;
import mini_max.MMChessBoard;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import piece.Piece;

public class MMKnight extends Piece {
	
	MMChessBoard board;

	public MMKnight(Square square, PieceColor cColor,MMChessBoard board) {
		super(board.getSquare(square.x, square.y),PieceType.knight, cColor);
		this.board = board;
	}

	@Override
	public void getMovement() {
		options.clear();
		int[][] movei = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 },
				{ 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }
		};

		for (int[] move : movei) {
			int newx = square.x + move[0];
			int newy = square.y + move[1];
			if (newx >= 0 && newx < ChessBoard.BOARD_SIZE && newy >= 0 && newy < ChessBoard.BOARD_SIZE) {
				options.add(create(board.getSquare(newx, newy),color));
			}
		}
	}
}
