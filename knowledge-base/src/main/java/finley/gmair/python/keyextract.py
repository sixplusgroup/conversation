from keyextract_textrank import textrank
from keyextract_tfidf import tf_idf
from collections import Counter
import pymysql
import time
import redis

from sys import argv

def connect(dbName):
    '''连接MySQL数据库'''
    try:
        db = pymysql.connect(
            host='47.98.191.109',
            port=3306,
            user='root',
            passwd='123456',
            db=dbName,
            charset='utf8'
            )
        return db
    except Exception:
        raise Exception("数据库连接失败")

#date转Timestamp，单位为毫秒
def dateToStamp(str):
    stamp = time.mktime(time.strptime(str, '%Y-%m-%d %H:%M:%S'))
    return (int(stamp)*1000)


def main(argv):
    stopData = argv[1]
    print(stopData)
    idList = []
    chatlogList= []
    idx =1
    try:
        db = connect('gmair_chatlog')
        cursor = db.cursor()
        # cursor.execute("""SELECT session_id FROM `usr_session_jd` where timestamp>=UNIX_TIMESTAMP(%s) and timestamp <=UNIX_TIMESTAMP(%s)"""%("2021-10-29 10:45:54","2022-01-21 14:21:54"))
        cursor.execute("""SELECT session_id FROM `usr_session_jd` where timestamp>=%d and timestamp <=%d"""%(dateToStamp("2021-10-29 10:45:54"),dateToStamp("2022-01-21 11:20:03")))
        res = cursor.fetchall()
        for item in res:
            sessionId = item[0]
            cursor.execute("""SELECT content FROM `session_message_jd` where is_from_waiter=0 and session_id = %d"""%(sessionId))
            message_res = cursor.fetchall()
            session = ""
            for message in message_res:
                session =  session+(message[0])+"@"
            idList.append(idx)
            idx+=1
            chatlogList.append(session)
        cursor.close()
        db.close()
    except Exception:
        db.rollback()
        print("错误")
    if idList!=[]:
        total = []
        topk=10
        totalTopk=10
        print("--------------tfidf------------")
        total.extend(tf_idf(idList,chatlogList,topk,totalTopk,stopData))
        print("\n---------------textrank----------")
        total.extend(textrank(idList,chatlogList,topk,totalTopk,stopData))
        c = Counter(total)
        print("\n------------total---------")
        final = ""
        for k in c.most_common(totalTopk):
            final=final+k[0]+"、"
        final=final[:-1]
        r = redis.StrictRedis(host='47.98.191.109', port=6381, password="Gmair2022", db=0,decode_responses=True, charset='UTF-8', encoding='UTF-8')
        r.set("keywords",final)
        #设置过期时间为31天
        r.expire("keywords",60*60*24*31)
        final='\''+final+'\''
        db = connect('gmair')
        cursor = db.cursor()
        cursor.execute("""INSERT INTO keywords(keyword) values(%s)"""%(final))
        cursor.close()
        db.commit()
        db.close()
    else:
        cursor.close()
        db.close()
if __name__ == '__main__':
    main(argv)

