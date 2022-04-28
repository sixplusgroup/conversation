module.exports = {
  '@disabled': true,
    '登陆测试': function (browser) {
      browser
      .init()
    //   .waitForElementVisible('#app')
        .setValue('input', '1@qq.com')
        .setValue('.login-password>input', '123456')
        .click('button')
        .assert.urlContains('client/hotelList')
        .waitForElementVisible('.logout')
        .moveToElement('.logout',5,5)
        .moveToElement('.outIcon',5,5)
        .click('.outIcon')

        .setValue('.login-email>input', '4@qq.com')
        .setValue('.login-password>input', '123456')
        .click('button')
        .assert.urlContains('hotelManager')
        .waitForElementVisible('.logout')
        .moveToElement('.logout',5,5)
        .moveToElement('.outIcon',5,5)
        .click('.outIcon')

        .setValue('input', '7@qq.com')
        .setValue('.login-password>input', '123456')
        .click('button')
        .assert.urlContains('siteMarket')
        .waitForElementVisible('.logout')
        .moveToElement('.logout',5,5)
        .moveToElement('.outIcon',5,5)
        .click('.outIcon')

        .setValue('input', '8@qq.com')
        .setValue('.login-password>input', '123456')
        .click('button')
        .assert.urlContains('admin')
        .waitForElementVisible('.logout')
        .moveToElement('.logout',5,5)
        .moveToElement('.outIcon',5,5)
        .click('.outIcon')
        .pause(1000)
        .end();
    }
  };