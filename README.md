#neoCampus







exit status:
0 : Succes

1 : [FilDeDiscussion.java >  public Message ajouterMessage(Utilisateur u, String m)]
	Un message LOGGER apparaît si l'utilisateur qui envoie le message n'appartient pas au groupe concerné et n'est pas le créateur du fil
 
3 : [Message.java > public void recu(Utilisateur u)]
	L'utilisateur n'est pas en attente du message
	
4 : [Message.java > public void recu(Utilisateur u)]
    L'utilisateur est déjà dans le groupe reçu
    
5 : [Message.java > public void lu(Utilisateur u)]
    L'utilisateur désigné n'est pas dans la liste des messages reçus
    
6 : [Message.java > public void lu(Utilisateur u)]
    L'utilisateur est déja dans lu
	
3333 : [communicationBDD.java > public static Paquet download()]
    Lors du téléchargement du paquet une de ces trois exceptions a été levée :
        FileNotFoundException
        IOException
        ClassNotFoundException
        
        
Fonctionnement :
Première étape : lancer le serveur
Deuxième étape : lancer l'application client avec le login
    La première personne à pouvoir se logger est l'administrateur 0 avec un mdp prédéfini
    Ensuite cet admin peut créer de nouveaux utilisateurs avec tous les paramètres nécessaires
    Les nouveaux utilisateurs ADMIN peuvent à leur tour créer des groupes, des utilisateurs, etc...
    
La Base de Données est stockée dans le fichier NeoCampus.xml, il n'y a rien à faire de particulier, le serveur s'occupe d'écrire et lire dedans.
        
