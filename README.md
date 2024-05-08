# :rocket:Template backend with authentication

### :gear:Configurações necessarias:

- Configurar o Datasource
```lombok.config
spring.datasource.url={URL_DO_BANCO}
spring.datasource.username={USUARIO}
spring.datasource.password={SENHA}
spring.database.driverClassName={DRIVER}
```
O driver tem que estar nas dependencias
```lombok.config
runtimeOnly 'com.ibm.db2:jcc'
runtimeOnly 'com.oracle.database.jdbc:ojdbc11'
runtimeOnly 'org.postgresql:postgresql'
runtimeOnly 'com.mysql:mysql-connector-j'
```
