package com.facedamon.smart.system.domain;

import com.facedamon.smart.common.annotation.Excel;
import com.facedamon.smart.common.base.BaseEntity;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 用户bean
 * @Author: facedamon
 * @CreateDate: 2018/9/30 下午9:12
 * @UpdateUser: facedamon
 * @UpdateDate: 2018/9/30 下午9:12
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Alias("User")
public class User extends BaseEntity {

    /**
     * 用户ID
     */
    @Excel(name = "用户编号")
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门父ID
     */
    private Long parentId;

    /**
     * 登录名称
     */
    @Excel(name = "登录名称")
    private String loginName;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称")
    private String userName;

    /**
     * 用户邮箱
     */
    @Excel(name = "邮箱")
    private String email;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    private String phonenumber;

    /**
     * 用户性别
     */
    @Excel(name = "性别" ,isDict = true,dictType = "sys_user_sex")
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐加密
     */
    private String salt;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "用户状态",isDict = true,dictType = "sys_normal_disable")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登陆IP
     */
    private String loginIp;

    /**
     * 最后登陆时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;

    /**
     * 角色集合
     */
    private List<Role> roles;

    /**
     * 角色组
     */
    private Long[] roleIds;

    /**
     * 岗位组
     */
    private Long[] postIds;

    private Dept dept;

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

}