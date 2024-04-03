from django.shortcuts import render
import hashlib
import json
from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse
import xml.etree.ElementTree as ET
import time
import requests
import random

token = "11uyBbVRtGNZa8poIT8jYrQhzkz"
appid = "wx16d150eb712b1aa1"
secret = "cf6ec6059c92ae11a049a4dd2a9174ed"

def deal_with_content(content, user):
    return "Ciallo～(∠・ω< )⌒★!"

access_token = ""
expire_time = 0

def get_access_token():
    global access_token, expire_time
    if time.time() > expire_time:
        url = 'https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={}&secret={}'.format(appid, secret)
        ans = json.loads(requests.get(url).text)
        assert 'errcode' not in ans, str(ans)
        access_token = ans["access_token"]
        expire_time = ans["expires_in"] + time.time()
    return access_token

@csrf_exempt
def wechat_main(request):
    get_access_token()
    if request.method == 'GET':
        return wechat_heartbeat(request)
    else:
        return wechat_distributor(request)

def wechat_distributor(request):
    assert request.method == 'POST', 'wechat distributor only receive "POST" method'

    xmldata = ET.fromstring(request.body)
    msg_type = xmldata.find('MsgType').text
    from_user = xmldata.find('FromUserName').text
    to_user = xmldata.find('ToUserName').text

    print(msg_type, from_user, to_user)

    if msg_type == 'text':
        content = xmldata.find('Content').text
        content = deal_with_content(content, from_user)
        return HttpResponse(wechat_return_data(to_user=from_user, from_user=to_user, content=content))

    elif msg_type == 'image':  # 直接把图片保存下来并且发回去同样的
        media_id = xmldata.find('MediaId').text
        pic_url = xmldata.find('PicUrl').text
        with open(media_id + ".png", 'wb') as f:
            f.write(requests.get(pic_url).content)
        return HttpResponse(wechat_return_data(to_user=from_user, from_user=to_user, content=media_id))

    return HttpResponse(wechat_return_data(to_user=from_user, from_user=to_user, content="请发送文本或者图片"))

def wechat_return_data(to_user, from_user, content):
    return """<xml>
<ToUserName><![CDATA[{to_user}]]></ToUserName>
<FromUserName><![CDATA[{from_user}]]></FromUserName>
<CreateTime>{ctime}</CreateTime>
<MsgType><![CDATA[text]]></MsgType>
<Content><![CDATA[{content}]]></Content>
</xml>""".format(to_user=to_user, from_user=from_user, ctime=time.time(), content=content)

def wechat_heartbeat(request):
    assert request.method == 'GET', 'wechat heartbeat only receive "GET" method'

    signature = request.GET.get('signature', None)
    timestamp = request.GET.get('timestamp', None)
    nonce = request.GET.get('nonce', None)
    echostr = request.GET.get('echostr', None)

    return HttpResponse(echostr)

    # hashlist = [token, timestamp, nonce]
    # hashlist.sort()
    # hashstr = ''.join(hashlist)
    # hashstr = hashlib.sha1(bytes(hashstr, encoding='utf-8')).hexdigest()
    # if hashstr == signature:
    #     return HttpResponse(echostr)
    # else:
    #     print("ERROR")
    #     return HttpResponse("ERROR")

