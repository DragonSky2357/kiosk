package domain;

public class Product extends Menu{
    private int price;

    public Product(String name, String describe, int price) {
        super(name, describe);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
