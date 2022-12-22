package warehouse;

public class PurchaseProduct {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        // create a new warehouse
        Warehouse warehouse = new Warehouse();

        // read number of products
        int n = StdIn.readInt();

        // read queries (add or restock)
        for (int i = 0; i < n; i++) {
            if(StdIn.readString().equals("add")){
                // read new product and add
                int day = StdIn.readInt();
                int id = StdIn.readInt();
                String name = StdIn.readString();
                int stock = StdIn.readInt();
                int demand = StdIn.readInt();
                warehouse.addProduct(id, name, stock, day, demand);
            }else{
                // purchase product
                int day = StdIn.readInt();
                int id = StdIn.readInt();
                int amount = StdIn.readInt();
                warehouse.purchaseProduct(id, day, amount);
            }
            
        }

        StdOut.println(warehouse);
    }
}
