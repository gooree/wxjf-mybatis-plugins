# mybatis公用插件
##用法

在mybatis-config.xml中加入如下配置
  ```xml
    <plugins>
      <plugin interceptor="com.wxjfkg.mybatis.plugins.PaginationInterceptor">
        <property name="dialect" value="mysql"/>
      </plugin>
      <plugin interceptor="com.wxjfkg.mybatis.plugins.PerformanceInterceptor">
        <property name="timeoutMillis" value="3000"/>
      </plugin>
    </plugins>
  ```
  
##说明
1. 分页查询插件PaginationInterceptor
2. 性能调试插件PerformanceInterceptor
