//package ADS_Project;
import java.io.BufferedReader;
import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class risingCity {
	public static void main(String []args) throws IOException
	{
		//String fileName=args[0];
		File inputFile = new File("C:\\Users\\shash\\eclipse-workspace\\ADS\\inputFile.txt");
	    if (inputFile.exists())
	    {
	    	//Reading input from the file.
	    	BufferedReader br = new BufferedReader(new FileReader(inputFile));
	    	String firstLine;
	    	firstLine = br.readLine();
	    	//Directing the system print stream to the output file. If exists, overwrites and if not, creates a new file.
	    	PrintStream o = new PrintStream(new File("output_file.txt"));
	    	System.setOut(o); 
	    	//start reading input.
	    	if (firstLine == null) 
	    	{
	    		System.out.println("The input file is empty");
	    		br.close();
	    		return;
	    	}
	    	else 
	    	{
	    		//MinHeap initialization with maximum size as parameter to the constructor.
	    		MinHeap Heap=new MinHeap(2000);
	    		//Red-BlackTree initialization.
	    		RedBlackTree rbt=new RedBlackTree();
	    		//If first operation is print, since no values are inserted yet, simply print default value.
	    		if(firstLine.contains("Print"))
	    		{
	    			System.out.println("(0,0,0)");
	    			Building.globalCounter++;
    			}
	    		//Reading the firstLine and splitting the string to retrieve parameters.
	    		String[] firstLineParam = firstLine.split(":", 2);
	 			firstLineParam[1] = firstLineParam[1].substring(8, firstLineParam[1].length() - 1);
	 			String[] firstBuilding = firstLineParam[1].split(",", 2);
	 			//inserting first building into MinHeap.
	 			Building newBuilding=Heap.Insert(Integer.parseInt(firstBuilding[0]),Integer.parseInt(firstBuilding[1]));
	 			//inserting first building into Red-Black Tree.
	 			rbt.insert(newBuilding);
	 			//bufferVariable keeps track of the number of elements in the buffer.
	 			int bufferVariable=0;
	 			//buffer to store values, if we are already working on some building and new building is inserted.
	 			int[][] buffer=new int[5][2];
	 			String nextLine;
	 			//read next line until we reach the end of file. 
		    	while ((nextLine = br.readLine()) != null)
	 			{
		    		//splitting input line to retrieve parameters.
	 				String[] splitString = nextLine.split(":", 2);
					int nextOperationDay = Integer.parseInt(splitString[0]);
					String nextOperation = splitString[1];
					//We work on existing buildings until we get another operation.
					while (Building.globalCounter <= nextOperationDay) 
					{
						//Save new inserts in the buffer
						if (Building.globalCounter == nextOperationDay && nextOperation.contains("Insert"))
						{
							nextOperation =nextOperation.substring(8, splitString[1].length() - 1);
							String[] params = nextOperation.split(",", 2);
							//store in buffer
							buffer[bufferVariable][0] = Integer.parseInt(params[0]);
							buffer[bufferVariable][1] = Integer.parseInt(params[1]);
							bufferVariable++;
						}
						//increment executed time
						int BuildingDelete=Heap.UpdateExecTime(rbt);
						//localCounter keeps track of the number of days a building is being worked on.
						if(Building.localCounter==5&&BuildingDelete<0)
						{
							//reset localCounter since we have worked on the building with minimum executed_time for 5 days.
							Building.localCounter=0;
							//Heapify with updated executed times.
							Heap.Heapify(0);
							//insert all buffer elements into the heap and Red-Black Tree. 
							for(int i=0;i<bufferVariable;i++)
							{
								newBuilding=Heap.Insert(buffer[i][0],buffer[i][1]);
								rbt.insert(newBuilding);
							}	
							//empty buffer.
							bufferVariable=0;
						}
						//if operation is PrintBuilding, call printBuildingsInRange function of Red-black tree.
						if (Building.globalCounter == nextOperationDay && nextOperation.contains("PrintBuilding"))
						{ 							
							//splitting input line to retrieve parameters.
							nextOperation = nextOperation.substring(15, splitString[1].length() - 1);
							String[] params = nextOperation.split(",", 2);
							if(params.length==2)
							{
							int minimumBuilding = Integer.parseInt(params[0]);
							int maximumBuilding = Integer.parseInt(params[1]);
							rbt.printBuildingsInRange(minimumBuilding,maximumBuilding);
							System.out.println();
							}
						}
						//If not PrintBuilding, check if it is Print.
						else if (Building.globalCounter == nextOperationDay && nextOperation.contains("Print"))
						{
							//splitting input line to retrieve parameters.
							nextOperation = nextOperation.substring(7, splitString[1].length() - 1);
							int buildingToSearch = Integer.parseInt(nextOperation);
							boolean foundInBuffer=false;
							//check if the current building is still in the buffer.
							//If so, print with executed_time 0 since it is not yet inserted into the heap.
							for(int i=0;i<bufferVariable;i++)
							{
								if(buffer[i][0]==buildingToSearch)
								{
									System.out.println("(" + buffer[i][0] + "," + 0 + ","+ buffer[i][1] + ")");
									foundInBuffer=true;
								}
								}
								//If not found in buffer, check in red-Black Tree.
								if(!foundInBuffer)
								{
									Building found = rbt.search(buildingToSearch);
									if(found != null) 
										System.out.println("(" + found.BuildingNum + "," + found.executed_time + ","+ found.total_time + ")");
									else
										System.out.println("(0,0,0)");
								}
						 }
						 if(BuildingDelete>0)
						 {
							 Heap.removeBuilding(rbt,BuildingDelete);
						 }
						 //increment local and global counter - Marks the end of a day.
						 Building.localCounter++;
						 Building.globalCounter++;
						}
	 				 }
	 				 Building.localCounter--;
	 				 //Once the input file is completely read, we complete working on the current building 
	 				 //for any remaining days if localCounter is less than 5
	 				 while(Building.localCounter>0&&Building.localCounter<5)
	 				 {
	 					 int buildingDelete=Heap.UpdateExecTime(rbt);
	 					 if(buildingDelete>0)
	 					 {
	 						Heap.removeBuilding(rbt,buildingDelete);
	 					 }
	 		 			 Building.localCounter++;
	 	 				 Building.globalCounter++;	 					
	   				 }
	 				 Building.globalCounter--;
	 				 //Heapify and insert buffer elements, if any, into the heap and Red-Black Tree.
		 			 Heap.Heapify(0);
	 				 for(int i=0;i<bufferVariable;i++)
	 				 {
	 			 		newBuilding=Heap.Insert(buffer[i][0],buffer[i][1]);
						rbt.insert(newBuilding);						
	 			   	 }
	 				 //All the buildings are now in the heap and RedBlackTree, 
	 				 //we continue working on building in heap until all of them are completed.
	 				 Heap.final_update(rbt);	
	    		 }
	    		 //close buffer reader.
	    		 br.close();
	   		 }
	    	 else
	    	 {
	    		 System.out.println("Input file does not exist in the path specified.");
	    	 }
	}
}
