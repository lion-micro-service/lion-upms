package com.lion.upms.dao.user.impl;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.exception.BusinessException;
import com.lion.upms.dao.role.RoleDao;
import com.lion.upms.dao.user.UserDaoEx;
import com.lion.upms.entity.role.Role;
import com.lion.upms.entity.user.User;
import com.lion.upms.entity.user.dto.UserSearchDto;
import com.lion.upms.entity.user.vo.UserListVo;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * @author mr.liu
 * @title: UserDaoImpl
 * @description: 用户dao复杂sql操作扩展
 * @date 2020/8/15下午5:00
 */
public class UserDaoImpl implements UserDaoEx {

    @Autowired
    private BaseDao<User> baseDao;

    @Autowired
    private DataSource dataSource;

    @Override
    public Page<UserListVo> list(Pageable pageable, UserSearchDto userSearchDto) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> searchParameter = new HashMap<String, Object>();
        sb.append(" select distinct new com.lion.upms.entity.user.vo.UserListVo(u, d) from User u left join DepartmentUser du on u.id = du.userId ");
        sb.append(" left join Department d on du.departmentId = d.id ");
        sb.append(" left join RoleUser ru on u.id = ru.userId ");
        sb.append(" left join Role r on ru.roleId = r.id ");
        sb.append(" where 1=1 ");
        if (StringUtils.hasText(userSearchDto.getName())){
            sb.append(" and u.name like :name ");
            searchParameter.put("name","%"+userSearchDto.getName()+"%");
        }
        if (StringUtils.hasText(userSearchDto.getUsername())){
            sb.append(" and u.username like :username ");
            searchParameter.put("username","%"+userSearchDto.getUsername()+"%");
        }
        if (StringUtils.hasText(userSearchDto.getEmail())){
            sb.append(" and u.email like :email ");
            searchParameter.put("email","%"+userSearchDto.getEmail()+"%");
        }
        if (Objects.nonNull(userSearchDto.getAge())){
            sb.append(" and u.age = :age ");
            searchParameter.put("age", userSearchDto.getAge());
        }
        if (Objects.nonNull(userSearchDto.getBirthday())){
            sb.append(" and u.birthday = :birthday ");
            searchParameter.put("birthday", userSearchDto.getBirthday());
        }
        if (Objects.nonNull(userSearchDto.getDepartmentId()) && userSearchDto.getDepartmentId().size()>0){
            sb.append(" and d.id in( :departmentId ) ");
            searchParameter.put("departmentId", userSearchDto.getDepartmentId());
        }
        if (Objects.nonNull(userSearchDto.getRoleId())){
            sb.append(" and r.id = :roleId ");
            searchParameter.put("roleId", userSearchDto.getRoleId());
        }
//        sb.append(" and u.username not in ('admin','superadmin') ");
//        sb.append(" order by u.createDateTime  desc");
        Page page = baseDao.findNavigator(pageable, sb.toString(), searchParameter);

        return (Page<UserListVo>) page;
    }

    @Override
    public List<User> find(List<Long> in, List<Long> notIn) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> searchParameter = new HashMap<String, Object>();
        sb.append(" select u  from User u ");
        sb.append(" where 1=1 ");
        if (Objects.nonNull(in) && in.size()>0){
            sb.append(" and u.id in :in ");
            searchParameter.put("in",in);
        }
        if (Objects.nonNull(notIn) && notIn.size()>0){
            sb.append(" and u.id not in :notIn ");
            searchParameter.put("notIn",notIn);
        }
        return (List<User>) baseDao.findAll(sb.toString(),searchParameter);
    }

    @Override
    public Optional<User> find(String username) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            User user = queryRunner.query("select * from t_user where username = ? limit 0,1 ",new BeanHandler<User>(User.class),new String[]{username});
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            BusinessException.throwException("查询用户失败");
        }
        return Optional.empty();
    }

}
