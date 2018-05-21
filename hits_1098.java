/* DEEPEN PANCHAL CS610 1098 prp */

import java.io.*;
import java.lang.*;
import java.util.*;

public class hits_1098{
    
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
        try
        {
            BufferedReader bufr = new BufferedReader(new FileReader(inputFile));
        
            //Finding the number of nodes
            String tempo = bufr.readLine();
            String[] separator = tempo.split(" ");
            int noNodes = 0;
            if(tempo != null)
                noNodes = Integer.parseInt(separator[0]);
            
            //Create the Adjacency Matrix
            double[][] adjMat = new double[noNodes][noNodes];
            
            //Initialize the value for Convergence
            boolean checkConvergence = false;
            
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
           
            //Transpose of the matrix
            double[][] transMat = new double[noNodes][noNodes];
            double temp;
            for(int i = 0; i < noNodes; i++)
            {
                for(int j=0; j < noNodes; j++)
                {
                    temp = adjMat[j][i];
                    transMat[i][j] = temp;
                }
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
            
            //Hub and Authority Arrays for iterations other than base
            double[] newauthorityMat = new double[noNodes];
            double[] newhubbMat = new double[noNodes];
            
            //Initialize Authority Array
            double[] authMat = new double[noNodes];
            for(int i=0; i<noNodes; i++)
            {
                authMat[i] = initialValue;
            }
            
            //Initialize Hub Array
            double[] hubMat = new double[noNodes];
            for(int i = 0; i<noNodes; i++)
            {
                hubMat[i] = initialValue;
            }
            
            //Display Base values for Authority and Hub Arrays
            int iterationNo = 0;
            String outputLine;
            outputLine = "Base  : " + iterationNo + "  :";
            for (int i = 0; i < noNodes; i++) {
                outputLine += " A/H["+ i +"] = "
                + String.format("%.7f", authMat[i])
                + "/"
                + String.format("%.7f", hubMat[i]);
            }
            System.out.println(outputLine);
            
            //Increment Iteration Count
            iterationNo = 1;
            
            //Run Iterations number of times
            if(iterations > 0)
            {
                while (iterationNo < iterations+1)
                {
                    //Authoritative Step - Update Authority Array values
                    int k;
                    double sum=0;
                    for(k=0; k<noNodes; k++)
                    {
                        sum=0;
                        for(int j=0; j<noNodes; j++)
                        {
                            sum += transMat[k][j] * hubMat[j];
                        }
                        authMat[k]=sum;
                    }
                            
                    //Hub Step - Update Hub Array Values
                    k=0;
                    sum=0;
                    for(k=0; k<noNodes; k++)
                    {
                        sum=0;
                        for(int j=0; j<noNodes; j++)
                        {
                            sum += adjMat[k][j] * authMat[j];
                        }
                        hubMat[k]=sum;
                    }
            
                    //Scaling Step for Authority Values
                    for(int i=0; i < noNodes; i++)
                        sum += Math.pow(authMat[i], 2);
                    double authSqrt = 0.0;
                    authSqrt = Math.sqrt(sum);
                    if(authSqrt==0)
                        authSqrt = 1;
                    double[] newauthMat = new double[noNodes];
                    for(int i=0; i < noNodes; i++)
                        newauthMat[i] = (double) (authMat[i]/authSqrt);
            
                    //Scaling Step for Hub Values
                    for(int i=0; i < noNodes; i++)
                        sum += Math.pow(hubMat[i], 2);
                    double hubSqrt = 0.0;
                    hubSqrt = Math.sqrt(sum);
                    if(hubSqrt==0)
                        hubSqrt=1;
                    double[] newhubMat = new double[noNodes];
                    for(int i=0; i < noNodes; i++)
                        newhubMat[i] = (double) (hubMat[i]/hubSqrt);
    
                    //Display Authority and Hub Values for Iterations
                    outputLine = "Iter  : " + iterationNo + "  :";
    
                    for (int i = 0; i < noNodes; i++)
                    {
                        outputLine += " A/H["+i+"] = "
                        + String.format("%.7f", newauthMat[i])
                        + "/"
                        + String.format("%.7f", newhubMat[i]);
                    }
                    System.out.println(outputLine);
                    iterationNo++;
                }
            
            }
            
            else
            {
                
                while(!checkConvergence)
                {
                    //Authoritative Step - Update Authority Array values
                    int k;
                    double sum = 0;
                    for(k = 0; k < noNodes; k++)
                    {
                        sum = 0;
                        for(int j = 0; j < noNodes; j++)
                        {
                            sum += transMat[k][j] * hubMat[j];
                        }
                        newauthorityMat[k] = sum;
                    }
                    
                    //Hub Step - Update Hub Array Values
                    k = 0;
                    sum = 0;
                    for(k = 0; k < noNodes; k++)
                    {
                        sum=0;
                        for(int j = 0; j < noNodes; j++)
                        {
                            sum += adjMat[k][j] * newauthorityMat[j];
                        }
                        newhubbMat[k] = sum;
                    }
                    
                    //Scaling Step for Authority Values
                    for(int i = 0; i < noNodes; i++)
                        sum += Math.pow(newauthorityMat[i], 2);
                    double authSqrt = 0.0;
                    authSqrt = Math.sqrt(sum);
                    if(authSqrt==0)
                        authSqrt = 1;
                    double[] newauthMat = new double[noNodes];
                    for(int i = 0; i < noNodes; i++)
                        newauthMat[i] = (double) (newauthorityMat[i]/authSqrt);
                    
                    //Scaling Step for Hub Values
                    for(int i=0; i < noNodes; i++)
                        sum += Math.pow(newhubbMat[i], 2);
                    double hubSqrt = 0.0;
                    hubSqrt = Math.sqrt(sum);
                    if(hubSqrt==0)
                        hubSqrt = 1;
                    double[] newhubMat = new double[noNodes];
                    for(int i=0; i < noNodes; i++)
                        newhubMat[i] = (double) (newhubbMat[i]/hubSqrt);
                    
                    
                    
                    //Display Authority and Hub values for remaining Iterations
                    outputLine = "Iter  : " + iterationNo + "  :";
                    for (int i = 0; i < noNodes; i++)
                    {
                        outputLine += " A/H["+i+"] = "
                        + String.format("%.7f", newauthMat[i])
                        + "/"
                        + String.format("%.7f", newhubMat[i]);
                    }
                    System.out.println(outputLine);
                    
                    //Get value from Convergence method to continue the loop
                    hits_1098 hits = new hits_1098();
                    boolean checkauthConvergence = hits.checkConvergence(newauthMat, authMat, errorRate, noNodes);
                    boolean checkhubConvergence = hits.checkConvergence(newhubMat, hubMat, errorRate, noNodes);
                    
                    if(checkauthConvergence && checkhubConvergence)
                        break;
                    else
                    {
                        //Assign the current values to earlier arrays
                        for(int i = 0; i< noNodes; i++)
                        {
                            authMat[i] = newauthMat[i];
                            hubMat[i] = newhubMat[i];
                        }
                    }
                    
                    
                    
                    iterationNo++;
                }
            }
        }
        
        catch(FileNotFoundException e)
        {
            error +="File Read Error. Please check if the file is in same folder.";
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
