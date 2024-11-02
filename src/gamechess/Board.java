/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gamechess;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Admin
 */
public class Board {

    final int MAX_COL = 8;
    final int MAX_ROW = 8;
    public static final int SQUARE_SIZE = 80;
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE / 2;

    public void draw(Graphics2D g2) {

        int c = 0;

        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                if (c == 0) {
                    g2.setColor(new Color(210, 165, 125));
                    c = 1;
                } else {
                    g2.setColor(new Color(175, 115, 70));
                    c = 0;
                }
                g2.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
            if(c==0) c = 1;
            else c = 0;
        }
        
        // Đặt font và màu chữ
        g2.setFont(g2.getFont().deriveFont(12f));

        // Vẽ số từ 8 đến 1 ở cột đầu tiên
        for (int row = 0; row < MAX_ROW; row++) {
            int num = 8 - row;
            int x = 5; // Tọa độ X (cạnh trái của ô)
            int y = row * SQUARE_SIZE + 20; // Tọa độ Y để canh chỉnh lên góc trên

            // Chọn màu chữ ngược với màu ô
            if (row % 2 == 0) g2.setColor(new Color(175, 115, 70));
            else g2.setColor(new Color(210, 165, 125));

            g2.drawString(String.valueOf(num), x, y);
        }

        // Vẽ các chữ từ 'a' đến 'h' ở hàng cuối cùng
        for (int col = 0; col < MAX_COL; col++) {
            char letter = (char) ('a' + col);
            int x = col * SQUARE_SIZE + 70; // Tọa độ X ở góc dưới phải của ô
            int y = MAX_ROW * SQUARE_SIZE - 5; // Tọa độ Y để canh chỉnh bên dưới

            // Chọn màu chữ ngược với màu ô
            if (col % 2 == 0) g2.setColor(new Color(210, 165, 125));
            else g2.setColor(new Color(175, 115, 70));

            g2.drawString(String.valueOf(letter), x, y);
        }
    }
}
