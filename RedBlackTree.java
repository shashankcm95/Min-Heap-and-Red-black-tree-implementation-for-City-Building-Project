//Implementation of RedBlackTree and its functions.
public class RedBlackTree extends Building
{
	private Building root;
	private Building NIL;
	//private method that recursively finds p building in the RB-tree.
	private Building searchTree(Building building, int buildingNumToBeSearched) 
	{
		if (building == NIL || buildingNumToBeSearched == building.BuildingNum) 
			return building;
		if (buildingNumToBeSearched < building.BuildingNum)
			return searchTree(building.left, buildingNumToBeSearched); 
		return searchTree(building.right, buildingNumToBeSearched);
	}
	//Restore RedBlack tree after deletion. 
	private void restoreUponDelete(Building p) {
		Building s;
		while (p != root && p.color == 0) {
			if (p == p.parent.left) {
				s = p.parent.right;
				if (s.color == 1) {
					// case 3.1
					s.color = 0;
					p.parent.color = 1;
					rotateLeft(p.parent);
					s = p.parent.right;
				}
				if(s.left!=null||s.right==null) break;
				if (s.left.color == 0 && s.right.color == 0) {
					// case 3.2
					s.color = 1;
					p = p.parent;
				} else {
					if (s.right.color == 0) {
						// case 3.3
						s.left.color = 0;
						s.color = 1;
						rotateRight(s);
						s = p.parent.right;
					} 

					// case 3.4
					s.color = p.parent.color;
					p.parent.color = 0;
					s.right.color = 0;
					rotateLeft(p.parent);
					p = root;
				}
			} else {
				s = p.parent.left;
				if (s.color == 1) {
					// case 3.1
					s.color = 0;
					p.parent.color = 1;
					rotateRight(p.parent);
					s = p.parent.left;
				}
				if(s.right==null||s.left==null) break;
				if (s.right.color == 0 && s.right.color == 0) {
					// case 3.2
					s.color = 1;
					p = p.parent;
				} else {
					if (s.left.color == 0) {
						// case 3.3
						s.right.color = 0;
						s.color = 1;
						rotateLeft(s);
						s = p.parent.left;
					} 

					// case 3.4
					s.color = p.parent.color;
					p.parent.color = 0;
					s.left.color = 0;
					rotateRight(p.parent);
					p = root;
				}
			} 
		}
		p.color = 0;
	}

	//swaps 2 building objects
	private void swapNodes(Building x, Building y){
		if (x.parent == null) {
			root = y;
		} else if (x == x.parent.left){
			x.parent.left = y;
		} else {
			x.parent.right = y;
		}
		y.parent = x.parent;
	}
	//deletes a building from the RB-Tree.
	private void delete(Building building, int buildingNumToBeSearched) {
		// find the building containing buildingNumToBeSearched
		Building r = NIL;
		Building p, q;
		while (building != NIL){
			if (building.BuildingNum == buildingNumToBeSearched) {
				r = building;
			}

			if (building.BuildingNum <= buildingNumToBeSearched) {
				building = building.right;
			} else {
				building = building.left;
			}
		}

		if (r == NIL)	
			return;
		q = r;
		int yOriginalColor = q.color;
		if (r.left == NIL) {
			p = r.right;
			swapNodes(r, r.right);
		} else if (r.right == NIL) {
			p = r.left;
			swapNodes(r, r.left);
		} else {
			q = minimum(r.right);
			yOriginalColor = q.color;
			p = q.right;
			if (q.parent == r) {
				p.parent = q;
			} else {
				swapNodes(q, q.right);
				q.right = r.right;
				q.right.parent = q;
			}

			swapNodes(r, q);
			q.left = r.left;
			q.left.parent = q;
			q.color = r.color;
		}
		if (yOriginalColor == 0){
			restoreUponDelete(p);
		}
	}
	
	//Restore RedBlack tree after insertion. 
	private void RestoreUponInsert(Building k){
		Building x;
		while (k.parent.color == 1) {
			if (k.parent == k.parent.parent.right) {
				x = k.parent.parent.left; // uncle
				if (x.color == 1) {
					// case 3.1
					x.color = 0;
					k.parent.color = 0;
					k.parent.parent.color = 1;
					k = k.parent.parent;
				} else {
					if (k == k.parent.left) {
						// case 3.2.2
						k = k.parent;
						rotateRight(k);
					}
					// case 3.2.1
					k.parent.color = 0;
					k.parent.parent.color = 1;
					rotateLeft(k.parent.parent);
				}
			} else {
				x = k.parent.parent.right; // uncle

				if (x.color == 1) {
					// mirror case 3.1
					x.color = 0;
					k.parent.color = 0;
					k.parent.parent.color = 1;
					k = k.parent.parent;	
				} else {
					if (k == k.parent.right) {
						// mirror case 3.2.2
						k = k.parent;
						rotateLeft(k);
					}
					// mirror case 3.2.1
					k.parent.color = 0;
					k.parent.parent.color = 1;
					rotateRight(k.parent.parent);
				}
			}
			if (k == root) {
				break;
			}
		}
		root.color = 0;
	}
	//constructor
	public RedBlackTree() {
		NIL = new Building();
		NIL.color = 0;
		NIL.left = null;
		NIL.right = null;
		root = NIL;
	}
	// search for the buildingNumToBeSearched in the RB-Tree
	// and return the buildingNode
	public Building search(int buildingNumber) 
	{
		return searchTree(this.root, buildingNumber);
	}
	// find the building with the minimum buildingNum
	public Building minimum(Building building) {
		while (building.left != NIL) {
			building = building.left;
		}
		return building;
	}

