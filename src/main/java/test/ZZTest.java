/**
 * 
 */
package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * @author zhangle
 *
 */
public class ZZTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        System.out.println(System.currentTimeMillis());
//        String secret = UUID.randomUUID().toString() + UUID.randomUUID().toString();
//        System.out.println(secret.replace("-", ""));
        // String inputFile =
        // "/Users/zhangle/dev/jingyou/normal_chinese_code.txt";
        // String outputFile = "/Users/zhangle/dev/jingyou/code.txt";
        //
        // trim(inputFile, outputFile);
        // Long a = null;
        // long b = 12345l;
        // System.out.println("equals? " + (a == b));
//        System.out.println(System.currentTimeMillis());
//        System.out.println(UUID.randomUUID());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String template = "=IF(工作量细化!%s=元数据!B2,元数据!C2,\n" + 
                "IF(工作量细化!%s=元数据!B3,元数据!C3,\n" + 
                "IF(工作量细化!%s=元数据!B4,元数据!C4,\n" + 
                "IF(工作量细化!%s=元数据!B5,元数据!C5,\n" + 
                "IF(工作量细化!%s=元数据!B6,元数据!C6,\n" + 
                "IF(工作量细化!%s=元数据!B7,元数据!C7,\n" + 
                "IF(工作量细化!%s=元数据!B8,元数据!C8,\n" + 
                "IF(工作量细化!%s=元数据!B9,元数据!C9,\n" + 
                "IF(工作量细化!%s=元数据!B10,元数据!C10,0)))))))))*%s";
        for (int i = 5; i < 180; i++) {
            System.out.println("=====================================");
            System.out.println(String.format(template, "D"+i, "D"+i, "D"+i, "D"+i, "D"+i, "D"+i, "D"+i, "D"+i, "D"+i, "E"+i));
        }
        
//        try {
//            System.out.println(sdf.parse("2017-05-03 19:30:00").getTime());
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.out.println(sdf.format(new java.util.Date(1477929600000L)));
//        System.out.println(sdf.format(new java.util.Date(1479281867854L)));
////        testNumber();
////        JSONObject json = JSONObject.fromObject("{\"subject\":20,\"isKnowledge\":false,\"section\":\"xxx\",\"selection\":[]}");
////        JSONArray arr = json.optJSONArray("selection");
////        System.out.println("arr is:" + arr);
////        double fee = 10.0;
////        if (fee >= 30) {
////            System.out.println(3);
////        } else if (fee >= 10) {
////            System.out.println(2);
////        } else if (fee >= 5) {
////            System.out.println(1);
////        }
//        String buildingJson = "{\n" +
//                "    \"id\":\"233\",\n" +
//                "    \"name\":\"2号楼\",\n" +
//                "    \"floorShapeList\":\n" +
//                "    [\n" +
//                "        {\n" +
//                "            \"id\":\"2331\",\n" +
//                "            \"name\":\"1层\",\n" +
//                "            \"bgImageUrl\":\"http://static.octopusdio.com/floor.jpg\",\n" +
//                "            \"bound\":{\n" +
//                "                \"left\":0,\n" +
//                "                \"top\":0,\n" +
//                "                \"right\":1280,\n" +
//                "                \"bottom\":696\n" +
//                "            },\n" +
//                "            \"apartmentShapeList\":[\n" +
//                "                {\n" +
//                "                    \"id\":\"23311\",\n" +
//                "                    \"name\":\"2101\",\n" +
//                "                    \"available\":true,\n" +
//                "                    \"bound\":{\n" +
//                "                        \"left\":170,\n" +
//                "                        \"top\":180,\n" +
//                "                        \"right\":370,\n" +
//                "                        \"bottom\":280\n" +
//                "                    }\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"id\":\"23312\",\n" +
//                "                    \"name\":\"2102\",\n" +
//                "                    \"available\":true,\n" +
//                "                    \"bound\":{\n" +
//                "                        \"left\":425,\n" +
//                "                        \"top\":350,\n" +
//                "                        \"right\":625,\n" +
//                "                        \"bottom\":450\n" +
//                "                    }\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"id\":\"23313\",\n" +
//                "                    \"name\":\"2103\",\n" +
//                "                    \"available\":false,\n" +
//                "                    \"bound\":{\n" +
//                "                        \"left\":642,\n" +
//                "                        \"top\":350,\n" +
//                "                        \"right\":842,\n" +
//                "                        \"bottom\":450\n" +
//                "                    }\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"id\":\"23314\",\n" +
//                "                    \"name\":\"2104\",\n" +
//                "                    \"available\":true,\n" +
//                "                    \"bound\":{\n" +
//                "                        \"left\":900,\n" +
//                "                        \"top\":180,\n" +
//                "                        \"right\":1100,\n" +
//                "                        \"bottom\":280\n" +
//                "                    }\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        }\n" +
//                "    ]\n" +
//                "}";
//        System.out.println(buildingJson);
    }

    private static void trim(String inFile, String outFile) {
        try {
            FileReader fr = new FileReader(new File(inFile));
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (null != line) {
                if (line.trim().length() > 0) {
                    String[] codes = line.split(",");
                    for (int i = 0; i < codes.length; i++) {
                        if (codes[i].trim().length() > 0) {
                            String code = codes[i].trim();
                            int unicode = Integer.parseInt(code.substring(2),
                                    16);
                            sb.append(String.valueOf(unicode));
                            sb.append(',');
                        }
                    }
                }
                line = br.readLine();
            }
            br.close();
            fr.close();

            FileWriter fw = new FileWriter(new File(outFile));
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sb.toString());
            bw.close();
            fw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void convertChar(String inFile, String outFile) {
        try {
            FileReader fr = new FileReader(new File(inFile));
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (null != line) {
                if (line.trim().length() > 0) {
                    String[] codes = line.split(",");
                    for (int i = 0; i < codes.length; i++) {
                        if (codes[i].trim().length() > 0) {
                            String code = codes[i].trim();
                            int unicode = Integer.parseInt(code.substring(2),
                                    16);
                            char c = (char) unicode;
                            sb.append(c);
                        }
                    }
                }
                line = br.readLine();
            }
            FileWriter fw = new FileWriter(new File(outFile));
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sb.toString());
            bw.close();
            fw.close();
            br.close();
            fr.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
