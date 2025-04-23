package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import board.BoardCreator;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import piece.Piece;

public class SaveMatches {

	String db_url = "jdbc:mysql://localhost:3306/chessbot_matches";
	String user = "root";
	String pass = "1234";

	Connection conn;
	String propInsertQuery;
	PreparedStatement preparedStatement;
	public int gameId;
	public int moveId;

	public SaveMatches() {
		try {
			conn = DriverManager.getConnection(db_url, user, pass);
			gameId = readGameID();
			propInsertQuery = "INSERT INTO moves(move_id, game_id, move_number, move, isWhiteMove, board_state) VALUES (?,?,?,?,?,?)";
			preparedStatement = conn.prepareStatement(propInsertQuery);
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					whileApplicationIsClosing();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void whileApplicationIsClosing() throws SQLException {
		preparedStatement.close();
		conn.close();
		Connection conn = DriverManager.getConnection(db_url, user, pass);

		String propInsertQuery = "INSERT INTO matches(id, result, botIsWhite, plycount) VALUES (?,?,?,?)";
		PreparedStatement preparedStatement = conn.prepareStatement(propInsertQuery);

		preparedStatement = conn.prepareStatement(propInsertQuery);
		preparedStatement.setInt(1, gameId);
		preparedStatement.setString(2, BoardCreator.cBoard.ruleTool.whoIsCheckMate());
		preparedStatement.setBoolean(3, (BoardCreator.cBoard.boardTool.userColor != PieceColor.white));
		preparedStatement.setInt(4, (BoardCreator.cBoard.boardTool.plyCount - 1));

		preparedStatement.executeUpdate();
		preparedStatement.close();
		conn.close();
	}

	public void addMoveDB(Piece piece, Square lastMove, Square moveSquare, PieceType pawnPromotionPieceType) {
		try {
			moveId = Integer.parseInt(Integer.toString(gameId) + "00" + 0);
			preparedStatement.setInt(1, moveId += BoardCreator.cBoard.boardTool.plyCount);
			preparedStatement.setInt(2, gameId);
			preparedStatement.setInt(3, BoardCreator.cBoard.boardTool.plyCount);
			preparedStatement.setString(4, squareMove(lastMove, moveSquare, pawnPromotionPieceType));
			preparedStatement.setBoolean(5, (piece.color == PieceColor.white));
			preparedStatement.setString(6, BoardCreator.cBoard.jsonCreator.generateJSON(gameId, moveId,
					squareMove(lastMove, moveSquare, pawnPromotionPieceType)));
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public String squareMove(Square lastMove, Square moveSquare, PieceType pawnPromotionPieceType) {
		return chessXNot(lastMove.x) + "" + (lastMove.y + 1) + "" + chessXNot(moveSquare.x) + "" + (moveSquare.y + 1)
				+ ((pawnPromotionPieceType != null) ? pawnPromotionPieceType.toString().substring(0, 1) : "");
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

	int readGameID() throws SQLException {
		String sql = "SELECT MAX(id) FROM matches";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		int id = 0;
		while (rs.next()) {
			id = rs.getInt("MAX(id)");
		}
		rs.close();
		stmt.close();
		return id + 1;
	}
}
