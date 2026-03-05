from flask import Blueprint, jsonify
import tensorflow as tf

MODEL_PATH = 'D:\\luizd\\Projects\\ProjetoIABackFront\\neuralNetwork\\img_class (1).h5'

model = tf.keras.models.load_model(MODEL_PATH)

model_details_bp = Blueprint('model_details', __name__)

@model_details_bp.route('/lossFunc', methods=['GET'])
def get_loss_function():
    try:
        loss_function = model.loss
        return jsonify({'loss_function': str(loss_function)})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@model_details_bp.route('/optimizer', methods=['GET'])
def get_optimizer():
    try:
        optimizer = model.optimizer
        return jsonify({'optimizer': str(optimizer)})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@model_details_bp.route('/metrics', methods=['GET'])
def get_metrics():
    try:
        metrics = model.metrics
        return jsonify({'metrics': [str(metric) for metric in metrics]})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@model_details_bp.route('/layers', methods=['GET'])
def get_layers():
    try:
        layers = model.layers
        layer_details = []
        for layer in layers:
            layer_info = {
                'name': layer.name,
                'type': type(layer).__name__,
                'output_shape': layer.output_shape,
                'activation': str(layer.activation) if hasattr(layer, 'activation') else None
            }
            layer_details.append(layer_info)
        return jsonify({'layers': layer_details})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@model_details_bp.route('/summary', methods=['GET'])
def get_model_summary():
    from io import StringIO
    import sys

    try:
        old_stdout = sys.stdout
        sys.stdout = StringIO()
        model.summary()
        summary_str = sys.stdout.getvalue()
    except Exception as e:
        return jsonify({'error': str(e)}), 500
    sys.stdout = old_stdout

    return jsonify({'model_summary': summary_str})
