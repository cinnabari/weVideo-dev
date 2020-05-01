package com.weVideo.mapper;

import java.util.List;

import com.weVideo.pojo.SearchRecords;
import com.weVideo.utils.MyMapper;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
	
	public List<String> getHotwords();
}