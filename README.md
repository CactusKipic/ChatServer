# ChatSoftware


## Commandes réseaux
Le serveur et le client communiquent ensemble par le biais de commande en début de requête sur 5 caractères.

Commande serveur:
* STDBY: "Stand-by", aucun nouvel évènement
* MDMSG: "Mid-Message", envoie du plus vieux message au client encore en attente et qui n'est pas le dernier
* LAMSG: "Last-Message", envoie du dernier message en date de la conversation
* ACKMS: "Acknowledge message", confirmation de réception du message du client
* ACKCO: "Acknowledge connection", connexion du client enregistré et valide
* REFCO: "Refus connexion", connexion du client refusé (pseudonyme déjà enregistré dans la session)
* UNKWN: "Unknown", requête du client non reconnue
* ENDCO: "End connection", demande de fin de connexion de la part du client

Commande client:
* STDBY: "Stand-by", client en attente, requête de demande d'évènement (nouveau message)
* NXMSG: "Next message", requête de message en attente signalé par le serveur à la requête précédente
* MSGSD: "Message send", envoie d'un message de la part du client
* CTION: "Connection", requête de connexion à une session de chat
* ENDCO: "End connection", requête de fermeture de connection
