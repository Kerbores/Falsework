package ${project.group}.${project.name}.bean;


import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import org.nutz.plugin.spring.boot.service.entity.DataBaseEntity;

/**
 * 
 * @author nutz-matic
 * 
 */
@Table("${table.name}")
@Comment("${has(table.remarks) ? table.remarks : table.name}")
public class ${table.className!} extends DataBaseEntity {

	<% for(field in table.fields){ %>
		<% if(field.pk! && (field.jdbcTypeName! == 'NUMERIC'|| field.jdbcTypeName! == 'BIGINT' || field.jdbcTypeName! == 'INTEGER' || field.jdbcTypeName! == 'INT')){ %>
		@Id
		<% }else if(field.pk){ %>
		@Name
		<% } %>
		@Column("${field.name!}")
		@Comment("${has(field.remarks) ?field.remarks : field.name}")
		@ColDefine(width=${field.length!})
		private ${field.javaName!} ${field.javaProperty!} ;
		
		public ${field.javaName!} get${field.upperjavaProperty!}() {
			return ${field.javaProperty!};
		}
		
		public void set${field.upperjavaProperty!}(${field.javaName!} ${field.javaProperty!}) {
			this.${field.javaProperty!} = ${field.javaProperty!};
		}
	<% } %>
}