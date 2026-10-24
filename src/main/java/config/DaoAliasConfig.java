package config;

import dao.DaoApi;
import dao.IDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/*
 * Stratégie B : un bean "alias" qui pointe vers l'implémentation voulue.
 * Avantage : on change de DAO ici, sans toucher aux classes DAO ni au métier.
 * Limite   : il faut quand même modifier cette ligne et recompiler.
 *
 * Active uniquement avec le profil "alias" (-Dspring.profiles.active=alias),
 * sinon elle entrerait en conflit avec le @Primary de DynamicDaoConfig.
 */
@Configuration
@Profile("alias")
public class DaoAliasConfig {

    // @Bean : l'objet renvoyé par cette méthode devient un bean géré par Spring
    @Bean
    @Primary
    public IDao daoAlias() {
        // Pointe vers l'implémentation API (remplacer par new DaoFile() ou new DaoCsv() pour changer de cible)
        return new DaoApi();
    }
}
