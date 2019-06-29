package com.liuzi.elasticsearch.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.min.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentile;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentileRanks;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentileRanksAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentiles;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentilesAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.liuzi.elasticsearch.annotation.ESField;
import com.liuzi.elasticsearch.enums.AggsType;
import com.liuzi.elasticsearch.enums.FieldType;
import com.liuzi.elasticsearch.util.Constant;
import com.liuzi.elasticsearch.util.IndexTools;
import com.liuzi.elasticsearch.util.MetaData;

/**
 * 聚合操作
 * @author zsy
 */
@Slf4j
public class ElasticsearchAggregationsImpl extends ElasticsearchIndexImpl{
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> Map aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, 
			String bucketName) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        Field f_metric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        Field f_bucket = clazz.getDeclaredField(bucketName.replaceAll(KEYWORD, ""));
        if (f_metric == null) {
            throw new Exception("metric field is null");
        }
        if (f_bucket == null) {
            throw new Exception("bucket field is null");
        }
        metricName = genKeyword(f_metric, metricName);
        bucketName = genKeyword(f_bucket, bucketName);

        String by = "by_" + bucketName;
        String me = aggsType.toString() + "_" + metricName;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregation = AggregationBuilders.terms(by)
                .field(bucketName);
        //默认按照聚合结果降序排序
        aggregation.order(BucketOrder.aggregation(me, false));
