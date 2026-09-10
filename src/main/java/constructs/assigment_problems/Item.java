package constructs.assigment_problems;

public class Item {
    String itemName;
    int stock;

    public Item(String itemName, int stock) {
        this.itemName = itemName;
        this.stock = stock;
    }

    public void restock(int stock) {
        this.stock += stock;
    }

    public void display() {
        System.out.println(this.itemName + " | Stock: " + this.stock);
    }

    public static void main(String[] args) {
        Item[] items = {
            new Item("Notebook", 50),
            new Item("Pen", 100),
            new Item("Folder", 35),
            new Item("Marker", 40)
        };

        for (Item item : items) {
            item.restock(20);
            item.display();
        }
    }
}
