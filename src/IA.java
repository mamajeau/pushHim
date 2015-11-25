import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.lang.reflect.Array;
import java.util.*;

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
    int nombreNoeud=5;
    Mouvement mouvementAFaire;
    public IA(Plateau plateau)
    {
        this.plateau=plateau;
    }

    public void choixCoup()
    {
        startTime = System.currentTimeMillis();
        Random randomizer = new Random();
        //Minmax represente le noeud racine duquel decoulera le reste de l'arbre
        minMax = new Noeud(plateau.board,null);
        //Appel recursif de la fonction qui va generer les noeuds du minmax
        genererNoeuds(minMax,plateau,this.couleur,0);
        mouvementAFaire = null;
        int max;
        max = elagageAlphaBeta(minMax, 0, 100, -100);
        System.out.println("=================>>" + max);
        //Mouvement mouvementAFaire = minMax.listeEnfant.get(randomizer.nextInt(minMax.listeEnfant.size())).mouvementFait;
    }

    //Fonction qui evaluera un plateau et lui assignera une valeur
    private int evaluerVieux(int[] board){
        Random randomizer = new Random();
        int poids = randomizer.nextInt(50);
        return poids;
    }

    //Fonction elagage

    private int elagageAlphaBeta(Noeud racine, int profondeur, int min, int max){
        int score = 0;
        long stopTime = System.currentTimeMillis();
        if ((stopTime - startTime) > 400000){
            return 0;
        }
        if (racine.listeEnfant.size() == 0){
            return racine.mouvementFait.poids;
        }
        //Si c'est pair, on cherche un max
        if(profondeur == 0){
            for (int i=0; i<racine.listeEnfant.size(); i++) {
                score = elagageAlphaBeta(racine.listeEnfant.get(i), profondeur + 1, min, max);
                if (score >= max) {
                    max = score;
                    if (profondeur == 0) {
                        mouvementAFaire = racine.listeEnfant.get(i).mouvementFait;
                    }
                }
            }
            return max;
        }
        else if(profondeur % 2 == 0){
            for (int i=0; i<racine.listeEnfant.size(); i++) {
                score = elagageAlphaBeta(racine.listeEnfant.get(i), profondeur + 1, min, max);
                if (score >= max) {
                    max = score;
                    if (profondeur == 0) {
                        mouvementAFaire = racine.listeEnfant.get(i).mouvementFait;
                    }
                }
            }
            return max;
        }
        else{
            for (int i=0; i<racine.listeEnfant.size(); i++){
                score = elagageAlphaBeta(racine.listeEnfant.get(i),profondeur + 1,min,max);
                if (score <= min){
                    min = score;
                }
            }
            return min;
        }
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

        if (poidsBlanc>poidsNoir)
        {
            poidsBlanc+=1000;
        }

        //Calcule du poids par rapport a la position

            for (int z = 0; z < plateau.board.length; z++) {
                if (plateau.board[z] == plateau.POUSSE_BLANC || plateau.board[z] == plateau.POUSSEUR_BLANC) {
                    //Ceci cacule la ligne de la piece -36, car on doit voir le board a lenvers
                    double hauteur = Math.ceil(Math.abs(z - 63) / 8);
                    poidsNoir += hauteur ;
                } else if (plateau.board[z] == plateau.POUSSE_NOIR || plateau.board[z] == plateau.POUSSEUR_NOIR) {
                    //Ceci cacule la ligne de la piece
                    double hauteur = Math.ceil(z / 8);
                    poidsBlanc += hauteur;
                }
            }
            //La bonne couleur moins la couleur inverse pour savoir si notre poids est plus grande que l<autre
            if (couleur) {
                poids = poidsBlanc - poidsNoir;
            } else {
                poids = poidsNoir - poidsBlanc;
            }

        System.out.println(poids);
        return poids;
    }

    //Fonction qui genere le noeuds, racine represente le noeud parents des enfants qui seront attaches. Pour l'instant, on genere
    //un enfant par mouvement possible et on y associe un poid aleatoire. A chaque appel, on inverse la couleur utilisee precedemment.
    private void genererNoeuds(Noeud racine, Plateau p,boolean couleur, int profondeur){
        long stopTime = System.currentTimeMillis();
        //L'arbre va arreter de faire apres 4 secondes
        if ((stopTime - startTime) > 2000){
            return;
        }
        if (profondeur > 3){
            return;
        }
            ArrayList<Mouvement> mouvementsPossibles;
            mouvementsPossibles = p.genererMouvements(couleur);
            //Boucle qui creera et associera chaque mouvement possible a la racine passee en parametre
            for (int i = 0; i < mouvementsPossibles.size(); i++) {
                int[] boardEnfant = p.deplacerDansBoard(mouvementsPossibles.get(i).ligneDepart, mouvementsPossibles.get(i).colonneDepart, mouvementsPossibles.get(i).ligneArrivee, mouvementsPossibles.get(i).colonneArrivee);
                mouvementsPossibles.get(i).poids = evaluationPlateau(new Plateau(boardEnfant));
                racine.ajouterEnfant(new Noeud(boardEnfant, mouvementsPossibles.get(i)));
            }
                //Boucle qui fera l'appel recursif pour chaque enfant ajoute dans la boucle precedente.
            for (int i = 0; i < racine.listeEnfant.size(); i++) {
                genererNoeuds(racine.listeEnfant.get(i), new Plateau(racine.listeEnfant.get(i).board), !couleur, profondeur + 1);
            }
        }
    /* private void genererNoeuds(Noeud racine, Plateau p,boolean couleur,boolean type){
        //Type=1 =>blanc Type=0 =>noir
        long stopTime = System.currentTimeMillis();
        LinkedList<Integer> plusHaut=new LinkedList<>();
        LinkedList<Integer> plusBas= new LinkedList<>();
        int maxValue=-1000;
        int minValue=1000;

        //L'arbre va arreter de faire apres 3 secondes

        //Bon mais en commentiare pour des tests seulement
      /* if ((stopTime - startTime) > 3000){
            return;
        }

        ArrayList<Mouvement> mouvementsPossibles;
        mouvementsPossibles = p.genererMouvements(couleur);

        //Boucle qui creera et associera chaque mouvement possible a la racine passee en parametre
        for (int i = 0; i < mouvementsPossibles.size(); i++) {
            int[] boardEnfant = p.deplacerDansBoard(mouvementsPossibles.get(i).ligneDepart, mouvementsPossibles.get(i).colonneDepart, mouvementsPossibles.get(i).ligneArrivee, mouvementsPossibles.get(i).colonneArrivee);
            //mouvementsPossibles.get(i).poids = evaluerVieux(boardEnfant);
            //Test
            // Pour linstant ca lair que je ne prend pas le bon board, fak ca va devoir etre checker
            int poids=evaluationPlateau(new Plateau(boardEnfant));
            mouvementsPossibles.get(i).poids=poids;

            //contruction da la liste des meilleurs noeuds

            if(type)
            {
                if (poids>=maxValue)
                {
                    if(poids==maxValue && plusHaut.size()==nombreNoeud)
                    {
                        //Cette partie est pour le fait que nous aillons 12 valeurs maximum, on choisit au hasard lesquels
                        Random random = new Random();
                        if(random.nextBoolean())
                        {
                            plusHaut.removeFirst();
                            plusHaut.add(i);
                        }
                    }
                    else
                    {
                        if(plusHaut.size()>nombreNoeud)
                        {
                            plusHaut.removeFirst();
                        }
                        plusHaut.add(i);
                    }
                    if (poids>maxValue){
                        maxValue = poids;
                    }
                }
            }
            else
            {
                if (poids<=minValue)
                {
                    if(poids==minValue && plusBas.size()==nombreNoeud)
                    {
                        //Cette partie est pour le fait que nous aillons 12 valeurs maximum, on choisit au hasard lesquels
                        Random random = new Random();
                        if(random.nextBoolean())
                        {
                            plusBas.removeFirst();
                            plusBas.add(i);
                        }
                    }
                    else
                    {
                        if(plusBas.size()>nombreNoeud)
                        {
                            plusBas.removeFirst();
                        }
                        plusBas.add(i);
                    }
                    if (poids>minValue){
                        minValue = poids;
                    }
                }
            }
        }
        System.out.println("Liste plus haut=====");
        System.out.println(plusHaut);
        System.out.println("=================");
        System.out.println("Liste plus bas=====");
        System.out.println(plusBas);
        System.out.println("=================");
        //Ajout des noeuds de la liste de plus hautes valeurs
        if(type)
        {
            for (int j=0;j<plusHaut.size();j++)
            {
                int i=plusHaut.get(j);
                int[] boardEnfant = p.deplacerDansBoard(mouvementsPossibles.get(i).ligneDepart, mouvementsPossibles.get(i).colonneDepart, mouvementsPossibles.get(i).ligneArrivee, mouvementsPossibles.get(i).colonneArrivee);
                racine.ajouterEnfant(new Noeud(boardEnfant, mouvementsPossibles.get(i)));
            }
        }
        else
        {
            for (int j=0;j<plusBas.size();j++)
            {
                int i=plusBas.get(j);
                int[] boardEnfant = p.deplacerDansBoard(mouvementsPossibles.get(i).ligneDepart, mouvementsPossibles.get(i).colonneDepart, mouvementsPossibles.get(i).ligneArrivee, mouvementsPossibles.get(i).colonneArrivee);
                racine.ajouterEnfant(new Noeud(boardEnfant, mouvementsPossibles.get(i)));
            }
        }


        //boucle qui va ajouter les 5 nouveaux noeuds

        //Boucle qui fera l'appel recursif pour chaque enfant ajoute dans la boucle precedente.
        for (int i = 0; i < racine.listeEnfant.size(); i++) {
            Jeu j = new Jeu();
            j.construirePlateau(racine.listeEnfant.get(i).getBoard());
            genererNoeuds(racine.listeEnfant.get(i), j.plateau, !couleur);
        }
}*/

    public String jouerCoup()
    {
        choixCoup();

        //On deplace dans notre plateau global
        System.out.println(mouvementAFaire.colonneDepart + " " + mouvementAFaire.ligneDepart);
        String deplacement=plateau.deplacer(mouvementAFaire.ligneDepart,mouvementAFaire.colonneDepart,mouvementAFaire.ligneArrivee,mouvementAFaire.colonneArrivee);
        System.out.println("Notre coup: "+deplacement);

        return deplacement;

    }
}
