package warehouse;

public class Restock {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

	    // Use this file to test restock
        // create a new warehouse
        Warehouse warehouse = new Warehouse();

        // read number of products
        int n = StdIn.readInt();

        // read queries
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
                // restock product
                warehouse.restockProduct(StdIn.readInt(), StdIn.readInt());
            }
            
        }

        StdOut.println(warehouse);

    }
}
