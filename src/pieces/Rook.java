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
public class Rook extends Piece{
    
    public Rook(int color, int col, int row) {
        super(color, col, row);
        
        type =Type.ROOK;
        if(color == GamePanel.WHITE){
            image = getImage("/images/wR");
        }
        else{
            image = getImage("/images/bR");
        }        
    }
    public boolean canMove(int targetCol, int targetRow){
        if(isWithinBoard(targetCol, targetRow )&& isSameSquare(targetCol,targetRow)==false){
            // Rook can move as long as either its col or row is the same
            if(targetCol == preCol || targetRow == preRow){
                if(isValidSquare(targetCol,targetRow) && pieceIsOnStraightLine(targetCol,targetRow)==false){
                    return true;
                }
            }
        }
        return false;
    }
}