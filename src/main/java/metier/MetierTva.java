package metier;

import dao.IDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/*
 * Troisième règle métier, plus proche d'un vrai besoin : un calcul de prix TTC.
 * La valeur du DAO est vue comme un prix hors taxes (HT), on lui applique la TVA :
 *     prix TTC = prix HT x (1 + taux)
 * Le taux est lu dans app.properties (clé metier.tva), 20 % par défaut.
 * Si la TVA change, on modifie le fichier de configuration, pas le code.
 */
@Service("metierTva")
public class MetierTva implements IMetier {
    private final IDao dao;
    // Taux de TVA sous forme décimale : 0.20 = 20 %
    private final double tauxTva;

    public MetierTva(IDao dao, @Value("${metier.tva:0.20}") double tauxTva) {
        // Un taux négatif n'a pas de sens : on refuse de démarrer plutôt que de calculer des prix faux
        if (tauxTva < 0) {
            throw new IllegalArgumentException("Taux de TVA négatif : " + tauxTva);
        }
        this.dao = dao;
        this.tauxTva = tauxTva;
    }

    @Override
    public double calcul() {
        double prixHt = dao.getValue();
        double prixTtc = prixHt * (1 + tauxTva);
        System.out.println("[TVA] " + prixHt + " HT + " + (tauxTva * 100) + " % de TVA = " + prixTtc + " TTC");
        return prixTtc;
    }
}
