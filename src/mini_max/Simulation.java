package mini_max;

import java.util.ArrayList;

import board.BoardCreator;
import obj.PieceType;
import obj.Square;
import option.Option;
import piece.Piece;

public class Simulation {

	public Simulation() {
		if(BoardCreator.cBoard.boardTool.queue!=BoardCreator.cBoard.boardTool.userColor)return;
		MMChessBoard simBoard = new MMChessBoard();

		minimax(simBoard, 3);

		simBoard.traverse();
	}

	int basicEval(MMChessBoard board) {
		int eval = 0;
		int mobility = 0;
		for (int i = 0; i < MMChessBoard.BOARD_SIZE; i++) {
			for (int j = 0; j < MMChessBoard.BOARD_SIZE; j++) {
				if (board.getSquare(i, j).piece != null) {
					eval += pieceFactors(board.getSquare(i, j).piece.type);
					mobility += board.boardTool.selectedPieceMove(board.getSquare(i, j).piece).size();
				}
			}
		}
		return eval + mobility;
	}

	int pieceFactors(PieceType type) {
		switch (type) {
		case pawn:
			return 1;
		case knight:
			return 3;
		case bishop:
			return 3;
		case rook:
			return 5;
		case queen:
			return 9;
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}

	void minimax(MMChessBoard board, int depth) {
		if (depth == 0 || board.ruleTool.isCheckMate())
			return;

		ArrayList<Move> moves = generateAllMoves(board);

		for (int i = 0; i < moves.size(); i++) {
			MMChessBoard tempBoard = new MMChessBoard(board);
			Square tempSq = moves.get(i).from;
			tempBoard.boardTool.move(tempBoard.getSquare(tempSq.x, tempSq.y).piece, moves.get(i).to.x,
					moves.get(i).to.y);

			minimax(tempBoard, depth - 1);
		}
	}
	
	ArrayList<Move> generateAllMoves(MMChessBoard board){
		ArrayList<Piece> moveablePieces = new ArrayList<Piece>();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board.getSquare(j, i).piece != null) {
					if (board.getSquare(j, i).piece.color == board.boardTool.queue) {
						if (!board.boardTool.selectedPieceMove(board.getSquare(j, i).piece).isEmpty()) {
							moveablePieces.add(board.getSquare(j, i).piece);
						}
					}
				}
			}
		}

		ArrayList<Move> moves = new ArrayList<Move>();

		for (Piece mPiece : moveablePieces) {
			for (Option opt : board.boardTool.selectedPieceMove(mPiece)) {
				moves.add(new Move(mPiece.square, opt.xsqu));
			}
		}
		
		return moves;
	}
}
