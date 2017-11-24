package com.egojit.easyweb.common.base;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by egojit on 2017/11/23.
 */
@Transactional(readOnly = true)
public abstract class BaseService<M extends Mapper<T>, T extends BaseEntity> {

    @Autowired
    protected M mapper;
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public int deleteByPrimaryKey(Object o) {
        return mapper.deleteByPrimaryKey(o);
    }

    public int delete(T model) {
        return mapper.delete(model);
    }

    public int insert(T model) {
        model.preInsert();
        return mapper.insert(model);
    }

    public int insertSelective(T model) {
        model.preInsert();
        return mapper.insertSelective(model);
    }


    public boolean existsWithPrimaryKey(Object o) {
        return mapper.existsWithPrimaryKey(o);
    }


    public List<T> selectAll() {
        return mapper.selectAll();
    }


    public T selectByPrimaryKey(Object o) {
        return mapper.selectByPrimaryKey(o);
    }

    public int selectCount(T model) {
        return mapper.selectCount(model);
    }

    public List<T> select(T model) {
        return mapper.select(model);
    }


    public T selectOne(T model) {
        return mapper.selectOne(model);
    }

    public int updateByPrimaryKey(T model) {
        model.preUpdate();
        return mapper.updateByPrimaryKey(model);
    }

    public int updateByPrimaryKeySelective(T model) {
        return mapper.updateByPrimaryKeySelective(model);
    }

    public int deleteByExample(Object o) {
        return mapper.deleteByExample(o);
    }

    public List<T> selectByExample(Object o) {
        return mapper.selectByExample(o);
    }

    public int selectCountByExample(Object o) {
        return mapper.selectCountByExample(o);
    }

    public int updateByExample(T model, Object o) {
        return mapper.updateByExample(model, o);
    }

    public int updateByExampleSelective(T model, Object o) {
        return mapper.updateByExampleSelective(model, o);
    }

    public List<T> selectByExampleAndRowBounds(Object o, RowBounds rowBounds) {
        return mapper.selectByExampleAndRowBounds(o, rowBounds);
    }

    public List<T> selectByRowBounds(T model, RowBounds rowBounds) {
        return mapper.selectByRowBounds(model, rowBounds);
    }

}
