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
public class Queen extends Piece{
    
    public Queen(int color, int col, int row) {
        super(color, col, row);
        type = Type.QUEEN;
        if(color == GamePanel.WHITE){
            image = getImage("/images/wQ");
        }
        else{
            image = getImage("/images/bQ");
        }   
    }
    
    public boolean canMove(int targetCol, int targetRow){
        if(isWithinBoard(targetCol,targetRow) && isSameSquare(targetCol,targetRow)==false){
            if(Math.abs(targetCol-preCol)==Math.abs(targetRow-preRow)){
                if(isValidSquare(targetCol,targetRow) && pieceIsOnDiagonalLine(targetCol,targetRow)==false){
                    return true;
                }
            }
            if(targetCol == preCol || targetRow == preRow){
                if(isValidSquare(targetCol,targetRow) && pieceIsOnStraightLine(targetCol,targetRow)==false){
                    return true;
                }
            }
        }
        return false;
    }
}
