package dao;

import org.springframework.stereotype.Component;

/*
 * Source simulée n°1 : base de données de PRODUCTION (renvoie 100).
 * @Component("dao") : Spring crée automatiquement un objet (bean) de cette classe et le nomme "dao".
 *
 * Stratégie A (@Primary) : décommenter @Primary ferait de DaoImpl le choix par défaut.
 * Elle est désactivée car DynamicDaoConfig (stratégie C) déclare déjà un bean @Primary,
 * et Spring refuse deux @Primary du même type ("more than one 'primary' bean found").
 */
// @Primary
@Component("dao")
public class DaoImpl implements IDao {
    @Override
    public double getValue() {
        System.out.println("[DAO] Lecture depuis la base de données (prod)");
        return 100.0;
    }
}
