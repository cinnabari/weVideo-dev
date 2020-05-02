package com.weVideo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.weVideo.utils.RedisOperator;

@RestController
public class BasicController {
	
	@Autowired
	public RedisOperator redis;
	
	public static final String USER_REDIS_SESSION = "user-redis-session";
	//文件保存的命名空间
	public static final String FILE_SPACE = "G:/wevideo_res";
	
	//ffmpeg所在目录
	public static final String FFMPEG_EXE = "G:\\wevideo_res\\ffmpeg\\bin\\ffmpeg.exe";
	
	public static final Integer PAGE_SIZE = 5;
}
