package com.weindependent.app.service.impl;

import com.weindependent.app.database.mapper.weindependent.MostSavedMapper;
import com.weindependent.app.service.SavedCountService;
import com.weindependent.app.database.dataobject.SavedCountDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SavedCountServiceImpl implements SavedCountService {

    @Autowired
    private MostSavedMapper mostSavedMapper;

    @Override
    public Map<Integer, Integer> getSavedCountMapByArticleIds(List<Integer> articleIds) {
        // 调用 Mapper 层批量查询收藏数数据
        List<SavedCountDO> savedCountList = mostSavedMapper.getSavedCountsByArticleIds(articleIds);
        // 将列表转换为 Map，key 为文章ID，value 为对应的收藏数
        return savedCountList.stream()
                .collect(Collectors.toMap(SavedCountDO::getArticleId, SavedCountDO::getSavedCount));
    }
}
