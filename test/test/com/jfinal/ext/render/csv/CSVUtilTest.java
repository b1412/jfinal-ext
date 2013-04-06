package test.com.jfinal.ext.render.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.render.csv.CsvUtil;

public class CSVUtilTest {
    public static void main(String[] args) {
        String[] str1 = { "a1", "b1", "c1", "d1" };
        String[] str2 = { "a2", "b2", "c2", "d2" };
        String[] str3 = { "a3", "b3", "c3", "d3" };
        List<List<String>> data = new ArrayList<List<String>>();
        // data.add(str1);
        // data.add(str2);
        // data.add(str3);
        List<String> arr1 = Arrays.asList(str1);
        List<String> arr2 = Arrays.asList(str2);
        List<String> arr3 = Arrays.asList(str3);
        data.add(arr1);
        data.add(arr2);
        data.add(arr3);
        List<String> header = new ArrayList<String>();
        header.add("列数1");
        header.add("列数2");
        header.add("列数3");
        List<Map<Integer, String>> data2 = new ArrayList<Map<Integer, String>>();
        Map<Integer, String> map1 = new HashMap<Integer, String>();
        map1.put(111, "map11");
        map1.put(222, "map12");
        map1.put(333, "map13");
        Map<Integer, String> map2 = new HashMap<Integer, String>();
        map2.put(111, "map21");
        map2.put(222, "map22");
        map2.put(333, "map23");
        data2.add(map1);
        data2.add(map2);

        List<Integer> columns = new ArrayList<Integer>();
        columns.add(111);
        columns.add(333);
        System.out.println(CsvUtil.createCSV(header, data2, null));
    }
}
