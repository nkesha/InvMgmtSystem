
import java.io.*;
import java.util.*;

public class A5Nkeshimana7735006
{
    public static void main(String[] args) {
        System.out.println("Comp 2140 Assignment 5 Winter 2016");
        System.out.println("------------------------------------");

        readInventory ();

        System.out.println("\nPROGRAM ENDS.");
    }

    public static void readInventory ()
    {
        Scanner keyboard;
        String fileName;
        FileReader fileReaderIn;
        BufferedReader fileIn;
        String inputLine;

        // Creating my BST tree.
        BST categories = new BST();
        TwoThreeTree products = new TwoThreeTree( );


        String [] tokens ;

        String nameStore;
        int size;

        String productCode, category, description;
        int aisle, currInventory;

        keyboard = new Scanner( System.in );
        System.out.println( "\nEnter the input file name (.txt files only): " );
        fileName = keyboard.nextLine();

        try
        {
            fileReaderIn = new FileReader(fileName);
            fileIn = new BufferedReader(fileReaderIn);
            inputLine = fileIn.readLine();
            nameStore = inputLine;
            inputLine = fileIn.readLine();
            size = Integer.parseInt(inputLine);

            ProductRecord currRecord = null;

            // Starting to Fill my BST
            for (int i = 0; i < size; i++)
            {
                inputLine = fileIn.readLine();

                currRecord = parseProductRecord (inputLine);

                //inserting record in BST (by category)

                System.out.println (currRecord.toString());
                categories.insert(currRecord.category, currRecord );
                products.insert(currRecord.productCode, currRecord);

            }
            inputLine = fileIn.readLine();

            while (inputLine != null)
            {
                tokens = inputLine.split ("\\s");

                // handles Look up by category command.
                if (tokens[0].equals("LC"))
                {

                    System.out.println ("\nCommand: LC " + tokens[1].substring(1, tokens[1].length() - 1));
                    categories.printKeyMatches(tokens[1].substring(1, tokens[1].length() - 1));

                }

                // handles Look up by product code command
                else if (tokens[0].equals("LP"))
                {
                    System.out.println ("\nCommand : LP "+ tokens[1]);

                    if ( products.find (tokens[1]) != null )
                        System.out.println (products.find (tokens[1]).toString());
                    else
                        System.out.println ("No items found for key = " + tokens[1]);
                }


                //case for Selling command
                else if (tokens[0].equals("S"))
                {
                    System.out.println ("\nCommand : S "+ tokens[1] + " "+  tokens[2]);

                    ProductRecord currElement = products.find (tokens[1]);

                    if (currElement != null &&
                            currElement.currInventory >= Integer.parseInt(tokens[2]))
                    {
                        currElement.currInventory -= Integer.parseInt(tokens[2]);

                        if (currElement.currInventory == 0)
                        {
                            System.out.println ("\nReorder product " + tokens [1] + " (the last one just sold)" );
                        }

                    }
                    else
                    {
                        System.out.println ("Could not make the Sale");
                    }
                }

                // case to handle Receive command.

                else if (tokens[0].equals("R"))
                {
                    System.out.println ("\nCommand : R "+ tokens[1] + " "+  tokens[2]);
                    ProductRecord currElement = products.find (tokens[1]);

                    if (currElement != null )
                    {
                        currElement.currInventory += Integer.parseInt(tokens[2]);
                    }
                    else
                    {
                        System.out.println ("Item not received");
                    }

                }

                inputLine = fileIn.readLine();
            }

            System.out.println ("\nTable with key = product code ");
            System.out.println("-------------------------------");

            products.printTable();

            System.out.println ("\n*********************************\n");
            System.out.println (nameStore);

            System.out.println ("\nTable with key = category :");
            System.out.println("-------------------------------");

            categories.printTree();
        }

        catch (Exception e)
        {
            System.out.println (e.getMessage ());
            e.printStackTrace();
        }
    }

    /*************************
     *
     * Method parse ProductRecord
     *
     * PURPOSE : take an inputLine and read all the components of the productRecord.
     *
     * - param inputLine
     * - return a product record
     *
     ****************************/

    private static ProductRecord parseProductRecord (String inputLine) {

        ProductRecord result = null;
        String[] tokens;

        String productCode, category, description;
        int aisle, currInventory;

        if (inputLine != null && !inputLine.equals("")) {
            tokens = inputLine.split("\"");
            productCode = tokens[0].trim();
            category = tokens[1];
            description = tokens[3];

            tokens = tokens[4].trim().split("\\s");

            aisle = Integer.parseInt(tokens[0]);
            currInventory = Integer.parseInt(tokens[1]);

            result = new ProductRecord(productCode, category, description, aisle, currInventory);

        }
        return result;
    }
}


