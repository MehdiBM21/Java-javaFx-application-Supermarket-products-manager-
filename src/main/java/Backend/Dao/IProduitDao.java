package Backend.Dao;

import Backend.Produit.Produit;

import java.util.List;

public interface IProduitDao extends IDao<Produit>{
    public List<Produit> getProduitByKeyword(String keyword);
    //public List<Backend.Produit> getProduitByDesignation(String designation);
}