	//find the building with the maximum buildingNum
	public Building maximum(Building building) {
		while (building.right != NIL) {
			building = building.right;
		}
		return building;
	}

	// find the successor of the given building
	public Building successor(Building p) {
		// if the right subtree is not null,
		// the successor is the leftmost building in the
		// right subtree
		if (p.right != NIL) {
			return minimum(p.right);
		}

		// else it is the lowest ancestor of p whose
		// left child is also an ancestor of p.
		Building q = p.parent;
		while (q != NIL && p == q.right) {
			p = q;
			q = q.parent;
		}
		return q;
	}

	// find the predecessor of the given building
	public Building predecessor(Building p) {
		// if the left subtree is not null,
		// the predecessor is the rightmost building in the 
		// left subtree
		if (p.left != NIL) {
			return maximum(p.left);
		}

		Building q = p.parent;
		while (q != NIL && p == q.left) {
			p = q;
			q = q.parent;
		}

		return q;
	}

	//rotate building p to the left
	public void rotateLeft(Building p) {
		Building q = p.right;
		p.right = q.left;
		if (q.left != NIL) {
			q.left.parent = p;
		}
		q.parent = p.parent;
		if (p.parent == null) {
			this.root = q;
		} else if (p == p.parent.left) {
			p.parent.left = q;
		} else {
			p.parent.right = q;
		}
		q.left = p;
		p.parent = q;
	}

	// rotate building p to the right
	public void rotateRight(Building p) {
		Building q = p.left;
		p.left = q.right;
		if (q.right != NIL) {
			q.right.parent = p;
		}
		q.parent = p.parent;
		if (p.parent == null) {
			this.root = q;
		} else if (p == p.parent.right) {
			p.parent.right = q;
		} else {
			p.parent.left = q;
		}
		q.right = p;
		p.parent = q;
	}

	//Insert the building to the tree in its appropriate position according to it's building number
	// and restore RB-properties to the tree.
	public void insert(Building building) {
		// Ordinary Binary Search Insertion
		building.parent = null;
		building.left = NIL;
		building.right = NIL;
		building.color = 1; //new building is initially inserted as red.

		Building q = null;
		Building p = this.root;

		while (p != NIL) {
			q = p;
			if (building.BuildingNum < p.BuildingNum) {
				p = p.left;
			} else {
				p = p.right;
			}
		}

		// q is parent of p
		building.parent = q;
		if (q == null) {
			root = building;
		} else if (building.BuildingNum < q.BuildingNum) {
			q.left = building;
		} else {
			q.right = building;
		}

		// if new building is root, simply return
		if (building.parent == null){
			building.color = 0;
			return;
		}

		// if the grandparent is null, simply return
		if (building.parent.parent == null) {
			return;
		}

		// Fix the tree
		RestoreUponInsert(building);
	}

	// delete the building from the tree
	public void deleteBuilding(int data) {
		delete(this.root, data);
	}
	//store printable string
	StringBuilder str;
	//public method that prints buildings in range
	public void printBuildingsInRange(int minBuilding, int maxBuilding)
	{
		str=new StringBuilder();
		buildingsInRange(this.root,str, minBuilding, maxBuilding);
		if(str.length()>0)
		System.out.print(str.substring(0, str.length()-1));
	}
	//private method that gets all building values in a given range recursively. 
	private void buildingsInRange(Building root, StringBuilder str, int min, int max) {
		if (root == null || root.BuildingNum == 0) {
			return;
		}
		if (min < root.BuildingNum) {
			buildingsInRange(root.left,str, min, max);
		}
		if (min <= root.BuildingNum && max >= root.BuildingNum) {
			str=str.append("(" + root.BuildingNum + "," + root.executed_time + "," + root.total_time + "),");
		}
		if (max > root.BuildingNum) {
			buildingsInRange(root.right,str,min, max);
		}
	}
}

