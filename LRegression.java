package hw2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

public class LRegression {

	static TreeSet<String> V = new TreeSet<String>();
	static Integer totalSpamCount = 0, totalHamCount = 0, N = 0, category = 0;
	static double learningRateAlpha = 0.0001;
	static double lambda = 0.05;
	static int totaStepsToBeRun = 250;
	static Double[] weights;
	static double weight0;
	static ArrayList<String> initialDataSet = new ArrayList<String>();
	static ArrayList<String> testFeatures = new ArrayList<String>();
	static ArrayList<String[]> extractEnglishAlphabet;
	static int totalNumberTrainingDataSets, hamExamples, spamExamples;
	static int[][] arrayOfMatrix;
	static int[] testMatrix;
	static ArrayList<Integer> categoryData = new ArrayList<Integer>();
	static ArrayList<TreeSet<String>> hamText;
	static TreeSet<String> testTree;
	static HashMap<Integer, HashMap<String, Integer>> fileFeatureValueTrain = new HashMap<Integer, HashMap<String, Integer>>();
	static String TrainDataForHam, TrainDataForSpam, TestDataForHam, TestDataForSpam;

	public static double weightsCalculation() {
		double temp = 0.0;
		for (int i = 0; i < weights.length; i++) {
			temp += weights[i] * testMatrix[i];

		}
		temp += weight0;
		return temp;

	}

	public static void caluclationOfArrayOfDatasets() {
		testMatrix = new int[initialDataSet.size()];
		Arrays.fill(testMatrix, 0);
		for (int i = 0; i < initialDataSet.size(); i++) {
			Iterator<String> itr = testTree.iterator();
			while (itr.hasNext()) {
				if (itr.next().contentEquals(initialDataSet.get(i))) {
					testMatrix[i] = 1;
				}
			}
			
		}
	}

	public static int countTotalNumberOfHamSpam(int val) {
		File testFolder;
		if (val == 0)
			testFolder = new File(TestDataForHam);
		else
			testFolder = new File(TestDataForSpam);

		File[] testlistOfFiles = testFolder.listFiles();
		return testlistOfFiles.length;
	}

