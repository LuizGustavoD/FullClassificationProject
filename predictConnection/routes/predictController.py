from flask import Blueprint, request, jsonify
import tensorflow as tf
import numpy as np
from werkzeug.utils import secure_filename
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

        filename = secure_filename(file.filename)
        path = os.path.join('uploads', filename)

        file.save(path)

        predicted_class = predict_image(path)

        return jsonify({'predicted_class': int(predicted_class)})

    except Exception as e:
        return jsonify({'error': str(e)}), 500


def predict_image(image_path):
    img = tf.keras.preprocessing.image.load_img(image_path, target_size=(32, 32))
    img_array = tf.keras.preprocessing.image.img_to_array(img)
    img_array = tf.expand_dims(img_array, 0)  
    img_array = img_array / 255.0

    predictions = model.predict(img_array)
    predicted_class = np.argmax(predictions[0])
    return predicted_class
