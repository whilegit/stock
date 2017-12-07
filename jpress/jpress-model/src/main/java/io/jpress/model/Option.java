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
package io.jpress.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.jfinal.kit.StrKit;

import io.jpress.model.base.BaseOption;
import io.jpress.model.core.Table;
import io.jpress.model.query.OptionQuery;

@Table(tableName = "option", primaryKey = "id")
public class Option extends BaseOption<Option> {

	private static final long serialVersionUID = 1L;

	public static final String KEY_WEB_NAME = "web_name";
	public static final String KEY_TEMPLATE_ID = "web_template_id";

	public static class CanBorrowMoneyItem implements Comparable<CanBorrowMoneyItem>{
		int age;
		double amount;
		public CanBorrowMoneyItem(int age, double amount) {
			this.age = age;
			this.amount = amount;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		@Override
		public int compareTo(CanBorrowMoneyItem o) {
			// TODO Auto-generated method stub
			if(o == null) return 1;
			else if (this.age > o.age) return 1;
			else if(this.age == o.age) return 0;
			else if(this.age < o.age) return -1;
			
			return 0;
		}
	}
	
	public static double calCanBorrowMoney(Date birthday) {
		if(birthday == null) return 0.0;
		
		String credittable = OptionQuery.me().findValueNoCache("credittable");
		if(StrKit.isBlank(credittable)) return 0.0;
		String[] items = credittable.split(";");
		List<CanBorrowMoneyItem> ary = new ArrayList<CanBorrowMoneyItem>();
		for(int i = 0; i<items.length; i++) {
			String[] it = items[i].split(":");
			if(it.length != 2) continue;
			int age = Integer.parseInt(it[0]);
			double amount = Double.parseDouble(it[1]);
			ary.add(new CanBorrowMoneyItem(age, amount));
		}
		if(ary.size() == 0) return 0.0;
		
		Collections.sort(ary);
		
		double amount = 0.0;
		long age = (System.currentTimeMillis() - birthday.getTime()) / (365L * 86400 * 1000 );
		for(int i = 0; i<ary.size(); i++) {
			CanBorrowMoneyItem item = ary.get(i);
			if(age >= item.getAge()) {
				amount = item.getAmount();
			} else {
				break;
			}
		}
		
		return amount;
	}
	
	@Override
	public boolean update() {
		removeCache(getOptionKey());
		return super.update();
	}
	
	
	@Override
	public boolean save() {
		removeCache(getOptionKey());
		return super.save();
	}

	@Override
	public boolean delete() {
		removeCache(getOptionKey());
		return super.delete();
	}

	
}
