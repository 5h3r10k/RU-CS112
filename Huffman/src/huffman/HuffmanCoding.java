package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        
        StdIn.setFile(fileName); // set StdIn to read from the file

        int[] freq = new int[128]; // init array for all 128 possible chars

        int count = 0; // number of characters in the file

        // read in the file and count the frequency of each character
        while (StdIn.hasNextChar()) {
            char c = StdIn.readChar();
            freq[c]++;
            count++;
        }

        sortedCharFreqList = new ArrayList<CharFreq>(); //init arraylist


        // creates a CharFreq object for each character with a frequency > 0
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] > 0) {
                sortedCharFreqList.add(new CharFreq((char) i, (double)freq[i]/(double)count));
            }
        }

        // if the arraylist is of length 1, we need to add a dummy character
        // the dummy character should be 1 ascii value above the only character in the list (wrap around if 127)
        // the dummy character should have a frequency of 0
        if (sortedCharFreqList.size() == 1) {
            char dummy = (char) (sortedCharFreqList.get(0).getCharacter() + 1);
            if (dummy == 128) dummy = 0;
            sortedCharFreqList.add(new CharFreq((char)dummy, 0.00));
        }

        Collections.sort(sortedCharFreqList); // sorts by frequency
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {

	    /* Your code goes here */
        Queue<TreeNode> srcQ = new Queue<TreeNode>();
        Queue<TreeNode> targetQ = new Queue<TreeNode>();

        // add all the CharFreq objects to the source queue
        for (CharFreq cf : sortedCharFreqList) {
            srcQ.enqueue(new TreeNode(cf, null, null));
        }

        
        // while there are still nodes in the source queue
        while(!srcQ.isEmpty() || targetQ.size() > 1) {

            // picking the 2 nodes with the lowest frequencies, from the src and target queues
            TreeNode left, right;
            double srcFreq, targetFreq;

            // picking left node
            srcFreq = srcQ.isEmpty() ? Double.MAX_VALUE : srcQ.peek().getData().getProbOcc();
            targetFreq = targetQ.isEmpty() ? Double.MAX_VALUE : targetQ.peek().getData().getProbOcc();

            if (srcFreq <= targetFreq) {
                left = srcQ.dequeue();
            } else {
                left = targetQ.dequeue();
            }

            // picking right node
            srcFreq = srcQ.isEmpty() ? Double.MAX_VALUE : srcQ.peek().getData().getProbOcc();
            targetFreq = targetQ.isEmpty() ? Double.MAX_VALUE : targetQ.peek().getData().getProbOcc();

            if (srcFreq <= targetFreq) {
                right = srcQ.dequeue();
            } else {
                right = targetQ.dequeue();
            }


            // we now have the 2 nodes with the lowest frequencies, so we can create a new node with them as children
            TreeNode newNode = new TreeNode(new CharFreq(null, 
                                            left.getData().getProbOcc() + right.getData().getProbOcc()), 
                                            left, right);

            // add the new node to the target queue
            targetQ.enqueue(newNode);
        }

        // the last node in the target queue is the root of the huffman tree
        huffmanRoot = targetQ.dequeue();

    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {

        encodings = new String[128]; // init array

        // call recursive helper method
        recurCode(huffmanRoot, "");

        System.out.println(encodings[77]);

    }

    /**
     * Uses recursion to traverse the huffman tree, and sets the encodings array.
     * @param root
     * @param code
     */
    private void recurCode(TreeNode root, String code){

        // if the node is a leaf, set the encoding for that character
        if (root.getLeft() == null && root.getRight() == null) {
            encodings[root.getData().getCharacter()] = code;
        } else {
            // if the node is not a leaf, recur on both children
            recurCode(root.getLeft(), code + "0");
            recurCode(root.getRight(), code + "1");
        }

    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName); // set StdIn to read from the file

        String encoded = ""; // init string to store the encoded text

        // read in the file and encode each character
        while(StdIn.hasNextChar()){
            char c = StdIn.readChar();
            encoded += encodings[c];
        }

        // write the encoded string to the encoded file
        writeBitString(encodedFile, encoded);
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {

        StdOut.setFile(decodedFile); // set StdOut to write to the decoded file

        // read the encoded file into a bit string
        String bitString = readBitString(encodedFile);

        // init variables
        TreeNode curr = huffmanRoot;

        // traverse the tree using the bit string
        for (int i = 0; i < bitString.length(); i++) {
            if (bitString.charAt(i) == '0') {
                curr = curr.getLeft();
            } else {
                curr = curr.getRight();
            }

            // if we reach leaf, write the character to decode file
            if (curr.getLeft() == null && curr.getRight() == null) {
                StdOut.print(curr.getData().getCharacter());
                curr = huffmanRoot;
            }
        }
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
