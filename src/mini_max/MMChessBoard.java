package mini_max;

import java.util.ArrayList;

import board.BoardCreator;
import mm_piece.MMBishop;
import mm_piece.MMKing;
import mm_piece.MMKnight;
import mm_piece.MMPawn;
import mm_piece.MMQueen;
import mm_piece.MMRook;
import obj.PieceType;
import obj.Square;
import piece.Piece;

public class MMChessBoard {
	public static final int BOARD_SIZE = 8;
	public Square[][] board;
	public MMBoardTool boardTool;
	public MMGameRuleTool ruleTool;

	MMChessBoard parent = null;
	ArrayList<MMChessBoard> childs;

	public MMChessBoard() {
		boardTool = new MMBoardTool(this);
		ruleTool = new MMGameRuleTool(this);

		childs = new ArrayList<MMChessBoard>();

		this.board = new Square[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board[i][j] = new Square(i, j);
				if (BoardCreator.cBoard.getSquare(i, j).piece != null) {
					this.boardTool.placePiece(newPiece(BoardCreator.cBoard.getSquare(i, j).piece, this));
				}
			}
		}

		this.boardTool.queue = BoardCreator.cBoard.boardTool.queue;
		this.boardTool.plyCount = BoardCreator.cBoard.boardTool.plyCount;
		this.boardTool.promotionType = BoardCreator.cBoard.boardTool.promotionType;
		/**/this.boardTool.lastMovePiece = (BoardCreator.cBoard.boardTool.lastMovePiece != null)
				? getSquare(BoardCreator.cBoard.boardTool.lastMovePiece.x,
						BoardCreator.cBoard.boardTool.lastMovePiece.y)
				: null;
		this.boardTool.lastMoveType = BoardCreator.cBoard.boardTool.lastMoveType;
		this.boardTool.lastMoveColor = BoardCreator.cBoard.boardTool.lastMoveColor;
		/**/this.boardTool.lastMoveSquare = (BoardCreator.cBoard.boardTool.lastMoveSquare != null)
				? getSquare(BoardCreator.cBoard.boardTool.lastMoveSquare.x,
						BoardCreator.cBoard.boardTool.lastMoveSquare.y)
				: null;
		this.boardTool.pawnPromotionDB = BoardCreator.cBoard.boardTool.pawnPromotionDB;
	}

	public MMChessBoard(MMChessBoard other) {
		parent = other;
		parent.childs.add(this);
		childs = new ArrayList<MMChessBoard>();

		boardTool = new MMBoardTool(this);
		ruleTool = new MMGameRuleTool(this);

		this.board = new Square[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board[i][j] = new Square(i, j);
				if (other.getSquare(i, j).piece != null) {
					this.boardTool.placePiece(newPiece(other.getSquare(i, j).piece, this));
				}
			}
		}

		this.boardTool.queue = other.boardTool.queue;
		this.boardTool.plyCount = other.boardTool.plyCount;
		this.boardTool.promotionType = other.boardTool.promotionType;
		/**/this.boardTool.lastMovePiece = (other.boardTool.lastMovePiece != null)
				? getSquare(other.boardTool.lastMovePiece.x, other.boardTool.lastMovePiece.y)
				: null;
		this.boardTool.lastMoveType = other.boardTool.lastMoveType;
		this.boardTool.lastMoveColor = other.boardTool.lastMoveColor;
		/**/this.boardTool.lastMoveSquare = (other.boardTool.lastMoveSquare != null)
				? getSquare(other.boardTool.lastMoveSquare.x, other.boardTool.lastMoveSquare.y)
				: null;
		this.boardTool.pawnPromotionDB = other.boardTool.pawnPromotionDB;
	}

	public Square getSquare(int x, int y) {
		return board[x][y];
	}

	public void setSquare(Square square, int newx, int newy) {
		Piece piece = square.piece;

		square.piece = null;

		Square targetSquare = getSquare(newx, newy);
		targetSquare.piece = piece;
		piece.square = targetSquare;
	}

	public boolean isLeaf() {
		return childs.isEmpty();
	}

	public boolean isRoot() {
		return parent == null;
	}

	public void traverse() {
		traverse(this);
	}

	private void traverse(MMChessBoard board) {
		board.boardTool.printBoardState();
//		System.out.println("===========");

		for (MMChessBoard child : board.childs) {
			traverse(child);
		}
	}

	Piece newPiece(Piece piece, MMChessBoard board) {
		if (piece.type == PieceType.pawn) {
			return new MMPawn(board.getSquare(piece.square.x, piece.square.y), piece.color, board);
		} else if (piece.type == PieceType.rook) {
			return new MMRook(board.getSquare(piece.square.x, piece.square.y), piece.color, board);
		} else if (piece.type == PieceType.knight) {
			return new MMKnight(board.getSquare(piece.square.x, piece.square.y), piece.color, board);
		} else if (piece.type == PieceType.bishop) {
			return new MMBishop(board.getSquare(piece.square.x, piece.square.y), piece.color, board);
		} else if (piece.type == PieceType.queen) {
			return new MMQueen(board.getSquare(piece.square.x, piece.square.y), piece.color, board);
		} else if (piece.type == PieceType.king) {
			return new MMKing(board.getSquare(piece.square.x, piece.square.y), piece.color, board);
		}
		return null;
	}
}
