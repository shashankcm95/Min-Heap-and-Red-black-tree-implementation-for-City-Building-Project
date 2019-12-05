public class Building
{
	//Static variables used as Global variables.
	static int globalCounter=1;
	static int localCounter=1;
	//Building properties.
	protected int BuildingNum;
    protected int executed_time;
    protected int total_time;
    //Red-Black Tree node pointers to its parent and children. 
    protected Building left;
    protected Building right;
    protected Building parent;
    //color property of Red-Black Node
    protected int color;
}
