package com.egojit.easyweb.common.base;

import com.egojit.easyweb.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 备注：基础服务类
 * 作者：高露
 * 日期：2017-11-24
 */
@Transactional
public abstract class BaseService<M extends Mapper<T>, T extends BaseEntity> {

    @Autowired
    protected M mapper;

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());


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



    private  Class <T> getTClass(){
        Class <T> tClass=(Class < T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return tClass;
    }

    /**
     * 根据example条件查询数据
     * @param example
     * @return
     */
    public List<T> selectByExample(Example example) {
        if(example==null){

            example=new Example(getTClass());
        }
        String order= example.getOrderByClause();
        if(StringUtils.isEmpty(order)){
            example.setOrderByClause(" create_date DESC");
        }
        return mapper.selectByExample(example);
    }

    /**
     * 根据example条件查询分页数据
     * @param example example条件
     * @param rowBounds 分页条件
     * @return
     */
    public List<T> selectByExampleAndRowBounds(Example example, RowBounds rowBounds) {
        if(example==null){

            example=new Example(getTClass());
        }
        String order= example.getOrderByClause();
        if(StringUtils.isEmpty(order)){
            example.setOrderByClause(" create_date DESC");
        }
        return mapper.selectByExampleAndRowBounds(example, rowBounds);
    }

    /**
     * 根据example条件查询数据记录数
     * @param example
     * @return
     */
    public int selectCountByExample(Example example) {
        return mapper.selectCountByExample(example);
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
     * @param example 对象
     * @return 返回page对象
     */
    public Page<T> selectPageByExample(Example example, Page<T> page) {
        if(!page.isDisabled()) {
            long count= this.selectCountByExample(example);
            page.setRecords(count);
            PageHelper.startPage(page.getPage(), page.getPageSize());
        }

        List<T> list=this.selectByExample(example);
        return page.setList(list);
    }
    /**
     * 根据对象条件查询分页数据-返回page对象
     * @param model 对象
     * @return 返回page对象
     */
    public Page<T> selectPage(T model, Page<T> page) {
        if(!page.isDisabled()) {
            long count= selectCount(model);
            page.setRecords(count);
            PageHelper.startPage(page.getPage(),page.getPageSize());
        }
        List<T> list=this.select(model);
        return page.setList(list);
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
}
