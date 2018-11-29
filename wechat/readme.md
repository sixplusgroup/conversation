# wechat module

##API list
* upload picture: 
    * purpose: upload temporary picture to wechat platform to get mediaId
    * url: /wechat/picture/upload/get/mediaId
    * parameters: accessToken, imgUrl
    * returnType: String
* reply
    * purpose: platform reply picture to certain user
    * url: /wechat/picture/reply
    * parameters: openId(user), mediaId
    * returnType: String(xml)

## Function
