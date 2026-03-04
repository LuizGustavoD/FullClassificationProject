from flask import blueprint, request, jsonify
import tensorflow as tf
import os 

predict_bp = blueprint('predict', __name__)

model = tf.keras.models.load_model('model.keras')

@predict_bp.route('/predict', methods=['POST'])
def predict():
    if request.method == 'POST':
        data = request.get_json()
        file = request.files['image']
        if file:
            filename = secure_filename(file.filename)
            file.save(os.path.join('uploads', filename))
            response = {'predicted_class': predict_image(os.path.join('uploads', filename))}
    return jsonify(response)

def predict_image(image_path):
    img = tf.keras.preprocessing.image.load_img(image_path, target_size=(32, 32))
    img_array = tf.keras.preprocessing.image.img_to_array(img)
    img_array = tf.expand_dims(img_array, 0)  

    predictions = model.predict(img_array)
    predicted_class = np.argmax(predictions[0])
    return predicted_class