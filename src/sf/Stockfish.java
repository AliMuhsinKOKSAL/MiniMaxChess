package sf;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import board.BoardCreator;
import obj.Square;

public class Stockfish {

	String stockfishPath;
	String fen;
	Process process;
	FENGenerator fenGenerator = new FENGenerator();

	public Stockfish() {
		String stockfishPath = "\"C:\\Users\\alimu\\Documents\\stockfish-windows-x86-64-avx2\\stockfish\\stockfish-windows-x86-64-avx2.exe\"";
		ProcessBuilder builder = new ProcessBuilder(stockfishPath);
		builder.redirectErrorStream(true);
		try {
			process = builder.start();

			sendCommand(process, "uci");

			sendCommand(process, "setoption name UCI_LimitStrength value true");
			sendCommand(process, "setoption name UCI_Elo value 1300");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			sendCommand(process, "quit");
		}));
	}

	public String stockfishBestMove() throws IOException {

		fen = fenGenerator.generateFEN();

		sendCommand(process, "position fen " + fen);
		sendCommand(process, "go depth 10");

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("bestmove")) {
				line = line.replaceFirst("bestmove ", "");

				line = line.replaceFirst("ponder ", "");

				int spaceIndex = line.lastIndexOf(" ");
				if (spaceIndex != -1) {
					line = line.substring(0, spaceIndex);
				}
				break;
			}
		}

		return line;
	}

	public static void sendCommand(Process process, String command) {
		try {
			OutputStream outputStream = process.getOutputStream();
			outputStream.write((command + "\n").getBytes());
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doBestMove(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_T) {
			if (BoardCreator.cBoard.boardTool.queue != BoardCreator.cBoard.boardTool.userColor) {
				try {
					sfBestMove();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void sfBestMove() throws IOException {
		String strBestMove = stockfishBestMove();
		String strPiece = strBestMove.substring(0, 2);
		String strMove = strBestMove.substring(2, 4);

		if (strBestMove.length() == 5) {
			switch (Character.toLowerCase(strBestMove.charAt(4))) {
			case 'r':
				findSquare(strPiece).piece = new piece.Rook(findSquare(strPiece), findSquare(strPiece).piece.color);
				break;
			case 'n':
				findSquare(strPiece).piece = new piece.Knight(findSquare(strPiece), findSquare(strPiece).piece.color);
				break;
			case 'b':
				findSquare(strPiece).piece = new piece.Bishop(findSquare(strPiece), findSquare(strPiece).piece.color);
				break;
			case 'q':
				findSquare(strPiece).piece = new piece.Queen(findSquare(strPiece), findSquare(strPiece).piece.color);
				break;
			}
		}

		BoardCreator.cBoard.boardTool.move(findSquare(strPiece).piece, findSquare(strMove).x, findSquare(strMove).y);
	}

	Square findSquare(String str) {
		int col = -1;
		int row = Integer.parseInt(str.substring(1, 2)) - 1;
		switch (str.charAt(0)) {
		case 'a':
			col = 0;
			break;
		case 'b':
			col = 1;
			break;
		case 'c':
			col = 2;
			break;
		case 'd':
			col = 3;
			break;
		case 'e':
			col = 4;
			break;
		case 'f':
			col = 5;
			break;
		case 'g':
			col = 6;
			break;
		case 'h':
			col = 7;
			break;
		}
		return BoardCreator.cBoard.getSquare(col, row);
	}

}
