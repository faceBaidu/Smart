package com.facedamon.smart.project.system.role.service;

import com.facedamon.smart.common.constant.Constants;
import com.facedamon.smart.common.support.Convert;
import com.facedamon.smart.common.utils.security.ShiroUtils;
import com.facedamon.smart.project.system.role.domain.Role;
import com.facedamon.smart.project.system.role.domain.RoleDept;
import com.facedamon.smart.project.system.role.domain.RoleMenu;
import com.facedamon.smart.project.system.role.mapper.RoleDeptMapper;
import com.facedamon.smart.project.system.role.mapper.RoleMapper;
import com.facedamon.smart.project.system.role.mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description:    角色服务
 * @Author:         facedamon
 * @CreateDate:     2018/9/30 下午11:20
 * @UpdateUser:     facedamon
 * @UpdateDate:     2018/9/30 下午11:20
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleDeptMapper roleDeptMapper;

    /**
     * 根据角色条件分页查询角色信息
     * @param role
     * @return
     */
    @Override
    public List<Role> selectRoleList(Role role) {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 根据用户ID查询权限信息
     * @param userId
     * @return
     */
    @Override
    public Set<String> selectRoleKeys(Long userId) {
        List<Role> perms = roleMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (Role perm : perms){
            if (null != perm){
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询角色信息
     * @param userId
     * @return
     */
    @Override
    public List<Role> selectRoleByUserId(Long userId) {
        List<Role> perms = roleMapper.selectRolesByUserId(userId);
        List<Role> roles = selectRoleAll();
        for (Role role : roles){
            for (Role perm : perms){
                if (perm.getRoleId().longValue() == role.getRoleId().longValue()){
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * 查询所有角色
     * @return
     */
    @Override
    public List<Role> selectRoleAll() {
        return roleMapper.selectRoleList(Role.builder().build());
    }

    /**
     * 通过角色ID查询角色
     * @param roleId
     * @return
     */
    @Override
    public Role selectRoleById(Long roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     *  通过制定角色ID删除角色
     * @param roleId
     * @return
     */
    @Override
    public boolean deleteRoleById(Long roleId) {
        return roleMapper.deleteRoleById(roleId) > 0 ? true : false;
    }

    @Override
    public int deleteRoleByIds(String ids) throws Exception {
        Long[] roleIds = Convert.toLongArray(ids);
        for (Long roleId : roleIds){
            Role role = roleMapper.selectRoleById(roleId);
            //TODO 删除角色
        }
        return 0;
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @Override
    public int insertRole(Role role) {
        role.setCreateBy(ShiroUtils.getLoginName());
        roleMapper.insertRole(role);
        //TODO 刷新shiro权限缓存
        return insertRoleMenu(role);
    }

    /**
     * 更新角色
     * @param role
     * @return
     */
    @Override
    public int updateRole(Role role) {
        role.setUpdateBy(ShiroUtils.getLoginName());
        roleMapper.updateRole(role);
        //TODO 刷新shiro权限缓存
        /**
         * 角色菜单关系发生变化
         */
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        insertRoleMenu(role);
        return 0;
    }

    /**
     * 更新数据权限信息
     * @param role
     * @return
     */
    @Override
    public int updateRule(Role role) {
        role.setUpdateBy(ShiroUtils.getLoginName());
        roleMapper.updateRole(role);
        //TODO 刷新shiro权限缓存
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        return insertRoleDept(role);
    }

    /**
     * 校验角色名称是否唯一
     * @param role
     * @return
     */
    @Override
    public String checkRoleNameUnique(Role role) {
        Long roleId = role == null ? -1L : role.getRoleId();
        Role next = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (null != next && next.getRoleId().longValue() == roleId.longValue()){
            return Constants.ROLE_NAME_NOT_UNIQUE.getValue();
        }
        return Constants.ROLE_NAME_UNIQUE.getValue();
    }

    /**
     * 校验角色权限是否唯一
     * @param role
     * @return
     */
    @Override
    public String checkRoleKeyUnique(Role role) {
        Long roleId = role == null ? -1L : role.getRoleId();
        Role next = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (null != next && next.getRoleId().longValue() == roleId.longValue()){
            return Constants.ROLE_KEY_NOT_UNIQUE.getValue();
        }
        return Constants.ROLE_KEY_UNIQUE.getValue();
    }

    @Override
    public int selectUserRoleByRoleId(Long roleId) {
        // TODO
        return 0;
    }

    /**
     * 新增角色菜单信息
     * @param role
     * @return
     */
    private int insertRoleMenu(Role role){
        int rows = 0;
        List<RoleMenu> roleMenus = new ArrayList<>();
        for (Long menuId : role.getMenuIds()){
            roleMenus.add(RoleMenu.builder().menuId(menuId).roleId(role.getRoleId()).build());
        }
        if (!roleMenus.isEmpty()){
            rows = roleMenuMapper.batchRoleMenu(roleMenus);
        }
        return rows;
    }

    /**
     * 新增角色部门信息
     * @param role
     * @return
     */
    private int insertRoleDept(Role role) {
        int rows = 0;
        List<RoleDept> roleDepts = new ArrayList<>();
        for (Long deptId : role.getDeptIds()){
            roleDepts.add(RoleDept.builder().deptId(deptId).roleId(role.getRoleId()).build());
        }
        if (!roleDepts.isEmpty()){
            rows = roleDeptMapper.batchRoleDept(roleDepts);
        }
        return rows;
    }
}