FROM httpd:2.4

# Copio archivos estaticos a para desplegarlos en htdocs
COPY ./public-html/ /usr/local/apache2/htdocs/

# Copio archivo de configuracion de apache
COPY ./conf/httpd.conf /usr/local/apache2/conf/httpd.conf

RUN ls -la /usr/local/apache2/logs

RUN chmod go-w /usr/local/apache2/logs

#EXPOSE 81 883
