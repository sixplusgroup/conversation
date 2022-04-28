module.exports = {
    '@disabled': true,
    '查看酒店经营情况': function (browser) {
      browser
      .init()
    //   .waitForElementVisible('#app')
        .setValue('input', '4@qq.com')
        .setValue('.login-password>input', '123456')
        .click('button')
        .assert.urlContains('hotelManager/hotelSituation')
    },
    '更新客房基本信息': function (browser) {
      browser
      .waitForElementVisible('.jumpToUpdateRoom')
        .click('.jumpToUpdateRoom')
        .assert.urlContains('hotelManager/updateRoom')
        .waitForElementVisible('.addRoom')
        .click('.addRoom')
        .waitForElementVisible('.uniqueType')
        .click('.uniqueType')
    },
    '优惠券': function (browser) {
      browser
      .waitForElementVisible('.jumpToCoupon')
        .click('.jumpToCoupon')
        .assert.urlContains('hotelManager/coupon')
    },
    '酒店信息': function (browser) {
      browser
      .waitForElementVisible('.jumpToHotelInfo')
        .click('.jumpToHotelInfo')
        .assert.urlContains('hotelManager/hotelInfo')
    },
    '处理订单': function (browser) {
      browser
      .waitForElementVisible('.jumpToHandleOrders')
        .click('.jumpToHandleOrders')
        .assert.urlContains('hotelManager/handleOrders')
    },

    
  };