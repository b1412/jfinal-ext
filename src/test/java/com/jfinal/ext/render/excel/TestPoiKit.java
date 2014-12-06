package com.jfinal.ext.render.excel;

import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by kid on 14-12-4.
 */
public class TestPoiKit {
    public static void main(String[] args) throws IOException {
        List<Record> data = Lists.newArrayList();
        for(int i = 0; i < 65538;i++){
            Record record = new Record();
            record.set("name","leonzhou"+i);
            data.add(record);
        }
        Workbook workbook = PoiKit.data(data).version(PoiKit.VERSION_2003).sheetName("data").columns("name").headers("A").export();
        String pathname = PathKit.getRootClassPath() + "/excel.xls";
        System.out.println(pathname);
        workbook.write(new FileOutputStream(new File(pathname)));
    }
}
