/**
 * Copyright (c) 2011-2016, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.plugin.activerecord.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.Table;

/**
 * Dialect.
 */
public abstract class Dialect {
	
	// Methods for common
	public abstract String forTableBuilderDoBuild(String tableName);
	
	/**
	 * 分页查询　在sql后面增加 limit (pageNum-1) * pageSize,pageSize
	 * @param pageNumber　输入参数，页码，从1开始
	 * @param pageSize　　　输入参数，每页的记录数
	 * @param select 　　　　输入参数，原查询语句
	 * @param sqlExceptSelect　附加在原查询语句后面
	 * @return　返回附加了limit后的分页sql
	 */
	public abstract String forPaginate(int pageNumber, int pageSize, String select, String sqlExceptSelect);
	
	/**
	 * 生成以主键为条件的sql查询语句
	 * @param table
	 * @param columns 输入参数，以逗号隔开不同字段,也可以是*号
	 * @return　输出，例：　select column1,column2,... Where pkey1=? And pkey2=?
	 */
	public abstract String forModelFindById(Table table, String columns);
	
	/**
	 * 生成 Delete from 语句。类似　forDbDeleteById（...）
	 * @param table
	 * @return 返回如　delete from table_xxx where pkey1=? and pkey2=? ...
	 */
	public abstract String forModelDeleteById(Table table);
	
	/**
	 *  Insert Into 
	 * @param table　输入参数
	 * @param attrs 输入参数，字段名和字段值组成的Map
	 * @param sql   输出，sql存放于此，生成如　insert into table_xxx (col1,col2,...) Values (?, ?, ...)
	 * @param paras 输出，sql绑定的对象存放于此,用于上面的？替换
	 */
	public abstract void forModelSave(Table table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras);
	
	/**
	 * 生成 Update .. set 语句
	 * @param table 输入参数
	 * @param attrs 输入参数 字段名和字段值组成的Map
	 * @param modifyFlag 输入参数，以字段名为值的Set
	 * @param sql 输出，例　Update table_xxx Set col1=?,col2=?... Where pkey1=? And pkey2=? 
	 * @param paras 输出, 获得attrs存放的字段值
	 */
	public abstract void forModelUpdate(Table table, Map<String, Object> attrs, Set<String> modifyFlag, StringBuilder sql, List<Object> paras);
	
	/**
	 * 生成以主键为条件的sql查询语句
	 * @param table
	 * @param columns 输出参数，以逗号隔开不同字段,也可以是*号
	 * @return　输出，例：　select ＊ Where pkey1=? And pkey2=?
	 */
	public abstract String forDbFindById(String tableName, String[] pKeys);
	
	/**
	 * 生成 Delete from语句，类似　类似　forModelDeleteById（...）
	 * @param tableName
	 * @param pKeys 主键列表
	 * @return 返回如　delete from table_xxx where pkey1=? and pkey2=? ...
	 */
	public abstract String forDbDeleteById(String tableName, String[] pKeys);
	
	/**
	 * 生成Insert into语句
	 * @param tableName　输入参数，表名
	 * @param pKeys　　输入参数，主键列表
	 * @param record 输入参数，一条记录，存放键值对
	 * @param sql　输出结果,sql
	 * @param paras　输出结果，sql绑定的值
	 */
	public abstract void forDbSave(String tableName, String[] pKeys, Record record, StringBuilder sql, List<Object> paras);
	
	/**
	 * 生成 Update .. set 语句
	 * @param tableName  输入参数，表名
	 * @param pKeys      输入参数，主键列表
	 * @param ids        输入参数，主键列表的值
	 * @param record　　　　　输入参数，记录
	 * @param sql　　　　　　　　输出结果
	 * @param paras　　　　　　输出结果，绑定值列表
	 */
	public abstract void forDbUpdate(String tableName, String[] pKeys, Object[] ids, Record record, StringBuilder sql, List<Object> paras);
	
	public boolean isOracle() {
		return false;
	}
	
	public boolean isTakeOverDbPaginate() {
		return false;
	}
	
	public Page<Record> takeOverDbPaginate(Connection conn, int pageNumber, int pageSize, Boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) throws SQLException {
		throw new RuntimeException("You should implements this method in " + getClass().getName());
	}
	
	public boolean isTakeOverModelPaginate() {
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public Page takeOverModelPaginate(Connection conn, Class<? extends Model> modelClass, int pageNumber, int pageSize, Boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) throws Exception {
		throw new RuntimeException("You should implements this method in " + getClass().getName());
	}
	
	/**
	 * 将参数绑定到预编译的sql语句上
	 * @param pst　输入输出参数
	 * @param paras　输入参数
	 * @throws SQLException
	 */
	public void fillStatement(PreparedStatement pst, List<Object> paras) throws SQLException {
		for (int i=0, size=paras.size(); i<size; i++) {
			pst.setObject(i + 1, paras.get(i));
		}
	}
	
	/**
	 * 将参数绑定到预编译的sql语句上
	 * @param pst　输入输出参数
	 * @param paras　输入参数
	 * @throws SQLException
	 */
	public void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
		for (int i=0; i<paras.length; i++) {
			pst.setObject(i + 1, paras[i]);
		}
	}
	
	public String getDefaultPrimaryKey() {
		return "id";
	}
	
	public boolean isPrimaryKey(String colName, String[] pKeys) {
		for (String pKey : pKeys) {
			if (colName.equalsIgnoreCase(pKey)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 一、forDbXxx 系列方法中若有如下两种情况之一，则需要调用此方法对 pKeys 数组进行 trim():
	 * 1：方法中调用了 isPrimaryKey(...)：为了防止在主键相同情况下，由于前后空串造成 isPrimaryKey 返回 false
	 * 2：为了防止 tableName、colName 与数据库保留字冲突的，添加了包裹字符的：为了防止串包裹区内存在空串
	 *   如 mysql 使用的 "`" 字符以及 PostgreSql 使用的 "\"" 字符
	 * 不满足以上两个条件之一的 forDbXxx 系列方法也可以使用 trimPrimaryKeys(...) 方法让 sql 更加美观，但不是必须
	 * 
	 * 二、forModelXxx 由于在映射时已经trim()，故不再需要调用此方法
	 */
	public void trimPrimaryKeys(String[] pKeys) {
		for (int i=0; i<pKeys.length; i++) {
			pKeys[i] = pKeys[i].trim();
		}
	}
	
	protected static class Holder {
		// "order\\s+by\\s+[^,\\s]+(\\s+asc|\\s+desc)?(\\s*,\\s*[^,\\s]+(\\s+asc|\\s+desc)?)*";
		private static final Pattern ORDER_BY_PATTERN = Pattern.compile(
			"order\\s+by\\s+[^,\\s]+(\\s+asc|\\s+desc)?(\\s*,\\s*[^,\\s]+(\\s+asc|\\s+desc)?)*",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	}
	
	/**
	 * 删除sql语句的Order By或Order by ... desc部分
	 * @param sql
	 * @return
	 */
	public String replaceOrderBy(String sql) {
		return Holder.ORDER_BY_PATTERN.matcher(sql).replaceAll("");
	}
}






