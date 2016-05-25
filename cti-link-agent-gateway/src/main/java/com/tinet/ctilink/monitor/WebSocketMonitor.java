package com.tinet.ctilink.monitor;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

/**
 * 通过JMX暴露WebSocket统计信息，用于监控
 * 
 * @author Jiangsl
 *
 */
@Component
@ManagedResource(objectName = "Ctilink:type=WebSocketMonitor,app=agent-gateway", description = "WebSocket统计信息")
public class WebSocketMonitor {

	@Autowired
	private WebSocketHandler subProtocolWebSocketHandler;
	@Autowired
	private ThreadPoolTaskExecutor clientInboundChannelExecutor;
	@Autowired
	private ThreadPoolTaskExecutor clientOutboundChannelExecutor;

	@ManagedAttribute(description = "总连接数（包括WebSocket、HttpStreaming和HttpPolling）")
	public int getTotalConnections() {
		return getWebSocketConnections() + getHttpStreamingConnections() + getHttpPollingConnections();
	}

	@ManagedAttribute(description = "WebSocket连接数")
	public int getWebSocketConnections() {
		return this.getWebSocketStat("webSocket");
	}

	@ManagedAttribute(description = "HttpStreaming连接数")
	public int getHttpStreamingConnections() {
		return this.getWebSocketStat("httpStreaming");
	}

	@ManagedAttribute(description = "HttpPolling连接数")
	public int getHttpPollingConnections() {
		return this.getWebSocketStat("httpPolling");
	}

	@ManagedAttribute(description = "入站通道线程池的活动线程数")
	public int getInboundChannelActiveCount() {
		return clientInboundChannelExecutor.getActiveCount();
	}

	@ManagedAttribute(description = "出站通道线程池的活动线程数")
	public int getOutboundChannelActiveCount() {
		return clientOutboundChannelExecutor.getActiveCount();
	}

	@ManagedAttribute(description = "入站通道线程池的当前线程数")
	public int getInboundChannelPoolSize() {
		return clientInboundChannelExecutor.getPoolSize();
	}

	@ManagedAttribute(description = "出站通道线程池的当前线程数")
	public int getOutboundChannelPoolSize() {
		return clientOutboundChannelExecutor.getPoolSize();
	}

	@ManagedAttribute(description = "入站通道线程池设置的最大线程数")
	public int getInboundChannelMaxPoolSize() {
		return clientInboundChannelExecutor.getMaxPoolSize();
	}

	@ManagedAttribute(description = "出站通道线程池设置的最大线程数")
	public int getOutboundChannelMaxPoolSize() {
		return clientOutboundChannelExecutor.getMaxPoolSize();
	}

	@ManagedAttribute(description = "入站通道线程池曾经达到的线程数峰值")
	public int getInboundChannelLargestPoolSize() {
		return clientInboundChannelExecutor.getThreadPoolExecutor().getLargestPoolSize();
	}

	@ManagedAttribute(description = "出站通道线程池曾经达到的线程数峰值")
	public int getOutboundChannelLargestPoolSize() {
		return clientOutboundChannelExecutor.getThreadPoolExecutor().getLargestPoolSize();
	}

	@ManagedAttribute(description = "入站通道线程池的队列任务数")
	public int getInboundChannelQueueSize() {
		return clientInboundChannelExecutor.getThreadPoolExecutor().getQueue().size();
	}

	@ManagedAttribute(description = "出站通道线程池的队列任务数")
	public int getOutboundChannelQueueSize() {
		return clientOutboundChannelExecutor.getThreadPoolExecutor().getQueue().size();
	}
	
	@ManagedAttribute(description = "入站通道线程池已经执行完的任务数")
	public long getInboundChannelCompletedTaskCount() {
		return clientInboundChannelExecutor.getThreadPoolExecutor().getCompletedTaskCount();
	}
	
	@ManagedAttribute(description = "出站通道线程池已经执行完的任务数")
	public long getOutboundChannelCompletedTaskCount() {
		return clientOutboundChannelExecutor.getThreadPoolExecutor().getCompletedTaskCount();
	}

	/**
	 * 内置的SubProtocolWebSocketHandler对连接数做了统计，但是因为是private的，不能直接访问，只好通过反射获取
	 * 
	 * @param item 统计项，对应SubProtocolWebSocketHandler$Stats的属性名称
	 * @return
	 */
	private int getWebSocketStat(String item) {
		int stat = 0;
		try {
			Field outerField = SubProtocolWebSocketHandler.class.getDeclaredField("stats");
			outerField.setAccessible(true);
			Object stats = outerField.get(subProtocolWebSocketHandler);

			Class<?> innerClass = Class
					.forName("org.springframework.web.socket.messaging.SubProtocolWebSocketHandler$Stats");
			Field innerField = innerClass.getDeclaredField(item);
			innerField.setAccessible(true);
			Object statProperty = innerField.get(stats);
			stat = ((AtomicInteger) statProperty).get();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return stat;
	}

}
