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
import static gamechess.Type.BISHOP;
import static gamechess.Type.KNIGHT;
import static gamechess.Type.QUEEN;
import static gamechess.Type.ROOK;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import learnmode.BackNoYes;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class GamePanelAI extends GamePanel {

    private JButton backButton;
    private List<Point> list1 = new ArrayList<>();
    private Piece activeAI;

    public GamePanelAI(JFrame frame) {
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
        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(WHITE, i, 6));
        }
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));

        // Thêm quân cờ đen
        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn(BLACK, i, 1));
        }
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
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
            if (currentColor == WHITE) {
                promoting();
            } else {
                promotingAI();
            }
        } else if (gameOver == false && stalemate == false) {
            if (currentColor == WHITE) {
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
                            isKingInCheck();
                            if(checkingP!=null) System.out.println("checkingP WHITE" + checkingP.type + " " + checkingP.col + " " + checkingP.row  );
                            System.out.println(isKingInCheck());
                            if(checkingP!=null) System.out.println(isCheckmate());
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
            } else {
                // BLACK Player's turn
                aiTurn();
            }
        }
    }
// Phương thức xử lý lượt đi của AI
    List<Point> checkpossibleMoves = new ArrayList<>();
    List<Point> possibleMoves = new ArrayList<>();
    Piece tmp;

    private void aiTurn() {
        int cnt = 0;
        // Tạo danh sách các quân cờ đen
        List<Piece> blackPieces = new ArrayList<>();
        for (Piece piece : simPieces) {
            if (piece.color == BLACK) {
                blackPieces.add(piece);
            }
        }

        // Kiểm tra xem có quân nào có nước đi khả thi không
        for (Piece piece : blackPieces) {
            cnt += 1;
            System.out.println(cnt);
            activeP = piece.clone();
            tmp = piece; // Bản sao quân cờ để không làm thay đổi quân gốc
            System.out.println(activeP.type + " từ " + activeP.col + ", " + activeP.row);

            // Reset các danh sách nước đi
            possibleMoves.clear();
            checkpossibleMoves.clear();

            List<Point> availableCaptures = piece.getAvailableCaptures();
            List<Point> availableMoves = piece.getAvailableMoves();

            // Kết hợp danh sách các nước đi và nước bắt quân
            possibleMoves.addAll(availableCaptures);
            possibleMoves.addAll(availableMoves);
            checkpossibleMoves.addAll(possibleMoves);

            // Kiểm tra tính hợp lệ của nước đi bằng simulateAI()
            simulateAI();

            if (!possibleMoves.isEmpty()) {
                activeP = tmp; // Khôi phục quân cờ gốc
                break; // Thoát vòng lặp nếu tìm thấy nước đi hợp lệ
            }
        }

        if (!possibleMoves.isEmpty()) {
            // Lấy nước đi ngẫu nhiên từ các nước đi hợp lệ
            Random rand = new Random();
            Point randomMove = possibleMoves.get(rand.nextInt(possibleMoves.size()));
            // Kiểm tra xem ô mới có chứa quân cờ không
            Piece capturedPiece = null;
            for (Piece p : simPieces) {
                if (p.col == randomMove.x && p.row == randomMove.y) {
                    capturedPiece = p; // Ghi lại quân cờ bị bắt
                    break;
                }
            }
            for (Piece it : simPieces) {
                if (it.type == Type.KING && it.color == BLACK) {
                    System.out.println("Con vua hiện đang: " + it.type + " từ " + it.col + " " + it.row);
                }
            }
            // Cập nhật vị trí của quân cờ
            simPieces.remove(activeP.getIndex());
            activeP.x = randomMove.x * Board.SQUARE_SIZE;
            activeP.y = randomMove.y * Board.SQUARE_SIZE;
            activeP.col = activeP.getCol(activeP.x);
            activeP.row = activeP.getRow(activeP.y);
            activeP.updatePosition();
            if (castlingP != null) {
                castlingP.updatePosition();
            }
            simPieces.add(activeP); // Thêm quân cờ trở lại danh sách
            // Nếu có quân bị bắt, xóa nó khỏi danh sách
            if (capturedPiece != null) {
                simPieces.remove(capturedPiece.getIndex());
            }
            // Cập nhật danh sách quân cờ
            copyPieces(simPieces, pieces);
            for (Piece it : simPieces) {
                if (it.type == Type.KING && it.color == BLACK) {
                    System.out.println("Con vua hiện đang: " + it.type + " từ " + it.col + " " + it.row);
                }
            }
            if (isKingInCheck() && isCheckmate()) {
                gameOver = true;
            } else if (isStalemate() && isKingInCheck() == false) {
                stalemate = true;
            } else { // The game is still going on
                if (canPromote() == true) {
                    promotion = true;
                } else {
                    activeP = null;
                    changePlayer();
                }
            }
            
            availableMoves = null;
            availableCaptures = null;
        }
        
    }

    private void simulateAI() {
        // Kiểm tra các nước đi khả thi
        Iterator<Point> iter = checkpossibleMoves.iterator();
        while (iter.hasNext()) {
            Point randomMove = iter.next();
            boolean validSquare = false;
            Piece capturedPiece = null;

            // Kiểm tra nếu có quân cờ đối thủ tại vị trí muốn đi
            for (Piece p : simPieces) {
                if (p.col == randomMove.x && p.row == randomMove.y) {
                    capturedPiece = p; // Ghi lại quân cờ bị bắt
                    break;
                }
            }
            // Lưu vị trí ban đầu của activeP
            int originalCol = activeP.col;
            int originalRow = activeP.row;

            // Xóa activeP từ simPieces tại vị trí hiện tại
            int activePIndex = activeP.getIndex();
            if (activePIndex != -1) {
                simPieces.remove(activePIndex); // Chỉ xóa khi tìm thấy activeP
            }
            System.out.println("Xuat phat activeP: " + activeP.type + " " + activeP.col + " " + activeP.row);

            // Cập nhật vị trí mới của activeP
            activeP.x = randomMove.x * Board.SQUARE_SIZE;
            activeP.y = randomMove.y * Board.SQUARE_SIZE;
            activeP.col = activeP.getCol(activeP.x);
            activeP.row = activeP.getRow(activeP.y);
            simPieces.add(activeP); // Thêm lại activeP vào danh sách
            System.out.println("Cập nhật activeP: " + activeP.type + " " + activeP.col + " " + activeP.row);
            // Kiểm tra tính hợp lệ của nước đi
            if (activeP.canMove(activeP.col, activeP.row)) {
                checkCastling(); // Kiểm tra trường hợp nhập thành
                if (capturedPiece != null) {
                    int capturedIndex = capturedPiece.getIndex();
                    if (capturedIndex != -1) {
                        System.out.println("Xóa captured: " + capturedPiece.type + " " + capturedPiece.col + " " + capturedPiece.row);
                        simPieces.remove(capturedIndex); // Xóa quân cờ bị bắt
                    }
                }
                
                System.out.println(isIllegal(activeP));
                System.out.println(opponentCanCaptureKing());
                if (!isIllegal(activeP) && !opponentCanCaptureKing()) {
                    validSquare = true; // Nước đi hợp lệ
                }
            }

            // Khôi phục activeP về vị trí ban đầu
            if (!validSquare) {
                System.out.println("xóa nước đi");
                iter.remove(); // Xóa nước đi không hợp lệ
            }

            // Phục hồi activeP về vị trí ban đầu
            activeP.col = originalCol;
            activeP.row = originalRow;
            activeP.x = originalCol * Board.SQUARE_SIZE;
            activeP.y = originalRow * Board.SQUARE_SIZE;
            activeP.updatePosition();
            if(capturedPiece!=null) simPieces.add(capturedPiece);
            // Phục hồi danh sách simPieces ban đầu
            copyPieces(pieces, simPieces);
        }

        // Cập nhật lại danh sách nước đi khả thi cho possibleMoves
        possibleMoves.clear();
        possibleMoves.addAll(checkpossibleMoves);

        // Khôi phục lại activeP về quân cờ ban đầu
        activeP = tmp;
        copyPieces(pieces, simPieces);
    }


    private void promotingAI() {
        Random random = new Random();

        // Chọn ngẫu nhiên một loại quân từ danh sách các loại quân phong cấp
        int choice = random.nextInt(4); // Có 4 loại quân có thể phong cấp

        switch (choice) {
            case 0:
                simPieces.add(new Rook(currentColor, activeP.col, activeP.row));
                break;
            case 1:
                simPieces.add(new Knight(currentColor, activeP.col, activeP.row));
                break;
            case 2:
                simPieces.add(new Queen(currentColor, activeP.col, activeP.row));
                break;
            case 3:
                simPieces.add(new Bishop(currentColor, activeP.col, activeP.row));
                break;
            default:
                break;
        }

        // Loại bỏ quân tốt đã phong cấp và cập nhật danh sách quân cờ
        simPieces.remove(activeP.getIndex());
        copyPieces(simPieces, pieces);
        promotion = false;
        changePlayer();
    }


    @Override
    public void paintComponent(Graphics g
    ) {
        super.paintComponent(g); // Gọi phương thức của lớp cha
        Graphics2D g2 = (Graphics2D) g;
        for (Piece p : simPieces) {
            p.draw(g2);
        }
    }
}
