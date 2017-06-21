/**
 * Copyright (c) 2011-2016, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.core;

import java.util.List;
import com.jfinal.config.Constants;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Routes;
import com.jfinal.config.Plugins;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.log.Log;
import com.jfinal.log.LogManager;
import com.jfinal.plugin.IPlugin;

class Config {
	
	private static final Constants constants = new Constants();
	private static final Routes routes = new Routes(){public void config() {}};
	private static final Plugins plugins = new Plugins();
	private static final Interceptors interceptors = new Interceptors();
	private static final Handlers handlers = new Handlers();
	private static Log log;
	
	// prevent new Config();
	private Config() {
	}
	
	/*
	 * Config order: constant, route, plugin, interceptor, handler
	 */
	static void configJFinal(JFinalConfig jfinalConfig) {
		// 由实际项目实现常量的配置
		jfinalConfig.configConstant(constants);
		//默认使用com.jfinal.log.Log4jLogFactory
		//并生成本类和JFinalFilter的log对象
		initLogFactory();
		// 由实际注册路由
		jfinalConfig.configRoute(routes);
		// 由实际项目配置插件
		jfinalConfig.configPlugin(plugins);					
		startPlugins();	// very important!!!
		//拦截器由实际项目创建
		jfinalConfig.configInterceptor(interceptors);
		//　处理句柄由实际项目创建
		jfinalConfig.configHandler(handlers);
	}
	
	public static final Constants getConstants() {
		return constants;
	}
	
	public static final Routes getRoutes() {
		return routes;
	}
	
	public static final Plugins getPlugins() {
		return plugins;
	}
	
	public static final Interceptors getInterceptors() {
		return interceptors;
	}
	
	public static Handlers getHandlers() {
		return handlers;
	}
	
	private static void startPlugins() {
		List<IPlugin> pluginList = plugins.getPluginList();
		if (pluginList == null)
			return ;
		
		for (IPlugin plugin : pluginList) {
			try {
				// process ActiveRecordPlugin devMode
				if (plugin instanceof com.jfinal.plugin.activerecord.ActiveRecordPlugin) {
					com.jfinal.plugin.activerecord.ActiveRecordPlugin arp = (com.jfinal.plugin.activerecord.ActiveRecordPlugin)plugin;
					if (arp.getDevMode() == null)
						arp.setDevMode(constants.getDevMode());
				}
				
				if (plugin.start() == false) {
					String message = "Plugin start error: " + plugin.getClass().getName();
					log.error(message);
					throw new RuntimeException(message);
				}
			}
			catch (Exception e) {
				String message = "Plugin start error: " + plugin.getClass().getName() + ". \n" + e.getMessage();
				log.error(message, e);
				throw new RuntimeException(message, e);
			}
		}
	}
	
	private static void initLogFactory() {
		LogManager.me().init();
		
		log = Log.getLog(Config.class);
		JFinalFilter.initLog();
	}
}
