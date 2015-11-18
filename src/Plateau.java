import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by maaj on 015-11-02.
 *
 * i = ligne ex. 1,2,...
 * j = colonne ex. A,B,...
 */
public class Plateau implements Cloneable{

    public final static int POUSSE_BLANC = 3;
    public final static int POUSSEUR_BLANC = 4;
    public final static int POUSSE_NOIR = 1;
    public final static int POUSSEUR_NOIR = 2;
    int[] board;

    Convertisseur conv = Convertisseur.getInstance();
    private ArrayList<Integer> pousseurNoir = new ArrayList<>();
    private ArrayList<Integer> pousseurBlanc = new ArrayList<>();
    private ArrayList<Integer> pousseNoir = new ArrayList<>();
    private ArrayList<Integer> pousseBlanc = new ArrayList<>();

    public Plateau(int[] board){
        this.board = board;
        this.board.toString();
        construireListePousseur();
    }


    /**
     *****************
     * Liste Pousseur*
     *****************
     */

    public void construireListePousseur()
    {
        //Ajoute les pousseur a leur liste
        for(int i=0; i<64;i++){
            switch (board[i])
            {
                case POUSSEUR_BLANC:
                    pousseurBlanc.add(i);
                    break;
                case POUSSEUR_NOIR:
                    pousseurNoir.add(i);
                    break;
                case POUSSE_BLANC:
                    pousseBlanc.add(i);
                    break;
                case POUSSE_NOIR:
                    pousseNoir.add(i);
                    break;
            }
        }
    }

    /**Un clone du board au complet parce que le .clone() fail.
     *
     * @return un clone du board
     */
    public int[] clonerBoard(){
        int [] clone = new int [64];
        for (int i =0; i<64; i++){

            clone[i] = board[i];

        }
        return clone;
    }

    /**
     *  Fonction retournant la valeur
     *  d'une casse du tableau
     *
     *      colonne
     *     l+++++++
     *     i+++++++
     *     g+++++++
     *     n+++++++
     *     e+++++++
     *
     *  Ex. D1 = getBoardValue(3,0)
     * @param ligne (j) de 0 a 7
     * @param colonne (i) de 0 a 7
     * @return la valeur de la case
     */

    public int getBoardValue(int colonne,int ligne)
    {
        int pion = -1;
        if(((ligne)*8)+colonne > 0 && ((ligne)*8)+colonne < 64)
        {
            pion = board[((ligne)*8)+colonne];
        }

        return pion;
    }

    /**
     * Fonction qu'on a a appeler pour generer
     * les mouvements en fonction d'un plateau
     *
     * @param couleur
     * @return
     */
    public ArrayList<Mouvement> genererMouvements(boolean couleur){

        ArrayList<Mouvement> arrayMouvements = new ArrayList<Mouvement>();
        ArrayList<Integer> pousseurs = null;

        int cible,ennemi1,ennemi2,pousse,direction;

        if(couleur){
            pousseurs = pousseurBlanc;
            ennemi1 = POUSSE_NOIR;
            ennemi2 = POUSSEUR_NOIR;
            pousse = POUSSE_BLANC;
            direction = -1;
        }else{
            pousseurs = pousseurNoir;
            ennemi1 = POUSSE_BLANC;
            ennemi2 = POUSSEUR_BLANC;
            pousse = POUSSE_NOIR;
            direction = + 1;
        }

        for (int i = 0; i < pousseurs.size(); i++) {

            int position = pousseurs.get(i);
            //gauche blanc - droite noir
            if((position % 8 != 0 && direction == 1)||(position % 8 != 7 && direction == -1)) {
                cible = board[position + (direction * 7)];
                //case libre ou mange ennemi
                if (cible == 0 || cible == ennemi1 || cible == ennemi2) {
                    arrayMouvements.add(new Mouvement(position/8,position%8,(position+(direction * 7))/8,(position+(direction * 7))%8));
                }
                //pousse
                if(cible == pousse)
                {   if((position+(direction * 7))%8 != 0) {
                    int cible2 = board[position + (direction * 14)];
                    //case libre ou mange ennemi
                    if (cible2 == 0 || cible2 == ennemi1 || cible2 == ennemi2) {
                        arrayMouvements.add(new Mouvement((position + (direction * 7)) / 8, (position + (direction * 7)) % 8, (position + (direction * 14)) / 8, (position + (direction * 14)) % 8));
                    }
                }
                }
            }
            //haut
            cible = board[position+(direction * 8)];
            //case libre
            if(cible == 0)
            {
                arrayMouvements.add(new Mouvement(position/8,position%8,(position+(direction * 8))/8,position%8));
            }
            //pousse
            if(cible == pousse)
            {
                if(board[position+(direction * 16)] == 0)
                {
                    arrayMouvements.add(new Mouvement((position+(direction * 8))/8,position%8,(position+(direction * 16))/8,position%8));
                }
            }

            //droite blanc - gauche noir
            if((position % 8 != 0 && direction == -1)||(position % 8 != 7 && direction == 1)) {
                cible = board[position + (direction * 9)];
                //case libre ou mange ennemi
                if (cible == 0 || cible == ennemi1 || cible == ennemi2) {
                    arrayMouvements.add(new Mouvement(position/8,position%8,(position+(direction * 9))/8,(position+(direction * 9))%8));
                }
                //pousse
                if(cible == pousse)
                {   if(((position+(9*direction)) % 8 != 0 && direction == -1)||((position + (9*direction)) % 8 != 7 && direction == 1)) {
                    int cible2 = board[position + (direction * 18)];
                    //case libre ou mange ennemi
                    if (cible2 == 0 || cible2 == ennemi1 || cible2 == ennemi2) {
                        arrayMouvements.add(new Mouvement((position + (direction * 9)) / 8, (position + (direction * 9)) % 8, (position + (direction * 18)) / 8, (position + (direction * 18)) % 8));
                    }
                }
                }
            }
        }

        return arrayMouvements;
    }

