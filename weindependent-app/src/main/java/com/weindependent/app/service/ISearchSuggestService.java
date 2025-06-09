package com.weindependent.app.service;

import java.util.*;
import java.io.IOException;

public interface ISearchSuggestService {
    List<String> suggestKeywords(String query) throws IOException;
}