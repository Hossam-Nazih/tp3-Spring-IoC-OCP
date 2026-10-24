package metier;

import dao.IDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
 * Extension : un métier qui utilise TOUS les DAO en même temps au lieu d'un seul.
 * Quand un constructeur demande une List ou une Map d'un type, Spring y met tous les beans de ce type.
 * Ici, aucune ambiguïté : on ne choisit pas, on prend tout.
 */
@Service
public class MetierMultiple implements IMetier {
    // Tous les beans IDao, dans une liste
    private final List<IDao> daos;
    // Les mêmes beans, rangés par nom : "dao" -> DaoImpl, "daoCsv" -> DaoCsv, ...
    private final Map<String, IDao> daoMap;

    public MetierMultiple(List<IDao> daos, Map<String, IDao> daoMap) {
        this.daos = daos;
        this.daoMap = daoMap;
    }

    public Map<String, IDao> getDaoMap() {
        return daoMap;
    }

    @Override
    public double calcul() {
        // Somme des valeurs de toutes les sources (dynamicDao compris, car c'est aussi un IDao)
        return daos.stream().mapToDouble(IDao::getValue).sum();
    }
}
