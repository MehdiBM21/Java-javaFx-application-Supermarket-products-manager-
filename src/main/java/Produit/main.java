package Produit;

import java.time.LocalDate;

public class main {
    public static void main(String[] args) {
        ProduitDaoImpl dao = new ProduitDaoImpl();
        //Produit p = new Produit( 1, "LELE", 2, 5000, LocalDate.now(), LocalDate.now());
        //dao.add(p);
        //System.out.println(p.getId());
        System.out.println(dao.getProduitByCategorie(1));
        System.out.println(dao.getAll());
    }

}