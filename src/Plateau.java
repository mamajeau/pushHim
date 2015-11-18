import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by maaj on POUSSEUR_NOIR015-11-0POUSSEUR_NOIR.
 */
public class Plateau implements Cloneable{
    public static int POUSSE_BLANC = 3;
    public static int POUSSEUR_BLANC = 4;
    public static int POUSSE_NOIR = 1;
    public static int POUSSEUR_NOIR = 2;
    int[][] board;
    Convertisseur conv = Convertisseur.getInstance();

    public Plateau(int[][] board){
        this.board = board;

        //afficherBoard();
        //afficherCase();
    }
    //Un clone du board au complet parce que le .clone() fail.
    public int[][] clonerBoard(){
        int [][] clone = new int [8][8];
        for (int i =0; i<8; i++){
            for (int j=0; j<8 ; j++){
              clone[i][j] =board[i][j];
            }
        }
        return clone;
    }
    //Fonction qu'on a a appeler pour generer les mouvements en fonction d'un plateau (Tu vois mamajeau jmets des commentaires)
    public ArrayList<Mouvement> genererMouvements(boolean couleur){
        ArrayList<Mouvement> arrayMouvementsNoirs = new ArrayList<Mouvement>();
        ArrayList<Mouvement> arrayMouvementsBlancs = new ArrayList<Mouvement>();
        for (int i=0; i<8; i++) {
            for (int j = 0; j < 8; j++) {
                int pion = board[i][j];
                    if (pion == POUSSEUR_NOIR || pion == POUSSEUR_BLANC) {
                        if (pion == POUSSEUR_BLANC && i >0) {
                            //Deplacement tout droit pour pousseur blanc
                            if (board[i - 1][j] == 0) {
                                Mouvement m = new Mouvement(i, j, i - 1, j);
                                arrayMouvementsBlancs.add(m);
                            }
                            //Deplacement a droite pour pousseur blanc
                            if (j != 7) {
                                if (board[i - 1][j + 1] == 0) {
                                    Mouvement m = new Mouvement(i, j, i - 1, j + 1);
                                    arrayMouvementsBlancs.add(m);
                                }
                                //Si la case d'arrivee est occupee par un mechant
                                else if (board[i - 1][j + 1] != 0 && (board[i - 1][j + 1] == POUSSEUR_NOIR || board[i - 1][j + 1]== POUSSE_NOIR)) {
                                    Mouvement m = new Mouvement(i, j, i - 1, j + 1);
                                    arrayMouvementsBlancs.add(m);
                                }
                            }
                            //Deplacement a gauche pour pousseur blanc
                            if (j != 0) {
                                if (board[i - 1][j - 1] == 0) {
                                    Mouvement m = new Mouvement(i, j, i - 1, j - 1);
                                    arrayMouvementsBlancs.add(m);
                                }
                                //Si la case d'arrivee est occupee par un mechant
                                else if (board[i - 1][j - 1] != 0 && (board[i - 1][j - 1] == POUSSEUR_NOIR || board[i - 1][j - 1] == POUSSE_NOIR)) {
                                    Mouvement m = new Mouvement(i, j, i - 1, j - 1);
                                    arrayMouvementsBlancs.add(m);
                                }
                            }
                        }
                        //Deplacement tout droit pour pousseur noir
                        else if (pion == POUSSEUR_NOIR && i<7) {
                            if (board[i + 1][j] == 0) {
                                Mouvement m = new Mouvement(i, j, i + 1, j);
                                arrayMouvementsNoirs.add(m);
                                //Deplacement a droite pousseur noir
                            }
                            if (j != 7) {
                                if (board[i + 1][j + 1] == 0) {
                                    Mouvement m = new Mouvement(i, j, i + 1, j + 1);
                                    arrayMouvementsNoirs.add(m);
                                }
                                //Si la case d'arrivee est occupee par un mechant
                                else if (board[i + 1][j + 1] != 0 && (board[i + 1][j + 1] == POUSSE_BLANC || board[i + 1][j + 1] == POUSSEUR_BLANC)) {
                                    Mouvement m = new Mouvement(i, j, i + 1, j + 1);
                                    arrayMouvementsNoirs.add(m);
                                }
                            }
                            //Deplacement a gauche pour pousseur noir
                            if (j != 0) {
                                if (board[i + 1][j - 1] == 0) {
                                    Mouvement m = new Mouvement(i, j, i + 1, j - 1);
                                    arrayMouvementsNoirs.add(m);
                                }
                                //Si la case d'arrivee est occupee par un mechant
                                else if (board[i + 1][j - 1] != 0 && (board[i + 1][j - 1] == POUSSE_BLANC || board[i + 1][j - 1] == POUSSEUR_BLANC)) {
                                    Mouvement m = new Mouvement(i, j, i + 1, j - 1);
                                    arrayMouvementsNoirs.add(m);
                                }
                            }
                        }
                    } else if (pion == POUSSE_NOIR || pion == POUSSE_BLANC) {
                        //Si le pousse est blanc
                        if (pion == POUSSE_BLANC && i>0) {
                            //Deplacement tout droit pour pousse blanc
                            try {
                                if (board[i - 1][j] == 0 && board[i + 1][j] == POUSSEUR_BLANC) {
                                    Mouvement m = new Mouvement(i, j, i - 1, j);
                                    arrayMouvementsBlancs.add(m);
                                }
                            } catch (NullPointerException e) {
                                System.out.println("Il n'y a pas de pousseur a la case ");
                            }
                            //Deplacement a droite pour pousse blanc
                            if (j < 7 && j != 0) {
                                try {
                                    if (board[i - 1][j + 1] == 0 && board[i + 1][j - 1] == POUSSEUR_BLANC) {
                                        Mouvement m = new Mouvement(i, j, i - 1, j + 1);
                                        arrayMouvementsBlancs.add(m);
                                    }
                                    //Si la case d'arrivee est occupee par un mechant
                                    else if (board[i - 1][j + 1] != 0 && (board[i - 1][j + 1] == POUSSEUR_NOIR || board[i - 1][j + 1] == POUSSE_NOIR) && board[i + 1][j - 1] == POUSSEUR_BLANC) {
                                        Mouvement m = new Mouvement(i, j, i - 1, j + 1);
                                        arrayMouvementsBlancs.add(m);
                                    }
                                } catch (NullPointerException e) {
                                    System.out.println("Il n'y a pas pousseur a la case ");
                                }
                            }
                            //Deplacement a gauche pour pousse blanc
                            if (j > 0 && j != 7) {
                                try {
                                    if (board[i - 1][j - 1] == 0 && board[i + 1][j + 1] == POUSSEUR_BLANC) {
                                        Mouvement m = new Mouvement(i, j, i - 1, j - 1);
                                        arrayMouvementsBlancs.add(m);
                                    }
                                    //Si la case d'arrivee est occupee par un mechant
                                    else if (board[i - 1][j - 1] != 0 && (board[i - 1][j - 1] == POUSSEUR_NOIR || board[i - 1][j - 1] == POUSSE_NOIR) && board[i + 1][j + 1] == POUSSEUR_BLANC) {
                                        Mouvement m = new Mouvement(i, j, i - 1, j - 1);
                                        arrayMouvementsBlancs.add(m);
                                    }
                                } catch (NullPointerException e) {
                                    System.out.println("Il n'y a pas pousseur a la case ");
                                }
                            }
                        } else if (pion == POUSSE_NOIR && i<7) {
                            //Deplacement tout droit pour pousse noir
                            try {
                                if (board[i + 1][j] == 0 && board[i - 1][j] == POUSSEUR_NOIR) {
                                    Mouvement m = new Mouvement(i, j, i + 1, j);
                                    arrayMouvementsNoirs.add(m);
                                }
                            } catch (NullPointerException e) {
                                System.out.println("Il n'y a pas pousseur a la case ");
                            }
                            //Deplacement a droite pour pousse noir
                            if (j < 7 && j != 0) {
                                try {
                                    if (board[i + 1][j + 1] == 0 && board[i - 1][j - 1] == POUSSEUR_NOIR) {
                                        Mouvement m = new Mouvement(i, j, i + 1, j + 1);
                                        arrayMouvementsNoirs.add(m);
                                    }
                                    //Si la case d'arrivee est occupee par un mechant
                                    else if (board[i + 1][j + 1] != 0 && (board[i + 1][j + 1] == POUSSE_BLANC || board[i + 1][j + 1] == POUSSEUR_BLANC) && board[i - 1][j - 1] == POUSSEUR_NOIR) {
                                        Mouvement m = new Mouvement(i, j, i + 1, j + 1);
                                        arrayMouvementsNoirs.add(m);
                                    }
                                } catch (NullPointerException e) {
                                    System.out.println("Il n'y a pas d'occupant a la case ");
                                }
                            }
                            //Deplacement a gauche pour pousse noir
                            if (j > 0 && j != 7) {
                                try {
                                    if (board[i + 1][j - 1] == 0 && board[i - 1][j + 1] == POUSSEUR_NOIR) {
                                        Mouvement m = new Mouvement(i, j, i + 1, j - 1);
                                        arrayMouvementsNoirs.add(m);
                                    }
                                    //Si la case d'arrivee est occupee par un mechant
                                    else if (board[i + 1][j - 1] != 0 && (board[i + 1][j - 1] == POUSSE_BLANC || board[i + 1][j - 1] == POUSSEUR_BLANC) && board[i - 1][j + 1] == POUSSEUR_NOIR) {
                                        Mouvement m = new Mouvement(i, j, i + 1, j - 1);
                                        arrayMouvementsNoirs.add(m);
                                    }
                                } catch (NullPointerException e) {
                                    System.out.println("Il n'y a pas pousseur a la case ");
                                }
                            }
                        }
                }
            }
        }
        ArrayList<Mouvement> arrayMouvements = null;
        if (couleur) {
            arrayMouvements = arrayMouvementsBlancs;
        }
        else {
            arrayMouvements = arrayMouvementsNoirs;
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
    //Fonction pour deplacer sur les cases et faire la verification
    public String deplacer(int ligneDepart, int colonneDepart, int ligneArrivee, int colonneArrivee)
    {
        Convertisseur conv = Convertisseur.getInstance();
        //Deplacer dans les mapping
        board[ligneArrivee][colonneArrivee]=board[ligneDepart][colonneDepart];
        board[ligneDepart][colonneDepart] = 0;
       // afficherBoard();
        String idDepart = conv.ChiffreALettre(colonneDepart) + "" + (8- ligneDepart);
        String idArrivee = conv.ChiffreALettre(colonneArrivee) + "" + (8 -ligneArrivee);
        return idDepart+idArrivee;
    }
    //Fonction pour modifier seulement les boards des enfants de la racines
    public int[][] deplacerDansBoard(int ligneDepart, int colonneDepart, int ligneArrivee, int colonneArrivee)
    {
        Convertisseur conv = Convertisseur.getInstance();
        //Deplacer dans les mapping
        int[][]boardAModifier = clonerBoard();
        boardAModifier[ligneArrivee][colonneArrivee]=boardAModifier[ligneDepart][colonneDepart];
        boardAModifier[ligneDepart][colonneDepart] = 0;
        return boardAModifier;
    }

    //petite fonction pour passer  travers le board pour afficher les cases
    public void afficherBoard()
    {
        for (int i=0; i<8; i++) {
                    String ligne="";
                    for (int j = 0; j < 8; j++) {
                        //ligne=ligne+Integer.toString(board[j][i]);
                        ligne+=board[i][j];
            }
            System.out.println(ligne);
        }
    }

}
