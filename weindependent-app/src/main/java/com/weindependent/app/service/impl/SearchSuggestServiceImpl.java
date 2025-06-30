package com.weindependent.app.service.impl;

import com.weindependent.app.service.ISearchSuggestService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.*;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SearchSuggestServiceImpl implements ISearchSuggestService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public List<String> suggestKeywords(String inputText) throws IOException {
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        CompletionSuggestionBuilder completionSuggestionBuilder =
                SuggestBuilders.completionSuggestion("suggest")
                        .prefix(inputText, Fuzziness.AUTO)
                        .skipDuplicates(true)
                        .size(5);

        suggestBuilder.addSuggestion("blog-suggest", completionSuggestionBuilder);

        SearchRequest searchRequest = new SearchRequest("blog_suggest");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.suggest(suggestBuilder);
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Suggest suggest = searchResponse.getSuggest();
        CompletionSuggestion suggestion = suggest.getSuggestion("blog-suggest");

        List<String> results = new ArrayList<>();
        for (CompletionSuggestion.Entry.Option option : suggestion.getOptions()) {
            results.add(option.getText().string());
        }

        return results;
    }
}