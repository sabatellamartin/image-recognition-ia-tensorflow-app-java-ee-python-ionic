
FROM tensorflow/tensorflow

# Upgrade pip
RUN pip install --upgrade pip

# Install tensorflow-hub for retrain model image-recognition
RUN pip install tensorflow-hub

# Install the microframework for REST comunication
RUN pip install Flask
RUN pip install Flask-RESTful

# Run REST app
CMD [ "python", "/notebooks/source/endpoint.py" ]
