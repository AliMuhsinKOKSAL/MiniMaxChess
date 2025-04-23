package piece;

import board.ChessBoard;
import board.BoardCreator;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import option.OpType;
import option.Option;

public class Queen extends Piece {

	public Queen(Square square, PieceColor cColor) {
		super(square, PieceType.queen, cColor);
		BoardCreator.cBoard.boardTool.placePiece(this);
	}

	@Override
	public void getMovement() {
		options.clear();
		// x- y+
		for (int i = 1; i < ChessBoard.BOARD_SIZE; i++) {
			if (square.x - i >= 0 && square.y + i < ChessBoard.BOARD_SIZE) {
				Option opt = create(BoardCreator.cBoard.getSquare(square.x - i, square.y + i), color);
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
				Option opt = create(BoardCreator.cBoard.getSquare(square.x - i, square.y - i), color);
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
				Option opt = create(BoardCreator.cBoard.getSquare(square.x + i, square.y + i), color);
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
				Option opt = create(BoardCreator.cBoard.getSquare(square.x + i, square.y - i), color);
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

		// yatay sağ
		for (int i = square.x + 1; i < ChessBoard.BOARD_SIZE; i++) {
			Option opt = create(BoardCreator.cBoard.getSquare(i, square.y), color);
			if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
				options.add(opt);
			} else {
				options.add(opt);
				break;
			}
		}

		// yatay sol
		for (int i = square.x - 1; i >= 0; i--) {
			Option opt = create(BoardCreator.cBoard.getSquare(i, square.y), color);
			if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
				options.add(opt);
			} else {
				options.add(opt);
				break;
			}
		}

		// dikey sağ
		for (int i = square.y + 1; i < ChessBoard.BOARD_SIZE; i++) {
			Option opt = create(BoardCreator.cBoard.getSquare(square.x, i), color);
			if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
				options.add(opt);
			} else {
				options.add(opt);
				break;
			}
		}

		// dikey sol
		for (int i = square.y - 1; i >= 0; i--) {
			Option opt = create(BoardCreator.cBoard.getSquare(square.x, i), color);
			if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
				options.add(opt);
			} else {
				options.add(opt);
				break;
			}
		}

	}
}
