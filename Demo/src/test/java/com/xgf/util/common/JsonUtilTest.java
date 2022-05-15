package com.xgf.util.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xgf.bean.Hobby;
import com.xgf.bean.User;
import com.xgf.bean.WorkInfo;
import com.xgf.common.JsonUtil;
import lombok.Data;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author xgf
 * @create 2022-05-14 14:53
 * @description json 测试类
 **/

public class JsonUtilTest {


    private Map<String, User> mockStr2UserMap() {
        Map<String, User> map = new HashMap<String, User>();
        map.put("user1", User.builder()
                .userUuid("userUuid1")
                .stringList(Arrays.asList("1", "2", "3")).age(18)
                .workInfo(WorkInfo.builder().workUuid("workUuid1").workContent("workContent").build())
                .updatedTime(new Date())
                .build());

        map.put("user2", User.builder()
                .userUuid("userUuid2")
                .stringList(Arrays.asList("6", "7", "8")).age(22)
                .workInfoList(Arrays.asList(WorkInfo.builder().workUuid("workUuid1").workContent("workContent111").build(),
                        WorkInfo.builder().workUuid("workUuid2").workContent("workContent222").build(),
                        WorkInfo.builder().workUuid("workUuid3").workContent("workContent333").build()))
                .bigDecimal(new BigDecimal("9999.9999"))
                .hobby(Hobby.builder().hobbyType("hobbyType").hobbyCount(2).build())
                .createdTime(new Date())
                .build());
        return map;
    }

    /**
     * 模拟  数据
     * @return json数据
     */
    private String mockListStr2ObjMapData() {
        Map<String, User> map1 = mockStr2UserMap();

        Map<String, User> map2 = new HashMap<String, User>();
        map2.put("userMap2", User.builder()
//                .userUuid("userUuidMap2")
                        .age(22)
                .build());

        List<Map<String, User>> str2UserMapList = Arrays.asList(map1, map2);

        return JSON.toJSONString(str2UserMapList);
    }


    @Test
    public void testToListStr2ObjMap() {
        String str2UserMapListJson = mockListStr2ObjMapData();
//        System.out.println("str2UserMapListJson = " + str2UserMapListJson);
//        List<Map<String, Object>> str2ObjMapList = JsonUtil.toListStr2ObjMap(str2UserMapListJson);
//        System.out.println("str2ObjMapList = " + JsonUtil.toJsonString(str2ObjMapList));
//        List<Map<String, User>> str2UserMapList = JsonUtil.toListStr2ObjMap(str2UserMapListJson, User.class);
//        System.out.println("str2UserMapList = " + JsonUtil.toJsonString(str2UserMapList));
//        List<Map<String, WorkInfo>> str2WorkMapList = JsonUtil.toListStr2ObjMap(str2UserMapListJson, WorkInfo.class);
//        System.out.println("str2WorkMapList = " + JsonUtil.toJsonString(str2WorkMapList));

//        List<Map<String, Hobby>> str2HobbyMapList = JsonUtil.toListStr2ObjMap(str2UserMapListJson, Hobby.class);
//        System.out.println("str2HobbyMapList = " + JsonUtil.toJsonString(str2HobbyMapList));
////        System.out.println(str2HobbyMapList.get(0).get("user1"));
////        System.out.println(str2HobbyMapList.get(0).get("user1").getHobbyContent());
//
//        List<Map<String, Hobby>> str2hobbyList = JSON.parseObject(str2UserMapListJson, new TypeReference<List<Map<String, Hobby>>>() {});
////        Map<String, Hobby> stringHobbyMap = str2hobbyList.get(0);
////        Hobby hobby1 = stringHobbyMap.get("user1");
//        System.out.println("str2hobbyList = " + JsonUtil.toJsonString(str2hobbyList));
//
//
//        List<Map<String, User>> maps = JsonUtil.toListMap(str2UserMapListJson, String.class, User.class);
//        System.out.println("maps = " + JsonUtil.toJsonString(maps));
//        List<Map<BBBBBB, User>> maps2 = JsonUtil.toListMap(str2UserMapListJson, BBBBBB.class, User.class);
//        System.out.println("maps2 = " + JsonUtil.toJsonString(maps2));
//        System.out.println(JSON.toJSONString(maps2.get(0).keySet()));
//        ArrayList<BBBBBB> bbbbbbs = new ArrayList<>(maps2.get(0).keySet());
//        System.out.println(bbbbbbs);
//        System.out.println(bbbbbbs.get(0));
//        BBBBBB bbbbbb = bbbbbbs.get(0);
//        System.out.println(bbbbbb.getIb());
//        System.out.println(bbbbbb.getSb());

        List<Map<String, Hobby>> maps1 = JSON.parseObject(str2UserMapListJson, new TypeReference<List<Map<String, Hobby>>>() {});
        System.out.println(JSON.toJSONString(maps1));


    }


