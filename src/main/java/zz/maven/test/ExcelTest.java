/**
 * 
 */
package zz.maven.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @author zhangle
 *
 */
public class ExcelTest {

    private List<String> resourceNames;
    private static final String DETAIL_SHEET_NAME = "人员费-开发费用明细";
    private static final String META_SHEET_NAME = "人力资源单价参考";
    private static final String META_RESOURCE_NAME_HEADER = "B";
    private static final String META_RESOURCE_PRICE_HEADER = "C";
    private static final String DETAIL_RESOURCE_NAME_HEADER = "D";
    private static final String DETAIL_RESOURCE_COUNT_HEADER = "E";
    private static final int DETAIL_RESOURCE_NAME_INDEX = 3;
    private static final int DETAIL_RESOURCE_FORMULA_INDEX = 5;
    private static final int META_RESOURCE_ROW_START_INDEX = 2;

    public static void main(String[] args) {
        ExcelTest et = new ExcelTest();
        et.update();
    }

    public void update() {
        String inputFile = "D:\\Documents\\aiit\\oa\\budget.xlsx";
        String outputFile = "D:\\Documents\\aiit\\oa\\budget-gen.xlsx";
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

            Sheet sheet = workbook.getSheet(DETAIL_SHEET_NAME);
            int rowCount = sheet.getLastRowNum();
            int firstRow = -1;
            int lastRow = 0;
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                if (this.isResourceRow(row)) {
                    if (firstRow < 0) {
                        firstRow = (i + 1);
                    }
                    String fomula = this.getCellFomula(i + 1, workbook);
                    Cell c = row.getCell(DETAIL_RESOURCE_FORMULA_INDEX);
                    c.setCellFormula(fomula);
                    lastRow = (i + 1);
                }
            }
            setValidationData(sheet, firstRow, lastRow, DETAIL_RESOURCE_NAME_INDEX, DETAIL_RESOURCE_NAME_INDEX);
            inputStream.close();
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    List<String> getResourceNames(Workbook wb) {
        if (resourceNames != null && resourceNames.size() > 0) {
            return resourceNames;
        }
        Sheet sheet = wb.getSheet(META_SHEET_NAME);
        List<String> result = new ArrayList<String>();
        if (sheet == null) {
            return result;
        }
        int rowCount = sheet.getLastRowNum();
        System.out.println(META_SHEET_NAME + " row count:" + rowCount);
        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Cell cell = row.getCell(0);
            if (cell == null) {
                continue;
            }
            try {
                double type = cell.getNumericCellValue();
                if (type > 0) {
                    cell = row.getCell(1);
                    result.add(cell.getStringCellValue());
                    System.out.println(cell.getStringCellValue());
                }
            } catch (Exception ignore) {
            }
        }
        resourceNames = result;
        return result;
    }

    public void setValidationData(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol)
            throws IllegalArgumentException {
        if (firstRow < 0 || lastRow < 0 || firstCol < 0 || lastCol < 0 || lastRow < firstRow || lastCol < firstCol) {
            throw new IllegalArgumentException(
                    "Wrong Row or Column index : " + firstRow + ":" + lastRow + ":" + firstCol + ":" + lastCol);
        }
        List<String> resourceNames = this.getResourceNames(sheet.getWorkbook());
        // 设置下拉列表的内容
        String[] textlist = new String[resourceNames.size()];
        for (int i = 0; i < resourceNames.size(); i++) {
            textlist[i] = resourceNames.get(i);
        }
        if (sheet instanceof XSSFSheet) {
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
            XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                    .createExplicitListConstraint(textlist);
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
            sheet.addValidationData(validation);
        } else if (sheet instanceof HSSFSheet) {
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(textlist);
            DataValidation validation = new HSSFDataValidation(addressList, dvConstraint);
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
            sheet.addValidationData(validation);
        }
    }

    boolean isResourceRow(Row r) {
        Cell cell = r.getCell(DETAIL_RESOURCE_NAME_INDEX);
        String value = cell.getStringCellValue();
        List<String> resList = this.getResourceNames(r.getSheet().getWorkbook());
        if (resList.contains(value)) {
            return true;
        }
        return false;
    }

    public String getCellFomula(int row, Workbook wb) {
        List<String> resourceNames = this.getResourceNames(wb);
        String detailResCell = DETAIL_RESOURCE_NAME_HEADER + row;
        String detailCntCell = DETAIL_RESOURCE_COUNT_HEADER + row;
        String formula = getFormula(resourceNames.size() + META_RESOURCE_ROW_START_INDEX - 1, detailResCell, "");
        formula = formula + "*" + detailCntCell;
        return formula;
        // String template = "IF(工作量细化!%s=元数据!B2,元数据!C2,\n" +
        // "IF(工作量细化!%s=元数据!B3,元数据!C3,\n"
        // + "IF(工作量细化!%s=元数据!B4,元数据!C4,\n" + "IF(工作量细化!%s=元数据!B5,元数据!C5,\n" +
        // "IF(工作量细化!%s=元数据!B6,元数据!C6,\n"
        // + "IF(工作量细化!%s=元数据!B7,元数据!C7,\n" + "IF(工作量细化!%s=元数据!B8,元数据!C8,\n" +
        // "IF(工作量细化!%s=元数据!B9,元数据!C9,\n"
        // + "IF(工作量细化!%s=元数据!B10,元数据!C10,0)))))))))*%s";
        // return String.format(template, "D" + row, "D" + row, "D" + row, "D" +
        // row, "D" + row, "D" + row, "D" + row,
        // "D" + row, "D" + row, "E" + row);

    }

    public String getFormula(int resourceIdx, String resCell, String elseValue) {
        if (resourceIdx == META_RESOURCE_ROW_START_INDEX) {
            elseValue = "0";
        } else {
            elseValue = getFormula(resourceIdx - 1, resCell, elseValue);
        }
        String result = String.format("IF('%s'!%s='%s'!%s%s,'%s'!%s%s,%s)", DETAIL_SHEET_NAME, resCell, META_SHEET_NAME,
                META_RESOURCE_NAME_HEADER, String.valueOf(resourceIdx), META_SHEET_NAME, META_RESOURCE_PRICE_HEADER,
                String.valueOf(resourceIdx), elseValue);
        return result;
    }
}
