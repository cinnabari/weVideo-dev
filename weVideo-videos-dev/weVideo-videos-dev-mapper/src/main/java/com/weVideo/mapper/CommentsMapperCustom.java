package com.weVideo.mapper;

import java.util.List;

import com.weVideo.pojo.Comments;
import com.weVideo.pojo.vo.CommentsVO;
import com.weVideo.utils.MyMapper;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	public List<CommentsVO> queryComments(String videoId);
}