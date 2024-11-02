/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gamechess;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import learnmode.LearnModeScreen;

public class GameChess {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessStartScreen startScreen = new ChessStartScreen();
            startScreen.setVisible(true);
        });
    }

    public static void startChessGame() {
        JFrame window = new JFrame("Simple Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        GamePanelMain gp = new GamePanelMain(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void Interface() {
        ChessStartScreen startScreen = new ChessStartScreen();
        startScreen.setVisible(true);
    }

    public static void LearnMode() {
        LearnModeScreen startScreen = new LearnModeScreen();
        startScreen.setVisible(true);
    }

    public static void PawnLearn() {
        JFrame window = new JFrame("Pawn Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        PawPanel gp = new PawPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void KnightLearn() {
        JFrame window = new JFrame("Knight Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        KnightPanel gp = new KnightPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void RookLearn() {
        JFrame window = new JFrame("Rook Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        RookPanel gp = new RookPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void BishopLearn() {
        JFrame window = new JFrame("Bishop Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        BishopPanel gp = new BishopPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void QueenLearn() {
        JFrame window = new JFrame("Queen Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        QueenPanel gp = new QueenPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void KingLearn() {
        JFrame window = new JFrame("King Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        KingPanel gp = new KingPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void CapturingLearn() {
        JFrame window = new JFrame("Capturing Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        CapturingPanel gp = new CapturingPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void NotationLearn() {
        JFrame window = new JFrame("Notation Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        NotationPanel gp = new NotationPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void CheckLearn() {
        JFrame window = new JFrame("Check Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        CheckPanel gp = new CheckPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void GetOutLearn() {
        JFrame window = new JFrame("GetOut Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        GetOutPanel gp = new GetOutPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void StalemateLearn() {
        JFrame window = new JFrame("Stalemate Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        StalematePanel gp = new StalematePanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void CheckmateLearn() {
        JFrame window = new JFrame("Checkmate Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        CheckmatePanel gp = new CheckmatePanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }

    public static void CastlingKingLearn() {
        JFrame window = new JFrame("CastlingKing Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        CastlingKingPanel gp = new CastlingKingPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }
    public static void CastlingQueenLearn() {
        JFrame window = new JFrame("CastlingQueen Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        CastlingQueenPanel gp = new CastlingQueenPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }
    public static void PromotionLearn() {
        JFrame window = new JFrame("Promotion Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        PromotionPanel gp = new PromotionPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }
    public static void EnPassantLearn() {
        JFrame window = new JFrame("EnPassant Learn");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        // Add GamePanel to the window        

        EnPassantPanel gp = new EnPassantPanel(window);
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }
}
