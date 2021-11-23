package com.xgf.designpattern.structure.adapter;

import com.xgf.DemoApplication;
import com.xgf.designpattern.structure.adapter.impladapter.AbstractAdapter;
import com.xgf.designpattern.structure.adapter.impladapter.SourceSub1;
import com.xgf.designpattern.structure.adapter.impladapter.SourceSub2;
import com.xgf.designpattern.structure.adapter.obj.Source;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xgf
 * @create 2021-11-22 23:49
 * @description
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class AdapterTest {

    @Test
    public void testClassAdapter(){
        // 测试类适配器
        ClassAdapter classAdapter = new ClassAdapter();
        classAdapter.editTextFile();
        classAdapter.editWordFile();
        classAdapter.editExcelFile();
    }

    @Test
    public void testObjectAdapter(){
        // 测试对象适配器
        ObjectAdapter objectAdapter = new ObjectAdapter(new Source());
        objectAdapter.editTextFile();
        objectAdapter.editWordFile();
        objectAdapter.editExcelFile();
    }

    @Test
    public void testInterfaceAdapter(){
        // 测试对象适配器
        AbstractAdapter sourceSub1 = new SourceSub1();
        AbstractAdapter sourceSub2 = new SourceSub2();
        sourceSub1.editTextFile();
        sourceSub2.editWordFile();
        sourceSub2.editExcelFile();
    }

}
