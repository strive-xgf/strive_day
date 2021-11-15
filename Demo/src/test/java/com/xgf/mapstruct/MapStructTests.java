package com.xgf;

import cn.hutool.core.bean.BeanUtil;
import com.xgf.bean.*;
import com.xgf.bean.User;
import com.xgf.convert.MapStructConvert;
import com.xgf.create.CreateRandomObjectUtil;
import constant.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

/**
 * @author xgf
 * @create 2021-10-26 11:33
 * @description MapStructTest
 **/

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
@SpringBootTest
//@ComponentScan(basePackages = "com.xgf.convert")
public class MapStructTests {

    @Resource
    private MapStructConvert mapStructConvert;

    @Test
    public void testMapStruct(){
        User object = CreateRandomObjectUtil.createData(User.class);
        Person person = CreateRandomObjectUtil.createData(Person.class);
        WorkInfo workInfo = CreateRandomObjectUtil.createData(WorkInfo.class);
//        User user1 = mapStructConvert.copyBean(object);
        User user1 = mapStructConvert.personWorkToUser(person, workInfo, new BigDecimal("123"));
        System.out.println(user1);

    }

    @Test
    public void testEnum(){

    }


    @Test
    public void testCopyData() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        compareCopy(User.class, UserDTO.class, 1);
        System.out.println();
        compareCopy(User.class, UserDTO.class, 100);
        System.out.println();
        compareCopy(User.class, UserDTO.class, 10000);
        System.out.println();
        compareCopy(User.class, UserDTO.class, 1000000);
        System.out.println();
        compareCopy(User.class, UserDTO.class, 10000000);

    }

    @Test
    public void test(){

        GradeObject gradeObject = new GradeObject();
        NumberObject numberObject = new NumberObject();
        numberObject.setNumberEnum(NumberEnum.FIVE);
        numberObject.setGradeCodeEnum(GradeCodeEnum2.GRADE_A);
        System.out.println(gradeObject);
//        BeanUtils.copyProperties(gradeObject, numberObject);
        BeanUtils.copyProperties(numberObject, gradeObject);
        System.out.println(gradeObject);
        System.out.println(gradeObject.getGradeEnum());

        System.out.println(mapStructConvert.enumCopy2(numberObject));
        System.out.println(gradeObject);

    }

    /**
     * 比较复制对象耗时
     * @param sourceParam 对象
     * @param n 复制次数
     */
    public <T, V> void compareCopy(Class<T> sourceParam, Class<V> targetParam, int n) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {

        //  创建随机对象
        Object source = CreateRandomObjectUtil.createData(sourceParam);
        Object target = targetParam.newInstance();


        // mapStruct
        testMapStruct(n, source, target);

        // hutools 糊涂工具 BenUtil 复制
        testHutoolsBeanUtil(n, source, target);

        // spring 的 BeanUtils
        testSpringBeanUtils(n, source, target);

        // apache 的 BeanUtils
        testApacheBeanUtils(n, source, target);

        // apache 的 PropertyUtils
        testApachePropertyUtils(n, source, target);
    }


    /**
     * commons.beanutils 复制对象
     */
    private void testApachePropertyUtils(int n, Object source, Object target) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            PropertyUtils.copyProperties(source, target);
        }
//        System.out.println("target = " + target.getClass() + JSON.toJSONString(target));
        System.out.println("apache的PropertyUtils复制" + source.getClass() + "对象，" + n + "次所花时间为：" + (System.currentTimeMillis() - start) + "ms");
    }


    private void testApacheBeanUtils(int n, Object source, Object target) throws InvocationTargetException, IllegalAccessException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            org.apache.commons.beanutils.BeanUtils.copyProperties(source, target);
        }
//        System.out.println("target = " + target.getClass() + JSON.toJSONString(target));
        System.out.println("apache的BeanUtils复制" + source.getClass() + "对象，" + n + "次所花时间为：" + (System.currentTimeMillis() - start) + "ms");
    }

    private void testSpringBeanUtils(int n, Object source, Object target) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            BeanUtils.copyProperties(source, target);
        }
//        System.out.println("target = " + target.getClass() + JSON.toJSONString(target));
        System.out.println("spring的BeanUtils复制" + source.getClass() + "对象，" + n + "次所花时间为：" + (System.currentTimeMillis() - start) + "ms");
    }

    private void testHutoolsBeanUtil(int n, Object source, Object target) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            BeanUtil.copyProperties(source, target);
        }
//        System.out.println("target = " + target.getClass() + JSON.toJSONString(target));
        System.out.println("hutools的BeanUtil复制" + source.getClass() + "对象，" + n + "次所花时间为：" + (System.currentTimeMillis() - start) + "ms");
    }

    private void testMapStruct(int n, Object source, Object target) {
        long start = System.currentTimeMillis();
        User sourceUser = (User) source;
        UserDTO targetUser = (UserDTO) target;
        for (int i = 0; i < n; i++) {
            targetUser = mapStructConvert.copyBean(sourceUser);
        }
//        System.out.println("target = " + target.getClass() + JSON.toJSONString(target));
        System.out.println("mapstruct复制" + source.getClass() + "对象，" + n + "次所花时间为：" + (System.currentTimeMillis() - start) + "ms");
    }

}