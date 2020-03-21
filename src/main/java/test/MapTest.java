/**
 * 
 */
package test;

/**
 * @author zhangle
 *
 */
public class MapTest {
    public static void main(String[] args) {
        String json = WebUtils.getJsonStrFromGetUrl("http://api.map.baidu.com/geocoding/v3/?address=北京协和医院&output=json&ak=u5qBmrogGrklgE2aNn5RMhcgzGVcYhVH&ret_coordtype=gcj02ll");
        System.out.println(json);
        json = WebUtils.getJsonStrFromGetUrl("http://api.map.baidu.com/reverse_geocoding/v3/?ak=u5qBmrogGrklgE2aNn5RMhcgzGVcYhVH&output=json&coordtype=wgs84ll&location=39.988518,116.366471");
        System.out.println(json);
    }
}
