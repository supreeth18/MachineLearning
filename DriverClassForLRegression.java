package hw2;

public class DriverClassForLRegression {
	public static void main(String[] args) throws Exception {
		System.out.println("MCAP Logistic Regression Algorithm With L2 Regularization");
		LRegression.learningRateAlpha = Double.parseDouble(args[0]);
		LRegression.lambda = Double.parseDouble(args[1]);
		LRegression.totaStepsToBeRun = Integer.parseInt(args[2]);
		LRegression.TrainDataForHam = args[3];
		LRegression.TrainDataForSpam = args[4];
		LRegression.TestDataForHam = args[5];
		LRegression.TestDataForSpam = args[6];

		
		LRegression.ExtractFeaturesFromDataFiles();
		
		LRegression.featureCalculation();
		
		LRegression.totalNumberTrainingDataSets = LRegression.extractEnglishAlphabet
				.get(0).length + LRegression.extractEnglishAlphabet.get(1).length;
		LRegression.arrayOfMatrix = new int[LRegression.totalNumberTrainingDataSets][LRegression.V
				.size()];
		LRegression.mappingVocabulationSelection();
		

		System.out.println();
		LRegression.trainingDataInitialize();
		System.out.println("Weights Initialization");
		for (int i = 0; i < 5; i++) {
			System.out.print(LRegression.weights[i] + "|j|");
		}

		LRegression.sigmoidFunctionCalculation();
		System.out.println("Weights that has been Learnt");
		for (int i = 0; i < 5; i++) {
			System.out.print(LRegression.weights[i] + "|j|");
		}
		int toatlSum = LRegression.countTotalNumberOfHamSpam(0);
		int countTotalWords = 0;
		for (int i = 0; i < toatlSum; i++) {

			LRegression.extractTestSetFeatures(i, 0);
			LRegression.caluclationOfArrayOfDatasets();
			double logisticRegressionData = LRegression.weightsCalculation();
			if (logisticRegressionData > 0)
				countTotalWords++;
		}
		System.out.println(countTotalWords);
		System.out.println(toatlSum);
		System.out.println("Ham Accuracy = " + (double) countTotalWords / toatlSum);

		toatlSum = LRegression.countTotalNumberOfHamSpam(1);
		countTotalWords = 0;
		for (int i = 0; i < toatlSum; i++) {
			LRegression.extractTestSetFeatures(i, 1);
			LRegression.caluclationOfArrayOfDatasets();
			double result = LRegression.weightsCalculation();

			if (result < 0)
				countTotalWords++;
		}
		System.out.println(countTotalWords);
		System.out.println(toatlSum);
		System.out.println("Spam Accuracy = " + (double) countTotalWords / toatlSum);

		System.out.println();
	}
}
