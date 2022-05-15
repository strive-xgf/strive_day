package com.xgf.util;

import com.xgf.collection.AssemblyUtil;
import com.xgf.designpattern.structure.proxy.Employee;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xgf
 * @create 2022-01-17 10:26
 * @description
 **/

public class AssemblyUtilTest {

    private <T> void printList(List<T> list) {
        list.forEach(p -> System.out.println(p.toString()));
    }


    @Test
    public void testDistinctByKeys() {

        List<Employee> employeeList = new ArrayList<>();

        employeeList.addAll(Arrays.asList(
                Employee.builder().employeeName("夏1").phone("111").desc("Java高级工程师").source("xxxHeadHunter").build(),
                Employee.builder().employeeName("赵2").phone("222").desc("Python工程师").source("xxxHeadHunter").build(),
                Employee.builder().employeeName("张3").phone("333").desc("Java架构师").source("xxxHeadHunter").build(),
                Employee.builder().employeeName("李4").phone("444").desc("Java实习生").source("xxxHeadHunter").build(),
                Employee.builder().employeeName("王5").phone("555").desc("Java讲师").source("xxxHeadHunter").build(),

                Employee.builder().employeeName("夏1").phone("111").desc("Java高级工程师").source("yyyHeadHunter").build(),
                Employee.builder().employeeName("张3").phone("333").desc("Java讲师").source("yyyHeadHunter").build(),
                Employee.builder().employeeName("赵2").phone("666").desc("Python工程师").source("yyyHeadHunter").build(),
                Employee.builder().employeeName("李4").phone("777").desc("Java架构师").source("yyyHeadHunter").build(),
                Employee.builder().employeeName("王5").phone("888").desc("Java实习生").source("yyyHeadHunter").build()
        ));
        printList(employeeList);

        List<Employee> distinctList = employeeList.stream().filter(Objects::nonNull).filter(AssemblyUtil.distinctByCondition(Employee::getEmployeeName, Employee::getPhone)).collect(Collectors.toList());
        System.out.println("\n\n name + phone ======");
        printList(distinctList);

        List<Employee> distinctNameList = employeeList.stream().filter(Objects::nonNull).filter(AssemblyUtil.distinctByCondition(Employee::getEmployeeName)).collect(Collectors.toList());
        System.out.println("\n\n name ======");
        printList(distinctNameList);

        List<Employee> distinctPhoneList = employeeList.stream().filter(Objects::nonNull).filter(AssemblyUtil.distinctByCondition(Employee::getPhone)).collect(Collectors.toList());
        System.out.println("\n\n phone ====== ");
        printList(distinctNameList);


        List<Employee> distinctAssembleList = employeeList.stream().filter(Objects::nonNull).filter(AssemblyUtil.distinctByCondition(Arrays.asList(Employee::getEmployeeName, Employee::getPhone))).collect(Collectors.toList());
        System.out.println("\n\n name + phone  list ======");
        printList(distinctAssembleList);

    }




}
