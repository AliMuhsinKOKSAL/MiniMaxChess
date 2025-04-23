package mm_piece;

import board.ChessBoard;
import mini_max.MMChessBoard;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import option.OpType;
import option.Option;
import piece.Piece;

public class MMBishop extends Piece {

	MMChessBoard board;
	
	public MMBishop(Square square, PieceColor cColor, MMChessBoard board) {
		super(square, PieceType.bishop, cColor);
		this.board = board;
	}

	@Override
	public void getMovement() {
		options.clear();
		// x- y+
		for (int i = 1; i < ChessBoard.BOARD_SIZE; i++) {
			if (square.x - i >= 0 && square.y + i < ChessBoard.BOARD_SIZE) {
				Option opt = create(board.getSquare(square.x - i, square.y + i), color);
				if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
					options.add(opt);
				} else {
					options.add(opt);
					break;
				}
			} else {
				break;
			}
		}

		// x- y-
		for (int i = 1; i < ChessBoard.BOARD_SIZE; i++) {
			if (square.x - i >= 0 && square.y - i >= 0) {
				Option opt = create(board.getSquare(square.x - i, square.y - i), color);
				if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
					options.add(opt);
				} else {
					options.add(opt);
					break;
				}
			} else {
				break;
			}
		}

		// x+ y+
		for (int i = 1; i < ChessBoard.BOARD_SIZE; i++) {
			if (square.x + i < ChessBoard.BOARD_SIZE && square.y + i < ChessBoard.BOARD_SIZE) {
				Option opt = create(board.getSquare(square.x + i, square.y + i), color);
				if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
					options.add(opt);
				} else {
					options.add(opt);
					break;
				}
			} else {
				break;
			}
		}

		// x+ y-
		for (int i = 1; i < ChessBoard.BOARD_SIZE; i++) {
			if (square.x + i < ChessBoard.BOARD_SIZE && square.y - i >= 0) {
				Option opt = create(board.getSquare(square.x + i, square.y - i), color);
				if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
					options.add(opt);
				} else {
					options.add(opt);
					break;
				}
			} else {
				break;
			}
		}
	}
}
