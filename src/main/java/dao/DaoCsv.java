package dao;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/*
 * DAO ajouté en plus de l'énoncé : contrairement aux autres DAO qui renvoient une valeur en dur,
 * celui-ci lit un VRAI fichier (notes.csv dans src/main/resources) et renvoie la moyenne des notes.
 * Il respecte le même contrat IDao, donc MetierImpl peut l'utiliser sans aucune modification (OCP).
 */
@Component("daoCsv")
public class DaoCsv implements IDao {

    // Fichier cherché dans le classpath (src/main/resources est copié dans le classpath par Maven)
    private static final String FICHIER = "notes.csv";

    @Override
    public double getValue() {
        System.out.println("[DAO] Lecture des notes depuis " + FICHIER);

        InputStream flux = getClass().getClassLoader().getResourceAsStream(FICHIER);
        if (flux == null) {
            throw new IllegalStateException("Fichier introuvable dans le classpath : " + FICHIER);
        }

        double somme = 0;
        int nombre = 0;
        // try-with-resources : le fichier est fermé automatiquement, même en cas d'erreur
        try (BufferedReader lecteur = new BufferedReader(new InputStreamReader(flux, StandardCharsets.UTF_8))) {
            lecteur.readLine(); // on saute l'en-tête "etudiant;note"
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                if (ligne.trim().isEmpty()) {
                    continue; // ignore les lignes vides
                }
                String[] colonnes = ligne.split(";");
                somme += Double.parseDouble(colonnes[1].trim());
                nombre++;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erreur de lecture de " + FICHIER, e);
        }

        if (nombre == 0) {
            throw new IllegalStateException("Aucune note trouvée dans " + FICHIER);
        }
        // La valeur renvoyée au métier est la moyenne de la classe
        return somme / nombre;
    }
}
