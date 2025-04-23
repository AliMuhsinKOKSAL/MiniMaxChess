package piece;

import java.util.ArrayList;

import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import option.OpType;
import option.Option;

public abstract class Piece {

	public Square square;
	public PieceColor color;
	public PieceType type;
	public ArrayList<Option> options;

	public Piece(Square square, PieceType type, PieceColor cColor) {
		this.square = square;
		this.type = type;
		this.color = cColor;
		this.options = new ArrayList<Option>();
	}

	protected Option create(Square xsquare, PieceColor pieceColor) {
		OpType xOpType;
		
		if (xsquare.piece == null) {
			xOpType = OpType.movedTo;
		} else if (xsquare.piece != null) {
			if (pieceColor != xsquare.piece.color) {
				xOpType = OpType.take;
			} else {
				xOpType = OpType.notTake;
			}
		} else {
			xOpType = OpType.notMovedTo;
		}
		return new Option(xsquare, xOpType);
	}
	
	public ArrayList<Option> getOptions() {
		getMovement();
		return options;
	}

	public abstract void getMovement();
	
	public char toCharPiece() {
		char firstChar = (type != PieceType.knight) ? type.name().charAt(0) : 'n';
		if(color == PieceColor.white) {
			firstChar = Character.toUpperCase(firstChar);
		}
		return firstChar;
	}
	
}
