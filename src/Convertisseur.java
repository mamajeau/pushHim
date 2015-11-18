import java.util.Hashtable;

/**
 * Created by Gabriel on 04/11/2015.
 */
public class Convertisseur {

    private static Convertisseur instance = null;

   private Hashtable<Character,Integer> converLettre = new Hashtable<Character,Integer>();
   private Hashtable<Integer,Character> converNombre = new Hashtable<Integer,Character>();

    protected Convertisseur()
    {
        this.converNombre.put(0, 'A');
        this.converNombre.put(1,'B');
        this.converNombre.put(2, 'C');
        this.converNombre.put(3,'D');
        this.converNombre.put(4, 'E');
        this.converNombre.put(5,'F');
        this.converNombre.put(6, 'G');
        this.converNombre.put(7, 'H');
        this.converLettre.put('A',0);
        this.converLettre.put('B',1);
        this.converLettre.put('C',2);
        this.converLettre.put('D',3);
        this.converLettre.put('E',4);
        this.converLettre.put('F',5);
        this.converLettre.put('G',6);
        this.converLettre.put('H',7);
    }

    public static Convertisseur getInstance() {
        if (instance == null) {
            instance = new Convertisseur();
        }
        return instance;
    }

    public int LettreAChiffre(char lettre)
    {

        return converLettre.get(lettre);
    }

    public char ChiffreALettre(int chiffre)
    {

        return converNombre.get(chiffre);
    }


}
