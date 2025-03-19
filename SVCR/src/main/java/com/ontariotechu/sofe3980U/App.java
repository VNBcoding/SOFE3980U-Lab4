package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 */
public class App {
    public static void main(String[] args) {
        String[] models = {"model_1.csv", "model_2.csv", "model_3.csv"};
        double bestMSE = Double.MAX_VALUE, bestMAE = Double.MAX_VALUE, bestMARE = Double.MAX_VALUE;
        String bestMSEModel = "", bestMAEModel = "", bestMAREModel = "";

        for (String model : models) {
            List<String[]> allData = readCSV(model);
            if (allData == null) continue;

            double mse = MSE(allData);
            double mae = MAE(allData);
            double mare = MARE(allData);

            System.out.printf("For %s\n", model);
            System.out.printf("    MSE = %.5f\n", mse);
            System.out.printf("    MAE = %.7f\n", mae);
            System.out.printf("    MARE = %.8f\n", mare);

            if (mse < bestMSE) {
                bestMSE = mse;
                bestMSEModel = model;
            }
            if (mae < bestMAE) {
                bestMAE = mae;
                bestMAEModel = model;
            }
            if (mare < bestMARE) {
                bestMARE = mare;
                bestMAREModel = model;
            }
        }

        System.out.printf("\nAccording to MSE, The best model is %s\n", bestMSEModel);
        System.out.printf("According to MAE, The best model is %s\n", bestMAEModel);
        System.out.printf("According to MARE, The best model is %s\n", bestMAREModel);
    }

    public static List<String[]> readCSV(String filePath) {
        try {
            FileReader filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            return csvReader.readAll();
        } catch (Exception e) {
            System.out.println("Error reading the CSV file: " + filePath + " - " + e.getMessage());
            return null;
        }
    }

    public static double MSE(List<String[]> allData) {
        double MSE = 0;
        for (String[] row : allData) {
            double y_true = Double.parseDouble(row[0]);
            double y_predicted = Double.parseDouble(row[1]);
            MSE += Math.pow((y_true - y_predicted), 2);
        }
        return MSE / allData.size();
    }

    public static double MAE(List<String[]> allData) {
        double MAE = 0;
        for (String[] row : allData) {
            double y_true = Double.parseDouble(row[0]);
            double y_predicted = Double.parseDouble(row[1]);
            MAE += Math.abs(y_true - y_predicted);
        }
        return MAE / allData.size();
    }

    public static double MARE(List<String[]> allData) {
        double MARE = 0;
        for (String[] row : allData) {
            double y_true = Double.parseDouble(row[0]);
            double y_predicted = Double.parseDouble(row[1]);
            MARE += (Math.abs(y_true - y_predicted) / (Math.abs(y_true) + 1e-12)); // Avoid division by zero
        }
        return MARE / allData.size();
    }
}
