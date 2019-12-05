public class MinHeap extends Building
{
	//Array used to store the buildings in MinHeap.
    private Building[] heap;
    //Number of current buildings in heap.
    private int size;
    //Constructor to take maximum size of the array and create an instance of that many buildings.
    MinHeap(int Size)
    {
        heap=new Building[Size];
        //current size of heap, initially 0.
        size=0;
        for(int i=0;i<Size;i++){
            heap[i]=new Building();
        }
    }
    public void Heapify(int root)
    {
    	heapify(root);
    }
    private void heapify(int node)
    {
    	//only consider non leaf nodes. 
        while(node<size/2)
        {
        	//get children.
            int leftChild=2*node+1;
            int rightChild=leftChild+1;
            //right child exists and is less or equal to the left child.
            if(rightChild<size&&heap[rightChild].executed_time<=heap[leftChild].executed_time)
            {
            	//if parent has lesser executed time than right child,order is restored.
            	if(heap[rightChild].executed_time>heap[node].executed_time) break;
            	//if rightChild is smaller than left child, compare with parent node,  
            	if(heap[rightChild].executed_time<heap[leftChild].executed_time)
            	{
            		//if executed time of rightChild is lesser than parent, swap them.
            		if(heap[rightChild].executed_time<heap[node].executed_time)
            		{
                		swap(rightChild,node);
                    	node=rightChild;	
            		}
            		//if executed time of parent and right child are equal, compare their building numbers.
            		if(heap[rightChild].executed_time==heap[node].executed_time) 
                	{
            			//if parent has smaller building than right child,order is restored.
                    	if(heap[rightChild].BuildingNum>heap[node].BuildingNum) break;
                    	//if parent has greater building than right child,swap them.
                    	if(heap[rightChild].BuildingNum<heap[node].BuildingNum)
                		{
                			swap(rightChild,node);
                			node=rightChild;
                		}
                	}
            	}
            	//if rightChild and leftChild have same executed time,
            	if(heap[rightChild].executed_time==heap[leftChild].executed_time) 
            	{	
            		//get the child with smaller buildingNum
            		int tmp=(heap[rightChild].BuildingNum<heap[leftChild].BuildingNum)?rightChild:leftChild; 
            		//Since left and right children have same executed time at this point, we need only check with one of them.
                	//if parent has lesser executed time than right child,order is restored.
            		if(heap[rightChild].executed_time>heap[node].executed_time) break;
                	//if executed time of children is lesser than their parent, swap parent with child with smaller buildingNum.
            		if(heap[tmp].executed_time<heap[node].executed_time) 
            		{
            			swap(tmp,node);
                		node=tmp;
            		}
            		//if executed time of children is equal to their parent,
            		if(heap[rightChild].executed_time==heap[node].executed_time) 
                	{
            			//if it's building number is larger than the smallest child,
            			//swap parent with child with smaller buildingNum with parent.
            			if(heap[tmp].BuildingNum<heap[node].BuildingNum)
            			{
            				swap(tmp,node);
                			node=tmp;
            			}
            			//if parent's building is already smaller than the smallest child, order is restored.
                		if(heap[tmp].BuildingNum>heap[node].BuildingNum) break;                			
                	}
              	}
            }
            else
            {
            	//Either if rightChild doesn't exist or leftChild is smaller than rightChild, 
            	//if parent is smaller than leftChild, order is restored.
            	 if(heap[leftChild].executed_time>heap[node].executed_time) break;
            	 //if leftChild has lesser executed time than parent, swap them.
            	 if(heap[leftChild].executed_time<heap[node].executed_time) 
            	 {
            		 swap(leftChild,node);
            		 node=leftChild;
            	 }
            	 //if leftChild and parent have same executed time,
            	 if(heap[leftChild].executed_time==heap[node].executed_time) 
            	 {
            		 //check if parent has smaller building number, if so, order is restored.
            		 if(heap[leftChild].BuildingNum>heap[node].BuildingNum) break;
                	 //else, if parent has larger building number than its child, swap them.
            		 if(heap[leftChild].BuildingNum<heap[node].BuildingNum)
            		 {
            			swap(leftChild,node);
             			node=leftChild;
            		 }
           		}
            }
        }
    }
    public Building Insert(int buildingNumber,int totalTime)
    {
    	return insert(buildingNumber,totalTime);
    }
    //inserting a new element into the min-heap
    private Building insert(int buildingNumber,int totalTime)
    {
    	//initialize values.
    	heap[size]=new Building();
    	heap[size].BuildingNum=buildingNumber;
        heap[size].total_time=totalTime;
        heap[size].executed_time=0;
        int node=size;
        //Heapify bottom-up
        while(node>0)
        {
            int parent=node/2;
            if(heap[parent].executed_time<heap[node].executed_time) break;
            //if parent and child have same executed_time, compare them building numbers and swap if necessary.
            else if(heap[parent].executed_time==heap[node].executed_time)
            { 
            	if(heap[parent].BuildingNum<heap[node].BuildingNum) break;
            		swap(node,parent);
            		node=parent;
            		break;
            }
            //if child is smaller, swap.
            else
            {
            swap(node,parent);
            node=parent;
            }
        }
        size++;
        return heap[node];
    }
    //swap function to swap two building nodes.
    private void swap(int i,int j)
    {
        Building tmp=heap[i];
    	heap[i]=heap[j];
    	heap[j]=tmp;
    }
    private void removeMin()
    {
    	//copy last element to first and reduce size of heap, and make last value null.
    	heap[0]=heap[size-1];
    	heap[size-1]=null;
    	size--;
    	//call Heapify to restore the Heap.
        Heapify(0);
   }
    public Building getMin()
    {
    	//returns the root element in the minHeap.
        return heap[0];
    }
    
    public int UpdateExecTime(RedBlackTree rbt)
    {
    	return updateExecTime(rbt);
    }
    private int updateExecTime(RedBlackTree rbt) 
    {
    	//get min element from heap
    	Building tmp=getMin();
    	if(tmp == null)
    		return -1;
        int cur_ex=tmp.executed_time;
        cur_ex++;
        //update executed time by 1.
        heap[0].executed_time=cur_ex;
        //if a building has been completed, return the building number, else return some negative value
        if(cur_ex>=tmp.total_time) return heap[0].BuildingNum;
        return -1;
    }
    public void removeBuilding(RedBlackTree rbtNode,int removeElement)
    {
    	remove(rbtNode,removeElement);
    }
    //removes building from both heap and RedBlack Tree and resets the localCounter.
    private void remove(RedBlackTree rbt,int remove)
    {
    	System.out.println("("+remove+","+Building.globalCounter+")");
        removeMin();
        rbt.deleteBuilding(remove);
        Building.localCounter=0;
    }
    
    public void final_update(RedBlackTree Node)
    {
    	FinalUpdate(Node);
    }
    //Once the data is read from file, we continue processing till we complete all buildings.
    private void FinalUpdate(RedBlackTree node)
    {
    	while(size>0)
        {
    		Building tmp=getMin();
    		int cur_ex=tmp.executed_time;
            int ctr=0;
            //ctr is another local counter to keep count of number of days worked on a building.
            while(ctr<5)
            {
            	cur_ex++;
            	ctr++;
            	Building.globalCounter++;
            	heap[0].executed_time=cur_ex;
            	//if a building has been completed, delete the building by calling deleteBuilding()
                if(cur_ex>=tmp.total_time) {
            		int remove=heap[0].BuildingNum;
                	removeBuilding(node, remove);
                	//building removed, now we can reset counter and start working on next building
            		break;
                }
            }
            Heapify(0);
        }
    	//System.out.println("City Built in "+Building.globalCounter+" days.");
    }
}
