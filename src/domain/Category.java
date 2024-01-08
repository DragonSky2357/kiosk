package domain;

import java.util.LinkedList;
import java.util.List;

public class Category extends Menu{
    private List<Product> products = new LinkedList<>();

    public Category(String name, String describe) {
        super(name, describe);
    }

    public boolean addProduct(Product product){
        return this.products.add(product);
    }

    public Product getProduct(int i) {
        return products.get(i);
    }

    public void DrawProduct(){
        System.out.println("[ "+this.getName()+" MENU ]");

        products.forEach((p)->{
            System.out.printf("%-6s| W %d | %s\n",p.getName(),p.getPrice(),p.getDescribe());
        });
    }
}
