package Interface;

import net.Client;
import utilisateurs.GroupeNomme;
import utilisateurs.TypeUtilisateur;
import utilisateurs.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField ident;
    private JTextField mdp;
    private JLabel loginFailed;
    private JTextField ipField;
    private JTextField portField;
    private JLabel adressePortIncorrect;


    public Login() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        ipField.setText("127.0.0.1");
        portField.setText("12700");
        loginFailed.setVisible(false);
        adressePortIncorrect.setVisible(false);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {

        if (!ident.getText().isEmpty()  &&
                ident.getText().matches(".*\\d+.*") &&
                !mdp.getText().isEmpty() &&
                !ipField.getText().isEmpty() &&
                !portField.getText().isEmpty() &&
                portField.getText().matches(".*\\d+.*")){
            Client c = new Client(ident.getText(),Integer.parseInt(portField.getText()));
            int errno = c.connect();
            //TODO AJOUTER VERIF ADRESSE
            if (errno == 1){
                adressePortIncorrect.setVisible(false);
                errno = c.authentification(new Utilisateur("","",Integer.parseInt(ident.getText()),mdp.getText(),null));
                if (errno == 1){
                    loginFailed.setVisible(false);
                    System.out.println("Authentification Reussi!");
                    //En fonction du statut de l'étudiant
                    dispose();
                    Chat chat = new Chat(c);
                    chat.pack();
                    chat.setVisible(true);
                }else{
                    loginFailed.setVisible(true);
                    this.pack();
                }

            }else{
                adressePortIncorrect.setVisible(true);
                this.pack();
            }
        }else{
            loginFailed.setVisible(true);
            this.pack();
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {

        Login dialog = new Login();
        dialog.pack();
        dialog.setVisible(true);
    }
}
