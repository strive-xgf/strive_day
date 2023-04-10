package com.xgf.file;

import com.xgf.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author strive_day
 * @create 2023-04-08 3:46
 * @description
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class ImageFileUtilTest {


    @Test
    public void test_imgDpiModify() throws IOException {
        ImageFileUtil.imgDpiModify(
                "H:\\000000plan\\0001.jpg",
                "H:\\000000plan\\dealImg\\0002.jpg",
                160,
                160,
                false
        );
    }



    @Test
    public void test_imgDpiModifyByDir() {
        ImageFileUtil.imgDpiModifyByDir(
                "F:\\wqq",
                "H:\\wqq",
                null,
                3840,
                2160,
                true,
                false,
                false
        );
    }

}
