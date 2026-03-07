from flask import Blueprint, request, jsonify
from ..Service.mail_sender_service import MailSenderService
from ..model.mail_model import MailModel
import os

mail_sender_bp = Blueprint('mail_sender', __name__)

@mail_sender_bp.route('/send_mail', methods=['POST'])
def send_mail():
    data = request.get_json()

    recipient = data.get('recipient')
    confirmation_link = data.get('confirmation_link')

    if not recipient or not confirmation_link:
        return jsonify({
            "error": "Recipient and confirmation link are required."
        }), 400

    mail_model = MailModel(
        recipient=recipient,
        confirmation_link=confirmation_link
    )

    mail_service = MailSenderService(
        smtp_server=os.getenv('SMTP_SERVER', 'smtp.gmail.com'),
        port=int(os.getenv('SMTP_PORT', 587)),
        username=os.getenv('SMTP_USERNAME'),
        password=os.getenv('SMTP_PASSWORD')
    )

    mail_service.send_mail(mail_model)

    return jsonify({"message": "Email sent successfully."}), 200
