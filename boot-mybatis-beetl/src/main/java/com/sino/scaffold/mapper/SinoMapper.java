package com.sino.scaffold.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author kerbores
 *
 */
public interface SinoMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
