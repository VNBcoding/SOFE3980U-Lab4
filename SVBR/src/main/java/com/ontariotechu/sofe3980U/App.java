package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Binary Classification Model
 */
public class App {
	public static void main(String[] args) {
		String[] modelFiles = { "model_1.csv", "model_2.csv", "model_3.csv" };
		double bestBCE = Double.MAX_VALUE;
		double bestAccuracy = 0;
		double bestPrecision = 0;
		double bestRecall = 0;
		double bestF1 = 0;
		double bestAUCROC = 0;
		String bestModelBCE = "";
		String bestModelAccuracy = "";
		String bestModelPrecision = "";
		String bestModelRecall = "";
		String bestModelF1 = "";
		String bestModelAUCROC = "";

		for (String filePath : modelFiles) {
			FileReader filereader;
			List<String[]> allData;

			try {
				filereader = new FileReader(filePath);
				CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
				allData = csvReader.readAll();
			} catch (Exception e) {
				System.out.println("Error reading the CSV file: " + filePath);
				continue;
			}

			int TP = 0, FP = 0, FN = 0, TN = 0;
			double bce = 0.0;
			int count = 0;

			for (String[] row : allData) {
				try {
					int y_true = Integer.parseInt(row[0]);
					float y_pred_prob = Float.parseFloat(row[1]);
					int y_pred = y_pred_prob >= 0.5 ? 1 : 0;

					bce += -(y_true * Math.log(y_pred_prob) + (1 - y_true) * Math.log(1 - y_pred_prob));

					if (y_true == 1 && y_pred == 1)
						TP++;
					if (y_true == 0 && y_pred == 1)
						FP++;
					if (y_true == 1 && y_pred == 0)
						FN++;
					if (y_true == 0 && y_pred == 0)
						TN++;
					count++;
				} catch (Exception e) {
					System.out.println("Error processing row in " + filePath + ": " + String.join(",", row));
				}
			}

			if (count > 0) {
				bce /= count;
			}

			double accuracy = (double) (TP + TN) / (TP + TN + FP + FN);
			double precision = TP + FP == 0 ? 0 : (double) TP / (TP + FP);
			double recall = TP + FN == 0 ? 0 : (double) TP / (TP + FN);
			double f1 = precision + recall == 0 ? 0 : 2 * (precision * recall) / (precision + recall);
			double aucRoc = (double) (TP + TN) / count; // Approximate AUC-ROC

			System.out.println("For " + filePath);
			System.out.println("BCE = " + bce);
			System.out.println("Confusion Matrix:");
			System.out.println("\ty=1\ty=0");
			System.out.println("y^=1\t" + TP + "\t" + FP);
			System.out.println("y^=0\t" + FN + "\t" + TN);
			System.out.println("Accuracy = " + accuracy);
			System.out.println("Precision = " + precision);
			System.out.println("Recall = " + recall);
			System.out.println("F1 Score = " + f1);
			System.out.println("AUC-ROC = " + aucRoc);
			System.out.println();

			if (bce < bestBCE) {
				bestBCE = bce;
				bestModelBCE = filePath;
			}
			if (accuracy > bestAccuracy) {
				bestAccuracy = accuracy;
				bestModelAccuracy = filePath;
			}
			if (precision > bestPrecision) {
				bestPrecision = precision;
				bestModelPrecision = filePath;
			}
			if (recall > bestRecall) {
				bestRecall = recall;
				bestModelRecall = filePath;
			}
			if (f1 > bestF1) {
				bestF1 = f1;
				bestModelF1 = filePath;
			}
			if (aucRoc > bestAUCROC) {
				bestAUCROC = aucRoc;
				bestModelAUCROC = filePath;
			}
		}

		System.out.println("According to BCE, The best model is " + bestModelBCE);
		System.out.println("According to Accuracy, The best model is " + bestModelAccuracy);
		System.out.println("According to Precision, The best model is " + bestModelPrecision);
		System.out.println("According to Recall, The best model is " + bestModelRecall);
		System.out.println("According to F1 Score, The best model is " + bestModelF1);
		System.out.println("According to AUC-ROC, The best model is " + bestModelAUCROC);
	}
}