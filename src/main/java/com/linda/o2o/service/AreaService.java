package com.linda.o2o.service;

import com.linda.o2o.entity.Area;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AreaService {
    public  static final String AREALISTKEY = "arealist";

    List<Area>getAreaList();
}
