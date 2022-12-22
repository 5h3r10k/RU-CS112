package warehouse;

/*
 * Use this class to test to addProduct method.
 */
public class AddProduct {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        // create a new warehouse
        Warehouse warehouse = new Warehouse();

        // read number of products
        int n = StdIn.readInt();

        // read products
        for (int i = 0; i < n; i++) {
            int day = StdIn.readInt();
            int id = StdIn.readInt();
            String name = StdIn.readString();
            int stock = StdIn.readInt();
            int demand = StdIn.readInt();

            // add product
            warehouse.addProduct(id, name, stock, day, demand);
        }

        StdOut.println(warehouse);
        
    }
}
