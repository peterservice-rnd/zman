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
