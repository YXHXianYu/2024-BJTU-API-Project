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

def get_reply(info, key="30e5b3fa112e408a91bec1b3ff78c3ca"):  # 这个key是一个我自己申请的，大家可以自己注册图灵机器人来获取一个key

    return "Ciallo～(∠・ω< )⌒★!"

    url = 'http://www.tuling123.com/openapi/api'
    data = {"key": key, "info": info}
    # 获取机器人的回复
    ans = requests.post(url, data).text
    a = json.loads(ans)
    return a['text']

def deal_with_content(content, user):
    return get_reply(content)

access_token = ""
expire_time = 0

def get_access_token():
    global access_token, expire_time
    if time.time() > expire_time:
        url = 'https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={}&secret={}'.format(appid, secret)
        ans = json.loads(requests.get(url).text)
        print(ans)
        access_token = ans["access_token"]
        expire_time = ans["expires_in"] + time.time()
    return access_token

# Create your views here.
@csrf_exempt
def wechat_main(request):
    get_access_token()
    if request.method == 'GET':
        signature = request.GET.get('signature', None)
        timestamp = request.GET.get('timestamp', None)
        nonce = request.GET.get('nonce', None)
        echostr = request.GET.get('echostr', None)

        hashlist = [token, timestamp, nonce]
        hashlist.sort()
        hashstr = ''.join(hashlist)
        hashstr = hashlib.sha1(bytes(hashstr, encoding='utf-8')).hexdigest()
        if hashstr == signature:
            return HttpResponse(echostr)
        else:
            print("ERROR")
            return HttpResponse("ERROR")
    else:
        xmldata = ET.fromstring(request.body)
        msg_type = xmldata.find('MsgType').text
        FromUserName = xmldata.find('FromUserName').text
        ToUserName = xmldata.find('ToUserName').text
        if msg_type == 'text':
            Content = xmldata.find('Content').text
            Content = deal_with_content(Content, FromUserName)
            return_data = """<xml>
  <ToUserName><![CDATA[{toUser}]]></ToUserName>
  <FromUserName><![CDATA[{fromUser}]]></FromUserName>
  <CreateTime>{ctime}</CreateTime>
  <MsgType><![CDATA[text]]></MsgType>
  <Content><![CDATA[{content}]]></Content>
</xml>""".format(toUser=FromUserName, fromUser=ToUserName, ctime=time.time(), content=Content)
            return HttpResponse(return_data)
        elif msg_type == 'image':  # 直接把图片保存下来并且发回去同样的
            MediaId = xmldata.find('MediaId').text
            PicUrl = xmldata.find('PicUrl').text
            with open(MediaId + ".png", 'wb') as f:
                f.write(requests.get(PicUrl).content)
            return_data = """<xml>
  <ToUserName><![CDATA[{toUser}]]></ToUserName>
  <FromUserName><![CDATA[{fromUser}]]></FromUserName>
  <CreateTime>{ctime}</CreateTime>
  <MsgType><![CDATA[image]]></MsgType>
  <Image>
    <MediaId><![CDATA[{Content}]]></MediaId>
  </Image>
  </xml>""".format(toUser=FromUserName, fromUser=ToUserName, ctime=time.time(), Content=MediaId)
            return HttpResponse(return_data)
        return HttpResponse("""<xml>
  <ToUserName><![CDATA[{toUser}]]></ToUserName>
  <FromUserName><![CDATA[{fromUser}]]></FromUserName>
  <CreateTime>{ctime}</CreateTime>
  <MsgType><![CDATA[text]]></MsgType>
  <Content><![CDATA[{content}]]></Content>
</xml>""".format(toUser=FromUserName, fromUser=ToUserName, ctime=time.time(), content="请发送文本或者图片"))
