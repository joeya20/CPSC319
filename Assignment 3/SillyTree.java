import java.io.*;
import java.util.regex.*;

public class SillyTree {
    public static void main(String[] args) throws IOException {
        String inputFilePath = args[0];
        String outputFilePath = args[1];
        // String inputFilePath = "input.txt";
        // String outputFilePath = "output.txt";
        File outputFile = null;
        BufferedReader reader = null;
        String line = null;
        Tree myTree = null;

		try {
            outputFile = new File(outputFilePath);
            if(!outputFile.exists()){
                outputFile.createNewFile();
            }
			reader = new BufferedReader(new FileReader(
				inputFilePath));
            
            myTree = new Tree(outputFilePath);

            // myTree.addLeftChild("root", "test");
            // myTree.addMiddleChild("root", "test");
            // myTree.addRightChild("root", "test");
            // myTree.addRightChild("root", "$success");
            // myTree.delLeftChild("root");
            // myTree.getRoot().setLeftChild(`("test", myTree.getRoot().getLevel()+1));
            // myTree.getRoot().setMiddleChild(new Node("test", myTree.getRoot().getLevel()+1));
            // myTree.getRoot().setRightChild(new Node("test", myTree.getRoot().getLevel()+1));
            // Node l = myTree.getRoot().getLeftChild();
            // Node r = myTree.getRoot().getRightChild();
            // l.setLeftChild(new Node("test", l.getLevel()+1));
            // l.setMiddleChild(new Node("test", l.getLevel()+1));
            // l.setRightChild(new Node("test", l.getLevel()+1));
            // r.setLeftChild(new Node("test", r.getLevel()+1));
            // r.setMiddleChild(new Node("test", r.getLevel()+1));
            // r.setRightChild(new Node("test", r.getLevel()+1));
            // myTree.exchange("test", "success");
            // l.getLeftChild().setElement("test");
            // r.setElement("test");

            // myTree.addLeftChild("test", "success");
            // l.setLeftChild(new Node("test", l.getLevel()+1));
            // r.setMiddleChild(new Node("test", r.getLevel()+1));
            // l.getLeftChild().setLeftChild(new Node("test", l.getLevel()+2));
            // r.getMiddleChild().setRightChild(new Node("test", r.getLevel()+2));
            // Node test = myTree.searchLeft(myTree.getRoot(), "test");
            // test.setElement("success");
            // myTree.delLeftChild("test");
            // myTree.print();
            // System.out.println("AddL(,,q)".replaceFirst("^AddL\\([^\s]+[,]+[^\s]+\\)", ""));

			while ((line = reader.readLine()) != null){    //read until eof

                if(line.replaceFirst("^AddL\\([^\s]+[,]+[^\s]+\\)", "").length() == 0) {
                    String[] funcArgs = findFuncArgs(line);
                    // System.out.println("myTree.addLeftChild(\"" + funcArgs[0] + "\",\"" + funcArgs[1] + "\")");
                    myTree.addLeftChild(funcArgs[0], funcArgs[1]);
                }
                else if(line.replaceFirst("^AddM\\([^\s]+[,]+[^\s]+\\)", "").length() == 0) {
                    String[] funcArgs = findFuncArgs(line);
                    // System.out.println("myTree.addMiddleChild(\"" + funcArgs[0] + "\",\"" + funcArgs[1] + "\")");
                    myTree.addMiddleChild(funcArgs[0], funcArgs[1]);
                }
                else if(line.replaceFirst("^AddR\\([^\s]+[,]+[^\s]+\\)", "").length() == 0) {
                    String[] funcArgs = findFuncArgs(line);
                    // System.out.println("myTree.addRightChild(\"" + funcArgs[0] + "\",\"" + funcArgs[1] + "\")");
                    myTree.addRightChild(funcArgs[0], funcArgs[1]);
                }
                else if(line.replaceFirst("^Exchange\\([^\s]+[,]+[^\s]+\\)", "").length() == 0) {
                    String[] funcArgs = findFuncArgs(line);
                    // System.out.println("myTree.exchange(\"" + funcArgs[0] + "\",\"" + funcArgs[1] + "\")");
                    myTree.exchange(funcArgs[0], funcArgs[1]);
                }
                else if(line.replaceFirst("^DelL\\([^\s]+\\)", "").length() == 0) {
                    String a = line.substring(5, line.length()-1);
                    // System.out.println("myTree.delLeftChild(" + a + ")");
                    myTree.delLeftChild(a);
                }
                else if(line.replaceFirst("^DelM\\([^\s]+\\)", "").length() == 0) {
                    String a = line.substring(5, line.length()-1);
                    // System.out.println("myTree.delMiddleChild(" + a + ")");
                    myTree.delMiddleChild(a);
                }
                else if(line.replaceFirst("^DelR\\([^\s]+\\)", "").length() == 0) {
                    String a = line.substring(5, line.length()-1);
                    // System.out.println("myTree.delRightChild(" + a + ")");
                    myTree.delRightChild(a);
                }
                else if(line.replaceFirst("^Print\\(\\)", "").length() == 0) {
                    // System.out.println("myTree.print()");
                    myTree.print();
                }
                else {
                    throw new IllegalArgumentException();
                }
			}
		}
        catch(IllegalArgumentException ex) {
            myTree.writeToFile("Input error.");
        }
        finally {   
            //close writer and reader
            reader.close();
            myTree.closeWriter();
        }
    }

