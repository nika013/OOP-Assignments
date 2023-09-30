package org.example;

public class Product {
    private String name;
    private String productId;
    private int price;
    private int quantity;
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
    public Product(String productId, String name, int price) {
        this.name = name;
        this.price = price;
        this.productId = productId;
    }

    public int getPrice() {
        return price;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product otherProduct = (Product) obj;
        return productId.equals(otherProduct.productId);
    }

    @Override
    public int hashCode() {
        return productId.hashCode();
    }
}
