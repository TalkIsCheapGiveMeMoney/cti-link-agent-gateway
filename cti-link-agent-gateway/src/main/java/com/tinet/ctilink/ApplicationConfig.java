package com.tinet.ctilink;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.tinet.ctilink.agentgateway.MultiplePathMatcher;

@Configuration
@EnableWebSocketMessageBroker
public class ApplicationConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {

	@Override
	public void configureStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/agent").setAllowedOrigins("*").withSockJS()
		// .setStreamBytesLimit(512 * 1024)
		// .setHttpMessageCacheSize(1000)
		// .setDisconnectDelay(30 * 1000)
		// .setInterceptors(new HttpSessionHandshakeInterceptor())
		;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableSimpleBroker("/enterprise", "/queue", "/agent");
		registry.setPathMatcher(new MultiplePathMatcher());
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		//registration.taskExecutor().corePoolSize(20).maxPoolSize(100).queueCapacity(1024);
		registration.taskExecutor().corePoolSize(50);
		super.configureClientInboundChannel(registration);
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		//registration.taskExecutor().corePoolSize(20).maxPoolSize(100).queueCapacity(1024);
		registration.taskExecutor().corePoolSize(50);
		super.configureClientInboundChannel(registration);
	}


	/*
	
	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.setSendTimeLimit(15 * 1000).setSendBufferSizeLimit(512 * 1024);
		registration.setMessageSizeLimit(128 * 1024);
	}
	
	*/

}