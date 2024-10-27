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
public class Bishop extends Piece{
    
    public Bishop(int color, int col, int row) {
        super(color, col, row);
        type = Type.BISHOP;
        if(color == GamePanel.WHITE){
            image = getImage("/images/wB");
        }
        else{
            image = getImage("/images/bB");
        }
        
    }
    
    public boolean canMove(int targetCol, int targetRow){
        if(isWithinBoard(targetCol,targetRow) && isSameSquare(targetCol,targetRow)==false){
            if(Math.abs(targetCol-preCol)==Math.abs(targetRow-preRow)){
                if(isValidSquare(targetCol,targetRow) && pieceIsOnDiagonalLine(targetCol,targetRow)==false){
                    return true;
                }
            }
        }
        return false;
    }
}