	@SuppressWarnings({ "unused", "resource" })
	public static void extractTestSetFeatures(int i, int j) throws Exception {
		File testFolder;
		if (j == 0)
			testFolder = new File(TestDataForHam);
		else
			testFolder = new File(TestDataForSpam);

		
		testTree = new TreeSet<String>();
		File[] arrayOfFiles = testFolder.listFiles();
		FileInputStream inputStream = new FileInputStream(new File(
				testFolder.listFiles()[i].toString()));
		BufferedReader readerData = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(
						testFolder.listFiles()[i].toString()))));
		String str = readerData.readLine();
		while (str != null) {
			str = str.replaceAll(" ' ", "'");
			str = str.replaceAll("[^a-zA-Z']", " ");
			str = str.replaceAll("''", " ");
			str = str.replaceAll("\\s+", " ");
			str = str.trim();
			String arr[] = str.split(" ");
			for (int h = 0; h < arr.length; h++) {
				testTree.add(arr[h]);
				
			}
			str = readerData.readLine();
		}
	}

	public static void mappingVocabulationSelection() {
		for (int i = 0; i < totalNumberTrainingDataSets; i++) {
			TreeSet<String> temp = hamText.get(i);
			Iterator<String> itr = temp.iterator();
			while (itr.hasNext()) {
				String word = itr.next();
				for (int j = 0; j < initialDataSet.size(); j++) {
					if (initialDataSet.get(j).contentEquals(word)) {
						arrayOfMatrix[i][j] = 1;
						break;
					}
				}
			}
		}
	}

	public static void mappingFeatureSelection() {
		for (int i = 0; i < totalNumberTrainingDataSets; i++) {
			TreeSet<String> temp = hamText.get(i);
			Iterator<String> itr = temp.iterator();
			HashMap<String, Integer> hm = new HashMap<String, Integer>();
			while (itr.hasNext()) {
				String word = itr.next();
				for (int j = 0; j < initialDataSet.size(); j++) {
					if (initialDataSet.get(j).contentEquals(word)) {
						hm.put(initialDataSet.get(j), 1);
						// System.out.println(word);
						// System.exit(1);
						break;
					}
				}
			}
			fileFeatureValueTrain.put(i, hm);
		}
	}

	public static void featureCalculation() {
		Iterator<String> itr = V.iterator();
		while (itr.hasNext()) {
			initialDataSet.add(itr.next().toLowerCase());
		}

	}

	public static void trainingDataInitialize() {
		
		weights = new Double[V.size()];
		Random r = new Random();
		weight0 = -3 + r.nextDouble() * (3 - (-3));
		for (int i = 0; i < weights.length; i++) {
			double x = r.nextDouble();
			weights[i] = (double) x;
			weights[i] = -4 + r.nextDouble() * (3 - (-3));
		}
		System.out.println();
	}

	public static void TrainingDataCalculation() {

		System.out.println(totalNumberTrainingDataSets);
		System.out.println(weights.length);
		System.out.println(arrayOfMatrix.length);
		System.out.println(arrayOfMatrix[0].length);

		int k = 0;
		for (int i = 0; i < weights.length; i++) {
			double temp = 0;
			double temp1 = 0;

			for (int j = 0; j < weights.length; j++) {
				temp1 += weights[j] * arrayOfMatrix[k][j];
			}
			double predValue = sigmoid(temp1 + weight0);
			for (int l = 0; l < totalNumberTrainingDataSets; l++) {
				String feature = initialDataSet.get(i);
				double val = categoryData.get(l) - predValue;
				temp += val * (arrayOfMatrix[l][i]);
			}
			k++;
			weights[i] = weights[i] + (learningRateAlpha) * temp
					- (learningRateAlpha * lambda * weights[i]);
		}

	}

	public static void learningRateApha() {
		for (int m = 0; m < 1; m++) {

		

			for (int i = 0; i < weights.length; i++) {
				
				System.out.println(i);
				double temp = 0;
				double sum = 0;
				for (int j = 0; j < totalNumberTrainingDataSets; j++) {
					double classVal = categoryData.get(j);
					double pred = 0;
					for (int j2 = 0; j2 < weights.length; j2++) {
						pred += weights[j2] * arrayOfMatrix[j][j2];
					}
					pred = sigmoid(pred + weight0);
					pred = 1 - pred;
					sum += arrayOfMatrix[j][i] * (classVal - pred);
				}
				weights[i] += (learningRateAlpha * sum)
						- (learningRateAlpha * 1 * weights[i]);
			}
		}
	}

	public static void sigmoidInitialCalculation() {
		for (int m = 0; m < 10; m++) {
			System.out.println(m + "th iteration Running ");
			for (int i = 0; i < weights.length; i++) {
				double temp = 0;
				double sum = 0;
				System.out.println(i);
				for (int j = 0; j < totalNumberTrainingDataSets; j++) {
					double classVal = 0;
					if (j < hamExamples)
						classVal = 1;
					double pred = 0;
					// System.out.println(j);
					HashMap<String, Integer> h = fileFeatureValueTrain.get(j);
					for (int j2 = 0; j2 < weights.length; j2++) {
						int val = 0;
						System.out.println(j2);
						if (h.containsKey(initialDataSet.get(j2))) {
							pred += weights[j2] * 1;
						}
					}
					pred = sigmoid(pred + weight0);
					pred = 1 - pred;
					int zval = 0;
					if (h.get(i) != null) {
						zval = 1;
					}
					sum += zval * (classVal - pred);

				}
				weights[i] += (learningRateAlpha * sum)
						- (learningRateAlpha * 1 * weights[i]);
			}
		}
	}

	public static void sigmoidFunctionCalculation() {
		for (int m = 0; m < totaStepsToBeRun; m++) {
			System.out.println(m + "th iteration Running ");
			for (int i = 0; i < totalNumberTrainingDataSets; i++) {
				double pred = 0;
				for (int j = 0; j < weights.length; j++) {
					pred += weights[j] * arrayOfMatrix[i][j];
				}
				double label = 0;
				if (i < hamExamples) {
					label = 1;
					pred = sigmoid(-1 * (pred + weight0));
					pred = 1 - pred;
				} else {
					pred = sigmoid(-1 * (pred + weight0));
				}
				for (int j = 0; j < weights.length; j++) {
					weights[j] = weights[j] + (learningRateAlpha) * (label - pred)
							* arrayOfMatrix[i][j]
							- (learningRateAlpha * lambda * weights[j]);
				}
			}
		}
	}

	private static double sigmoid(double z) {
		double sigmoid = 1 / (double) (1 + Math.exp(-1 * z));
		return sigmoid;
	}

	public static void ExtractFeaturesFromDataFiles() throws Exception {
		extractEnglishAlphabet = VocabularyExtractionDataFiles();
		category = extractEnglishAlphabet.size();
		String[] ham = extractEnglishAlphabet.get(0);
		String[] spam = extractEnglishAlphabet.get(1);
		for (int i = 0; i < ham.length; i++) {
			ham[i] = ham[i].replaceAll(" ' ", "'");
			ham[i] = ham[i].replaceAll("[^a-zA-Z']", " ");
			ham[i] = ham[i].replaceAll("''", " ");
			ham[i] = ham[i].replaceAll("\\s+", " ");
			ham[i] = ham[i].trim();
			String temp[] = ham[i].split(" ");
			for (int j = 0; j < temp.length; j++) {
				V.add(temp[j]);
			}
		}
		for (int i = 0; i < spam.length; i++) {
			spam[i] = spam[i].replaceAll(" ' ", "'");
			spam[i] = spam[i].replaceAll("[^a-zA-Z']", " ");
			spam[i] = spam[i].replaceAll("''", " ");
			spam[i] = spam[i].replaceAll("\\s+", " ");
			spam[i] = spam[i].trim();

			String temp[] = spam[i].split(" ");
			for (int j = 0; j < temp.length; j++) {
				V.add(temp[j]);
			}
		}
	}

	public static ArrayList<String[]> VocabularyExtractionDataFiles() throws Exception {
		// TODO Auto-generated method stub
		File hamfolder = new File(TrainDataForHam);
		File[] hamlistOfFiles = hamfolder.listFiles();
		StringBuilder builder = new StringBuilder();
		String[] strArrayDataSets = new String[hamlistOfFiles.length];
		hamExamples = hamlistOfFiles.length;
		hamText = new ArrayList<TreeSet<String>>();
		TreeSet<String> tree = new TreeSet<String>();
		for (int i = 0; i < hamlistOfFiles.length; i++) {
			tree = new TreeSet<String>();
			builder = new StringBuilder();
			FileInputStream en = new FileInputStream(new File(
					hamlistOfFiles[i].toString()));
			BufferedReader x = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(
							hamlistOfFiles[i].toString()))));
			String data = x.readLine();
			while (data != null) {
				builder.append(data);
				data = data.replaceAll(" ' ", "'");
				data = data.replaceAll("[^a-zA-Z']", " ");
				data = data.replaceAll("''", " ");
				data = data.replaceAll("\\s+", " ");
				data = data.trim();
				String arr[] = data.split(" ");
				for (int j = 0; j < arr.length; j++) {
					tree.add(arr[j].toLowerCase());
				}
				data = x.readLine();
			}
			hamText.add(tree);
			categoryData.add(1);
			strArrayDataSets[i] = builder.toString();
		}
		// System.exit(1);
		File spamfolder = new File(TrainDataForSpam);
		File[] spamlistOfFiles = spamfolder.listFiles();
		String[] arrayResultOfDSets = new String[spamlistOfFiles.length];
		spamExamples = spamlistOfFiles.length;
		for (int i = 0; i < spamlistOfFiles.length; i++) {
			tree = new TreeSet<String>();
			builder = new StringBuilder();
			FileInputStream en = new FileInputStream(new File(
					spamlistOfFiles[i].toString()));
			BufferedReader readerRes = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(
							spamlistOfFiles[i].toString()))));
			String dataRes = readerRes.readLine();
			while (dataRes != null) {
				builder.append(dataRes);

				dataRes = dataRes.replaceAll(" ' ", "'");

				dataRes = dataRes.replaceAll("[^a-zA-Z']", " ");

				dataRes = dataRes.replaceAll("''", " ");

				dataRes = dataRes.replaceAll("\\s+", " ");
				dataRes = dataRes.trim();

				String arr[] = dataRes.split(" ");
				for (int j = 0; j < arr.length; j++) {
					tree.add(arr[j].toLowerCase());
				}
				dataRes = readerRes.readLine();
			}
			hamText.add(tree);
			categoryData.add(0);
			arrayResultOfDSets[i] = builder.toString();
		}

		ArrayList<String[]> result = new ArrayList<String[]>();
		result.add(strArrayDataSets);
		result.add(arrayResultOfDSets);
		return result;
	}

}
