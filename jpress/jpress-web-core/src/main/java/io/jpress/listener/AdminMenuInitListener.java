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
package io.jpress.listener;

import java.util.List;

import io.jpress.menu.MenuGroup;
import io.jpress.menu.MenuItem;
import io.jpress.menu.MenuManager;
import io.jpress.message.Message;
import io.jpress.message.MessageListener;
import io.jpress.message.annotation.Listener;
import io.jpress.template.Template;
import io.jpress.template.TemplateManager;
import io.jpress.template.TplModule;
import io.jpress.template.TplTaxonomyType;
import io.jpress.utils.StringUtils;

@Listener(action = MenuManager.ACTION_INIT_MENU, async = false)
public class AdminMenuInitListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		Object temp = message.getData();
		if (temp == null && !(temp instanceof MenuManager)) {
			return;
		}

		MenuManager menuMnager = (MenuManager) temp;
		initModuleMenuGroup(menuMnager);

		//menuMnager.addMenuGroup(createAttachmentMenuGroup());

		//menuMnager.addMenuGroup(createWechatMenuGroup());

		//menuMnager.addMenuGroup(MenuGroup.createBlockGroup());
		menuMnager.addMenuGroup(createUserMenuGroup());
		
		menuMnager.addMenuGroup(createContractMenuGroup());
		menuMnager.addMenuGroup(createApplyMenuGroup());
		menuMnager.addMenuGroup(createChargeMenuGroup());
		menuMnager.addMenuGroup(createDrawMenuGroup());
		menuMnager.addMenuGroup(createAdMenuGroup());
		menuMnager.addMenuGroup(createReportMenuGroup());
		menuMnager.addMenuGroup(createFeedbackMenuGroup());
		menuMnager.addMenuGroup(createOperationLogMenuGroup());
		//menuMnager.addMenuGroup(createTemplateMenuGroup());
		//menuMnager.addMenuGroup(createAddonMenuGroup());
		menuMnager.addMenuGroup(createSettingMenuGroup());
		//menuMnager.addMenuGroup(createToolsMenuGroup());
	}

	public void initModuleMenuGroup(MenuManager menuMnager) {
		Template t = TemplateManager.me().currentTemplate();
		if (t == null || t.getModules() == null) {
			return;
		}

		List<TplModule> modules = t.getModules();
		for (TplModule module : modules) {
			String iconClass = module.getIconClass();
			if (StringUtils.isBlank(iconClass)) {
				iconClass = "fa fa-file-text-o";
			}
			MenuGroup group = new MenuGroup(module.getName(), iconClass, module.getTitle());

			group.addMenuItem(new MenuItem("list", "/admin/content?m=" + module.getName(), module.getListTitle()));
			group.addMenuItem(new MenuItem("edit", "/admin/content/edit?m=" + module.getName(), module.getAddTitle()));

			List<TplTaxonomyType> types = module.getTaxonomyTypes();
			if (types != null && !types.isEmpty()) {
				for (TplTaxonomyType type : types) {
					group.addMenuItem(new MenuItem(type.getName(),
							"/admin/taxonomy?m=" + module.getName() + "&t=" + type.getName(), type.getTitle()));
				}
			}

			if (StringUtils.isNotBlank(module.getCommentTitle())) {
				group.addMenuItem(new MenuItem("comment", "/admin/comment?t=comment&m=" + module.getName(),
						module.getCommentTitle()));
			}

			menuMnager.addMenuGroup(group);
		}
	}

	private MenuGroup createWechatMenuGroup() {
		MenuGroup group = new MenuGroup("wechat", "fa fa-weixin", "微信");

		{
			group.addMenuItem(new MenuItem("r", "/admin/wechat", "自动回复"));
			group.addMenuItem(new MenuItem("rd", "/admin/wechat/reply_default", "默认回复"));
			group.addMenuItem(new MenuItem("menu", "/admin/wechat/menu", "菜单设置"));
			group.addMenuItem(new MenuItem("option", "/admin/wechat/option", "微信设置"));
		}
		return group;
	}

	private MenuGroup createAttachmentMenuGroup() {
		MenuGroup group = new MenuGroup("attachment", "fa fa-file-image-o", "附件");
		{
			group.addMenuItem(new MenuItem("list", "/admin/attachment", "所有附件"));
			group.addMenuItem(new MenuItem("upload", "/admin/attachment/upload", "上传"));
		}
		return group;
	}

	private MenuGroup createUserMenuGroup() {
		MenuGroup group = new MenuGroup("user", "fa fa-user", "用户");
		{
			group.addMenuItem(new MenuItem("list", "/admin/user", "用户列表", "user-list"));
			group.addMenuItem(new MenuItem("edit", "/admin/user/add", "添加用户", "user-add"));
			group.addMenuItem(new MenuItem("info", "/admin/user/info", "我的资料", "user-view"));
		}
		return group;
	}
	
	
	
	private MenuGroup createContractMenuGroup(){
		MenuGroup group = new MenuGroup("contract", "fa fa-file-text-o", "借条管理");
		{
			group.addMenuItem(new MenuItem("list", "/admin/contract", "借条列表", "contract-list"));
			group.addMenuItem(new MenuItem("list", "/admin/contract?today=1", "今日借条", "contract-list"));
		}
		return group;
	}
	
	private MenuGroup createApplyMenuGroup(){
		MenuGroup group = new MenuGroup("apply", "fa fa-file-text-o", "申请管理");
		{
			group.addMenuItem(new MenuItem("list", "/admin/apply", "申请列表", "apply-list"));
			//group.addMenuItem(new MenuItem("info", "/admin/apply/stat", "申请统计"));
		}
		return group;
	}
	
	private MenuGroup createDrawMenuGroup(){
		MenuGroup group = new MenuGroup("draw", "fa fa-file-text-o", "提现管理");
		{
			group.addMenuItem(new MenuItem("list", "/admin/withdraw", "提现列表", "withdraw-list"));
			group.addMenuItem(new MenuItem("list", "/admin/withdraw?today=1", "今日提现", "withddraw-list"));
		}
		return group;
	}
	
	private MenuGroup createChargeMenuGroup() {
		MenuGroup group = new MenuGroup("charge", "fa fa-file-text-o", "充值管理");
		{
			group.addMenuItem(new MenuItem("list", "/admin/unionpaylog", "充值列表", "unionpaylog-list"));
			group.addMenuItem(new MenuItem("list", "/admin/unionpaylog?today=1", "今日充值", "unionpaylog-list"));

		}
		return group;
	}
	
	private MenuGroup createAdMenuGroup(){
		MenuGroup group = new MenuGroup("ad", "fa fa-file-text-o", "广告管理");
		{
			group.addMenuItem(new MenuItem("list", "/admin/ad", "广告列表", "ad-list"));
			group.addMenuItem(new MenuItem("add", "/admin/ad/add", "添加广告", "ad-add"));
		}
		return group;
	}
	
	private MenuGroup createFeedbackMenuGroup(){
		MenuGroup group = new MenuGroup("feedback", "fa fa-file-text-o", "反馈管理");
		{
			group.addMenuItem(new MenuItem("list", "/admin/feedback", "反馈列表", "feedback-list"));
		}
		return group;
	}
	private MenuGroup createOperationLogMenuGroup(){
		MenuGroup group = new MenuGroup("oplog", "fa fa-file-text-o", "操作日志");
		{
			group.addMenuItem(new MenuItem("list", "/admin/oplog", "日志列表", "oplog-list"));
		}
		return group;
	}
	
	
	private MenuGroup createReportMenuGroup(){
		MenuGroup group = new MenuGroup("report", "fa fa-file-text-o", "举报管理");
		{
			group.addMenuItem(new MenuItem("list", "/admin/report", "举报列表", "report-list"));
		}
		return group;
	}
	
	

	private MenuGroup createTemplateMenuGroup() {
		MenuGroup group = new MenuGroup("template", "fa fa-magic", "模板");
		{
			group.addMenuItem(new MenuItem("list", "/admin/template", "所有模板"));
			group.addMenuItem(new MenuItem("install", "/admin/template/install", "模板安装"));
			group.addMenuItem(new MenuItem("menu", "/admin/template/menu", "菜单"));
			group.addMenuItem(new MenuItem("setting", "/admin/template/setting", "设置"));
			group.addMenuItem(new MenuItem("edit", "/admin/template/edit", "编辑"));
		}
		return group;
	}

	private MenuGroup createAddonMenuGroup() {
		MenuGroup group = new MenuGroup("addon", "fa fa-plug", "插件");
		{
			group.addMenuItem(new MenuItem("list", "/admin/addon", "所有插件"));
			group.addMenuItem(new MenuItem("install", "/admin/addon/install", "安装"));
		}
		return group;
	}

	private MenuGroup createSettingMenuGroup() {
		MenuGroup group = new MenuGroup("option", "fa fa-cog", "设置");
		{
			group.addMenuItem(new MenuItem("credittable", "/admin/option/credittable", "信用额度表"));
			group.addMenuItem(new MenuItem("sensitive", "/admin/option/sensitive", "敏感词设置"));
			group.addMenuItem(new MenuItem("list", "/admin/option/web", "常规"));
			/*group.addMenuItem(new MenuItem("edit", "/admin/option/comment", "评论"));
			group.addMenuItem(new MenuItem("n", "/admin/option/notification", "通知"));
			group.addMenuItem(new MenuItem("seo", "/admin/option/seo", "SEO"));
			group.addMenuItem(new MenuItem("watermark", "/admin/option/watermark", "水印"));
			group.addMenuItem(new MenuItem("url", "/admin/option/url", "连接形式"));
			group.addMenuItem(new MenuItem("reg", "/admin/option/register", "登录注册"));
			group.addMenuItem(new MenuItem("cdn", "/admin/option/cdn", "CDN加速"));
			group.addMenuItem(new MenuItem("api", "/admin/api", "API应用"));
			*/
		}

		return group;
	}

	private MenuGroup createToolsMenuGroup() {
		MenuGroup group = new MenuGroup("tools", "fa fa-wrench", "工具");

		{
			group.addMenuItem(new MenuItem("import", "/admin/tools/_import", "导入"));
			group.addMenuItem(new MenuItem("export", "/admin/tools/export", "导出"));
			group.addMenuItem(new MenuItem("sync", "/admin/tools/sync", "同步"));
		}
		return group;
	}

}
