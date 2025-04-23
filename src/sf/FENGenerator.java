package sf;

import board.BoardCreator;
import board.ChessBoard;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;

public class FENGenerator {

	public String generateFEN() {
		StringBuilder fen = new StringBuilder("");
		for (int i = ChessBoard.BOARD_SIZE - 1; i >= 0; i--) {
			int counter = 0;
			for (int j = 0; j < ChessBoard.BOARD_SIZE; j++) {
				if (BoardCreator.cBoard.getSquare(j, i).piece == null) {
					counter++;
				} else {
					if (counter != 0) {
						fen.append(counter);
						counter = 0;
					}
					fen.append(BoardCreator.cBoard.getSquare(j, i).piece.toCharPiece());
				}
			}
			if (counter != 0) {
				fen.append(counter);
			}
			if (i != 0) {
				fen.append("/");
			}
		}
		String strQueue = (BoardCreator.cBoard.boardTool.queue == PieceColor.white) ? "w" : "b";

		String[] castling = castling();

		String allcast = castling[0] + castling[1] + castling[2] + castling[3];
		fen.append(" " + strQueue + " " + allcast + " "+strEnPass()+" " + BoardCreator.cBoard.boardTool.plyCount + " "
				+ (Math.floorDiv(BoardCreator.cBoard.boardTool.plyCount, 2) + 1));
//		System.out.println(fen);
		return fen.toString();
	}
	
	public String strEnPass() {
		for(int i = 0;i<ChessBoard.BOARD_SIZE;i++) {
			for(int j = 0;j<ChessBoard.BOARD_SIZE;j++) {
				if(BoardCreator.cBoard.getSquare(i, j).piece instanceof piece.Pawn) {
					Square target = ((piece.Pawn)BoardCreator.cBoard.getSquare(i, j).piece).enPassantTarget();
					if(target!=null) {
						return chessXNot(target.x)+""+(target.y+1);
					}
				}
			}
		}
		return "-";
	}
	
	public char chessXNot(int intX) {
		char x;
		switch (intX) {
		case 0:
			x = 'a';
			break;
		case 1:
			x = 'b';
			break;
		case 2:
			x = 'c';
			break;
		case 3:
			x = 'd';
			break;
		case 4:
			x = 'e';
			break;
		case 5:
			x = 'f';
			break;
		case 6:
			x = 'g';
			break;
		case 7:
			x = 'h';
			break;
		default:
			throw new IllegalStateException();
		}
		return x;
	}

	// 0: w short| 1: w long| 2: b short| 3: b long
	String[] castling() {
		boolean[] castling = { false, false, false, false };
		if (BoardCreator.cBoard.getSquare(4, 0).piece != null) {
			if (BoardCreator.cBoard.getSquare(4, 0).piece.color == PieceColor.white) {
				if (BoardCreator.cBoard.getSquare(4, 0).piece.type == PieceType.king) {
					if (BoardCreator.cBoard.getSquare(7, 0).piece != null) {
						if (BoardCreator.cBoard.getSquare(7, 0).piece.color == PieceColor.white) {
							if (BoardCreator.cBoard.getSquare(7, 0).piece.type == PieceType.rook) {
								castling[0] = true;
							}
						}
					}
					if (BoardCreator.cBoard.getSquare(0, 0).piece != null) {
						if (BoardCreator.cBoard.getSquare(0, 0).piece.color == PieceColor.white) {
							if (BoardCreator.cBoard.getSquare(0, 0).piece.type == PieceType.rook) {
								castling[1] = true;
							}
						}
					}
				}
			}
		}
		if (BoardCreator.cBoard.getSquare(4, 7).piece != null) {
			if (BoardCreator.cBoard.getSquare(4, 7).piece.color == PieceColor.black) {
				if (BoardCreator.cBoard.getSquare(4, 7).piece.type == PieceType.king) {
					if (BoardCreator.cBoard.getSquare(7, 7).piece != null) {
						if (BoardCreator.cBoard.getSquare(7, 7).piece.color == PieceColor.black) {
							if (BoardCreator.cBoard.getSquare(7, 7).piece.type == PieceType.rook) {
								castling[2] = true;
							}
						}
					}
					if (BoardCreator.cBoard.getSquare(0, 7).piece != null) {
						if (BoardCreator.cBoard.getSquare(0, 7).piece.color == PieceColor.black) {
							if (BoardCreator.cBoard.getSquare(0, 7).piece.type == PieceType.rook) {
								castling[3] = true;
							}
						}
					}
				}
			}
		}
		String[] strCastling = { "", "", "", "" };
		if (castling[0] == true) {
			strCastling[0] = "K";
		}
		if (castling[1] == true) {
			strCastling[1] = "Q";
		}
		if (castling[2] == true) {
			strCastling[2] = "k";
		}
		if (castling[3] == true) {
			strCastling[3] = "q";
		}else {
			strCastling[0] = "-";
		}
		return strCastling;
	}
}
