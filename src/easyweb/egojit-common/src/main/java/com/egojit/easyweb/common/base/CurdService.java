package com.egojit.easyweb.common.base;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 备注：CurdService 支持增删改的服务
 * 作者：egojit
 * 日期：2017/11/27
 */
@Transactional
public abstract class CurdService<M extends Mapper<T>, T extends CurdEndity> extends BaseService<M,T> {
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
     * 删除对象
     * @param ids  ids
     * @return
     */
    public int deleteByIds(List<String> ids) {
        int count=0;
        if(ids!=null)
            for (String id:ids ) {
                count+=mapper.deleteByPrimaryKey(id);
            }
        return count;
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


}