    @Test
    public void testToObject() {
        User user = User.builder()
                .userUuid("userUuid1")
                .stringList(Arrays.asList("1", "2", "3")).age(18)
                .workInfo(WorkInfo.builder().workUuid("workUuid1").workContent("workContent").build())
                .updatedTime(new Date())
                .build();

        User user1 = JsonUtil.toObject(JsonUtil.toJsonString(user), User.class);
        System.out.println("user1 = " + JsonUtil.toJsonString(user1));

        Hobby hobby1 = JsonUtil.toObject(JsonUtil.toJsonString(user), Hobby.class);
        System.out.println("hobby1 = " + JsonUtil.toJsonString(hobby1));

        AObjectData aObjectData = JsonUtil.toObject(JsonUtil.toJsonString(user), AObjectData.class);
        System.out.println("aObjectData = " + JsonUtil.toJsonString(aObjectData));

        // str 字符串 转对象
        AObjectData aObjectData2 = JsonUtil.toObject(JsonUtil.toJsonString("str"), AObjectData.class);
        System.out.println("aObjectData2 = " + JsonUtil.toJsonString(aObjectData2));
    }

    @Test
    public void testToArray() {
//        List<User> userList = Arrays.asList(User.builder().userUuid("userUuid1").build(), User.builder().userUuid("userUuid2").build());
        List<User> userList = Arrays.asList(User.builder().age(22).build(), User.builder().age(18).build());

            System.out.println("userList toArray = " + JsonUtil.toArray(JsonUtil.toJsonString(userList), User.class));
        // JSON 数据转换对象/数组等等，如果json字符串为null，转换为null，否则在json格式一致的情况下（对象转对象，数组转数组）会根据字段名称映射，如果没有相同字段，则会生成一个目标的空对象（初始化所有值都为null
        // 返回空对象，值都为null
        System.out.println("userList toArray = " + JsonUtil.toArray(JsonUtil.toJsonString(userList), Hobby.class));

        // {} 对象 转数组 报错
//        System.out.println("userList toArray = " + JsonUtil.toArray(JsonUtil.toJsonString(User.builder().age(19).build()), Hobby.class));

    }

    @Test
    public void testToArrayIncludeObj() {
        User user1 = User.builder().userUuid("userUuid1").build();
        User user2 = User.builder().userUuid("userUuid2").build();
        List<User> userList = Arrays.asList(user1, user2);

        System.out.println("userList toArrayIncludeObj = " + JsonUtil.toArrayIncludeObj(JsonUtil.toJsonString(user1), User.class));
        System.out.println("userList toArrayIncludeObj = " + JsonUtil.toArrayIncludeObj(JsonUtil.toJsonString(userList), User.class));

    }

