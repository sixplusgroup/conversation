# Message Module

## Introduction
* This module aims to provide message service for all gmair service nodes
* It is a component to:
    * send a text message to a single person
    * send a text message to a group of person
    * read text message replied by user
* Messages will not be checked here and therefore validation should be done before the interface is called.

## Functions
* Message sending.
* Message receiving.
* Message template.

## Data Structure
* Text message
    * Attributes: message_id | mobile | text_message | block_flag | create_time

## API
2 APIs are provided to send text message to a specific user.

+ Send single message.
    + In this scenario, only one text message will be sent to a given mobile number.
    > URL: /message/send/single
    + Parameters(phone[String], text[String]) 

+ Send group message.
    + In this scenario, one text message will be sent to a group of mobile number.
    > URL: /message/send/group
    + Parameters(phone[String], text[String])
        * multiple phone numbers are split by comma(,) in variable phone, e.g. 130xxxx2305, 131xxxx2305
    
+ Receive reply from users
    + In some situations, users will send back a reply. Here we config a URL to receive notification from the messaging platform.
    > URL: /message/receive
    + Parameters(message[String], mobile[String])
    
+ Retrieve replies from users
    + replies from users will be fetched upon the given query
    > URL: /message/receive/list
    + Parameters()