//        aggregation.order(BucketOrder.key(false));
        if (AggsType.COUNT == aggsType) {
            aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.MIN == aggsType) {
            aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.MAX == aggsType) {
            aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.SUM == aggsType) {
            aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.AVG == aggsType) {
            aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);

        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        if(metaData.isLog()){
            log.info(searchSourceBuilder.toString());
        }
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms by_risk_code = aggregations.get(by);
        Map map = new LinkedHashMap();
        for (Terms.Bucket bucket : by_risk_code.getBuckets()) {
            if (AggsType.COUNT == aggsType) {
                ValueCount count = bucket.getAggregations().get(me);
                long value = count.getValue();
                map.put(bucket.getKey(), value);
            } else if (AggsType.MIN == aggsType) {
                ParsedMin min = bucket.getAggregations().get(me);
                double value = min.getValue();
                map.put(bucket.getKey(), value);
            } else if (AggsType.MAX == aggsType) {
                ParsedMax max = bucket.getAggregations().get(me);
                double value = max.getValue();
                map.put(bucket.getKey(), value);
            } else if (AggsType.SUM == aggsType) {
                ParsedSum sum = bucket.getAggregations().get(me);
                double value = sum.getValue();
                map.put(bucket.getKey(), value);
            } else if (AggsType.AVG == aggsType) {
                ParsedAvg avg = bucket.getAggregations().get(me);
                double value = avg.getValue();
                map.put(bucket.getKey(), value);
            }
        }
        return map;
    }

    public <T> List<Down> aggswith2level(String metricName, AggsType aggsType, QueryBuilder queryBuilder, 
    		Class<T> clazz, String... bucketNames) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        Field f_metric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (bucketNames == null) {
            throw new NullPointerException();
        }
        if (bucketNames.length != 2) {
            throw new Exception("仅支持两层下钻聚合!");
        }
        Field[] f_buckets = new Field[bucketNames.length];
        for (int i = 0; i < bucketNames.length; i++) {
            f_buckets[i] = clazz.getDeclaredField(bucketNames[i].replaceAll(KEYWORD, ""));
            if (f_buckets[i] == null) {
                throw new Exception("bucket field is null");
            }
        }
        if (f_metric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(f_metric, metricName);
        String me = aggsType.toString() + "_" + metricName;

        String[] bys = new String[bucketNames.length];
        for (int i = 0; i < f_buckets.length; i++) {
            bucketNames[i] = genKeyword(f_buckets[i], bucketNames[i]);
            bys[i] = "by_" + bucketNames[i];
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder[] termsAggregationBuilders = new TermsAggregationBuilder[bucketNames.length];
        for (int i = 0; i < bucketNames.length; i++) {
            TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(bys[i]).field(bucketNames[i]);
            termsAggregationBuilders[i] = aggregationBuilder;
        }
        for (int i = 0; i < termsAggregationBuilders.length; i++) {
            if (i != termsAggregationBuilders.length - 1) {
                termsAggregationBuilders[i].subAggregation(termsAggregationBuilders[i + 1]);
            }
        }
        if (AggsType.COUNT == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.MIN == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.MAX == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.SUM == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.AVG == aggsType) {
            termsAggregationBuilders[termsAggregationBuilders.length - 1]
                    .subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(termsAggregationBuilders[0]);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        //下面不支持2层以上的下钻
        List<Down> downList = new ArrayList<>();
        Terms terms1 = searchResponse.getAggregations().get(bys[0]);
        Terms terms2;
        for (Terms.Bucket bucket : terms1.getBuckets()) {
            terms2 = bucket.getAggregations().get(bys[1]);
            for (Terms.Bucket bucket2 : terms2.getBuckets()) {
                Down down = new Down();
                down.setLevel_1_key(bucket.getKey().toString());
                down.setLevel_2_key(bucket2.getKey().toString());
                if (AggsType.COUNT == aggsType) {
                    ValueCount count = bucket2.getAggregations().get(me);
                    long value = count.getValue();
                    down.setValue(value);
                } else if (AggsType.MIN == aggsType) {
                    ParsedMin min = bucket2.getAggregations().get(me);
                    double value = min.getValue();
                    down.setValue(value);
                } else if (AggsType.MAX == aggsType) {
                    ParsedMax max = bucket2.getAggregations().get(me);
                    double value = max.getValue();
                    down.setValue(value);
                } else if (AggsType.SUM == aggsType) {
                    ParsedSum sum = bucket2.getAggregations().get(me);
                    double value = sum.getValue();
                    down.setValue(value);
                } else if (AggsType.AVG == aggsType) {
                    ParsedAvg avg = bucket2.getAggregations().get(me);
                    double value = avg.getValue();
                    down.setValue(value);
                }
                downList.add(down);
            }
        }
        return downList;
    }
    
    public <T> double aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        String me = aggsType.toString() + "_" + metricName;
        Field f_metric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (f_metric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(f_metric, metricName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        if (AggsType.COUNT == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.MIN == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.MAX == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.SUM == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.AVG == aggsType) {
            searchSourceBuilder.aggregation(AggregationBuilders.avg(me).field(metricName));
        }
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        if (AggsType.COUNT == aggsType) {
            ValueCount count = searchResponse.getAggregations().get(me);
            long value = count.getValue();
            return Double.parseDouble(String.valueOf(value));
        } else if (AggsType.MIN == aggsType) {
            ParsedMin min = searchResponse.getAggregations().get(me);
            double value = min.getValue();
            return value;
        } else if (AggsType.MAX == aggsType) {
            ParsedMax max = searchResponse.getAggregations().get(me);
            double value = max.getValue();
            return value;
        } else if (AggsType.SUM == aggsType) {
            ParsedSum sum = searchResponse.getAggregations().get(me);
            double value = sum.getValue();
            return value;
        } else if (AggsType.AVG == aggsType) {
            ParsedAvg avg = searchResponse.getAggregations().get(me);
            double value = avg.getValue();
            return value;
        }
        return 0d;
    }
    
    public <T> Stats statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        String me = "stats";
        Field f_metric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (f_metric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(f_metric, metricName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        StatsAggregationBuilder aggregation = AggregationBuilders.stats(me).field(metricName);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Stats stats = searchResponse.getAggregations().get(me);
        return stats;
    }
    
    public <T> Map<String, Stats> statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, String bucketName) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        Field f_metric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        Field f_bucket = clazz.getDeclaredField(bucketName.replaceAll(KEYWORD, ""));
        if (f_metric == null) {
            throw new Exception("metric field is null");
        }
        if (f_bucket == null) {
            throw new Exception("bucket field is null");
        }
        metricName = genKeyword(f_metric, metricName);
        bucketName = genKeyword(f_bucket, bucketName);

        String by = "by_" + bucketName;
        String me = "stats" + "_" + metricName;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregation = AggregationBuilders.terms(by)
                .field(bucketName);
        //默认按照count的降序排序
        aggregation.order(BucketOrder.count(false));
        aggregation.subAggregation(AggregationBuilders.stats(me).field(metricName));
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Aggregations aggregations = searchResponse.getAggregations();
        Terms by_risk_code = aggregations.get(by);
        Map<String, Stats> map = new LinkedHashMap<>();
        for (Terms.Bucket bucket : by_risk_code.getBuckets()) {
            Stats stats = bucket.getAggregations().get(me);
            map.put(bucket.getKey().toString(), stats);
        }
        return map;
    }
    
    public <T> Aggregations aggs(AggregationBuilder aggregationBuilder, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregationBuilder);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        if(metaData.isLog()){
            log.info(searchSourceBuilder.toString());
        }
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        return searchResponse.getAggregations();
    }
    
    public <T> long cardinality(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        Field f_metric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (f_metric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(f_metric, metricName);
        String me = "cardinality_" + metricName;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        CardinalityAggregationBuilder aggregation = AggregationBuilders
                .cardinality(me)
                .field(metricName);
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Cardinality agg = searchResponse.getAggregations().get(me);
        return agg.getValue();
    }
    
    @SuppressWarnings("unchecked")
	public <T> Map<Double,Double> percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception {
        return percentilesAggs(metricName,queryBuilder,clazz, Constant.DEFAULT_PERCSEGMENT);
    }

    @SuppressWarnings("rawtypes")
	public <T> Map percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double... customSegment) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        Field f_metric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (f_metric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(f_metric, metricName);
        String me = "percentiles_" + metricName;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        PercentilesAggregationBuilder aggregation = AggregationBuilders.percentiles(me).field(metricName).percentiles(customSegment);
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Map<Double,Double> map = new LinkedHashMap<>();
        Percentiles agg = searchResponse.getAggregations().get(me);
        for (Percentile entry : agg) {
            double percent = entry.getPercent();
            double value = entry.getValue();
            map.put(percent,value);
        }
        return map;
    }
    
    @SuppressWarnings("rawtypes")
	public <T> Map percentileRanksAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, double... customSegment) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        Field f_metric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (f_metric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(f_metric, metricName);
        String me = "percentiles_" + metricName;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        PercentileRanksAggregationBuilder aggregation = AggregationBuilders.percentileRanks(me,customSegment).field(metricName);
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Map<Double,Double> map = new LinkedHashMap<>();
        PercentileRanks agg = searchResponse.getAggregations().get(me);
        for (Percentile entry : agg) {
            double percent = entry.getPercent();
            double value = entry.getValue();
            map.put(percent,value);
        }
        return map;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Map filterAggs(String metricName, AggsType aggsType,  QueryBuilder queryBuilder,Class<T> clazz, FiltersAggregator.KeyedFilter... filters) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (filters == null) {
            throw new NullPointerException();
        }
        Field f_metric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        if (f_metric == null) {
            throw new Exception("metric field is null");
        }
        metricName = genKeyword(f_metric, metricName);
        String me = aggsType.toString() + "_" + metricName;
        AggregationBuilder aggregation = AggregationBuilders.filters("filteragg", filters);
        searchSourceBuilder.size(0);
        if (AggsType.COUNT == aggsType) {
            aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.MIN == aggsType) {
            aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.MAX == aggsType) {
            aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.SUM == aggsType) {
            aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.AVG == aggsType) {
            aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Filters agg = searchResponse.getAggregations().get("filteragg");
        Map map = new LinkedHashMap();
        for (Filters.Bucket entry : agg.getBuckets()) {
            if (AggsType.COUNT == aggsType) {
                ValueCount count = entry.getAggregations().get(me);
                long value = count.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.MIN == aggsType) {
                ParsedMin min = entry.getAggregations().get(me);
                double value = min.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.MAX == aggsType) {
                ParsedMax max = entry.getAggregations().get(me);
                double value = max.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.SUM == aggsType) {
                ParsedSum sum = entry.getAggregations().get(me);
                double value = sum.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.AVG == aggsType) {
                ParsedAvg avg = entry.getAggregations().get(me);
                double value = avg.getValue();
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> Map histogramAggs(String metricName,  AggsType aggsType,QueryBuilder queryBuilder,Class<T> clazz,String bucketName,double interval) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        Field f_metric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        Field f_bucket = clazz.getDeclaredField(bucketName.replaceAll(KEYWORD, ""));
        if (f_metric == null) {
            throw new Exception("metric field is null");
        }
        if (f_bucket == null) {
            throw new Exception("bucket field is null");
        }
        metricName = genKeyword(f_metric, metricName);
        bucketName = genKeyword(f_bucket, bucketName);
        String by = "by_" + bucketName;
        String me = aggsType.toString() + "_" + metricName;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        AggregationBuilder aggregation = AggregationBuilders.histogram(by).field(bucketName).interval(interval);
        searchSourceBuilder.size(0);
        if (AggsType.COUNT == aggsType) {
            aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.MIN == aggsType) {
            aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.MAX == aggsType) {
            aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.SUM == aggsType) {
            aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.AVG == aggsType) {
            aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        ParsedHistogram agg = searchResponse.getAggregations().get(by);
        Map map = new LinkedHashMap();
        for (Histogram.Bucket entry : agg.getBuckets()) {
            if (AggsType.COUNT == aggsType) {
                ValueCount count = entry.getAggregations().get(me);
                long value = count.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.MIN == aggsType) {
                ParsedMin min = entry.getAggregations().get(me);
                double value = min.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.MAX == aggsType) {
                ParsedMax max = entry.getAggregations().get(me);
                double value = max.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.SUM == aggsType) {
                ParsedSum sum = entry.getAggregations().get(me);
                double value = sum.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.AVG == aggsType) {
                ParsedAvg avg = entry.getAggregations().get(me);
                double value = avg.getValue();
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Map dateHistogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, DateHistogramInterval interval) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexName();
        Field f_metric = clazz.getDeclaredField(metricName.replaceAll(KEYWORD, ""));
        Field f_bucket = clazz.getDeclaredField(bucketName.replaceAll(KEYWORD, ""));
        if (f_metric == null) {
            throw new Exception("metric field is null");
        }
        if (f_bucket == null) {
            throw new Exception("bucket field is null");
        }else if(f_bucket.getType() != Date.class){
            throw new Exception("bucket type is not support");
        }
        ESField ESField = f_bucket.getAnnotation(ESField.class);
        if(ESField != null && ESField.type() != FieldType.Date){
            throw new Exception("bucket type is not support");
        }
        metricName = genKeyword(f_metric, metricName);
        bucketName = genKeyword(f_bucket, bucketName);
        String by = "by_" + bucketName;
        String me = aggsType.toString() + "_" + metricName;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        AggregationBuilder aggregation = AggregationBuilders.dateHistogram(by).field(bucketName).dateHistogramInterval(interval);
        searchSourceBuilder.size(0);
        if (AggsType.COUNT == aggsType) {
            aggregation.subAggregation(AggregationBuilders.count(me).field(metricName));
        } else if (AggsType.MIN == aggsType) {
            aggregation.subAggregation(AggregationBuilders.min(me).field(metricName));
        } else if (AggsType.MAX == aggsType) {
            aggregation.subAggregation(AggregationBuilders.max(me).field(metricName));
        } else if (AggsType.SUM == aggsType) {
            aggregation.subAggregation(AggregationBuilders.sum(me).field(metricName));
        } else if (AggsType.AVG == aggsType) {
            aggregation.subAggregation(AggregationBuilders.avg(me).field(metricName));
        }
        if (queryBuilder != null) {
            searchSourceBuilder.query(queryBuilder);
        }
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest searchRequest = new SearchRequest(indexname);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        ParsedDateHistogram agg = searchResponse.getAggregations().get(by);
        Map map = new LinkedHashMap();
        for (Histogram.Bucket entry : agg.getBuckets()) {
            if (AggsType.COUNT == aggsType) {
                ValueCount count = entry.getAggregations().get(me);
                long value = count.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.MIN == aggsType) {
                ParsedMin min = entry.getAggregations().get(me);
                double value = min.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.MAX == aggsType) {
                ParsedMax max = entry.getAggregations().get(me);
                double value = max.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.SUM == aggsType) {
                ParsedSum sum = entry.getAggregations().get(me);
                double value = sum.getValue();
                map.put(entry.getKey(), value);
            } else if (AggsType.AVG == aggsType) {
                ParsedAvg avg = entry.getAggregations().get(me);
                double value = avg.getValue();
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    /**
     * 组织字段是否带有.keyword
     *
     * @param field
     * @param name
     * @return
     */
    private String genKeyword(Field field, String name) {
    	ESField ESField = field.getAnnotation(ESField.class);
        //带着.keyword直接忽略
        if (name == null || name.indexOf(KEYWORD) > -1) {
            return name;
        }
        //只要keyword是true就要拼接
        //没配注解，但是类型是字符串，默认keyword是true
        if (ESField == null) {
            if (field.getType() == String.class) {
                return name + KEYWORD;
            }
        }
        //配了注解，但是类型是字符串，默认keyword是true
        else {
            if (ESField.type() == FieldType.Text && ESField.keyword() == true) {
                return name + KEYWORD;
            }
        }
        return name;
    }
    
    @Data
    class Down {
        private String level_1_key;
        private String level_2_key;
        private Object value;
    }
}
