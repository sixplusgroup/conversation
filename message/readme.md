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

## Data Structure
* Text message
    * Columns: message_id | mobile | text_message | block_flag | create_time