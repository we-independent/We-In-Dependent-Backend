package com.weindependent.app.service.impl;

import com.weindependent.app.service.ISearchSuggestService;
import com.weindependent.app.database.mapper.weindependent.BlogArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchSuggestServiceImpl implements ISearchSuggestService {

    @Autowired
    private BlogArticleMapper blogMapper;

    @Override
    public List<String> suggestKeywords(String inputText) {
        List<String> keywords = Arrays.stream(inputText.toLowerCase().split("\\s+"))
                .map(k -> k.replaceAll("[^a-z0-9]", ""))
                .filter(k -> !k.isEmpty())
                .collect(Collectors.toList());

        if (keywords.isEmpty()) return Collections.emptyList();

        return blogMapper.suggestTitles(keywords);
    }
}