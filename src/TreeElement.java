import java.util.ArrayList;

public class TreeElement {

	int sickDataIndex, splitPoint;
	
	boolean[] canBeUsedDataIndexes;
	
	ArrayList<Sick> list;
	
	TreeElement smallElement;
	TreeElement bigElement;
	
	public boolean harmfull;
	
	public TreeElement(ArrayList<Sick> incomingList, boolean[] inheritedDataIndex) {
		this.list = incomingList;
		canBeUsedDataIndexes = new boolean[10];
		for(int i = 0;i<10;i++) {
			canBeUsedDataIndexes[i] = inheritedDataIndex[i];
		}
		canBeUsedDataIndexes[0] = false;
		
		sickDataIndex = -1;
		splitPoint = -1;
		
		harmfull = false;
	}
	
}
