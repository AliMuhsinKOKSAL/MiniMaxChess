package mini_max;

import java.util.ArrayList;
import java.util.Iterator;

import board.ChessBoard;
import mm_piece.MMPawn;
import obj.EscapeMoveSquare;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import option.OpType;
import option.Option;
import piece.Piece;

public class MMGameRuleTool {

	MMChessBoard board;
	public Piece eatablePiece;
	public ArrayList<Square> moveableSquares;
	ArrayList<EscapeMoveSquare> escMovesSquare;

	public MMGameRuleTool(MMChessBoard board) {
		this.board = board;
		moveableSquares = new ArrayList<Square>();
		escMovesSquare = new ArrayList<EscapeMoveSquare>();
	}

	public Piece findKing(PieceColor color) {
		for (int i = 0; i < ChessBoard.BOARD_SIZE; i++) {
			for (int j = 0; j < ChessBoard.BOARD_SIZE; j++) {
				if (board.getSquare(i, j).piece != null) {
					if (board.getSquare(i, j).piece.type == PieceType.king) {
						if (board.getSquare(i, j).piece.color == color) {
							return board.getSquare(i, j).piece;
						}
					}
				}
			}
		}
		return null;
	}

	public ArrayList<Piece> kingAttacker(PieceColor color) {
		ArrayList<Piece> attackers = new ArrayList<Piece>();
		Piece king = findKing(color);
		if (king != null) {
			for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
				for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
					if (board.getSquare(k, l).piece != null) {
						if (board.getSquare(k, l).piece.color != king.color) {
							ArrayList<Option> optCopy = new ArrayList<Option>(
									board.getSquare(k, l).piece.getOptions());
							for (Option opt : optCopy) {
								if (opt.xsqu.piece == king) {
									if (opt.opType == OpType.take) {
										attackers.add(board.getSquare(k, l).piece);
									}
								}
							}
						}
					}
				}
			}
		}
		return attackers;
	}

	public ArrayList<Piece> kingsAllyPiece(PieceColor color) {
		ArrayList<Piece> kingsPiece = new ArrayList<Piece>();
		for (int i = 0; i < ChessBoard.BOARD_SIZE; i++) {
			for (int j = 0; j < ChessBoard.BOARD_SIZE; j++) {
				if (board.getSquare(i, j).piece != null) {
					if (board.getSquare(i, j).piece.color == color) {
						kingsPiece.add(board.getSquare(i, j).piece);
					}
				}
			}
		}
		return kingsPiece;
	}

	public boolean canKingEscape(PieceColor color) {
		escMovesSquare.clear();
		Piece king = findKing(color);
		boolean canEsc = false;
		if (king != null) {
			ArrayList<Option> copyOptions = new ArrayList<Option>(king.getOptions());
			Iterator<Option> iterator = copyOptions.iterator();
			while (iterator.hasNext()) {
				Option opt = iterator.next();
				if (opt.opType == OpType.notTake || opt.opType == OpType.notMovedTo) {
					iterator.remove();
				}
			}

			if (copyOptions.size() != 0) {
				for (Option opt : copyOptions) {
					escMovesSquare.add(new EscapeMoveSquare(king, opt.xsqu));
				}
				canEsc = true;
			}

			for (Option opt : copyOptions) {
				for (Piece attacker : kingAttacker(color)) {
					if (opt.xsqu == attacker.square) {
						escMovesSquare.add(new EscapeMoveSquare(king, opt.xsqu));
						canEsc = true;
					}
				}
			}

			for (Piece attacker : kingAttacker(color)) {
				for (Piece allyPiece : kingsAllyPiece(color)) {
					for (Option opt : allyPiece.getOptions()) {
						if (allyPiece.type != PieceType.pawn) {
							if (opt.xsqu == attacker.square) {
								escMovesSquare.add(new EscapeMoveSquare(allyPiece, opt.xsqu));
								MMPawn.isValidMoveOn = false;
								canEsc = true;
							}
						} else {
							if (allyPiece.square.x != opt.xsqu.x) {
								MMPawn.isValidMoveOn = true;
								if (opt.xsqu == attacker.square) {
									escMovesSquare.add(new EscapeMoveSquare(allyPiece, opt.xsqu));
									MMPawn.isValidMoveOn = false;
									canEsc = true;
								}
							}
						}
					}
				}
				MMPawn.isValidMoveOn = false;
			}

			ArrayList<Square> aList = new ArrayList<Square>();
			for (Piece attacker : kingAttacker(color)) {
				int rx = attacker.square.x - king.square.x;
				int ry = attacker.square.y - king.square.y;

				int directionX = rx == 0 ? 0 : rx < 0 ? -1 : +1;
				int directionY = ry == 0 ? 0 : ry < 0 ? -1 : +1;

				for (int step = 1; step < Math.max(Math.abs(rx), Math.abs(ry)); step++) {
					int currentX = king.square.x + directionX * step;
					int currentY = king.square.y + directionY * step;

					if (attacker.type == PieceType.bishop || attacker.type == PieceType.rook
							|| attacker.type == PieceType.queen)
						aList.add(board.getSquare(currentX, currentY));
				}
			}

			for (Piece allyPiece : kingsAllyPiece(color)) {
				for (Option opt : allyPiece.getOptions()) {
					for (Square threadSquare : aList) {
						if (opt.xsqu.equals(threadSquare)) {
							escMovesSquare.add(new EscapeMoveSquare(allyPiece, threadSquare));
							canEsc = true;
						}
					}
				}
			}

		}
		return canEsc;
	}

	public boolean isKingInCheck(PieceColor color) {
		Piece king = findKing(color);
		if (king != null) {
			for (Piece attackers : kingAttacker(color)) {
				ArrayList<Option> optCopy = new ArrayList<Option>(attackers.getOptions());
				for (Option opt : optCopy) {
					if (opt.xsqu.piece == king) {
						if (opt.opType == OpType.take) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isStalemate(PieceColor color) {
		ArrayList<Boolean> allAllyPieceHaveMove = new ArrayList<Boolean>();
		if (!isKingInCheck(color)) {
			for (Piece allyPiece : kingsAllyPiece(color)) {
				ArrayList<Option> copyOptions = new ArrayList<Option>(allyPiece.getOptions());
				copyOptions.removeIf(opt -> opt.opType == OpType.notTake || opt.opType == OpType.notMovedTo);
				allAllyPieceHaveMove.add(copyOptions.size() == 0);
			}

			if (!allAllyPieceHaveMove.contains(false)) {
				return true;
			}
		}
		return false;
	}

	public String whoIsCheckMate() {
		if (isKingInCheck(PieceColor.white)) {
			if (!canKingEscape(PieceColor.white)) {
				return "0-1";
			}
		} else if (isKingInCheck(PieceColor.black)) {
			if (!canKingEscape(PieceColor.black)) {
				return "1-0";
			}
		} else if (isStalemate(PieceColor.white) || isStalemate(PieceColor.black)) {
			return "1/2-1/2";
		}
		return (board.boardTool.userColor == PieceColor.white) ? "0-1" : "1-0";
	}

	public boolean isCheckMate() {
		final String RESET = "\033[0m";
		final String GREEN = "\033[32m";
		final String BLUE = "\033[34m";
		if (isKingInCheck(PieceColor.white)) {
			if (canKingEscape(PieceColor.white)) {
				System.out.println(GREEN + "---------------------");
				System.out.println("Check white" + RESET);
				return false;
			} else if (!canKingEscape(PieceColor.white)) {
				System.err.println("---------------------");
				System.err.println("Check MAte white");
				return true;
			}
		}
		if (isKingInCheck(PieceColor.black)) {
			if (canKingEscape(PieceColor.black)) {
				System.out.println(GREEN + "---------------------");
				System.out.println("Check black" + RESET);
				return false;
			} else if (!canKingEscape(PieceColor.black)) {
				System.err.println("---------------------");
				System.err.println("Check MAte black");
				return true;
			}
		}

		else if (isStalemate(PieceColor.white)) {
			System.out.println(BLUE + "---------------------");
			System.out.println("Stale MAte" + RESET);
		} else if (isStalemate(PieceColor.black)) {
			System.out.println(BLUE + "---------------------");
			System.out.println("Stale MAte" + RESET);
		}
		return false;
	}

	public ArrayList<Piece> pieceAttacker(Piece piece) {
		ArrayList<Piece> attackers = new ArrayList<Piece>();
		for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
			for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
				if (board.getSquare(k, l).piece != null) {
					if (board.getSquare(k, l).piece.color != piece.color) {
						ArrayList<Option> optCopy = new ArrayList<Option>(
								board.getSquare(k, l).piece.getOptions());
						for (Option opt : optCopy) {
							if (opt.xsqu.piece == piece) {
								if (opt.opType == OpType.take) {
									attackers.add(board.getSquare(k, l).piece);
								}
							}
						}
					}
				}
			}
		}
		return attackers;
	}

	public Piece ifMoveKingInCheck(Piece currentPiece) {
		moveableSquares.clear();
		for (int i = 0; i < ChessBoard.BOARD_SIZE; i++) {
			for (int j = 0; j < ChessBoard.BOARD_SIZE; j++) {
				if (board.getSquare(i, j).piece != null) {
					if (board.getSquare(i, j).piece.color != currentPiece.color) {
						MMPawn.isValidMoveOn = true;
						for (Option opt : board.getSquare(i, j).piece.getOptions()) {
							if (opt.xsqu.piece == currentPiece) {
								if (opt.opType == OpType.take) {
									if (board.getSquare(i, j).piece.type == PieceType.queen
											|| board.getSquare(i, j).piece.type == PieceType.rook
											|| board.getSquare(i, j).piece.type == PieceType.bishop) {
										int rx = (board.getSquare(i, j).piece.square.x - opt.xsqu.x) * -1;
										int ry = (board.getSquare(i, j).piece.square.y - opt.xsqu.y) * -1;
										int directionX = rx == 0 ? 0 : rx < 0 ? -1 : +1;
										int directionY = ry == 0 ? 0 : ry < 0 ? -1 : +1;

										for (int kx = 1; kx < ChessBoard.BOARD_SIZE; kx++) {
											if (i + kx * directionX < ChessBoard.BOARD_SIZE
													&& j + kx * directionY < ChessBoard.BOARD_SIZE
													&& i + kx * directionX >= 0 && j + kx * directionY >= 0) {
												moveableSquares.add(board.getSquare(i + kx * directionX,
														j + kx * directionY));
											}
										}

										for (int kx = 1; kx < ChessBoard.BOARD_SIZE; kx++) {
											if (opt.xsqu.x + kx * directionX < ChessBoard.BOARD_SIZE
													&& opt.xsqu.y + kx * directionY < ChessBoard.BOARD_SIZE
													&& opt.xsqu.x + kx * directionX >= 0
													&& opt.xsqu.y + kx * directionY >= 0) {
												if (board.getSquare(opt.xsqu.x + kx * directionX,
														opt.xsqu.y + kx * directionY).piece != null) {
													if (board.getSquare(opt.xsqu.x + kx * directionX,
															opt.xsqu.y + kx
																	* directionY).piece.color == currentPiece.color) {
														if (board.getSquare(opt.xsqu.x + kx * directionX,
																opt.xsqu.y + kx
																		* directionY).piece.type == PieceType.king) {
															eatablePiece = board.getSquare(i, j).piece;
															// System.out.println(board.getSquare(i,
															// j).piece);
															return opt.xsqu.piece;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		MMPawn.isValidMoveOn = false;
		return null;
	}

	public ArrayList<Piece> checkPathSquare(Piece currentPiece) {
		ArrayList<Piece> cPathSquare = new ArrayList<Piece>();
		for (int i = 0; i < ChessBoard.BOARD_SIZE; i++) {
			for (int j = 0; j < ChessBoard.BOARD_SIZE; j++) {
				if (board.getSquare(i, j).piece != null) {
					if (board.getSquare(i, j).piece.color != currentPiece.color) {
						MMPawn.isValidMoveOn = true;
						for (Option opt : board.getSquare(i, j).piece.getOptions()) {
							if (opt.xsqu.piece == currentPiece) {
								if (opt.opType == OpType.take) {
									if (board.getSquare(i, j).piece.type == PieceType.queen
											|| board.getSquare(i, j).piece.type == PieceType.rook
											|| board.getSquare(i, j).piece.type == PieceType.bishop) {
										int rx = (board.getSquare(i, j).piece.square.x - opt.xsqu.x) * -1;
										int ry = (board.getSquare(i, j).piece.square.y - opt.xsqu.y) * -1;
										int directionX = rx == 0 ? 0 : rx < 0 ? -1 : +1;
										int directionY = ry == 0 ? 0 : ry < 0 ? -1 : +1;

										for (int kx = 1; kx < ChessBoard.BOARD_SIZE; kx++) {
											if (opt.xsqu.x + kx * directionX < ChessBoard.BOARD_SIZE
													&& opt.xsqu.y + kx * directionY < ChessBoard.BOARD_SIZE
													&& opt.xsqu.x + kx * directionX >= 0
													&& opt.xsqu.y + kx * directionY >= 0) {
												if (board.getSquare(opt.xsqu.x + kx * directionX,
														opt.xsqu.y + kx * directionY).piece != null) {
													cPathSquare.add(
															board.getSquare(opt.xsqu.x + kx * directionX,
																	opt.xsqu.y + kx * directionY).piece);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		MMPawn.isValidMoveOn = false;
		return cPathSquare;
	}

}
