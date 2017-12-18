package GUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigInteger;
import pl.rabin.Main;

import static pl.rabin.Main.*;

public class rabin {

    private JFrame fileChooser;
    private JTextField keyBeforeEncrypted;
    private JTextField messageFieldBeforeEncrypted;
    private JButton encryptButton;
    private JPanel encryptPanel;
    private JPanel decryptPanel;
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
    private JTextField publicKey3;
    private JTextField encryptedFilePath;
    private JButton openFileToDecryptButton;
    private JTextField keyFilePath1;
    private JTextField fileToDecryptPath;
    private JButton encryptFileButton;
    private JButton decryptFileButton;
    private JButton openKeyFileButton;
    private JTextField decryptedFilename;

    private JTextField publicKey1;
    private JTextField publicKey2;
    private JTextField privateKeyP1;
    private JTextField privateKeyP2;
    private JTextField privateKeyQ1;
    private JTextField privateKeyQ2;
    private JTextField messageBeforeEncryptedField;
    private JTextField messageBeforeDecryptedField;
    private JTextField messageAfterEncryptedField;
    private JTextField messageAfterDecryptedField;
    private JTextField privateKeyP3;
    private JTextField privateKeyQ3;
    private JTextField privateKeyP4;
    private JTextField privateKeyQ4;
    private JTextField publicKey4;
    private JTextField encryptedFilename;




    public rabin() {
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageBeforeEncryptedField.getText();
                BigInteger p = new BigInteger(privateKeyP1.getText());
                BigInteger q = new BigInteger(privateKeyQ1.getText());
                BigInteger publicKey = p.multiply(q);
                publicKey1.setText(publicKey.toString());
                //BigInteger[] encryptedMessage = Main.encryptMessage(message.getBytes(), p, q);
                BigInteger[] encryptedMessage = Main.encryptMessage(message.getBytes(), p, q);
                String encryptedMessageString = getEncryptedMessage(encryptedMessage);

                messageAfterEncryptedField.setText(encryptedMessageString);
            }
        });


        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String encryptedMessage = messageAfterEncryptedField.getText();
                BigInteger p = new BigInteger(privateKeyP2.getText());
                BigInteger q = new BigInteger(privateKeyQ2.getText());
                String decryptedMessage = decryptMessage(convertStringToArray(encryptedMessage), p, q);
                messageAfterDecryptedField.setText(decryptedMessage);
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


            }
        });
        encryptFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String encryptedFilenameString = encryptedFilename.getText();
                BigInteger p = new BigInteger(privateKeyP3.getText());
                BigInteger q = new BigInteger(privateKeyQ3.getText());

                if (encryptedFilenameString.equals("")) {
                    JOptionPane.showMessageDialog(getPanel1(), "Error. You must set all fields!");
                    return;
                }

                encryptFile(new File(fileToEncryptPath.getText()).toPath(), encryptedFilenameString, p, q);

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

        decryptFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String encryptedFilename = fileToDecryptPath.getText();
                String decryptedFilename = rabin.this.decryptedFilename.getText();
                BigInteger p = new BigInteger(privateKeyP4.getText());
                BigInteger q = new BigInteger(privateKeyQ4.getText());

                if (encryptedFilename.equals("") || decryptedFilename.equals("")) {
                    JOptionPane.showMessageDialog(getPanel1(), "All fields must be set!");
                    return;
                }

                decryptFile(new File(encryptedFilename).toPath(), decryptedFilename, p, q);
                JOptionPane.showMessageDialog(getPanel1(), "Decrypted file was saved successfully!");

            }
        });

    }


    public JPanel getPanel1() {
        return panel1;
    }

    public static void main(String[] args) {
        JFrame jframe = new JFrame("Rabin");
        jframe.setContentPane(new rabin().panel1);
        jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);

    }

}
