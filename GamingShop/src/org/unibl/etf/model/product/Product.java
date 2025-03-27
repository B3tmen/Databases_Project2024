package org.unibl.etf.model.product;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.unibl.etf.database.dao.implementations.CategoryDAOImpl;
import org.unibl.etf.database.dao.implementations.ManufacturerDAOImpl;
import org.unibl.etf.database.dao.interfaces.CategoryDAO;
import org.unibl.etf.database.dao.interfaces.ManufacturerDAO;
import org.unibl.etf.util.ImagePaths;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Objects;


public class Product {
    private int id;
    private String name;
    private BigDecimal price;       // BigDecimal instead of Double because MySQL database has DECIMAL type
    private BigDecimal discountedPrice;
    private int quantityAvailable;
    private String description;
    private int warrantyMonths;
    private String imageUrl;
    private int discountId;
    private int employeeId;
    private int manufacturerId;
    private String manufacturerName;
    private String categoryName;
    private transient ImageView image;

    public Product() {}

    public Product(int id, String name, BigDecimal price, BigDecimal discountedPrice, int quantityAvailable,
                   String description, int warrantyMonths, String imageUrl,
                   int discountId, int employeeId, int manufacturerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.quantityAvailable = quantityAvailable;
        this.description = description;
        this.warrantyMonths = warrantyMonths;
        this.imageUrl = imageUrl;
        this.discountId = discountId;
        this.employeeId = employeeId;
        this.manufacturerId = manufacturerId;
        this.image = null;

        if(imageUrl == null){
            try{
                String path = getClass().getResource(ImagePaths.PRODUCT_DEFAULT_IMAGE).getFile();
                String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);

                this.image = new ImageView(new Image(getClass().getResource(ImagePaths.PRODUCT_DEFAULT_IMAGE).toExternalForm()));
                this.image.setFitWidth(100);
                this.image.setFitHeight(100);

                File file = new File(decodedPath);
                this.imageUrl = file.getAbsolutePath();
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        else{
            this.image = new ImageView(imageUrl);
            this.image.setFitWidth(100);
            this.image.setFitHeight(100);
        }
        setManufacturerName();
    }

    public void setCategoryName() {
        CategoryDAO categoryDAO = new CategoryDAOImpl();

        try {
            Category category = categoryDAO.getCategoryByProductId(id);
            categoryName = category.getName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setManufacturerName() {
        ManufacturerDAO manufacturerDAO = new ManufacturerDAOImpl();

        try {
            manufacturerName = manufacturerDAO.get(manufacturerId).getName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }
    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }
    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }
    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageView getImage() {
        return image;
    }
    public void setImage(ImageView image) {
        this.image = image;
    }

    public int getDiscountId() {
        return discountId;
    }
    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }
    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }
    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof Product && id == ((Product) obj).id;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Price: " + price + ", Quantity Available: " + quantityAvailable + ", Description: " + description +
                ", Warranty Months: " + warrantyMonths + ", imageUrl: " + imageUrl;
    }
}
