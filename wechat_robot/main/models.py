from typing import Any
from django.db import models

from .constants import *

class User(models.Model):
    def __init__(self, *args: Any, **kwargs: Any) -> None:
        super().__init__(*args, **kwargs)

    id = models.AutoField(primary_key=True)

    username = models.CharField(max_length=512, unique=True)
    state = models.IntegerField(default=USER_STATE_DEFAULT)

    request_queue = models.CharField(max_length=1024, default="[]")

    def __str__(self):
        return f"User ({self.username}, {self.state})"

class Offer(models.Model):
    def __init__(self, *args: Any, **kwargs: Any) -> None:
        super().__init__(*args, **kwargs)

    id = models.AutoField(primary_key=True)

    company = models.CharField(max_length=64)
    city = models.CharField(max_length=64)
    position = models.CharField(max_length=64)
    salary = models.IntegerField()

    datetime = models.DateTimeField(auto_now_add=True)

    from_user = models.ForeignKey(User, on_delete=models.CASCADE, null=True)

    def __str__(self):
        return f"Offer ({self.company}, {self.city}, {self.position}, {self.salary})"

