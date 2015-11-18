import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by maaj on 2015-11-02.
 */
public class Jeu {
    public Plateau plateau;
    public IA ia;

    public void construireIA()
    {
        this.ia=new IA(this.plateau);
    }

    public void construirePlateau(int[] board) {

        this.plateau = new Plateau(board);
    }



    private String getId(int ligne, int colonne) {
        String id = "";
        switch (colonne){
            case 0: id += "A";
                break;
            case 1: id += "B";
                break;
            case 2: id += "C";
                break;
            case 3: id += "D";
                break;
            case 4: id += "E";
                break;
            case 5: id += "F";
                break;
            case 6: id += "G";
                break;
            case 7: id += "H";
                break;
        }
        switch (ligne){
            case 0: id += "8";
                break;
            case 1: id += "7";
                break;
            case 2: id += "6";
                break;
            case 3: id += "5";
                break;
            case 4: id += "4";
                break;
            case 5: id += "3";
                break;
            case 6: id += "2";
                break;
            case 7: id += "1";
                break;
        }
        return id;
    }
}
