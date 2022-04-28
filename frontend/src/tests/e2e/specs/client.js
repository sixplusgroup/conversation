module.exports = {
  // '@disabled': true,
    '搜索酒店测试': function (browser) {
      browser
      .init()
      .waitForElementVisible('input')
        .setValue('input', '1@qq.com')
        .setValue('.login-password>input', '123456')
        .click('button')
        .waitForElementVisible('.ant-input-search')
        .click('.ant-input-search>button')
        .waitForElementVisible('.hotelCard')
        .click('.hotelCard')
        .assert.urlContains('client/hotelDetail')
    },
    '客户预定酒店': function (browser) {
      browser
      .waitForElementVisible('.reserve')
        .click('.reserve')
        .waitForElementVisible('.date')
        .click('.date')
        .moveToElement('.date',350,150)
        .pause(100)
        .mouseButtonClick(0)
        .moveToElement('.date',400,150)
        .pause(100)
        .mouseButtonClick(0)
        .pause(500)
        .click('.peopleNum')
        .moveToElement('.peopleNum',200,200)
        .pause(500)
        .mouseButtonClick(0)
        .click('.roomNum')
        .moveToElement('.roomNum',200,200)
        .pause(500)
        .mouseButtonClick(0)
        .click('.handleSubmit')
    },
    '客户维护个人信息': function (browser) {
      browser
        .click('.toUserInfo')
        .assert.urlContains('client/info')
        .waitForElementVisible('.onModifyInfo')
        .click('.onModifyInfo')
        .setValue('.modifyName','测试')
        .click('.confirmModify')
        .verify.containsText('.userName','测试')
        .end()
    },
    
  };