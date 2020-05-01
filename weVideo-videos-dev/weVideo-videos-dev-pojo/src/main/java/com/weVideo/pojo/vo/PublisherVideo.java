package com.weVideo.pojo.vo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="发布视频", description="这是发布视频")
public class PublisherVideo {
	
	public UsersVO publisher;
	
	public boolean userLikeVideo;

	public UsersVO getPublisher() {
		return publisher;
	}

	public void setPublisher(UsersVO publisher) {
		this.publisher = publisher;
	}

	public boolean isUserLikeVideo() {
		return userLikeVideo;
	}

	public void setUserLikeVideo(boolean userLikeVideo) {
		this.userLikeVideo = userLikeVideo;
	}
	
	
}