package dao;

import org.springframework.stereotype.Component;

/*
 * Source simulée n°4 : une API REST externe (renvoie 220).
 * Aucun vrai appel réseau ici, on montre juste qu'une API peut remplacer une base de données.
 */
@Component("daoApi")
public class DaoApi implements IDao {
    @Override
    public double getValue() {
        System.out.println("[DAO] Appel à l'API REST externe");
        return 220.0;
    }
}
