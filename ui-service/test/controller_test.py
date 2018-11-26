import unittest

import ui_service


class ControllerTest(unittest.TestCase):

    def setUp(self):
        self.app = ui_service.app.test_client()

    def test_sanity_check(self):
        self.assertIsNotNone(self.app)

    def test_login_route_responds_ok(self):
        response = self.app.get('/login', follow_redirects=True)
        self.assertEqual(response.status_code, 200)

    def test_login_route_route_response_html_with_utf8_charset(self):
        response = self.app.get('/login', follow_redirects=True)
        self.assertEqual(response.content_type, 'text/html; charset=utf-8')

