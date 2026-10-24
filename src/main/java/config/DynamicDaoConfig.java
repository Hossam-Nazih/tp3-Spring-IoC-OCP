package config;

import dao.DaoApi;
import dao.DaoCsv;
import dao.DaoFile;
import dao.DaoImpl;
import dao.DaoImpl2;
import dao.IDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/*
 * Stratégie C (la plus flexible) : le DAO est choisi par la clé dao.target, lue dans la configuration.
 * On change de source sans modifier ni recompiler le code Java.
 *
 * Ordre de priorité (le premier trouvé gagne) :
 *   1. propriété système   -Ddao.target=dao2
 *   2. variable d'environnement   DAO_TARGET=dao2
 *   3. app.properties   dao.target=dao2
 *   4. valeur par défaut après ":"   ->  "dao"
 *
 * @Profile("!alias") : active partout SAUF avec le profil "alias" (où c'est la stratégie B qui décide).
 */
@Configuration
@Profile("!alias")
public class DynamicDaoConfig {

    // Spring remplace ${dao.target:dao} par la valeur trouvée (ou "dao" si la clé n'existe nulle part)
    @Value("${dao.target:dao}")
    private String daoTarget;

    // @Primary : quand plusieurs IDao existent, c'est celui-ci que Spring injecte dans MetierImpl
    @Bean
    @Primary
    public IDao dynamicDao() {
        switch (daoTarget) {
            case "dao": return new DaoImpl();
            case "dao2": return new DaoImpl2();
            case "daoFile": return new DaoFile();
            case "daoApi": return new DaoApi();
            case "daoCsv": return new DaoCsv();
            // Valeur inconnue : on échoue tout de suite au démarrage avec un message clair
            default: throw new IllegalArgumentException("Cible DAO inconnue: " + daoTarget);
        }
    }
}
