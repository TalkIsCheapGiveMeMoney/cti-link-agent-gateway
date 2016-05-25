package com.tinet.ctilink.agentgateway;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.tinet.ctilink.cache.CacheKey;
import com.tinet.ctilink.cache.RedisService;
import com.tinet.ctilink.conf.model.QueueMember;
import com.tinet.ctilink.inc.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 依据cid获取qids
 * <p>
 * 文件名： CidQids.java
 * <p>
 * 
 * @author mucw
 */
@Component
public class WebSocketCache {
	private static ConcurrentHashMap<String, List<String>> CACHE = new ConcurrentHashMap<String, List<String>>();

	@Autowired
	private RedisService redisService;

	public List<String> getQidsbyCid(int enterpriseId, String cno){
		List<String> qids = CACHE.get(enterpriseId + cno);
		if (qids == null || qids.size() == 0){
//			CallAgent callAgent = new CallAgent();
//			if (callAgent == null) {
//				return null;
//			}

			List<QueueMember> queueMemberList = redisService.getList(Const.REDIS_DB_CONF_INDEX, String.format(CacheKey.QUEUE_MEMBER_ENTERPRISE_ID_CNO, enterpriseId, cno)
					, QueueMember.class);
			List<String> qList = new ArrayList<>();
			for (QueueMember queueMember : queueMemberList) {
				qList.add(queueMember.getEnterpriseId() + queueMember.getQno());
			}
			if(qList.size() > 0){
				CACHE.put(enterpriseId + cno, qList);
				return qList;
			}else{
				return null;
			}
		}else{
			return qids;
		}
	}
	
	/**
	 * 通过删除对应的缓存以实现缓存同步
	 * 
	 * @param cid 企业编号+座席号
	 */
	public void removeCache(String cid) {
		CACHE.remove(cid);
	}
}
