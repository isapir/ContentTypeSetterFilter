This filter sets the Content-Type header to the supplied init-param value.

- Use the contentType init-param to specify the content type

- Use the url-pattern to specify for which requests the filter should be applied

Example configuration:

```
<filter>
  <filter-name>XmlContentTypeSetterFilter</filter-name>
  <filter-class>net.twentyonesolutions.ContentTypeSetterFilter</filter-class>
  <init-param>
    <param-name>contentType</param-name>
    <param-value>application/xml; charset=utf-8</param-value>
  </init-param>
  <init-param>
    <param-name>debug</param-name>
    <param-value>false</param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>XmlContentTypeSetterFilter</filter-name>
  <url-pattern>/rest/rss/*</url-pattern>
</filter-mapping>
```
