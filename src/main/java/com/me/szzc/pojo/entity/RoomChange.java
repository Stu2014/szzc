package com.me.szzc.pojo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 选房类
 * Created by bbfang on 2019/7/27.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomChange extends BaseRowModel implements Serializable {

    /**
     * 主键自增长id
     */
    private Long id;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "房源项目", index = 1)
    private String name;

    /**
     * 房号
     */
    @ExcelProperty(value = "房号", index = 2)
    private String number;

    /**
     * 栋
     */
    private String ridgepole;

    /**
     * 单元
     */
    private String unit;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 号
     */
    private String mark;

    /**
     * 面积
     */
    @ExcelProperty(value = "面积", index = 3)
    private String area;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价", index = 4)
    private String unitPrice;

    /**
     * 总价
     */
    @ExcelProperty(value = "总价", index = 5)
    private String totalPrice;

    /**
     * 点房人
     */
    private String choosePeople;

    /**
     * 所属片区
     */
    private String district;

    /**
     * 分配房源
     */
    @ExcelProperty(value = "分配征收项目", index = 6)
    private String assignedProject;

    /**
     * 房源平台
     */
    @ExcelProperty(value = "提供房源平台", index = 7)
    private String housingPlatform;
}
