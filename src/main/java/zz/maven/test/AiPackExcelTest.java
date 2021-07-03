/**
 * 
 */
package zz.maven.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author zhangle
 *
 */
public class AiPackExcelTest {

    private static final String TEMPLATE_FOLDER = "D:\\Documents\\lzwm\\产品\\AI包装设计云平台\\模板库\\NF_LZ_CY_L530_W287_D370\\";
    private static final String SHAPE_SHEET_NAME = "装饰层（svg）";
    private static final String TEXT_SHEET_NAME = "文案层";
    private static final String LOGO_SHEET_NAME = "LOGO层";
    private static final String IMAGE_SHEET_NAME = "商品层";
    private static final String INFO_SHEET_NAME = "信息层";
    private static final String BG_SHEET_NAME = "背景层（svg）";
    private static final String MODEL_SHEET_NAME = "模型信息";
    private String folderBase = "";

    public static void main(String[] args) {
        AiPackExcelTest et = new AiPackExcelTest();
        String inputFile = TEMPLATE_FOLDER + "设计模板位置信息.xlsx";
        String outputFile = TEMPLATE_FOLDER + "template.json";
        for (String arg : args) {
            System.out.println(arg);
        }
        try {
            File f = new File(inputFile);
            et.folderBase = f.getParent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("input file:" + inputFile);
        System.out.println("#### folder base:" + et.folderBase);
        et.transform(inputFile, outputFile);
        System.out.println("Json file saved into:" + outputFile);
    }

    public void transform(String inputFile, String outputFile) {
        JSONObject json = this.transform(inputFile);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            File f = new File(outputFile);
            if (!f.exists()) {
                f.createNewFile();
            }
            fw = new FileWriter(f);
            bw = new BufferedWriter(fw);
            bw.write(json.toString());
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ignore) {
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    public JSONObject transform(String fileName) {
        try {
            System.out.println("transforming...");
            JSONObject json = new JSONObject();
            FileInputStream inputStream = new FileInputStream(new File(fileName));
            Workbook workbook = WorkbookFactory.create(inputStream);
            transformModelInfo(workbook, json);
            System.out.println("---------------------------------------");
            transformFaceInfo(workbook, json);
            System.out.println("---------------------------------------");
            transformShapeInfo(workbook, json);
            System.out.println("---------------------------------------");
            transformLogoInfo(workbook, json);
            System.out.println("---------------------------------------");
            transformTextInfo(workbook, json);
            System.out.println("---------------------------------------");
            transformProductInfo(workbook, json);
            System.out.println("---------------------------------------");
            transformCodesInfo(workbook, json);
            System.out.println("......................");
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void transformModelInfo(Workbook workbook, JSONObject json) {
        Sheet sheet = workbook.getSheet(MODEL_SHEET_NAME);
        if (sheet == null) {
            throw new IllegalStateException("找不到sheet'" + MODEL_SHEET_NAME + "'");
        }
        System.out.println("loading model info '" + MODEL_SHEET_NAME + "'...");
        Row headerRow = sheet.getRow(0);
        Row row = sheet.getRow(1);
        String name = this.getCellString(row, headerRow, "模型名称");
        int lmm = this.getCellInt(row, headerRow, "长（L）（mm）");
        int wmm = this.getCellInt(row, headerRow, "宽（W）（mm）");
        int dmm = this.getCellInt(row, headerRow, "高（D）（mm）");
        int lpx = this.getCellInt(row, headerRow, "长（L）（px）");
        int wpx = this.getCellInt(row, headerRow, "宽（W）（px）");
        int dpx = this.getCellInt(row, headerRow, "高（D）（px）");
        System.out.println("loading model size (mm):" + lmm + "," + wmm + "," + dmm);
        System.out.println("loading model size (px):" + lpx + "," + wpx + "," + dpx);
        Map<String, Integer> hmmMap = getIntMap(sheet, headerRow, "搭舌宽度（H）（mm）");
        Map<String, Integer> hpxMap = getIntMap(sheet, headerRow, "搭舌宽度（H）（px）");
        String objName = this.getCellString(row, headerRow, "OBJ文件名");
        String mtlName = this.getCellString(row, headerRow, "MTL文件名");
        List<Integer> sizeFrame = this.getIntList(row, headerRow, "线框图宽高", ';', 2);
        List<Integer> sizeAI = this.getIntList(row, headerRow, "AI文件宽高", ';', 2);
        System.out.println("obj file name:" + objName);
        System.out.println("mtl file name:" + mtlName);

        json.put("name", name);
        json.put("urlBase", "");
        json.put("objFile", objName);
        json.put("mtlFile", mtlName);
        json.put("backgroundWidth", sizeFrame.get(0));
        json.put("backgroundHeight", sizeFrame.get(1));
        json.put("aiWidth", sizeAI.get(0));
        json.put("aiHeight", sizeAI.get(1));
        JSONObject d = new JSONObject();
        d.put("l", lmm);
        d.put("w", wmm);
        d.put("d", dmm);
        d.put("L", lpx);
        d.put("W", wpx);
        d.put("D", dpx);
        for (String key : hmmMap.keySet()) {
            d.put(key.toLowerCase(), hmmMap.get(key));
        }
        for (String key : hpxMap.keySet()) {
            d.put(key.toUpperCase(), hpxMap.get(key));
        }
        json.put("dimensions", d);
    }

    private void transformFaceInfo(Workbook workbook, JSONObject json) {
        Sheet sheet = workbook.getSheet(BG_SHEET_NAME);
        if (sheet == null) {
            throw new IllegalStateException("找不到sheet'" + BG_SHEET_NAME + "'");
        }
        System.out.println("loading face info '" + BG_SHEET_NAME + "'...");
        Row headerRow = sheet.getRow(0);
        JSONArray arr = new JSONArray();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String svgFile = this.getCellString(row, headerRow, "svg文件名");
            String svg = this.getSvgContent(svgFile);
            String mtlJpg = this.getCellString(row, headerRow, "mtl文件中映射的jpg文件名");
            String color = this.getCellString(row, headerRow, "色号");
            List<Integer> resolution = this.getIntList(row, headerRow, "大小（分辨率）", '×', 2);
            String left = this.getCellString(row, headerRow, "左侧位置（计算公式）");
            String top = this.getCellString(row, headerRow, "顶部位置（计算公式）");
            String width = this.getCellString(row, headerRow, "长度（计算公式）");
            String height = this.getCellString(row, headerRow, "宽度（计算公式）");

            JSONObject face = new JSONObject();
            face.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
            face.put("mtlImgFileName", mtlJpg);
            face.put("mtlWidth", resolution.get(0));
            face.put("mtlHeight", resolution.get(1));
            face.put("svg", svg);
            face.put("x", left);
            face.put("y", top);
            face.put("width", width);
            face.put("height", height);
            arr.add(face);
            json.put("backgroundColor", color);
        }
        json.put("faceList", arr);
    }

    private void transformShapeInfo(Workbook workbook, JSONObject json) {
        Sheet sheet = workbook.getSheet(SHAPE_SHEET_NAME);
        if (sheet == null) {
            throw new IllegalStateException("找不到sheet'" + SHAPE_SHEET_NAME + "'");
        }
        System.out.println("loading shape info '" + SHAPE_SHEET_NAME + "'...");
        Row headerRow = sheet.getRow(0);
        JSONArray arr = null;
        if (json.has("imageList")) {
            arr = json.getJSONArray("imageList");
        }
        if (arr == null) {
            arr = new JSONArray();
        }
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String url = this.getCellString(row, headerRow, "名称");
            List<Integer> position = this.getIntList(row, headerRow, "坐标位置（像素）", ';', 2);
            List<Integer> resolution = this.getIntList(row, headerRow, "大小（分辨率）", '×', 2);
            String svgFile = this.getCellString(row, headerRow, "svg文件名称");
            String svg = this.getSvgContent(svgFile);

            JSONObject shape = new JSONObject();
            shape.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
            shape.put("url", url);
            shape.put("x", position.get(0));
            shape.put("y", position.get(1));
            shape.put("width", resolution.get(0));
            shape.put("height", resolution.get(1));
            shape.put("svg", svg);
            shape.put("rotate", 0);
            arr.add(shape);
        }
        json.put("imageList", arr);
    }

    private String getSvgContent(String svgFile) {
        SAXReader reader = new SAXReader();
        try {
            if (!svgFile.endsWith(".svg")) {
                svgFile = svgFile + ".svg";
            }
            svgFile = this.folderBase + "/" + svgFile;
            System.out.println("reading svg content from:" + svgFile);
            Document doc = reader.read(new File(svgFile));
            Element root = doc.getRootElement();
            String result = getSvgContent(root);
            System.out.println("reading svg content:" + result);
            return result;
        } catch (DocumentException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    private String getSvgContent(Element ele) {
        List<Element> children = ele.elements();
        for (Element e : children) {
            if ("path".equals(e.getName())) {
                Attribute attr = e.attribute("d");
                if (attr != null) {
                    return attr.getValue();
                }
            }
            String tmp = getSvgContent(e);
            if (!StringUtils.isEmpty(tmp)) {
                return tmp;
            }
        }
        return "";
    }

    private String getSvgContent2(String svgFile) {
        FileReader fr = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        String pathStart = "<path";
        String pathEnd = "/>";
        try {
            File f = new File(svgFile);
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String line = br.readLine();
            int start = line.indexOf(pathStart);
            if (start >= 0) {
                int end = line.indexOf(pathEnd);
                if (end >= 0) {
                    line.substring(start, end + pathEnd.length());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException ignore) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignore) {
                }
            }
        }
        return "";
    }

    private void transformLogoInfo(Workbook workbook, JSONObject json) {
        Sheet sheet = workbook.getSheet(LOGO_SHEET_NAME);
        if (sheet == null) {
            throw new IllegalStateException("找不到sheet'" + LOGO_SHEET_NAME + "'");
        }
        System.out.println("loading logo info '" + LOGO_SHEET_NAME + "'...");
        Row headerRow = sheet.getRow(0);
        JSONArray arr = null;
        if (json.has("logoList")) {
            arr = json.getJSONArray("logoList");
        }
        if (arr == null) {
            arr = new JSONArray();
        }
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            int index = this.getCellInt(row, headerRow, "LOGO编号");
            List<Integer> position = this.getIntList(row, headerRow, "坐标位置（像素）", ';', 2);
            List<Integer> resolution = this.getIntList(row, headerRow, "大小（分辨率）", '×', 2);
            int rotate = this.getCellInt(row, headerRow, "旋转角度");

            JSONObject logo = new JSONObject();
            logo.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
            logo.put("index", index);
            logo.put("x", position.get(0));
            logo.put("y", position.get(1));
            logo.put("width", resolution.get(0));
            logo.put("height", resolution.get(1));
            logo.put("rotate", rotate);
            arr.add(logo);
        }
        json.put("logoList", arr);
    }

    private void transformTextInfo(Workbook workbook, JSONObject json) {
        Sheet sheet = workbook.getSheet(TEXT_SHEET_NAME);
        if (sheet == null) {
            throw new IllegalStateException("找不到sheet'" + TEXT_SHEET_NAME + "'");
        }
        System.out.println("loading text info '" + TEXT_SHEET_NAME + "'...");
        Row headerRow = sheet.getRow(0);
        JSONArray arr = null;
        if (json.has("textList")) {
            arr = json.getJSONArray("textList");
        }
        if (arr == null) {
            arr = new JSONArray();
        }
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String content = this.getCellString(row, headerRow, "文字内容（回车用\\n表示)");
            String font = this.getCellString(row, headerRow, "字体");
            float size = this.getCellFloat(row, headerRow, "字号");
            String color = this.getCellString(row, headerRow, "颜色（#）");
            List<Integer> position = this.getIntList(row, headerRow, "坐标位置（像素）", ';', 2);
            int rotate = this.getCellInt(row, headerRow, "旋转角度");

            JSONObject text = new JSONObject();
            text.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
            text.put("text", content);
            text.put("fontFamily", font);
            text.put("fontSize", size);
            text.put("color", color);
            text.put("x", position.get(0));
            text.put("y", position.get(1));
            text.put("rotate", rotate);
            arr.add(text);
        }
        json.put("textList", arr);
    }

    private void transformProductInfo(Workbook workbook, JSONObject json) {
        Sheet sheet = workbook.getSheet(IMAGE_SHEET_NAME);
        if (sheet == null) {
            throw new IllegalStateException("找不到sheet'" + IMAGE_SHEET_NAME + "'");
        }
        System.out.println("loading product info '" + IMAGE_SHEET_NAME + "'...");
        Row headerRow = sheet.getRow(0);
        JSONArray arr = null;
        if (json.has("imageList")) {
            arr = json.getJSONArray("imageList");
        }
        if (arr == null) {
            arr = new JSONArray();
        }
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String url = this.getCellString(row, headerRow, "名称");
            List<Integer> position = this.getIntList(row, headerRow, "坐标位置（像素）", ';', 2);
            List<Integer> resolution = this.getIntList(row, headerRow, "大小（分辨率）", '×', 2);
            String file = this.getCellString(row, headerRow, "文件名称");
            int rotate = this.getCellInt(row, headerRow, "旋转角度");

            JSONObject product = new JSONObject();
            product.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
            product.put("url", url);
            product.put("x", position.get(0));
            product.put("y", position.get(1));
            product.put("width", resolution.get(0));
            product.put("height", resolution.get(1));
            product.put("file", file);
            product.put("rotate", rotate);
            arr.add(product);
        }
        json.put("imageList", arr);
    }

