package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        List<String> nameList = new ArrayList<>();

        List<String> findList = new ArrayList<>();

        int[] totalEntry = { 0 };

        initialize(nameList, findList, totalEntry);

        int[] foundEntry = { 0 };

        System.out.println("Start searching (linear search)...");

        long linearSearchTime = Search.linearSearch(nameList, findList, foundEntry);

        display.initialDisplay(totalEntry, foundEntry, linearSearchTime);

        boolean[] isLinear = { false };

        System.out.println("\n\nStart searching (bubble sorting + jump search)...");

        long bubbleSortTime = Sort.bubbleSort(nameList, linearSearchTime, isLinear);

        long[] searchTime = { 0 };

        foundEntry[0] = 0;

        if (isLinear[0]) {
            searchTime[0] = Search.linearSearch(nameList, findList, foundEntry);
        } else {
            searchTime[0] = Search.jumpSearch(nameList, findList, foundEntry);
        }

        display.initialDisplay(totalEntry, foundEntry, (bubbleSortTime + searchTime[0]));

        display.displayTime(bubbleSortTime, searchTime[0], isLinear[0]);

        System.out.println("\n\nStart searching (quick sort + binary search)...");

        List<String> newNameList = new ArrayList<>();
        List<String> newFindList = new ArrayList<>();

        initialize(newNameList, newFindList, totalEntry);

        long sortTime = Sort.quickSort(newNameList, 0, newNameList.size() - 1);

        searchTime[0] = Search.binarySearch(newNameList, newFindList);

        display.initialDisplay(totalEntry, foundEntry, (sortTime + searchTime[0]));

        display.displayTime(sortTime, searchTime[0], false);
    }

    private static void initialize(List<String> nameList, List<String> findList, int[] a) throws FileNotFoundException {

        a[0] = 0;

        String directoryPath = "C:\\Users\\Pritam\\Documents\\Java\\directory.txt";

        String findPath = "C:\\Users\\Pritam\\Documents\\Java\\find.txt";

        File directoryFile = new File(directoryPath);
        File findFile = new File(findPath);

        Scanner directoryScanner = new Scanner(directoryFile);
        Scanner findScanner = new Scanner(findFile);

        while (directoryScanner.hasNextLine()) {
            directoryScanner.next();
            nameList.add(directoryScanner.nextLine().trim());
        }

        while (findScanner.hasNextLine()) {
            findList.add(findScanner.nextLine());
            a[0]++;
        }

    }

}

class Sort {

    public static long quickSort(List<String> list, int startIndex, int endIndex) {

        long startTime = System.currentTimeMillis();

        if (startIndex >= endIndex) {

            long endTime = System.currentTimeMillis();
            return endTime - startTime;

        }

        int pivotIndex = new Random().nextInt(endIndex - startIndex) + startIndex;
        String pivot = list.get(pivotIndex);
        swap(list, pivotIndex, endIndex);

        int leftIndex = startIndex;
        int rightIndex = endIndex;


        while (leftIndex < rightIndex) {

            while (leftIndex < rightIndex && lexicography(pivot, list.get(leftIndex))) {
                leftIndex++;
            }

            while (leftIndex < rightIndex && lexicography(list.get(rightIndex), pivot)) {
                rightIndex--;
            }

            swap(list, leftIndex, rightIndex);
        }

        swap(list, leftIndex, endIndex);

        long endTime = System.currentTimeMillis();

        return (endTime - startTime) + quickSort(list, startIndex, leftIndex - 1) + quickSort(list, leftIndex + 1, endIndex);

    }

    public static long bubbleSort(List<String> list, long linearSearchTime, boolean[] isLinear) {
        long start = System.currentTimeMillis();

        long[] end = { 0 };

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                end[0] = System.currentTimeMillis();
                if ((end[0] - start) > (linearSearchTime + 600000)) {
                    isLinear[0] = true;
                    return (end[0] - start);
                }

                if (lexicography(list.get(j), list.get(j + 1))) {
                    String temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }

        return (end[0] - start);
    }

