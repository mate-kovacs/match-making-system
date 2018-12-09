from flask import Flask, render_template, redirect, url_for
from flask_bootstrap import Bootstrap
from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, BooleanField
from wtforms.validators import InputRequired, Email, Length

app = Flask(__name__)
app.config['SECRET_KEY'] = "I dont know what to put in here."
Bootstrap(app)

class LoginForm(FlaskForm):
    email = StringField("email", validators=[InputRequired(), Email(message="Invalid email"), Length(max=50)])
    password = PasswordField("password", validators=[InputRequired(), Length(min=6 , max=80)])
    remember = BooleanField("remember me")

class RegisterForm(FlaskForm):
    email = StringField("email", validators=[InputRequired(), Email(message="Invalid email"), Length(max=50)])
    username = StringField("username", validators=[InputRequired(), Length(max=24)])
    password = PasswordField("password", validators=[InputRequired(), Length(min=6 , max=80)])

@app.route('/')
def hello_world():
    return redirect(url_for('login'))


@app.route('/login', methods=['GET', 'POST'])
def login():
    form = LoginForm()

    if form.validate_on_submit():
        return "<h1>" + form.email.data + " " + form.password.data + "</h1>"

    return render_template('login.html', form = form)


@app.route('/register', methods=['GET', 'POST'])
def register():
    form = RegisterForm()

    if form.validate_on_submit():
        return "<h1>" + form.email.data + " " + form.username.data + " " + form.password.data + "</h1>"

    return render_template('register.html', form = form)


@app.route('/lobby')
def lobby():
    return render_template('lobby.html')


@app.route('/logout')
def logout():
    return ''


if __name__ == '__main__':
    app.run(debug=True)
