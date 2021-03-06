package com.facedamon.smart.system.domain;

import com.facedamon.smart.common.base.BaseEntity;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 菜单bean
 * @Author: facedamon
 * @CreateDate: 2018/9/30 下午9:05
 * @UpdateUser: facedamon
 * @UpdateDate: 2018/9/30 下午9:05
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@Alias("Menu")
public class Menu extends BaseEntity {
    /**
     * 菜单ID
     */
    private Long menuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 父菜单名称
     */
    private String parentName;
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 显示顺序
     */
    private String orderNum;
    /**
     * 菜单URL
     */
    private String url;
    /**
     * 类型:0目录,1菜单,2按钮
     */
    private String menuType;
    /**
     * 菜单状态:0显示,1隐藏
     */
    private String visible;
    /**
     * 权限字符串
     */
    private String perms;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 子菜单
     */
    private List<Menu> children = new ArrayList<>();
}