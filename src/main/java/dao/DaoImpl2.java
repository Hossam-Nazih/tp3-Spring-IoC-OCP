package dao;

import org.springframework.stereotype.Component;

/*
 * Source simulée n°2 : base de données de DÉVELOPPEMENT (renvoie 150).
 * Le nom du bean ("dao2") est celui qu'on écrit dans app.properties pour la sélectionner.
 */
@Component("dao2")
public class DaoImpl2 implements IDao {
    @Override
    public double getValue() {
        System.out.println("[DAO] Lecture depuis la base de données (dev)");
        return 150.0;
    }
}
