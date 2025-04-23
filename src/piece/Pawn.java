package piece;

import board.ChessBoard;
import board.BoardCreator;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import option.OpType;
import option.Option;

public class Pawn extends Piece {

	public static boolean isValidMoveOn = false;

	public Pawn(Square square, PieceColor cColor) {
		super(BoardCreator.cBoard.getSquare(square.x, square.y), PieceType.pawn, cColor);
		BoardCreator.cBoard.boardTool.placePiece(this);
	}

	@Override
	public void getMovement() {
		options.clear();

		boolean[] cPass = canEnPassant();
		if (cPass[0]) {
			options.add(new Option(BoardCreator.cBoard.getSquare(square.x - 1, square.y + 1), OpType.take));
		} else if (cPass[1]) {
			options.add(new Option(BoardCreator.cBoard.getSquare(square.x - 1, square.y - 1), OpType.take));
		} else if (cPass[2]) {
			options.add(new Option(BoardCreator.cBoard.getSquare(square.x + 1, square.y + 1), OpType.take));
		} else if (cPass[3]) {
			options.add(new Option(BoardCreator.cBoard.getSquare(square.x + 1, square.y - 1), OpType.take));
		}

		if (color == PieceColor.white) {
			if (square.y == 1 && BoardCreator.cBoard.getSquare(square.x, square.y + 1).piece == null)
				if (square.y + 2 < ChessBoard.BOARD_SIZE) {
					Option opt = createPawn(BoardCreator.cBoard.getSquare(square.x, square.y + 2), color);
					options.add(opt);
				}

			if (square.y + 1 < ChessBoard.BOARD_SIZE) {
				Option opt = createPawn(BoardCreator.cBoard.getSquare(square.x, square.y + 1), color);
				options.add(opt);
			}

			if (square.x + 1 < ChessBoard.BOARD_SIZE && square.y + 1 < ChessBoard.BOARD_SIZE) {
				if (isFullBishop(BoardCreator.cBoard.getSquare(square.x + 1, square.y + 1))) {
					options.add(create(BoardCreator.cBoard.getSquare(square.x + 1, square.y + 1), color));
				}
			}
			if (square.x - 1 >= 0 && square.y + 1 < ChessBoard.BOARD_SIZE) {
				if (isFullBishop(BoardCreator.cBoard.getSquare(square.x - 1, square.y + 1))) {
					options.add(create(BoardCreator.cBoard.getSquare(square.x - 1, square.y + 1), color));
				}
			}
			// left - right+
		} else {
			if (square.y == 6 && BoardCreator.cBoard.getSquare(square.x, square.y - 1).piece == null)
				if (square.y - 2 >= 0) {
					Option opt = createPawn(BoardCreator.cBoard.getSquare(square.x, square.y - 2), color);
					options.add(opt);
				}
			if (square.y - 1 >= 0) {
				Option opt = createPawn(BoardCreator.cBoard.getSquare(square.x, square.y - 1), color);
				options.add(opt);
			}
			if (square.x - 1 >= 0 && square.y - 1 >= 0) {
				if (isFullBishop(BoardCreator.cBoard.getSquare(square.x - 1, square.y - 1))) {
					options.add(create(BoardCreator.cBoard.getSquare(square.x - 1, square.y - 1), color));
				}
			}
			if (square.x + 1 < ChessBoard.BOARD_SIZE && square.y - 1 >= 0) {
				if (isFullBishop(BoardCreator.cBoard.getSquare(square.x + 1, square.y - 1))) {
					options.add(create(BoardCreator.cBoard.getSquare(square.x + 1, square.y - 1), color));
				}
			}
		}
	}

	boolean isFullBishop(Square xs) {
		// PieceColor px = (color == PieceColor.white) ? PieceColor.black :
		// PieceColor.white;
		if (xs.piece != null) {
			return true;
		} else if (isValidMoveOn) {
			return true;
		} else if (!isValidMoveOn) {
			return false;
		} else {
			return false;
		}
	}

	Option createPawn(Square xsquare, PieceColor pieceColor) {
		OpType xOpType;

		if (xsquare.piece == null) {
			xOpType = OpType.movedTo;
		} else {
			xOpType = OpType.notMovedTo;
		}
		return new Option(xsquare, xOpType);
	}

	public Piece pawnChange(Piece piece, PieceType type) {
		Piece newPiece = null;
		switch (type) {
		case rook:
			newPiece = new Rook(piece.square, piece.color);
			break;
		case knight:
			newPiece = new Knight(piece.square, piece.color);
			break;
		case bishop:
			newPiece = new Bishop(piece.square, piece.color);
			break;
		case queen:
			newPiece = new Queen(piece.square, piece.color);
			break;
		default:
			throw new NullPointerException("Geçersiz taş tipi");
		}

		return BoardCreator.cBoard.getSquare(piece.square.x, piece.square.y).piece = newPiece;
	}

	// 1: beyaz -1 2: siyah -1 3: beyaz 1 4: siyah 1
	public boolean[] canEnPassant() {
		boolean[] canpass = { false, false, false, false };
		if (BoardCreator.cBoard.boardTool.lastMoveType == PieceType.pawn) {
			if (BoardCreator.cBoard.boardTool.lastMoveColor != color) {
				if (Math.abs(BoardCreator.cBoard.boardTool.lastMovePiece.y
						- BoardCreator.cBoard.boardTool.lastMoveSquare.y) == 2) {
					if (square.y == BoardCreator.cBoard.boardTool.lastMoveSquare.y) {
						if (square.x - BoardCreator.cBoard.boardTool.lastMovePiece.x == 1) {
							if (color == PieceColor.white) {
								canpass[0] = true;
							} else {
								canpass[1] = true;
							}
						} else if (square.x - BoardCreator.cBoard.boardTool.lastMovePiece.x == -1) {
							if (color == PieceColor.white) {
								canpass[2] = true;
							} else {
								canpass[3] = true;
							}
						}
					}
				}
			}
		}
		return canpass;
	}

	public void eatEnPassant(Square passSquare) {
		boolean[] cPass = canEnPassant();
		if (cPass[0]) {
			if (passSquare.x == square.x - 1 && passSquare.y == square.y + 1) {
				BoardCreator.cBoard.getSquare(square.x - 1, square.y).piece = null;
			}
		} else if (cPass[1]) {
			if (passSquare.x == square.x - 1 && passSquare.y == square.y - 1) {
				BoardCreator.cBoard.getSquare(square.x - 1, square.y).piece = null;
			}
		} else if (cPass[2]) {
			if (passSquare.x == square.x + 1 && passSquare.y == square.y + 1) {
				BoardCreator.cBoard.getSquare(square.x + 1, square.y).piece = null;
			}
		} else if (cPass[3]) {
			if (passSquare.x == square.x + 1 && passSquare.y == square.y - 1) {
				BoardCreator.cBoard.getSquare(square.x + 1, square.y).piece = null;
			}
		}
	}

	public Square enPassantTarget() {
		boolean[] cPass = canEnPassant();
		if (cPass[0]) {
			return BoardCreator.cBoard.getSquare(square.x - 1, square.y + 1);
		} else if (cPass[1]) {
			return BoardCreator.cBoard.getSquare(square.x - 1, square.y - 1);
		} else if (cPass[2]) {
			return BoardCreator.cBoard.getSquare(square.x + 1, square.y + 1);
		} else if (cPass[3]) {
			return BoardCreator.cBoard.getSquare(square.x + 1, square.y - 1);
		}
		return null;
	}
}
