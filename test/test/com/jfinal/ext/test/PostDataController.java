package test.com.jfinal.ext.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import com.jfinal.core.Controller;

public class PostDataController extends Controller {
    public void index() throws Exception {
        String xmlReq = "";
        DataInputStream dis;
        try {
            dis = new DataInputStream(getRequest().getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
            String lines = "";
            while ((lines = br.readLine()) != null) {
                xmlReq += lines;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("post data is: " + xmlReq);
        renderNull();
    }
}
