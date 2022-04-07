package de.autohaus.ui;

import javax.swing.*;

import static de.autohaus.model.Connect.tryConnect;

public class MainGUI {
    private static JFrame frameLogin;

    public static void main(String[] args) {
        setLook();
        if (tryConnect()) {
            SwingUtilities.invokeLater(MainGUI::createGUI);
        } else {
            System.err.println("No connection to Database!");
        }
    }

    private static void createGUI(){
        Login ui = new Login();
        JPanel root = ui.getRootPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frameLogin = frame;
    }

    public static JFrame getLogin(){
        return frameLogin;
    }

    private static void setLook(){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }
}
