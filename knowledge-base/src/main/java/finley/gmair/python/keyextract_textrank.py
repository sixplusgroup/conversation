#!/usr/bin/python
# coding=utf-8
# 采用TextRank方法提取文本关键词
import sys
import pandas as pd
import jieba.analyse
from collections import Counter
"""
       TextRank权重：

            1、将待抽取关键词的文本进行分词、去停用词、筛选词性
            2、以固定窗口大小(默认为5，通过span属性调整)，词之间的共现关系，构建图
            3、计算图中节点的PageRank，注意是无向带权图
"""

# 处理标题和摘要，提取关键词
def getKeywords_textrank(idList,chatlogList,topK,toatalTopK,stopdata):
    keys = []
    totalKeys=[]
    for index in range(len(idList)):
        jieba.analyse.set_stop_words(stopdata) # 加载自定义停用词表
        keywords = jieba.analyse.textrank(chatlogList[index], topK=topK, allowPOS=('n','nz','v','vd','vn','l','a','d'))  # TextRank关键词提取，词性筛选
        totalKeys.extend(keywords)
        word_split = " ".join(keywords)
        keys.append(word_split)
    c = Counter(totalKeys)
    res = []
    for k in c.most_common(toatalTopK):
        print(k[0], end=" ")
        res.append(k[0])
    print("\n")
    # result = pd.DataFrame({"id": idList,  "key": keys}, columns=['id', 'key'])
    return res

def textrank(idList,chatlogList,topk,total,stopdata):
    return getKeywords_textrank(idList,chatlogList,topk,total,stopdata)
