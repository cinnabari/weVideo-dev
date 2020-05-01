package com.weVideo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.util.StringUtil;
import com.weVideo.pojo.Users;
import com.weVideo.pojo.UsersReport;
import com.weVideo.pojo.vo.PublisherVideo;
import com.weVideo.pojo.vo.UsersVO;
import com.weVideo.service.UserService;
import com.weVideo.utils.IMoocJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户相关业务的接口",tags = {"注册和登录的controller"})
@RequestMapping("/user")
public class UserController extends BasicController{
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value="用户上传头像", notes = "用户上传头像的接口")
	@ApiImplicitParam(name="userId",value="用户id",required=true,
	dataType="String",paramType="query")
	@PostMapping("/uploadFace")
	public IMoocJSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorMsg("用户id不能为空");
		}
		
		String fileSpace = "G:/wevideo_res";
		
		String uploadPathDB = "/" + userId + "/face";
		
		if(files != null && files.length > 0) {
			FileOutputStream fileOutputStream = null;
			InputStream inputStream = null;
			
			String fileName = files[0].getOriginalFilename();
			try {
				if(StringUtils.isNoneBlank(fileName)) {
					//文件上传路径 G:/wevideo_res/userId/face/fileName
					String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
					//数据库保存的路径 /userId/face/fileName 
					uploadPathDB += ("/" + fileName);
					
					File outFile = new File(finalFacePath);
					if(outFile.getParent() != null || !outFile.getParentFile().isDirectory()) {
						outFile.getParentFile().mkdirs();
					}
					
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = files[0].getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (fileOutputStream != null) {
					fileOutputStream.flush();
					fileOutputStream.close();
				}
			}
			
			Users user = new Users();
			user.setId(userId);
			user.setFaceImage(uploadPathDB);
			userService.updateUserInfo(user);
			
		}else {
			return IMoocJSONResult.errorMsg("上传出错");
		}
		
			return IMoocJSONResult.ok(uploadPathDB);
		
	}
	
	@ApiOperation(value="查询用户信息", notes = "查询用户信息的接口")
	@ApiImplicitParam(name="userId",value="用户id",required=true,
	dataType="String",paramType="query")
	@PostMapping("/query")
	public IMoocJSONResult query(String userId, String fanId) {
		
		if(StringUtil.isEmpty(userId)) {
			return IMoocJSONResult.errorMsg("用户id不能为空");
		}
		
		Users user = userService.queryUserInfo(userId);
		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(user, userVO);
		
		
		userVO.setFollow(userService.queryIfFollow(userId, fanId));
		
		return IMoocJSONResult.ok(userVO);
	}
	
	@PostMapping("/queryPublisher")
	public IMoocJSONResult queryPublisher(String loginUserId, String videoId,
			String publishUserId) {
		
		if(StringUtil.isEmpty(publishUserId) ) {
			return IMoocJSONResult.errorMsg("");
		}
		
		Users user = userService.queryUserInfo(publishUserId);
		UsersVO publisher = new UsersVO();
		BeanUtils.copyProperties(user, publisher);
		
		boolean userLikeVideo = userService.isUserLikeVideo(loginUserId, videoId);
		PublisherVideo bean = new PublisherVideo();
		bean.setPublisher(publisher);
		bean.setUserLikeVideo(userLikeVideo);
		
		return IMoocJSONResult.ok(bean);
	}
	
	@PostMapping("/beyourfans")
	public IMoocJSONResult beyourfans(String userId, String fanId) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return IMoocJSONResult.errorMsg("");
		}
		
		userService.saveUserFanRelation(userId, fanId);
		
		return IMoocJSONResult.ok("关注成功");
	}
	
	@PostMapping("/dontbeyourfans")
	public IMoocJSONResult dontbeyourfans(String userId, String fanId) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return IMoocJSONResult.errorMsg("");
		}
		
		userService.deleteUserFanRelation(userId, fanId);
		
		return IMoocJSONResult.ok("取消关注成功");
	}
	
	@PostMapping("/reportUser")
	public IMoocJSONResult reportUser(@RequestBody UsersReport usersReport) throws Exception {
		
		// 保存举报信息
		userService.reportUser(usersReport);
		
		return IMoocJSONResult.errorMsg("举报成功...有你平台变得更美好...");
	}
	
}