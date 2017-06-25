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
package io.jpress.admin.controller;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.JBaseCRUDController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.model.User;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import io.jpress.template.TemplateManager;
import io.jpress.utils.EncryptUtils;
import io.jpress.utils.StringUtils;

import yjt.core.perm.PermAnnotation;
import yjt.core.perm.PermGroup;
import yjt.core.perm.PermKit;

@RouterMapping(url = "/admin/user", viewPath = "/WEB-INF/admin/user")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _UserController extends JBaseCRUDController<User> {

	@PermAnnotation("user-list")
	public void index() {
		setAttr("userCount", UserQuery.me().findCount());
		setAttr("adminCount", UserQuery.me().findAdminCount());

		Page<User> page = UserQuery.me().paginate(getPageNumber(), getPageSize(), null);
		setAttr("page", page);

		String templateHtml = "admin_user_index.html";
		if (TemplateManager.me().existsFile(templateHtml)) {
			setAttr("include", TemplateManager.me().currentTemplatePath() + "/" + templateHtml);
			return;
		}
		setAttr("include", "_index_include.html");
	}

	@PermAnnotation("user-edit")
	public void edit() {
		BigInteger id = getParaToBigInteger("id");
		if (id != null) {
			setAttr("user", UserQuery.me().findById(id));
		}

		String templateHtml = "admin_user_edit.html";
		if (TemplateManager.me().existsFile(templateHtml)) {
			setAttr("include", TemplateManager.me().currentTemplatePath() + "/" + templateHtml);
			return;
		}
		setAttr("include", "_edit_include.html");

	}

	/**
	 * 编辑或新增用户
	 * 
	 */
	@PermAnnotation("user-edit")
	public void save() {
		HashMap<String, String> files = getUploadFilesMap();
		final Map<String, String> metas = getMetas(files);

		final User user = getModel(User.class);
		String password = user.getPassword();

		if (StringUtils.isBlank(user.getUsername())) {
			renderAjaxResultForError("用户名不能为空。");
			return;
		}

		if (user.getId() == null) {

			User dbUser = UserQuery.me().findUserByUsername(user.getUsername());
			if (dbUser != null) {
				renderAjaxResultForError("该用户名已经存在，不能添加。");
				return;
			}

			if (StringUtils.isNotBlank(user.getEmail())) {
				dbUser = UserQuery.me().findUserByEmail(user.getEmail());
				if (dbUser != null) {
					renderAjaxResultForError("邮件地址已经存在，不能添加该邮箱。");
					return;
				}
			}
			if (StringUtils.isNotBlank(user.getMobile())) {
				dbUser = UserQuery.me().findUserByMobile(user.getMobile());
				if (dbUser != null) {
					renderAjaxResultForError("手机号码地址已经存在，不能添加该手机号码。");
					return;
				}
			}

			// 新增用户
			String salt = EncryptUtils.salt();
			user.setSalt(salt);
			user.setCreated(new Date());
			if (StringUtils.isNotEmpty(password)) {
				password = EncryptUtils.encryptPassword(password, salt);
				user.setPassword(password);
			}
		} else {

			if (StringUtils.isNotBlank(user.getEmail())) {
				User dbUser = UserQuery.me().findUserByEmail(user.getEmail());
				if (dbUser != null && user.getId().compareTo(dbUser.getId()) != 0) {
					renderAjaxResultForError("邮件地址已经存在，不能修改为该邮箱。");
					return;
				}
			}
			if (StringUtils.isNotBlank(user.getMobile())) {
				User dbUser = UserQuery.me().findUserByMobile(user.getMobile());
				if (dbUser != null && user.getId().compareTo(dbUser.getId()) != 0) {
					renderAjaxResultForError("手机号码地址已经存在，不能修修改为该手机号码。");
					return;
				}
			}

			// 用户修改了密码
			if (StringUtils.isNotEmpty(password)) {
				User dbUser = UserQuery.me().findById(user.getId());
				user.setSalt(dbUser.getSalt());
				password = EncryptUtils.encryptPassword(password, dbUser.getSalt());
				user.setPassword(password);
			} else {
				// 清除password，防止密码被置空
				user.remove("password");
			}
		}

		if (files != null && files.containsKey("user.avatar")) {
			user.setAvatar(files.get("user.avatar"));
		}

		boolean saved = Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {

				if (!user.saveOrUpdate()) {
					return false;
				}

				if (metas != null) {
					for (Map.Entry<String, String> entry : metas.entrySet()) {
						user.saveOrUpdateMetadta(entry.getKey(), entry.getValue());
					}
				}

				return true;
			}
		});

		if (saved) {
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}

		user.saveOrUpdate();

		renderAjaxResultForSuccess("ok");
	}

	@PermAnnotation("user-view")
	public void info() {
		User user = getLoginedUser();
		if (user != null) {
			user = UserQuery.me().findById(user.getId());
		}
		setAttr("user", user);

		String templateHtml = "admin_user_info.html";
		if (TemplateManager.me().existsFile(templateHtml)) {
			setAttr("include", TemplateManager.me().currentTemplatePath() + "/" + templateHtml);
			render("edit.html");
			return;
		}

		templateHtml = "admin_user_edit.html";
		if (TemplateManager.me().existsFile(templateHtml)) {
			setAttr("include", TemplateManager.me().currentTemplatePath() + "/" + templateHtml);
			render("edit.html");
			return;
		}

		setAttr("include", "_edit_include.html");
		render("edit.html");

	}
	
	@PermAnnotation("perm-edit")
	public void perm(){
		BigInteger id = getParaToBigInteger("id");
		if (id != null) {
			User user = UserQuery.me().findByIdNoCache(id);
			if(user != null){
				String perms = this.getPara("perms");
				if(StrKit.isBlank(perms) == true){
					//设置用户权限
					PermGroup[] permGroups = PermKit.getCopyofPermGroups();
					String userPermStr = user.getPerm();
					if(StrKit.notBlank(userPermStr)){
						//为了freemarker中的预先选中
						String[] permAry = userPermStr.split(",");
						List<String> permList = Arrays.asList(permAry);
						for(PermGroup grp : permGroups){
							List<PermGroup.Perm> m = grp.getPerms();
							Iterator<PermGroup.Perm> iter = m.iterator();
							while(iter.hasNext()){
								PermGroup.Perm p = iter.next();
								if(permList.contains(p.getKey())){
									p.setChecked(true);
								}else{
									p.setChecked(false);
								}
							}
						}
					}
					setAttr("user", UserQuery.me().findById(id));
					setAttr("permGroups", permGroups);
					setAttr("include", "_perm_include.html");
					render("perm.html");
					return;
				}else{
					//保存用户权限
					String[] permsFromRemote = perms.split(",");
					for(String p : permsFromRemote){
						if(PermKit.isLegalPerm(p) == false){
							this.renderAjaxResultForError("错误：无法识别的用户权限编码 " + p);
							return;
						}
					}
					user.setPerm(perms);
					boolean flag = user.update();
					if(flag) 
						this.renderAjaxResultForSuccess();
					else
						this.renderAjaxResultForError("错误: 保存权限失败");
					return;
				}
			}
		}
		renderText("错误：用户不存在");
	}

	public void frozen() {
		BigInteger id = getParaToBigInteger("id");
		if (id != null) {
			User user = UserQuery.me().findById(id);
			user.setStatus(User.STATUS_FROZEN);
			user.update();
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}
	}

	public void restore() {
		BigInteger id = getParaToBigInteger("id");
		if (id != null) {
			User user = UserQuery.me().findById(id);
			user.setStatus(User.STATUS_NORMAL);
			user.update();
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}
	}

	@Override
	@PermAnnotation("user-delete")
	public void delete() {
		BigInteger id = getParaToBigInteger("id");
		if (id == null) {
			renderAjaxResultForError();
			return;
		}

		User user = getLoginedUser();
		if (user.getId().compareTo(id) == 0) {
			renderAjaxResultForError("不能删除自己...");
			return;
		}

		super.delete();
	}

}
