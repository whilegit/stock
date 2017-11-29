/**
 * Author: linzhongren 6215714@qq.com
 */
package yjt.forum;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.BaseFrontController;
import io.jpress.model.Comment;
import io.jpress.model.Content;
import io.jpress.model.User;
import io.jpress.model.query.CommentQuery;
import io.jpress.model.query.ContentQuery;
import io.jpress.model.query.UserQuery;
import io.jpress.router.RouterMapping;
import io.jpress.utils.StringUtils;
import yjt.core.Utils.Common;

@RouterMapping(url = "/forum", viewPath="/")
public class ForumController extends BaseFrontController {

	public void index() {
		BigInteger memberID = getAttr("memberID");
		//memberID = BigInteger.valueOf(8L);
		if(memberID != null) {
			User user = UserQuery.me().findById(memberID);
			String nickname = user.getNickname();
			if(StrKit.isBlank(nickname)) {
				setAttr("shouldSetNickname", 1);
			}
		}
		render("forum.html");
	}
	
	public void query() {
		String typeRaw = getPara("type","valuable");
		boolean type = "valuable".equals(typeRaw);
		Page<Content> contentAry = ContentQuery.me().paginate(getPageNumber(), getPageSize(), new String[] {"article"}, "normal", type);
		if(contentAry != null && contentAry.getList() != null && contentAry.getList().size() > 0) {
			List<Content> list= contentAry.getList();
			BigInteger[] usersIdAry = new BigInteger[list.size()];
			int index = 0;
			for(Content content : list) {
				usersIdAry[index++] = content.getUserId();
				long view_content = content.getViewCount();
				content.setViewCount(view_content + 1 );
				content.update();
			}
			List<User> userAry = UserQuery.me().findList(usersIdAry);
			for(Content content : list) {
				BigInteger uid = content.getUserId();
				if(uid == null) continue;
				for(User user : userAry) {
					if(user.getId().equals(uid)) {
						String author = StringUtils.isNotBlank(user.getNickname()) ? user.getNickname() : user.getRealname();
						String avatar = user.getAvatar();
						if(StrKit.isBlank(avatar)) avatar = "";
						content.setAuthor(author + "@&@" + avatar);
						break;
					}
				}
			}
		}
		this.renderJson(contentAry);
	}
	
	public void query_comment() {
		BigInteger contentId = getParaToBigInteger("contentId",BigInteger.ZERO);
		Content content = ContentQuery.me().findById(contentId);
		if(content == null) {
			this.renderText("帖子不存在");
			return;
		}
		Page<Comment> commentAry = CommentQuery.me().paginateByContentId(getPageNumber(), getPageSize(), contentId);
		this.renderJson(commentAry);
	}
	
	public void newf() {
		render("forum_new.html");
	}
	
	@Before(MemberTokenInterceptor.class)
	public void newf_post() {
		BigInteger uid = getAttr("memberID");
		//BigInteger uid = BigInteger.ZERO;
		String text = getPara("text", "");
		String title = getPara("title", "");
		if(StrKit.isBlank(title)) {
			this.renderAjaxResult("设个标题吧", 1);
			return;
		}
		if(StrKit.isBlank(text)) {
			this.renderAjaxResult("写点内容吧", 2);
			return;
		}
		
		//合法性检查
		if(Common.checkSensitive(text) || Common.checkSensitive(title) ){
			this.renderAjaxResult("对不起，您提交的内容可能违反了相关法律法规", 3);
			return;
		}
		Content content = new Content();
		content.setTitle(title);
		content.setText(text);
		content.setModule("article");
		content.setUserId(uid);
		content.setCreated(new Date());
		content.setStatus(Content.STATUS_NORMAL);
		content.save();
		this.renderAjaxResult("发布成功", 0);
	}
	
	public void detail() {
		BigInteger id = getParaToBigInteger("id", BigInteger.ZERO);
		Content content = ContentQuery.me().findById(id);
		if(content == null) {
			this.renderText("帖子不存在");
			return;
		}
		int page = 1;
		int size = 10;
		setAttr("pageIndex", page);
		setAttr("pageSize", size);
		Page<Comment> commentAry = CommentQuery.me().paginateByContentId(page, size, id);
		setAttr("content", content);
		setAttr("page", commentAry);
		
		render("forum_detail.html");
	}
	
	@Before(MemberTokenInterceptor.class)
	public void postComment(){
		BigInteger uid = getAttr("memberID");
		//BigInteger uid = BigInteger.ONE;
		User user = UserQuery.me().findById(uid);
		BigInteger contentId = getParaToBigInteger("contentId", BigInteger.ZERO);
		Content content = ContentQuery.me().findById(contentId);
		if(content == null) {
			this.renderAjaxResult("帖子不存在", 1);
			return;
		}
		String text = this.getPara("text", "");
		if(StrKit.isBlank(text)){
			this.renderAjaxResult("写点内容吧", 2);
			return;
		}
		//合法性检查
		if(Common.checkSensitive(text)){
			this.renderAjaxResult("对不起，您提交的内容可能违反了相关法律法规", 3);
			return;
		}
		Comment comment = new Comment();
		comment.setContentId(contentId);
		comment.setContentModule("article");
		comment.setIp(getIPAddress());
		comment.setAgent(getUserAgent());
		comment.setText(text);
		String author = StringUtils.isNotBlank(user.getNickname()) ? user.getNickname() : user.getRealname();
		comment.setAuthor(author);
		comment.setUserId(uid);
		comment.setCreated(new Date());
		comment.setStatus(Comment.STATUS_NORMAL);
		comment.save();
		this.renderAjaxResult("跟帖成功", 0);
		return;
	}
	
	@Before(MemberTokenInterceptor.class)
	public void setNickname() {
		String nickname = getPara("nickname", "");
		BigInteger uid = getAttr("memberID");
		//uid = BigInteger.valueOf(8L);
		User user = UserQuery.me().findById(uid);
		if(user == null) {
			this.renderAjaxResult("对不起，用户不存在", 1);
			return;
		}
		if(Common.checkSensitive(nickname)){
			this.renderAjaxResult("对不起，您提交的昵称可能违反了相关法律法规", 2);
			return;
		}
		user.setNickname(nickname);
		user.update();
		this.renderAjaxResult("昵称设置成功", 0);
		return;
	}
}
