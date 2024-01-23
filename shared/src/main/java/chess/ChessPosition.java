package chess;

import java.util.Objects;
/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 *
 * using 1-8 for values
 */
public class ChessPosition {

    private final int row;
    private final int col;
    public ChessPosition(int row, int col) {
       // if (row > 8 || row < 1 || col > 8 || col < 1) If out of bounds.
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return this.col;
    }

    /**
     * @return converts column to index to be used by game board
     * 0 codes for left column
     */
    public int getColumnIndex() {
        return this.col - 1;
    }

    /**
     *
     * @return converts row to index to be used by game board
     * 0 codes for top row
     */
    public int getRowIndex() {
        return 8 - this.getRow();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
