import java.io.*;
import java.net.*;


class Client {
    public static void main(String[] args) {
        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        Joueur joueur = new Joueur();
        int[] board = new int[64];
        Jeu jeu=null;
        try {
            MyClient = new Socket("localhost", 8888);
            input    = new BufferedInputStream(MyClient.getInputStream());
            output   = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while(1 == 1){
                char cmd = 0;

                cmd = (char)input.read();

                // Début de la partie en joueur blanc
                if(cmd == '1'){
                    joueur.couleur = true;
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[x] = Integer.parseInt(boardValues[i]);
                        x++;
                    }

                    jeu = new Jeu();
                    //Initialisation du jeu
                    jeu.construirePlateau(board);
                    jeu.construireIA();
                    jeu.ia.couleur=true;
                    //=========================


                    System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
                    String coup=jeu.ia.jouerCoup();
                    String move = null;
                    //move = console.readLine();
                    move=coup;
                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                    }
                    // Début de la partie en joueur Noir
                    if(cmd == '2'){
                        System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
                        byte[] aBuffer = new byte[1024];
                        int size = input.available();
                        //System.out.println("size " + size);
                        input.read(aBuffer,0,size);
                        String s = new String(aBuffer).trim();
                        System.out.println(s);
                        String[] boardValues;
                        boardValues = s.split(" ");
                        int x=0;
                        for(int i=0; i<boardValues.length;i++){
                            board[x] = Integer.parseInt(boardValues[i]);
                            x++;
                        }
                        jeu = new Jeu();
                        //Initialisation du jeu
                        jeu.construirePlateau(board);
                        jeu.construireIA();
                        jeu.ia.couleur=false;
                        //=========================
                }


                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joué.
                if(cmd == '3'){
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer);

                    //Coup du joueur humain
                    jeu.plateau.updateJoueur(s);
                    jeu.plateau.afficherBoard();

                    System.out.println("Dernier coup : "+ s);
                    System.out.println(s);
                    System.out.println("Entrez votre coup : ");

                    String coup=jeu.ia.jouerCoup();
                    String move = null;
                    //move = console.readLine();
                    move=coup;

                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }
                // Le dernier coup est invalide
                if(cmd == '4'){
                    System.out.println("Coup invalide, entrez un nouveau coup : ");

                    String coup=jeu.ia.jouerCoup();
                    String move = null;
                    //move = console.readLine();
                    move=coup;
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }
}