    private static void swap(List<String> list, int leftIndex, int rightIndex) {
        String temp = list.get(rightIndex);
        list.set(rightIndex, list.get(leftIndex));
        list.set(leftIndex, temp);
    }

    public static boolean lexicography(String a, String b) {

        int index = 0;

        while (true) {
            if (a.equals(b)){
                return true;
            }
            if (a.charAt(index) > b.charAt(index)) {
                return true;
            } else if (a.charAt(index) == b.charAt(index)) {

                if (a.length() - 1 == index && b.length() - 1 == index) {
                    return false;
                } else if (a.length() - 1 == index) {
                    return false;
                } else if (b.length() - 1 == index) {
                    return true;
                } else {
                    index++;
                }


            } else {
                return false;
            }
        }
    }

}

class Search {

    public static long binarySearch(List<String> nameList, List<String> findList) {

        long startTime = System.currentTimeMillis();

        for (String s : findList) {
            binarySearchRecursion(nameList, s , 0, nameList.size() - 1);
        }

        long endTime = System.currentTimeMillis();

        return startTime - endTime;

    }

    public static void binarySearchRecursion(List<String> nameList, String find, int startPoint, int endPoint) {

        if (endPoint >= startPoint) {
            return;
        }

        int mid = (startPoint + endPoint) / 2;

        if (find.equals(nameList.get(mid))) {
            return;
        }
        if (Sort.lexicography(nameList.get(mid), find)) {
            binarySearchRecursion(nameList, find, startPoint, mid - 1);
        } else {
            binarySearchRecursion(nameList, find, mid + 1, endPoint);
        }

    }

    public static long linearSearch(List<String> nameList, List<String> findList, int[] a) {

        long start = System.currentTimeMillis();

        for (String s : findList) {
            for (String f : nameList) {
                if (s.equalsIgnoreCase(f)) {
                    a[0] += 1;
                    break;
                }
            }
        }


        long end = System.currentTimeMillis();

        return (end - start);

    }

    public static long jumpSearch(List<String> nameList, List<String> findList, int[] foundList) {

        long start = System.currentTimeMillis();

        int index = 0;

        int step;

        boolean notReverse = true;

        for (String s : findList) {

            step = (int) Math.floor(Math.sqrt(nameList.size()));

            while (true) {
                if (step == 1 && index == 0 && !nameList.get(index).equals(s)) break;

                if (notReverse) {

                    if (nameList.get(index).equals(s)) {
                        foundList[0]++;
                        break;
                    } else if (Sort.lexicography(s, nameList.get(index))) {

                        if (index + step >= nameList.size()) {
                            index = nameList.size() - 1;
                        }
                        else {
                            index += step;
                        }
                    } else {
                        notReverse = false;
                        step = (int) Math.floor(Math.sqrt(step));
                        if (index > 0){
                            index--;
                        }
                    }

                } else {
                    if (nameList.get(index).equals(s)) {
                        foundList[0]++;
                        break;
                    } else if (Sort.lexicography(s, nameList.get(index))) {
                        notReverse = true;
                        step = (int) Math.floor(Math.sqrt(step));
                        index++;
                    } else {
                        if (index - step < 0) {
                            index = 0;
                        }
                        else {
                            index -= step;
                        }

                    }
                }

            }

        }

        long end = System.currentTimeMillis();

        return end - start;

    }

}

class display {

    public static void displayTime(long a, long b, boolean isLinear) {

        System.out.print("\nSorting time: " + timeTaken(a));

        if (isLinear) {
            System.out.print(" - STOPPED, moved to linear search");
        }

        System.out.print("\nSearching time: " + timeTaken(b));

    }

    public static String timeTaken(long totalTime) {

        int ms = (int) (totalTime % 1000);

        totalTime /= 1000;

        int s = (int) (totalTime % 60);

        totalTime /= 60;

        int m = (int) (totalTime % 60);

        return m + " min. " + s + " sec. " + ms + " ms.";

    }

    public static void initialDisplay(int[] totalEntry, int[] foundEntry, long totalTime) {

        System.out.print("Found " + foundEntry[0] + " / " + totalEntry[0] + " entries. ");
        System.out.print("Time taken: " + timeTaken(totalTime));

    }

}
