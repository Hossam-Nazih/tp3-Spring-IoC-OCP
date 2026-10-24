package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/*
 * Configuration principale, donnée à Spring au démarrage dans Main.
 *
 * @Configuration  : cette classe sert à configurer le conteneur Spring.
 * @ComponentScan  : Spring parcourt ces paquets et crée un bean pour chaque @Component / @Service / @Configuration.
 *                   "config" est ajouté (absent de l'énoncé), sinon DynamicDaoConfig et DaoAliasConfig ne seraient jamais lus.
 * @PropertySource : charge app.properties pour que @Value("${dao.target}") puisse lire la valeur.
 */
@Configuration
@ComponentScan(basePackages = {"dao", "metier", "config"})
@PropertySource("classpath:app.properties")
public class AppConfig {
}
