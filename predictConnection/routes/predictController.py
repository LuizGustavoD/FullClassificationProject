from flask import request, jsonify, Blueprint
import tensorflow as tf
from werkzeug.utils import secure_filename
from PIL import Image
import numpy as np

import os 

MODEL_PATH = 'D:\\luizd\\Projects\\ProjetoIABackFront\\neuralNetwork\\img_class (1).h5'

predict_bp = Blueprint('predict', __name__)

model = tf.keras.models.load_model(MODEL_PATH)

@predict_bp.route('/predict', methods=['POST'])
def predict():
    try:
        if 'image' not in request.files:
            return jsonify({'error': 'No image provided'}), 400

        file = request.files['image']

        if file.filename == '':
            return jsonify({'error': 'Empty filename'}), 400

        predicted_class = predict_image(file)

        return jsonify({'predicted_class': int(predicted_class)})

    except Exception as e:
        return jsonify({'error': str(e)}), 500


def predict_image(file):

    img = Image.open(file.stream)

    img = img.resize((32, 32))
    img_array = np.array(img)

    img_array = np.expand_dims(img_array, axis=0)
    img_array = img_array / 255.0

    predictions = model.predict(img_array)

    predicted_class = np.argmax(predictions[0])

    return predicted_class
