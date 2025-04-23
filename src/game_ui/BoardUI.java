package game_ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import board.ChessBoard;
import board.BoardCreator;
import obj.PieceColor;
import obj.PieceType;
import obj.Square;
import option.Option;
import piece.Piece;
import sf.Stockfish;
import tool.BoardTool;

@SuppressWarnings("serial")
public class BoardUI extends JFrame {

	public static final int FRAME_SIZE = ChessBoard.BOARD_SIZE * 64;
	btnSquare[][] squares;
	private static final HashMap<String, ImageIcon> imageCache = new HashMap<String, ImageIcon>();
	boolean isPressed = false;
	CheckBoxWindow cFrame;
	int btnX;
	int btnY;

	boolean isWhite = BoardCreator.cBoard.boardTool.userColor == PieceColor.white;

	ArrayList<Option> GetSelectedMoves = new ArrayList<Option>();
	ArrayList<Square> GetValidMoves = new ArrayList<Square>();
	ArrayList<Piece> GetThreadMoves = new ArrayList<Piece>();
	ArrayList<Piece> GetProtectedMoves = new ArrayList<Piece>();

	public BoardUI() throws IOException {
		setTitle("Satranç");
		setSize(FRAME_SIZE, FRAME_SIZE);
		setLayout(new GridLayout(ChessBoard.BOARD_SIZE, ChessBoard.BOARD_SIZE));
		cFrame = new CheckBoxWindow();

		squares = new btnSquare[ChessBoard.BOARD_SIZE][ChessBoard.BOARD_SIZE];
		startGame();
	}

