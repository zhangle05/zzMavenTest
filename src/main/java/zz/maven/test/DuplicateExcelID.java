/**
 * 
 */
package zz.maven.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author zhangle
 *
 */
public class DuplicateExcelID {
    
    private static final String SHEET_NAME = "Sheet1";
    private static final int START_ROW = 1;
    private static final int ID_COL = 0;

    public static void main(String[] args) {
        DuplicateExcelID et = new DuplicateExcelID();
        et.saveDuplicate();
    }

    public void saveDuplicate() {
        String inputFile = "D:\\Documents\\lzwm\\tmp\\ID.xlsx";
        String outputFile = "D:\\\\Documents\\\\lzwm\\\\tmp\\\\ID_dup.xlsx";
        Workbook wb = this.findDuplicate(inputFile);
        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            wb.write(outputStream);
            wb.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Workbook findDuplicate(String fileName) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(fileName));
            Workbook workbook = WorkbookFactory.create(inputStream);

//            Workbook newWorkBook = WorkbookFactory.create(true);
//            Sheet newSheet = newWorkBook.createSheet();
            Set<String> idSet = new HashSet<String>();
            Sheet sheet = workbook.getSheet(SHEET_NAME);
            int rowCount = sheet.getLastRowNum();
//            int newRowCount = 0;
            int newIdBase = 700000;
            System.out.println("row count is:" + rowCount);
            for (int i = START_ROW; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                String id = null;
                Cell idcell = row.getCell(ID_COL);
                if (idcell != null) {
                    id = getCellString(idcell);
                }
                if (id != null) {
                    if (idSet.contains(id)) {
                        
//                        Row newRow = newSheet.createRow(newRowCount);
//                        Cell newCell = newRow.createCell(0);
//                        newCell.setCellValue(id);
                        String newId = "DC" + newIdBase;
                        idcell.setCellValue(newId);
                        newIdBase++;
                        System.out.println(id + " is replaced with " + newId);
                    } else {
                        idSet.add(id);
                    }
                }
            }
            inputStream.close();
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCellString(Cell c) {
        CellType t = c.getCellType();
        if (t == CellType.BLANK) {
            return "";
        } else if (t == CellType.NUMERIC) {
            return String.valueOf(c.getNumericCellValue());
        } else if (t == CellType.STRING) {
            return c.getStringCellValue();
        }
        return "";
    }
}
