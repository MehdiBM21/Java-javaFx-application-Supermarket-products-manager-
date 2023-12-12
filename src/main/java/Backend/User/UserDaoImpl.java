package Backend.User;

import java.sql.PreparedStatement;
import Backend.Dao.AbstractDao;
import Backend.Dao.IUserDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends AbstractDao implements IUserDao {
    @Override
    public void add(User obj) {
        PreparedStatement pst = null;
        String sql = "INSERT INTO user(id,username,password,type) values (?,?,?,?)";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1,obj.getId());
            pst.setString(2,obj.getUsername());
            pst.setString(3,obj.getPassword());
            pst.setString(4,obj.getType());
            System.out.println("Succes d'exec de la requete!!");
            pst.executeUpdate();

        } catch(SQLException exp){
            System.out.println(exp.getMessage());
        }

    }

    @Override
    public void delete(int id) {
        PreparedStatement pst = null;
        String sql = "DELETE FROM user WHERE id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1,id);
            System.out.println("Succes d'exec de la requete!!");
            pst.executeUpdate();

            } catch(SQLException exp){
        System.out.println(exp.getMessage());
    }


    }

    @Override
    public User getById(int id) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM user WHERE id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1,id);
            System.out.println("Succes d'exec de la requete!!");
            rs = pst.executeQuery();
            if (rs.next()){
                //System.out.println(rs.getLong("id")+" "+rs.getString("email")+" "+rs.getString("password")+" "+rs.getString("type"));
                return new User(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("type"));
            }
        } catch(SQLException exp){
            System.out.println(exp.getMessage());
        }


        return null;

    }

    @Override
    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM user";

        try {
            pst = connection.prepareStatement(sql);
            System.out.println("Succes d'exec de la requete!!");
            rs = pst.executeQuery();
            while(rs.next()){
                //System.out.println(rs.getLong("id")+" "+rs.getString("email")+" "+rs.getString("password")+" "+rs.getString("type"));
                list.add(new User(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("type")));
            }
        } catch(SQLException exp){
            System.out.println(exp.getMessage());
        }


        return list;


    }


    @Override
    public List<User> getByType(String type) {
        List<User> list = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM user WHERE type LIKE ?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1,type+"%");
            System.out.println("Succes d'exec de la requete!!");
            rs = pst.executeQuery();
            while(rs.next()){
                //System.out.println(rs.getLong("id")+" "+rs.getString("email")+" "+rs.getString("password")+" "+rs.getString("type"));
                list.add(new User(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("type")));
            }
        } catch(SQLException exp){
            System.out.println(exp.getMessage());
        }


        return list;


    }
}
