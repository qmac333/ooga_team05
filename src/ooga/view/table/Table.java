package ooga.view.table;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ooga.view.DisplayableItem;

/**
 * A table class for organizing nodes.
 */
public class Table implements DisplayableItem {

  private int numRows;
  private int numCols;
  private double cellWidth;
  private double cellHeight;

  private List<List<TableCell>> tableCells;
  private List<HBox> visualRows;
  private VBox visualDisplay;


  /**
   * Initializes the table.
   * @param rows is the initial number of rows in the table.
   * @param cols is the initial number of cols in the table.
   * @param cellWidth is the width of an individual cell
   * @param cellHeight is the height of an individual cell
   */
  public Table(int rows, int cols, double cellWidth, double cellHeight) {
    numRows = 0; // will be incremented by the addRow() method when adding rows
    numCols = cols;
    this.cellHeight = cellHeight;
    this.cellWidth = cellWidth;

    tableCells = new ArrayList<>();
    visualRows = new ArrayList<>();
    visualDisplay = new VBox();
    visualDisplay.setAlignment(Pos.CENTER);
    for (int i = 0; i < rows; i++) {
      addRow();
    }
  }

  /**
   * @return the number of rows in the table
   */
  public int getNumRows() {
    return numRows;
  }

  /**
   * @return the number of columns in the table
   */
  public int getNumCols() {
    return numCols;
  }

  /**
   * Adds a new row to the end of the table.
   */
  public void addRow() {
    List<TableCell> newRow = new ArrayList<>();
    HBox newRowVisual = new HBox();
    newRowVisual.setAlignment(Pos.CENTER);

    for (int j = 0; j < numCols; j++) {
      TableCell cell = new TableCell(cellWidth, cellHeight);
      newRow.add(cell);
      newRowVisual.getChildren().add(cell.getCell());
    }

    tableCells.add(newRow);
    visualRows.add(newRowVisual);
    visualDisplay.getChildren().add(newRowVisual);

    numRows++;
  }

  /**
   * Deletes a row from the table.
   * @index i is the row number to delete.
   * @throw IndexOutofBoundsException
   */
  public void deleteRow(int row) throws IndexOutOfBoundsException {
      tableCells.remove(row);
      visualRows.remove(row);
      visualDisplay.getChildren().remove(row);
      numRows--;
  }

  /**
   * Edits the cell at position (x, y) in the table.
   * @param contents the new contents of the cell to set.
   * @throw IndexOutofBoundsException for invalid cell coordinates.
   */
  public void setCell(int x, int y, Node contents) throws IndexOutOfBoundsException {
      tableCells.get(y).get(x).setContents(contents);
  }

  /**
   * Gets the contents of a cell at (x, y).
   * @throw IndexOutofBoundsException for invalid cell coordinates.
   */
  public Node getCell(int x, int y) throws IndexOutOfBoundsException {
    return tableCells.get(y).get(x).getContents();
  }

  @Override
  public Node getDisplayableItem() {
    return visualDisplay;
  }

}
