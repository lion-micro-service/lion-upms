FROM lion/jdk:1.0.0
MAINTAINER Mr.Liu 158442534@qq.com
ARG JAR_FILE
COPY target/${JAR_FILE} /opt/app.jar
ENV JAVA_OPTS='-Xmx384m -Xmx384m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=~/heapdump.hprof  -XX:NumberOfGCLogFiles=30 -XX:GCLogFileSize=10M -XX:UseGCLogFileRotation -Xloggc:~/gc.log' \
 SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR='127.0.0.1:8848' \
 SPRING_CLOUD_NACOS_CONFIG_NAMESPACE='' \
 LANG='C.UTF-8'
WORKDIR /opt
ENTRYPOINT ["/bin/sh","-c","java -jar app.jar ${JAVA_OPTS} --server.port=8080 --spring.cloud.nacos.config.server-addr=${SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR} --spring.cloud.nacos.config.namespace=${SPRING_CLOUD_NACOS_CONFIG_NAMESPACE}"]

#usermod -a -G docker gitlab-runner
#vim /lib/systemd/system/docker.service               -H tcp://0.0.0.0:2375

#docker rmi $(docker images -q -f dangling=true)
#docker run -it --rm --net=host --env SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR=172.16.30.100:8848 --env SPRING_CLOUD_NACOS_CONFIG_NAMESPACE=7a0bcf1f-e5b7-4f63-aefa-cb86df8c79eb www.nexusdocker.com:8085/lion/lion-upms-console-restful:1.0.0-SNAPSHOT