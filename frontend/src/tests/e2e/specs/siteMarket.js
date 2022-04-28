module.exports = {
    '@disabled': true,
    '主页': function (browser) {
      browser
      .init()
    //   .waitForElementVisible('#app')
        .setValue('input', '7@qq.com')
        .setValue('.login-password>input', '123456')
        .click('button')
        .assert.urlContains('siteMarket/siteSituation')
    },
    '信用充值': function (browser) {
      browser
      .waitForElementVisible('.addCredit')
        .click('.addCredit')
        .assert.urlContains('siteMarket/addCredit')
    },
    '商圈管理': function (browser) {
      browser
      .waitForElementVisible('.bizRegion')
        .click('.bizRegion')
        .assert.urlContains('siteMarket/bizRegion')
    },

    
  };