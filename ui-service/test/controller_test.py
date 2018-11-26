import unittest

import ui_service

class ControllerTest(unittest.TestCase):

    def setUp(self):
        self.app = ui_service.app.test_client()
        pass

    def test_basic(self):
        self.assertIsNotNone(self.app)
