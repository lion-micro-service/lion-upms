package com.lion.upms.service.department.impl;

import com.lion.core.service.impl.BaseServiceImpl;
import com.lion.exception.BusinessException;
import com.lion.upms.dao.department.DepartmentDao;
import com.lion.upms.entity.department.Department;
import com.lion.upms.entity.department.vo.DepartmentTreeVo;
import com.lion.upms.service.department.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.print.PeekGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: DepartmentServiceImpl
 * @description: 部门service
 * @date 2020/8/15下午5:38
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department> implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public List<DepartmentTreeVo> listTree() {
        List<Department> list = departmentDao.findByParentId(0L);
        List<DepartmentTreeVo> listTree = convertVo(list);
        listTree.forEach(departmentTreeVo -> {
            departmentTreeVo.setChildren(convertVo(findChildren(departmentTreeVo.getId())));
        });
        return listTree;
    }

    @Override
    public Department findDepartment(Long parentId, String name) {
        return departmentDao.findFirstByParentIdAndName(parentId, name);
    }

    @Override
    public Boolean checkNameIsExist(Long parentId, String name,Long id) {
        Department department = findDepartment(parentId, name);
        if (Objects.isNull(department)){
            return false;
        }
        if (Objects.nonNull(id) && department.getId().equals(id)){
            return false;
        }
        return true;
    }

    @Override
    public void delete(Long id) {
        Department department = this.findById(id);
        List<Department> list = new ArrayList<Department>();
        list.add(department);
        findAllChilder(department.getId(),list);
        this.deleteInBatch(list);
    }

    @Override
    public List<Department> findAllChilder(Long id) {
        List<Department> list = new ArrayList<Department>();
        findAllChilder(id, list);
        return list;
    }

    @Override
    public DepartmentTreeVo findTreeParentDepartment(Long parentId) {
        Department department = this.findById(parentId);
        return findTreeParentDepartment(findTreeParentDepartment(department));
    }

    @Override
    public List<Department> findAllParentDepartment(Long parentId) {
        List<Department> list = new ArrayList<Department>();
        findAllParentDepartment(parentId,list);
        return list;
    }

    @Override
    public <S extends Department> S save(S entity) {
        if (checkNameIsExist(entity.getParentId(),entity.getName(),entity.getId())){
            new BusinessException("该名称在同节点中已存在");
        }
        return super.save(entity);
    }

    @Override
    public void update(Department entity) {
        if (checkNameIsExist(entity.getParentId(),entity.getName(),entity.getId())){
            new BusinessException("该名称在同节点中已存在");
        }
        super.update(entity);
    }

    /**
     * 转vo
     * @param list
     * @return
     */
    private List<DepartmentTreeVo> convertVo(List<Department> list){
        List<DepartmentTreeVo> listTree = new ArrayList<DepartmentTreeVo>();
        if (Objects.nonNull(list) && list.size()>0){
            list.forEach(department -> {
                DepartmentTreeVo departmentTreeVo = convertVo(department);
                if (Objects.nonNull(departmentTreeVo)) {
                    departmentTreeVo.setChildren(convertVo(findChildren(departmentTreeVo.getId())));
                    listTree.add(convertVo(departmentTreeVo));
                }
            });
        }
        return listTree.size()>0?listTree:null;
    }

    /**
     * 转vo
     * @param department
     * @return
     */
    private DepartmentTreeVo convertVo(Department department){
        if (Objects.isNull(department)){
            return null;
        }
        DepartmentTreeVo departmentTreeVo = new DepartmentTreeVo();
        BeanUtils.copyProperties(department, departmentTreeVo);
        return departmentTreeVo;
    }

    /**
     * 根据父节点id获取子节点
     * @param parentId
     * @return
     */
    private List<Department> findChildren(Long parentId){
        return departmentDao.findByParentId(parentId);
    }

    /**
     *
     * @param parentId
     * @param list
     */
    private void findAllChilder(Long parentId,List<Department> list){
        List<Department> chidren = findChildren(parentId);
        list.addAll(chidren);
        chidren.forEach(department -> {
            findAllChilder(department.getId(), list);
        });
    }

    /**
     * 获取所有父节点(链表数据结构)
     * @param parentId
     * @param departmentTreeVo
     * @return
     */
    private DepartmentTreeVo findTreeParentDepartment(Long parentId,DepartmentTreeVo departmentTreeVo) {
        Department department = this.findById(parentId);
        return findTreeParentDepartment(findTreeParentDepartment(department));
    }

    /**
     * 获取所有父节点(链表数据结构)
     * @param department
     * @return
     */
    private DepartmentTreeVo findTreeParentDepartment(Department department){
        if (Objects.nonNull(department)){
            DepartmentTreeVo departmentTreeVo = convertVo(department);
            departmentTreeVo.setParentDepartment(findTreeParentDepartment(departmentTreeVo.getParentId(),departmentTreeVo));
            return departmentTreeVo;
        }
        return null;
    }

    /**
     * 获取所有父节点
     * @param parentId
     * @param list
     */
    private void findAllParentDepartment(Long parentId,List<Department> list){
        Department department = this.findById(parentId);
        if (Objects.nonNull(department)) {
            list.add(department);
            findAllParentDepartment(department.getParentId(), list);
        }
    }
}
