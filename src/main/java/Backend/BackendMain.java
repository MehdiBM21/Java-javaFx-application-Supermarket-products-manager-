package Backend;

import Backend.Categorie.CategorieDaoImpl;
import Backend.Produit.Produit;
import Backend.Produit.ProduitDaoImpl;

import java.time.LocalDate;

public class BackendMain {
    public static void main(String[] args) {
        ProduitDaoImpl dao = new ProduitDaoImpl();
        CategorieDaoImpl catDao = new CategorieDaoImpl();
        System.out.println(catDao.getNomCategorieFromId(1));
        Produit p = new Produit( 1, "LELE", 2, 5000, LocalDate.now(), LocalDate.now());
        dao.add(p);
        //System.out.println(p.getId());
        System.out.println(dao.getProduitByCategorie( 1));
        System.out.println(dao.getAll());
    }

}
