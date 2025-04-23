package mini_max;

import java.security.SecureRandom;
import java.util.ArrayList;

import board.ChessBoard;
import database.SaveMatches;
import mm_piece.MMKing;
import mm_piece.MMPawn;
import mm_piece.MMRook;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import option.OpType;
import option.Option;
import piece.Piece;

public class MMBoardTool {

	public static Piece currentPiece;

	MMChessBoard board;
	SecureRandom randomColor;
	public PieceColor userColor;
	public PieceColor queue;
	public int plyCount;
	public boolean isActiveDB = false;
	public PieceType promotionType = PieceType.knight;
	public Square lastMovePiece = null;
	public PieceType lastMoveType = null;
	public PieceColor lastMoveColor = null;
	public Square lastMoveSquare = null;
	public PieceType pawnPromotionDB = null;
	SaveMatches mController;

	public MMBoardTool(MMChessBoard board) {
		this.board = board;
		randomColor = new SecureRandom();
		userColor = randomColor.nextInt(1, 5) == 1 ? PieceColor.white : PieceColor.black;
		queue = PieceColor.white;
		plyCount = 1;
		if (isActiveDB == true) {
			mController = new SaveMatches();
		}
	}

	public void placePiece(Piece piece) {
		board.getSquare(piece.square.x, piece.square.y).piece = piece;
	}
	
	public void printBoardState() {
		for (int i = 7; i >= 0; i--) {
			for (int j = 0; j < 8; j++) {
				System.out.print(
						board.getSquare(j, i).piece != null ? board.getSquare(j, i).piece.toCharPiece() : ".");
			}
			System.out.println();
		}
		System.out.println("Queue: " + queue);
		System.out.println("Ply Count: "+ plyCount);
	}

	boolean isValidMove = false;
	Square tempSquare = null;
	public void move(Piece piece, int newX, int newY) {
		tempSquare = board.getSquare(piece.square.x, piece.square.y);
		for (Option opt : selectedPieceMove(piece)) {
			if (opt.opType == OpType.movedTo || opt.opType == OpType.take) {
				if (opt.xsqu.x == newX && opt.xsqu.y == newY) {
					isValidMove = true;
				}
			}
		}
		if (isValidMove) {
			if (piece instanceof MMKing) {
				if (((MMKing) piece).isFirstMove)
					((MMKing) piece).isCastling(piece, board.getSquare(newX, newY));
				((MMKing) piece).isFirstMove = false;
			} else if (piece instanceof MMRook) {
				((MMRook) piece).isFirstMove = false;
			} else if (piece instanceof MMPawn) {
				((MMPawn) piece).eatEnPassant(board.getSquare(newX, newY));
				if (piece.color == PieceColor.white) {
					if (newY == 7) {
						pawnPromotionDB = ((MMPawn) piece).pawnChange(piece, promotionType).type;
					}
				} else {
					if (newY == 0) {
						pawnPromotionDB = ((MMPawn) piece).pawnChange(piece, promotionType).type;
					}
				}
			}
			pawnPromotionDB = null;
			lastMovePiece = tempSquare;
			lastMoveSquare = board.getSquare(newX, newY);
			lastMoveType = piece.type;
			lastMoveColor = piece.color;
			board.setSquare(piece.square, newX, newY);
			if (isActiveDB == true) {
				mController.addMoveDB(piece, tempSquare, board.getSquare(newX, newY), pawnPromotionDB);
			}
			plyCount++;
			queue = queue == PieceColor.white ? PieceColor.black : PieceColor.white;
			isValidMove = false;
		}
	}

