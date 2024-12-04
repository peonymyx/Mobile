package Nhom2.com.example.doanmobile.Models;

public class Category {
    private String categoryID;  // Unique ID for the category
    private String name;        // Name of the category
    private String description; // Description of the category (optional)
    private String imageUrl;    // URL for the category's display image (optional)

    // Default Constructor
    public Category() {
    }

    // Parameterized Constructor
    public Category(String categoryID, String name, String description, String imageUrl) {
        this.categoryID = categoryID;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryID='" + categoryID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}