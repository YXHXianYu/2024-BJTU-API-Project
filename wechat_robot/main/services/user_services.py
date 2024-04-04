from ..models import User
from ..constants import *

import time

def get_user(username):
    return User.objects.filter(username=username).first()

def create_user(username):
    user = User(username=username)
    user.save()

    return RETURN_STATE_SUCCESS

def get_user_state(username):
    user = User.objects.filter(username=username).first()

    return user.state

def check_user_state(username, command, update=False):
    user = User.objects.filter(username=username).first()

    if user is None:
        if update:
            create_user(username)
        else:
            return AUTHORITY_CHECK_USER_NOT_EXIST

    if update:
        queue = eval(user.request_queue)
        cur_time = int(time.time())
        while len(queue) > 0 and cur_time - queue[0] >= REQUEST_QUEUE_LIMIT_TIME:
            queue.pop(0)
        queue.append(cur_time)
        if len(queue) >= REQUEST_QUEUE_LIMIT_SIZE:
            user.state |= USER_STATE_SPIDER
        user.request_queue = str(queue)
        user.save()

    if user.state & USER_STATE_BANNED != 0:
        return AUTHORITY_CHECK_FAILED
    
    if user.state & USER_STATE_SPIDER != 0:
        return AUTHORITY_CHECK_FAILED
    
    return AUTHORITY_CHECK_PASS

def reset_user_state(username):
    user = User.objects.filter(username=username).first()
    user.state = USER_STATE_DEFAULT
    user.save()

def update_user_state(username, state):
    user = User.objects.filter(username=username).first()
    user.state = state
    user.save()