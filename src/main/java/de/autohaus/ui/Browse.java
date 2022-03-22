package de.autohaus.ui;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class Browse {
    private JPanel rootPanel;
    private JFileChooser fileChooser;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private String linkFile;

    public String getLinkFile() {
        return linkFile;
    }

    public Browse() {
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            this.linkFile = selectedFile.getAbsolutePath();
        }
    }
}
