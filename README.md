# ZMan

### API



### Configuration:
Use application.yml to configure zman.<br>
Default properties:
```yaml
zman:
  cache:
    max-elements: 20 # max active zookeeper services
    time-to-idle-seconds: 1800 # idle time before zookeeper service expires
    eviction-delay-seconds: 60 # delay between eviction of expired zookeeper services
  curator:
    retries: 2 # connection retries count
    sleep-time-between-retries-millis: 1000 # sleep time between retries (millis)
```
#### Configuring Authentication
zman application can be protected with authentication. Currently three authentication types are supported, besides simple non-protected one:
* LDAP: authentication against any LDAP directory using generic LDAP interface. User is prompted to input his name and password.
* AD: LDAP authentication against MS Active Directory. Does the same as LDAP, but with much easier configuration.
* KERBEROS: authentication against any Kerberos MDC, including MS Active Directory. It outperforms "AD" type for Windows users, as it allows for SSO, letting user access zman automatically without entering their domain credentials.
##### Configuring Authentication For Active Directory
```yaml
authentication:
  type: AD
  ad:
    domain: "yourdomain.org"
    url: "ldap://YOUR-DOMAIN-CONTROLLER-MACHINE-NAME/"
```
##### Configuring Kerberos Authentication For Active Directory
```yaml
authentication:
  type: KERBEROS
  kerberos:
    keytabFilePath: ./http-server-name.keytab
    servicePrincipal: HTTP/server-name.subnet.yourdomain.com@YOURDOMAIN.COM
```
*servicePrincipal* should be the one added to keytab specified in keytabFilePath
*keytabFilePath* should be generated for HTTP service and principal name that includes server full DNS address. Domain administrator may generate with this command:
> ktpass /out c:\temp\http-server-name.keytab /mapuser srvAccountName@YOURDOMAIN.COM /princ HTTP/server-name.subnet.yourdomain.com@YOURDOMAIN.COM /pass ~7rGrC76# /ptype KRB5_NT_PRINCIPAL /crypto All

where *srvAccountName* is some domain account to associate with zman application, *~7rGrC76#* - its sample password
Pricipal name HTTP/server-name.subnet.yourdomain.com@YOURDOMAIN.COM should correspond to the network name where zman is run.
Debug hint: SPNEGO won't work when you access local zman server. You may either make requests from another machine or open server:port/login URL manually.

### Usage:
```bash
Simple Usage
    zman <start|stop> [-p=port#|--port=port#]
    
where:

start
    start ZMan on default 8080 port
stop
    stop running ZMan

Options
-p
    The option sets specified port. Example: -p=8888
-f
    Run ZMan in foreground.
        
```

### Docker:
```bash
docker build -t zman .
docker run -v /opt/zman/zk-storage:/opt/zman/db/zk-storage -p 8888:8080 -d --name zman zman

where:
-v
    Mount a host directory /opt/zman/zk-storage as a data volume /opt/zman/db/zk-storage
-p
    Run ZMan with port 8888 on host
```
