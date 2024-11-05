package gamechess;

import static gamechess.GameChess.Interface;
import static gamechess.GameChess.LearnMode;
import gamechess.GamePanel;
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
import static gamechess.GameChess.LearnMode;
import static gamechess.GameChess.NextMode;
import static gamechess.GamePanel.BLACK;
import static gamechess.GamePanel.HEIGHT;
import static gamechess.GamePanel.WHITE;
import static gamechess.GamePanel.WIDTH;
import static gamechess.GamePanel.castlingP;
import static gamechess.GamePanel.pieces;
import static gamechess.GamePanel.simPieces;
import learnmode.BackNoYes;
import learnmode.BackNoYes1;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class BishopPanel extends GamePanel {
    private JButton nextButton;
    private JButton backButton;
    private JTextArea messageArea; // Khai báo JLabel
    private boolean isInteractionEnabled = false;
    private int click = 0;
    private int ok = 0;

    public BishopPanel(JFrame frame) {
        super(frame); // Gọi constructor của GamePanel với JFrame
        setPreferredSize(new Dimension(WIDTH, HEIGHT + 110));
        setLayout(null); // Sử dụng layout null để đặt vị trí cho nút
        // Khởi tạo và thêm JLabel
        messageArea = new JTextArea("Quân tượng\n Quân Tượng di chuyển bất kỳ số ô trông nào theo đường chéo. Tượng không thể nhảy qua các quân cờ khác. ");
        messageArea.setBounds(0, 650, 770, 110); // Đặt vị trí và kích thước cho JTextArea
        messageArea.setForeground(Color.BLACK); // Thiết lập màu chữ
        messageArea.setFont(new Font("Serif", Font.BOLD, 18)); // Thiết lập font chữ
        messageArea.setEditable(false); // Không cho phép chỉnh sửa
        messageArea.setLineWrap(true); // Cho phép xuống dòng
        messageArea.setWrapStyleWord(true); // Giữ nguyên từ khi xuống dòng

        add(messageArea); // Thêm JTextArea vào BoardPanel

        // Khởi tạo nút "Next"
        nextButton = new JButton("Next");
        nextButton.setBounds(WIDTH - 100, HEIGHT +110- 50, 80, 30); // Đặt vị trí cho nút (x, y, width, height)

        // Thêm ActionListener cho nút
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Thực hiện hành động khi nút được nhấn
                click +=1;
                System.out.println("Next button clicked");
                messageArea.setText("Hãy thử di chuyển quân Tượng đến bất kỳ ô hợp lệ nào.");
                if(click==1){
                    isInteractionEnabled = true;
                    nextButton.setEnabled(false);
                }
            }
        });

        backButton = new JButton("Back");
        backButton.setBounds(WIDTH - 100, HEIGHT +110- 100, 80, 30); // Đặt vị trí và kích thước
        backButton.addActionListener(e -> {
            BackNoYes1 confirmationDialog = new BackNoYes1(frame); // Truyền frame chính vào
            confirmationDialog.setVisible(true);
        }); // Gọi hàm Interface() khi nhấn nút
        backButton.setFocusable(false); // Tắt tính năng tiêu điểm cho nút

        add(backButton); // Thêm nút vào GamePanel
        add(nextButton); // Thêm nút vào BoardPanel
    }

    @Override
    public void setPieces() {
        super.setPieces(); // Gọi phương thức setPieces từ GamePanel
        // Thực hiện các thay đổi riêng cho CapturingPanel nếu cần
        pieces.add(new Pawn(WHITE, 4, 5));
        pieces.add(new Pawn(BLACK, 4, 3));
        pieces.add(new Bishop(WHITE,4,4));
        pieces.add(new King(WHITE, 4, 7));
        pieces.add(new King(BLACK, 4, 0));
    }
    
    @Override
    public void changePlayer() {
        super.changePlayer();
        isInteractionEnabled = false;
        ok = 1;
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
        if(ok==1){
        NextMode();
        frame.dispose();
        ok = 0;
        }
        if (isInteractionEnabled) {
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
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Gọi phương thức của lớp cha
        g.setColor(Color.WHITE);
        g.fillRect(0, 640, 880, 110);
        Graphics2D g2 = (Graphics2D) g;
        for (Piece p : simPieces) {
           if (p.type != Type.KING) {
                p.draw(g2);
            }
        }        
    }

}
