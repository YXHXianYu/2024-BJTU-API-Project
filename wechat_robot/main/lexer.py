import re

def resolve(content):
    tokens = tokenize(content)
    print(f"Command {content} was tokenized to {tokens if tokens else 'None'}")
    return tokens

patterns = {
    'help': {
        'base': r'help',
    },
    'commit': {
        'base': r'commit\s+(\S+)\s+(\S+)\s+(\S+)\s+(\d+)',
    },
    'query': {
        'base'        : r'query',
        'company'     : r'(?:--company|-co)\s+(\S+)',
        'city'        : r'(?:--city|-ci)\s+(\S+)',
        'position'    : r'(?:--position|-po)\s+(\S+)',
        'page'        : r'(?:--page|-pa)\s+(\d+)',
        'sort-new'    : r'(--sort-new)',
        'sort-salary' : r'(--sort-salary)',
    },
    'group-commit': {
        'base': r'group-commit((?:\s+\S+)+)',
        'sub': r'(\S+)\s+(\S+)\s+(\S+)\s+(\d+)',
    },
}

def re_wrapper(pattern, string):
    match = re.search(pattern, string)
    if match:
        return match.group(1)
    else:
        return None

def re_wrapper_int(pattern, string):
    match = re.search(pattern, string)
    if match:
        return int(match.group(1))
    else:
        return None

def re_wrapper_bool(pattern, string):
    match = re.search(pattern, string)
    if match:
        return True
    else:
        return False

def tokenize(command):
    for name, pattern in patterns.items():
        match = re.match(pattern['base'], command)
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
                }
            elif name == 'query':
                return {
                    'command': 'query',
                    'company': re_wrapper(pattern['company'], command),
                    'city': re_wrapper(pattern['city'], command),
                    'position': re_wrapper(pattern['position'], command),
                    'page': re_wrapper_int(pattern['page'], command),
                    'sort-new': re_wrapper_bool(pattern['sort-new'], command),
                    'sort-salary': re_wrapper_bool(pattern['sort-salary'], command),
                }
            elif name == 'group-commit':
                params = params[0].split()

                tokens = {
                    'command': 'group-commit',
                    'offers': [
                        {
                            'company': params[0],
                            'city': params[1],
                            'position': params[2],
                            'salary': int(params[3]),
                        },
                    ]
                }

                for i in range(4, len(params), 4):
                    tokens['offers'].append({
                        'company': params[i],
                        'city': params[i + 1],
                        'position': params[i + 2],
                        'salary': int(params[i + 3]),
                    })
                
                return tokens
            else:
                assert False, f"Unknown command name: {name}"

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
