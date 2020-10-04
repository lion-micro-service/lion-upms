package com.lion.upms.dao.department;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.department.DepartmentUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author mr.liu
 * @title: DepartmentUserDao
 * @description: 部门与用户关联dao
 * @date 2020/8/15下午5:55
 */
public interface DepartmentUserDao extends BaseDao<DepartmentUser> ,DepartmentUserDaoEx {

    /**
     * 根据部门ID和用户ID删除所关联的用户
     * @param departmentId
     * @param userId
     * @return
     */
    public Integer deleteByDepartmentIdAndUserIdIn(Long departmentId, List<Long> userId);

    /**
     *根据部门ID和用户ID查询在此部门所关联的用户
     * @param departmentId
     * @param userId
     * @return
     */
    public List<DepartmentUser> findByDepartmentIdAndUserIdIn(Long departmentId, List<Long> userId);

    /**
     * 根据部门ID和用户ID查询不在此部门的用户却在其它部门的用户
     * @param departmentId
     * @param userId
     */
    @Query(" select du0 from DepartmentUser du0 where du0.userId not in " +
            "( " +
            "   select du1.userId from DepartmentUser du1 where du1.departmentId=:departmentId and du1.userId in (:userId) " +
            "   " +
            ") and du0.departmentId<>:departmentId and du0.userId in (:userId)")
    public List<DepartmentUser> findNotInDepartmentUser(@Param("departmentId") Long departmentId,@Param("userId") List<Long> userId);

    /**
     * 根据用户Id删除部门与用户的关联
     * @param userId
     */
    public void deleteByUserId(Long userId);

    /**
     * 根据部门id删除部门与用户的关联
     * @param departmentId
     */
    public void deleteByDepartmentId(Long departmentId);
}
