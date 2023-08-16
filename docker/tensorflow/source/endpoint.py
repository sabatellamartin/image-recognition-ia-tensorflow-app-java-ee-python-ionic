# Example: https://www.youtube.com/watch?v=s_ht4AKnWZg
from flask import Flask, jsonify
from flask_restful import Api, Resource

from classify_image import classify

app = Flask(__name__)
api = Api(app)

PATH = '/notebooks/source/shared/'

class ClassifyImage(Resource):
    def get(self, image, predictions):
        result = {"name": image, "path": PATH + image, "predictions": classify(image, predictions)}
        if image != "":
            return result, 200
        return "Image not found", 404
    
api.add_resource(ClassifyImage, "/classify/image/<string:image>/<int:predictions>")

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=False)