class MailModel:
    def __init__(
        self,
        recipient: str,
        confirmation_link: str,
        subject: str = "Confirme seu registro",
    ):
        self.recipient = recipient
        self.subject = subject
        self.body = f"""
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Confirmação de Registro</title>
</head>

<body style="margin:0;padding:0;background-color:#f4f6f9;font-family:Arial,Helvetica,sans-serif;">

<table width="100%" cellpadding="0" cellspacing="0" style="padding:40px 0;background-color:#f4f6f9;">
<tr>
<td align="center">

<table width="600" cellpadding="0" cellspacing="0" style="background:#ffffff;border-radius:10px;padding:40px;box-shadow:0 4px 10px rgba(0,0,0,0.08);">

<tr>
<td align="center">

<h2 style="color:#2c3e50;margin-bottom:10px;">
Confirme seu cadastro
</h2>

<p style="color:#555;font-size:16px;line-height:1.5;margin-bottom:30px;">
Obrigado por se registrar! Para ativar sua conta, clique no botão abaixo e confirme seu cadastro.
</p>

<a href="{confirmation_link}"
style="
display:inline-block;
padding:14px 28px;
background-color:#4CAF50;
color:#ffffff;
text-decoration:none;
border-radius:6px;
font-size:16px;
font-weight:bold;
">
Confirmar Registro
</a>

<p style="margin-top:30px;color:#777;font-size:14px;">
Se você não solicitou este registro, pode ignorar este e-mail.
</p>

</td>
</tr>

<tr>
<td align="center" style="padding-top:30px;border-top:1px solid #eee;">
<p style="font-size:12px;color:#999;">
© 2026 Sua Plataforma
</p>
</td>
</tr>

</table>

</td>
</tr>
</table>

</body>
</html>
"""
