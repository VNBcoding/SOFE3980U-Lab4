package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;
import java.util.Arrays;

/**
 * Evaluate Multi-Class Classification Model
 */
public class App {
	public static void main(String[] args) {
		String filePath = "model.csv";
		FileReader filereader;
		List<String[]> allData;

		try {
			filereader = new FileReader(filePath);
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			allData = csvReader.readAll();
		} catch (Exception e) {
			System.out.println("Error reading the CSV file: " + filePath);
			return;
		}

		int numClasses = 5;
		int[][] confusionMatrix = new int[numClasses][numClasses];
		double ce = 0.0;
		int count = 0;

		for (String[] row : allData) {
			try {
				int y_true = Integer.parseInt(row[0]) - 1; // Convert to zero-based index
				float[] y_pred_probs = new float[numClasses];
				for (int i = 0; i < numClasses; i++) {
					y_pred_probs[i] = Float.parseFloat(row[i + 1]);
				}

				int y_pred = getMaxIndex(y_pred_probs);
				ce += -Math.log(y_pred_probs[y_true]);
				confusionMatrix[y_pred][y_true]++;
				count++;
			} catch (Exception e) {
				System.out.println("Error processing row in " + filePath + ": " + String.join(",", row));
			}
		}

		if (count > 0) {
			ce /= count;
		}

		System.out.println("CE = " + ce);
		System.out.println("Confusion Matrix:");
		System.out.print("\ty=1\ty=2\ty=3\ty=4\ty=5\n");
		for (int i = 0; i < numClasses; i++) {
			System.out.print("y^=" + (i + 1) + "\t");
			for (int j = 0; j < numClasses; j++) {
				System.out.print(confusionMatrix[i][j] + "\t");
			}
			System.out.println();
		}
	}

	private static int getMaxIndex(float[] arr) {
		int maxIndex = 0;
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > arr[maxIndex]) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}
}