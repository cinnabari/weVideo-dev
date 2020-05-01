package com.weVideo.service;


import java.util.List;

import com.weVideo.pojo.Bgm;

public interface BgmService {

	public List<Bgm> queryBgmList();
	
	public Bgm queryBgmById(String bgmId);
	
}
