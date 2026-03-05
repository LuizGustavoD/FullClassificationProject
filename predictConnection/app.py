from flask import request, jsonify, Flask

from routes.predictController import predict_bp as pb_routes
from routes.modelDetails import model_details_bp as md_routes

app = Flask(__name__)

app.register_blueprint(pb_routes)
app.register_blueprint(md_routes)

if __name__ == '__main__':
    app.run(debug=True, port=5000)
