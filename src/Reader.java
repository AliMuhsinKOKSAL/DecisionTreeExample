import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;

public class Reader {

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("breast-cancer-wisconsin.data"));
		ArrayList<Sick> list = new ArrayList<Sick>();

		String line;
		while ((line = in.readLine()) != null) {
			String lines[] = line.split(",");
			int[] datas = new int[11];
			for (int i = 0; i < lines.length; i++) {
				datas[i] = (lines[i].equals("?")) ? 0 : Integer.parseInt(lines[i]);
			}
			list.add(new Sick(datas));
		}
		in.close();

		int numberOfHarmfulls = 0;
		int numberOfHarmlesses = 0;
		for (Sick s : list) {
			if (s.isHarmful == true) {
				numberOfHarmfulls++;
			} else {
				numberOfHarmlesses++;
			}
		}
//		System.out.println(numberOfHarmfulls + " " + numberOfHarmlesses);

		ArrayList<Sick> training = new ArrayList<Sick>();
		ArrayList<Sick> test = new ArrayList<Sick>();

		Collections.sort(list, new Comparator<Sick>() {

			@Override
			public int compare(Sick s1, Sick s2) {
				if (s1.isHarmful && !s2.isHarmful) {
					return -1;
				} else if (!s1.isHarmful && s2.isHarmful) {
					return 1;
				} else {
					return 0;
				}
			}
		});
//		for (Sick s : list) {
//			System.out.println(s.toString());
//		}
		
		int listIndex = 0;
		for(int i = 0;i<numberOfHarmlesses/2;i++) {
			training.add(list.get(listIndex));
			listIndex++;
			test.add(list.get(listIndex));
			listIndex++;
		}
		
		for(int i = 0;i<numberOfHarmfulls/2;i++) {
			training.add(list.get(listIndex));
			listIndex++;
			test.add(list.get(listIndex));
			listIndex++;
		}
		
//		System.out.println("Training Set:");
//		for(Sick s:training) {
//			System.out.println(s.toString());
//		}
//		
//		System.out.println("----------------------------------");
//		System.out.println("Test Set:");
//		for(Sick s:test) {
//			System.out.println(s.toString());
//		}
		
		DecisionTree dTree = new DecisionTree(training);
		dTree.mainProcess();		
		JFrame f = new JFrame("Tree");
		f.add(dTree);
		f.setSize(1600,900);
		f.setVisible(true);
		
		
		System.out.println("test results: ");
		float rightGuess = 0f;
		float wrongGuess = 0f;
		for(Sick s: list) {
			boolean thisSicksGuessResult = dTree.testSick(s);
			if(thisSicksGuessResult) {
				System.out.println("Tahmin: Zararlı  |"+s);
				if(s.isHarmful == thisSicksGuessResult) {
					rightGuess++;
				}else {
					wrongGuess++;
				}
			}else {
				System.out.println("Tahmin: Zararsız |"+s);
				if(s.isHarmful == thisSicksGuessResult) {
					rightGuess++;
				}else {
					wrongGuess++;
				}
			}
		}
		
		System.out.println("doğru sayısı: "+rightGuess + " yanlış sayısı:"+wrongGuess);
		
		System.out.println("doğruluk oranı: "+rightGuess/(rightGuess+wrongGuess)*100);
	}

}
