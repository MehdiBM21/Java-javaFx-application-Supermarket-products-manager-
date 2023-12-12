package Backend.Categorie;

import lombok.Data;

@Data
public class Categorie {
    private int id;
    private String categoryName;
    // Remove the 'description' attribute
    public Categorie(int id, String categoryName){
        this.id = id;
        this.categoryName = categoryName;
    }
    public Categorie(String categoryName){
        this.categoryName = categoryName;
    }
    public Categorie(){

    }
}
