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
        //this.board.toString();
        construireListePousseur();
    }


    /**
     *****************
     * Liste Pousseur*
     *****************
     */

    public void construireListePousseur()
    {
        pousseurBlanc = new ArrayList<>();
        pousseurNoir = new ArrayList<>();
        pousseBlanc = new ArrayList<>();
        pousseNoir = new ArrayList<>();


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
        int pion =  board[((ligne)*8)+colonne];

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

        int[] coord,coord_cible,coord_cible2;
        int cible,cible2;


        if(couleur){
            for (int pos: pousseurBlanc) {
                coord = new int[]{pos % 8, pos / 8};//col,ligne

                if((coord[1]-1) > 0) { //Fin du tableau
                    if (coord[0] != 0) {//gauche
                        coord_cible = new int[]{(pos % 8)-1, (pos / 8)-1};
                        cible = getBoardValue(coord_cible[0],coord_cible[1]);
                        if(cible == POUSSE_BLANC) //gauche pousse
                        {
                            if((coord_cible[1]-1) > 0) {//Fin du tableau
                                if (coord_cible[0] != 0) {
                                    coord_cible2 = new int[]{(pos % 8)-2, (pos / 8)-2};
                                    cible2 = getBoardValue(coord_cible2[0],coord_cible2[1]);

                                    if((cible2 != POUSSE_BLANC && cible2 !=POUSSEUR_BLANC) && (cible2 ==0 || cible2 == POUSSE_NOIR|| cible2 == POUSSEUR_NOIR))
                                    {
                                        arrayMouvements.add(new Mouvement(coord_cible[1],coord_cible[0],coord_cible2[1],coord_cible2[0]));
                                    }

                                }
                            }
                        }else if(cible !=POUSSEUR_BLANC && (cible ==0 || cible == POUSSE_NOIR|| cible == POUSSEUR_NOIR)) {
                            arrayMouvements.add(new Mouvement(coord[1], coord[0], coord_cible[1], coord_cible[0]));
                        }

                    }



                    //haut
                        coord_cible = new int[]{(pos % 8), (pos / 8)-1};
                        cible = getBoardValue(coord_cible[0],coord_cible[1]);
                    if(cible == POUSSE_BLANC) //haut pousse
                    {
                        if((coord_cible[1]-1) > 0) { //Fin du tableau
                            coord_cible2 = new int[]{(pos % 8), (pos / 8)-2};
                            cible2 = getBoardValue(coord_cible2[0],coord_cible2[1]);
                            if(cible2 == POUSSE_BLANC || cible2 == POUSSE_NOIR || cible2 == POUSSEUR_NOIR || cible2 == POUSSEUR_BLANC)
                            {
                                //fait rien
                            }else if((cible2 == 0 ))
                            {
                                arrayMouvements.add(new Mouvement(coord_cible[1],coord_cible[0],coord_cible2[1],coord_cible2[0]));
                            }
                        }
                    }else if(cible == 0 ) {
                        arrayMouvements.add(new Mouvement(coord[1], coord[0], coord_cible[1], coord_cible[0]));
                    }




                    if (coord[0] != 7) {//droite
                        coord_cible = new int[]{(pos % 8)+1, (pos / 8)-1};
                        cible = getBoardValue(coord_cible[0],coord_cible[1]);
                        if(cible == POUSSE_BLANC) //droite pousse
                        {
                            if((coord_cible[1]-1) > 0) { //Fin du tableau
                                if (coord_cible[0] != 7) {
                                    coord_cible2 = new int[]{(pos % 8)+2, (pos / 8)-2};
                                    cible2 = getBoardValue(coord_cible2[0],coord_cible2[1]);
                                    if(cible2 != POUSSE_BLANC && cible2 !=POUSSEUR_BLANC){
                                        if((cible2 ==0 || cible2 == POUSSE_NOIR|| cible2 == POUSSEUR_NOIR))
                                        {
                                            arrayMouvements.add(new Mouvement(coord_cible[1],coord_cible[0],coord_cible2[1],coord_cible2[0]));
                                        }}

                                }
                            }
                        }else if(cible !=POUSSEUR_BLANC) {
                            if ((cible == 0 || cible == POUSSE_NOIR || cible == POUSSEUR_NOIR)) {
                                arrayMouvements.add(new Mouvement(coord[1], coord[0], coord_cible[1], coord_cible[0]));
                            }
                        }

                    }

                }
            }
        }else{ //Noir
            for (int pos: pousseurNoir) {
                coord = new int[]{pos % 8, pos / 8};//col,ligne

                if((coord[1]+1) < 7) { //Fin du tableau

                    if (coord[0] != 0) {//gauche
                        coord_cible = new int[]{(pos % 8)-1, (pos / 8)+1};
                        cible = getBoardValue(coord_cible[0],coord_cible[1]);
                        if(cible == POUSSE_NOIR) //gauche pousse
                        {
                            if((coord_cible[1]+1) < 7) {//Fin du tableau
                                if (coord_cible[0] != 0) {
                                    coord_cible2 = new int[]{(pos % 8)-2, (pos / 8)+2};
                                    cible2 = getBoardValue(coord_cible2[0],coord_cible2[1]);
                                    //System.out.println(cible2);
                                    if(cible2 != POUSSE_NOIR && cible2 !=POUSSEUR_NOIR ) {
                                        if ((cible2 == 0 || cible2 == POUSSE_BLANC || cible2 == POUSSEUR_BLANC)) {
                                            arrayMouvements.add(new Mouvement(coord_cible[1], coord_cible[0], coord_cible2[1], coord_cible2[0]));
                                        }
                                    }

                                }
                            }
                        }else if(cible !=POUSSEUR_NOIR) {
                            if ((cible == 0 || cible == POUSSE_BLANC || cible == POUSSEUR_BLANC)) {
                                arrayMouvements.add(new Mouvement(coord[1], coord[0], coord_cible[1], coord_cible[0]));
                            }
                        }

                    }


                    //haut
                    coord_cible = new int[]{(pos % 8), (pos / 8)+1};
                    cible = getBoardValue(coord_cible[0],coord_cible[1]);
                    if(cible == POUSSE_NOIR) //haut pousse
                    {
                        if((coord_cible[1]+1) < 7) { //Fin du tableau
                            coord_cible2 = new int[]{(pos % 8), (pos / 8)+2};
                            cible2 = getBoardValue(coord_cible2[0],coord_cible2[1]);
                            if(cible2 == POUSSE_BLANC || cible2 == POUSSE_NOIR || cible2 == POUSSEUR_NOIR || cible2 == POUSSEUR_BLANC)
                            {
                                //fait rien
                            }else if(cible2 == 0 )
                            {
                                arrayMouvements.add(new Mouvement(coord_cible[1],coord_cible[0],coord_cible2[1],coord_cible2[0]));
                            }

                        }
                    }else if(cible == 0 )
                    {
                            arrayMouvements.add(new Mouvement(coord[1], coord[0], coord_cible[1], coord_cible[0]));

                    }




                    if (coord[0] != 7) {//droite
                        coord_cible = new int[]{(pos % 8)+1, (pos / 8)+1};
                        cible = getBoardValue(coord_cible[0],coord_cible[1]);
                        if(cible == POUSSE_NOIR) //droite pousse
                        {
                            if((coord_cible[1]+1) < 7) { //Fin du tableau
                                if (coord_cible[0] != 7) {
                                    coord_cible2 = new int[]{(pos % 8)+2, (pos / 8)+2};
                                    cible2 = getBoardValue(coord_cible2[0],coord_cible2[1]);
                                    if(cible2 != POUSSE_NOIR && cible2 !=POUSSEUR_NOIR){
                                        if((cible2 ==0 || cible2 == POUSSE_BLANC|| cible2 == POUSSEUR_BLANC))
                                        {
                                            arrayMouvements.add(new Mouvement(coord_cible[1],coord_cible[0],coord_cible2[1],coord_cible2[0]));
                                        }}

                                }
                            }
                        }
                        else if( cible !=POUSSEUR_NOIR) {
                            if ((cible == 0 || cible == POUSSE_BLANC || cible == POUSSEUR_BLANC)) {
                                arrayMouvements.add(new Mouvement(coord[1], coord[0], coord_cible[1], coord_cible[0]));
                            }
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
        board[((ligneArrivee)*8)+colonneArrivee]=board[((ligneDepart)*8)+colonneArrivee];
        System.out.print("Switch !!!!!!!!!!!!!!!!!!!!!!!!!!");
        board[((ligneDepart) * 8) +colonneDepart] = 0;
       // afficherBoard();
        String idDepart = conv.ChiffreALettre(colonneDepart) + "" + ((8-ligneDepart));
        String idArrivee = conv.ChiffreALettre(colonneArrivee) + "" + ((8-ligneArrivee));

        miseAJourPousseur(ligneDepart, colonneDepart, ligneArrivee, colonneArrivee);
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
    {String ligne="";
        for (int i=0; i<64; i++) {

            if(i%8==0){
            System.out.println(ligne);
            ligne = "";

            }
            ligne+=(board[i] + "("+i+")");
        }
        System.out.println(ligne);
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
    */
    //Fonction pour mettre a jour les pousseur x == ligne  y==colonne
    public void miseAJourPousseur(int xDepart, int yDepart, int xArrivee, int yArrivee){

        construireListePousseur();
        /*
        for(int i = 0; i<=7; i++){
            if(pousseurBlanc.get(i) % 8 == yDepart && pousseurBlanc.get(i)/8 == xDepart){
                System.out.println("Mise a jour P blanc");
                pousseurBlanc.remove(i);
                pousseurBlanc.add((xArrivee*8)+ yArrivee);
            }else if(pousseurNoir.get(i)%8 == yDepart && pousseurNoir.get(i)/8 == xDepart){
                System.out.println("Mise a jour P noir");
                pousseurNoir.remove(i);
                pousseurNoir.add((xArrivee*8)+ yArrivee);
            }else if(pousseNoir.get(i)%8 == yDepart && pousseNoir.get(i)/8 == xDepart){
                System.out.println("Mise a jour p noir");
                pousseNoir.remove(i);
                pousseNoir.add((xArrivee*8)+ yArrivee);
            }else if(pousseBlanc.get(i)%8 == yDepart && pousseBlanc.get(i)/8 == xDepart){
                System.out.println("Mise a jour p noir");
                pousseBlanc.remove(i);
                pousseBlanc.add((xArrivee*8)+ yArrivee);
            }
        }*/
    }

}