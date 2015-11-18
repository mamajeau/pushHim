import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by maaj on 2015-11-02.
 */
public class IA {
    Plateau plateau;
    Convertisseur conv = Convertisseur.getInstance();
    Noeud minMax;
    boolean couleur;
    long startTime;

    public IA(Plateau plateau)
    {
        this.plateau=plateau;
    }

    public Mouvement choixCoup()
    {
        startTime = System.currentTimeMillis();
        int[][] board = plateau.board;
        Random randomizer = new Random();
        //Minmax represente le noeud racine duquel decoulera le reste de l'arbre
        minMax = new Noeud(board,null);
        //Appel recursif de la fonction qui va generer les noeuds du minmax
        genererNoeuds(minMax,plateau,this.couleur);

        Mouvement mouvementAFaire = minMax.listeEnfant.get(randomizer.nextInt(minMax.listeEnfant.size())).mouvementFait;
        return mouvementAFaire;
    }

    //Fonction qui evaluera un plateau et lui assignera une valeur
    private int evaluerPlateau(int[][] board){
        Random randomizer = new Random();
        int poids = randomizer.nextInt(50);
        return poids;
    }
    //Fonction qui genere le noeuds, racine represente le noeud parents des enfants qui seront attaches. Pour l'instant, on genere
    //un enfant par mouvement possible et on y associe un poid aleatoire. A chaque appel, on inverse la couleur utilisee precedemment.
    private void genererNoeuds(Noeud racine, Plateau p,boolean couleur){
        long stopTime = System.currentTimeMillis();
        //L'arbre va arreter de faire apres 4 secondes
       if ((stopTime - startTime) > 3000){
            return;
        }
        ArrayList<Mouvement> mouvementsPossibles;
        mouvementsPossibles = p.genererMouvements(couleur);
        //Boucle qui creera et associera chaque mouvement possible a la racine passee en parametre
        for (int i = 0; i < mouvementsPossibles.size(); i++) {
            int[][] boardEnfant = p.deplacerDansBoard(mouvementsPossibles.get(i).ligneDepart, mouvementsPossibles.get(i).colonneDepart, mouvementsPossibles.get(i).ligneArrivee, mouvementsPossibles.get(i).colonneArrivee);
            mouvementsPossibles.get(i).poids = evaluerPlateau(boardEnfant);
            racine.ajouterEnfant(new Noeud(boardEnfant, mouvementsPossibles.get(i)));
        }
        //Boucle qui fera l'appel recursif pour chaque enfant ajoute dans la boucle precedente.
        for (int i = 0; i < racine.listeEnfant.size(); i++) {
            Jeu j = new Jeu();
            j.construirePlateau(racine.listeEnfant.get(i).getBoard());
            genererNoeuds(racine.listeEnfant.get(i), j.plateau, !couleur);
        }
}

    public String jouerCoup()
    {
        Mouvement mouvementAFaire=null;
        mouvementAFaire = choixCoup();

        //On deplace dans notre plateau global
        String deplacement=plateau.deplacer(mouvementAFaire.ligneDepart,mouvementAFaire.colonneDepart,mouvementAFaire.ligneArrivee,mouvementAFaire.colonneArrivee);


        return deplacement;
    }
}