    @Test
    public void testToMap() {
        HashMap<String, User> str2UserMap = new HashMap<String, User>() {{
            put("user111", User.builder().userUuid("userUuid1").age(18).build());
            put("user222", User.builder().userUuid("userUuid2").age(22).build());
        }};

        Map<String, User> stringUserMap = JSON.parseObject(JSON.toJSONString(str2UserMap), new TypeReference<Map<String, User>>() {});
        System.out.println("stringUserMap = " + JSON.toJSONString(stringUserMap));

        Map<String, AObjectData> stringAObjectDataMap = JSON.parseObject(JSON.toJSONString(str2UserMap), new TypeReference<Map<String, AObjectData>>() {});
        System.out.println("stringAObjectDataMap = " + JSON.toJSONString(stringAObjectDataMap));

        // 没有被强转 todo
        Map<String, AObjectData> stringAObjectDataMap1 = JsonUtil.toMap(JSON.toJSONString(str2UserMap), String.class, AObjectData.class);
        System.out.println("stringAObjectDataMap1 = " + JSON.toJSONString(stringAObjectDataMap1));

        Map<String, Object> map = JsonUtil.toMap(JSON.toJSONString(str2UserMap));
        System.out.println("map = " + JSON.toJSONString(map));


    }


    @Test
    public void test() {
        Map<String, User> str2UserMap = mockStr2UserMap();

        Map<String, User> map1 = JsonUtil.toMap(JsonUtil.toJsonString(str2UserMap), String.class, User.class);
        System.out.println("map1 = " + JsonUtil.toJsonString(map1));
        Map<AObjectData, User> map2 = JsonUtil.toMap(JsonUtil.toJsonString(str2UserMap), AObjectData.class, User.class);
        System.out.println("map2 = " + JsonUtil.toJsonString(map2));


    }

    /**
     * 测试 TypeReference 的使用
     */
    @Test
    public void testTypeReference() {

        // 此时 JSON 数据是一个对象User {}，满足 TypeReference 的泛型 AObjectData 也是一个对象，所以可以进行转换，基于字段映射
        // 因为 ObjectData 不存在json数据里面的 age 字段，所以返回一个空对象（AObjectData 初始化，所有值都为null）
        AObjectData aObjectData = JSON.parseObject(JSON.toJSONString(User.builder().age(22).build()), new TypeReference<AObjectData>() {});
        AObjectData aObjectData2 = JSON.parseObject(JSON.toJSONString(null), new TypeReference<AObjectData>() {});
        // 返回false, 此时 aObjectData 值为 ib=null, sb=null
        System.out.println(Objects.isNull(aObjectData));
        System.out.println("aObjectData = " + JSON.toJSONString(aObjectData));
        System.out.println(Objects.isNull(aObjectData2));
        System.out.println("aObjectData2 = " + JSON.toJSONString(aObjectData2));

        System.out.println(">>>>>>>>>>>>>>>>>>>");

        // 复杂数据格式赋值，首先保证 json 的数据格式 和需要转换的 TypeReference 泛型数据类型一直 [{}], 然后会根据字段映射来进行赋值
        // 如果不存在相同字段则返回空对象{}，如果json数据为null，则返回null
        List<HashMap<String, AObjectData>> str2AObjectMapList = Arrays.asList(
                new HashMap<String, AObjectData>() {
                    {
                    put("ia1", null);
                    put("ia2", AObjectData.valueOf(20, null));
                    put("ia3", AObjectData.valueOf(18, "ia3_str"));
                }},
                new HashMap<String, AObjectData>() {{
                    put("ia2_1", AObjectData.valueOf(22, "ia2_1_str"));
                }}
        );

        List<HashMap<String, BObjectData>> str2BObjectMapList =
                JSON.parseObject(JSON.toJSONString(str2AObjectMapList), new TypeReference<List<HashMap<String, BObjectData>>>() {});
        // 2 = [{"ia2":{"str":"ia2_str"}},{"ia2":{"str":"ia3_str"}}]
        System.out.println(str2BObjectMapList.size() + " = " + JSON.toJSONString(str2BObjectMapList));

        System.out.println(">>>>>>>> ");

        // 如果 json 数据格式，不满足转换条件 TypeReference 的泛型类型格式，则抛出异常 `com.alibaba.fastjson.JSONException: syntax error, expect {, actual string, pos 0`
        // 因为 "str" 的json字符串，转换为AObjectData 的{}对象格式，不满足数据格式，抛出异常
//        AObjectData aObjectData3 = JSON.parseObject(JSON.toJSONString("str"), new TypeReference<AObjectData>() {});
    }

