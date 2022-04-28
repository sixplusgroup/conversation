module.exports = {
    '@disabled': true,
    '酒店及人员': function (browser) {
      browser
      .init()
    //   .waitForElementVisible('#app')
        .setValue('input', '8@qq.com')
        .setValue('.login-password>input', '123456')
        .click('button')
        .assert.urlContains('admin/hotelAndManager')
    },
    '酒店信息修改': function (browser) {
      browser
      .waitForElementVisible('.hotelEdit')
        .click('.hotelEdit')
        .assert.urlContains('admin/hotelEdit')
    },

    
  };