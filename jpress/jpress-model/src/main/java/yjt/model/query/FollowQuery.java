package yjt.model.query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.jpress.model.query.JBaseQuery;
import yjt.model.Follow;

public class FollowQuery extends JBaseQuery{
	protected static final Follow DAO = new Follow();
	private static final FollowQuery QUERY = new FollowQuery();
	
	public static FollowQuery me(){
		return QUERY;
	}
	
	public Follow getFollow(BigInteger followedId, BigInteger followerId){
		if(followedId == null || followerId == null) return null;
		
		StringBuilder sqlBuilder = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();
		

		appendAndIfNotEmpty(sqlBuilder, "followed_id", followedId.toString(), params);
		appendAndIfNotEmpty(sqlBuilder, "follower_id", followerId.toString(), params);

		Follow follow = DAO.doFindFirst(sqlBuilder.toString(), params.toArray());
		return follow;

	}
	
	/**
	 * 获取toId的粉丝所有Follow记录
	 * @param toId
	 * @return
	 */
	public List<Follow> getFollowerTo(BigInteger toId){
		StringBuilder sqlBuilder = new StringBuilder("select * from follow f ");
		LinkedList<Object> params = new LinkedList<Object>();
		appendIfNotEmpty(sqlBuilder, "f.followed_id", toId.toString(), params, true);
		return DAO.find(sqlBuilder.toString(), params.toArray());
	}
	
	/**
	 * 获取toId的粉丝所有id
	 * @param toId
	 * @return
	 */
	public BigInteger[] getFollowerList(BigInteger toId){
		ArrayList<BigInteger> ary = new ArrayList<BigInteger>();
		List<Follow> fans = getFollowedFrom(toId);
		for(Follow f : fans ){
			ary.add(f.getFollowerId());
		}
		return (BigInteger[]) ary.toArray();
	}
	
	/**
	 * 获取fromId关注的所有Follow记录
	 * @param fromId
	 * @return
	 */
	public List<Follow> getFollowedFrom(BigInteger fromId){
		StringBuilder sqlBuilder = new StringBuilder("select * from follow f ");
		LinkedList<Object> params = new LinkedList<Object>();
		appendIfNotEmpty(sqlBuilder, "f.follower_id", fromId.toString(), params, true);
		return DAO.find(sqlBuilder.toString(), params.toArray());
	}
	
	/**
	 * 获取fromId关注的所有用户id
	 * @param fromId
	 * @return
	 */
	public BigInteger[] getFollowedList(BigInteger fromId){
		ArrayList<BigInteger> ary = new ArrayList<BigInteger>();
		List<Follow> tops = getFollowedFrom(fromId);
		for(Follow t : tops ){
			ary.add(t.getFollowedId());
		}
		return (BigInteger[]) ary.toArray();
	}
	
	/** 
	 * 凭用户id获取相互关注列表
	 * @param id
	 * @return
	 */
	public BigInteger[] getEachFollowList(BigInteger id){
		ArrayList<BigInteger> ary = new ArrayList<BigInteger>();
		//获取所有粉丝
		List<Follow> fans = this.getFollowerTo(id);
		//获取所有被id关注的人
		List<Follow> tops = this.getFollowedFrom(id);
		
	    /*tops      followerid(自己)    followedid(别人)
		  fans      followerid(别人)    followedid(自己)
		  tops.followedid == fans.followerid成立时，就是相互关注*/
		 
		for(Follow f : fans){
			for(Follow t : tops ){
				if(t.getFollowedId().equals(f.getFollowerId())){
					ary.add(t.getFollowedId());
					break;
				}
			}
		}
		return (BigInteger[]) ary.toArray();
	}
	
	/**
	 * 凭fans记录和tops记录，取交集获取相互关注用户id
	 * @param fans
	 * @param tops
	 * @return
	 */
	public BigInteger[] getEachFollowList(BigInteger[] fans, BigInteger[] tops){
		ArrayList<BigInteger> ary = new ArrayList<BigInteger>();
		if(fans == null || tops == null) return (BigInteger[]) ary.toArray();
		for(BigInteger f : fans){
			for(BigInteger t : tops ){
				if(t.equals(f)){
					ary.add(t);
					break;
				}
			}
		}
		return (BigInteger[]) ary.toArray();
	}
}
