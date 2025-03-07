
public class Sick {

	public int[] sicksDatas;
	int dataSize = 10;
	
	boolean isHarmful;
	
	public Sick(int[] datas) {
		sicksDatas = new int[dataSize];
		for(int i = 0;i<dataSize;i++) {
			sicksDatas[i] = datas[i];
		}
		
		if(datas[dataSize] == 2) {
			isHarmful = false;
		}else {
			isHarmful = true;
		}
	}
	
	public String toString() {
		String result = "";
		
		for(int data: sicksDatas) {
			result += data + " ";
		}
		
		result += (isHarmful)?"zararlı":"zararsız";
		
		return result;
	}
	
}
