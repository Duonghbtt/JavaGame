package gamechess;

import static gamechess.GameChess.Interface;
import static gamechess.GameChess.LearnMode;
import static gamechess.GamePanel.BLACK;
import static gamechess.GamePanel.HEIGHT;
import static gamechess.GamePanel.WHITE;
import static gamechess.GamePanel.WIDTH;
import static gamechess.GamePanel.pieces;
import static gamechess.GamePanel.simPieces;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
import static gamechess.BishopPanel.BLACK;
import static gamechess.BishopPanel.HEIGHT;
import static gamechess.BishopPanel.WHITE;
import static gamechess.BishopPanel.WIDTH;
import static gamechess.BishopPanel.pieces;
import learnmode.BackNoYes;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class GamePanelMain extends GamePanel {

    private JButton nextButton;
    private JButton backButton;
    private JTextArea messageArea; // Khai báo JLabel
    private boolean isInteractionEnabled = false;
    private int click = 0;

    public GamePanelMain(JFrame frame) {
        super(frame); // Gọi constructor của GamePanel với JFrame

        // Khởi tạo nút Back
        backButton = new JButton("Back");
        backButton.setBounds(WIDTH - 100, HEIGHT - 50, 80, 30); // Đặt vị trí và kích thước
        backButton.addActionListener(e -> {
            BackNoYes confirmationDialog = new BackNoYes(frame); // Truyền frame chính vào
            confirmationDialog.setVisible(true);
        }); // Gọi hàm Interface() khi nhấn nút
        backButton.setFocusable(false); // Tắt tính năng tiêu điểm cho nút

        setLayout(null); // Để sử dụng setBounds
        add(backButton); // Thêm nút vào GamePanel
    }

    @Override
    public void setPieces() {
        super.setPieces(); // Gọi phương thức setPieces từ GamePanel
        // Thực hiện các thay đổi riêng cho CapturingPanel nếu cần
        pieces.add(new Pawn(WHITE, 0, 6));
        pieces.add(new Pawn(WHITE, 1, 6));
        pieces.add(new Pawn(WHITE, 2, 6));
        pieces.add(new Pawn(WHITE, 3, 6));
        pieces.add(new Pawn(WHITE, 4, 6));
        pieces.add(new Pawn(WHITE, 5, 6));
        pieces.add(new Pawn(WHITE, 6, 6));
        pieces.add(new Pawn(WHITE, 7, 6));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));

        // Black team
        pieces.add(new Pawn(BLACK, 0, 1));
        pieces.add(new Pawn(BLACK, 1, 1));
        pieces.add(new Pawn(BLACK, 2, 1));
        pieces.add(new Pawn(BLACK, 3, 1));
        pieces.add(new Pawn(BLACK, 4, 1));
        pieces.add(new Pawn(BLACK, 5, 1));
        pieces.add(new Pawn(BLACK, 6, 1));
        pieces.add(new Pawn(BLACK, 7, 1));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));
    }

    @Override
    public void changePlayer() {
        super.changePlayer();
        if (currentColor == WHITE) {
            currentColor = BLACK;
            // Reset black's two stepped status
            for (Piece piece : pieces) {
                if (piece.color == BLACK) {
                    piece.twoStepped = false;
                }
            }
        } else {
            currentColor = WHITE;
            // Reset white's two stepped status
            for (Piece piece : pieces) {
                if (piece.color == WHITE) {
                    piece.twoStepped = false;
                }
            }
        }
        activeP = null;
    }

    @Override
    public void update() {
        super.update();
        if (promotion) {
            promoting();
        } else if (gameOver == false && stalemate == false) {
            ///// MOUSE BUTTON PRESSED ////
            if (mouse.pressed) {
                if (activeP == null) {
                    /// if the activeP is null, check if you can pick up a piece
                    for (Piece piece : simPieces) {
                        // If the mouse is on an ally piece, pick it up as the activeP
                        if (piece.color == currentColor
                                && piece.col == mouse.x / Board.SQUARE_SIZE
                                && piece.row == mouse.y / Board.SQUARE_SIZE) {
                            activeP = piece;
                            availableCaptures = activeP.getAvailableCaptures();
                            availableMoves = activeP.getAvailableMoves();
                        }
                    }
                } else {
                    // if the player holding a piece, simulate the move
                    simulate();
                }
            }
            /// MOUSE BUTTON RELEASED ////
            if (mouse.pressed == false) {
                if (activeP != null) {

                    if (validSquare) {
                        // Move confirmed

                        //Update the piece list in case a piece has been captured and removed during the simulation
                        copyPieces(simPieces, pieces);
                        activeP.updatePosition();
                        if (castlingP != null) {
                            castlingP.updatePosition();
                        }
                        //System.out.println(isKingInCheck());
                        if (isKingInCheck() && isCheckmate()) {
                            gameOver = true;
                        } else if (isStalemate() && isKingInCheck() == false) {
                            stalemate = true;
                        } else { // The game is still going on
                            if (canPromote() == true) {
                                promotion = true;
                            } else {
                                changePlayer();
                            }
                        }

                    } else {
                        // the move is not valid so reset everything
                        copyPieces(pieces, simPieces);
                        activeP.resetPosition();
                    }
                    if (!promotion) {
                        activeP = null;
                    }
                    availableMoves = null;
                    availableCaptures = null;
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Gọi phương thức của lớp cha
        Graphics2D g2 = (Graphics2D) g;
        for (Piece p : simPieces) {
            p.draw(g2);
        }
    }

}
