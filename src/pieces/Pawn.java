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
            // Difine the move value based on its color
            int moveValue;
            if(color == GamePanel.WHITE){
                moveValue = -1;
            }else{
                moveValue = 1;
            }
            
            // Check the hittng piece
            hittingP = getHittingP(targetCol, targetRow);
            
            // 1 square movment
            if(targetCol == preCol && targetRow == preRow + moveValue && hittingP == null){
                return true;
            }
            
            // 2square.movement
            if(targetCol == preCol && targetRow == preRow + moveValue*2 && hittingP == null && moved == false &&
                    pieceIsOnStraightLine(targetCol,targetRow) == false){
                return true;
            }
            
            // Diagonal movement & Capture (if a piece is on a square diagonally in front of it)
            if(Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue && hittingP != null && hittingP.color != color){
                return true;
            }
            
            // En Pasant
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
