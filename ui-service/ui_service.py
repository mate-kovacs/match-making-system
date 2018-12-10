from flask import Flask, render_template, redirect, url_for
from flask_bootstrap import Bootstrap
from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, BooleanField
from wtforms.validators import InputRequired, Email, Length

from http import HTTPStatus
import requests
import json

app = Flask(__name__)
app.config['SECRET_KEY'] = "I dont know what to put in here."
Bootstrap(app)


class User():

    def __init__(self, id, name, password, email, status, elo):
        self.id = id
        self.name = name
        self.password = password
        self.email = email
        self.status = status
        self.elo = elo


class LoginForm(FlaskForm):
    email = StringField("email", validators=[InputRequired(), Email(message="Invalid email"), Length(max=50)])
    password = PasswordField("password", validators=[InputRequired(), Length(min=6, max=80)])
    remember = BooleanField("remember me")


class RegisterForm(FlaskForm):
    email = StringField("email", validators=[InputRequired(), Email(message="Invalid email"), Length(max=50)])
    username = StringField("username", validators=[InputRequired(), Length(max=24)])
    password = PasswordField("password", validators=[InputRequired(), Length(min=6, max=80)])


@app.route('/')
def hello_world():
    return redirect(url_for('login'))


@app.route('/login', methods=['GET', 'POST'])
def login():
    form = LoginForm()

    if form.validate_on_submit():
        url = "http://localhost:8080/user"
        params = {'email': form.email.data}
        response = requests.get(url, params=params)
        data = response.json()
        if data[0]['password'] == form.password.data:
            return "Success"

        # todo use hashing when comparing passwords
        # todo if yes then set the users status to online and let them log in
        # todo otherwise return with an error message
        return "<h1>" + form.email.data + " " + form.password.data + "</h1>"


    return render_template('login.html', form=form)


@app.route('/register', methods=['GET', 'POST'])
def register():
    form = RegisterForm()

    if form.validate_on_submit():
        new_id = None
        starting_elo = 100
        status = "ONLINE" # online
        user = User(new_id, form.username.data, form.password.data, form.email.data, status, starting_elo)

        url = "http://localhost:8080/user"
        data = json.dumps(user.__dict__)
        response = requests.post(url, data = data)
        if response.status_code == HTTPStatus.OK:
            # todo log the user in immediately
            return redirect(url_for('lobby'))
        # todo in case of possible problem - like email duplication - return the error message
        message = response.text
        return "<h1>" + message + " "+ form.email.data + " " + form.username.data + " " + form.password.data + "</h1>"

    return render_template('register.html', form=form)


@app.route('/lobby')
def lobby():
    return render_template('lobby.html')


@app.route('/logout')
def logout():
    return ''


if __name__ == '__main__':
    app.run(debug=True)
