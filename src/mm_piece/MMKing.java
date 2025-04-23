package mm_piece;

import board.ChessBoard;
import mini_max.MMChessBoard;

import java.util.ArrayList;

import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import option.OpType;
import option.Option;
import piece.Piece;

public class MMKing extends Piece {

	MMChessBoard board;
	public boolean isFirstMove = true;
	public static boolean isValidMoveOn = false;

	public MMKing(Square square, PieceColor cColor, MMChessBoard board) {
		super(board.getSquare(square.x, square.y), PieceType.king, cColor);
		this.board = board;
	}

	@Override
	public void getMovement() {
		options.clear();
		int[][] movei = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 1 }, { -1, -1 }, { -1, 1 }, { 1, -1 } };

		for (int[] move : movei) {
			int newx = square.x + move[0];
			int newy = square.y + move[1];
			if (newx >= 0 && newx < ChessBoard.BOARD_SIZE && newy >= 0 && newy < ChessBoard.BOARD_SIZE) {
				Option op = create(board.getSquare(newx, newy), color);
				if (!isCheck(board.getSquare(newx, newy)))
					options.add(op);
			}
		}
		if (canCastleLong()) {
			if (color == PieceColor.white) {
				if (!isCheck(board.getSquare(2, 0)) && !isKingInCheck()) {
					options.add(create(board.getSquare(2, 0), color));
				}
			} else {
				if (!isCheck(board.getSquare(2, 7)) && !isKingInCheck()) {
					options.add(create(board.getSquare(2, 7), color));
				}
			}
		}
		if (canCastleShort()) {
			if (color == PieceColor.white) {
				if (!isCheck(board.getSquare(6, 0)) && !isKingInCheck()) {
					options.add(create(board.getSquare(6, 0), color));
				}
			} else {
				if (!isCheck(board.getSquare(6, 7)) && !isKingInCheck()) {
					options.add(create(board.getSquare(6, 7), color));
				}
			}
		}
	}

	boolean isCheck(Square square) {
		if (MMKing.isValidMoveOn == false) {
			for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
				for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
					if (board.getSquare(k, l).piece != null) {
						if (board.getSquare(k, l).piece.color != color) {
							MMPawn.isValidMoveOn = true;
							if (board.getSquare(k, l).piece.type != PieceType.king) {
								for (Option opt : board.getSquare(k, l).piece.getOptions()) {
									if (board.getSquare(k, l).piece.type != PieceType.pawn) {
										if (opt.xsqu == square) {
											MMPawn.isValidMoveOn = false;
											return true;
										} else if (opt.xsqu.piece == this) {
											if (board.getSquare(k, l).piece.type == PieceType.queen
													|| board.getSquare(k, l).piece.type == PieceType.rook
													|| board.getSquare(k, l).piece.type == PieceType.bishop) {

												int rx = (board.getSquare(k, l).piece.square.x
														- opt.xsqu.x) * -1;
												int ry = (board.getSquare(k, l).piece.square.y
														- opt.xsqu.y) * -1;
												int directionX = rx == 0 ? 0 : rx < 0 ? -1 : +1;
												int directionY = ry == 0 ? 0 : ry < 0 ? -1 : +1;

												for (int kx = 1; kx < ChessBoard.BOARD_SIZE; kx++) {
													for (int ky = 1; ky < ChessBoard.BOARD_SIZE; ky++) {
														if (opt.xsqu.x + kx * directionX < ChessBoard.BOARD_SIZE
																&& opt.xsqu.y + ky * directionY < ChessBoard.BOARD_SIZE
																&& opt.xsqu.x + kx * directionX >= 0
																&& opt.xsqu.y + ky * directionY >= 0) {
															if (square == board.getSquare(
																	opt.xsqu.x + kx * directionX,
																	opt.xsqu.y + ky * directionY)) {
																return true;
															}
														}
													}
												}
											}
										}
										MMPawn.isValidMoveOn = false;
									} else {
										if (board.getSquare(k, l).piece.square.x != opt.xsqu.x) {
											if (opt.xsqu == square) {
												MMPawn.isValidMoveOn = false;
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
									int newx = board.getSquare(k, l).piece.square.x + move[0];
									int newy = board.getSquare(k, l).piece.square.y + move[1];
									if (newx >= 0 && newx < ChessBoard.BOARD_SIZE && newy >= 0
											&& newy < ChessBoard.BOARD_SIZE) {
										copyKOpt.add(board.getSquare(newx, newy));
									}
								}
								for (Square oKingOpt : copyKOpt) {
									if (oKingOpt == square) {
										MMPawn.isValidMoveOn = false;
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		MMPawn.isValidMoveOn = false;
		return false;
	}

	public boolean canCastleLong() {
		if (color == PieceColor.white) {
			if (board.getSquare(0, 0).piece != null) {
				if (board.getSquare(0, 0).piece.type == PieceType.rook)
					if (((MMRook) board.getSquare(0, 0).piece).isFirstMove) {
						if (isFirstMove) {
							for (int i = 3; i > 1; i--) {
								for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
									for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
										if (board.getSquare(k, l).piece != null) {
											if (board.getSquare(k, l).piece.color != color) {
												if (board.getSquare(k, l).piece.type != PieceType.king) {
													for (Option opt : board.getSquare(k, l).piece
															.getOptions()) {
														if (opt.xsqu == board.getSquare(i, 0)) {
															return false;
														}
													}
												} else {
													for (Option opt : board.getSquare(k,
															l).piece.options) {
														if (opt.xsqu == board.getSquare(i, 0)) {
															return false;
														}
													}
												}
											}
										}
									}
								}
								if (board.getSquare(i, 0).piece != null) {
									return false;
								}
							}
							return true;
						}
					}
			}
		} else {
			if (board.getSquare(0, 7).piece != null) {
				if (board.getSquare(0, 7).piece.type == PieceType.rook)
					if (((MMRook) board.getSquare(0, 7).piece).isFirstMove) {
						if (isFirstMove) {
							for (int i = 3; i > 1; i--) {
								for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
									for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
										if (board.getSquare(k, l).piece != null) {
											if (board.getSquare(k, l).piece.color != color) {
												if (board.getSquare(k, l).piece.type != PieceType.king) {
													for (Option opt : board.getSquare(k, l).piece
															.getOptions()) {
														if (opt.xsqu == board.getSquare(i, 7)) {
															return false;
														}
													}
												} else {
													for (Option opt : board.getSquare(k,
															l).piece.options) {
														if (opt.xsqu == board.getSquare(i, 7)) {
															return false;
														}
													}
												}
											}
										}
									}
								}
								if (board.getSquare(i, 7).piece != null) {
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
			if (board.getSquare(7, 0).piece != null) {
				if (board.getSquare(7, 0).piece.type == PieceType.rook)
					if (((MMRook) board.getSquare(7, 0).piece).isFirstMove) {
						for (int i = 5; i < 7; i++) {
							for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
								for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
									if (board.getSquare(k, l).piece != null) {
										if (board.getSquare(k, l).piece.color != color) {
											if (board.getSquare(k, l).piece.type != PieceType.king) {
												for (Option opt : board.getSquare(k, l).piece
														.getOptions()) {
													if (opt.xsqu == board.getSquare(i, 0)) {
														return false;
													}
												}
											} else {
												for (Option opt : board.getSquare(k, l).piece.options) {
													if (opt.xsqu == board.getSquare(i, 0)) {
														return false;
													}
												}
											}
										}
									}
								}
							}
							if (board.getSquare(i, 0).piece != null) {
								return false;
							}
						}
						if (isFirstMove) {
							return true;
						}
					}
			}
		} else {
			if (board.getSquare(7, 7).piece != null) {
				if (board.getSquare(7, 7).piece.type == PieceType.rook)
					if (((MMRook) board.getSquare(7, 7).piece).isFirstMove) {
						for (int i = 5; i < 7; i++) {
							for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
								for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
									if (board.getSquare(k, l).piece != null) {
										if (board.getSquare(k, l).piece.color != color) {
											if (board.getSquare(k, l).piece.type != PieceType.king) {
												for (Option opt : board.getSquare(k, l).piece
														.getOptions()) {
													if (opt.xsqu == board.getSquare(i, 7)) {
														return false;
													}
												}
											} else {
												for (Option opt : board.getSquare(k, l).piece.options) {
													if (opt.xsqu == board.getSquare(i, 7)) {
														return false;
													}
												}
											}
										}
									}
								}
							}
							if (board.getSquare(i, 7).piece != null) {
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
			if (square.equals(board.getSquare(2, 0))) {
				board.setSquare(board.getSquare(0, 0), 3, 0);
			} else if (square.equals(board.getSquare(6, 0))) {
				board.setSquare(board.getSquare(7, 0), 5, 0);
			}
		} else {
			if (square.equals(board.getSquare(2, 7))) {
				board.setSquare(board.getSquare(0, 7), 3, 7);
			} else if (square.equals(board.getSquare(6, 7))) {
				board.setSquare(board.getSquare(7, 7), 5, 7);
			}
		}
	}

	public boolean isKingInCheck() {
		ArrayList<Piece> attackers = new ArrayList<Piece>();
		for (int k = 0; k < ChessBoard.BOARD_SIZE; k++) {
			for (int l = 0; l < ChessBoard.BOARD_SIZE; l++) {
				if (board.getSquare(k, l).piece != null) {
					if (board.getSquare(k, l).piece.color != this.color) {
						if (board.getSquare(k, l).piece.type != PieceType.king) {
							ArrayList<Option> optCopy = new ArrayList<Option>(
									board.getSquare(k, l).piece.getOptions());
							for (Option opt : optCopy) {
								if (opt.xsqu.piece == this) {
									if (opt.opType == OpType.take) {
										attackers.add(board.getSquare(k, l).piece);
									}
								}
							}
						} else {
							for (Option opt : this.options) {
								if (opt.xsqu.piece == this) {
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
