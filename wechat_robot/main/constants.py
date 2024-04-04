
# Return States
RETURN_STATE_SUCCESS               = 0
RETURN_STATE_NOT_FOUND_RECORD      = 1
RETURN_STATE_ARGUMENT_NOT_COMPLETE = 2

# User States
USER_STATE_DEFAULT = 0b0000
USER_STATE_BANNED  = 0b0001
USER_STATE_SPIDER  = 0b0010

# Authority Checking
AUTHORITY_CHECK_PASS   = 0
AUTHORITY_CHECK_FAILED = 1
AUTHORITY_CHECK_USER_NOT_EXIST = 2

# Request Queue in User Service
# 意味着如果一个用户在30秒内发送了超过30条消息，那么他将被禁止发送消息
REQUEST_QUEUE_LIMIT_TIME = 30
REQUEST_QUEUE_LIMIT_SIZE = 30

# Query
RECORDS_PER_PAGE = 10

# Message
MESSAGE_CIALLO = 'Ciallo～(∠・ω< )⌒★!'
MESSAGE_UNKNOWN_COMMAND = '这个命令看不懂喵~~'
MESSAGE_COMMIT_SUCCESS = '收到喵~'
MESSAGE_VALUE_ERROR = '有数字变成字符串了喵~'

MESSAGE_HELP = """简易版Offer Show API手册

1. help: 帮助

2. commit: 提交一份Offer
  - 命令格式: commit company city position salary(int)

3. query: 查询Offers
  - 命令格式: query [--company company] [--city city] [--position pos] [--page page(int)] [--sort-new] [--sort-salary]

4. group-commit: 提交多份Offer
  - 命令格式：group-commit co1 ci1 po1 sa1(int) [co2 ci2 po2 sa2(int) ...]

备注: 命令格式中的[]表示可选项，如果一个参数末尾带有(int)，则表示它为一个整数类型。 """
