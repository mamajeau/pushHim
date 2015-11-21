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

    //Valeur arbitaire pour tout changer partout
    int poidsPousseur=2;
    int poidsPousse=1;
    int multiple=2;

    public IA(Plateau plateau)
    {
        this.plateau=plateau;
    }

    public Mouvement choixCoup()
    {
        startTime = System.currentTimeMillis();
        Random randomizer = new Random();
        //Minmax represente le noeud racine duquel decoulera le reste de l'arbre
        minMax = new Noeud(plateau.board,null);
        //Appel recursif de la fonction qui va generer les noeuds du minmax
        genererNoeuds(minMax,plateau,this.couleur);

        Mouvement mouvementAFaire = minMax.listeEnfant.get(randomizer.nextInt(minMax.listeEnfant.size())).mouvementFait;
        return mouvementAFaire;
    }

    //Fonction qui evaluera un plateau et lui assignera une valeur
    private int evaluerVieux(int[] board){
        Random randomizer = new Random();
        int poids = randomizer.nextInt(50);
        return poids;
    }

    //Fonction devaluation
    private int evaluationPlateau(Plateau plateau)
    {
        int poidsNoir=0;
        int poidsBlanc=0;
        int poids=0;
        //Calcule du nombre de piece
        poidsBlanc+=plateau.pousseBlanc.size()*poidsPousse;
        poidsBlanc+=plateau.pousseurBlanc.size()*poidsPousseur;

        poidsNoir+=plateau.pousseNoir.size()*poidsPousse;
        poidsNoir+=plateau.pousseurNoir.size()*poidsPousseur;

        //Calcule du poids par rapport a la position
        for(int z=0;z< plateau.board.length;z++)
        {
            if(plateau.board[z]==plateau.POUSSE_BLANC || plateau.board[z]==plateau.POUSSEUR_BLANC)
            {
                //Ceci cacule la ligne de la piece -36, car on doit voir le board a lenvers
                poidsNoir+=Math.ceil(Math.abs(z-63)/8)*multiple;
            }
            else if (plateau.board[z]==plateau.POUSSE_NOIR || plateau.board[z]==plateau.POUSSEUR_NOIR)
            {
                //Ceci cacule la ligne de la piece
                poidsBlanc+=Math.ceil(z/8)*multiple;
            }
        }
        //La bonne couleur moins la couleur inverse pour savoir si notre poids est plus grande que l<autre
        if(couleur)
        {
            poids=poidsBlanc-poidsNoir;
        }
        else
        {
            poids=poidsNoir-poidsBlanc;
        }
        System.out.println("couleur"+this.couleur);
        return poids;
    }

    //Fonction qui genere le noeuds, racine represente le noeud parents des enfants qui seront attaches. Pour l'instant, on genere
    //un enfant par mouvement possible et on y associe un poid aleatoire. A chaque appel, on inverse la couleur utilisee precedemment.
    private void genererNoeuds(Noeud racine, Plateau p,boolean couleur){
        long stopTime = System.currentTimeMillis();
        //L'arbre va arreter de faire apres 3 secondes
       if ((stopTime - startTime) > 3000){
            return;
        }
        ArrayList<Mouvement> mouvementsPossibles;
        mouvementsPossibles = p.genererMouvements(couleur);
        //Boucle qui creera et associera chaque mouvement possible a la racine passee en parametre
        for (int i = 0; i < mouvementsPossibles.size(); i++) {
            int[] boardEnfant = p.deplacerDansBoard(mouvementsPossibles.get(i).ligneDepart, mouvementsPossibles.get(i).colonneDepart, mouvementsPossibles.get(i).ligneArrivee, mouvementsPossibles.get(i).colonneArrivee);
            //mouvementsPossibles.get(i).poids = evaluerVieux(boardEnfant);
            //Test
            mouvementsPossibles.get(i).poids=evaluationPlateau(p);
            System.out.println("==============================");
            System.out.println(mouvementsPossibles.get(i).poids);

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
        System.out.println(mouvementAFaire.colonneDepart + " " + mouvementAFaire.ligneDepart);
        String deplacement=plateau.deplacer(mouvementAFaire.ligneDepart,mouvementAFaire.colonneDepart,mouvementAFaire.ligneArrivee,mouvementAFaire.colonneArrivee);
        System.out.println("Notre coup: "+deplacement);

        return deplacement;

    }
}
