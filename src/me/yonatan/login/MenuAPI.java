package me.yonatan.login;

public class MenuAPI {
	
	public static int getIndex(int column, int line) {
        if (line <= 0) {
            line = 1;
        }

        if (column > 9) {
            column = 9;
        }
        if (column <= 0) {
            column = 1;
        }

        int index = (line - 1) * 9;
        return index + (column - 1);
    }
	public static int getLine(int index) {
        return (index / 9) + 1;
    }
	public static int getColumn(int index) {
        if (index < 9) {
            return index + 1;
        }
        return (index % 9) + 1;
    }
	
	
}
