
# User States
USER_STATE_DEFAULT = 0b0000
USER_STATE_BANNED  = 0b0001
USER_STATE_SPIDER  = 0b0010

MESSAGE_UNKNOWN_COMMAND = "Command is error"
MESSAGE_COMMIT_SUCCESS = "Commit is successful"

MESSAGE_HELP = """简易版Offer Show API手册

1. help: 帮助

2. commit: 提交一个Offer
  - 命令格式: commit company city position salary(int)

3. query: 查询Offers
  - 命令格式: query [--company company] [--city city] [--position pos] [--page page(int)]

备注: 命令格式中的[]表示可选项，如果一个参数末尾带有(int)，则表示它为一个整数类型。
"""
