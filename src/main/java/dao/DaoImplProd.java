package dao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/*
 * Exemple de PROFIL Spring : ce bean n'existe dans le conteneur que si le profil "prod" est actif
 * (lancement avec -Dspring.profiles.active=prod). Sans ce profil, Spring l'ignore complètement.
 */
@Component("daoProd")
@Profile("prod")
public class DaoImplProd implements IDao {
    @Override
    public double getValue() {
        System.out.println("[DAO] Lecture depuis la base de production (profil prod)");
        return 100.0;
    }
}
