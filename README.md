# Image recognition use IA Tensorflow model v3

This app uses Ionic for the client app, Java EE for the backend app, Python for the Tensorflow inception v3 model, Postgres for app data, Mongo DB to store the results and Docker Compose to deploy.

The model inception v3 con be retrained to recognize specific object in image.
Once the image is recognized it is possible to translate the result to any language using the Yandex translation api

App flow

The client sends a recognition request to the java backend, stores the image, and then sends it to an api in Python Flask that sends it to be processed with Tensorflow. The response you get is a list of probabilities of what the AI ​​thinks the object is. In the end the app can translate the result to any language with Yandex. The results are stored in the Mongo database, the images are deleted from the server every so often or when they exceed 10.

#### Home

<img src="img\pic1.jpg" alt="Home" style="zoom:80%;" />

#### Photo capture recognition page with language selector

<img src="img\pic2.jpg" alt="Home" style="zoom:80%;" />

#### Recognition history captures ordinary objects

<img src="img\pic3.jpg" alt="Home" style="zoom:80%;" />

### Fonts

Yandex Translate
https://translate.yandex.com

Yandex Java API repo
https://github.com/huymluu/yandex-translate-api

Yandex API needs lombok, retrofit and json.
https://mvnrepository.com/artifact/org.projectlombok/lombok
https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
https://mvnrepository.com/artifact/org.json/json
