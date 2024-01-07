package noctiluca.test

const val ACCOUNT_ID = "1"

val JSON_ACCOUNT_CREDENTIAL = """
    {
      "id": "$ACCOUNT_ID",
      "username": "test1",
      "acct": "test1",
      "display_name": "サンプル太郎",
      "locked": false,
      "bot": false,
      "discoverable": false,
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
      "noindex": false,
      "source": {
        "privacy": "public",
        "sensitive": false,
        "language": null,
        "note": "note",
        "fields": [
          {
            "name": "フィールド1",
            "value": "ほげほげ",
            "verified_at": null
          }
        ],
        "follow_requests_count": 0
      },
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

val JSON_STATUS_NORMAL = """
    {
      "id": "100",
      "created_at": "2023-03-01T00:00:00.000Z",
      "in_reply_to_id": null,
      "in_reply_to_account_id": null,
      "sensitive": false,
      "spoiler_text": "",
      "visibility": "public",
      "language": "ja",
      "uri": "$URL_SAMPLE_COM/users/test1/statuses/100",
      "url": "$URL_SAMPLE_COM/@test1/100",
      "replies_count": 0,
      "reblogs_count": 0,
      "favourites_count": 0,
      "edited_at": null,
      "favourited": false,
      "reblogged": false,
      "muted": false,
      "bookmarked": false,
      "pinned": false,
      "content": "<p>Test Status</p>",
      "filtered": [],
      "reblog": null,
      "application": {
        "name": "Web",
        "website": null
      },
      "account": {
        "id": "$ACCOUNT_ID",
        "username": "test1",
        "acct": "test1",
        "display_name": "サンプル太郎",
        "locked": false,
        "bot": false,
        "discoverable": false,
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
        "noindex": false,
        "emojis": [],
        "fields": [
          {
            "name": "フィールド1",
            "value": "ほげほげ",
            "verified_at": null
          }
        ]
      },
      "media_attachments": [],
      "mentions": [],
      "tags": [],
      "emojis": [],
      "card": null,
      "poll": null
    }
""".trim()

val JSON_STATUS_NORMAL_ID_99 = """
    {
      "id": "99",
      "created_at": "2023-02-28T00:00:00.000Z",
      "in_reply_to_id": null,
      "in_reply_to_account_id": null,
      "sensitive": false,
      "spoiler_text": "",
      "visibility": "public",
      "language": "ja",
      "uri": "$URL_SAMPLE_COM/users/test1/statuses/99",
      "url": "$URL_SAMPLE_COM/@test1/99",
      "replies_count": 0,
      "reblogs_count": 0,
      "favourites_count": 0,
      "edited_at": null,
      "favourited": false,
      "reblogged": false,
      "muted": false,
      "bookmarked": false,
      "pinned": false,
      "content": "<p>Test Status</p>",
      "filtered": [],
      "reblog": null,
      "application": {
        "name": "Web",
        "website": null
      },
      "account": {
        "id": "$ACCOUNT_ID",
        "username": "test1",
        "acct": "test1",
        "display_name": "サンプル太郎",
        "locked": false,
        "bot": false,
        "discoverable": false,
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
        "noindex": false,
        "emojis": [],
        "fields": [
          {
            "name": "フィールド1",
            "value": "ほげほげ",
            "verified_at": null
          }
        ]
      },
      "media_attachments": [],
      "mentions": [],
      "tags": [],
      "emojis": [],
      "card": null,
      "poll": null
    }
