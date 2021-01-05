/**
 * 
 */
package zz.maven.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.alipay.api.internal.util.StringUtils;

/**
 * @author zhangle
 *
 */
public class CompanyBatchQuery {
    
    private static final String XIAOSHAN_SHEET_NAME = "萧山";
    private static final int START_ROW = 1;
    private static final int COMPANY_NAME_COL = 0;
    private static final int COMPANY_ID_COL = 2;
    private static final int COMPANY_ADDRESS_COL = 3;
    private static final int COMPANY_JSON_COL = 5;

    public static void main(String[] args) {
        CompanyBatchQuery et = new CompanyBatchQuery();
        et.update();
    }

    public void update() {
        String inputFile = "D:\\Documents\\lzwm\\产品\\大数据平台\\company2.xlsx";
        String outputFile = "D:\\\\Documents\\\\lzwm\\\\产品\\\\大数据平台\\\\company_new.xlsx";
        Workbook wb = this.updateExcel(inputFile);
        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            wb.write(outputStream);
            wb.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Workbook updateExcel(String fileName) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(fileName));
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheet(XIAOSHAN_SHEET_NAME);
            int rowCount = sheet.getLastRowNum();
            for (int i = START_ROW; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                updateRow(row);
            }
            inputStream.close();
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateRow(Row row) {
        Cell cell = row.getCell(COMPANY_NAME_COL);
        if (cell == null) {
            return;
        }
        String name = getCellString(cell);
        String id = null;
        Cell idcell = row.getCell(COMPANY_ID_COL);
        if (idcell != null) {
            id = getCellString(idcell);
        }
        if (!StringUtils.isEmpty(id)) {
            System.out.println("no:" + row.getRowNum() + ", name:" + name + ", id:" + id);
            return;
        }

        String dataJson = getCompanyData(name);
        setCompanyData(row, dataJson);
    }

    private String getCompanyData(String name) {
        return name;
    }

    private void setCompanyData(Row row, String data) {
        Cell cell = row.getCell(COMPANY_JSON_COL);
        if (cell == null) {
            cell = row.createCell(COMPANY_JSON_COL);
        }
        cell.setCellValue(data);
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
