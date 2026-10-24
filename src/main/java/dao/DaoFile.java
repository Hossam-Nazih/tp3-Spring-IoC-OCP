package dao;

import org.springframework.stereotype.Component;

/*
 * Source simulée n°3 : un fichier local (renvoie 180).
 * Simulation seulement : pour une vraie lecture de fichier, voir DaoCsv.
 */
@Component("daoFile")
public class DaoFile implements IDao {
    @Override
    public double getValue() {
        System.out.println("[DAO] Lecture depuis un fichier local");
        return 180.0;
    }
}
