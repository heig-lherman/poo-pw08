package chess;

/**
 * Regroupe les méthodes appelées par la vue.
 */
public interface ChessController {

    /**
     * Démarre la logique (contrôleur) du programme.
     * Appelé une fois (voir Chess.main())
     *
     * @param view la vue à utiliser
     */
    void start(ChessView view);

    /**
     * Appelé lorsque l'utilisateur a demandé un déplacement de la position X à la position Y.
     * La position 0, 0 est en bas à gauche de l'échiquier.
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return true si le mouvement a pu avoir lieu, false dans le cas contraire.
     */
    boolean move(int fromX, int fromY, int toX, int toY);

    /**
     * Démarre une nouvelle partie. L'échiquier doit être remis dans sa position initiale.
     */
    void newGame();

}