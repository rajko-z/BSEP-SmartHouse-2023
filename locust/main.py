import requests
import json
from locust import HttpUser, task, between
from gevent.lock import Semaphore


class QuickstartUser(HttpUser):
