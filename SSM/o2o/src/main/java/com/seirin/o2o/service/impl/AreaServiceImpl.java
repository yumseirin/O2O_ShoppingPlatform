package com.seirin.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seirin.o2o.dao.AreaDao;
import com.seirin.o2o.entity.Area;
import com.seirin.o2o.service.AreaService;

@Service
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaDao areaDao;
	
	public List<Area> getAreaList() {
		return areaDao.queryArea();
	}

}
