import math

from typing import List, Optional, Tuple
from django.db.models import Q

from ..models import Offer, User
from ..constants import *

filter_keys = ['company', 'city', 'position']

def get_offer(args) -> Optional[Offer]:
    '''查询匹配Filter的第一个Offer. 若不存在, 则返回 None'''

    offers = list_offers(args)
    if offers:
        return offers[0]
    else:
        return None

def list_offers(args) -> List[Offer]:
    '''查询匹配Filter的Offer集合'''

    # Filter
    filter = {key: args[key] for key in filter_keys if args[key] is not None}
    query = Q()
    for key, value in filter.items():
        query &= Q(**{f"{key}__exact": value})
    results = Offer.objects.filter(query)

    # Sort
    if args['sort-new'] is not None and args['sort-new']:
        results = results.order_by('-datetime')
    if args['sort-salary'] is not None and args['sort-salary']:
        results = results.order_by('-salary')

    # Return
    return results

def list_offers_with_page(args) -> Tuple[List[Offer], int, int]:
    '''分页，查询匹配Filter、并且位于对应页数的Offer集合'''
    
    results = list_offers(args)

    # Page
    page_lim = math.ceil(len(results) / RECORDS_PER_PAGE)
    if args['page'] is None:
        page = 1
    else:
        page = min(max(args['page'], 1), page_lim)
    page_min = (page - 1) * RECORDS_PER_PAGE 
    page_max = page * RECORDS_PER_PAGE 

    results = results[page_min: page_max]

    # Return
    return (results, page, page_lim)

# === 修改方法，单个Offer ===

def create_offer(args, username):
    '''根据对应信息创建一个新的Offer'''

    user = User.objects.filter(username=username).first()

    offer = Offer(
        company=args['company'],
        city=args['city'],
        position=args['position'],
        salary=args['salary'],
        from_user=user,
    )
    offer.save()

    return RETURN_STATE_SUCCESS

def update_offer(args):
    '''根据Filter和对应信息，更新第一个匹配的Offer'''

    result = get_offer(args)

    for key in filter_keys:
        if args['new_' + key] is None:
            continue
        setattr(result, key, args['new_' + key])
    
    result.save()
    
    return RETURN_STATE_SUCCESS

def delete_offer(args):
    '''根据Filter，删除第一个匹配的Offer'''

    result = get_offer(args)

    if result is not None:
        result.delete()
        return RETURN_STATE_SUCCESS
    else:
        return RETURN_STATE_NOT_FOUND_RECORD

def replace_offer(args):
    '''根据Filter和对应信息，整体替换第一个匹配的Offer'''

    result = get_offer(args)

    for key in filter_keys:
        if args['new_' + key] is None:
            return RETURN_STATE_ARGUMENT_NOT_COMPLETE
        setattr(result, key, args['new_' + key])
    
    result.save()

    return RETURN_STATE_SUCCESS

# === 修改方法，批量修改 ===

def create_offers(args, username):
    '''根据对应信息创建多个新的Offer'''

    user = User.objects.filter(username=username).first()

    for arg in args:
        offer = Offer(
            company=arg['company'],
            city=arg['city'],
            position=arg['position'],
            salary=arg['salary'],
            from_user=user,
        )
        offer.save()

    return RETURN_STATE_SUCCESS

def update_offers(args):
    '''根据Filter，更新所有匹配的Offer'''

    results = list_offers(args)
    update_map = {}

    for key in filter_keys:
        if args['new_' + key] is None:
            continue
        update_map[key] = args['new_' + key]
    
    results.update(**update_map)

    return RETURN_STATE_SUCCESS

def delete_offers(args):
    '''根据Filter，删除所有匹配的Offer'''

    results = list_offers(args)
    results.delete()

    return RETURN_STATE_SUCCESS

def replace_offers(args):
    '''根据Filter，整体替换所有匹配的Offer'''

    results = list_offers(args)
    replace_map = {}

    for key in filter_keys:
        if args['new_' + key] is None:
            return RETURN_STATE_ARGUMENT_NOT_COMPLETE
        replace_map[key] = args['new_' + key]
    
    results.update(**replace_map)

    return RETURN_STATE_SUCCESS