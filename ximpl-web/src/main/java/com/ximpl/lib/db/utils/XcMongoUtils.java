package com.ximpl.lib.db.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ximpl.lib.util.XcJsonUtils;

/**
 * Page Utility class
 */
public class XcMongoUtils {
	/**
	 * Convert to Pageable
	 * @param page
	 * @param size
	 * @param sortBy
	 * @param direction
	 * @return
	 */
	public static Pageable toPageable(int page, int size, String sortBy, String direction){
		final Direction dir = toDirection(direction);
		if (dir == null || Strings.isNullOrEmpty(sortBy))
			return new PageRequest(page, size);
		else
			return new PageRequest(page, size, dir, sortBy);
	}
	
	/**
	 * Convert to Sort direction
	 * @param direction
	 * @return
	 */
	public static Direction toDirection(String direction){
		try{
			return Direction.valueOf(direction.trim().toUpperCase());
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * Covert to query
	 * @param filter
	 * @param sortBy
	 * @param direction
	 * @return
	 */
	public static Query toQuery(String filter, String sortBy, String direction){
		final JsonObject jFilter = Strings.isNullOrEmpty(filter) ? null : XcJsonUtils.toJsonElement(filter).getAsJsonObject();
		return toQuery(jFilter, sortBy, direction);
	}
	
	public static Query toQuery(JsonObject jFilter, String sortBy, String direction){
		final Sort sort = new Sort(toDirection(direction), sortBy);
		final Query query = new Query().with(sort);
		
		if (jFilter == null || jFilter.isJsonNull())
			return query;
		
		addFilter(query, jFilter);
		
		return query;
	}
	
	public static void addFilter(Query query, JsonObject jFilter){
		for(Entry<String, JsonElement> jElement : jFilter.entrySet()){
			final String key = jElement.getKey();
			final JsonElement jValue = jElement.getValue();
			if(jValue.isJsonPrimitive()){
				final String value = jValue.getAsString();
				if (Strings.isNullOrEmpty(value)){
					query.addCriteria(toEmptyCriteria(key));
				}else{
					if (key.contains(".")){
						query.addCriteria(toLikeCriteria(key, value));
					}else{
						try{
							final UUID uuid = UUID.fromString(value);
							query.addCriteria(toUuidCriteria(key, uuid));
						}catch(Exception e){
							query.addCriteria(toLikeCriteria(key, value));
						}
					}
				}
			}else if (jValue.isJsonArray()){
				query.addCriteria(toOrCriteria(key, jValue.getAsJsonArray()));
			}else if (jValue.isJsonNull()){
				query.addCriteria(toEmptyCriteria(key));
			}
		}
	}
	
	/**
	 * Convert to like criteria
	 * @param key
	 * @param regex
	 * @return
	 */
	public static Criteria toLikeCriteria(String key, String regex){
		return Criteria.where(key).regex( Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE));
	}
	
	public static Criteria toOrCriteria(String key, JsonArray jArray){
		final Criteria criteria = new Criteria();
		final List<Criteria> subCriterias = new ArrayList<Criteria>();
		for(JsonElement jElement : jArray){
			final String value = jElement.getAsString();
			
			try{
				final UUID uuid = UUID.fromString(value);
				subCriterias.add(toUuidCriteria(key, uuid));
			}catch(Exception e){
				subCriterias.add(toLikeCriteria(key, value));
			}
		}
		criteria.orOperator(subCriterias.toArray(new Criteria[0]));
		return criteria;
	}
	
	public static Criteria toUuidCriteria(String key, UUID uuid){
		return Criteria.where(key).is(uuid);
	}
	
	public static Criteria toEmptyCriteria(String key){
		return Criteria.where(key).exists(false);
	}
}
