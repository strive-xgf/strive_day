package com.xgf.designpattern.create.singleton;

import com.xgf.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xgf
 * @create 2021-11-17 15:55
 * @description
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class SingletonTest {

    @Resource
    private SingletonSelector singletonSelector;

    /**
     * 创建对象
     */
    List<Singleton> singletonList = new ArrayList<>();

    Map<String, List<Singleton>> singletonListMap = new HashMap<>();

    @Test
    public void test() throws InterruptedException {
        // 创建固定数量的线程池
        ExecutorService executor = Executors.newFixedThreadPool(10);

//        execute(1000, SingletonTypeEnum.ENUM_SINGLETON, executor);
//        Thread.sleep(3000);
        execute(100000, SingletonTypeEnum.INNER_CLASS_SINGLETON, executor);
////        Thread.sleep(3000);
//        execute(100000, SingletonTypeEnum.DCL_SINGLETON, executor);
////        Thread.sleep(3000);
//        execute(100000, SingletonTypeEnum.HUNGRY_SINGLETON, executor);
////        Thread.sleep(3000);
//        execute(100000, SingletonTypeEnum.LAZY_SINGLETON, executor);
////        Thread.sleep(3000);
//        execute(10000, SingletonTypeEnum.LAZY_SINGLETON_NO_LOCK, executor);

        // 关闭线程池
        executor.shutdown();

        if (!executor.isTerminated()) {
            System.out.println("no isTerminated");
        }
        System.out.println("singletonList = " + singletonList);
    }

    /**
     * todo 测试方法需要修改，有问题
     * @param executeNum
     * @param singletonTypeEnum
     * @param executor
     * @return
     */

    private List<Singleton> execute(int executeNum, SingletonTypeEnum singletonTypeEnum, ExecutorService executor){

        for (int i = 0; i < executeNum; i++) {
            // 创建线程并添加到线程池中, 并启动运行线程
            int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Singleton singletonInstance = singletonSelector.getSingletonInstance(singletonTypeEnum);
                    if(! singletonList.contains(singletonInstance)){
                        // 不同实例就加入到数组中
                        SingletonTest.this.singletonList.add(singletonInstance);
                    }
//                    System.out.println("线程名: " + Thread.currentThread().getName() + ", 执行次数: " + finalI + ", 创建对象: " + singletonInstance + ", 活动线程数 : " + ((ThreadPoolExecutor)executor).getActiveCount());
                }
            });
        }
        System.out.println("执行创建线程次数 : " + executeNum + ", 单例类型 : " + singletonTypeEnum);
        System.out.println("  >>>>>> 创建对象数 : " + singletonList.size() + ", 创建对象列表列表 : " + singletonList);
        System.out.println();
        return singletonList;

    }


}
