package de.autohaus.ui;

import de.autohaus.logic.CheckLogin;
import de.autohaus.logic.GetBenutzerAdmin;
import de.autohaus.logic.InsertLogs;

import javax.swing.*;

import static de.autohaus.ui.MainGUI.getLogin;

@SuppressWarnings("deprecation")
public class Login {
    private JPanel rootPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton abbrechenButton;
    private JButton loginButton;
    private JLabel LIError;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public Login(){
        LIError.setVisible(false);

        loginButton.addActionListener(e -> {
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

                new InsertLogs(textField1.getText(),"login", "Login");

                ui.setBenutzerID(textField1.getText());

                if (new GetBenutzerAdmin(textField1.getText()).getAdmin()) {
                    ui.tabbedPane1.setEnabledAt(4, true);
                }

            } else {
                new InsertLogs(textField1.getText(),"login", "Login", true, null);
                LIError.setVisible(true);
            }
        });

        abbrechenButton.addActionListener(e -> {
            new InsertLogs("login", "abbruch");
            System.exit(0);
        });
    }
}
