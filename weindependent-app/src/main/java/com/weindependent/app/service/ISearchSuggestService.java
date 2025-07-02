package com.weindependent.app.service;

import org.apache.ibatis.annotations.Param;
import java.util.*;
import java.io.IOException;

public interface ISearchSuggestService {
    List<String> suggestKeywords(String query) throws IOException;
}