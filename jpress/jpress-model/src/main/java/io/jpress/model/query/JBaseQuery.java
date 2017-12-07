/**
 * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.model.query;

import java.math.BigInteger;
import java.util.List;

import io.jpress.utils.StringUtils;

public class JBaseQuery {

	protected static boolean appendWhereOrAnd(StringBuilder builder, boolean needWhere) {
		if (needWhere) {
			builder.append(" WHERE ");
		} else {
			builder.append(" AND ");
		}
		return false;
	}

	protected static boolean appendIfNotEmpty(StringBuilder builder, String colName, String value, List<Object> params,
			boolean needWhere) {
		if (value != null) {
			needWhere = appendWhereOrAnd(builder, needWhere);
			builder.append(" ").append(colName).append(" = ? ");
			params.add(value);
		}
		return needWhere;
	}

	protected static boolean appendIfNotEmpty(StringBuilder builder, String colName, BigInteger value,
			List<Object> params, boolean needWhere) {
		if (value != null) {
			needWhere = appendWhereOrAnd(builder, needWhere);
			builder.append(" ").append(colName).append(" = ? ");
			params.add(value);
		}
		return needWhere;
	}

	/**
	 * 追加sql的条件部分，(colName = ? Or colName = ? ...)
	 * @param builder 输入输出参数，内含sql语句
	 * @param colName 输入参数，字段名
	 * @param array   输入参数，字段colName可能的值列表，sql语句使用or连接。如参数为null时，直接返回输入参数needWhere
	 * @param params　　输入输出参数，加入绑定的参数列表
	 * @param needWhere 输入参数，是否要在sql后面加上Where字符串
	 * @return
	 */
	protected static boolean appendIfNotEmpty(StringBuilder builder, String colName, Object[] array,
			List<Object> params, boolean needWhere) {
		if (null != array && array.length > 0) {
			needWhere = appendWhereOrAnd(builder, needWhere);
			builder.append(" (");
			for (int i = 0; i < array.length; i++) {
				if (i == 0) {
					builder.append(" ").append(colName).append(" = ? ");
				} else {
					builder.append(" OR ").append(colName).append(" = ? ");
				}
				params.add(array[i]);
			}
			builder.append(" ) ");
		}
		return needWhere;
	}

	/**
	 * 只生成sql的条件部分(and)，不添加where
	 * @author lzr
	 */
	protected static void appendAndIfNotEmpty(StringBuilder builder, String colName, String value, List<Object> params){
		if(value != null){
			if(params.size() > 0) builder.append(" And ");
			builder.append(" ").append(colName).append(" = ? ");
			params.add(value);
		}
	}
	
	protected static boolean appendIfNotEmptyWithLike(StringBuilder builder, String colName, String value,
			List<Object> params, boolean needWhere) {
		if (StringUtils.isNotBlank(value)) {
			needWhere = appendWhereOrAnd(builder, needWhere);
			builder.append(" ").append(colName).append(" like ? ");
			if (value.contains("%")) {
				params.add(value);
			} else {
				params.add("%" + value + "%");
			}

		}
		return needWhere;
	}

	protected static boolean appendIfNotEmptyWithLike(StringBuilder builder, String colName, String[] array,
			List<Object> params, boolean needWhere) {
		if (null != array && array.length > 0) {
			needWhere = appendWhereOrAnd(builder, needWhere);
			builder.append(" (");
			for (int i = 0; i < array.length; i++) {
				if (i == 0) {
					builder.append(" ").append(colName).append(" like ? ");
				} else {
					builder.append(" OR ").append(colName).append(" like ? ");
				}
				String value = array[i];
				if (value.contains("%")) {
					params.add(value);
				} else {
					params.add("%" + value + "%");
				}
			}
			builder.append(" ) ");
		}
		return needWhere;
	}

}
