package gamechess;

import static gamechess.GameChess.Interface;
import java.awt.AlphaComposite;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

/**
 *
 * @author Admin
 */
public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 880;
    public static final int HEIGHT = 640;
    final int FPS = 60;
    private Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();
    private JButton backButton;
    public JFrame frame;
    //Color
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;

    //PIECES
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    ArrayList<Piece> promoPieces = new ArrayList<>();
    public static Piece castlingP;
    Piece activeP, checkingP;
    public List<Point> availableMoves;
    public List<Point> availableCaptures;

    /// BOOLEANS
    boolean canMove;
    boolean validSquare;
    boolean promotion;
    boolean gameOver;
    boolean stalemate;

    public GamePanel(JFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(139, 159, 97));
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        pieces.clear(); // Xóa danh sách quân cờ hiện tại
        simPieces.clear(); // Xóa danh sách quân cờ dự đoán
        setPieces();
        copyPieces(pieces, simPieces);
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setPieces() {

    }

    public void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }

    @Override
    public void run() {
        // Game loop
        double drawInterval = 1000000000 / FPS; // Thời gian giữa các khung hình (FPS: frames per second)
        double delta = 0; // Biến tích lũy để theo dõi thời gian
        long lastTime = System.nanoTime(); // Thời gian hiện tại tính bằng nano giây
        long currentTime;

        while (gameThread != null) { // Khi luồng game còn hoạt động
            currentTime = System.nanoTime(); // Lấy thời gian hiện tại

            delta += (currentTime - lastTime) / drawInterval; // Tính toán delta dựa trên thời gian trôi qua
            lastTime = currentTime; // Cập nhật lastTime

            if (delta >= 1) { // Nếu đủ thời gian để cập nhật
                update(); // Cập nhật trạng thái trò chơi
                repaint(); // Vẽ lại giao diện
                delta--; // Giảm delta đi 1
            }
        }
    }

    public void update() {

    }

    public void simulate() {

        canMove = false;
        validSquare = false;
        // Trả lại danh sách quân cờ trong mỗi vòng lặp
        copyPieces(pieces, simPieces);
        // Trả lại giá trị cho quân cờ nhập thành
        if (castlingP != null) {
            castlingP.col = castlingP.preCol;
            castlingP.x = castlingP.getX(castlingP.col);
            castlingP = null;
        }
        // nếu quân cờ đang được giữ thì cập nhật vị trí
        activeP.x = mouse.x - Board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - Board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);
        if (activeP.canMove(activeP.col, activeP.row)) {
            canMove = true;
            
            if (activeP.hittingP != null) {
                simPieces.remove(activeP.hittingP.getIndex());
            }
            checkCastling();
            if (isIllegal(activeP) == false && opponentCanCaptureKing() == false) {
                validSquare = true;
            }

        }
    }

    public boolean isIllegal(Piece king) {
        if (king.type == Type.KING) {
            for (Piece piece : simPieces) {
                if (piece != king && piece.color != king.color && piece.canMove(king.col, king.row)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean opponentCanCaptureKing() {
        Piece king = getKing(false);
        for (Piece piece : simPieces) {
            if (piece.color != king.color && piece.canMove(king.col, king.row)) {
                return true;
            }
        }
        return false;
    }

    public boolean isKingInCheck() {
        checkingP = null;
        Piece king = getKing(true);
        if (activeP != null && activeP.canMove(king.col, king.row)) {
            
            checkingP = activeP;
            return true;
        } else if (activeP != null && activeP.canMove(king.col, king.row) == false) {
            for (Piece piece : simPieces) {
                if (piece.canMove(king.col, king.row)) {
                    checkingP = piece;
                    return true;
                }
            }
        }
        return false;
    }

    private Piece getKing(boolean opponent) {
        Piece king = null;
        for (Piece piece : simPieces) {
            if (opponent) {
                if (piece.type == Type.KING && piece.color != currentColor) {
                    king = piece;
                }
            } else {
                if (piece.type == Type.KING && piece.color == currentColor) {
                    king = piece;
                }
            }
        }
        return king;
    }

    public boolean isCheckmate() {
        Piece king = getKing(true);

        if (kingCanMove(king)) {
            return false;
        } else {
            // Kiểm tra xem có thể chặn cuộc tấn công bằng quân cờ của mình không
            // Kiểm tra vị trí của quân đang chiếu và quân vua
            int colDiff = Math.abs(checkingP.col - king.col);
            int rowDiff = Math.abs(checkingP.row - king.row);

            if (colDiff == 0) {
                // Kiểm tra chiều dọc 
                if (checkingP.row < king.row) {
                    // Quân chiếu nằm phía trên vua
                    for (int row = checkingP.row; row < king.row; row++) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row)) {
                                return false;
                            }
                        }
                    }
                }
                if (checkingP.row > king.row) {
                    // Quân chiếu nằm phía dưới vưa
                    for (int row = checkingP.row; row > king.row; row--) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(checkingP.col, row)) {
                                return false;
                            }
                        }
                    }
                }
            } else if (rowDiff == 0) {
                //Kiểm tra chiều ngang
                if (checkingP.col < king.col) {
                    // Quân chiếu ở bên trái
                    for (int col = checkingP.col; col < king.col; col++) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row)) {
                                return false;
                            }
                        }
                    }
                }
                if (checkingP.col > king.col) {
                    // Quân chiếu ở bên phải
                    for (int col = checkingP.col; col > king.col; col--) {
                        for (Piece piece : simPieces) {
                            if (piece != king && piece.color != currentColor && piece.canMove(col, checkingP.row)) {
                                return false;
                            }
                        }
                    }

                }
            } else if (colDiff == rowDiff) {
                // Hướng chéo
                if (checkingP.row < king.row) {
                    // Quân chiếu ở trên vua
                    if (checkingP.col < king.col) {
                        // Quân chiếu ở phía trên bên trái
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row++) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                    if (checkingP.col > king.col) {
                        // Quân chiếu ở phía trên bên phải
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row++) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
                if (checkingP.row > king.row) {
                    // Quân chiếu ở dưới vưa
                    if (checkingP.col < king.col) {
                        // Quân chiếu ở dưới bên trái
                        for (int col = checkingP.col, row = checkingP.row; col < king.col; col++, row--) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                    if (checkingP.col > king.col) {
                        //Quân chiếu ở dưới bên phải
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row--) {
                            for (Piece piece : simPieces) {
                                if (piece != king && piece.color != currentColor && piece.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean kingCanMove(Piece king) {
        if (isValidMove(king, -1, -1)) {
            return true;
        }
        if (isValidMove(king, 0, -1)) {
            return true;
        }
        if (isValidMove(king, 1, -1)) {
            return true;
        }
        if (isValidMove(king, -1, 0)) {
            return true;
        }
        if (isValidMove(king, 1, 0)) {
            return true;
        }
        if (isValidMove(king, -1, -1)) {
            return true;
        }
        if (isValidMove(king, 0, 1)) {
            return true;
        }
        if (isValidMove(king, 1, 1)) {
            return true;
        }

        return false;
    }

    private boolean isValidMove(Piece king, int colPlus, int rowPlus) {
        boolean isValidMove = false;

        king.col += colPlus;
        king.row += rowPlus;

        if (king.canMove(king.col, king.row)) {
            if (king.hittingP != null) {
                simPieces.remove(king.hittingP.getIndex());
            }
            if (isIllegal(king) == false) {
                isValidMove = true;
            }
        }
        
        king.resetPosition();
        copyPieces(pieces, simPieces);
        return isValidMove;
    }

    public boolean isStalemate() {
        int count = 0;
        
        for (Piece piece : simPieces) {
            if (piece.color != currentColor) {
                count++;
            }
        }

        
        if (count == 1) {
            if (kingCanMove(getKing(true)) == false) {
                return true;
            }
        }
        return false;
    }

    public void checkCastling() {
        if (castlingP != null) {
            if (castlingP.col == 0) {
                castlingP.col = 3;
            } else if (castlingP.col == 7) {
                castlingP.col = 5;
            }
            castlingP.x = castlingP.getX(castlingP.col);
        }
    }

    public void changePlayer() {

    }

    public boolean canPromote() {
        if (activeP.type == Type.PAWN) {
            if (currentColor == WHITE && activeP.row == 0 || currentColor == BLACK && activeP.row == 7) {
                promoPieces.clear();
                promoPieces.add(new Rook(currentColor, 9, 2));
                promoPieces.add(new Knight(currentColor, 9, 3));
                promoPieces.add(new Bishop(currentColor, 9, 4));
                promoPieces.add(new Queen(currentColor, 9, 5));
                return true;
            }
        }

        return false;
    }

    public void promoting() {
        if (mouse.pressed) {
            for (Piece piece : promoPieces) {
                if (piece.col == mouse.x / Board.SQUARE_SIZE && piece.row == mouse.y / Board.SQUARE_SIZE) {
                    switch (piece.type) {
                        case ROOK:
                            simPieces.add(new Rook(currentColor, activeP.col, activeP.row));
                            break;
                        case KNIGHT:
                            simPieces.add(new Knight(currentColor, activeP.col, activeP.row));
                            break;
                        case QUEEN:
                            simPieces.add(new Queen(currentColor, activeP.col, activeP.row));
                            break;
                        case BISHOP:
                            simPieces.add(new Bishop(currentColor, activeP.col, activeP.row));
                            break;
                        default:
                            break;
                    }
                    simPieces.remove(activeP.getIndex());
                    copyPieces(simPieces, pieces);
                    promotion = false;
                    changePlayer();
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        //board
        board.draw(g2);
        //pieces

        if (activeP != null) {

            if (availableCaptures != null) {
                g2.setColor(new Color(255, 0, 0, 100)); // Màu đỏ nhạt
                for (Point move : availableCaptures) {
                    g2.fillRect(move.x * Board.SQUARE_SIZE, move.y * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                }
            }

            if (availableMoves != null) {
                g2.setColor(new Color(0, 255, 0, 100)); // Màu xanh nhạt
                for (Point move : availableMoves) {
                    g2.fillRect(move.x * Board.SQUARE_SIZE, move.y * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                }
            }

            if (canMove) {
                g2.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(activeP.col * Board.SQUARE_SIZE, activeP.row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

            if (isIllegal(activeP) || opponentCanCaptureKing()) {
                g2.setColor(Color.red);  // Đổi màu chữ "X" thành màu đỏ để dễ thấy
                g2.setFont(new Font("Arial", Font.BOLD, 42));  // Đặt font chữ và kích thước cho chữ "X"

                // Sử dụng FontMetrics để tính toán kích thước chữ "X"
                FontMetrics metrics = g2.getFontMetrics();
                int textWidth = metrics.stringWidth("X");
                int textHeight = metrics.getAscent() - metrics.getDescent();

                // Tính toán tọa độ để chữ "X" nằm chính giữa ô cấm
                int xPosition = activeP.col * Board.SQUARE_SIZE + (Board.SQUARE_SIZE - textWidth) / 2;
                int yPosition = activeP.row * Board.SQUARE_SIZE + (Board.SQUARE_SIZE + textHeight) / 2;

                g2.drawString("X", xPosition, yPosition);  // Vẽ chữ "X" vào tọa độ đã tính
            }
        }

        // STATUS MESSAGES
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Book Antiqua", Font.PLAIN, 32));
        g2.setColor(Color.white);

        if (promotion) {
            g2.drawString("Promote to", 672, 150);
            for (Piece piece : promoPieces) {
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.row), Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
            }
        } else {
            if (currentColor == WHITE) {
                g2.drawString("White's turn", 672, 450);
                if (checkingP != null && checkingP.color == BLACK) {
                    g2.setColor(Color.red);
                    g2.drawString("The King", 672, 550);
                    g2.drawString("Is in check", 672, 600);
                }
            } else {
                g2.drawString("Black's turn", 672, 250);

                if (checkingP != null && checkingP.color == WHITE) {
                    
                    g2.setColor(Color.red);
                    g2.drawString("The King", 672, 100);
                    g2.drawString("Is in check", 672, 150);
                }
            }
        }

        if (gameOver == true) {
            String s = "";
            if (currentColor == WHITE) {
                s = "White Wins";
            } else {
                s = "Black Wins";
            }
            g2.setFont(new Font("Arial", Font.PLAIN, 90));
            g2.setColor(Color.green);
            g2.drawString(s, 200, 420);
        }

        if (stalemate) {
            g2.setFont(new Font("Arial", Font.PLAIN, 90));
            g2.setColor(Color.lightGray);
            g2.drawString("Stalemate", 160, 420);
        }

    }

}
