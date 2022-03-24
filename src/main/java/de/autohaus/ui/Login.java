package de.autohaus.ui;

import de.autohaus.logic.CheckLogin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static de.autohaus.ui.MainGUI.getLogin;

public class Login {
    private JPanel rootPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton abbrechenButton;
    private JButton loginButton;
    private JLabel LIerror;
    private boolean success;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public Login(){
        LIerror.setVisible(false);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (new CheckLogin(textField1.getText(), passwordField1.getText()).check()) {
                    GUI ui = new GUI();
                    JPanel root = ui.getRootPanel();
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setContentPane(root);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);

                    getLogin().setVisible(false);
                    ui.setBenutzerID(textField1.getText());
                } else {
                    LIerror.setVisible(true);
                }
            }
        });
        abbrechenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }


}
