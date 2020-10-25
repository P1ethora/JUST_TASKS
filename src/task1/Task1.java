package task1;

import java.util.Arrays;
import java.util.Scanner;

public class Task1 {

    private static int[] arraySelections = {5, 3, 2}; //массив выбора

    public static void main(String[] args) {

        int[] array =  createArray();
        System.out.println("Initial data: " + Arrays.toString(array));
        System.out.println("Answer: " + Arrays.toString(arrayResult(selectNumber(new Scanner(System.in)),array)));
    }

    private static int[] createArray() {
        System.out.println("Specify the size of the array:");
        int[] array = new int[new Scanner(System.in).nextInt()];
        Arrays.setAll(array, i -> (int) (Math.random() * 10));
        return array;
    }

    private static int selectNumber(Scanner scanner) {
        System.out.println("Select a number from: " + Arrays.toString(arraySelections));
        return scanner.nextInt();
    }

    private static int[] arrayResult(int selectNumber, int[] array) {
        int[] arrayResult = new int[1];

        if (array.length % selectNumber == 0 && selectNumber != 1) {
            for (int e : arraySelections) {
                if (selectNumber == e && array.length % e == 0) {
                    arrayResult = new int[array.length / selectNumber];
                    break;
                }
            }
        } else selectNumber = array.length;

        fillArrayResult(arrayResult, selectNumber, array);

        return arrayResult;
    }

    private static void fillArrayResult(int[] arrayResult, int selectNumber, int[] mainArray) {
        int counter = 0;
        int number = selectNumber;
        for (int i = 0; i < mainArray.length; i++) {
            arrayResult[counter] += mainArray[i];
            if (i == number - 1) {
                number += selectNumber;
                counter++;
            }
        }
    }
}