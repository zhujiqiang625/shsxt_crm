package com.shsxt.crm.dao;

import com.shsxt.crm.model.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionDao {

    void releaseModule(@Param(value ="roleId") Integer roleId, @Param(value ="moduleId") Integer moduleId);

    void insertBatch(@Param(value="permissions")List<Permission> permissions);

    @Select("SELECT count(1) from t_permission where role_id = #{roleId} "
            + " and module_id= #{moduleId} and is_valid=1")
    Integer findByRoleIdModuleId(@Param(value ="roleId")Integer roleId, @Param(value ="moduleId")Integer moduleId);
    @Select("SELECT DISTINCT t3.acl_value FROM t_user t1 " +
            " LEFT JOIN t_user_role t2 ON t1.id = t2.user_id " +
            " LEFT JOIN t_permission t3 ON t2.role_id = t3.role_id" +
            " WHERE t1.id = #{userId} and t3.is_valid=1")
    List<String> findUserPermissionsInteger(@Param(value = "userId") Integer id);
}