	public ArrayList<Option> selectedPieceMove(Piece piece) {
		currentPiece = piece;
		
		int checkersSize = board.ruleTool.kingAttacker(piece.color).size();
		ArrayList<Option> copyOptions = new ArrayList<Option>(piece.getOptions());
		copyOptions.removeIf(opt -> opt.opType == OpType.notMovedTo ||opt.opType == OpType.notTake);
		copyOptions.removeIf(option -> (board.ruleTool.isKingInCheck(piece.color)
				&& board.ruleTool.canKingEscape(piece.color)
				&& board.ruleTool.escMovesSquare.stream()
						.noneMatch(escSquare -> option.xsqu.equals(escSquare.square) && piece.equals(escSquare.piece)))
				|| (checkersSize > 1 && piece.type != PieceType.king));

		int pSquareSize = board.ruleTool.checkPathSquare(piece).size();
		
		//Sonradan ekleme emin değilim
		if(pSquareSize + checkersSize > 2 && piece.type != PieceType.king) {
			copyOptions.clear();
		}

		if (piece != null) {
			if (board.ruleTool.ifMoveKingInCheck(piece) != null) {
				if (pSquareSize <= 1) {
					copyOptions.removeIf(option -> !((option.opType == OpType.take
							&& option.xsqu.piece == board.ruleTool.eatablePiece)
							|| board.ruleTool.moveableSquares.contains(option.xsqu)));
				}
			}
		}

		if (piece instanceof MMPawn) {
			ArrayList<Piece> kAtt = board.ruleTool.kingAttacker(piece.color);
			if (kAtt.size() == 1) {
				boolean[] enPass = ((MMPawn) piece).canEnPassant();
				if (enPass[0]) {
					Piece enemy = kAtt.get(0);
					if (enemy.square.equals(board.getSquare(piece.square.x - 1, piece.square.y))) {
						copyOptions.add(new Option(
								board.getSquare(piece.square.x - 1, piece.square.y + 1), OpType.take));
					}
				} else if (enPass[1]) {
					Piece enemy = kAtt.get(0);
					if (enemy.square.equals(board.getSquare(piece.square.x - 1, piece.square.y))) {
						copyOptions.add(new Option(
								board.getSquare(piece.square.x - 1, piece.square.y - 1), OpType.take));
					}
				} else if (enPass[2]) {
					Piece enemy = kAtt.get(0);
					if (enemy.square.equals(board.getSquare(piece.square.x + 1, piece.square.y))) {
						copyOptions.add(new Option(
								board.getSquare(piece.square.x + 1, piece.square.y + 1), OpType.take));
					}
				} else if (enPass[3]) {
					Piece enemy = kAtt.get(0);
					if (enemy.square.equals(board.getSquare(piece.square.x + 1, piece.square.y))) {
						copyOptions.add(new Option(
								board.getSquare(piece.square.x + 1, piece.square.y - 1), OpType.take));
					}
				}
			}
		}

		return copyOptions;
	}

