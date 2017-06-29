package com.sino.scaffold.model.base;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author kerbores
 *
 */
public class BaseEntity<T extends Model> extends Model<T>  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@TableId("id")
    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.baomidou.mybatisplus.activerecord.Model#pkVal()
	 */
	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
