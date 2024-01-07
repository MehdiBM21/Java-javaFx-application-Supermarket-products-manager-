package Backend.Historique;

import Backend.Dao.AbstractDao;
import Backend.Dao.IHistoriqueDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class HistoriqueDaoImpl extends AbstractDao implements IHistoriqueDao {
    @Override
    public List<Historique> getAll() {
        List<Historique> listeHistoriques = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM historique");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Date dateSql = rs.getDate("date");

                listeHistoriques.add(new Historique(
                        rs.getInt("id"),
                        rs.getInt("idCategorie"),
                        rs.getString("designation"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix"),
                        rs.getInt("type"),
                        dateSql != null ? dateSql.toLocalDate() : null));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les historiques");
            e.printStackTrace();
        }
        return listeHistoriques;
    }
    @Override
    public void add(Historique h){
        PreparedStatement pstAction = null;
        String sqlAction = "INSERT INTO historique (idCategorie, designation, quantite, prix, type, date) VALUES (?,?,?,?,?,?)";
        try {
            pstAction = connection.prepareStatement(sqlAction);
            pstAction.setInt(1, h.getIdCategorie());
            pstAction.setString(2, h.getDesignation());
            pstAction.setInt(3, h.getQte());
            pstAction.setDouble(4, h.getPrix());
            pstAction.setInt(5, h.getType());
            pstAction.setDate(6, Date.valueOf(h.getDate()));
            pstAction.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


}
