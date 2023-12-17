package com.jingdianjichi.subject.infra.basic.service.impl;

import com.alibaba.fastjson.JSON;
import com.jingdianjichi.subject.infra.basic.entity.SubjectInfoEs;
import com.jingdianjichi.subject.infra.basic.esRepo.SubjectEsRepository;
import com.jingdianjichi.subject.infra.basic.service.SubjectEsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SubjectEsServiceImpl implements SubjectEsService {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private SubjectEsRepository subjectEsRepository;

    @Override
    public void createIndex() {
        IndexOperations indexOperations =
                elasticsearchRestTemplate.indexOps(SubjectInfoEs.class);
        indexOperations.create();
        Document mapping = indexOperations.createMapping(SubjectInfoEs.class);
        indexOperations.putMapping(mapping);
    }

    @Override
    public void addDoc() {
        List<SubjectInfoEs> list = new ArrayList<>();
        list.add(new SubjectInfoEs(1L,"redis是什么","redis是一个缓存","鸡翅",new Date()));
        list.add(new SubjectInfoEs(2L,"mysql是什么","mysql是数据库","鸡翅",new Date()));
        subjectEsRepository.saveAll(list);
    }

    @Override
    public void find() {
        Iterable<SubjectInfoEs> all = subjectEsRepository.findAll();
        for (SubjectInfoEs subjectInfoEs : all){
            log.info("subjectInfoEs:{}",JSON.toJSONString(subjectInfoEs));
        }
    }

    @Override
    public void search() {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("subjectName","redis"))
                .build();
        SearchHits<SubjectInfoEs> search = elasticsearchRestTemplate.
                search(nativeSearchQuery, SubjectInfoEs.class);
        List<SearchHit<SubjectInfoEs>> searchHits = search.getSearchHits();
        log.info("searchHits:{}", JSON.toJSONString(searchHits));
    }

}
