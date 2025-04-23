package mm_piece;

import board.ChessBoard;
import mini_max.MMChessBoard;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import option.OpType;
import option.Option;
import piece.Piece;

public class MMRook extends Piece {
	
	MMChessBoard board;
	public boolean isFirstMove = true;

	public MMRook(Square square, PieceColor cColor,MMChessBoard board) {
		super(board.getSquare(square.x, square.y), PieceType.rook, cColor);
		this.board = board;
	}

	@Override
	public void getMovement() {
		options.clear();
		// yatay sağ
		for (int i = square.x + 1; i < ChessBoard.BOARD_SIZE; i++) {

			Option opt = create(board.getSquare(i, square.y), color);
			if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
				options.add(opt);
			} else {
				options.add(opt);
				break;
			}
		}

		// yatay sol
		for (int i = square.x - 1; i >= 0; i--) {
			Option opt = create(board.getSquare(i, square.y), color);
			if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
				options.add(opt);
			} else {
				options.add(opt);
				break;
			}
		}

		// dikey sağ
		for (int i = square.y + 1; i < ChessBoard.BOARD_SIZE; i++) {
			Option opt = create(board.getSquare(square.x, i), color);
			if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
				options.add(opt);
			} else {
				options.add(opt);
				break;
			}
		}

		// dikey sol
		for (int i = square.y - 1; i >= 0; i--) {
			Option opt = create(board.getSquare(square.x, i), color);
			if (opt.opType != OpType.take && opt.opType != OpType.notTake) {
				options.add(opt);
			} else {
				options.add(opt);
				break;
			}
		}
	}
}
