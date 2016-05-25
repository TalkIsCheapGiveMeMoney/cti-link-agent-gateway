package com.tinet.ctilink.agentgateway.cache;

import com.tinet.ctilink.cache.DataSyncEventHandler;
import com.tinet.ctilink.conf.model.QueueMember;
import com.tinet.ctilink.inc.Const;
import com.tinet.ctilink.agentgateway.WebSocketCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * cid qids对应关系的本地数据同步处理器
 * 
 * @author mucw
 *
 */
@Component
public class WebsocketCacheSyncEventHandler implements DataSyncEventHandler {
	@Autowired
	private WebSocketCache webSocketCache;
	@Override
	public String getChannel() {
		return Const.REDIS_CHANNEL_SYNC_QUEUEMEMBER;
	}
	@Override
	public void handle(Object object) {
		QueueMember queueMember = (QueueMember) object;
		webSocketCache.removeCache(queueMember.getEnterpriseId() + queueMember.getCno());
	}
}
