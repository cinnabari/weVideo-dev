package com.weVideo.service;

import com.weVideo.utils.PagedResult;
import com.weVideo.pojo.Users;

public interface UsersService {

	public PagedResult queryUsers(Users user, Integer page, Integer pageSize);
	
}
