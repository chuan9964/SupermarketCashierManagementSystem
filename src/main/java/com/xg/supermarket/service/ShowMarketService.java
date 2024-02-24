package com.xg.supermarket.service;

import com.xg.supermarket.vo.DataVo;
import com.xg.supermarket.vo.ShowMarketVo;

import java.util.Date;
import java.util.List;

/**
 * @author chuan9964
 * @version 1.0.0
 * @ClassName ShowMarketService.java
 * @Description 显示销售数据
 * @createTime 2023年06月30日 09:35:00
 */
public interface ShowMarketService {
    ShowMarketVo getTodayShowMarket(Date date);
    List<DataVo> listWeekVos(Date start_date, Date end_date);
    List<DataVo> listCategoryVos(Date date);
}
