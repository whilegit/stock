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
package io.jpress.menu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.jpress.model.User;
import io.jpress.utils.StringUtils;
import yjt.core.perm.PermKit;

public class MenuGroup {

	public static final String TYPE_NORMAL = "_normal";
	public static final String TYPE_BLOCK = "_block";

	private String id;
	private String iconClass;
	private String text;
	private String type = TYPE_NORMAL;
	private List<MenuItem> menuItems;

	public MenuGroup() {
	}

	public MenuGroup(String id, String iconClass, String text) {
		this.id = id;
		this.iconClass = iconClass;
		this.text = text;
	}

	public MenuGroup(String id, String iconClass, String text, String type) {
		this.id = id;
		this.iconClass = iconClass;
		this.text = text;
		this.type = type;
	}

	public static MenuGroup createBlockGroup() {
		MenuGroup group = new MenuGroup();
		group.setType(TYPE_BLOCK);
		return group;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	public void addMenuItem(MenuItem item) {
		if (this.menuItems == null) {
			menuItems = new LinkedList<MenuItem>();
		}
		menuItems.add(item);
	}

	public void addMenuItem(int index, MenuItem item) {
		if (this.menuItems == null) {
			menuItems = new LinkedList<MenuItem>();
		}
		menuItems.add(index, item);
	}

	public void removeMenuItemById(String id) {
		if (StringUtils.isBlank(id)) {
			return;
		}

		if (menuItems == null || menuItems.isEmpty()) {
			return;
		}

		MenuItem deleteItem = null;
		for (MenuItem item : menuItems) {
			if (id.equals(item.getId())) {
				deleteItem = item;
				break;
			}
		}
		if (deleteItem != null) {
			menuItems.remove(deleteItem);
		}
	}

	public String generateHtml(User user) {
		if (TYPE_BLOCK.equals(type)) {
			return "<li class=\"jpress_block\"></li>";
		}
		MenuGroup copy = null;
		if(user.isSuperAdministrator() == false) {
			try {
				copy = (MenuGroup) this.clone();
				if(copy == null) return "";
				PermKit.permFilter(copy, user.getPerm());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return "";
			}
		} else {
			copy = this;
		}
		
		if(copy.menuItems == null || copy.menuItems.size() == 0) {
			return "";
		}
		
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<li class=\"treeview\" id=\"" + id + "\">");
		{
			htmlBuilder.append("<a href=\"#\">");
			htmlBuilder.append("<i class=\"" + iconClass + "\" ></i>");
			htmlBuilder.append("<span>" + text + "</span>");
			htmlBuilder.append("<i class=\"fa fa-angle-left pull-right\" ></i>");
			htmlBuilder.append("</a>");
			htmlBuilder.append("<ul class=\"treeview-menu\">");

			if (copy.menuItems != null && copy.menuItems.size() > 0) {
				for (MenuItem item : copy.menuItems) {
					htmlBuilder.append(item.generateHtml());
				}
			}

			htmlBuilder.append("</ul>");
		}
		htmlBuilder.append("</li>");
		return htmlBuilder.toString();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		MenuGroup menuGrp = new MenuGroup();
		menuGrp.id = this.id;
		menuGrp.iconClass = this.iconClass;
		menuGrp.text = this.text;
		menuGrp.type = TYPE_NORMAL;
		menuGrp.menuItems = new ArrayList<MenuItem>();
		if(this.menuItems != null && this.menuItems.size() > 0) {
			for(MenuItem item : this.menuItems) {
				menuGrp.addMenuItem(item);
			}
		}
			
		return menuGrp;
	}
	

}
