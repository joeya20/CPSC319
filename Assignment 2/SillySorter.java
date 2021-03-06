import java.io.*;

/**
 * symbol encoding:
 * Do - 0.5
 * Re - 100.5
 * Mi - 1000.5
 * & - 3.2
 * \@ - 3.1
 * % - 1005000.5
 * Asymbolwithareallylongname - 55.5
 * $ - 20.5
 * Fa - 15.5
 * One - 103.1
 * Two - 103.2
 * Three - 103.3
 */

public class SillySorter{
    public static void main(String[] args) 
    throws IOException {
        String inputFilePath = args[0];
        String outputFilePath = args[1];
        String line = null;
        File outputFile = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        boolean ascending = false;
        List myList = new List();

        try {
            outputFile = new File(outputFilePath);
            if(!outputFile.exists()) {
                outputFile.createNewFile();
            }
            reader = new BufferedReader(new FileReader(
                inputFilePath));
            writer = new BufferedWriter(new FileWriter(
                outputFile, false));
            
            //1. read file and add elements to list
            while ((line = reader.readLine()) != null) {
                if(isNaturalNumber(line)) {
                    if(line.equals("666")) {
                        if(!ascending) {
                            ascending = true;
                            myList.add(getSymbolCode("@"));
                        }
                    }
                    else {
                        myList.add(Double.parseDouble(line));
                    }
                }
                else if(isSymbol(line)) {
                    myList.add(getSymbolCode(line));
                }
                else {
                    writer.write("Input error.");
                    return;
                }
            }

            //2. Sort list in descending order
            myList.sort();

            //3. reverse the list for ascending order
            if(ascending) {
                myList.reverse();
            }

            //4. Output list to file
            outputToFile(writer, myList);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            //close writer and reader
            if(writer != null) {
                writer.close();
            }
            if(reader != null) {
                reader.close();
            }
        }
    }

    /**
     * prints the list content to the output file using the buffered writer
     * @param writer
     * @param listToPrint
     * @throws IOException
     */
    private static void outputToFile(
    BufferedWriter writer,
    List listToPrint)
    throws IOException {
        for(int i = 0; i < listToPrint.length(); i++) {
            if(i != 0) {
                writer.newLine();
            }
            
            double output = listToPrint.get(i);
            if(isCodedSymbol(output)) {   //if the value is the encoded value of a symbol, output the symbol
                writer.write(getSymbol(output));
            }
            else {
                writer.write(String.valueOf((long)output));
            }
        }
    }
    
    /**
     * Checks if integer representation of line is a natural number
     * @param line string containing value to check
     * @return true if value of string is a natural number
     */
    private static boolean isNaturalNumber(String line) {
        try {
            long elementVal = Long.parseLong(line); //convert into int, throws exception if not an integer

            if((String.valueOf(elementVal).length() != line.length()) || (elementVal < 0)) { //if contains leading zeros or is negative
                return false;
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    /**
     * Checks if the double argument provided is the encoded value of a symbol
     * @param value value to check
     * @return true if value is the value of a symbol
     */
    private static boolean isCodedSymbol(double value) {
        return (value - (long)value) != 0;
    }

    /**
     * checks if line is one of the twelve symbols allowed
     * @param line
     * @return
     */
    private static boolean isSymbol(String line) {
        if(line.equals("Do") || line.equals("Re") ||
        line.equals("Mi") || line.equals("&") || line.equals("@") ||
        line.equals("%") || line.equals("Asymbolwithareallylongname") || 
        line.equals("$") || line.equals("Fa") || line.equals("One") ||
        line.equals("Two") || line.equals("Three")) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * converts from the symbol to its encoded value
     * @param line
     * @return
     */
    private static double getSymbolCode(String line) {
        if(line.equals("Do")) {
            return 0.5;
        }
        else if(line.equals("Re")) {
            return 100.5;
        }
        else if(line.equals("Mi")) {
            return 1000.5;
        }
        else if(line.equals("&")) {
            return 3.2;
        }
        else if(line.equals("@")) {
            return 3.1;
        }
        else if(line.equals("%")) {
            return 1005000.5;
        }
        else if(line.equals("Asymbolwithareallylongname")) {
            return 55.5;
        }
        else if(line.equals("$")) {
            return 20.5;
        }
        else if(line.equals("Fa")) {
            return 15.5;
        }
        else if(line.equals("One")) {
            return 103.1;
        }
        else if(line.equals("Two")) {
            return 103.2;
        }
        else if(line.equals("Three")) {
            return 103.3;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * converts from encoded double value to Symbol
     * @param value value of symbol
     * @return  String containing symbol
     */
    private static String getSymbol(double value) {
        if(value == 0.5) {
            return "Do";
        }
        else if(value == 100.5) {
            return "Re";
        }
        else if(value == 1000.5) {
            return "Mi";
        }
        else if(value == 3.2) {
            return "&";
        }
        else if(value == 3.1) {
            return "@";
        }
        else if(value == 1005000.5) {
            return "%";
        }
        else if(value == 55.5) {
            return "Asymbolwithareallylongname";
        }
        else if(value == 20.5) {
            return "$";
        }
        else if(value == 15.5) {
            return "Fa";
        }
        else if(value == 103.1) {
            return "One";
        }
        else if(value == 103.2) {
            return "Two";
        }
        else if(value == 103.3) {
            return "Three";
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}

class List{
    private final static int DEFAULTSIZE = 20;
    private int arrayLength = DEFAULTSIZE;
    private int length;
    private double[] list = new double[arrayLength];

    public double get(int index) {
        return list[index];
    }
    
    public int length() {
        return length;
    }

    /**
     * adds the element to the end of the list
     * @param element
     */
    public void add(double element) {
        //if max size of array is reached, expand array
        if(length == arrayLength) {
            arrayLength += DEFAULTSIZE;
            double[] newList = new double[arrayLength];
            System.arraycopy(list, 0, newList, 0, length);
            list = newList;
        }
        list[length++] = element;
    }

    /**
    * implements the bubble sort algorithm to sort the list in descending order
    */
    public void sort() {
        int i;
        int j;
        double swap;
        boolean swapped;
        for (i = 0; i < length-1; i++) {
            swapped = false;

            for (j = 0; j < length-i-1; j++) {
                if (list[j] < list[j+1]) {
                    swap = list[j]; 
                    list[j] = list[j+1];
                    list[j+1] = swap;
                    swapped = true;
                }
            }
            if(!swapped) {
                break;
            }
        }
    }

    /**
    * reverses the list; O(n) time complexity
    */
    public void reverse() {
        for(int i = 0; i < length/2; i++) {
            double temp = list[i];
            list[i] = list[length-i-1];
            list[length-i-1] = temp;
        }
    }
}