import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DecisionTree extends JPanel {

	TreeElement rootElement;

	int numberOfHarmfull = 0;
	int numberOfHarmless = 0;

	public DecisionTree(ArrayList<Sick> incomingList) {
		boolean[] canBeUsedDataIndexes = new boolean[10];
		for (int i = 0; i < 10; i++) {
			canBeUsedDataIndexes[i] = true;
		}
		canBeUsedDataIndexes[0] = false;

		rootElement = new TreeElement(incomingList, canBeUsedDataIndexes);
	}

	public void setElementResults(TreeElement e) {
		// for only set leaf elements
		if (e.smallElement == null && e.bigElement == null) {
			// this is leaf element
			// we are set results

			int harmfulls = 0;
			int harmlesses = 0;
			for (Sick s : e.list) {
				if (s.isHarmful) {
					harmfulls++;
				} else {
					harmlesses++;
				}
			}

			if (harmfulls >= harmlesses) {
				e.harmfull = true;
			} else {
				e.harmfull = false;
			}

		} else {
			// this element have child
			// set same function for child
			setElementResults(e.smallElement);
			setElementResults(e.bigElement);
		}
	}

	public void mainProcess() {
		process(rootElement);
		setElementResults(rootElement);
	}
	
	public boolean testSick(Sick s) {
		return test(s, rootElement);
	}
	
	private boolean test(Sick s,TreeElement e) {
		if(e.smallElement == null&&e.bigElement == null) {
			return e.harmfull;
		}else {
			if(s.sicksDatas[e.sickDataIndex]<=e.splitPoint) {
				return test(s,e.smallElement);
			}else {
				return test(s,e.bigElement);
			}
		}
	}

	public void process(TreeElement currentElement) {
		int bestDataIndex = -1;
		float bestEntropy = 5;
		int bestSplitPoint = -1;

		for (int i = 0; i < 10; i++) {
			if (currentElement.canBeUsedDataIndexes[i]) {
				float[] splitPndEnt = findBestSplitPoint(currentElement.list, i);
				if (splitPndEnt[1] <= bestEntropy) {
					bestDataIndex = i;
					bestEntropy = splitPndEnt[1];
					bestSplitPoint = (int) splitPndEnt[0];
				}
			}
		}

		if (bestDataIndex != -1) {
//			System.out.println("iki liste oluştu! ayrım noktası: "+bestSplitPoint+"\nen iyi veri indeksi: "+bestDataIndex+" en iyi entropy: "+bestEntropy);

			currentElement.canBeUsedDataIndexes[bestDataIndex] = false;
			currentElement.splitPoint = bestSplitPoint;
			currentElement.sickDataIndex = bestDataIndex;

			currentElement.smallElement = new TreeElement(new ArrayList<Sick>(), currentElement.canBeUsedDataIndexes);
			currentElement.bigElement = new TreeElement(new ArrayList<Sick>(), currentElement.canBeUsedDataIndexes);
			splitList(currentElement.list, bestDataIndex, bestSplitPoint, currentElement.smallElement.list,
					currentElement.bigElement.list);

			process(currentElement.smallElement);
			process(currentElement.bigElement);
		}
	}

	public float[] findBestSplitPoint(ArrayList<Sick> incomingList, int dataIndex) {
		float[] splitPndEnt = new float[2];

		int bestSplitPoint = 1;

		int iNumbers[] = splitListImaginary(incomingList, dataIndex, bestSplitPoint);

		// calculate entropy
		float bestEntropy = calculateEntropy(iNumbers);

		float tempEntropy;
		for (int i = 2; i <= 10; i++) {
			iNumbers = splitListImaginary(incomingList, dataIndex, i);
			tempEntropy = calculateEntropy(iNumbers);
			if (tempEntropy < bestEntropy) {
				bestEntropy = tempEntropy;
				bestSplitPoint = i;
			}
		}

		splitPndEnt[0] = bestSplitPoint;
		splitPndEnt[1] = bestEntropy;

		return splitPndEnt;
	}

	public float calculateEntropy(int[] data) {

		float entropy;

		float numberOfHarmfullSmall = data[0];
		float numberOfHarmlessSmall = data[1];
		float numberOfHarmfullBig = data[2];
		float numberOfHarmlessBig = data[3];

		float smallsTotal = numberOfHarmfullSmall + numberOfHarmlessSmall;
		float bigsTotal = numberOfHarmfullBig + numberOfHarmlessBig;

		float smallsHarmlessRatio = numberOfHarmlessSmall / smallsTotal;
		float smallsHarmfullRatio = 1f - smallsHarmlessRatio;

		float bigsHarmlessRatio = numberOfHarmlessBig / bigsTotal;
		float bigsHarmfullRatio = 1f - bigsHarmlessRatio;

		boolean smallsHarmless = smallsHarmlessRatio >= bigsHarmlessRatio;

		if (smallsHarmless) {
			// we said, smalls group is harmless, bigs group harmfull
			float pa = smallsHarmlessRatio;
			float pb = bigsHarmfullRatio;

			entropy = -pa * log2(pa) - pb * log2(pb);
		} else {
			// smalls group is harmfull, bigs group harmless
			float pa = smallsHarmfullRatio;
			float pb = bigsHarmlessRatio;

			entropy = -pa * log2(pa) - pb * log2(pb);
		}

		return entropy;
	}

	private float log2(float x) {
		float x1 = (float) ((x == 0f) ? 0f : Math.log((double) x) / Math.log((double) 2));
		return x1;
	}

	public void splitList(ArrayList<Sick> incomingList, int dataIndex, int splitPoint, ArrayList<Sick> smallsReturn,
			ArrayList<Sick> bigsReturn) {
		if (dataIndex >= 10 || dataIndex < 0) {
			System.err.println("data index is false!");
			return;
		}

		for (Sick s : incomingList) {
			if (s.sicksDatas[dataIndex] <= splitPoint) {
				smallsReturn.add(s);
			} else {
				bigsReturn.add(s);
			}
		}
	}

	public int[] splitListImaginary(ArrayList<Sick> incomingList, int dataIndex, int splitPoint) {
		if (dataIndex >= 10 || dataIndex < 0) {
			System.err.println("data index is false!");
			return null;
		}

		int numberOfHarmfullSmall = 0;
		int numberOfHarmlessSmall = 0;
		int numberOfHarmfullBig = 0;
		int numberOfHarmlessBig = 0;

		for (Sick s : incomingList) {
			if (s.sicksDatas[dataIndex] <= splitPoint) {
				if (s.isHarmful) {
					numberOfHarmfullSmall++;
				} else {
					numberOfHarmlessSmall++;
				}
			} else {
				if (s.isHarmful) {
					numberOfHarmfullBig++;
				} else {
					numberOfHarmlessBig++;
				}
			}
		}

		int numbers[] = new int[4];

		numbers[0] = numberOfHarmfullSmall;
		numbers[1] = numberOfHarmlessSmall;
		numbers[2] = numberOfHarmfullBig;
		numbers[3] = numberOfHarmlessBig;

		return numbers;
	}

	public void countList(ArrayList<Sick> incomingList) {
		numberOfHarmfull = 0;
		numberOfHarmless = 0;
		for (Sick s : incomingList) {
			if (s.isHarmful) {
				numberOfHarmfull++;
			} else {
				numberOfHarmless++;
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		TreeElement ce = rootElement;
		paintElement(g, ce, 800, 10, 160);
	}

	public void paintElement(Graphics g, TreeElement me, int x, int y, int widht) {
		if (me.smallElement == null && me.bigElement == null) {
			if (me.harmfull) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.green);
			}
			g.fillRect(x, y, 10, 10);
		} else {
			g.setColor(Color.BLACK);
			g.drawRect(x, y, 10, 10);
		}

		g.setColor(Color.BLACK);
		if (me.smallElement != null) {
			g.drawLine(x, y, x - widht, y + 20);
			paintElement(g, me.smallElement, x - widht, y + 20, widht - 18);
		}
		if (me.bigElement != null) {
			g.drawLine(x, y, x + widht, y + 20);
			paintElement(g, me.bigElement, x + widht, y + 20, widht - 18);
		}
	}

}
