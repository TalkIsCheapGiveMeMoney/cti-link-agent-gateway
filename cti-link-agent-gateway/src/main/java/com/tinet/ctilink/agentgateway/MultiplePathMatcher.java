package com.tinet.ctilink.agentgateway;

import org.springframework.util.AntPathMatcher;

/**
 * 当一个座席属于多个队列时，如果对多个队列进行广播，座席会重复收到多条相同的消息，因此对stomp的订阅做了扩展，支持诸如
 * /queue/30000001,30000002,30000003的定义格式，用逗号作为分隔符，同时订阅多个队列
 * 
 * @author Jiangsl
 *
 */
public class MultiplePathMatcher extends AntPathMatcher {

	@Override
	public boolean match(String pattern, String path) {
		int patternSeparatorPostion = pattern.lastIndexOf("/");
		int pathSeparatorPostion = path.lastIndexOf("/");

		if (patternSeparatorPostion > 0 && pathSeparatorPostion > 0) {
			String patternPrefix = pattern.substring(0, patternSeparatorPostion);
			String pathPrefix = path.substring(0, pathSeparatorPostion);
			if (patternPrefix.equals(pathPrefix)) {
				String patternSuffix = pattern.substring(patternSeparatorPostion + 1);
				String pathSuffix = path.substring(pathSeparatorPostion + 1);
				if (patternSuffix.equals(pathSuffix)) {
					return true;
				}

				String[] patternTokens = patternSuffix.split(",");
				String[] pathTokens = pathSuffix.split(",");

				for (String patternToken : patternTokens) {
					for (String pathToken : pathTokens) {
						if (patternToken.equals(pathToken)) {
							return true;
						}
					}
				}
			}
		}

		return super.match(pattern, path);
	}

}
