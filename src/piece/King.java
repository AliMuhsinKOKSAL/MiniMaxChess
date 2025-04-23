package piece;

import board.ChessBoard;

import java.util.ArrayList;

import board.BoardCreator;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import option.OpType;
import option.Option;

public class King extends Piece {

	public boolean isFirstMove = true;
	public static boolean isValidMoveOn = false;

	public King(Square square, PieceColor cColor) {
		super(BoardCreator.cBoard.getSquare(square.x, square.y), PieceType.king, cColor);
		BoardCreator.cBoard.boardTool.placePiece(this);
	}

	@Override
	public void getMovement() {
		options.clear();
		int[][] movei = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 1 }, { -1, -1 }, { -1, 1 }, { 1, -1 } };

		for (int[] move : movei) {
			int newx = square.x + move[0];
			int newy = square.y + move[1];
			if (newx >= 0 && newx < ChessBoard.BOARD_SIZE && newy >= 0 && newy < ChessBoard.BOARD_SIZE) {
				Option op = create(BoardCreator.cBoard.getSquare(newx, newy), color);
				if (!isCheck(BoardCreator.cBoard.getSquare(newx, newy)))
					options.add(op);
			}
		}
		if (canCastleLong()) {
			if (color == PieceColor.white) {
				if (!isCheck(BoardCreator.cBoard.getSquare(2, 0)) && !isKingInCheck()) {
					options.add(create(BoardCreator.cBoard.getSquare(2, 0), color));
				}
			} else {
				if (!isCheck(BoardCreator.cBoard.getSquare(2, 7)) && !isKingInCheck()) {
					options.add(create(BoardCreator.cBoard.getSquare(2, 7), color));
				}
			}
		}
		if (canCastleShort()) {
			if (color == PieceColor.white) {
				if (!isCheck(BoardCreator.cBoard.getSquare(6, 0)) && !isKingInCheck()) {
					options.add(create(BoardCreator.cBoard.getSquare(6, 0), color));
				}
			} else {
				if (!isCheck(BoardCreator.cBoard.getSquare(6, 7)) && !isKingInCheck()) {
					options.add(create(BoardCreator.cBoard.getSquare(6, 7), color));
				}
			}
		}
	}

	boolean isCheck(Square square) {
		if (King.isValidMoveOn == false) {
			for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
				for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
					if (BoardCreator.cBoard.getSquare(k, l).piece != null) {
						if (BoardCreator.cBoard.getSquare(k, l).piece.color != color) {
							Pawn.isValidMoveOn = true;
							if (BoardCreator.cBoard.getSquare(k, l).piece.type != PieceType.king) {
								for (Option opt : BoardCreator.cBoard.getSquare(k, l).piece.getOptions()) {
									if (BoardCreator.cBoard.getSquare(k, l).piece.type != PieceType.pawn) {
										if (opt.xsqu == square) {
											Pawn.isValidMoveOn = false;
											return true;
										} else if (opt.xsqu.piece == this) {
											if (BoardCreator.cBoard.getSquare(k, l).piece.type == PieceType.queen
													|| BoardCreator.cBoard.getSquare(k, l).piece.type == PieceType.rook
													|| BoardCreator.cBoard.getSquare(k, l).piece.type == PieceType.bishop) {

												int rx = (BoardCreator.cBoard.getSquare(k, l).piece.square.x
														- opt.xsqu.x) * -1;
												int ry = (BoardCreator.cBoard.getSquare(k, l).piece.square.y
														- opt.xsqu.y) * -1;
												int directionX = rx == 0 ? 0 : rx < 0 ? -1 : +1;
												int directionY = ry == 0 ? 0 : ry < 0 ? -1 : +1;

												for (int kx = 1; kx < ChessBoard.BOARD_SIZE; kx++) {
													for (int ky = 1; ky < ChessBoard.BOARD_SIZE; ky++) {
														if (opt.xsqu.x + kx * directionX < ChessBoard.BOARD_SIZE
																&& opt.xsqu.y + ky * directionY < ChessBoard.BOARD_SIZE
																&& opt.xsqu.x + kx * directionX >= 0
																&& opt.xsqu.y + ky * directionY >= 0) {
															if (square == BoardCreator.cBoard.getSquare(
																	opt.xsqu.x + kx * directionX,
																	opt.xsqu.y + ky * directionY)) {
																return true;
															}
														}
													}
												}
											}
										}
										Pawn.isValidMoveOn = false;
									} else {
										if (BoardCreator.cBoard.getSquare(k, l).piece.square.x != opt.xsqu.x) {
											if (opt.xsqu == square) {
												Pawn.isValidMoveOn = false;
												return true;
											}
										}
									}
								}
							} else {
								ArrayList<Square> copyKOpt = new ArrayList<Square>();

								int[][] movei = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 1 }, { -1, -1 },
										{ -1, 1 }, { 1, -1 } };

								for (int[] move : movei) {
									int newx = BoardCreator.cBoard.getSquare(k, l).piece.square.x + move[0];
									int newy = BoardCreator.cBoard.getSquare(k, l).piece.square.y + move[1];
									if (newx >= 0 && newx < ChessBoard.BOARD_SIZE && newy >= 0
											&& newy < ChessBoard.BOARD_SIZE) {
										copyKOpt.add(BoardCreator.cBoard.getSquare(newx, newy));
									}
								}
								for (Square oKingOpt : copyKOpt) {
									if (oKingOpt == square) {
										Pawn.isValidMoveOn = false;
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		Pawn.isValidMoveOn = false;
		return false;
	}

	public boolean canCastleLong() {
		if (color == PieceColor.white) {
			if (BoardCreator.cBoard.getSquare(0, 0).piece != null) {
				if (BoardCreator.cBoard.getSquare(0, 0).piece.type == PieceType.rook)
					if (((Rook) BoardCreator.cBoard.getSquare(0, 0).piece).isFirstMove) {
						if (isFirstMove) {
							for (int i = 3; i > 1; i--) {
								for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
									for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
										if (BoardCreator.cBoard.getSquare(k, l).piece != null) {
											if (BoardCreator.cBoard.getSquare(k, l).piece.color != color) {
												if (BoardCreator.cBoard.getSquare(k, l).piece.type != PieceType.king) {
													for (Option opt : BoardCreator.cBoard.getSquare(k, l).piece
															.getOptions()) {
														if (opt.xsqu == BoardCreator.cBoard.getSquare(i, 0)) {
															return false;
														}
													}
												} else {
													for (Option opt : BoardCreator.cBoard.getSquare(k,
															l).piece.options) {
														if (opt.xsqu == BoardCreator.cBoard.getSquare(i, 0)) {
															return false;
														}
													}
												}
											}
										}
									}
								}
								if (BoardCreator.cBoard.getSquare(i, 0).piece != null) {
									return false;
								}
							}
							return true;
						}
					}
			}
		} else {
			if (BoardCreator.cBoard.getSquare(0, 7).piece != null) {
				if (BoardCreator.cBoard.getSquare(0, 7).piece.type == PieceType.rook)
					if (((Rook) BoardCreator.cBoard.getSquare(0, 7).piece).isFirstMove) {
						if (isFirstMove) {
							for (int i = 3; i > 1; i--) {
								for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
									for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
										if (BoardCreator.cBoard.getSquare(k, l).piece != null) {
											if (BoardCreator.cBoard.getSquare(k, l).piece.color != color) {
												if (BoardCreator.cBoard.getSquare(k, l).piece.type != PieceType.king) {
													for (Option opt : BoardCreator.cBoard.getSquare(k, l).piece
															.getOptions()) {
														if (opt.xsqu == BoardCreator.cBoard.getSquare(i, 7)) {
															return false;
														}
													}
												} else {
													for (Option opt : BoardCreator.cBoard.getSquare(k,
															l).piece.options) {
														if (opt.xsqu == BoardCreator.cBoard.getSquare(i, 7)) {
															return false;
														}
													}
												}
											}
										}
									}
								}
								if (BoardCreator.cBoard.getSquare(i, 7).piece != null) {
									return false;
								}
							}
							return true;
						}
					}
			}
		}
		return false;
	}

	public boolean canCastleShort() {
		if (color == PieceColor.white) {
			if (BoardCreator.cBoard.getSquare(7, 0).piece != null) {
				if (BoardCreator.cBoard.getSquare(7, 0).piece.type == PieceType.rook)
					if (((Rook) BoardCreator.cBoard.getSquare(7, 0).piece).isFirstMove) {
						for (int i = 5; i < 7; i++) {
							for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
								for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
									if (BoardCreator.cBoard.getSquare(k, l).piece != null) {
										if (BoardCreator.cBoard.getSquare(k, l).piece.color != color) {
											if (BoardCreator.cBoard.getSquare(k, l).piece.type != PieceType.king) {
												for (Option opt : BoardCreator.cBoard.getSquare(k, l).piece
														.getOptions()) {
													if (opt.xsqu == BoardCreator.cBoard.getSquare(i, 0)) {
														return false;
													}
												}
											} else {
												for (Option opt : BoardCreator.cBoard.getSquare(k, l).piece.options) {
													if (opt.xsqu == BoardCreator.cBoard.getSquare(i, 0)) {
														return false;
													}
												}
											}
										}
									}
								}
							}
							if (BoardCreator.cBoard.getSquare(i, 0).piece != null) {
								return false;
							}
						}
						if (isFirstMove) {
							return true;
						}
					}
			}
		} else {
			if (BoardCreator.cBoard.getSquare(7, 7).piece != null) {
				if (BoardCreator.cBoard.getSquare(7, 7).piece.type == PieceType.rook)
					if (((Rook) BoardCreator.cBoard.getSquare(7, 7).piece).isFirstMove) {
						for (int i = 5; i < 7; i++) {
							for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
								for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
									if (BoardCreator.cBoard.getSquare(k, l).piece != null) {
										if (BoardCreator.cBoard.getSquare(k, l).piece.color != color) {
											if (BoardCreator.cBoard.getSquare(k, l).piece.type != PieceType.king) {
												for (Option opt : BoardCreator.cBoard.getSquare(k, l).piece
														.getOptions()) {
													if (opt.xsqu == BoardCreator.cBoard.getSquare(i, 7)) {
														return false;
													}
												}
											} else {
												for (Option opt : BoardCreator.cBoard.getSquare(k, l).piece.options) {
													if (opt.xsqu == BoardCreator.cBoard.getSquare(i, 7)) {
														return false;
													}
												}
											}
										}
									}
								}
							}
							if (BoardCreator.cBoard.getSquare(i, 7).piece != null) {
								return false;
							}
						}
						if (isFirstMove) {
							return true;
						}
					}
			}
		}
		return false;
	}

	public void isCastling(Piece king, Square square) {
		if (king.color == PieceColor.white) {
			if (square.equals(BoardCreator.cBoard.getSquare(2, 0))) {
				BoardCreator.cBoard.setSquare(BoardCreator.cBoard.getSquare(0, 0), 3, 0);
			} else if (square.equals(BoardCreator.cBoard.getSquare(6, 0))) {
				BoardCreator.cBoard.setSquare(BoardCreator.cBoard.getSquare(7, 0), 5, 0);
			}
		} else {
			if (square.equals(BoardCreator.cBoard.getSquare(2, 7))) {
				BoardCreator.cBoard.setSquare(BoardCreator.cBoard.getSquare(0, 7), 3, 7);
			} else if (square.equals(BoardCreator.cBoard.getSquare(6, 7))) {
				BoardCreator.cBoard.setSquare(BoardCreator.cBoard.getSquare(7, 7), 5, 7);
			}
		}
	}

	public boolean isKingInCheck() {
		ArrayList<Piece> attackers = new ArrayList<Piece>();
		for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
			for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
				if (BoardCreator.cBoard.getSquare(k, l).piece != null) {
					if (BoardCreator.cBoard.getSquare(k, l).piece.color != this.color) {
						if (BoardCreator.cBoard.getSquare(k, l).piece.type != PieceType.king) {
							ArrayList<Option> optCopy = new ArrayList<Option>(
									BoardCreator.cBoard.getSquare(k, l).piece.getOptions());
							for (Option opt : optCopy) {
								if (opt.xsqu.piece == this) {
									if (opt.opType == OpType.take) {
										attackers.add(BoardCreator.cBoard.getSquare(k, l).piece);
									}
								}
							}
						} else {
							for (Option opt : this.options) {
								if (opt.xsqu.piece == this) {
									if (opt.opType == OpType.take) {
										attackers.add(BoardCreator.cBoard.getSquare(k, l).piece);
									}
								}
							}
						}
					}
				}
			}
		}
		for (Piece atts : attackers) {
			ArrayList<Option> optCopy = new ArrayList<Option>(atts.getOptions());
			for (Option opt : optCopy) {
				if (opt.xsqu.piece == this) {
					if (opt.opType == OpType.take) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
