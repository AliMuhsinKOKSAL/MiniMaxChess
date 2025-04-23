package board;

import json_creator.JSONCreator;
import obj.PieceColor;
import obj.Square;
import piece.Piece;
import tool.BoardTool;
import tool.GameRuleTool;

public class ChessBoard {
	public static final int BOARD_SIZE = 8;
	public Square[][] board;
	public BoardTool boardTool;
	public GameRuleTool ruleTool;
	public JSONCreator jsonCreator;

	public ChessBoard() {
		this.board = new Square[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board[i][j] = new Square(i, j);
			}
		}
		
		boardTool = new BoardTool();
		ruleTool = new GameRuleTool();
		jsonCreator = new JSONCreator();
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

	public void init() {
		// Beyaz taşlar
		new piece.Rook(BoardCreator.cBoard.getSquare(0, 0), PieceColor.white);
		new piece.Knight(BoardCreator.cBoard.getSquare(1, 0), PieceColor.white);
		new piece.Bishop(BoardCreator.cBoard.getSquare(2, 0), PieceColor.white);
		new piece.Queen(BoardCreator.cBoard.getSquare(3, 0), PieceColor.white);
		new piece.King(BoardCreator.cBoard.getSquare(4, 0), PieceColor.white);
		new piece.Bishop(BoardCreator.cBoard.getSquare(5, 0), PieceColor.white);
		new piece.Knight(BoardCreator.cBoard.getSquare(6, 0), PieceColor.white);
		new piece.Rook(BoardCreator.cBoard.getSquare(7, 0), PieceColor.white);

		new piece.Pawn(BoardCreator.cBoard.getSquare(0, 1), PieceColor.white);
		new piece.Pawn(BoardCreator.cBoard.getSquare(1, 1), PieceColor.white);
		new piece.Pawn(BoardCreator.cBoard.getSquare(2, 1), PieceColor.white);
		new piece.Pawn(BoardCreator.cBoard.getSquare(3, 1), PieceColor.white);
		new piece.Pawn(BoardCreator.cBoard.getSquare(4, 1), PieceColor.white);
		new piece.Pawn(BoardCreator.cBoard.getSquare(5, 1), PieceColor.white);
		new piece.Pawn(BoardCreator.cBoard.getSquare(6, 1), PieceColor.white);
		new piece.Pawn(BoardCreator.cBoard.getSquare(7, 1), PieceColor.white);

		// Siyah taşlar
		new piece.Rook(BoardCreator.cBoard.getSquare(0, 7), PieceColor.black);
		new piece.Knight(BoardCreator.cBoard.getSquare(1, 7), PieceColor.black);
		new piece.Bishop(BoardCreator.cBoard.getSquare(2, 7), PieceColor.black);
		new piece.Queen(BoardCreator.cBoard.getSquare(3, 7), PieceColor.black);
		new piece.King(BoardCreator.cBoard.getSquare(4, 7), PieceColor.black);
		new piece.Bishop(BoardCreator.cBoard.getSquare(5, 7), PieceColor.black);
		new piece.Knight(BoardCreator.cBoard.getSquare(6, 7), PieceColor.black);
		new piece.Rook(BoardCreator.cBoard.getSquare(7, 7), PieceColor.black);

		new piece.Pawn(BoardCreator.cBoard.getSquare(0, 6), PieceColor.black);
		new piece.Pawn(BoardCreator.cBoard.getSquare(1, 6), PieceColor.black);
		new piece.Pawn(BoardCreator.cBoard.getSquare(2, 6), PieceColor.black);
		new piece.Pawn(BoardCreator.cBoard.getSquare(3, 6), PieceColor.black);
		new piece.Pawn(BoardCreator.cBoard.getSquare(4, 6), PieceColor.black);
		new piece.Pawn(BoardCreator.cBoard.getSquare(5, 6), PieceColor.black);
		new piece.Pawn(BoardCreator.cBoard.getSquare(6, 6), PieceColor.black);
		new piece.Pawn(BoardCreator.cBoard.getSquare(7, 6), PieceColor.black);
	}	
}
