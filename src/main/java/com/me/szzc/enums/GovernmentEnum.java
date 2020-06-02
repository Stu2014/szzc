package com.me.szzc.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.szzc.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 政府部门
 * @author luwei
 * @date 2019/11/18
 */
@Slf4j
public enum GovernmentEnum {

    ROOT("root", "admin"),
    WCQ("wcq", "武昌区",  GovernmentEnum.ROOT.getCode()),
    BJSWQ("A001", "滨江商务区", GovernmentEnum.WCQ.getCode()),
    MLJ("A001001", "明伦街", GovernmentEnum.BJSWQ.getCode()),

    GCC("B001", "古城区", GovernmentEnum.WCQ.getCode()),
    ZYC("B001001", "紫阳村", GovernmentEnum.GCC.getCode()),
    XCH("B001002", "西城壕", GovernmentEnum.GCC.getCode()),

    HZJRC("C001", "华中金融城", GovernmentEnum.WCQ.getCode()),
    DZRGS("C001001", "电车二公司南侧地块", GovernmentEnum.HZJRC.getCode()),


    QKQ("qkq", "硚口区", GovernmentEnum.ROOT.getCode()),
    ;

    private String code;

    private String name;

    private String parentCode;

    /**项目管理区 一级及项目**/
    private static final Map<String, String> mgtMap = new HashMap<>();

    /**最具体的项目**/
    private static final Map<String, String> projectMap = new HashMap<>();

    private static final Map<String, GovernmentEnum> allNameMap = new HashMap<>();

    //所有
    private static final List<GovernmentEnum> allList = new ArrayList<>();

    static {
        for (GovernmentEnum governmentEnum : GovernmentEnum.values()) {
            if (governmentEnum.getCode().length() >= 4) {
                mgtMap.put(governmentEnum.getCode(), governmentEnum.getName());
            }


            if (governmentEnum.getCode().length() > 4) {
                projectMap.put(governmentEnum.getCode(), governmentEnum.getName());
            }

            allNameMap.put(governmentEnum.getName(), governmentEnum);
            allList.add(governmentEnum);
        }
    }


    GovernmentEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    GovernmentEnum(String code, String name, String parentCode) {
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public String getParentCode() {
        return parentCode;
    }

    public static String getNameByCode(String code) {
        String desc = mgtMap.get(code);
        return desc;
    }

    public static Map<String, String> getMgtMap() {
        return mgtMap;
    }

    public static Map<String, String> getProjectMap() {
        return projectMap;
    }

    public static List<Map<String, String>> queryAll() {
        List<Map<String, String>> list = new ArrayList<>();
        for (GovernmentEnum t : GovernmentEnum.values()) {
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("code", t.getCode());
            tempMap.put("test", t.getName());
            list.add(tempMap);
        }
        return list;
    }


    public static void main(String[] args) {
       /* JSONObject object = getScope("admin");
        System.out.println("权限：" + JSONObject.toJSONString(object, true));*/

        List<String> list = getProjectByScope("硚口区");
        System.out.println("项目：" + JSONObject.toJSONString(list, true));
    }

    //根据范围名称，获取管辖权限下的所有项目列表
    public static List<String> getProjectByScope(String scopeName){
        List<String> list = new ArrayList<>();

        GovernmentEnum governmentEnum = allNameMap.get(scopeName);
        if(governmentEnum == null){
            log.error("获取权限信息参数错误, scopeName:{}", scopeName);
            return null;
        }


        JSONArray jsonArray = getChild(governmentEnum.getCode());
        getProejctName(jsonArray, list);
        return list;
    }

    private static void getProejctName(JSONArray jsonArray, List<String> list){
        if(jsonArray == null ||  jsonArray.size() == 0){
            return;
        }
        for(int i=0;i<jsonArray.size();i++){
            JSONObject object = jsonArray.getJSONObject(i);
            if(object.getString("code").length() > 4){
                list.add(object.getString("name"));
            }

            JSONArray arr = object.getJSONArray("child");
            if(arr != null){
                getProejctName(arr, list);
            }
        }
    }


    /**
     * 根据范围名称，获取对应的权限信息
     * @param scopeName  admin 、武昌区、硚口区
     * @return
     */
    public static JSONObject getScope(String scopeName){
        if(StringUtils.isBlank(scopeName)){
            return null;
        }

        GovernmentEnum governmentEnum = allNameMap.get(scopeName);
        if(governmentEnum == null){
            log.error("获取权限信息参数错误, scopeName:{}", scopeName);
            return null;
        }

        JSONObject object = new JSONObject();
        object.put("code", governmentEnum.getCode());
        object.put("name", governmentEnum.getName());

        JSONArray jsonArray = getChild(governmentEnum.getCode());
        if(jsonArray != null && jsonArray.size() > 0 ){
            object.put("child", jsonArray);
        }

        return object;
    }

    /**根据父元素获取所有嵌套的子元素**/
    private static JSONArray getChild(String parentCode) {
        JSONArray arr = new JSONArray();
        for (GovernmentEnum gov : allList) {
            if (gov.getParentCode() == null) {
                continue;
            }
            if (gov.getParentCode().equals(parentCode)) {
                JSONObject object = new JSONObject();
                object.put("code", gov.getCode());
                object.put("name", gov.getName());
                JSONArray child = getChild(gov.getCode());
                if (child != null && child.size() > 0) {
                    object.put("child", child);
                }
                arr.add(object);
            }
        }

        return arr;
    }
}
