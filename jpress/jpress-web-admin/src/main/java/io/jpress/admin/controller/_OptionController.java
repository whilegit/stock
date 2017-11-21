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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.mchange.io.FileUtils;

import io.jpress.core.JBaseController;
import io.jpress.core.interceptor.ActionCacheClearInterceptor;
import io.jpress.interceptor.UCodeInterceptor;
import io.jpress.message.Actions;
import io.jpress.message.MessageKit;
import io.jpress.model.Option.CanBorrowMoneyItem;
import io.jpress.model.query.OptionQuery;
import io.jpress.router.RouterMapping;
import io.jpress.router.RouterNotAllowConvert;
import io.jpress.utils.StringUtils;
import yjt.core.Utils.Common;
import yjt.core.perm.PermAnnotation;

@RouterMapping(url = "/admin/option", viewPath = "/WEB-INF/admin/option")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _OptionController extends JBaseController {

	public void index() {
		System.out.println(getPara());
		render((getPara() == null ? "web" : getPara()) + ".html");
	}

	@Before(UCodeInterceptor.class)
	public void save() {

		HashMap<String, String> filesMap = getUploadFilesMap();

		HashMap<String, String> datasMap = new HashMap<String, String>();

		Map<String, String[]> paraMap = getParaMap();
		if (paraMap != null && !paraMap.isEmpty()) {
			for (Map.Entry<String, String[]> entry : paraMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().length > 0) {
					String value = null;
					for (String v : entry.getValue()) {
						if (StringUtils.isNotEmpty(v)) {
							value = v;
							break;
						}
					}
					datasMap.put(entry.getKey(), value);
				}
			}
		}

		String autosaveString = getPara("autosave");
		if (StringUtils.isNotBlank(autosaveString)) {
			String[] keys = autosaveString.split(",");
			for (String key : keys) {
				if (StringUtils.isNotBlank(key) && !datasMap.containsKey(key)) {
					datasMap.put(key.trim(), getRequest().getParameter(key.trim()));
				}
			}
		}
		
		if(filesMap!=null && !filesMap.isEmpty()){
			datasMap.putAll(filesMap);
		}

		for (Map.Entry<String, String> entry : datasMap.entrySet()) {
			OptionQuery.me().saveOrUpdate(entry.getKey(), entry.getValue());
		}

		MessageKit.sendMessage(Actions.SETTING_CHANGED, datasMap);
		renderAjaxResultForSuccess();
	}


	@PermAnnotation("option-credittable")
	public void credittable() {
		List<CanBorrowMoneyItem> credittable = new ArrayList<CanBorrowMoneyItem>();
		
		String credittableStr = OptionQuery.me().findValueNoCache("credittable");
		if(StrKit.notBlank(credittableStr)) {
			String[] items = credittableStr.split(";");
			for(int i = 0; i<items.length; i++) {
				String[] it = items[i].split(":");
				if(it.length != 2) continue;
				int age = Integer.parseInt(it[0]);
				double amount = Double.parseDouble(it[1]);
				credittable.add(new CanBorrowMoneyItem(age, amount));
			}
			Collections.sort(credittable);
		}
		setAttr("credittable", credittable);
	}
	
	@PermAnnotation("option-credittable")
	public void credittable_edit() {
		String ages = this.getPara("ages", "");
		String credits = this.getPara("credits", "");
		String[] agesAry = ages.split(",");
		String[] creditsAry = credits.split(",");
		if(agesAry.length == 0 || agesAry.length != creditsAry.length) {
			this.renderAjaxResult("错误: 年龄信用设置不能为空", 1);
			return;
		}
		List<CanBorrowMoneyItem> credittable = new ArrayList<CanBorrowMoneyItem>();
		try {
			for(int i = 0; i<agesAry.length; i++) {
				int age = Integer.valueOf(agesAry[i].trim());
				double credit = Double.valueOf(creditsAry[i].trim());
				CanBorrowMoneyItem item = new CanBorrowMoneyItem(age, credit);
				credittable.add(item);
			}
			Collections.sort(credittable);
			String credittableStr = "";
			for(CanBorrowMoneyItem item : credittable) {
				credittableStr += item.getAge() + ":" + item.getAmount() + ";";
			}
			credittableStr = credittableStr.substring(0, credittableStr.length() - 1);
			OptionQuery.me().saveOrUpdate("credittable", credittableStr);
			this.renderAjaxResult("保存成功", 0);
		} catch ( Exception e) {
			this.renderAjaxResult("错误: 格式错误", 2);
		}
	}
	
	@PermAnnotation("option-sensitive") 
	public void sensitive() {
		String path = OptionQuery.me().findValueNoCache("sensitive_file");
		if(StrKit.notBlank(path)) {
			setAttr("path", path);
		}
	}

	@PermAnnotation("option-sensitive") 
	public void sensitive_edit() {
		HashMap<String, String> files = getUploadFilesMap();
		//final Map<String, String> metas = getMetas(files);
		//System.out.println(JSON.toJSONString(metas));
		if(files == null || files.containsKey("file") == false || StrKit.isBlank(files.get("file"))) {
			this.renderAjaxResult("请提供敏感词文件", 2);
			return;
		}
		String text = "";
		try {
			text = FileUtils.getContentsAsString(new File(PathKit.getWebRootPath() + files.get("file")), 64*1024, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.renderAjaxResult("文件读取错误", 3);
			return;
		}
		
		if(StrKit.isBlank(text)) {
			this.renderAjaxResult("敏感词文件不能为空", 4);
			return;
		}
		
		text = text.replaceAll("[\r\n\t]", "");
		String[] words = text.split(",");
		ArrayList<String> okWords = new ArrayList<String>(4068);
		for(int i = 0; i<words.length; i++) {
			String word = words[i].trim();
			if(StrKit.notBlank(word)) {
				okWords.add(word);
			}
		}
		if(okWords.size() == 0) {
			this.renderAjaxResult("敏感词文件必须提供有效的文本", 5);
			return;
		}
		
		StringBuffer sb = new StringBuffer();
		boolean needColon = false;
		for(String w : okWords) {
			if(needColon) sb.append(",");
			sb.append(w);
			needColon = true;
		}
		String finalString = sb.toString();
		OptionQuery.me().saveOrUpdate("sensitive", finalString);
		OptionQuery.me().saveOrUpdate("sensitive_file", files.get("file"));
		Common.loadSensitive();
		
		this.renderAjaxResult("成功更新", 0);
	}
}
