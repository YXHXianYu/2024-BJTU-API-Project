from django.http import HttpResponse

import time
import requests
import json

token = "11uyBbVRtGNZa8poIT8jYrQhzkz"
appid = "wx16d150eb712b1aa1"
secret = "cf6ec6059c92ae11a049a4dd2a9174ed"

access_token = ""
expire_time = 0

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

def get_access_token():
    global access_token, expire_time
    if time.time() > expire_time:
        url = 'https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={}&secret={}'.format(appid, secret)
        ans = json.loads(requests.get(url).text)
        assert 'errcode' not in ans, str(ans)
        access_token = ans["access_token"]
        expire_time = ans["expires_in"] + time.time()
    return access_token