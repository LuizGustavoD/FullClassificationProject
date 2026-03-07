from flask import Flask
from routes.mail_sender_controller import mail_sender_bp


app = Flask(__name__)

app.register_blueprint(mail_sender_bp)

if __name__ == '__main__':

    app.run(debug=True, port=5001)