package com.weVideo.mapper;

import com.weVideo.pojo.Users;
import com.weVideo.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {

	public void addReceiveLikeCount(String userId);

	public void reduceReceiveLikeCount(String userId);

	public void addFansCount(String userId);

	public void addFollersCount(String userId);

	public void reduceFansCount(String userId);

	public void reduceFollersCount(String userId);

}