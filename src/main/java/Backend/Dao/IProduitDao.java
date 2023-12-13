package Backend.Dao;

import Backend.Produit.Produit;

import java.util.List;

public interface IProduitDao extends IDao<Produit>{
    public List<Produit> getProduitByKeyword(String designation, int idCategorie);
    public void update(Produit obj);
    public List<Produit> getProduitByCategorie(int idCategorie);
    //public List<Backend.Produit> getProduitByDesignation(String designation);
}
