import java.io.*;

public class SillySorter{
    public static void main(String[] args) throws IOException {
        String inputFilePath = args[0];
        String outputFilePath = args[1];
        String line = null;
        File outputFile = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        List myList = new List();
        boolean ascending = false;

        try {
            outputFile = new File(outputFilePath);
            if(!outputFile.exists()){
                outputFile.createNewFile();
            }
            reader = new BufferedReader(new FileReader(
                inputFilePath));
            writer = new BufferedWriter(new FileWriter(
                outputFile, false));
            
            //1. add elements
            while ((line = reader.readLine()) != null){    //read until eof
                if(line == "666"){
                    if(!ascending){
                        ascending = true;
                        myList.add("@");
                    }
                }
                else if(checkLine(line)){
                    myList.add(line);
                }
                else{
                    writer.write("Input error.");
                    return;
                }
            }
            //2. Sort list
            myList.sort(ascending);

            //3. Output list
            myList.outputToFile(writer);
        }
        catch(IOException e) {
            System.err.println(e.toString());
        }
        finally {   
            //close writer and reader
            if(writer != null){
                writer.close();
            }
            if(reader != null){
                reader.close();
            }
        }
    }

    private static boolean checkLine(
    String line){
        if(line.equals("Do") || line.equals("Re")
        || line.equals("Mi") || line.equals("&")
        || line.equals("@")  || line.equals("%")
        || line.equals("Asymbolwithareallylongname")
        || line.equals("$") || line.equals("Fa")
        || line.equals("One") || line.equals("Two")
        || line.equals("Three") || isNaturalNumber(line)){
            return true;
        }
        else{
            return false;
        }
    }

    private static boolean isNaturalNumber(
    String line){
        try{
            int elementVal = Integer.parseInt(line); //convert into int, throws exception if not an integer

            if(String.valueOf(elementVal).length() != line.length() 
            || elementVal < 0){
                return false;
            }
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}

class List{
    private int size;
    private Node headNode;

    public void add(String element){
        Node newHead = new 
            Node(element, headNode);
        this.headNode = newHead;
        size++;
    }

    public void sort(boolean ascending){
        if(ascending){
            ascendingSort();
        }
        else{
            descendingSort();
        }
    }

    public void ascendingSort(){

    }

    public void descendingSort(){
        
    }

    public void outputToFile(BufferedWriter writer)
    throws IOException {
        Node currentNode = headNode;
        writer.write(currentNode.getElement());

        for(int i = 0; i < size-1; i++) {
            currentNode = currentNode.getNext();
            writer.newLine();
            writer.write(currentNode.getElement());
        }
    }
}

class Node{
    private String element;
    private Node next;

    public Node(String element){
        this(element, null);
    }

    public Node(String element, Node next){
        this.element = element;
        this.next = next;
    }
    
    public String getElement(){
        return this.element;
    }

    public Node getNext(){
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setElement(String element) {
        this.element = element;
    }
}