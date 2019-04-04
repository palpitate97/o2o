package com.linda.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linda.o2o.cache.JedisUtil;
import com.linda.o2o.dao.AreaDao;
import com.linda.o2o.entity.Area;
import com.linda.o2o.service.AreaService;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private AreaDao areaDao;

    private static Logger logger =  LoggerFactory.getLogger(AreaServiceImpl.class);
    @Override
    @Transactional
    public List<Area> getAreaList() {
        String key = AREALISTKEY;
        List<Area>areaList = null;
        ObjectMapper mapper = new ObjectMapper() ;
       if(!jedisKeys.exists(AREALISTKEY)){
            areaList = areaDao.queryArea();
            String jsonString;
            try {
                jsonString = mapper.writeValueAsString(areaList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new RuntimeException(e.getMessage());

            }
            jedisStrings.set(key,jsonString);

        }else{
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,Area.class);
            try {
                areaList = mapper.readValue(jsonString,javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

        }
        return areaList;
    }
}
