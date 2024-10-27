/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pieces;

import gamechess.GamePanel;
import gamechess.Type;

/**
 *
 * @author Admin
 */
public class Knight extends Piece {

    public Knight(int color, int col, int row) {
        super(color, col, row);
        type = Type.KNIGHT;
        if (color == GamePanel.WHITE) {
            image = getImage("/images/wN");
        } else {
            image = getImage("/images/bN");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow)) {
            // Tính toán khoảng cách di chuyển
            int colDiff = Math.abs(targetCol - preCol); // khoảng cách cột
            int rowDiff = Math.abs(targetRow - preRow); // khoảng cách hàng

            // Quân mã di chuyển theo tỷ lệ 1:2 hoặc 2:1
            if ((colDiff == 2 && rowDiff == 1) || (colDiff == 1 && rowDiff == 2)) {
                if (isValidSquare(targetCol, targetRow)) {
                    return true;
                } // Kiểm tra ô có thể di chuyển đến
            }
        }
        return false; // Không thể di chuyển
    }

}
