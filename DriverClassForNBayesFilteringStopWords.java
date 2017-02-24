package hw2;

public class DriverClassForNBayesFilteringStopWords {
	public static void main(String[] args) throws Exception {
		System.out.println("Multinomial Naive Bayes Algorithm Without Stop Words");
    	NBayesFilteringStopWords.hamDataForTrain = args[0];
		NBayesFilteringStopWords.spamDataForTrain = args[1];
		NBayesFilteringStopWords.hamDataForTest = args[2];
		NBayesFilteringStopWords.spamDataForTest = args[3];
		NBayesFilteringStopWords.stopWordsFile = args[4];
		Double[][] prob = NBayesFilteringStopWords.naiveBayesBasic();
		System.out.println("Ham Accuracy is:"
				+ NBayesFilteringStopWords.hamTest(prob));
		System.out.println("Spam Accuracy is:"
				+ NBayesFilteringStopWords.spamTest(prob));
	}
}
