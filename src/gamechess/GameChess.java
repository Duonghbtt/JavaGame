/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gamechess;

import javax.swing.JFrame;

public class GameChess{
    public static void main(String[] args) {
        
        JFrame window = new JFrame("Simple Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        // Add GamePanel to the window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();
        
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        gp.launchGame();
    }
}