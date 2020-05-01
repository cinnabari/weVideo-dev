package com.weVideo.serviceImpl;

import java.util.Date;
import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weVideo.mapper.CommentsMapper;
import com.weVideo.mapper.CommentsMapperCustom;
import com.weVideo.mapper.SearchRecordsMapper;
import com.weVideo.mapper.UsersLikeVideosMapper;
import com.weVideo.mapper.UsersMapper;
import com.weVideo.mapper.VideosMapper;
import com.weVideo.mapper.VideosMapperCustom;
import com.weVideo.pojo.Comments;
import com.weVideo.pojo.SearchRecords;
import com.weVideo.pojo.UsersLikeVideos;
import com.weVideo.pojo.Videos;
import com.weVideo.pojo.vo.CommentsVO;
import com.weVideo.pojo.vo.VideosVO;
import com.weVideo.service.VideoService;
import com.weVideo.utils.PagedResult;
import com.weVideo.utils.TimeAgoUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class VideoServiceImpl implements VideoService {

	@Autowired
	private VideosMapper videosMapper;

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private VideosMapperCustom videosMapperCustom;

	@Autowired
	private SearchRecordsMapper searchRecordsMapper;

	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper;

	@Autowired
	private CommentsMapper commentsMapper;

	@Autowired
	private CommentsMapperCustom commentsMapperCustom;

	@Autowired
	private Sid sid;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveVideo(Videos video) {

		String id = sid.nextShort();
		video.setId(id);
		videosMapper.insertSelective(video);
		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String updateVideo(String videoId, String coverPath) {

		Videos video = new Videos();
		video.setId(videoId);
		video.setCoverPath(coverPath);

		videosMapper.updateByPrimaryKeySelective(video);
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {

		String desc = video.getVideoDesc();

		if (isSaveRecord != null && isSaveRecord == 1) {

			SearchRecords record = new SearchRecords();
			String id = sid.nextShort();
			record.setId(id);
			record.setContent(desc);

			searchRecordsMapper.insert(record);
		}

		PageHelper.startPage(page, pageSize);

		List<VideosVO> list = videosMapperCustom.queryAllVideos(desc);

		PageInfo<VideosVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<String> getHotwords() {

		return searchRecordsMapper.getHotwords();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userLikeVideo(String userId, String videoId, String videoCreator) {

		String likeId = sid.nextShort();
		UsersLikeVideos ulv = new UsersLikeVideos();
		ulv.setId(likeId);
		ulv.setUserId(userId);
		ulv.setVideoId(videoId);
		usersLikeVideosMapper.insert(ulv);

		videosMapperCustom.addVideoLikeCount(videoId);
		usersMapper.addReceiveLikeCount(userId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userUnlikeVideo(String userId, String videoId, String videoCreator) {

		Example example = new Example(UsersLikeVideos.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("videoId", videoId);

		usersLikeVideosMapper.deleteByExample(example);

		videosMapperCustom.reduceVideoLikeCount(videoId);
		usersMapper.reduceReceiveLikeCount(userId);

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVO> list = videosMapperCustom.queryMyLikeVideos(userId);

		PageInfo<VideosVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVO> list = videosMapperCustom.queryMyFollowVideos(userId);

		PageInfo<VideosVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveComment(Comments comment) {

		String id = sid.nextShort();
		comment.setId(id);
		comment.setCreateTime(new Date());
		commentsMapper.insert(comment);

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);

		List<CommentsVO> list = commentsMapperCustom.queryComments(videoId);

		for (CommentsVO c : list) {
			String timeAgo = TimeAgoUtils.format(c.getCreateTime());
			c.setTimeAgoStr(timeAgo);
		}

		PageInfo<CommentsVO> pageList = new PageInfo<>(list);

		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(list);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());

		return grid;
	}

}
