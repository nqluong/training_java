package JavaCollection.Array;

public class Main {
    public static void main(String[] args) {
        int [] array = new int[5];
        array[0] = 10;
        array[1] = 20;
        array[2] = 30;

        System.out.println("Array element at index 0: " + array[0]);
        System.out.println("Array element at index 1: " + array[1]);
        System.out.println("Array element at index 2: " + array[2]);
        System.out.println("Array element at index 3: " + array[3]);
        System.out.println("Array element at index 4: " + array[4]);

        int ages[] = {1, 2, 3, 4, 5, 6};
        for(int i = 0; i < ages.length; i++){
            System.out.println("ages[" + i + "] = " + ages[i]);
        }

        int[][] board = {{1, 2, 3, 4, 5, 6}, {1, 2, 3, 4, 5, 6}};
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
