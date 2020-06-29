FROM openjdk:11
COPY target/congestion-tracing-gateway-1.0.jar app.jar
ADD run.sh run.sh
RUN bash -c 'chmod +x /run.sh'
ENTRYPOINT ["/run.sh"]