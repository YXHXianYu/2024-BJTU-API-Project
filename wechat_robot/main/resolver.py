import re

from django.db.models import Q

from .models import *
from .constants import *

# === Vars ===

patterns = {
    'help': r'help\s*',
    'commit': r'commit\s+(\S+)\s+(\S+)\s+(\S+)\s+(\d+)(?:\s+(\S+))?(?:\s+(\S+))?',
    'query': r'query(?:\s+(?:--company|-co)\s+(\S+))?(?:\s+(?:--city|-ci)\s+(\S+))?(?:\s+(?:--position|-po)\s+(\S+))?(?:\s+(?:--page|-pa)\s+(\S+))?',
}

# === Resolve ===

def resolve(user_state, content):
    tokens = tokenize(content)

    if tokens is None:
        return MESSAGE_UNKNOWN_COMMAND

    if tokens['command'] == 'help':
        return MESSAGE_HELP
    elif tokens['command'] == 'commit':
        offer = Offer(
            company=tokens['company'],
            city=tokens['city'],
            position=tokens['position'],
            salary=tokens['salary']
        )
        offer.save()

        return MESSAGE_COMMIT_SUCCESS
    elif tokens['command'] == 'query':

        keys = ['company', 'city', 'position']
        filter = {key: tokens[key] for key in keys if tokens[key] is not None}

        query = Q()
        for key, value in filter.items():
            query &= Q(**{f"{key}__exact": value})

        results = Offer.objects.filter(query)

        if 'page' in tokens:
            page = 1
            page_min = 0
            page_max = 10
        else:
            page = max(tokens['page'], 1)
            page_min = (tokens['page'] - 1) * RECORDS_PER_PAGE 
            page_max = tokens['page'] * RECORDS_PER_PAGE 

        ans = f'第{page}页'
        ans += '{' + '\n'
        for offer in results:
            ans += str(offer) + '\n'
        ans += '}'

        return ans

# === Tokenize ===

def tokenize(command):
    for name, pattern in patterns.items():
        match = re.match(pattern, command)
        if match:
            params = match.groups()
            if name == 'help':
                return {
                    'command': 'help'
                }
            elif name == 'commit':
                return {
                    'command': 'commit',
                    'company': params[0],
                    'city': params[1],
                    'position': params[2],
                    'salary': int(params[3]),
                    'placeholder1': params[4] if params[4] else None,
                    'placeholder1': params[5] if params[5] else None,
                }
            elif name == 'query':
                return {
                    'command': 'query',
                    'company': params[0] if params[0] else None,
                    'city': params[1] if params[1] else None,
                    'position': params[2] if params[2] else None,
                    'page': params[3] if params[3] else None,
                }

# === Test ===

if __name__ == '__main__':
    commands = [
        "commit str1 str2 str3 10 1-st-argu 2-nd-argu",
        "commit str1 str2 str3 10 1-st-argu",
        "commit str1 str2 str3 10",
        "query --company str1 -ci str2",
        "query --city str2",
        "query",
        "query -com str1",
        "commit",
        "commit 10 srt1",
        "query --company str1 -ci str2 --position pos",
        "query -pos pos",
    ]

    for command in commands:
        print("Command:", command)
        tokens = tokenize(command)
        print("Tokens:", tokens)
