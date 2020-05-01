package com.weVideo.service;


import java.util.List;

import com.weVideo.pojo.Comments;
import com.weVideo.pojo.Videos;
import com.weVideo.utils.PagedResult;

public interface VideoService {

	
	public String saveVideo(Videos video);
	//修改视频封面
	public String updateVideo(String videoId, String coverPath);
	
	public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize2);
	
	public List<String> getHotwords();
	
	public void userLikeVideo(String userId,String videoId,String videoCreator);
	
	public void userUnlikeVideo(String userId,String videoId,String videoCreator);
	
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize);
	
	public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize);
	
	public void saveComment(Comments comment);
	
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize);
}
