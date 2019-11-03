FROM openjdk:11-jre


ADD ./boot.sh /srv/app/
ADD ./app.jar /srv/app/
ADD ./application.yml /srv/app/


ENTRYPOINT ["/srv/app/boot.sh"]