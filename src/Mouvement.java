import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by maaj on 2015-11-02.
 */
public class Mouvement {

    int ligneDepart;
    int ligneArrivee;
    int colonneDepart;
    int colonneArrivee;
    int poids=0;
    Convertisseur convertisseur= Convertisseur.getInstance();

    public Mouvement(int ligneDepart, int colonneDepart, int ligneArrivee, int colonneArrivee)
    {
        this.ligneDepart = ligneDepart;
        this.colonneDepart = colonneDepart;
        this.ligneArrivee = ligneArrivee;
        this.colonneArrivee = colonneArrivee;
    }


}
