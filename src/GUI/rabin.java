package GUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static pl.rabin.Main.*;

public class rabin {

    private JFrame fileChooser;
    private JTextField keyBeforeEncrypted;
    private JTextField messageFieldBeforeEncrypted;
    private JButton encryptButton;
    private JPanel encryptPanel;
    private JPanel decryptPanel;
    private JTextField encryptedMessageFieldBeforeDecrypted;
    private JTextField keyBeforeDecrypted;
    private JButton decryptButton;
    private JTextField messageFieldAfterDecrypted;
    private JTextField encryptedMessageFieldAfterEncrypted;
    private JLabel encryptedMessageLabel2;
    private JLabel saltLabel2;
    private JLabel saltLabel1;
    private JLabel messageLabel2;
    private JLabel messageLabel1;
    private JLabel encryptedMessageLabel1;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton openFileToEncryptButton;
    private JTextField fileToEncryptPath;
    private JTextField keyFilePath;
    private JTextField encryptedFilePath;
    private JButton openFileToDecryptButton;
    private JTextField keyFilePath1;
    private JTextField fileToDecryptPath;
    private JButton encryptFileButton;
    private JButton decryptFileButton;
    private JButton openKeyFileButton;
    private JTextField decryptedFilePath;
    private byte[] bytesKey;


    public rabin() {
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageFieldBeforeEncrypted.getText();
                String key = keyBeforeEncrypted.getText();
                String decryptedMessage = encryptMessage(message, key);
                encryptedMessageFieldAfterEncrypted.setText(decryptedMessage);
                encryptedMessageFieldBeforeDecrypted.setText(decryptedMessage);
            }
        });

        messageFieldBeforeEncrypted.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            void update() {
                String message = messageFieldBeforeEncrypted.getText();
                int size = message.length();
                String keyValue = generateRandomString(size);
                keyBeforeEncrypted.setText(null);
                keyBeforeEncrypted.setText(String.valueOf(keyValue));
                keyBeforeDecrypted.setText(String.valueOf(keyValue));

                encryptedMessageFieldAfterEncrypted.setText(null);
                encryptedMessageFieldBeforeDecrypted.setText(null);
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String encryptedMessage = encryptedMessageFieldBeforeDecrypted.getText();
                String salt = keyBeforeDecrypted.getText();
                String message = decryptMessage(encryptedMessage, salt);
                messageFieldAfterDecrypted.setText(message);
            }
        });

        openFileToEncryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                int rVal = c.showOpenDialog(fileChooser);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    File file = c.getSelectedFile();

                    fileToEncryptPath.setText(file.getPath());
                }
            }
        });
        fileToEncryptPath.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            void update() {
                String filename = fileToEncryptPath.getText();

                if (filename.equals("")) {
                    return;
                }

                setBytesKey(generateRandomByte(getFileSize(filename)));
            }
        });
        encryptFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyValueFilename = keyFilePath.getText();
                String encryptedFilename = encryptedFilePath.getText();

                if (keyValueFilename.equals("") || encryptedFilename.equals("")) {
                    JOptionPane.showMessageDialog(getPanel1(), "Error. You must set all fields!");
                    return;
                }

                saveByteArrayToFile(getBytesKey(), keyValueFilename);
                encryptFile(new File(
                        fileToEncryptPath.getText()).toPath(),
                        encryptedFilename,
                        convertFileToByteArray(new File(keyValueFilename).toPath()
                        ));

                JOptionPane.showMessageDialog(getPanel1(), "Encrypted successfully.");

            }
        });

        openFileToDecryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c= new JFileChooser();
                int rVal = c.showOpenDialog(fileChooser);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    String decryptedFilename = c.getSelectedFile().getPath();
                    fileToDecryptPath.setText(decryptedFilename);
                }

            }
        });

        openKeyFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                int rVal = c.showOpenDialog(fileChooser);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    String keyFilename = c.getSelectedFile().getPath();
                    keyFilePath1.setText(keyFilename);
                }
            }
        });

        decryptFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyFilename = keyFilePath1.getText();
                String encryptedFilename = fileToDecryptPath.getText();
                String decryptedFilename = decryptedFilePath.getText();

                if (keyFilename.equals("") || encryptedFilename.equals("") || decryptedFilename.equals("")) {
                    JOptionPane.showMessageDialog(getPanel1(), "All fields must be set!");
                    return;
                }

                byte[] byteKey = convertFileToByteArray(new File(keyFilename).toPath());
                decryptFile(new File(encryptedFilename).toPath(), decryptedFilename, byteKey);
                JOptionPane.showMessageDialog(getPanel1(), "Decrypted file was saved successfully!");

            }
        });

    }

    public byte[] getBytesKey() {
        return bytesKey;
    }

    public void setBytesKey(byte[] bytesKey) {
        this.bytesKey = bytesKey;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public static void main(String[] args) {
        JFrame jframe = new JFrame("OneTimePad");
        jframe.setContentPane(new OneTimePad().panel1);
        jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);

    }

}
