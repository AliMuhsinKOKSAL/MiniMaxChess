package piece;

import board.ChessBoard;
import board.BoardCreator;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;

public class Knight extends Piece {

	public Knight(Square square, PieceColor cColor) {
		super(BoardCreator.cBoard.getSquare(square.x, square.y),PieceType.knight, cColor);
		BoardCreator.cBoard.boardTool.placePiece(this);
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
				options.add(create(BoardCreator.cBoard.getSquare(newx, newy),color));
			}
		}
	}
}
