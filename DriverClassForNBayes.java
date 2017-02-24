package hw2;

import java.util.Scanner;


@SuppressWarnings("unused")
public class DriverClassForNBayes {

	public static void main(String[] args) throws Exception {

		NBayes.hamDataForTrain = args[0];
		NBayes.spamDataForTrain = args[1];
		NBayes.hamDataForTest = args[2];
		NBayes.spamDataForTest = args[3];
		Double[][] condprob = NBayes.basicNBayes();
		System.out.println("Ham Accuracy is:"
				+ NBayes.hamTest(condprob));
		System.out.println("Spam Accuracy is:"
				+ NBayes.spamTest(condprob));
	}

}
