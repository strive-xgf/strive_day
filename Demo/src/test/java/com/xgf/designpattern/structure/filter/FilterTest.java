package com.xgf.designpattern.structure.filter;

import com.xgf.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author xgf
 * @create 2021-12-19 23:23
 * @description 过滤器模式
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class FilterTest {

    @Resource(name = "filterAndAssembly")
    private FilterAndAssembly filterAndAssembly;

    @Resource(name = "filterOrAssembly")
    private FilterOrAssembly filterOrAssembly;

    @Resource(name = "filterMarried")
    private FilterCondition filterMarried;

    @Resource(name = "filterSingle")
    private FilterCondition filterSingle;

    @Resource(name = "filterMale")
    private FilterCondition filterMale;

    @Resource(name = "filterWoman")
    private FilterCondition filterWoman;

    @Test
    public void test() {

        List<Person> personList = Arrays.asList(
                Person.builder().name("person_111").age(18).gender(0).maritalStatus(0).build(),
                Person.builder().name("person_222").age(19).gender(0).maritalStatus(2).build(),
                Person.builder().name("person_333").age(20).gender(1).maritalStatus(0).build(),
                Person.builder().name("person_444").age(21).gender(1).maritalStatus(1).build(),
                Person.builder().name("person_555").age(22).gender(0).maritalStatus(1).build(),
                Person.builder().name("person_666").age(16).gender(0).maritalStatus(0).build(),
                Person.builder().name("person_777").age(17).gender(1).maritalStatus(0).build(),
                Person.builder().name("person_888").age(18).gender(0).maritalStatus(1).build(),
                Person.builder().name("person_999").age(21).gender(0).maritalStatus(0).build(),
                Person.builder().name("person_000").age(20).gender(-1).maritalStatus(0).build(),
                Person.builder().name("person_aaa").age(18).gender(0).maritalStatus(0).build(),
                Person.builder().name("person_bbb").age(28).gender(-1).maritalStatus(-1).build());

        System.out.println("====== filter male ======");
        toStringPrint(filterMale.executeFilter(personList));

        System.out.println("\n====== filter woman ======");
        toStringPrint(filterWoman.executeFilter(personList));

        System.out.println("\n====== filter single ======");
        toStringPrint(filterSingle.executeFilter(personList));

        System.out.println("\n====== filter married ======");
        toStringPrint(filterMarried.executeFilter(personList));

        System.out.println("\n====== filter woman and single ======");
        toStringPrint(filterAndAssembly.assemblyConditionExecute(personList, filterWoman, filterSingle));

        System.out.println("\n====== filter male and married ======");
        toStringPrint(filterAndAssembly.assemblyConditionExecute(personList, filterMale, filterMarried));

        System.out.println("\n====== filter woman or single ======");
        toStringPrint(filterOrAssembly.assemblyConditionExecute(personList, filterWoman, filterSingle));

        System.out.println("\n====== filter male or married ======");
        toStringPrint(filterOrAssembly.assemblyConditionExecute(personList, filterMale, filterMarried));

        System.out.println("\n====== filter (male or woman) and single ======");
        toStringPrint(filterAndAssembly.assemblyConditionExecute(filterOrAssembly.assemblyConditionExecute(personList, filterMale, filterWoman), filterSingle));
    }

    private <T> void toStringPrint(List<T> paramList) {
        paramList.forEach(p -> System.out.println(p.toString()));
    }


}
