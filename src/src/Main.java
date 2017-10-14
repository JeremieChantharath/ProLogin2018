import java.util.*;


public class Main
{
    static Scanner scanner = new Scanner(System.in);
    static int[] read_int_line()
    {
        String[] s = scanner.nextLine().split(" ");
        int[] out = new int[s.length];
        for (int i = 0; i < s.length; i++)
            out[i] = Integer.parseInt(s[i]);
        return out;
    }

    /**
     * On récupère les emplacements des marchands, les associe a leurs influences, place cela dans une Map
     * De cette map on cherche le marchand avec la plus grand influence car notre marchand se positionnera en fonction de l'emplacement de celui-ci
     * On regarde si l'on obtient la plus grande influence en se placant avant ou apres celui ci
     * On revoie cette valeur
     * @param n longueur plage
     * @param m nombre de marchands
     * @param emplacements tableaux avec position des marchands
     */
    static void faites_place0(int n, int m, int[] emplacements)
    {
        Arrays.sort(emplacements);

        // Map avec l'emplacement d'un marchand associé à son influence actuel
        TreeMap<Integer,Float> longueurInfluence = new TreeMap<>();

        // Place occupée sur la plage
        float occupe = 0;

        try
        {
            longueurInfluence.put(emplacements[0],getInfluencePremier(emplacements[0],emplacements[1]));
            occupe += longueurInfluence.get(emplacements[0]);
        }
        // Exception si le marchand est seul sur la plage
        catch (Exception e)
        {
            longueurInfluence.put(emplacements[0], (float)n);
        }

        for (int i =1; i<m; i++)
        {
            try
            {
                longueurInfluence.put(emplacements[i], getInfluenceMilieu(emplacements[i], emplacements[i-1], emplacements[i+1]));
                occupe += longueurInfluence.get(emplacements[i]);
            }
            //Exception si le marchand est le dernier sur la plage
            catch (Exception e2)
            {
                longueurInfluence.put(emplacements[i], (float)n - occupe);
            }
        }
        System.out.println((int)getMaxInfluence(getMeilleurEmplacementActuel(emplacements,longueurInfluence),emplacements, n));
    }

    //Récupère l'influence du premier marchand sur la plage
    private static float getInfluencePremier(int premierEmplacement, int emplacementSuivant)
    {
        return (float)premierEmplacement  + Math.abs((float)premierEmplacement - (float)emplacementSuivant)/2;
    }

    //Récupère l'influence d'un marchand ni premier ni dernier
    private static float getInfluenceMilieu(int emplacement, int emplacementPrecedent, int emplacementSuivant)
    {
        return (Math.abs(((float)emplacementPrecedent - (float)emplacement))/2) + (Math.abs(((float)emplacement - (float)emplacementSuivant))/2);
    }

    //Récupère le marchand avec le meilleur emplacement actuellement
    private static int getMeilleurEmplacementActuel(int[] emplacements,SortedMap<Integer, Float> listeInfluence)
    {
        int plusGrandEmplacement = listeInfluence.firstKey();
        for(int i = 1; i<emplacements.length;i++)
        {
            if(listeInfluence.get(emplacements[i]) > listeInfluence.get(plusGrandEmplacement))
                plusGrandEmplacement = emplacements[i];
        }
        return plusGrandEmplacement;
    }

    //Récupère l'influence maximale que pourra avoir notre marchand en se placant sur la plage par rapport au marchand avec la meilleur influence actuellement
    private static float getMaxInfluence(int meilleurEmplacementActuel, int[] emplacements, int n)
    {
        float maxInfluence = 0;

        for(int i = 0; i< emplacements.length; i++)
        {
            //cherche le meilleur marchand dans le tableau (nécessaire pour connaitre sa position sur la plage; ie premier,dernier,...)
            if (meilleurEmplacementActuel == emplacements[i])
            {
                //si c'est le premier
                if(i == 0)
                {
                    try
                    {
                        if(getInfluencePremier(meilleurEmplacementActuel-1, meilleurEmplacementActuel) >
                                getInfluenceMilieu(meilleurEmplacementActuel+1, meilleurEmplacementActuel, emplacements[i+1]))

                            maxInfluence = getInfluencePremier(meilleurEmplacementActuel-1, meilleurEmplacementActuel);

                        else
                            maxInfluence = getInfluenceMilieu(meilleurEmplacementActuel+1, meilleurEmplacementActuel, emplacements[i+1]);
                        //break;
                    }
                    //si c'est le premier et seul sur la plage
                    catch (Exception e)
                    {
                        if(getInfluencePremier(meilleurEmplacementActuel-1, meilleurEmplacementActuel) >
                                n - getInfluencePremier(meilleurEmplacementActuel-1, meilleurEmplacementActuel))

                            maxInfluence = getInfluencePremier(meilleurEmplacementActuel-1, meilleurEmplacementActuel);

                        else
                            maxInfluence =  n - getInfluencePremier(meilleurEmplacementActuel, meilleurEmplacementActuel-1) - (float)0.5;
                        //break;
                    }
                }
                //Si c'est le dernier sur la plage
                else if (i == emplacements.length-1)
                {
                    if(getInfluenceMilieu(meilleurEmplacementActuel-1,emplacements[i-1],meilleurEmplacementActuel) >
                            n - meilleurEmplacementActuel+1 + (float)0.5)

                        maxInfluence = getInfluenceMilieu(meilleurEmplacementActuel-1,emplacements[i-1],meilleurEmplacementActuel);
                    else
                        maxInfluence = n - meilleurEmplacementActuel-1 + (float)0.5;
                    //break;
                }
                //Autrement
                else
                if(getInfluenceMilieu(meilleurEmplacementActuel-1, emplacements[i-1], meilleurEmplacementActuel) >
                        getInfluenceMilieu(meilleurEmplacementActuel+1, meilleurEmplacementActuel, emplacements[i+1]))

                    maxInfluence = getInfluenceMilieu(meilleurEmplacementActuel-1, emplacements[i-1], meilleurEmplacementActuel);
                else
                    maxInfluence = getInfluenceMilieu(meilleurEmplacementActuel+1, meilleurEmplacementActuel, emplacements[i+1]);
                //break;
            }
        }
        return maxInfluence;
    }


    public static void main(String args[])
    {
        int n = Integer.parseInt(scanner.nextLine());
        int m = Integer.parseInt(scanner.nextLine());
        int[] emplacements = read_int_line();
        faites_place0(n, m, emplacements);
    }

}


