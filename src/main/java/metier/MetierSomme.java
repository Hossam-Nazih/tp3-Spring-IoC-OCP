package metier;

import dao.IDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/*
 * Deuxième règle métier : au lieu de multiplier par 2 (MetierImpl), on AJOUTE une valeur.
 * Elle reçoit le même DAO que MetierImpl : la source de données et le calcul sont indépendants.
 *
 * Nouveau calcul = nouvelle classe : MetierImpl n'a pas été modifié (principe OCP).
 */
@Service("metierSomme")
public class MetierSomme implements IMetier {
    private final IDao dao;
    // Valeur ajoutée, lue dans app.properties (clé metier.ajout), 50 par défaut
    private final double ajout;

    // Spring injecte le DAO choisi ET la valeur de la propriété, tous les deux par le constructeur
    public MetierSomme(IDao dao, @Value("${metier.ajout:50}") double ajout) {
        this.dao = dao;
        this.ajout = ajout;
    }

    @Override
    public double calcul() {
        // Logique métier : valeur du DAO + ajout
        return dao.getValue() + ajout;
    }
}
