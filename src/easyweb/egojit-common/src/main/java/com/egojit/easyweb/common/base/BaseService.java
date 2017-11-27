package com.egojit.easyweb.common.base;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 备注：基础服务类
 * 作者：高露
 * 日期：2017-11-24
 */
@Transactional(readOnly = true)
public abstract class BaseService<M extends Mapper<T>, T extends BaseEntity> {

    @Autowired
    protected M mapper;
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /***
     * 通过主键删除对象
     * @param o
     * @return
     */
    public int deleteByPrimaryKey(Object o) {
        return mapper.deleteByPrimaryKey(o);
    }

    /**
     * 删除对象
     * @param model
     * @return
     */
    public int delete(T model) {
        return mapper.delete(model);
    }

    /**
     * 插入对象
     * @param model
     * @return
     */
    public int insert(T model) {
        model.preInsert();
        return mapper.insert(model);
    }

    /**
     * 插入选择的值
     * @param model
     * @return
     */
    public int insertSelective(T model) {
        model.preInsert();
        return mapper.insertSelective(model);
    }


    /**
     * 根据主键判断对象是否存在
     * @param o
     * @return
     */
    public boolean existsWithPrimaryKey(Object o) {
        return mapper.existsWithPrimaryKey(o);
    }


    /**
     * 查询所有
     * @return
     */
    public List<T> selectAll() {
        return mapper.selectAll();
    }


    /**
     * 根据主键查询对象
     * @param o
     * @return
     */
    public T selectByPrimaryKey(Object o) {
        return mapper.selectByPrimaryKey(o);
    }

    /**
     * 根据对象条件查询数据记录数
     * @param model
     * @return
     */
    public int selectCount(T model) {
        return mapper.selectCount(model);
    }

    /**
     * 根据对象查询数据
     * @param model
     * @return
     */
    public List<T> select(T model) {
        return mapper.select(model);
    }

    /**
     * 根据对象查询单条对象
     * @param model
     * @return
     */
    public T selectOne(T model) {
        return mapper.selectOne(model);
    }

    /**
     * 根据主键更新对象-会更新所有字段包括空值
     * @param model
     * @return
     */
    public int updateByPrimaryKey(T model) {
        model.preUpdate();
        return mapper.updateByPrimaryKey(model);
    }

    /**
     * 根据主键更新对象-只有不为空的属性才会被更新
     * @param model
     * @return
     */
    public int updateByPrimaryKeySelective(T model) {
        return mapper.updateByPrimaryKeySelective(model);
    }

    /**
     * 删除对象
     * @param o
     * @return
     */
    public int deleteByExample(Object o) {
        return mapper.deleteByExample(o);
    }

    /**
     * 根据example条件查询数据
     * @param o
     * @return
     */
    public List<T> selectByExample(Object o) {
        return mapper.selectByExample(o);
    }

    /**
     * 根据example条件查询数据记录数
     * @param o
     * @return
     */
    public int selectCountByExample(Object o) {
        return mapper.selectCountByExample(o);
    }

    /**
     * 根据example条件更新符合条件的数据-会更新所有字段包括空值
     * @param model
     * @param o
     * @return
     */
    public int updateByExample(T model, Object o) {
        return mapper.updateByExample(model, o);
    }
    /**
     * 根据example条件更新符合条件的数据-只有不为空的属性才会被更新
     * @param model
     * @param o
     * @return
     */
    public int updateByExampleSelective(T model, Object o) {
        return mapper.updateByExampleSelective(model, o);
    }

    /**
     * 根据example条件查询分页数据
     * @param o example条件
     * @param rowBounds 分页条件
     * @return
     */
    public List<T> selectByExampleAndRowBounds(Object o, RowBounds rowBounds) {
        return mapper.selectByExampleAndRowBounds(o, rowBounds);
    }

    /**
     * 根据对象条件查询分页数据
     * @param model 对象
     * @param rowBounds 分页条件
     * @return 返回数据列表
     */
    public List<T> selectByRowBounds(T model, RowBounds rowBounds) {
        return mapper.selectByRowBounds(model, rowBounds);
    }

    /**
     * 根据对象条件查询分页数据-返回page对象
     * @param model 对象
     * @return 返回page对象
     */
    public Page<T> selectPage(T model, Page<T> page) {
        long count= selectCount(model);
        page.setRecords(count);
        List<T> list=this.selectByRowBounds(model,new RowBounds(page.getPage(),page.getPageSize()));
        return page.setList(list);
    }

    /**
     * 根据对象条件查询分页数据-返回page对象
     * @param model 对象
     * @return 返回page对象
     */
    public Page<T> selectPageByExample(T model, Page<T> page) {
        long count= this.selectCountByExample(model);
        page.setRecords(count);
        List<T> list=this.selectByExampleAndRowBounds(model,new RowBounds(page.getPage(),page.getPageSize()));
        return page.setList(list);
    }
}
