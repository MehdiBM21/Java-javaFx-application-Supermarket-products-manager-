package Backend.Categorie;

import Backend.Dao.AbstractDao;
import Backend.Dao.ICategorieDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDaoImpl extends AbstractDao implements ICategorieDao {

    @Override
    public void add(Categorie obj) {
        PreparedStatement pst = null;
        ResultSet generatedKeys = null;
        String sql = "INSERT INTO category (nom) VALUES (?)";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, obj.getNom());
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Catégorie ajoutée");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        PreparedStatement pst = null;
        String sql = "DELETE FROM category WHERE id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Category deleted successfully!");
            } else {
                System.out.println("Category with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Categorie getById(int id) {
        PreparedStatement pst = null;
        String sql = "SELECT * FROM categorie WHERE id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Categorie(rs.getInt("id"), rs.getString("nom"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Categorie> getAll() {
        List<Categorie> listeCategories = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM categorie");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                listeCategories.add(new Categorie(
                        rs.getInt("id"),
                        rs.getString("nom")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de toutes les catégories");
            e.printStackTrace();
        }
        return listeCategories;
    }
}
