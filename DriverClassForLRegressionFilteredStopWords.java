package hw2;

public class DriverClassForLRegressionFilteredStopWords {
	public static void main(String[] args) throws Exception {
		System.out
				.println("MCAP Logistic Regression Algorithm With L2 Regularization without Stop Words");

		System.out.println();

		LRegressionFilteringStopWords.learningRateAlpha = Double
				.parseDouble(args[0]);
		LRegressionFilteringStopWords.lambda = Double.parseDouble(args[1]);
		LRegressionFilteringStopWords.totaStepsToBeRun = Integer.parseInt(args[2]);
		LRegressionFilteringStopWords.TrainDataForHam = args[3];
		LRegressionFilteringStopWords.TrainDataForSpam = args[4];
		LRegressionFilteringStopWords.TestDataForHam = args[5];
		LRegressionFilteringStopWords.TestDataForSpam = args[6];
		LRegressionFilteringStopWords.stopWordsFile = args[7];

		LRegressionFilteringStopWords.ExtractFeaturesFromDataFiles();
		LRegressionFilteringStopWords.featureCalculation();
		LRegressionFilteringStopWords.totalNumberTrainingDataSets = LRegressionFilteringStopWords.extractEnglishAlphabet
				.get(0).length
				+ LRegressionFilteringStopWords.extractEnglishAlphabet.get(1).length;
		LRegressionFilteringStopWords.arrayOfMatrix = new int[LRegressionFilteringStopWords.totalNumberTrainingDataSets][LRegressionFilteringStopWords.V
				.size()];
		LRegressionFilteringStopWords.mappingVocabulationSelection();

		LRegressionFilteringStopWords.trainingDataInitialize();// done
		LRegressionFilteringStopWords.sigmoidFunctionCalculation();

		int totalCount = LRegressionFilteringStopWords.countTotalNumberOfHamSpam(0);
		int countWords = 0;
		for (int i = 0; i < totalCount; i++) {
			LRegressionFilteringStopWords.extractTestSetFeatures(i, 0);
			LRegressionFilteringStopWords.caluclationOfArrayOfDatasets();
			double logisticRegression = LRegressionFilteringStopWords.weightsCalculation();
			if (logisticRegression > 0)
				countWords++;
		}
		System.out.println("Ham Accuracy is :" + (double) countWords / totalCount);

		totalCount = LRegressionFilteringStopWords.countTotalNumberOfHamSpam(1);
		countWords = 0;
		for (int i = 0; i < totalCount; i++) {
			LRegressionFilteringStopWords.extractTestSetFeatures(i, 1);
			LRegressionFilteringStopWords.caluclationOfArrayOfDatasets();
			double result = LRegressionFilteringStopWords.weightsCalculation();
			if (result < 0)
				countWords++;
		}
		System.out
				.println("Spam Accuracy is :" + (double) countWords / totalCount);
	}
}
