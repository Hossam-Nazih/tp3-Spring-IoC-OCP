import config.AppConfig;
import metier.IMetier;
import metier.MetierMultiple;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/*
 * Point d'entrée. Main ne crée aucun DAO ni métier avec "new" : il demande tout au conteneur Spring.
 */
public class Main {
    public static void main(String[] args) {
        // Démarre le conteneur Spring : scan des paquets, lecture d'app.properties, création et injection des beans.
        // Un profil peut être activé au lancement : -Dspring.profiles.active=prod (ou alias)
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);

        // On demande "un IMetier" sans dire lequel : Spring renvoie MetierImpl (marqué @Primary)
        IMetier metier = context.getBean(IMetier.class);

        // Exécute le calcul avec le DAO choisi par la configuration
        double resultat = metier.calcul();
        System.out.println("Résultat final (multiplication) : " + resultat);

        // Autre règle métier, même DAO : on récupère le bean par son nom
        IMetier somme = context.getBean("metierSomme", IMetier.class);
        System.out.println("Résultat final (somme) : " + somme.calcul());

        // Règle métier "réelle" : prix TTC à partir d'un prix HT
        IMetier tva = context.getBean("metierTva", IMetier.class);
        System.out.println("Résultat final (TVA) : " + tva.calcul());

        // Extension : un métier qui reçoit tous les DAO d'un coup (List / Map)
        MetierMultiple multiple = context.getBean(MetierMultiple.class);
        System.out.println("[MULTI] Beans IDao : " + multiple.getDaoMap().keySet());
        System.out.println("[MULTI] Somme : " + multiple.calcul());

        // Ferme le conteneur et libère les beans
        context.close();
    }
}