    @Test
    public void testToListMap() {

        List<HashMap<String, AObjectData>> str2AObjectMapList = Arrays.asList(
                new HashMap<String, AObjectData>() {
                    {
                        put("ia1", null);
                        put("ia2", AObjectData.valueOf(20, null));
                        put("ia3", AObjectData.valueOf(18, "ia3_str"));
                    }},
                new HashMap<String, AObjectData>() {{
                    put("ia2_1", AObjectData.valueOf(22, "ia2_1_str"));
                }}
        );
        String str2AObjectMapListJson = JSON.toJSONString(str2AObjectMapList);

        List<Map<String, Hobby>> maps1 = JSON.parseObject(str2AObjectMapListJson, new TypeReference<List<Map<String, Hobby>>>() {});
        // 没有相同的字段， 所以映射的对象是空的 {}
        // maps1 = [{"ia3":{},"ia2":{}},{"ia2_1":{}}]
        System.out.println("maps1 = " + JSON.toJSONString(maps1));

        // 使用泛型方法，但是生成的结果就是 数据str2AObjectMapList 的值，没有变化 todo 不理解
        // todo 直接使用JSON.parseObject 的 TypeReference 来进行转换，可以得出正确的结果，但是包装一层泛型之后，转换就不生效了
        List<Map<String, Hobby>> maps2 = JsonUtil.toListMap(str2AObjectMapListJson, String.class, Hobby.class);
        // maps2 = [{"ia3":{"str":"ia3_str","ia":18},"ia2":{"ia":20}},{"ia2_1":{"str":"ia2_1_str","ia":22}}]
        System.out.println("maps2 = " + JSON.toJSONString(maps2));
        // 没有实现转换，还是原有的json字符串及其对象
        // {"str":"ia3_str","ia":18}
        System.out.println(maps2.get(0).get("ia3"));

    }


    @Test
    public void testToMap22() {

        HashMap<String, AObjectData> str2AObjectMap = new HashMap<String, AObjectData>() {
            {
                put("ia1", null);
                put("ia2", AObjectData.valueOf(20, null));
                put("ia3", AObjectData.valueOf(18, "ia3_str"));
            }
        };
        String str2AObjectMapJson = JSON.toJSONString(str2AObjectMap);

        Map<String, Hobby> map1 = JSON.parseObject(str2AObjectMapJson, new TypeReference<Map<String, Hobby>>() {});
        // 没有相同的字段， 所以映射的对象是空的 {}
        // map1 = {"ia3":{},"ia2":{}}
        System.out.println("map1 = " + JSON.toJSONString(map1));

        // todo 问题同上
        Map<String, Hobby> map2 = JsonUtil.toMap(str2AObjectMapJson, String.class, Hobby.class);
        // map2 = {"ia3":{"str":"ia3_str","ia":18},"ia2":{"ia":20}}
        System.out.println("map2 = " + JSON.toJSONString(map2));
        // 没有实现转换，还是原有的json字符串及其对象
        // {"str":"ia3_str","ia":18}
        System.out.println(map2.get("ia3"));

    }

    @Data
    static class AObjectData {
        private Integer ia;
        private String str;

        public static AObjectData valueOf(Integer ia, String str) {
            AObjectData result = new AObjectData();
            result.setIa(ia);
            result.setStr(str);
            return result;
        }
    }

    @Data
    static class BObjectData {
        private Integer ib;
        private String str;

        public static BObjectData valueOf(Integer ib, String str) {
            BObjectData result = new BObjectData();
            result.setIb(ib);
            result.setStr(str);
            return result;
        }
    }

}