    public static String[] findFuncArgs(String line) {
        
        String[] returnVal = new String[2];
        Matcher m1 = Pattern.compile("[,]{1}").matcher(line);
        Matcher m2 = Pattern.compile("[,]{2,}").matcher(line);

        if(m1.find()) {
            if(m2.find()) {
                if(m1.start() == m2.start()) {  //if first comma has adjacent commas, get index of last comma
                   returnVal[0] = line.substring(line.indexOf("(")+1, m2.end()-1);
                   returnVal[1] = line.substring(m2.end(), line.length()-1);
                   return returnVal;
                }
                else {  //if there are no commas adjacent to the first comma
                    returnVal[0] = line.substring(line.indexOf("(")+1, m1.end()-1);
                    returnVal[1] = line.substring(m1.end(), line.length()-1);
                    return returnVal;
                }
            }
            else {  //if there are no adjacent commas at all
                returnVal[0] = line.substring(line.indexOf("(")+1, m1.end()-1);
                returnVal[1] = line.substring(m1.end(), line.length()-1);
                return returnVal;
            }
        }
        throw new IllegalArgumentException("Invalid input arguments");   //throw exception to catch in main if there are no commas at all
    }
}

class Tree {
    private Node root;
    private BufferedWriter writer;
    private boolean firstLine = true;

    public Tree(String outputFilePath) throws IOException {
        root = new Node("root");
        this.writer = new BufferedWriter(new FileWriter(new File(outputFilePath), false));
    }

    public Node getRoot() {
        return root;
    }

    //*************************************** Main methods ***************************************************/
    public void addLeftChild(String key, String childVal) throws IOException {
        Node parent = searchRight(root, key);

        if(parent == null) {
            return;
        }
        else {  //if parent exists
            if(childVal.replaceFirst("^\\$", "").length() == childVal.length()-1) {
                if(parent.getLeftChild() != null) {
                    parent.getLeftChild().setElement(childVal.substring(1));
                }
                else {
                    parent.setLeftChild(new Node(childVal.substring(1), parent.getLevel()+1));
                }
            }
            else {
                if(parent.getLeftChild() == null) {
                    parent.setLeftChild(new Node(childVal, parent.getLevel()+1));
                }
                else {
                    writeToFile("Add operation not possible.");
                    writer.newLine();
                }
            }
        }
    }
    
    public void addMiddleChild(String key, String childVal) throws IOException {
        Node parent = searchRight(root, key);

        if(parent == null) {
            return;
        }
        else {  //if parent exists
            if(childVal.replaceFirst("^\\$", "").length() == childVal.length()-1) {
                if(parent.getMiddleChild() != null) {
                    parent.getMiddleChild().setElement(childVal.substring(1));
                }
                else {
                    parent.setMiddleChild(new Node(childVal.substring(1), parent.getLevel()+1));
                }
            }
            else {
                if(parent.getMiddleChild() == null) {
                    parent.setMiddleChild(new Node(childVal, parent.getLevel()+1));
                }
                else {
                    writeToFile("Add operation not possible.");
                }
            }
        }
    }

