package metier;

/*
 * Couche MÉTIER : les règles et calculs de l'application (le "cuisinier").
 * Main ne dépend que de cette interface, pas de MetierImpl directement.
 */
public interface IMetier {
    double calcul();
}
