
##Docker

###Post-installation steps for Linux

#####Manage Docker as a non-root user
The docker daemon binds to a Unix socket instead of a TCP port. By default that Unix socket is owned by the user root and other users can only access it using sudo. The docker daemon always runs as the root user.

https://docs.docker.com/install/linux/linux-postinstall/#manage-docker-as-a-non-root-user

https://stackoverflow.com/questions/44678725/cannot-connect-to-the-docker-daemon-at-unix-var-run-docker-sock-is-the-docker

The Ubuntu 16.04 users can follow these steps,

Inside file /lib/systemd/system/docker.service change: ExecStart=/usr/bin/dockerd fd:// with ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375

Inside file /etc/init.d/docker change:

DOCKER_OPTS= with DOCKER_OPTS="-H ****tcp://0.0.0.0:2375 "

and then restart your machine. And, start playing with docker.

##TensorFlow

###Comando para image recognision

sudo docker exec tensorflow-celerity python /notebooks/source/tutorials/image/imagenet/classify_image.py

###Guia de reentrenamiento de clasificacion de imagenes

https://www.tensorflow.org/hub/tutorials/image_retraining

###Ejemplo: Reentrenar el modelo IceptionV3 con imagenes.

http://androidkt.com/train-image-classifier/

###Ejemplo2: Reentrenar el modelo IceptionV3 con imagenes.

https://github.com/VikramTiwari/tensorflow-retrain-sample

#### Extension de chrome para descargar imagenes de google

https://chrome.google.com/webstore/category/extensions

##### Script de reentrenamiento para TensorFlow

curl -LO https://github.com/tensorflow/hub/raw/r0.1/examples/image_retraining/retrain.py

##### Script de test de reentrenamiento

curl -LO https://github.com/tensorflow/tensorflow/raw/master/tensorflow/examples/label_image/label_image.py

##### Comando de reentrenamiento

python retrain.py --bottleneck_dir=bottlenecks --how_many_training_steps=4000 --model_dir=inception --summaries_dir=training_summaries/basic --output_graph=retrained_graph.pb --output_labels=retrained_labels.txt --image_dir=db_photos

##### Comando de test

python label_image.py --graph=retrained_graph.pb --labels=retrained_labels.txt --output_layer=final_result --input_layer=Placeholder --image=/notebooks/retrain/test/gokuvegeta.jpg

##### Comando de test por docker de forma externa

sudo docker exec tensorflow-celerity python /notebooks/retrain/label_image.py --graph=/notebooks/retrain/retrained_graph.pb --labels=/notebooks/retrain/retrained_labels.txt --output_layer=final_result --input_layer=Placeholder --image=/notebooks/retrain/test/gokuvegeta.jpg


### Docker client

Controlar docker desde java

https://github.com/spotify/docker-client#download



##Install docker
**************
https://docs.docker.com/install/linux/docker-ce/ubuntu/


Install docker compose
**********************
https://docs.docker.com/compose/install/#install-compose


Servicios Docker
****************
****************


Apache HTTPD
************
https://hub.docker.com/_/httpd/


angular-cli
***********
https://hub.docker.com/r/teracy/angular-cli/

wildfly
*******
https://hub.docker.com/r/jboss/wildfly/

postgres
********
https://hub.docker.com/_/postgres/

mongo
*****
https://hub.docker.com/_/mongo/

nginx
*****
https://hub.docker.com/_/nginx/

## Docker Composer

Para desplegar la aplicacion en el directorio docker-ce, ejecutar los comandos:

# Construye los contenedores

sudo docker-compose build

# Instancia los contenedores

sudo docker-compose up


#!/bin/bash
# Delete all containers
docker rm $(docker ps -a -q)
# Delete all images
docker rmi $(docker images -q)



## List Docker CLI commands
docker
docker container --help

## Display Docker version and info
docker --version
docker version
docker info

## Excecute Docker image
docker run hello-world

## List Docker images
docker image ls

## List Docker containers (running, all, all in quiet mode)
docker container ls
docker container ls -all
docker container ls -a -q


#### Docker reverse proxy Nginx

Se deben agregar a la maquina huesped local lod DNS en el archivo /etc/hosts
https://medium.com/@itseranga/nginx-as-reverse-proxy-with-docker-c9ead938fffd

#Untrack ignored files

https://www.git-tower.com/learn/git/faq/ignore-tracked-files-in-git