    public void addRightChild(String key, String childVal) throws IOException {
        Node parent = searchRight(root, key);

        if(parent == null) {
            return;
        }
        else {  //if parent exists
            if(childVal.replaceFirst("^\\$", "").length() == childVal.length()-1) {
                if(parent.getRightChild() != null) {
                    parent.getRightChild().setElement(childVal.substring(1));
                }
                else {
                    parent.setRightChild(new Node(childVal.substring(1), parent.getLevel()+1));
                }
            }
            else {
                if(parent.getRightChild() == null) {
                    parent.setRightChild(new Node(childVal, parent.getLevel()+1));
                }
                else {
                    writeToFile("Add operation not possible.");
                }
            }
        }
    }
    
    /**
     * deletes the left child of the node with element key
     */
    public void delLeftChild(String key) {
        Node parent = searchLeft(root, key);

        if(parent != null) {    //if parent exists, delete its left subtree
            parent.setLeftChild(null);
        }
    }

    /**
     * deletes the middle child of the node with element key
     */
    public void delMiddleChild(String key) {
        Node parent = searchLeft(root, key);

        if(parent != null) {    //if parent exists, delete its middle subtree
            parent.setMiddleChild(null);
        }
    }

    /**
     * deletes the right child of the node with element key
     */
    public void delRightChild(String key) {
        Node parent = searchLeft(root, key);

        if(parent != null) {    //if parent exists, delete its right subtree
            parent.setRightChild(null);
        }
    }

    /**
     * exchanges all nodes with element key with element newVal
     */
    public void exchange(String key, String newVal) {
        Node nodeTochange = null;

        while((nodeTochange = searchRightRecursive(root, key)) != null) {    //search for every node with element key

            if(newVal.replaceFirst("^\\$", "").length() == newVal.length()-1) {
                nodeTochange.setElement(nodeTochange.getElement() + newVal.substring(1));
            }
            else {
                nodeTochange.setElement(newVal);
            }
        }
    }

    //*************************************** Search ***************************************************/
    /**
     * returns the rightmost, deepest node with matching element.
     */
    public Node searchRight(Node root, String key) {

        List matchesFound = new List();
        Node match = null;

        //find all matches and add to list
        while((match=searchRightRecursive(root, key)) != null) {
            matchesFound.add(match);
            match.setElement("");  //change the element so searchRightRecursive can find another match
        }
        
        //if any match was found, return the leftmost, highest level node
        if(matchesFound.length() > 0) {
            
            match = matchesFound.get(0);

            for(int j = 0; j < matchesFound.length(); j++) {
                //if a node with a higher level is found, switch match
                if(matchesFound.get(j).getLevel() > match.getLevel()){
                    match = matchesFound.get(j);
                }
                matchesFound.get(j).setElement(key);    //reset element back to right string
            }
        }
        
        return match;
    }

    /**
     * returns the leftmost, deepest node with matching element.
     */
    public Node searchLeft(Node root, String key) {

        List matchesFound = new List();
        Node match = null;

        //find all matches and add to list
        while((match=searchLeftRecursive(root, key)) != null) {
            matchesFound.add(match);
            match.setElement(""); //change the element so searchLeftRecursive can find another match
        }
        
        //if any match was found, return the leftmost, highest level node
        if(matchesFound.length() > 0) {
            
            match = matchesFound.get(0);

            for(int j = 0; j < matchesFound.length(); j++) {
                //if a node with a higher level is found, switch match
                if(matchesFound.get(j).getLevel() > match.getLevel()){
                    match = matchesFound.get(j);
                }
                matchesFound.get(j).setElement(key);    //reset element back to right string
            }
        }
        
        return match;
    }

    /**
     * returns the rightmost node with matching element
     */
    private Node searchRightRecursive(Node current, String key) {
        Node returnVal = null;

        //check right subtree
        if(current.getRightChild() != null) {
            if((returnVal = searchRightRecursive(current.getRightChild(), key)) != null) {
                return returnVal;
            }
        }
        //check middle subtree
        if(current.getMiddleChild() != null) {
            if((returnVal = searchRightRecursive(current.getMiddleChild(), key)) != null) {
                return returnVal;
            }
        }
        //check left subtree
        if(current.getLeftChild() != null) {
            if((returnVal = searchRightRecursive(current.getLeftChild(), key)) != null) {
                return returnVal;
            }
        }
        //base case
        if(current.getElement().equals(key)) {
            return current;
        }
        return null;
    }

