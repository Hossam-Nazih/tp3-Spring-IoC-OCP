package dao;

/*
 * Couche DAO (Data Access Object) : le "fournisseur" de données.
 * Cette interface est le CONTRAT que toutes les sources de données doivent respecter.
 * Le métier ne connaît que ce contrat, jamais la classe concrète qui est derrière :
 * c'est ce qui permet de changer de source (base, fichier, API, CSV...) sans toucher au métier.
 */
public interface IDao {
    /**
     * Récupère une valeur numérique depuis la source de données.
     * @return la valeur récupérée
     */
    double getValue();
}
