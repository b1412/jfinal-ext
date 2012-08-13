package com.jfinal.plugin.druid;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.util.StringKit;

public class DruidPlugin implements IPlugin, IDataSourceProvider {

	private DruidDataSource dataSource;

	private String url;
	private String username;
	private String password;
	private String driverClassName = "com.mysql.jdbc.Driver";
	private int initialSize = 1;
	private int minIdle = 1;
	private int maxActive = 20;
	private int maxWait = 60000;
	private int timeBetweenEvictionRunsMillis = 60000;
	private int minEvictableIdleTimeMillis = 300000;
	private String validationQuery = "SELECT 1";
	private boolean testWhileIdle = true;
	private boolean poolPreparedStatements = true;
	private int maxPoolPreparedStatementPerConnectionSize = 20;

	public DruidPlugin(DruidBuilder builder) {
		url = builder.url;
		username = builder.username;
		password = builder.password;
		driverClassName = builder.driverClassName;
		initialSize = builder.initialSize;
		minIdle = builder.minIdle;
		maxActive = builder.maxActive;
		maxWait = builder.maxWait;
		timeBetweenEvictionRunsMillis = builder.timeBetweenEvictionRunsMillis;
		minEvictableIdleTimeMillis = builder.minEvictableIdleTimeMillis;
		validationQuery = builder.validationQuery;
		testWhileIdle = builder.testWhileIdle;
		poolPreparedStatements = builder.poolPreparedStatements;
		maxPoolPreparedStatementPerConnectionSize = builder.maxPoolPreparedStatementPerConnectionSize;
	}

	public DruidPlugin setDriverClass(String driverClassName) {
		if (StringKit.isBlank(driverClassName))
			throw new IllegalArgumentException("driverClass can not be blank.");
		this.driverClassName = driverClassName;
		return this;
	}

	public boolean start() {
		dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setDriverClassName(driverClassName);
		dataSource.setInitialSize(initialSize);
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxActive(maxActive);
		dataSource.setMaxWait(maxWait);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		return true;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public boolean stop() {
		if (dataSource != null)
			dataSource.close();
		return true;
	}

	public static class DruidBuilder {
		private String url;
		private String username;
		private String password;
		private String driverClassName = "com.mysql.jdbc.Driver";
		private int initialSize = 1;
		private int minIdle = 1;
		private int maxActive = 20;
		private int maxWait = 60000;
		private int timeBetweenEvictionRunsMillis = 60000;
		private int minEvictableIdleTimeMillis = 300000;
		private String validationQuery = "SELECT 1";
		private boolean testWhileIdle = true;
		private boolean poolPreparedStatements = true;
		private int maxPoolPreparedStatementPerConnectionSize = 20;

		public DruidBuilder(String url, String username, String password) {
			this.url = url;
			this.username = username;
			this.password = password;
		}

		public DruidBuilder initialSize(int val) {
			initialSize = val;
			return this;
		}

		public DruidBuilder minIdle(int val) {
			minIdle = val;
			return this;
		}

		public DruidBuilder maxActive(int val) {
			maxActive = val;
			return this;
		}

		public DruidBuilder maxWait(int val) {
			maxWait = val;
			return this;
		}

		public DruidBuilder timeBetweenEvictionRunsMillis(int val) {
			timeBetweenEvictionRunsMillis = val;
			return this;
		}

		public DruidBuilder validationQuery(String val) {
			validationQuery = val;
			return this;
		}

		public DruidBuilder testWhileIdle(boolean val) {
			testWhileIdle = val;
			return this;
		}

		public DruidBuilder poolPreparedStatements(boolean val) {
			poolPreparedStatements = val;
			return this;
		}

		public DruidBuilder maxPoolPreparedStatementPerConnectionSize(int val) {
			maxPoolPreparedStatementPerConnectionSize = val;
			return this;
		}

		public DruidPlugin build() {
			return new DruidPlugin(this);
		}
	}

}
