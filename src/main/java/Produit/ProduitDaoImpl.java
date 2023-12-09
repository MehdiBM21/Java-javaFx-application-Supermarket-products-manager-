package Produit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitDaoImpl extends AbstractDao implements IProduitDao {
    @Override
    public void add(Produit obj) {
        PreparedStatement pst = null;
        ResultSet generatedKeys = null;
        String sql = "INSERT INTO produit (idCategorie, designation, quantite, prix, date, peremption) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, obj.getIdCategorie());
            pst.setString(2, obj.getDesignation());
            pst.setInt(3, obj.getQte());
            pst.setDouble(4, obj.getPrix());
            pst.setDate(5, Date.valueOf(obj.getDate()));
            pst.setDate(6, Date.valueOf(obj.getPeremption())); // Assuming obj.getPeremption() returns a LocalDate
            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated keys
                generatedKeys = pst.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Set the generated ID to your object
                    obj.setId(generatedKeys.getInt(1)); // Assuming the ID column is the first column
                } else {
                    System.out.println("Failed to retrieve the generated ID.");
                }
            }

            System.out.println("Produit ajouté avec ID: " + obj.getId());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Close the ResultSet and PreparedStatement in the finally block
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }




    @Override
    public void delete(int id) {
        PreparedStatement pst = null;
        String sql = "DELETE FROM produit WHERE id = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println("Product with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Produit getById(int id) {
        // TODO: Implement this method
        return null;
    }

    @Override
    public List<Produit> getAll() {
        List<Produit> listeProduits = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM produit");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Date dateSql = rs.getDate("date");
                Date peremptionSql = rs.getDate("peremption");

                listeProduits.add(new Produit(
                        rs.getInt("id"),
                        rs.getInt("idCategorie"),
                        rs.getString("designation"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix"),
                        dateSql != null ? dateSql.toLocalDate() : null,
                        peremptionSql != null ? peremptionSql.toLocalDate() : null));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les produits");
            e.printStackTrace();
        }
        return listeProduits;
    }

    @Override
    public List<Produit> getProduitByKeyword(String keyword) {
        List<Produit> listeProduits = new ArrayList<Produit>();
        String sql = "SELECT * FROM produit WHERE designation LIKE ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Date dateSql = rs.getDate("date");
                    Date peremptionSql = rs.getDate("peremption");

                    listeProduits.add(new Produit(
                            rs.getInt("id"),
                            rs.getInt("idCategorie"),
                            rs.getString("designation"),
                            rs.getInt("qte"),
                            rs.getDouble("prix"),
                            dateSql != null ? dateSql.toLocalDate() : null,
                            peremptionSql != null ? peremptionSql.toLocalDate() : null));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits par mot-clé");
            e.printStackTrace();
        }
        return listeProduits;
    }
    public List<Produit> getProduitByCategorie(int categorie){
        List<Produit> listeProduits = new ArrayList<Produit>();
        String sql = "SELECT * FROM produit WHERE idCategorie=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, categorie);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Date dateSql = rs.getDate("date");
                    Date peremptionSql = rs.getDate("peremption");

                    listeProduits.add(new Produit(
                            rs.getInt("id"),
                            rs.getInt("idCategorie"),
                            rs.getString("designation"),
                            rs.getInt("quantite"),
                            rs.getDouble("prix"),
                            dateSql != null ? dateSql.toLocalDate() : null,
                            peremptionSql != null ? peremptionSql.toLocalDate() : null));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits par mot-clé");
            e.printStackTrace();
        }
        return listeProduits;
    }
}
