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
public class Pawn extends Piece{
    
    public Pawn(int color, int col, int row) {
        super(color, col, row);
        
        type = Type.PAWN;
        if(color == GamePanel.WHITE){
            image = getImage("/images/wp");
        }
        else{
            image = getImage("/images/bp");
        }  
    }
    
    public boolean canMove(int targetCol, int targetRow){
        if(isWithinBoard(targetCol,targetRow) && isSameSquare(targetCol,targetRow)==false){
            int moveValue;
            if(color == GamePanel.WHITE){
                moveValue = -1;
            }else{
                moveValue = 1;
            }
             // Kiểm tra quân cờ tại vị trí đích.
            hittingP = getHittingP(targetCol, targetRow);
            // Di chuyển 1 ô về phía trước
            if(targetCol == preCol && targetRow == preRow + moveValue && hittingP == null){
                return true;
            }
            
            // Di chuyển hai ô về phía trước, chỉ khi quân cờ chưa từng di chuyển và không có vật cản
            if(targetCol == preCol && targetRow == preRow + moveValue*2 && hittingP == null && moved == false &&
                    pieceIsOnStraightLine(targetCol,targetRow) == false){
                return true;
            }
            
            // Bắt quân theo đường chéo
            if(Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue && hittingP != null && hittingP.color != color){
                return true;
            }
            
            // Bắt tốt qua đường
            if(Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue){
                for(Piece piece: GamePanel.simPieces){
                    if(piece.col == targetCol && piece.row == preRow && piece.twoStepped==true){
                        hittingP = piece;
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
