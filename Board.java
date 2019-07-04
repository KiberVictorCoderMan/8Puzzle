import java.util.Stack;

public class Board {
    //Такою має бути дошка
    final int TARGET_BOARD[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
    //Ця константа відповідає за розмірність
    private final int N = 3;
    //З цією дошкою ми працюємо
    private int blocks[][];
    // конструюємо дошку у вигляді двовимірного масиву N на N
    // (blocks[i][j] =блок в ряду i, колонці j)
    public Board(int[][] blocks) {
        this.blocks = new int[3][3];
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
    }
    // розмірність дошки N
    public int dimension() {
        return N;
    }
    // кількість блоків не на своєму місці
    /*
Функція Хеммінга – число блоків в невірних позиціях, плюс кількість кроків,
 що зроблена для досягнення поточної пошукової ноди. Інтуїтивно
 зрозуміло, що позиція на дошці в якій мала кількість блоків стоїть не на своїх місцях досить близька до шуканого стану.
     */
    public int hamming() {
        int noTargetPoss = 0;
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if (blocks[i][j] != TARGET_BOARD[i][j]) noTargetPoss++;
            }
        }
        return noTargetPoss;
    }
    // сума Манхатенських відстаней між блоками і цільовим станом
    /*
    Манхетенська функція  – сума Манхетенських відстаней (сума вертикальної і горизонтальної відстані) з
     поточної позиції кожного блока до шуканого, плюс кількість кроків, зроблених для досягнення поточного стану

     Обраховуємо відстань від кожного елменту до аналогічного елементу канонічної матриці
     */
    public int manhattan() {
        int distanceToTheDesiredSum = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != 0) {
                    distanceToTheDesiredSum += Math.abs(i - ((blocks[i][j] - 1) / (N))) + Math.abs(j - ((blocks[i][j] - 1) % (N)));
                }
            }
        }

        return distanceToTheDesiredSum;
    }

    //Чи еквівалентні два масиви?
    private boolean arrEq(int[][] arr1, int[][] arr2) {
        for (int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if(arr1[i][j] != arr2[i][j]) return false;
            }
        }
        return true;
    }

    // чи є ця дошка цільовим станом
    public boolean isGoal() {
        return arrEq(blocks, TARGET_BOARD);
    }
    // чи ця дошка рівна y?
    public boolean equals(Object y) {
        Board b = (Board)y;
        return arrEq(blocks, b.blocks);
    }

    //поміняти місцями елементи двомірного масиву
    public void swap(int[][] blocksForSwap, int x1, int y1, int x2, int y2) {
        int buffer = blocksForSwap[y2][x2];
        blocksForSwap[y2][x2] = blocksForSwap[y1][x1];
        blocksForSwap[y1][x1] = buffer;
    }

    // всі сусідні дошки
    public Iterable<Board> neighbors() {
        //Стек оптимальний варіант для нашої задачі
        Stack<Board> result = new Stack<>();

        int zeroPosX = 0;
        int zeroPosY = 0;
        //Шляхом перебору знаходимо місцезнаходження 0
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0) {
                    zeroPosY = i;
                    zeroPosX = j;
                    break;
                }
            }
        }

        //нижній сусід
        if (zeroPosY + 1 < N) {
            swap(blocks, zeroPosX, zeroPosY, zeroPosX, zeroPosY + 1);
            result.push(new Board(blocks));
            swap(blocks, zeroPosX, zeroPosY + 1, zeroPosX, zeroPosY);
        }
        // верхній сусід
        if (zeroPosY - 1 >= 0) {
            swap(blocks, zeroPosX, zeroPosY, zeroPosX, zeroPosY - 1);
            result.push(new Board(blocks));
            swap(blocks, zeroPosX, zeroPosY - 1, zeroPosX, zeroPosY);
        }
        //лівий сусід
        if (zeroPosX - 1 >= 0) {
            swap(blocks, zeroPosX, zeroPosY, zeroPosX - 1, zeroPosY);
            result.push(new Board(blocks));
            swap(blocks, zeroPosX - 1, zeroPosY, zeroPosX, zeroPosY);
        }
        //правий сусід
        if (zeroPosX + 1 < N) {
            swap(blocks, zeroPosX, zeroPosY, zeroPosX + 1, zeroPosY);
            result.push(new Board(blocks));
            swap(blocks, zeroPosX + 1, zeroPosY, zeroPosX, zeroPosY);
        }

        return result;
    }
    // строкове подання
    public String toString() {

        String buffer = "";

        for(int i = 0; i < N; i++) {
            buffer += "{ ";
            for(int j = 0; j < N; j++) {
                buffer += blocks[i][j] + " ";
            }
            buffer += "}\n";
        }

        return buffer;
    }

    public static void main(String args[]) {


        int arr[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board b = new Board(arr);
        System.out.println(b.toString());
        for(Board buff : b.neighbors()) {
            System.out.println(buff.toString());
        }
        System.out.println(b.hamming());
    }
}
