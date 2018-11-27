from flask import Flask, redirect

app = Flask(__name__)


@app.route('/')
def hello_world():
    return redirect('/login', code=302)


@app.route('/login')
def login_route():
    return ''


if __name__ == '__main__':
    app.run()
