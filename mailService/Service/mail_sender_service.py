import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

from ..model.mail_model import email_template as et


class MailSenderService:

    def __init__(self, smtp_server, port, username, password):
        self.smtp_server = smtp_server
        self.port = port
        self.username = username
        self.password = password

    def send_mail(self, mail_model: et.MailModel):

        message = MIMEMultipart("alternative")
        message["Subject"] = mail_model.subject
        message["From"] = self.username
        message["To"] = mail_model.recipient

        html_part = MIMEText(mail_model.body, "html")

        message.attach(html_part)

        with smtplib.SMTP(self.smtp_server, self.port) as server:
            server.starttls()
            server.login(self.username, self.password)
            server.sendmail(
                self.username,
                mail_model.recipient,
                message.as_string()
            )
