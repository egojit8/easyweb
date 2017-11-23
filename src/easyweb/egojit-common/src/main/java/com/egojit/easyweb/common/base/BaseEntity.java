package com.egojit.easyweb.common.base;

import com.egojit.easyweb.common.utils.IdGen;

import javax.persistence.Id;

/**
 * Created by egojit on 2017/11/23.
 */
public abstract class BaseEntity {

    public BaseEntity(){
        this.id= IdGen.uuid();
    }
    /**
     * id
     */
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
