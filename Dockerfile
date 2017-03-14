FROM java:8u111-jdk

ADD zman-assembly/target/assembly/zman-assembly.tar.gz /opt
WORKDIR /opt/zman

ENTRYPOINT ["bin/zman","start","-f"]