package com.weVideo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weVideo.pojo.Videos;
import com.weVideo.pojo.vo.VideosVO;
import com.weVideo.utils.MyMapper;

public interface VideosMapperCustom extends MyMapper<Videos> {
	public List<VideosVO> queryAllVideos(@Param("videoDesc") String desc);
	
	public void addVideoLikeCount(String videoId);
	
	public void reduceVideoLikeCount(String videoId);

	public List<VideosVO> queryMyFollowVideos(String userId);

	public List<VideosVO> queryMyLikeVideos(String userId);
	
	
}