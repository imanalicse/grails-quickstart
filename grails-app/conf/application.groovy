import grails.util.BuildSettings
import grails.util.Environment

import java.sql.Connection

grails {
    gorm {
        failOnError = true
        'default' {
            mapping = {
                cache true
                version false
                autoTimestamp false
                id generator: 'assigned'
                '*'(cascadeValidate: 'none')
            }
        }
    }
}

Properties ppt = new Properties()
String dbPath = BuildSettings.BASE_DIR.absolutePath + "/src/main/webapp/WEB-INF/db_config.properties"
if (Environment.isWarDeployed()) {
    try {
        dbPath = System.getProperty("catalina.home", "OPPSSSSSSSSSSSSSSSSSSSS")
        dbPath += "/webapps/ROOT/WEB-INF/db_config.properties"
    }
    catch (Throwable throwable) {
        throwable.printStackTrace()
    }
}
File file = new File(dbPath)
if (file.exists()) {
    file.getCanonicalFile().withInputStream { InputStream stream ->
        ppt.load(stream)
    }
}
println("Loading db configs from (1) ${file.absolutePath}, exists=${file.exists() ? 1 : 0}, db=${ppt.get("app.database.name", "none_db_selected")}")

hibernate {
    cache {
        queries = false
        use_second_level_cache = false
        use_query_cache = false
        region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
    }
}

dataSource {
    pooled = true
    jmxExport = true
    logSql = ppt.get("app.database.log_sql", "false").equals("true")
    driverClassName = ppt.get("app.database.driver", "org.mariadb.jdbc.Driver")
    username = ppt.get("app.database.user", "root")
    password = ppt.get("app.database.password", "")
    dbCreate = "update" // [create-drop] [update] [create]
    url = "jdbc:${ppt.get("app.database.server_type", "mysql")}://${ppt.get("app.database.host", "localhost")}/${ppt.get("app.database.name", "none_db_selected")}?useUnicode=yes" +
            "&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true" +
            "&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true"
    properties {
        jmxEnabled = true
        initialSize = 5
        maxActive = 2000
        minIdle = 5
        maxIdle = 5
        maxWait = 10000
        maxAge = 600000
        timeBetweenEvictionRunsMillis = 5000
        minEvictableIdleTimeMillis = 60000
        validationQuery = "SELECT 1"
        validationQueryTimeout = 3
        validationInterval = 15000
        testOnBorrow = true
        testWhileIdle = true
        testOnReturn = false
        jdbcInterceptors = "ConnectionState;StatementCache(max=200)"
        defaultTransactionIsolation = Connection.TRANSACTION_READ_COMMITTED
    }
}
