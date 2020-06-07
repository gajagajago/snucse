import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

//Name: Ryu Jun Yul
//StudentID# : 2016-17097

//======================================================Don't modify below===============================================================//
enum PieceType {
	king, queen, bishop, knight, rook, pawn, none
}

enum PlayerColor {
	black, white, none
}

public class ChessBoard {
	private final JPanel gui = new JPanel(new BorderLayout(3, 3));
	private JPanel chessBoard;
	private JButton[][] chessBoardSquares = new JButton[8][8];
	private Piece[][] chessBoardStatus = new Piece[8][8];
	private ImageIcon[] pieceImage_b = new ImageIcon[7];
	private ImageIcon[] pieceImage_w = new ImageIcon[7];
	private JLabel message = new JLabel("Enter Reset to Start");

	ChessBoard() {
		initPieceImages();
		initBoardStatus();
		initializeGui();
	}

	public final void initBoardStatus() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++)
				chessBoardStatus[j][i] = new Piece();
		}
	}

	public final void initPieceImages() {
		pieceImage_b[0] = new ImageIcon(
				new ImageIcon("./img/king_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[1] = new ImageIcon(
				new ImageIcon("./img/queen_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[2] = new ImageIcon(
				new ImageIcon("./img/bishop_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[3] = new ImageIcon(
				new ImageIcon("./img/knight_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[4] = new ImageIcon(
				new ImageIcon("./img/rook_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[5] = new ImageIcon(
				new ImageIcon("./img/pawn_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[6] = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));

		pieceImage_w[0] = new ImageIcon(
				new ImageIcon("./img/king_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[1] = new ImageIcon(
				new ImageIcon("./img/queen_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[2] = new ImageIcon(
				new ImageIcon("./img/bishop_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[3] = new ImageIcon(
				new ImageIcon("./img/knight_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[4] = new ImageIcon(
				new ImageIcon("./img/rook_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[5] = new ImageIcon(
				new ImageIcon("./img/pawn_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[6] = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
	}

	public ImageIcon getImageIcon(Piece piece) {
		if (piece.color.equals(PlayerColor.black)) {
			if (piece.type.equals(PieceType.king))
				return pieceImage_b[0];
			else if (piece.type.equals(PieceType.queen))
				return pieceImage_b[1];
			else if (piece.type.equals(PieceType.bishop))
				return pieceImage_b[2];
			else if (piece.type.equals(PieceType.knight))
				return pieceImage_b[3];
			else if (piece.type.equals(PieceType.rook))
				return pieceImage_b[4];
			else if (piece.type.equals(PieceType.pawn))
				return pieceImage_b[5];
			else
				return pieceImage_b[6];
		} else if (piece.color.equals(PlayerColor.white)) {
			if (piece.type.equals(PieceType.king))
				return pieceImage_w[0];
			else if (piece.type.equals(PieceType.queen))
				return pieceImage_w[1];
			else if (piece.type.equals(PieceType.bishop))
				return pieceImage_w[2];
			else if (piece.type.equals(PieceType.knight))
				return pieceImage_w[3];
			else if (piece.type.equals(PieceType.rook))
				return pieceImage_w[4];
			else if (piece.type.equals(PieceType.pawn))
				return pieceImage_w[5];
			else
				return pieceImage_w[6];
		} else
			return pieceImage_w[6];
	}

	public final void initializeGui() {
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
		gui.add(tools, BorderLayout.PAGE_START);
		JButton startButton = new JButton("Reset");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initiateBoard();
			}
		});

		tools.add(startButton);
		tools.addSeparator();
		tools.add(message);

		chessBoard = new JPanel(new GridLayout(0, 8));
		chessBoard.setBorder(new LineBorder(Color.BLACK));
		gui.add(chessBoard);
		ImageIcon defaultIcon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int i = 0; i < chessBoardSquares.length; i++) {
			for (int j = 0; j < chessBoardSquares[i].length; j++) {
				JButton b = new JButton();
				b.addActionListener(new ButtonListener(i, j));
				b.setMargin(buttonMargin);
				b.setIcon(defaultIcon);
				if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0))
					b.setBackground(Color.WHITE);
				else
					b.setBackground(Color.gray);
				b.setOpaque(true);
				b.setBorderPainted(false);
				chessBoardSquares[j][i] = b;
			}
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++)
				chessBoard.add(chessBoardSquares[j][i]);

		}
	}

	public final JComponent getGui() {
		return gui;
	}

	public static void main(String[] args) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				ChessBoard cb = new ChessBoard();
				JFrame f = new JFrame("Chess");
				f.add(cb.getGui());
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				f.setLocationByPlatform(true);
				f.setResizable(false);
				f.pack();
				f.setMinimumSize(f.getSize());
				f.setVisible(true);
			}
		};
		SwingUtilities.invokeLater(r);
	}

	// ================================Utilize these
	// functions========================================//
	class Piece {
		PlayerColor color;
		PieceType type;

		Piece() {
			color = PlayerColor.none;
			type = PieceType.none;
		}

		Piece(PlayerColor color, PieceType type) {
			this.color = color;
			this.type = type;
		}
	}

	public void setIcon(int x, int y, Piece piece) {
		chessBoardSquares[y][x].setIcon(getImageIcon(piece));
		chessBoardStatus[y][x] = piece;
	}

	public Piece getIcon(int x, int y) {
		return chessBoardStatus[y][x];
	}

	public void markPosition(int x, int y) {
		chessBoardSquares[y][x].setBackground(Color.pink);
	}

	public void unmarkPosition(int x, int y) {
		if ((y % 2 == 1 && x % 2 == 1) || (y % 2 == 0 && x % 2 == 0))
			chessBoardSquares[y][x].setBackground(Color.WHITE);
		else
			chessBoardSquares[y][x].setBackground(Color.gray);
	}

	public void setStatus(String inpt) {
		message.setText(inpt);
	}

	public void initiateBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++)
				setIcon(i, j, new Piece());
		}
		setIcon(0, 0, new Piece(PlayerColor.black, PieceType.rook));
		setIcon(0, 1, new Piece(PlayerColor.black, PieceType.knight));
		setIcon(0, 2, new Piece(PlayerColor.black, PieceType.bishop));
		setIcon(0, 3, new Piece(PlayerColor.black, PieceType.queen));
		setIcon(0, 4, new Piece(PlayerColor.black, PieceType.king));
		setIcon(0, 5, new Piece(PlayerColor.black, PieceType.bishop));
		setIcon(0, 6, new Piece(PlayerColor.black, PieceType.knight));
		setIcon(0, 7, new Piece(PlayerColor.black, PieceType.rook));
		for (int i = 0; i < 8; i++) {
			setIcon(1, i, new Piece(PlayerColor.black, PieceType.pawn));
			setIcon(6, i, new Piece(PlayerColor.white, PieceType.pawn));
		}
		setIcon(7, 0, new Piece(PlayerColor.white, PieceType.rook));
		setIcon(7, 1, new Piece(PlayerColor.white, PieceType.knight));
		setIcon(7, 2, new Piece(PlayerColor.white, PieceType.bishop));
		setIcon(7, 3, new Piece(PlayerColor.white, PieceType.queen));
		setIcon(7, 4, new Piece(PlayerColor.white, PieceType.king));
		setIcon(7, 5, new Piece(PlayerColor.white, PieceType.bishop));
		setIcon(7, 6, new Piece(PlayerColor.white, PieceType.knight));
		setIcon(7, 7, new Piece(PlayerColor.white, PieceType.rook));
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++)
				unmarkPosition(i, j);
		}
		onInitiateBoard();
	}
	// ======================================================Don't modify
	// above==============================================================//

	// ======================================================Implement
	// below=================================================================//
	enum MagicType {
		MARK, CHECK, CHECKMATE
	};

	private int selX, selY;
	private boolean check, checkmate, end;

	PlayerColor turn;
	boolean firstClick; // flag to notify 1st / 2nd click
	Piece clickedPiece; // piece clicked at each click
	Piece firstClickedPiece; // saved 1st clicked piece for move
	Point firstClickedPiecePosition; // saved 1st clicked piece position for removal
	ArrayList<Point> possibleMoves; // saved movable positions for 1st clicked piece

	class ButtonListener implements ActionListener {

		Point curr;

		ButtonListener(int x, int y) {
			curr = new Point(x, y);
		}

		public void actionPerformed(ActionEvent e) {
			if (end)
				return;

			clickedPiece = getIcon(curr.x, curr.y);

			if (firstClick) {
				if (clickedPiece.color != turn)
					return;

				switch (clickedPiece.type) {
					case king:
						markKing(curr.x, curr.y, clickedPiece.color);
						break;
					case queen:
						markQueen(curr.x, curr.y, clickedPiece.color);
						break;
					case bishop:
						markBishop(curr.x, curr.y, clickedPiece.color);
						break;
					case knight:
						markKnight(curr.x, curr.y, clickedPiece.color);
						break;
					case rook:
						markRook(curr.x, curr.y, clickedPiece.color);
						break;
					case pawn:
						markPawn(curr.x, curr.y, clickedPiece.color);
						break;
					case none:
						break;
				}

				for (Point p : possibleMoves)
					markPosition(p.x, p.y);

				firstClickedPiece = clickedPiece;
				firstClickedPiecePosition = curr;

				firstClick = false;
			} else {
				Point secondClickedPosition = curr;
				boolean isSecondClickValid = false;

				for (Point p : possibleMoves) {
					if (p.equals(secondClickedPosition)) {
						isSecondClickValid = true;
						setIcon(curr.x, curr.y, firstClickedPiece);
						setIcon(firstClickedPiecePosition.x, firstClickedPiecePosition.y, new Piece());
						for (Point po : possibleMoves)
							unmarkPosition(po.x, po.y);

						firstClick = true;
						possibleMoves = new ArrayList<>();
						turn = (turn == PlayerColor.black) ? PlayerColor.white : PlayerColor.black;
						String s1 = "";
						String s2 = "";
						if (isCheck(turn == PlayerColor.black ? PlayerColor.black : PlayerColor.white,
								findKing(turn == PlayerColor.black ? PlayerColor.black : PlayerColor.white))) {
							s1 = "CHECK";
							if (isCheckMate(turn == PlayerColor.black ? PlayerColor.black : PlayerColor.white)) {
								s2 = "MATE";
								end = true;
							}

						}
						;
						setStatus(turn + "'s turn " + s1 + s2);
						break;
					}
				}

				if (!isSecondClickValid) {
					for (Point p : possibleMoves)
						unmarkPosition(p.x, p.y);
					firstClick = true;
					possibleMoves = new ArrayList<>();
				}
			}
		}
	}

	void onInitiateBoard() {
		turn = PlayerColor.black;
		firstClick = true;
		possibleMoves = new ArrayList<>();
		setStatus(turn + "'s turn ");
		end = false;
	}

	void markPawn(int x, int y, PlayerColor pc) {
		int move = (pc == PlayerColor.black) ? 1 : -1;

		// ord mark
		if ((x == 7 && pc == PlayerColor.black) || (x == 0 && pc == PlayerColor.white))
			return;

		Piece advance = getIcon(x + move, y);
		if (advance.type == PieceType.none) {
			possibleMoves.add(new Point(x + move, y));
		}
		if ((pc == PlayerColor.black && x == 1) || (pc == PlayerColor.white && x == 6)) {
			Piece advanceTwo = getIcon(x + 2 * move, y);
			if (advance.type == PieceType.none && advanceTwo.type == PieceType.none)
				possibleMoves.add(new Point(x + 2 * move, y));
		}

		// kill mark
		switch (y) {
			case 0:
				if (isEnemy(x + move, y + 1, pc)) {
					possibleMoves.add(new Point(x + move, y + 1));
				}
				break;
			case 7:
				if (isEnemy(x + move, y - 1, pc)) {
					possibleMoves.add(new Point(x + move, y - 1));
				}
				break;
			default:
				if (isEnemy(x + move, y + 1, pc)) {
					possibleMoves.add(new Point(x + move, y + 1));
				}
				if (isEnemy(x + move, y - 1, pc)) {
					possibleMoves.add(new Point(x + move, y - 1));
				}
				break;
		}

		for (Point p : possibleMoves)
			System.out.println(p.toString());

		if (possibleMoves.isEmpty())
			return;
	}

	void markRook(int x, int y, PlayerColor pc) {
		// mark up
		for (int i = x - 1; i >= 0; --i) {
			if (getIcon(i, y).type == PieceType.none)
				possibleMoves.add(new Point(i, y));
			if (isAlly(i, y, pc))
				break;
			if (isEnemy(i, y, pc)) {
				possibleMoves.add(new Point(i, y));
				break;
			}
		}
		// mark down
		for (int i = x + 1; i < 8; ++i) {
			if (getIcon(i, y).type == PieceType.none)
				possibleMoves.add(new Point(i, y));
			if (isAlly(i, y, pc))
				break;
			if (isEnemy(i, y, pc)) {
				possibleMoves.add(new Point(i, y));
				break;
			}

		}
		// mark left
		for (int i = y - 1; i >= 0; --i) {
			if (getIcon(x, i).type == PieceType.none)
				possibleMoves.add(new Point(x, i));
			if (isAlly(x, i, pc))
				break;
			if (isEnemy(x, i, pc)) {
				possibleMoves.add(new Point(x, i));
				break;
			}
		}
		// mark right
		for (int i = y + 1; i < 8; ++i) {
			if (getIcon(x, i).type == PieceType.none)
				possibleMoves.add(new Point(x, i));
			if (isAlly(x, i, pc))
				break;
			if (isEnemy(x, i, pc)) {
				possibleMoves.add(new Point(x, i));
				break;
			}
		}

		if (possibleMoves.isEmpty())
			return;
	}

	void markKnight(int x, int y, PlayerColor pc) {
		for (int i = x - 2; i <= x + 2; i += 4) {
			for (int j = y - 1; j <= y + 1; j += 2) {
				if ((i >= 0) && (i < 8) && (j >= 0) && (j < 8)) {
					if (!isAlly(i, j, pc))
						possibleMoves.add(new Point(i, j));
				}
			}
		}

		for (int i = x - 1; i <= x + 1; i += 2) {
			for (int j = y - 2; j <= y + 2; j += 4) {
				if ((i >= 0) && (i < 8) && (j >= 0) && (j < 8)) {
					if (!isAlly(i, j, pc))
						possibleMoves.add(new Point(i, j));
				}
			}
		}
	}

	void markBishop(int x, int y, PlayerColor pc) {
		// mark upleft
		for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; --i, --j) {
			if (getIcon(i, j).type == PieceType.none)
				possibleMoves.add(new Point(i, j));
			if (isAlly(i, j, pc))
				break;
			if (isEnemy(i, j, pc)) {
				possibleMoves.add(new Point(i, j));
				break;
			}
		}
		// mark downleft
		for (int i = x + 1, j = y - 1; i < 8 && j >= 0; ++i, --j) {
			if (getIcon(i, j).type == PieceType.none)
				possibleMoves.add(new Point(i, j));
			if (isAlly(i, j, pc))
				break;
			if (isEnemy(i, j, pc)) {
				possibleMoves.add(new Point(i, j));
				break;
			}
		}
		// mark up right
		for (int i = x - 1, j = y + 1; i >= 0 && j < 8; --i, ++j) {
			if (getIcon(i, j).type == PieceType.none)
				possibleMoves.add(new Point(i, j));
			if (isAlly(i, j, pc))
				break;
			if (isEnemy(i, j, pc)) {
				possibleMoves.add(new Point(i, j));
				break;
			}
		}
		// mark right
		for (int i = x + 1, j = y + 1; i < 8 && j < 8; ++i, ++j) {
			if (getIcon(i, j).type == PieceType.none)
				possibleMoves.add(new Point(i, j));
			if (isAlly(i, j, pc))
				break;
			if (isEnemy(i, j, pc)) {
				possibleMoves.add(new Point(i, j));
				break;
			}
		}

		if (possibleMoves.isEmpty())
			return;
	}

	void markQueen(int x, int y, PlayerColor pc) {
		markRook(x, y, pc);
		markBishop(x, y, pc);
	}

	void markKing(int x, int y, PlayerColor pc) {
		for (int i = x - 1; i <= x + 1; ++i) {
			for (int j = y - 1; j <= y + 1; ++j) {
				if ((i >= 0) && (i < 8) && (j >= 0) && (j < 8)) {
					if (!isAlly(i, j, pc))
						possibleMoves.add(new Point(i, j));
				}
			}
		}
	}

	boolean isAlly(int x, int y, PlayerColor pc) {
		return getIcon(x, y).color == pc;
	}

	boolean isEnemy(int x, int y, PlayerColor pc) {
		return getIcon(x, y).color != pc && getIcon(x, y).color != PlayerColor.none;
	}

	Point findKing(PlayerColor pc) {
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				if (getIcon(i, j).color == pc && getIcon(i, j).type == PieceType.king)
					return new Point(i, j);
			}
		}

		return null; //
	}

	boolean isCheck(PlayerColor pc, Point king) {
		Point kingPosition = king;

		// check Straight Up
		for (int x = kingPosition.x - 1; x >= 0; --x) {
			if (isAlly(x, kingPosition.y, pc))
				break;

			if (isEnemy(x, kingPosition.y, pc) && ((getIcon(x, kingPosition.y).type == PieceType.rook)
					|| (getIcon(x, kingPosition.y).type == PieceType.queen))) {
				return true;
			}
		}
		// check Straight Down
		for (int x = kingPosition.x + 1; x < 8; ++x) {
			if (isAlly(x, kingPosition.y, pc))
				break;

			if (isEnemy(x, kingPosition.y, pc) && ((getIcon(x, kingPosition.y).type == PieceType.rook)
					|| (getIcon(x, kingPosition.y).type == PieceType.queen))) {
				return true;
			}
		}
		// check Horizontal left
		for (int y = kingPosition.y - 1; y >= 0; --y) {
			if (isAlly(kingPosition.x, y, pc))
				break;

			if (isEnemy(kingPosition.x, y, pc) && ((getIcon(kingPosition.x, y).type == PieceType.rook)
					|| (getIcon(kingPosition.x, y).type == PieceType.queen))) {
				return true;
			}
		}
		// check Horizontal Right
		for (int y = kingPosition.y + 1; y < 8; ++y) {
			if (isAlly(kingPosition.x, y, pc))
				break;

			if (isEnemy(kingPosition.x, y, pc) && ((getIcon(kingPosition.x, y).type == PieceType.rook)
					|| (getIcon(kingPosition.x, y).type == PieceType.queen))) {
				return true;
			}
		}

		// check leftUp
		for (int x = kingPosition.x - 1, y = kingPosition.y - 1; x >= 0 && y >= 0; --x, --y) {
			if (isAlly(x, y, pc))
				break;

			if (pc == PlayerColor.white && x == kingPosition.x - 1) {
				if (isEnemy(x, y, pc) && getIcon(x, y).type == PieceType.pawn)
					return true;
			}

			if ((isEnemy(x, y, pc))
					&& ((getIcon(x, y).type == PieceType.bishop) || getIcon(x, y).type == PieceType.queen)) {
				return true;
			}
		}
		// check leftDown
		for (int x = kingPosition.x + 1, y = kingPosition.y - 1; x < 8 && y >= 0; ++x, --y) {
			if (isAlly(x, y, pc))
				break;

			if (pc == PlayerColor.black && x == kingPosition.x + 1) {
				if (isEnemy(x, y, pc) && getIcon(x, y).type == PieceType.pawn)
					return true;
			}

			if ((isEnemy(x, y, pc))
					&& ((getIcon(x, y).type == PieceType.bishop) || getIcon(x, y).type == PieceType.queen)) {
				return true;
			}
		}

		// check rightup
		for (int x = kingPosition.x - 1, y = kingPosition.y + 1; x >= 0 && y < 8; --x, ++y) {
			if (isAlly(x, y, pc))
				break;

			if (pc == PlayerColor.white && x == kingPosition.x - 1) {
				if (isEnemy(x, y, pc) && getIcon(x, y).type == PieceType.pawn)
					return true;
			}

			if ((isEnemy(x, y, pc))
					&& ((getIcon(x, y).type == PieceType.bishop) || getIcon(x, y).type == PieceType.queen)) {
				return true;
			}
		}

		// check rightdown
		for (int x = kingPosition.x + 1, y = kingPosition.y + 1; x < 8 && y < 8; ++x, ++y) {
			if (isAlly(x, y, pc))
				break;

			if (pc == PlayerColor.black && x == kingPosition.x + 1) {
				if (isEnemy(x, y, pc) && getIcon(x, y).type == PieceType.pawn)
					return true;
			}

			if ((isEnemy(x, y, pc))
					&& ((getIcon(x, y).type == PieceType.bishop) || getIcon(x, y).type == PieceType.queen)) {
				return true;
			}
		}

		// check pawn

		// check knight
		for (int i = kingPosition.x - 2; i <= kingPosition.x + 2; i += 4) {
			for (int j = kingPosition.y - 1; j <= kingPosition.y + 1; j += 2) {
				if ((i >= 0) && (i < 8) && (j >= 0) && (j < 8)) {
					if (isEnemy(i, j, pc) && getIcon(i, j).type == PieceType.knight)
						return true;
				}
			}
		}

		for (int i = kingPosition.x - 1; i <= kingPosition.x + 1; i += 2) {
			for (int j = kingPosition.y - 2; j <= kingPosition.y + 2; j += 4) {
				if ((i >= 0) && (i < 8) && (j >= 0) && (j < 8)) {
					if (isEnemy(i, j, pc) && getIcon(i, j).type == PieceType.knight)
						return true;
				}
			}
		}

		return false;
	}

	boolean isCheckMate(PlayerColor pc) {
		Point kingPosition = findKing(pc);

		markKing(kingPosition.x, kingPosition.y, pc);
		for (Point po : possibleMoves) {
			if (!isCheck(pc, po)) {
				possibleMoves = new ArrayList<>();
				return false;
			}
		}

		possibleMoves = new ArrayList<>();
		return true;
	}
}