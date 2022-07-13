FROM openjdk:8
ARG ENV=dev
ARG XMS=2048m
ARG XMX=8192m

ENV ENV=${ENV} \
    DARGUMENTS=-Duser.timezone=America/Mexico_City \
    PATH_JAR_JUN=/var/jun/api-jun-0.0.1-SNAPSHOT.jar \
    PATH_HOME=/var/jun \
    XMS=${XMS} \
    XMX=${XMX}

ADD build/libs /var/jun

WORKDIR ${PATH_HOME}

EXPOSE 8081

CMD ["sh", "-c", "java -Dspring.profiles.active=${ENV} -Xms${XMS} -Xmx${XMX} ${DARGUMENTS} -jar ${PATH_JAR_JUN}"]
