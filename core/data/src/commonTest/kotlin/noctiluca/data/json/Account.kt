package noctiluca.data.json

import noctiluca.test.ACCOUNT_ID
import noctiluca.test.URL_SAMPLE_COM

const val OTHER_ACCOUNT_ID = "10"

val JSON_MY_ACCOUNT = """
    {
      "id": "$ACCOUNT_ID",
      "username": "test1",
      "acct": "test1",
      "display_name": "サンプル太郎",
      "locked": false,
      "bot": false,
      "group": false,
      "created_at": "2019-04-01T00:00:00.000Z",
      "note": "<p>note</p>",
      "url": "$URL_SAMPLE_COM/@test1",
      "avatar": "$URL_SAMPLE_COM/accounts/avatars/avater.png",
      "avatar_static": "$URL_SAMPLE_COM/accounts/avatars/original/avater.png",
      "header": "$URL_SAMPLE_COM/accounts/headers/header.png",
      "header_static": "$URL_SAMPLE_COM/accounts/headers/original/header.png",
      "followers_count": 100,
      "following_count": 100,
      "statuses_count": 1000,
      "last_status_at": "2022-12-31",
      "emojis": [],
      "fields": [
        {
          "name": "フィールド1",
          "value": "ほげほげ",
          "verified_at": null
        }
      ],
      "role": {
        "id": "-99",
        "name": "",
        "permissions": "0",
        "color": "",
        "highlighted": false
      }
    }
""".trim()

val JSON_OTHER_ACCOUNT = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "username": "test2",
      "acct": "test2",
      "display_name": "サンプル次郎",
      "locked": false,
      "bot": false,
      "group": false,
      "created_at": "2019-04-01T00:00:00.000Z",
      "note": "<p>note</p>",
      "url": "$URL_SAMPLE_COM/@test2",
      "avatar": "$URL_SAMPLE_COM/accounts/avatars/avater.png",
      "avatar_static": "$URL_SAMPLE_COM/accounts/avatars/original/avater.png",
      "header": "$URL_SAMPLE_COM/accounts/headers/header.png",
      "header_static": "$URL_SAMPLE_COM/accounts/headers/original/header.png",
      "followers_count": 100,
      "following_count": 100,
      "statuses_count": 1000,
      "last_status_at": "2022-12-31",
      "emojis": [],
      "fields": [
        {
          "name": "フィールド1",
          "value": "ふがふが",
          "verified_at": null
        }
      ],
      "role": {
        "id": "-99",
        "name": "",
        "permissions": "0",
        "color": "",
        "highlighted": false
      }
    }
""".trim()

val JSON_SUSPENDED_ACCOUNT = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "username": "test2",
      "acct": "test2",
      "display_name": "サンプル次郎",
      "locked": false,
      "bot": false,
      "group": false,
      "created_at": "2019-04-01T00:00:00.000Z",
      "note": "<p>note</p>",
      "url": "$URL_SAMPLE_COM/@test2",
      "avatar": "$URL_SAMPLE_COM/accounts/avatars/avater.png",
      "avatar_static": "$URL_SAMPLE_COM/accounts/avatars/original/avater.png",
      "header": "$URL_SAMPLE_COM/accounts/headers/header.png",
      "header_static": "$URL_SAMPLE_COM/accounts/headers/original/header.png",
      "followers_count": 100,
      "following_count": 100,
      "statuses_count": 1000,
      "last_status_at": "2022-12-31",
      "suspended": true,
      "emojis": [],
      "fields": [
        {
          "name": "フィールド1",
          "value": "ふがふが",
          "verified_at": null
        }
      ],
      "role": {
        "id": "-99",
        "name": "",
        "permissions": "0",
        "color": "",
        "highlighted": false
      }
    }
""".trim()

val JSON_LIMITED_ACCOUNT = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "username": "test2",
      "acct": "test2",
      "display_name": "サンプル次郎",
      "locked": false,
      "bot": false,
      "group": false,
      "created_at": "2019-04-01T00:00:00.000Z",
      "note": "<p>note</p>",
      "url": "$URL_SAMPLE_COM/@test2",
      "avatar": "$URL_SAMPLE_COM/accounts/avatars/avater.png",
      "avatar_static": "$URL_SAMPLE_COM/accounts/avatars/original/avater.png",
      "header": "$URL_SAMPLE_COM/accounts/headers/header.png",
      "header_static": "$URL_SAMPLE_COM/accounts/headers/original/header.png",
      "followers_count": 100,
      "following_count": 100,
      "statuses_count": 1000,
      "last_status_at": "2022-12-31",
      "limited": true,
      "emojis": [],
      "fields": [
        {
          "name": "フィールド1",
          "value": "ふがふが",
          "verified_at": null
        }
      ],
      "role": {
        "id": "-99",
        "name": "",
        "permissions": "0",
        "color": "",
        "highlighted": false
      }
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_NONE = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": false,
      "showing_reblogs": false,
      "notifying": false,
      "followed_by": false,
      "blocking": false,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": true,
      "showing_reblogs": false,
      "notifying": false,
      "followed_by": false,
      "blocking": false,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_SHOWING_REBLOGS = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": false,
      "showing_reblogs": true,
      "notifying": false,
      "followed_by": false,
      "blocking": false,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_NOTIFYING = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": false,
      "showing_reblogs": false,
      "notifying": true,
      "followed_by": false,
      "blocking": false,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_FOLLOWED_BY = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": false,
      "showing_reblogs": false,
      "notifying": false,
      "followed_by": true,
      "blocking": false,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_BLOCKING = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": false,
      "showing_reblogs": false,
      "notifying": false,
      "followed_by": false,
      "blocking": true,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_BLOCKED_BY = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": false,
      "showing_reblogs": false,
      "notifying": false,
      "followed_by": false,
      "blocking": false,
      "blocked_by": true,
      "muting": false,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_MUTING = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": false,
      "showing_reblogs": false,
      "notifying": false,
      "followed_by": false,
      "blocking": false,
      "blocked_by": false,
      "muting": true,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_MUTING_NOTIFICATIONS = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": false,
      "showing_reblogs": false,
      "notifying": false,
      "followed_by": false,
      "blocking": false,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": true,
      "requested": false,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_REQUESTED = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": false,
      "showing_reblogs": false,
      "notifying": false,
      "followed_by": false,
      "blocking": false,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": false,
      "requested": true,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_DOMAIN_BLOCKING = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": false,
      "showing_reblogs": false,
      "notifying": false,
      "followed_by": false,
      "blocking": false,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": true,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_ENDORSED = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": false,
      "showing_reblogs": false,
      "notifying": false,
      "followed_by": false,
      "blocking": false,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": false,
      "endorsed": true,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING_AND_SHOWING_REBLOGS = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": true,
      "showing_reblogs": true,
      "notifying": false,
      "followed_by": false,
      "blocking": false,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()

val JSON_ACCOUNTS_RELATIONSHIP_FOLLOWING_AND_NOTIFYING = """
    {
      "id": "$OTHER_ACCOUNT_ID",
      "following": true,
      "showing_reblogs": false,
      "notifying": true,
      "followed_by": false,
      "blocking": false,
      "blocked_by": false,
      "muting": false,
      "muting_notifications": false,
      "requested": false,
      "domain_blocking": false,
      "endorsed": false,
      "note": "<p>note</p>"
    }
""".trim()
