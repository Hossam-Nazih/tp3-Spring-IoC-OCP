package metier;

import dao.IDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/*
 * Le métier principal : demande une valeur au DAO puis applique la règle (x 2).
 * RÈGLE DU TP : cette classe ne doit JAMAIS être modifiée pour changer de source de données.
 * Elle ne contient aucun "new DaoXxx()" : c'est Spring qui lui donne son DAO (inversion de contrôle).
 *
 * @Service : comme @Component, mais indique que c'est une classe de la couche métier.
 * @Primary : MetierSomme, MetierTva et MetierMultiple implémentent aussi IMetier, donc on désigne MetierImpl comme métier par défaut.
 */
@Primary
@Service
public class MetierImpl implements IMetier {
    // final : le DAO est fixé une fois pour toutes à la construction
    private final IDao dao;

    // Injection par constructeur : Spring appelle ce constructeur avec le bean IDao choisi
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

    // @PostConstruct : exécutée par Spring juste après l'injection, pour vérifier quel DAO a été choisi
    @PostConstruct
    public void init() {
        System.out.println("[TRACE] Implémentation DAO injectée : " + dao.getClass().getSimpleName());
    }

    @Override
    public double calcul() {
        // Logique métier simple : double la valeur, quelle que soit sa provenance
        return dao.getValue() * 2.0;
    }
}