    private void transformCodesInfo(Workbook workbook, JSONObject json) {
        Sheet sheet = workbook.getSheet(INFO_SHEET_NAME);
        if (sheet == null) {
            throw new IllegalStateException("找不到sheet'" + INFO_SHEET_NAME + "'");
        }
        System.out.println("loading codes info '" + INFO_SHEET_NAME + "'...");
        Row headerRow = sheet.getRow(0);
        JSONArray arr = null;
        if (json.has("imageList")) {
            arr = json.getJSONArray("imageList");
        }
        if (arr == null) {
            arr = new JSONArray();
        }
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String url = this.getCellString(row, headerRow, "名称");
            List<Integer> position = this.getIntList(row, headerRow, "坐标位置（像素）", ';', 2);
            List<Integer> resolution = this.getIntList(row, headerRow, "大小（分辨率）", '×', 2);
            int rotate = this.getCellInt(row, headerRow, "旋转角度");

            JSONObject code = new JSONObject();
            code.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
            code.put("url", url);
            code.put("x", position.get(0));
            code.put("y", position.get(1));
            code.put("width", resolution.get(0));
            code.put("height", resolution.get(1));
            code.put("rotate", rotate);
            arr.add(code);
        }
        json.put("imageList", arr);
    }

    private String getCellString(Row row, Row headerRow, String columnName) {
        for (int i = headerRow.getFirstCellNum(); i <= headerRow.getLastCellNum(); i++) {
            Cell header = headerRow.getCell(i);
            if (columnName.equals(header.getStringCellValue())) {
                Cell c = row.getCell(i);
                CellType t = c.getCellType();
                if (t == CellType.BLANK) {
                    System.out.println("getting " + columnName + ": blank");
                    return "";
                } else if (t == CellType.NUMERIC) {
                    System.out.println("getting " + columnName + ": " + c.getNumericCellValue());
                    return String.valueOf(c.getNumericCellValue());
                } else if (t == CellType.STRING) {
                    System.out.println("getting " + columnName + ": \"" + c.getStringCellValue() + "\"");
                    return c.getStringCellValue();
                }
            }
        }
        throw new IllegalStateException("找不到'" + columnName + "'");
    }

    private int getCellInt(Row row, Row headerRow, String columnName) {
        for (int i = headerRow.getFirstCellNum(); i <= headerRow.getLastCellNum(); i++) {
            Cell header = headerRow.getCell(i);
            if (columnName.equals(header.getStringCellValue())) {
                Cell c = row.getCell(i);
                if (c == null) {
                    System.out.println("getting " + columnName + ": null");
                    return 0;
                }
                CellType t = c.getCellType();
                if (t == CellType.BLANK) {
                    System.out.println("getting " + columnName + ": blank");
                    return 0;
                } else if (t == CellType.NUMERIC) {
                    System.out.println("getting " + columnName + ": " + c.getNumericCellValue());
                    return (int) c.getNumericCellValue();
                } else if (t == CellType.STRING) {
                    System.out.println("getting " + columnName + ": \"" + c.getStringCellValue() + "\"");
                    return Integer.parseInt(c.getStringCellValue());
                }
            }
        }
        throw new IllegalStateException("找不到'" + columnName + "'");
    }

    private float getCellFloat(Row row, Row headerRow, String columnName) {
        for (int i = headerRow.getFirstCellNum(); i <= headerRow.getLastCellNum(); i++) {
            Cell header = headerRow.getCell(i);
            if (columnName.equals(header.getStringCellValue())) {
                Cell c = row.getCell(i);
                CellType t = c.getCellType();
                if (t == CellType.BLANK) {
                    System.out.println("getting " + columnName + ": blank");
                    return 0;
                } else if (t == CellType.NUMERIC) {
                    System.out.println("getting " + columnName + ": " + c.getNumericCellValue());
                    return (float) c.getNumericCellValue();
                } else if (t == CellType.STRING) {
                    System.out.println("getting " + columnName + ": \"" + c.getStringCellValue() + "\"");
                    return Float.parseFloat(c.getStringCellValue());
                }
            }
        }
        throw new IllegalStateException("找不到'" + columnName + "'");
    }

    private Map<String, Integer> getIntMap(Sheet sheet, Row headerRow, String columnName) {
        System.out.println("getting int map:" + columnName);
        int rowIdx = -1;
        for (int i = headerRow.getFirstCellNum(); i <= headerRow.getLastCellNum(); i++) {
            Cell header = headerRow.getCell(i);
            if (columnName.equals(header.getStringCellValue())) {
                rowIdx = i;
                break;
            }
        }
        if (rowIdx < 0) {
            throw new IllegalStateException("找不到'" + columnName + "'");
        }
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell c = row.getCell(rowIdx);
            String mapStr = c.getStringCellValue();
            if (StringUtils.isEmpty(mapStr) || mapStr.indexOf('=') < 0) {
                continue;
            }
            int idx = mapStr.indexOf('=');
            String name = mapStr.substring(0, idx);
            int value = Integer.parseInt(mapStr.substring(idx + 1));
            System.out.println(name + ":" + value);
            result.put(name, value);
        }
        return result;
    }

    private List<Integer> getIntList(Row row, Row headerRow, String columnName, char seperator, int size) {
        String str = null;
        for (int i = headerRow.getFirstCellNum(); i <= headerRow.getLastCellNum(); i++) {
            Cell header = headerRow.getCell(i);
            if (columnName.equals(header.getStringCellValue())) {
                Cell c = row.getCell(i);
                str = c.getStringCellValue();
                break;
            }
        }
        if (str == null) {
            throw new IllegalStateException("找不到'" + columnName + "'");
        }
        if (StringUtils.isEmpty(str) || str.indexOf(seperator) < 0) {
            throw new IllegalStateException("'" + columnName + "'的值'" + str + "'格式不对");
        }
        List<Integer> result = new ArrayList<Integer>();
        String[] arr = str.split(seperator + "");
        if (arr.length < size) {
            throw new IllegalStateException("'" + columnName + "'的值'" + str + "'数量不足");
        }
        for (int i = 0; i < size; i++) {
            result.add(Integer.parseInt(arr[i]));
        }
        return result;
    }

}
