package com.shsxt.crm.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.shsxt.crm.model.Module;
import com.shsxt.crm.vo.ModuleVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ModuleDao {
    public PageList<Module> selectForPage(PageBounds pageBounds);

     void add(Module module);


     Module findById(@Param(value = "parentId")Integer parentId);


    Module finByOptVaue(@Param(value = "optValue")String optValue);

    void update(Module module);

    void deleteBatch(@Param(value = "ids") String ids);

    List<Module> findModuleByGrade(@Param(value = "grade") Integer grade);

    List<Module> findbyParentId(@Param(value = "id")String id);

    @Select("select id, module_name, parent_id from t_module where is_valid = 1 ")
    List<ModuleVO> findAll();

    @Select("SELECT module_id FROM t_permission WHERE role_id = #{roleId} and is_valid = 1")
    List<Integer> findByRoleId(@Param(value = "roleId")Integer roleId);
}