	public ArrayList<Square> getValidMoves(Piece validPiece) {
		ArrayList<Square> validMoves = new ArrayList<Square>();
		ArrayList<Square> otherValidMoves = new ArrayList<Square>();
		ArrayList<Square> inThreadValidMoves = new ArrayList<Square>();
		if (validPiece != null) {
			for (Option opt : selectedPieceMove(validPiece)) {
				if (opt.opType == OpType.movedTo || opt.opType == OpType.take) {
					validMoves.add(board.getSquare(opt.xsqu.x, opt.xsqu.y));
				}
			}
			for (int i = 0; i < ChessBoard.BOARD_SIZE; i++) {
				for (int j = 0; j < ChessBoard.BOARD_SIZE; j++) {
					if (board.getSquare(i, j).piece != null) {
						if (board.getSquare(i, j).piece.color != validPiece.color) {
							MMPawn.isValidMoveOn = true;
							MMKing.isValidMoveOn = true;
							for (Option opt : board.getSquare(i, j).piece.getOptions()) {
								if (opt.opType == OpType.movedTo || opt.opType == OpType.notTake) {
									if (board.getSquare(i, j).piece.type == PieceType.pawn) {
										if (board.getSquare(i, j).piece.square.x != opt.xsqu.x) {
											otherValidMoves.add(board.getSquare(opt.xsqu.x, opt.xsqu.y));
										}
									} else {
										otherValidMoves.add(board.getSquare(opt.xsqu.x, opt.xsqu.y));
									}
									MMPawn.isValidMoveOn = false;
									MMKing.isValidMoveOn = false;
								} else if (opt.opType == OpType.take) {
									if (board.getSquare(i, j).piece.type == PieceType.queen
											|| board.getSquare(i, j).piece.type == PieceType.rook
											|| board.getSquare(i, j).piece.type == PieceType.bishop)
										if (opt.xsqu.piece == validPiece) {
											int rx = (board.getSquare(i, j).piece.square.x - opt.xsqu.x)
													* -1;
											int ry = (board.getSquare(i, j).piece.square.y - opt.xsqu.y)
													* -1;
											int directionX = rx == 0 ? 0 : rx < 0 ? -1 : +1;
											int directionY = ry == 0 ? 0 : ry < 0 ? -1 : +1;

											for (int kx = 1; kx < ChessBoard.BOARD_SIZE; kx++) {
												for (int ky = 1; ky < ChessBoard.BOARD_SIZE; ky++) {
													if (opt.xsqu.x + kx * directionX < ChessBoard.BOARD_SIZE
															&& opt.xsqu.y + ky * directionY < ChessBoard.BOARD_SIZE
															&& opt.xsqu.x + kx * directionX >= 0
															&& opt.xsqu.y + ky * directionY >= 0) {
														if (validPiece.type != PieceType.knight) {
															otherValidMoves.add(board.getSquare(
																	opt.xsqu.x + kx * directionX,
																	opt.xsqu.y + ky * directionY));
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
			for (Square validMove : validMoves) {
				for (Square otherValidMove : otherValidMoves) {
					if (validMove.equals(otherValidMove) && !inThreadValidMoves.contains(validMove)) {
						inThreadValidMoves.add(otherValidMove);
					}
				}

			}
		}
		return inThreadValidMoves;
	}

	public ArrayList<Piece> getThreateningPiece(Piece xpiece) {
		ArrayList<Piece> threateningPieces = new ArrayList<Piece>();
		for (int i = 0; i < ChessBoard.BOARD_SIZE; i++) {
			for (int j = 0; j < ChessBoard.BOARD_SIZE; j++) {
				if (board.getSquare(i, j).piece != null) {
					if (board.getSquare(i, j).piece.color != xpiece.color) {
						for (Option opt : board.getSquare(i, j).piece.getOptions()) {
							if (opt.opType == OpType.take) {
								if (board.getSquare(opt.xsqu.x, opt.xsqu.y).piece == xpiece) {
									threateningPieces.add(board.getSquare(
											board.getSquare(i, j).piece.square.x,
											board.getSquare(i, j).piece.square.y).piece);
								}
							}
						}
					}
				}
			}
		}
		return threateningPieces;
	}

	public ArrayList<Piece> getProtectingPiece(Piece xpiece) {
		ArrayList<Piece> protectingPieces = new ArrayList<Piece>();
		MMPawn.isValidMoveOn = true;
		for (int i = 0; i < ChessBoard.BOARD_SIZE; i++) {
			for (int j = 0; j < ChessBoard.BOARD_SIZE; j++) {
				if (board.getSquare(i, j).piece != null) {
					if (board.getSquare(i, j).piece.color == xpiece.color) {
						for (Option opt : board.getSquare(i, j).piece.getOptions()) {
							if (opt.opType == OpType.notTake) {
								if (board.getSquare(opt.xsqu.x, opt.xsqu.y).piece == xpiece) {
									protectingPieces.add(board.getSquare(
											board.getSquare(i, j).piece.square.x,
											board.getSquare(i, j).piece.square.y).piece);
								}
							}
						}
					}
				}
			}
		}
		MMPawn.isValidMoveOn = false;
		return protectingPieces;
	}
}