	public void startGame() throws IOException {
		for (int i = isWhite ? ChessBoard.BOARD_SIZE - 1 : 0; isWhite ? i >= 0
				: i < ChessBoard.BOARD_SIZE; i = isWhite ? i - 1 : i + 1) {
			for (int j = !isWhite ? ChessBoard.BOARD_SIZE - 1 : 0; !isWhite ? j >= 0
					: j < ChessBoard.BOARD_SIZE; j = !isWhite ? j - 1 : j + 1) {
				squares[i][j] = new btnSquare();
				squares[i][j].setBorderPainted(false);

				setSquareColor(i, j);
				setPieceInSquares(i, j);
				printSelectedPiece(i, j);
				printGetValidMoves(i, j);
				printFindThreads(i, j);
				printFindProtects(i, j);
				actionListener(i, j);

				add(squares[i][j]);
			}
		}

		componentListener();
		keyListener();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	public void refreshBoard() throws IOException {
		getContentPane().removeAll();

		for (int i = isWhite ? ChessBoard.BOARD_SIZE - 1 : 0; isWhite ? i >= 0
				: i < ChessBoard.BOARD_SIZE; i = isWhite ? i - 1 : i + 1) {
			for (int j = !isWhite ? ChessBoard.BOARD_SIZE - 1 : 0; !isWhite ? j >= 0
					: j < ChessBoard.BOARD_SIZE; j = !isWhite ? j - 1 : j + 1) {
				squares[i][j] = new btnSquare();
				squares[i][j].setBorderPainted(false);

				setSquareColor(i, j);
				setPieceInSquares(i, j);
				if (BoardCreator.cBoard.getSquare(btnX, btnY).piece != null) {
					if (isPressed) {
						if (cFrame.isSelectedCBSelected(BoardCreator.cBoard.getSquare(btnX, btnY).piece.color))
							printSelectedPiece(i, j);
						if (cFrame.isSelectedCBValid(BoardCreator.cBoard.getSquare(btnX, btnY).piece.color))
							printGetValidMoves(i, j);
						if (cFrame.isSelectedCBThreatened(BoardCreator.cBoard.getSquare(btnX, btnY).piece.color))
							printFindThreads(i, j);
						if (cFrame.isSelectedCBProtected(BoardCreator.cBoard.getSquare(btnX, btnY).piece.color))
							printFindProtects(i, j);
					}
				}
				actionListener(i, j);
				add(squares[i][j]);
			}
		}

		BoardCreator.cBoard.ruleTool.isCheckMate();
		revalidate();
		repaint();
	}

	private void actionListener(int i, int j) {
		squares[i][j].putClientProperty("x", j);
		squares[i][j].putClientProperty("y", i);

		squares[i][j].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton clickedButton = (JButton) e.getSource();

				btnX = (int) clickedButton.getClientProperty("x");
				btnY = (int) clickedButton.getClientProperty("y");

				if (!isPressed) {
					if (BoardCreator.cBoard.getSquare(btnX, btnY).piece != null) {
						isPressed = true;
					}
				} else {
					if (BoardTool.currentPiece != null) {
						// if (BoardCreator.cBoard.boardTool.userColor == BoardTool.currentPiece.color
						// && BoardCreator.cBoard.boardTool.queue ==
						// BoardCreator.cBoard.boardTool.userColor) {
						Piece selectedPiece = BoardTool.currentPiece;
						for (Option move : BoardCreator.cBoard.boardTool.selectedPieceMove(selectedPiece))
							if (move.xsqu.x == btnX && move.xsqu.y == btnY) {
								BoardCreator.cBoard.boardTool.move(selectedPiece, btnX, btnY);
								isPressed = false;
								break;
							}
						// }
					}
				}
				if (BoardCreator.cBoard.getSquare(btnX, btnY).piece != null) {
					// if (BoardCreator.cBoard.boardTool.userColor ==
					// BoardCreator.cBoard.getSquare(btnX, btnY).piece.color &&
					// BoardCreator.cBoard.boardTool.queue ==
					// BoardCreator.cBoard.boardTool.userColor) {
					GetSelectedMoves = BoardCreator.cBoard.boardTool
							.selectedPieceMove(BoardCreator.cBoard.getSquare(btnX, btnY).piece);
					GetThreadMoves = BoardCreator.cBoard.boardTool
							.getThreateningPiece(BoardCreator.cBoard.getSquare(btnX, btnY).piece);
					GetProtectedMoves = BoardCreator.cBoard.boardTool
							.getProtectingPiece(BoardCreator.cBoard.getSquare(btnX, btnY).piece);
					GetValidMoves = BoardCreator.cBoard.boardTool.getValidMoves(BoardTool.currentPiece);
					/*
					 * }else { BoardTool.currentPiece = null; GetSelectedMoves.clear();
					 * GetThreadMoves.clear(); GetProtectedMoves.clear(); GetValidMoves.clear(); }
					 */
				} else {
					BoardTool.currentPiece = null;
					GetSelectedMoves.clear();
					GetThreadMoves.clear();
					GetProtectedMoves.clear();
					GetValidMoves.clear();
				}

				try {
					refreshBoard();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

	}

	private void printFindThreads(int i, int j) throws IOException {
		for (int f = 0; f < GetThreadMoves.size(); f++) {
			if (GetThreadMoves.get(f).square.x == j && GetThreadMoves.get(f).square.y == i) {
				squares[i][j].setIcons(setFrameImage("/frame/green.png"), null);
			}
			if (GetThreadMoves.size() != 0) {
				if (btnX == j && btnY == i) {
					squares[i][j].setIcons(setFrameImage("/frame/blue.png"), null);
				}
			}
		}
	}

	private void printGetValidMoves(int i, int j) throws IOException {
		for (int f = 0; f < GetValidMoves.size(); f++) {
			if (GetValidMoves.get(f).x == j && GetValidMoves.get(f).y == i) {
				squares[i][j].setIcons(setFrameImage("/frame/purple_yellow.png"), null);
			}
		}
	}

	private void printFindProtects(int i, int j) throws IOException {
		for (int f = 0; f < GetProtectedMoves.size(); f++) {
			if (GetProtectedMoves.get(f).square.x == j && GetProtectedMoves.get(f).square.y == i) {
				squares[i][j].setIcons(setFrameImage("/frame/gray.png"), null);
			}
			if (GetProtectedMoves.size() != 0) {
				if (btnX == j && btnY == i) {
					squares[i][j].setIcons(setFrameImage("/frame/white.png"), null);
				}
			}
		}
	}

	void printSelectedPiece(int i, int j) throws IOException {
		boolean isMoveOption = false;
		boolean nTakeR = false;
		int moveX = 0;
		if (BoardTool.currentPiece != null) {
			for (Option move : GetSelectedMoves) {
				if (move.xsqu.x == j && move.xsqu.y == i) {
					moveX = move.xsqu.x;
					if (BoardTool.currentPiece.type == PieceType.pawn) {
						if (move.xsqu.piece != null) {
							if (BoardTool.currentPiece.square.x != move.xsqu.x) {
								if (move.xsqu.piece.color != BoardTool.currentPiece.color)
									nTakeR = true;
							}
						}
					} else {
						if (move.xsqu.piece != null) {
							if (move.xsqu.piece.color != BoardTool.currentPiece.color)
								nTakeR = true;
						}
					}
					isMoveOption = true;
					break;
				}
			}
			if (isMoveOption) {
				if (BoardCreator.cBoard.getSquare(j, i).piece == null) {
					if (BoardTool.currentPiece.type != PieceType.pawn) {
						squares[i][j].setIcons(setFrameImage("/frame/yellow.png"), null);
					} else {
						if (BoardTool.currentPiece.square.x == moveX) {
							squares[i][j].setIcons(setFrameImage("/frame/yellow.png"), null);
						}
					}
				} else if (nTakeR) {
					squares[i][j].setIcons(setFrameImage("/frame/red.png"), null);
				}
			}
		}
	}

	Stockfish sf = new Stockfish();

	public void keyListener() {
		setFocusable(true);
		requestFocusInWindow();

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_R) {
					if (!BoardCreator.cBoard.ruleTool.isCheckMate())
						//TODO mini-max algorithm
					try {
						refreshBoard();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						refreshBoard();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (!BoardCreator.cBoard.ruleTool.isCheckMate())
					sf.doBestMove(e);
				try {
					refreshBoard();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}

	public void componentListener() {
		setLocation(cFrame.cFrameW.getWidth(), 0);

		setAlwaysOnTop(true);
		cFrame.cFrameW.setAlwaysOnTop(true);
		cFrame.cFrameB.setAlwaysOnTop(true);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				int centerX = getX();
				int centerY = getY();
				int WY = centerY + 3 * cFrame.cFrameW.getHeight();

				cFrame.cFrameW.setLocation(centerX - cFrame.cFrameW.getWidth(), WY);
				cFrame.cFrameB.setLocation(centerX + getWidth(), centerY);
			}
		});
	}

	private void setPieceInSquares(int i, int j) throws IOException {
		if (BoardCreator.cBoard.getSquare(j, i).piece != null) {
			squares[i][j].setIcon(setImagePath(BoardCreator.cBoard.getSquare(j, i).piece));
		}
	}

	void setSquareColor(int i, int j) {
		if ((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
			squares[i][j].setBackground(new Color(240, 217, 181));
		} else {
			squares[i][j].setBackground(new Color(181, 136, 99));
		}
	}

	ImageIcon setFrameImage(String path) throws IOException {
		if (!imageCache.containsKey(path)) {
			BufferedImage image = ImageIO.read(getClass().getResource(path));
			ImageIcon icon = new ImageIcon(image);
			imageCache.put(path, icon);
		}
		return imageCache.get(path);
	}

	ImageIcon setImagePath(Piece piece) throws IOException {
		String path = getPieceImgPath(piece);
		if (!imageCache.containsKey(path)) {
			BufferedImage image = ImageIO.read(getClass().getResource(path));
			ImageIcon icon = new ImageIcon(image);
			imageCache.put(path, icon);
		}
		return imageCache.get(path);
	}

	String getPieceImgPath(Piece piece) {
		String color = piece.color == PieceColor.white ? "white" : "black";
		String type = piece.type + "";
		return "/piece_pic/" + color + "_" + type + ".png";
	}
}
