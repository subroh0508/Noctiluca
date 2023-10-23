package noctiluca.timeline.domain.usecase.json

import noctiluca.test.JSON_ACCOUNT_CREDENTIAL
import noctiluca.test.URL_SAMPLE_COM

val JSON_ACCOUNT_CREDENTIAL_1 = JSON_ACCOUNT_CREDENTIAL

val JSON_ACCOUNT_CREDENTIAL_2 = """
    {
      "id": "10",
      "username": "test2",
      "acct": "test2",
      "display_name": "サンプル次郎",
      "locked": false,
      "bot": false,
      "discoverable": false,
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
      "noindex": false,
      "source": {
        "privacy": "public",
        "sensitive": false,
        "language": null,
        "note": "note",
        "fields": [
          {
            "name": "フィールド1",
            "value": "ふがふが",
            "verified_at": null
          }
        ],
        "follow_requests_count": 0
      },
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

val JSON_ACCOUNT_CREDENTIAL_3 = """
    {
      "id": "100",
      "username": "test3",
      "acct": "test3",
      "display_name": "サンプル三郎",
      "locked": false,
      "bot": false,
      "discoverable": false,
      "group": false,
      "created_at": "2019-04-01T00:00:00.000Z",
      "note": "<p>note</p>",
      "url": "$URL_SAMPLE_COM/@test3",
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
            "value": "テスト文言",
            "verified_at": null
          }
        ],
        "follow_requests_count": 0
      },
      "emojis": [],
      "fields": [
        {
          "name": "フィールド1",
          "value": "テスト文言",
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
