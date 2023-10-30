package chess;

/**
 * Méthodes fournies par les vues.
 */
public interface ChessView {

    /**
     * Démarre la vue.
     */
    void startView();

    /**
     * Enlève l'affichage de toute pièce se trouvant sur la case donnée.
     *
     * @param x
     * @param y
     */
    void removePiece(int x, int y);

    /**
     * Affiche la pièce demandée sur la case fournie.
     *
     * @param type
     * @param color
     * @param x
     * @param y
     */
    void putPiece(PieceType type, PlayerColor color, int x, int y);

    /**
     * Affiche un message informatif à l'utilisateur.
     *
     * @param msg le message à afficher.
     */
    void displayMessage(String msg);

    /**
     * A utiliser pour demander un choix à l'utilisateur. La vue utilisera le texte fourni par la méthode
     * pour représenter la valeur du choix proposé.
     */
    interface UserChoice {
        String textValue();
    }

    /**
     * Demande à la vue de proposer un choix à l'utilisateur parmi les options fournies.
     *
     * @param title         le titre de la requête.
     * @param question      la question posée à l'utilisateur.
     * @param possibilities les options parmi lesquelles l'utilisateur doit choisir.
     * @param <T>           Le type du choix
     * @return l'option choisie par l'utilisateur parmis les choix proposés.
     */
    <T extends UserChoice> T askUser(String title, String question, T... possibilities);

}
