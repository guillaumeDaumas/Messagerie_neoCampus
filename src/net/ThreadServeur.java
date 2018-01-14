package net;

import BDD.ExtractDataBDD;
import discussion.FilDeDiscussion;
import utilisateurs.Groupe;
import utilisateurs.GroupeNomme;
import utilisateurs.Utilisateur;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentSkipListSet;

public class ThreadServeur implements Runnable {

    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    boolean connecte;
    Serveur serveur;

    public ThreadServeur( Socket socket,Serveur serveur) {
        this.socket = socket;
        this.serveur = serveur;


        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.connecte =true;
        } catch (IOException e) {
            e.printStackTrace();
            this.connecte=false;
        }
    }


    @Override
    public void run() {
        Groupe global;
        ConcurrentSkipListSet<GroupeNomme> listeGroupe;
        while(connecte){
            try {
                Paquet paquet = (Paquet) in.readObject();
                if(paquet.getAction()== Paquet.Action.AUTHENTIFICATION){
                    System.out.println("Demande d'authentification de "+paquet.getUtilisateur().getIdentifiant());

                    Paquet retour = authentification(paquet);
                    out.writeObject(retour);
                }
                else if(paquet.getAction()== Paquet.Action.REQUETTE){
                    
                    System.out.println("Demande de téléchargement de "+paquet.getUtilisateur().getIdentifiant());

//                    Paquet retour = new Paquet(Paquet.Action.REPONSE,paquet.getUtilisateur(),listeGroupe,global);
                    Paquet retour = SimuBDD.download();
                    /*Paquet retour = null;
                    try {
                        retour = ExtractDataBDD.download();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/

                    retour.setAction(Paquet.Action.REPONSE);
                    retour.setUtilisateur(paquet.utilisateur);
                    out.writeObject(retour);
                }else if(paquet.getAction()== Paquet.Action.REPONSE){
                   
                    System.out.println("Envoi infos depuis le Client " + paquet.getUtilisateur().getIdentifiant());
                    // serveur.maj(paquet.getListeGroupe(),paquet.getGroupeGlobal());
                    SimuBDD.upload(new Paquet(null,null,paquet.getListeGroupe(),paquet.getGlobal()));
                    if ( ! paquet.getListeGroupe().isEmpty()) {
                        System.out.println(paquet.getListeGroupe().first().getFilsDeDiscussion());
                    }
                }
                
                else if (paquet.getAction()== Paquet.Action.DECONNECT){
                    System.out.println("Déconnexion de "+paquet.getUtilisateur());
                    connecte=false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }//while
        /*try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    synchronized Paquet authentification(Paquet paquet){
        Groupe global;

        Paquet bdd = SimuBDD.download();
        global = bdd.getGlobal();
        Utilisateur co =null;
        for(Utilisateur u : global.getMembres() ){
            if(u.getIdentifiant()==paquet.getUtilisateur().getIdentifiant()  && u.getMotDePasse().equals(paquet.getUtilisateur().getMotDePasse())){
                co =u;
            }
        }
        System.out.println(co);
        return new Paquet(Paquet.Action.AUTHENTIFICATION,co,null,null);
    }

}


