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
package yjt.forum;

import java.math.BigInteger;

import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.BaseFrontController;
import io.jpress.model.Comment;
import io.jpress.model.Content;
import io.jpress.model.query.CommentQuery;
import io.jpress.model.query.ContentQuery;
import io.jpress.router.RouterMapping;

@RouterMapping(url = "/forum", viewPath="/")
public class ForumController extends BaseFrontController {

	public void index() {
		render("forum.html");
	}
	
	public void query() {
		String typeRaw = getPara("type","valuable");
		boolean type = "valuable".equals(typeRaw);
		Page<Content> contentAry = ContentQuery.me().paginate(getPageNumber(), getPageSize(), new String[] {"article"}, "normal", type);
		this.renderJson(contentAry);
	}
	
	public void newf() {
		render("forum_new.html");
	}
	
	public void detail() {
		BigInteger id = getParaToBigInteger("id", BigInteger.ZERO);
		Content content = ContentQuery.me().findById(id);
		if(content == null) {
			this.renderText("帖子不存在");
			return;
		}
		Page<Comment> commentAry = CommentQuery.me().paginateByContentId(1, 10, id);
		setAttr("content", content);
		setAttr("page", commentAry);
		render("forum_detail.html");
	}
}
