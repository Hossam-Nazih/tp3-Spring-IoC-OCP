package test;

import config.AppConfig;
import dao.DaoCsv;
import metier.IMetier;
import metier.MetierMultiple;
import org.junit.After;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
 * Tests de non-régression : on change UNIQUEMENT la configuration (propriété ou profil)
 * et on vérifie que le résultat change, alors que MetierImpl reste identique (principe OCP).
 */
public class OcpSelectionTest {

    // Remet la configuration à zéro après chaque test pour qu'ils ne s'influencent pas
    @After
    public void nettoyer() {
        System.clearProperty("dao.target");
        System.clearProperty("metier.ajout");
        System.clearProperty("metier.tva");
    }

    // Crée un contexte avec des profils : ils doivent être activés AVANT refresh(), sinon ils sont ignorés
    private AnnotationConfigApplicationContext contexte(String... profils) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(profils);
        context.register(AppConfig.class);
        context.refresh();
        return context;
    }

    @Test
    public void testDaoImplParDefaut() {
        // Une propriété système passe avant app.properties : aucun code métier modifié
        System.setProperty("dao.target", "dao");
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);
        IMetier metier = context.getBean(IMetier.class);

        // Si dao.target=dao, le résultat doit être 200.0 (100 x 2)
        assertEquals(200.0, metier.calcul(), 0.001);

        context.close();
    }

    @Test
    public void testProprietesAppProperties() {
        // Sans surcharge, app.properties contient dao.target=daoApi => 220 x 2
        AnnotationConfigApplicationContext context = contexte();
        assertEquals(440.0, context.getBean(IMetier.class).calcul(), 0.001);
        context.close();
    }

    @Test
    public void testChaqueCible() {
        // Chaque valeur de dao.target donne un résultat différent avec le même métier
        String[] cibles = {"dao", "dao2", "daoFile", "daoApi", "daoCsv"};
        double[] attendus = {200.0, 300.0, 360.0, 440.0, 29.0};
        for (int i = 0; i < cibles.length; i++) {
            System.setProperty("dao.target", cibles[i]);
            AnnotationConfigApplicationContext context = contexte();
            assertEquals(cibles[i], attendus[i], context.getBean(IMetier.class).calcul(), 0.001);
            context.close();
        }
    }

    @Test
    public void testDaoCsvLitLeFichier() {
        // notes.csv : 12, 15, 17, 14 => moyenne 14.5
        assertEquals(14.5, new DaoCsv().getValue(), 0.001);
    }

    @Test
    public void testMetierSomme() {
        // Même DAO (dao = 100), autre calcul : 100 + 50 (valeur de app.properties)
        System.setProperty("dao.target", "dao");
        AnnotationConfigApplicationContext context = contexte();
        assertEquals(150.0, context.getBean("metierSomme", IMetier.class).calcul(), 0.001);
        context.close();

        // On change la valeur ajoutée par configuration : 100 + 10
        System.setProperty("metier.ajout", "10");
        context = contexte();
        assertEquals(110.0, context.getBean("metierSomme", IMetier.class).calcul(), 0.001);
        context.close();
    }

    @Test
    public void testMetierTva() {
        // dao = 100 HT, TVA 20 % (app.properties) => 120 TTC
        System.setProperty("dao.target", "dao");
        AnnotationConfigApplicationContext context = contexte();
        assertEquals(120.0, context.getBean("metierTva", IMetier.class).calcul(), 0.001);
        context.close();

        // Taux réduit à 10 % par configuration => 110 TTC
        System.setProperty("metier.tva", "0.10");
        context = contexte();
        assertEquals(110.0, context.getBean("metierTva", IMetier.class).calcul(), 0.001);
        context.close();
    }

    @Test
    public void testStrategieAlias() {
        // Profil "alias" : DaoAliasConfig remplace DynamicDaoConfig, dao.target est donc ignoré
        System.setProperty("dao.target", "dao");
        AnnotationConfigApplicationContext context = contexte("alias");
        assertEquals(440.0, context.getBean(IMetier.class).calcul(), 0.001);
        context.close();
    }

    @Test
    public void testProfilProd() {
        // DaoImplProd n'apparaît dans le conteneur qu'avec le profil "prod"
        AnnotationConfigApplicationContext context = contexte("prod");
        MetierMultiple multiple = context.getBean(MetierMultiple.class);
        assertTrue(multiple.getDaoMap().containsKey("daoProd"));
        context.close();
    }

    @Test
    public void testInjectionMultiple() {
        System.setProperty("dao.target", "dao");
        AnnotationConfigApplicationContext context = contexte();
        MetierMultiple multiple = context.getBean(MetierMultiple.class);

        // dao, dao2, daoFile, daoApi, daoCsv + dynamicDao (qui est lui aussi un IDao)
        assertEquals(6, multiple.getDaoMap().size());
        assertEquals(100 + 150 + 180 + 220 + 14.5 + 100, multiple.calcul(), 0.001);
        context.close();
    }
}