""".trim()

val JSON_STATUS_MEDIA = """
    {
      "id": "200",
      "created_at": "2023-03-01T00:00:00.000Z",
      "in_reply_to_id": null,
      "in_reply_to_account_id": null,
      "sensitive": false,
      "spoiler_text": "",
      "visibility": "public",
      "language": "ja",
      "uri": "$URL_SAMPLE_COM/users/test1/statuses/200",
      "url": "$URL_SAMPLE_COM/@test1/200",
      "replies_count": 0,
      "reblogs_count": 0,
      "favourites_count": 0,
      "edited_at": "2023-01-09T16:37:11.423Z",
      "favourited": false,
      "reblogged": false,
      "muted": false,
      "bookmarked": false,
      "pinned": false,
      "content": "<p>Test Media</p>",
      "filtered": [],
      "reblog": null,
      "application": {
        "name": "Web",
        "website": null
      },
      "account": {
        "id": "$ACCOUNT_ID",
        "username": "test1",
        "acct": "test1",
        "display_name": "サンプル太郎",
        "locked": false,
        "bot": false,
        "discoverable": false,
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
        "noindex": false,
        "emojis": [],
        "fields": [
          {
            "name": "フィールド1",
            "value": "ほげほげ",
            "verified_at": null
          }
        ]
      },
      "media_attachments": [
        {
          "id": "10",
          "type": "image",
          "url": "$URL_SAMPLE_COM/media_attachments/files/109/654/559/723/047/027/original/9ae6953df4cce61d.jpeg",
          "preview_url": "$URL_SAMPLE_COM/media_attachments/files/109/654/559/723/047/027/small/9ae6953df4cce61d.jpeg",
          "remote_url": null,
          "preview_remote_url": null,
          "text_url": null,
          "meta": {
            "original": {
              "width": 432,
              "height": 858,
              "size": "432x858",
              "aspect": 0.5034965034965035
            },
            "small": {
              "width": 284,
              "height": 564,
              "size": "284x564",
              "aspect": 0.5035460992907801
            }
          },
          "description": null,
          "blurhash": "UbO|U?M{00t7ocaxoMj[4.oft7ayoKayayay"
        }
      ],
      "mentions": [],
      "tags": [],
      "emojis": [],
      "card": null,
      "poll": null
    }
""".trim()

val JSON_STATUS_MEDIA_ID_199 = """
    {
      "id": "199",
      "created_at": "2023-02-28T00:00:00.000Z",
      "in_reply_to_id": null,
      "in_reply_to_account_id": null,
      "sensitive": false,
      "spoiler_text": "",
      "visibility": "public",
      "language": "ja",
      "uri": "$URL_SAMPLE_COM/users/test1/statuses/199",
      "url": "$URL_SAMPLE_COM/@test1/199",
      "replies_count": 0,
      "reblogs_count": 0,
      "favourites_count": 0,
      "edited_at": "2023-01-09T16:37:11.423Z",
      "favourited": false,
      "reblogged": false,
      "muted": false,
      "bookmarked": false,
      "pinned": false,
      "content": "<p>Test Media</p>",
      "filtered": [],
      "reblog": null,
      "application": {
        "name": "Web",
        "website": null
      },
      "account": {
        "id": "$ACCOUNT_ID",
        "username": "test1",
        "acct": "test1",
        "display_name": "サンプル太郎",
        "locked": false,
        "bot": false,
        "discoverable": false,
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
        "noindex": false,
        "emojis": [],
        "fields": [
          {
            "name": "フィールド1",
            "value": "ほげほげ",
            "verified_at": null
          }
        ]
      },
      "media_attachments": [
        {
          "id": "10",
          "type": "image",
          "url": "$URL_SAMPLE_COM/media_attachments/files/109/654/559/723/047/027/original/9ae6953df4cce61d.jpeg",
          "preview_url": "$URL_SAMPLE_COM/media_attachments/files/109/654/559/723/047/027/small/9ae6953df4cce61d.jpeg",
          "remote_url": null,
          "preview_remote_url": null,
          "text_url": null,
          "meta": {
            "original": {
              "width": 432,
              "height": 858,
              "size": "432x858",
              "aspect": 0.5034965034965035
            },
            "small": {
              "width": 284,
              "height": 564,
              "size": "284x564",
              "aspect": 0.5035460992907801
            }
          },
          "description": null,
          "blurhash": "UbO|U?M{00t7ocaxoMj[4.oft7ayoKayayay"
        }
      ],
      "mentions": [],
      "tags": [],
      "emojis": [],
      "card": null,
      "poll": null
    }
""".trim()