    //Fonction pour la mise a jour des mouvements de l'adversaire dans notre board
    public void updateJoueur(String coordonner)
    {
        Convertisseur convertisseur = Convertisseur.getInstance();
        //Couper en deux la coordonner
        coordonner=coordonner.replaceAll("\\s","");
        coordonner=coordonner.replaceAll("[\u0000-\u001f]", "");
        String[] sousResultat=coordonner.split("-");
        //On va chercher ligne colonne de depart
        int colonneDepart = convertisseur.LettreAChiffre(sousResultat[0].charAt(0));
        int ligneDepart = 8 - (Integer.parseInt((String)(""+ sousResultat[0].charAt(1))));

        int colonneArrivee = convertisseur.LettreAChiffre(sousResultat[1].charAt(0));
        int ligneArrivee = 8 - (Integer.parseInt((String)(""+sousResultat[1].charAt(1))));

        //Mise a jour des listes de pousseur
        //miseAJourPousseur(ligneDepart, colonneDepart, ligneArrivee, colonneArrivee);
        deplacer(ligneDepart,colonneDepart,ligneArrivee,colonneArrivee);
    }


    /** Fonction pour deplacer sur les cases et faire la verification
     *
     *
     * */
    public String deplacer(int ligneDepart, int colonneDepart, int ligneArrivee, int colonneArrivee)
    {
        Convertisseur conv = Convertisseur.getInstance();
        //Deplacer dans les mapping
        board[((ligneArrivee) * 8)+colonneArrivee]=getBoardValue(ligneDepart,colonneDepart);
        board[((ligneDepart) * 8) +colonneDepart] = 0;
        // afficherBoard();
        String idDepart = conv.ChiffreALettre(colonneDepart) + "" + ((8-ligneDepart));
        String idArrivee = conv.ChiffreALettre(colonneArrivee) + "" + ((8-ligneArrivee));
        return idDepart+idArrivee;


    }

    /**
     * Fonction pour modifier seulement les boards des enfants de la racines
     * @param ligneDepart
     * @param colonneDepart
     * @param ligneArrivee
     * @param colonneArrivee
     * @return
     */
    public int[] deplacerDansBoard(int ligneDepart, int colonneDepart, int ligneArrivee, int colonneArrivee)
    {
        Convertisseur conv = Convertisseur.getInstance();
        //Deplacer dans les mapping
        int[]boardAModifier = clonerBoard();
        boardAModifier[((ligneArrivee)*8) + colonneArrivee]=boardAModifier[((ligneDepart)*8)+colonneDepart];
        boardAModifier[((ligneDepart)*8) + colonneDepart] = 0;
        return boardAModifier;
    }

    //petite fonction pour passer  travers le board pour afficher les cases
    public void afficherBoard()
    {
        for (int i=0; i<64; i++) {
            String ligne="";
            ligne+=board[i];

            System.out.println(ligne);
        }
    }
/*
	//Fonction pour addicher les pousseur
	public void afficherPousseur(){
		String blanc="";
		String noir="";
		for (int i=0; i<8; i++) {
			blanc += pousseurBlanc.get(i).toString() + ", ";
			noir += pousseurNoir.get(i).toString() + ", ";
		}
		System.out.println(blanc);
		System.out.println(noir);
	}

	//Fonction pour mettre a jour les pousseur
	public void miseAJourPousseur(int xDepart, int yDepart, int xArrivee, int yArrivee){
		for(int i = 0; i<=7; i++){
			if(pousseurBlanc.get(i).getY() == yDepart && pousseurBlanc.get(i).getX() == xDepart){
				System.out.println("Mise a jour blanc");
				pousseurBlanc.remove(i);
				pousseurBlanc.add(i, new Pousseur(true, xArrivee, yArrivee));
			}else if(pousseurNoir.get(i).getY() == yDepart && pousseurNoir.get(i).getX() == xDepart){
				System.out.println("Mise a jour noir");
				pousseurNoir.remove(i);
				pousseurNoir.add(i, new Pousseur(false, xArrivee, yArrivee));
			}
		}
	}*/

}