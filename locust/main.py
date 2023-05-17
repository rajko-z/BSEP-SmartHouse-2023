from locust import HttpUser, task
from gevent.lock import Semaphore
import random
import time

device_ids = []
lock = Semaphore()
normal_mode = True


class QuickstartUser(HttpUser):
    host = 'https://localhost:8081/smarthouse'

    devices = []
    active_users = 0

    def on_start(self):
        self.header = {'Content-Type': 'application/json; charset=utf-8'}
        self.device = {}

    @task(1)
    def get_devices(self):
        if not self.devices:
            self.devices = self.client.get('/device-script/get-all-devices', headers=self.header, verify=False).json()
            self.active_users = len(self.devices)

    @task(3)
    def update_device_state(self):
        if not self.devices:
            return

        if not self.device:
            self.device = self.get_next_device()
            if not self.device:
                return

        device_state = self.update_normal_state() if normal_mode else self.update_abnormal_state()

        print(device_state)

        response = self.client.put("/device-script/update-device-state", json=device_state, headers=self.header,
                                   verify=False)
        time.sleep(self.device["readingFrequencyTime"])

    def get_next_device(self):
        for device in self.devices:
            if device["id"] not in device_ids:
                device_ids.append(device["id"])
                return device
        return None

    def update_normal_state(self):
        possible_states = []
        possible_message = ""
        if self.device["deviceType"] == "THERMOMETER":
            possible_states = ["20℃", "21℃", "22℃", "23℃", "24℃"]
            possible_message = "Thermometer at "
        elif self.device["deviceType"] == "GATE":
            possible_states = ["OPEN", "CLOSED"]
            possible_message = "Gate is "
        elif self.device["deviceType"] == "COOKER":
            possible_states = ["50℃", "100℃", "150℃", "200℃", "220℃"]
            possible_message = "Cooker at "
        elif self.device["deviceType"] == "WATER_HEATER":
            possible_states = ["30℃", "40℃", "50℃", "60℃"]
            possible_message = "Water heater at "
        elif self.device["deviceType"] == "DOOR":
            possible_states = ["OPEN", "CLOSED"]
            possible_message = "Door is "
        elif self.device["deviceType"] == "LIGHT":
            possible_states = ["ON", "OFF"]
            possible_message = "Light is "
        elif self.device["deviceType"] == "VACUUM_CLEANER":
            possible_states = ["ON", "OFF"]
            possible_message = "Vacum cleaner is "
        elif self.device["deviceType"] == "AIR_CONDITIONING":
            possible_states = ["ON", "OFF"]
            possible_message = "Air conditioning is "
        elif self.device["deviceType"] == "CAMERA":
            possible_states = ["ON", "OFF"]
            possible_message = "Camera is "
        elif self.device["deviceType"] == "ALARM":
            possible_states = ["ON"]
            possible_message = "Alarm is "

        random_state = random.choice(possible_states)
        device_state = {
            "id": self.device["id"],
            "state": random_state,
            "message": possible_message+random_state,
            "messageType": "INFO"
        }
        return device_state

    def update_abnormal_state(self):
        possible_states = []
        possible_message = ""
        if self.device["deviceType"] == "THERMOMETER":
            possible_states = ["14℃", "15℃", "16℃", "30℃", "31℃", "32℃"]
            possible_message = "Thermometer at "
        elif self.device["deviceType"] == "GATE":
            possible_states = ["STUCK", "BLOCKED", "OUT OF POWER"]
            possible_message = "Gate is "
        elif self.device["deviceType"] == "COOKER":
            possible_states = ["250℃", "300℃", "HIGH VOLTAGE", "LOW VOLTAGE"]
            possible_message = "Cooker at "
        elif self.device["deviceType"] == "WATER_HEATER":
            possible_states = ["100℃", "110℃", "120℃", "LOW WATER", "HIGH VOLTAGE", "LOW VOLTAGE"]
            possible_message = "Water heater at "
        elif self.device["deviceType"] == "DOOR":
            possible_states = ["STUCK", "BROKEN"]
            possible_message = "Door is "
        elif self.device["deviceType"] == "LIGHT":
            possible_states = ["BROKEN"]
            possible_message = "Light is "
        elif self.device["deviceType"] == "VACUUM_CLEANER":
            possible_states = ["OVERHEATING", "FULL BAG"]
            possible_message = "Vacum cleaner is "
        elif self.device["deviceType"] == "AIR_CONDITIONING":
            possible_states = ["HIGH VOLTAGE", "STUCK"]
            possible_message = "Air conditioning is "
        elif self.device["deviceType"] == "CAMERA":
            possible_states = ["BLACK SCREEN", "WHITE SCREEN", "BROKEN"]
            possible_message = "Camera has "
        elif self.device["deviceType"] == "ALARM":
            possible_states = ["ACTIVATED", "OFF"]
            possible_message = "Alarm is "

        random_state = random.choice(possible_states)
        device_state = {
            "id": self.device["id"],
            "state": random_state,
            "message": possible_message+random_state,
            "messageType": "WARNING"
        }

        return device_state