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
<%if (has(table.remarks)) {%>
@Comment("${table.remarks}")
<%}%>
public class ${table.javaClassName!} extends DataBaseEntity {

	<% for(field in table.allColumns){ %>
		<% if(field.pk! && (field.jdbcTypeName! == 'NUMERIC'|| field.jdbcTypeName! == 'BIGINT' || field.jdbcTypeName! == 'INTEGER' || field.jdbcTypeName! == 'INT')){ %>
		@Id
		<% }else if(field.pk){ %>
		@Name
		<% } %>
		@Column("${field.name!}")
		<%if (has(field.remarks)) {%>
		@Comment("${field.remarks}")
		<%}%>
		@ColDefine(width=${field.length!}${field.scale!=0 ? ',precision='+field.scale,''})
		private ${field.fullyQualifiedJavaType.fullyQualifiedName!} ${field.javaProperty!} ;
		
		public ${field.fullyQualifiedJavaType.fullyQualifiedName!} get${field.upperjavaProperty!}() {
			return ${field.javaProperty!};
		}
		
		public void set${field.upperjavaProperty!}(${field.fullyQualifiedJavaType.fullyQualifiedName!} ${field.javaProperty!}) {
			this.${field.javaProperty!} = ${field.javaProperty!};
		}
	<% } %>
}