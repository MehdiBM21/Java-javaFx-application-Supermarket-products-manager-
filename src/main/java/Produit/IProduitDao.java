package Produit;

import java.util.List;

public interface IProduitDao extends IDao<Produit>{
    public List<Produit> getProduitByKeyword(String keyword);
    //public List<Produit> getProduitByDesignation(String designation);
}
