package option;

import obj.Square;

public class Option {
	public Square xsqu;
	public OpType opType;

	public Option(Square xsquare, OpType opType) {
		this.xsqu = xsquare;
		this.opType = opType;
	}
}
