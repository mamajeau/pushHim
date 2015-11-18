import java.util.ArrayList;

/**
 * Created by maaj on 2015-11-07.
 */
public class Noeud {

    int[] board;
    ArrayList<Noeud> listeEnfant=new ArrayList<Noeud>();
    int poids;
    Mouvement mouvementFait;

    public Noeud(int[] board)
    {
        this.board=board;
    }

    public Noeud(int[] board,Mouvement mouvementFait)
    {
        this.board=board;
        this.mouvementFait=mouvementFait;
    }

    public void ajouterEnfant(Noeud enfant)
    {
        listeEnfant.add(enfant);
    }

    public int[] getBoard(){
        return board.clone();
    }
}
