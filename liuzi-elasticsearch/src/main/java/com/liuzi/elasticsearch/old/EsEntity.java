package com.liuzi.elasticsearch.old;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liuzi.util.date.DateFormat;
import com.liuzi.util.date.DateUtil;


@Document(indexName="", type="_doc", shards = 1, replicas = 1)
public class EsEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * document id
	 */
	@Id
	protected Long id;
	/**
	 * 是否删除
	 */
	@Field(type = FieldType.Integer)
	protected Integer isDelete = 0;
	/**
	 * 创建时间
	 * 指定存储格式
	 * @Field(type = FieldType.Date, format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss")
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone ="GMT+8")
    protected Date createTime;
	/**
	 * 更新时间
	 * 数据格式转换，并加上8小时进行存储
	 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd HH:mm:ss.SSS", timezone ="GMT+8")
    protected Date updateTime;
    
    public static String indexName(String name){
    	return Elasticsearch._index(name, DateUtil.dateToString(new Date(), DateFormat.yyyyMMdd));
    }
    
    /**
     * 
     	一.说明
     	1. 官方英文文档 
     	https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping.html
		2. Mapping
		定义文档及其包含的字段如何存储和编制索引的过程，每个索引都有一个映射类型，用于确定文档将如何编制索引。
		Meta-fields：包括文档的_index，_type，_id和_source字段
		3. ES字段数据类型
		https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html
		
     	二. 字段类型
		1. 字符串类型
		text（分词, analyzer = "ik_max_word"） 、 keyword（不分词，精确查找）
		2. 数值类型
		整数类型 long, integer, short, byte
		浮点类型	double, float, half_float, scaled_float
		3. 日期类型
		date
		4. 逻辑类型
		boolean
		5. 二进制类型
		binary
		6. 范围类型range
		integer_range, float_range, long_range, double_range, date_range
		7. Array数据类型(Array不需要定义特殊类型)
		[ "one", "two" ]
		[ 1, 2 ]
		[{ "name": "Mary", "age": 12 },{ "name": "John", "age": 10}]
		8. Object数据类型 （json嵌套）
		{ 
			"region": "US",
		  	"manager": { 
				"age":     30,
				"name": { 
		  			"first": "John",
		  			"last":  "Smith"
				}
		  	}
		}
		9.嵌套类型
		nested
		10. 地理数据类型
		1）Geo-point
		2）Geo-Shape(比较复杂，参考官网文档，一般用Geo-point就可以了)
		11. 特殊数据类型
		1）ip (IPv4 and IPv6 addresses)
		2）completion (范围类型，自动完成/搜索)
		3）token_count (令牌计数（数值）类型，分析字符串，索引的数量)
		4）murmur3 (索引时计算字段值的散列并将它们存储在索引中的功能。 在高基数和大字符串字段上运行基数聚合时有很大帮助)
		5）join (同一索引的文档中创建父/子关系)
		6）attachment (附件类型)
		7）percolator (抽取类型)
		
		三.常用的参数类型定义&赋值
		类型			|	参数定义								|	赋值
		------------------------------------------------------------------------------------------------
		text		|	"name":{"type":"text"}				|	"name": "zhangsan"
		keyword		|	"tags":{"type":"keyword"}			|	"tags": "abc"
		date		|	"date":{"type": "date"}				|	"date":"2015-01-01T12:10:30Z"
		long		|	"age":{"type":"long"}				|	"age" :28
		double		|	"score":{"type":"double"}			|	"score":98.8
		boolean		|	"isgirl": { "type": "boolean" }		|	"isgirl" :true
		ip			|	"ip_addr":{"type":"ip"}				|	"ip_addr": "192.168.1.1"
		geo_point	|	"location": {"type":"geo_point"}	|	"location":{"lat":40.12,"lon":-71.34}
		
		四.Mapping parameters
		https://www.elastic.co/guide/en/elasticsearch/reference/6.2/mapping-params.html
		
		analyzer、normalizer	、boost、coerce、copy_to、doc_values、dynamic	 
		enabled、fielddata、eager_global_ordinals、format、ignore_above	 
		ignore_malformed、index_options、index、fields、norms、null_value	 
		position_increment_gap、properties、search_analyzer、similarity	 
		store、term_vector
    *
    */
}