    /**
     * returns the leftmost node with matching element
     */
    private Node searchLeftRecursive(Node current, String key) {
        Node returnVal = null;

        //check left subtree
        if(current.getLeftChild() != null) {
            if((returnVal = searchLeft(current.getLeftChild(), key)) != null) {
                return returnVal;
            }
        }
        //check middle subtree
        if(current.getMiddleChild() != null) {
            if((returnVal = searchLeft(current.getMiddleChild(), key)) != null) {
                return returnVal;
            }
        }
        //check right subtree
        if(current.getRightChild() != null) {
            if((returnVal = searchLeft(current.getRightChild(), key)) != null) {
                return returnVal;
            }
        }
        //base case
        if(current.getElement().equals(key)) {
            return current;
        }
        return null;
    }

    //*************************************** Print ***************************************************/

    /**
     * prints the tree to the output file, level by level
     */
    public void print() throws IOException{
        int height = maxLevel(root);
        int i;
        int j;
        List[] levelNodes = new List[height];

        //add all nodes of all levels to List array
        for(i = 1; i <= height; i++) {
            levelNodes[i-1] = new List();
            getLevelNodes(root, i, levelNodes[i-1]);
        }

        //Print List array, level by level
        for(i = 0; i < levelNodes.length; i++) {

            //start new line
            if(firstLine) {
                firstLine = false;
            }
            else {
                writer.newLine();
            }

            //print every element in level
            for (j = 0; j < levelNodes[i].length()-1; j++) {
                writer.write(levelNodes[i].get(j).getElement() + " ; ");
            }
            writer.write(levelNodes[i].get(j).getElement());
        }
    }

    /**
     * appends the supplied list with the elements of the desired level
     */
    public void getLevelNodes(Node current, int level, List list) {           
        //base case     
        if (current == null) {
            return;
        }
        //add node to list once the proper level is reached
        if (level == 1) {
            list.add(current);
        }
        //if not at the right level, go down one level
        else if (level > 1) {
            getLevelNodes(current.getLeftChild(), level-1, list);
            getLevelNodes(current.getMiddleChild(), level-1, list);
            getLevelNodes(current.getRightChild(), level-1, list);
        }
    }

    /**
     * returns the max level of the tree, root starts at 1
     */
    private int maxLevel(Node current) {
        //base case
        if (current == null) {
            return 0;
        }
        else {
            // get the depth of each subtree
            int lDepth = maxLevel(current.getLeftChild());
            int mDepth = maxLevel(current.getMiddleChild());
            int rDepth = maxLevel(current.getRightChild());
  
            // use the larger one
            return (Math.max(lDepth, Math.max(mDepth, rDepth)) + 1);
        }
    }
    
    /**
     * writes the message to the output file
     */
    public void writeToFile(String message) throws IOException {
        if(firstLine) {
            firstLine = false;
        }
        else {
            writer.newLine();
        }
        
        writer.write(message);
    }

    /**
     * closes the bufferedWriter object
     */
    public void closeWriter() throws IOException {
        this.writer.close();
    }
}

class Node {
    private Node leftChild;
    private Node middleChild;
    private Node rightChild;
    private int level;
    private String element;

    public Node(String element) {
        this.element = element;
        level = 1;
    }

    public Node(String element, int level) {
        this.element = element;
        this.level = level;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setMiddleChild(Node middleChild) {
        this.middleChild = middleChild;
    }
    
    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getMiddleChild() {
        return middleChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public int getLevel() {
        return level;
    }
}

class List{
    private final static int DEFAULTSIZE = 20;
    private int arrayLength = DEFAULTSIZE;
    private int length;
    private Node[] list = new Node[arrayLength];

    public Node get(int index) {
        return list[index];
    }
    
    public int length() {
        return length;
    }

    /**
     * adds the element to the end of the list
     */
    public void add(Node element) {
        //if max size of array is reached, expand array
        if(length == arrayLength) {
            arrayLength += DEFAULTSIZE;
            Node[] newList = new Node[arrayLength];
            System.arraycopy(list, 0, newList, 0, length);
            list = newList;
        }
        list[length++] = element;
    }
}