/* DEEPEN PANCHAL CS610 1098 prp */

import java.io.*;
import java.lang.*;
import java.util.*;

public class pgrk_1098
{
  
    public static void main(String[] args) throws IOException, FileNotFoundException
    {
        //Check for all arguments being present
        if(args.length != 3)
        {
            System.out.println("Arguments missing! Please enter all");
            return;
        }
        
        String error = " ";
        
        //Taking values from Arguments and initializing them
        String inputFile = args[2];
        double inputValue = Double.parseDouble(args[1]);
        int iterations = 0;
        try
        {
        iterations = Integer.parseInt(args[0]);
       
        }
        catch (NumberFormatException no)
        {
            char ch=args[0].charAt(0);
        if(!Character.isDigit(ch))
        {
            System.out.println("Enter an integer value for Iterations.");
            return;
        }
        }
    
        try (BufferedReader bufr = new BufferedReader(new FileReader(inputFile)))
        {
            //Finding the number of nodes
            String tempo = bufr.readLine();
            String[] separator = tempo.split(" ");
            int noNodes = 0;
            if(tempo != null)
                noNodes = Integer.parseInt(separator[0]);
            
            //Create the Adjacency Matrix
            double[][] adjMat = new double[noNodes][noNodes];
            
            //Assign values to the Adjacency Matrix
            int r;
            int c;
            String cursor;
            while ((cursor = bufr.readLine()) != null)
            {
                String[] another_separator = cursor.split(" ");
                r = Integer.parseInt(another_separator[0]);
                c = Integer.parseInt(another_separator[1]);
                adjMat[r][c] = 1;
            }
        
            //Create array to store outgoing links
            double[] outLinks = new double[noNodes];
            for(int i = 0; i < noNodes; i++)
            {
                outLinks[i] = 0;
                for(int j = 0; j < noNodes; j++)
                    outLinks[i] += adjMat[i][j];
            }
        
            
            //Check for number of nodes
            if(noNodes > 10)
            {
                iterations = 0;
                inputValue = -1;
            }
            
            //Initialize the value of InitialValue
            double initialValue = 0.0;
            if(inputValue == 0 || inputValue == 1)
                initialValue = inputValue;
            else if(inputValue == -1)
                initialValue = (double)1/noNodes;
            else if(inputValue == -2)
                initialValue = (double)1/Math.sqrt(noNodes);
            else
            {
                System.out.println("Incorrect Initial Value. Please enter 1, -1 or -2.");
                return;
            }
            
            //Determine value of errorRate
            double errorRate = 0.0;
            if(iterations < 0)
                errorRate = (double)Math.pow(10, (iterations));
            else if(iterations == 0)
                errorRate = (double)Math.pow(10, (-5));
        
            //Computing constants
            double d = 0.85;
            double constantVal;
            constantVal = (1-d)/noNodes;
            
            //Previous PageRank Array
            double[] previouspageRank = new double[noNodes];
            
            //Current PageRank Array
            double[] currentpageRank = new double[noNodes];
            
            //Initialize pagerank values for Base step
            for(int i = 0; i < noNodes; i++)
                previouspageRank[i] = initialValue;
            
            
            int iterationNo = 0;
            String outputLine;
            outputLine = "Base  : " + iterationNo + "  :";
            for (int i = 0; i < noNodes; i++) {
                outputLine += " P["+ i +"] = "
                + String.format("%.7f", previouspageRank[i]);
            }
            System.out.println(outputLine);
            
            //Initialize the value for Convergence & icreasing iterationNo
            boolean checkConvergence = false;
            iterationNo = 1;
            
            if(iterations > 0)
            {
                while (iterationNo < (iterations+1))
                {
                    
                    
                    //Store bracket value
                    double[] nonConstant = new double[noNodes];
                    for(int i = 0; i < noNodes; i++)
                        nonConstant[i] = 0.0;
                    
                    for(int i = 0; i < noNodes; i++)
                    {
                        for(int j = 0; j < noNodes; j++)
                        {
                            if(adjMat[j][i] == 1)
                                nonConstant[i] += previouspageRank[j]/outLinks[j];
                        }
                    }

                    
                    for(int i = 0; i < noNodes; i++)
                        currentpageRank[i] = constantVal + d*nonConstant[i];
                    
                    outputLine = "Iter  :" + iterationNo + "    :";
                    for (int i = 0; i < noNodes; i++)
                    {
                        outputLine += " P["+i+"] = "
                        + String.format("%.7f", currentpageRank[i]);
                    }
                    System.out.println(outputLine);
                    
                    //Copy previous to current
                    for(int i = 0; i < noNodes; i++)
                        previouspageRank[i] = currentpageRank[i];
                    iterationNo++;
                }
            }
            else
            {
                while(!checkConvergence)
                {
                    //Store bracket value
                    double[] nonConstant = new double[noNodes];
                    for(int i = 0; i < noNodes; i++)
                        nonConstant[i] = 0.0;
                    
                    
                    for(int i = 0; i < noNodes; i++)
                    {
                        for(int j = 0; j < noNodes; j++)
                        {
                            if(adjMat[j][i] == 1)
                                nonConstant[i] += previouspageRank[j]/outLinks[j];
                        }
                    }
                    
                    for(int i = 0; i < noNodes; i++)
                        currentpageRank[i] = constantVal + d*nonConstant[i];
                    
                    //Get value from Convergence method to continue the loop
                    pgrk_1098 pgrk = new pgrk_1098();
                    boolean checkPageConvergence =pgrk.checkConvergence(currentpageRank, previouspageRank, errorRate, noNodes);
                    
                    if(checkPageConvergence)
                        break;
                    
                    //Display Page Rank values for remaining Iterations
                    outputLine = "Iter  : " + iterationNo + "  :";
                    for (int i = 0; i < noNodes; i++)
                    {
                        outputLine += " P["+i+"] = "
                        + String.format("%.7f", currentpageRank[i]);
                    }
                    System.out.println(outputLine);
                    
                    //Copy previous to current
                    for(int i = 0; i < noNodes; i++)
                         previouspageRank[i] = currentpageRank[i];
                    
                    iterationNo++;
                
                }
                
            }
            
            
            
        }
        catch(FileNotFoundException exception)
        {
            error +="File not found. Make sure file is in the same folder. ";
            System.out.println(error);
        }
    }
    //Check the difference in the old and new values
    private static boolean checkConvergence(double[] newval, double[] oldval, double errorRate, int noNodes)
    {
        boolean checkConvergence = true;
        for (int i = 0; i < noNodes; i++)
        {
            if(Math.abs(oldval[i] - newval[i]) > errorRate)
                checkConvergence = false;
        }
        return checkConvergence;
    }
};
