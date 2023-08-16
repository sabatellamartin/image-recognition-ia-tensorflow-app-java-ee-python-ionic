
FROM kartoza/postgis:9.6-2.4

# Postgres 9.6 + Postgis 2.4
# repositorio de la imagen en  https://hub.docker.com/r/kartoza/postgis/

#COPY init.sh /docker-entrypoint-initdb.d/
#RUN chmod +x /docker-entrypoint-initdb.d/init.sh

# Agrego linea para permitir conexiones externas desde cualquier host
RUN  echo "host all all 0.0.0.0/0 md5" >> /etc/postgresql/9.6/main/pg_hba.conf

EXPOSE 5432
