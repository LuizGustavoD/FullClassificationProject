from flask import request, jsonify
import tensorflow as tf 
import numpy as np

from routes.predictController import predict_bp

model = tf.keras.models.load_model('model.keras')

app = Flask(__name__)

app.register_blueprint(predict_bp)

if __name__ == '__main__':
    app.run(debug=True, port=5000)



