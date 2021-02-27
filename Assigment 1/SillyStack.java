import java.io.*;
import java.util.EmptyStackException;

public class SillyStack {
    static boolean firstLine = true;
    public static void main(String[] args) throws IOException {

        String inputFilePath = args[0];
        String outputFilePath = args[1];
        File outputFile = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        Stack myStack = null;
        String line;

		try {
            outputFile = new File(outputFilePath);
            if(!outputFile.exists()){
                outputFile.createNewFile();
            }
            
			reader = new BufferedReader(new FileReader(
				inputFilePath));
            writer = new BufferedWriter(new FileWriter(
                outputFile, false));
            myStack = new Stack();
            
			while ((line = reader.readLine()) != null){    //read until eof
                /*********  if line starts with "push(" *********************/
                if(line.startsWith("push(")){
                    try {
                        //get bracket count
                        int openBracketCount = line.length() -
                            line.replaceAll("\\(", "").length();
                        int closeBracketCount = line.length() - 
                            line.replaceAll("\\)", "").length();

                        //if openBracketCount > closeBracketCount, automatic "Input error."
                        if(openBracketCount <= closeBracketCount){
                            //get push command
                            String command = line.substring(0, 
                                findClosingBracket(line, line.indexOf("("))+1);

                            //if line contains more than just the command, automatic "Input error."
                            if(line.length() > command.length()){
                                printOutput(writer, "Input error.");
                                break;
                            }
                            //if line consists of only push(arg)
                            else{
                                String arg = command.substring(    //get argument
                                    command.indexOf("push(")+5,
                                    command.lastIndexOf(")"));
                                int elementVal = Integer.parseInt(arg); //convert into int, throws exception if not an integer

                                if(String.valueOf(elementVal).length() != arg.length()){
                                    throw new IllegalArgumentException();   //if leading zeros present, throw exception
                                }

                                //throw exception if arg < 0
                                if(elementVal < 0){
                                    throw new IllegalArgumentException();
                                }
                                //if argument, is a positive integer, with no leading zeros present
                                else{                                               
                                    //if value = 0 and stack is empty, add to stack
                                    if(elementVal == 0){
                                        if(myStack.isEmpty())
                                        {
                                            myStack.push(0);   
                                        }
                                    }
                                    //if value = 666, add to stack 3 times
                                    else if(elementVal == 666){ 
                                        myStack.push(666);
                                        myStack.push(666);
                                        myStack.push(666);
                                    }
                                    //if value = 3, add 7
                                    else if(elementVal == 3){   
                                        myStack.push(7);
                                    }
                                    //if value = 13, empty and output stack first then add 13
                                    else if(elementVal == 13){  
                                        while(!myStack.isEmpty()){
                                            printOutput(
                                                writer, 
                                                Integer.toString(myStack.pop()));
                                        }
                                        myStack.push(13);
                                    }
                                    //for any other number just add to stack
                                    else{   
                                        myStack.push(elementVal);
                                    }
                                }
                            }
                        }
                        else{
                            printOutput(writer, "Input error.");
                            break;
                        }
                        
                    }
                    //output "Imput error." and end program
                    catch (IllegalArgumentException e){
                        printOutput(writer, "Imput error.");
                        break;
                    }
                }
                /*********  if line is exactly "pop()" *********************/
                else if(line.replaceFirst("^pop\\(\\)", "").length() == 0){  

                    //if stack is empty, output error and end program
                    if(myStack.isEmpty()){
                        printOutput(writer, "Error");
                        break;
                    }
                    else{
                        int topVal = myStack.top();
                        //if top of stack = 666, output 666 and pop next element
                        if(topVal == 666){
                            printOutput(writer, Integer.toString(myStack.pop()));
                            if(!myStack.isEmpty()){
                                myStack.pop();
                            }
                        }
                        //if top of stack = 7, output 7 but dont remove
                        else if(topVal == 7){
                            printOutput(writer, Integer.toString(topVal));
                        }
                        //if top of stack = 42, output 42 and pop rest of stack
                        else if(topVal == 42){
                            printOutput(writer, Integer.toString(myStack.pop()));
                            while(!myStack.isEmpty()){
                                myStack.pop();
                            }
                        }
                        //for any other number, just pop and output
                        else{
                            printOutput(writer, Integer.toString(myStack.pop()));
                        }
                    }
                }
                /*********  if line is exactly "top()" *********************/
                else if(line.replaceFirst("^top\\(\\)", "").length() == 0){

                    //if stack is empty, output null and continue
                    if(myStack.isEmpty()){
                        printOutput(writer, "null");
                    }
                    else{
                        int topVal = myStack.top();
                        //if top of stack = 666, output 999
                        if(topVal == 666){
                            printOutput(writer, "999");
                        }
                        //if top of stack = 7, pop 7 but dont output
                        else if(topVal == 7){   
                            myStack.pop();
                        }
                        //if top of stack = 319, output 666
                        else if(topVal == 319){ 
                            printOutput(writer, "666");
                        }
                        //for any other number, just output number
                        else{
                            printOutput(writer, Integer.toString(topVal));
                        }
                    }
                }
                /*************** if line is not "push(.*)", "pop()" or "top()" *******************/
                else{
                    printOutput(writer, "Input error.");
                    break;
                }
			}
		}
        catch(IOException e){
			e.getStackTrace();
		}
        finally {   
            //close writer and reader
            if(writer != null)
            {
                writer.close();
            }
            if(reader != null)
            {
                reader.close();
            }
        }
    }

    private static void printOutput(
    BufferedWriter writer,
    String message) 
    throws IOException{
        if(firstLine == false){
            writer.newLine();
        }
        else{
            firstLine = false;
        }
        writer.write(message);
    }

    public static int findClosingBracket(
    String text, 
    int openPosition) {
        int closePosition = openPosition;
        int counter = 1;
        while (counter > 0) {
            char c = text.charAt(++closePosition);
            if (c == '(') {
                counter++;
            }
            else if (c == ')') {
                counter--;
            }
        }

        return closePosition;
    }
}

class Stack {
    //fields
    private Node head;
    private int size;

    //Constructors
    public Stack(){
        head = null;
        size = 0;
    }
    
    public Stack(int element){
        this(new Node(element));
    }

    public Stack(Node headNode){
        head = headNode;
        size++;
    }

    //Methods
    public boolean isEmpty(){
        return size == 0;
    }

    public int top() 
    throws EmptyStackException{
        if(isEmpty()){
            throw new EmptyStackException();
        }
        else{
            return head.element;
        }
    }

    public int pop(){
        int returnVal = top();  //throws exception if stack is empty
        this.head = head.next;
        size--;

        return returnVal;
    }
    
    public void push(int elementVal){
        Node newHead = new 
            Node(elementVal, head);
        this.head = newHead;
        size++;
    }
}

class Node {

    //fields
    Node next;
    int element;

    //constructors
    Node(int element){
        this(element, null);
    }

    Node(int element, Node next){
        this.element = element;
        this.next = next;
    }
}