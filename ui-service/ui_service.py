from flask import Flask, render_template, redirect, url_for
from flask_bootstrap import Bootstrap
from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, BooleanField
from wtforms.validators import InputRequired, Email, Length

app = Flask(__name__)
app.config['SECRET_KEY'] = "I dont know what to put in here."
Bootstrap(app)

class LoginForm(FlaskForm):
    email = StringField("email", validators=[InputRequired, Length(max=50)])
    password = PasswordField("password", validators=[InputRequired, Length(min=6 , max=80)])
    remember = BooleanField("remember me")

@app.route('/')
def hello_world():
    return redirect(url_for('login'))


@app.route('/login')
def login():
    form = LoginForm()

    return render_template('login.html', form = form)


@app.route('/register')
def register():
    return render_template('register.html')


@app.route('/lobby')
def lobby():
    return render_template('lobby.html')


@app.route('/logout')
def logout():
    return ''


if __name__ == '__main__':
    app.run(debug=True)
