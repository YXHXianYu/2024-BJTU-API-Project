from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse
from django.db.models import Q

from xml.etree import ElementTree
import requests

from . import lexer
from .constants import *
from .wechat_utils import *
from .models import *
from .services import *

@csrf_exempt
def wechat_main(request):
    get_access_token()
    if request.method == 'GET':
        return wechat_heartbeat(request)
    else:
        return wechat_distributor(request)

def wechat_distributor(request):
    assert request.method == 'POST', 'wechat distributor only receive "POST" method'

    xmldata = ElementTree.fromstring(request.body)
    msg_type = xmldata.find('MsgType').text
    from_user = xmldata.find('FromUserName').text
    to_user = xmldata.find('ToUserName').text

    if msg_type == 'text':
        content = xmldata.find('Content').text
        if get_user(from_user) is None:
            create_user(from_user)
        try:
            tokens = lexer.resolve(content)
        except ValueError:
            content = MESSAGE_UNKNOWN_COMMAND
        else:
            content = MESSAGE_CIALLO + '\n' + wechat_command_distributor(from_user, tokens)
        return HttpResponse(wechat_return_data(to_user=from_user, from_user=to_user, content=content))

    elif msg_type == 'image':  # 直接把图片保存下来并且发回去同样的
        media_id = xmldata.find('MediaId').text
        pic_url = xmldata.find('PicUrl').text
        with open("./pics" + media_id + ".png", 'wb') as f:
            f.write(requests.get(pic_url).content)
        return HttpResponse(wechat_return_data(to_user=from_user, from_user=to_user, content=media_id))

    return HttpResponse(wechat_return_data(to_user=from_user, from_user=to_user, content="请发送文本或者图片"))

def wechat_command_distributor(from_user, tokens):
    if tokens is None:
        return MESSAGE_UNKNOWN_COMMAND
    
    if check_user_state(from_user, tokens['command'], update=True) != AUTHORITY_CHECK_PASS:
        return

    if tokens['command'] == 'help':
        return MESSAGE_HELP
    
    elif tokens['command'] == 'commit':
        create_offer(tokens, from_user)
        return MESSAGE_COMMIT_SUCCESS
    
    elif tokens['command'] == 'query':
        results, page, page_lim = list_offers_with_page(tokens)

        ans = f'第 {page} / {page_lim} 页 ' + '{' + '\n'
        for offer in results:
            ans += '  ' + str(offer) + '\n'
        ans += '}'

        return ans
    
    elif tokens['command'] == 'group-commit':
        batch_create_offers(tokens['offers'], from_user)
        return MESSAGE_COMMIT_SUCCESS
    
    else:
        assert False, f'Unknown command: {tokens["command"]}'