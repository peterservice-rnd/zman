# ZMan

ZMan is Zookeeper web interface built for multiuser/multiserver usage

## Building

```bash
$ mvn clean install
```

## Installation

Every installation (except for gentoo) type here prerequisites building 

### RPM-based distribution (RHEL, CentOS, Fedora)

```bash
# cd zman-assembly/target
# yum install zman-1.0.0-1-noarch.rpm
```

### Debian-based distribution (Debian, Ubuntu, Mint)

```bash
# cd zman-assembly/target
# apt install -f ./zman_1.0.0.deb
```

### Gentoo-based distribution (Gentoo, Calculate)

#### Prerequisite

You need some local overlay, you can read about it in [gentoo wiki](https://wiki.gentoo.org/wiki/Custom_repository)

#### Installation

```bash
# MY_LOCAL_OVERLAY=/usr/local/portage
# mkdir -p $MY_LOCAL_OVERLAY/dev-util/zman
# cp install/gentoo/zman.ebuild $MY_LOCAL_OVERLAY/dev-util/zman/zman-1.0.0.ebuild
# cd $MY_LOCAL_OVERLAY/dev-util/zman
# repoman manifest
# emerge zman
```

### Other *-nixes

#### Prerequisite

1. bash
2. systemd

#### Installation

```bash
# install/install.sh
```

### API

*Someone help us with this one :)*

### Configuration

You can configure some ZMan properties through either:

 - /opt/zman/application.yml
 - /etc/zman/config.yml

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

### Usage without systemd

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

### Docker

```bash
docker build -t zman .
docker run -v /opt/zman/zk-storage:/opt/zman/db/zk-storage -p 8888:8080 -d --name zman zman

where:
-v
    Mount a host directory /opt/zman/zk-storage as a data volume /opt/zman/db/zk-storage
-p
    Run ZMan with port 8888 on host
```
