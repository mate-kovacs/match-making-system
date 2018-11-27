import unittest
from http import HTTPStatus

import ui_service


class ControllerTest(unittest.TestCase):

    def setUp(self):
        self.app = ui_service.app.test_client()

    def test_sanity_check(self):
        self.assertIsNotNone(self.app)

    def test_login_route_responds_ok(self):
        response = self.app.get('/login', follow_redirects=True)
        self.assertEqual(response.status_code, HTTPStatus.OK)

    def test_login_route_response_html_with_utf8_charset(self):
        response = self.app.get('/login', follow_redirects=True)
        self.assertEqual(response.content_type, 'text/html; charset=utf-8')

    def test_main_route_redirects__to_login(self):
        response = self.app.get('/', follow_redirects=False)
        self.assertEqual(response.status_code, HTTPStatus.FOUND)

    def test_register_route_responds_ok(self):
        response = self.app.get('/register', follow_redirects=True)
        self.assertEqual(response.status_code, HTTPStatus.OK)

    def test_register_route_response_html_with_utf8_charset(self):
        response = self.app.get('/login', follow_redirects=True)
        self.assertEqual(response.content_type, 'text/html; charset=utf-8')