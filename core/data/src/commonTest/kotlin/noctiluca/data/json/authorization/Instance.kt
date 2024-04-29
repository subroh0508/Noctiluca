package noctiluca.data.json.authorization

import io.ktor.http.*
import noctiluca.test.shared.DOMAIN_SAMPLE_COM
import noctiluca.test.shared.URL_SAMPLE_COM

const val V4_INSTANCE_NAME = "Instance Sample"
const val V3_INSTANCE_NAME = "Old Instance Sample"

const val DOMAIN_V3_INSTANCE = "v3.sample.com"
val URL_V3_INSTANCE = "${URLProtocol.HTTPS.name}://$DOMAIN_V3_INSTANCE"

const val INSTANCE_DOMAIN_SUGGESTION_1 = "sample.jp"
const val INSTANCE_DOMAIN_SUGGESTION_2 = "sample.net"

val JSON_V4_INSTANCE_BY_V1_API = """
    {
      "uri": "$DOMAIN_SAMPLE_COM",
      "title": "$V4_INSTANCE_NAME",
      "short_description": "short description",
      "description": "description",
      "email": "admin@$DOMAIN_SAMPLE_COM",
      "version": "4.0.2",
      "urls": {
        "streaming_api": "wss://$DOMAIN_SAMPLE_COM"
      },
      "stats": {
        "user_count": 100,
        "status_count": 1000,
        "domain_count": 200
      },
      "thumbnail": "$URL_SAMPLE_COM/files/thumbnail.png",
      "languages": [
        "ja"
      ],
      "registrations": true,
      "approval_required": false,
      "invites_enabled": false,
      "configuration": {
        "accounts": {
          "max_featured_tags": 10
        },
        "statuses": {
          "max_characters": 500,
          "max_media_attachments": 4,
          "characters_reserved_per_url": 23
        },
        "media_attachments": {
          "supported_mime_types": [
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/heic",
            "image/heif",
            "image/webp",
            "image/avif",
            "video/webm",
            "video/mp4",
            "video/quicktime",
            "video/ogg",
            "audio/wave",
            "audio/wav",
            "audio/x-wav",
            "audio/x-pn-wave",
            "audio/vnd.wave",
            "audio/ogg",
            "audio/vorbis",
            "audio/mpeg",
            "audio/mp3",
            "audio/webm",
            "audio/flac",
            "audio/aac",
            "audio/m4a",
            "audio/x-m4a",
            "audio/mp4",
            "audio/3gpp",
            "video/x-ms-asf"
          ],
          "image_size_limit": 10485760,
          "image_matrix_limit": 16777216,
          "video_size_limit": 41943040,
          "video_frame_rate_limit": 60,
          "video_matrix_limit": 2304000
        },
        "polls": {
            "max_options": 4,
            "max_characters_per_option": 50,
            "min_expiration": 300,
            "max_expiration": 2629746
        }
      },
      "contact_account": {
        "id": "1",
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
      },
      "rules": [
        {
          "id": "1",
          "text": "ルール1"
        },
        {
          "id": "2",
          "text": "ルール2"
        }
      ]
    }
""".trim()

val JSON_V4_INSTANCE_BY_V2_API = """
    {
      "domain": "$DOMAIN_SAMPLE_COM",
      "title": "$V4_INSTANCE_NAME",
      "version": "4.0.2",
      "source_url": "https://github.com/sample/sample",
      "description": "description",
      "usage": {
        "users": {
          "active_month": 123
        }
      },
      "thumbnail": {
        "url": "$URL_SAMPLE_COM/files/thumbnail.png",
        "blurhash": "LEHV6nWB2yk8pyo0adR*.7kCMdnj",
        "versions": {
          "@1x": "$URL_SAMPLE_COM/site_uploads/files/000/000/001/@1x/57c12f441d083cde.png",
          "@2x": "$URL_SAMPLE_COM/site_uploads/files/000/000/001/@2x/57c12f441d083cde.png"
        }
      },
      "languages": [
        "ja"
      ],
      "configuration": {
        "urls": {
          "streaming": "wss://$DOMAIN_SAMPLE_COM"
        },
        "accounts": {
          "max_featured_tags": 10
        },
        "statuses": {
          "max_characters": 500,
          "max_media_attachments": 4,
          "characters_reserved_per_url": 23
        },
        "media_attachments": {
          "supported_mime_types": [
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/heic",
            "image/heif",
            "image/webp",
            "image/avif",
            "video/webm",
            "video/mp4",
            "video/quicktime",
            "video/ogg",
            "audio/wave",
            "audio/wav",
            "audio/x-wav",
            "audio/x-pn-wave",
            "audio/vnd.wave",
            "audio/ogg",
            "audio/vorbis",
            "audio/mpeg",
            "audio/mp3",
            "audio/webm",
            "audio/flac",
            "audio/aac",
            "audio/m4a",
            "audio/x-m4a",
            "audio/mp4",
            "audio/3gpp",
            "video/x-ms-asf"
          ],
          "image_size_limit": 10485760,
          "image_matrix_limit": 16777216,
          "video_size_limit": 41943040,
          "video_frame_rate_limit": 60,
          "video_matrix_limit": 2304000
        },
        "polls": {
            "max_options": 4,
            "max_characters_per_option": 50,
            "min_expiration": 300,
            "max_expiration": 2629746
        },
        "translation": {
          "enabled": true
        }
      },
      "registrations": {
        "enabled": true,
        "approval_required": false,
        "message": null
      },
      "contact": {
        "email": "admin@$DOMAIN_SAMPLE_COM",
        "account": {
          "id": "1",
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
        }
      },
      "rules": [
        {
          "id": "1",
          "text": "ルール1"
        },
        {
          "id": "2",
          "text": "ルール2"
        }
      ]
    }
""".trim()

val JSON_EXTENDED_DESCRIPTION = """
    {
      "updated_at": "2019-04-01T00:00:00.000Z",
      "content": "Extended Description"
    }
""".trim()

val JSON_INSTANCES = """
    {
      "instances": [
        {
          "id": "637b1c68baf9cf97b71f3022",
          "name": "$DOMAIN_SAMPLE_COM",
          "added_at": "2019-04-01T00:00:00.000Z",
          "updated_at": "2019-04-01T00:00:00.000Z",
          "checked_at": "2019-04-01T00:00:00.000Z",
          "uptime": 1,
          "up": true,
          "dead": false,
          "version": "4.0.2",
          "ipv6": true,
          "https_score": 5,
          "https_rank": "F ",
          "obs_score": 75,
          "obs_rank": "B ",
          "users": "100",
          "statuses": "1000",
          "connections": "200",
          "open_registrations": true,
          "info": {
            "short_description": null,
            "full_description": "<p>full description</p>",
            "topic": "topic",
            "languages": null,
            "other_languages_accepted": true,
            "federates_with": null,
            "prohibited_content": [],
            "categories": []
          },
          "thumbnail": "$URL_SAMPLE_COM/files/thumbnail.png",
          "thumbnail_proxy": "$URL_SAMPLE_COM/proxy",
          "active_users": 21,
          "email": "admin@$DOMAIN_SAMPLE_COM",
          "admin": "test1"
        },
        {
          "id": "5937d82ef49c1c689c725356",
          "name": "$INSTANCE_DOMAIN_SUGGESTION_1",
          "added_at": "2019-04-01T00:00:00.000Z",
          "updated_at": "2019-04-01T00:00:00.000Z",
          "checked_at": "2019-04-01T00:00:00.000Z",
          "uptime": 1,
          "up": true,
          "dead": false,
          "version": "4.0.1+glitch",
          "ipv6": true,
          "https_score": 10,
          "https_rank": "E ",
          "obs_score": 125,
          "obs_rank": "A+",
          "users": "346",
          "statuses": "71453",
          "connections": "17521",
          "open_registrations": false,
          "info": {
            "short_description": "short description",
            "full_description": "<p>full description</p>",
            "topic": "topic",
            "languages": [
              "en",
              "fr"
            ],
            "other_languages_accepted": true,
            "federates_with": "all",
            "prohibited_content": [
              "nudity_nocw",
              "pornography_nocw",
              "sexism",
              "racism",
              "illegalContentLinks",
              "spam",
              "advertising",
              "hateSpeeches",
              "harrassment"
            ],
            "categories": []
          },
          "thumbnail": "https://$INSTANCE_DOMAIN_SUGGESTION_1/files/thumbnail.png",
          "thumbnail_proxy": "https://$INSTANCE_DOMAIN_SUGGESTION_1/proxy",
          "active_users": 86,
          "email": "admin@$INSTANCE_DOMAIN_SUGGESTION_1",
          "admin": "admin"
        },
        {
          "id": "58f23f1b762338151ee6d943",
          "name": "$INSTANCE_DOMAIN_SUGGESTION_2",
          "added_at": "2019-04-01T00:00:00.000Z",
          "updated_at": "2019-04-01T00:00:00.000Z",
          "checked_at": "2019-04-01T00:00:00.000Z",
          "uptime": 1,
          "up": false,
          "dead": false,
          "version": "4.0.2",
          "ipv6": true,
          "https_score": 5,
          "https_rank": "F ",
          "obs_score": 75,
          "obs_rank": "B ",
          "users": "5521",
          "statuses": "14469027",
          "connections": "12179",
          "open_registrations": true,
          "info": {
            "short_description": "short description",
            "full_description": "<p>full description</p>",
            "topic": "topic",
            "languages": [
              "ja"
            ],
            "other_languages_accepted": true,
            "federates_with": null,
            "prohibited_content": [
              "nudity_nocw",
              "nudity_all",
              "pornography_nocw",
              "pornography_all",
              "illegalContentLinks",
              "spam"
            ],
            "categories": [
              "games"
            ]
          },
          "thumbnail": "https://$INSTANCE_DOMAIN_SUGGESTION_2/files/thumbnail.png",
          "thumbnail_proxy": "https://$INSTANCE_DOMAIN_SUGGESTION_2/proxy",
          "active_users": 402,
          "email": "admin@$INSTANCE_DOMAIN_SUGGESTION_2",
          "admin": "admin"
        },
        {
          "id": "5aa7f72d1a1b3f1036f3d267",
          "name": "$V3_INSTANCE_NAME",
          "added_at": "2019-04-01T00:00:00.000Z",
          "updated_at": "2019-04-01T00:00:00.000Z",
          "checked_at": "2019-04-01T00:00:00.000Z",
          "uptime": 1,
          "up": true,
          "dead": false,
          "version": "3.3.3",
          "ipv6": true,
          "https_score": 10,
          "https_rank": "E ",
          "obs_score": 80,
          "obs_rank": "B+",
          "users": "465",
          "statuses": "33199",
          "connections": "139522",
          "open_registrations": true,
          "info": {
            "short_description": "short description",
            "full_description": "<p>full description</p>",
            "topic": "topic",
            "languages": [
              "zh",
              "en",
              "ja"
            ],
            "other_languages_accepted": true,
            "federates_with": null,
            "prohibited_content": [
              "pornography_nocw",
              "advertising"
            ],
            "categories": [
              "anime",
              "games"
            ]
          },
          "thumbnail": "$URL_V3_INSTANCE/files/thumbnail.png",
          "thumbnail_proxy": "$URL_V3_INSTANCE/proxy",
          "active_users": 11,
          "email": "admin@$DOMAIN_V3_INSTANCE",
          "admin": "admin"
        }
      ],
      "pagination": {
        "total": 4
      }
    }
""".trim()

val JSON_V3_INSTANCE_BY_V1_API = """
    {
      "uri": "$DOMAIN_V3_INSTANCE",
      "title": "$V3_INSTANCE_NAME",
      "short_description": "short description",
      "description": "description",
      "email": "admin@$DOMAIN_V3_INSTANCE",
      "version": "3.3.3",
      "urls": {
        "streaming_api": "wss://$DOMAIN_V3_INSTANCE"
      },
      "stats": {
        "user_count": 100,
        "status_count": 1000,
        "domain_count": 200
      },
      "thumbnail": "$URL_V3_INSTANCE/files/thumbnail.png",
      "languages": [
        "ja"
      ],
      "registrations": true,
      "approval_required": false,
      "invites_enabled": false,
      "configuration": {
        "accounts": {
          "max_featured_tags": 10
        },
        "statuses": {
          "max_characters": 500,
          "max_media_attachments": 4,
          "characters_reserved_per_url": 23
        },
        "media_attachments": {
          "supported_mime_types": [
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/heic",
            "image/heif",
            "image/webp",
            "image/avif",
            "video/webm",
            "video/mp4",
            "video/quicktime",
            "video/ogg",
            "audio/wave",
            "audio/wav",
            "audio/x-wav",
            "audio/x-pn-wave",
            "audio/vnd.wave",
            "audio/ogg",
            "audio/vorbis",
            "audio/mpeg",
            "audio/mp3",
            "audio/webm",
            "audio/flac",
            "audio/aac",
            "audio/m4a",
            "audio/x-m4a",
            "audio/mp4",
            "audio/3gpp",
            "video/x-ms-asf"
          ],
          "image_size_limit": 10485760,
          "image_matrix_limit": 16777216,
          "video_size_limit": 41943040,
          "video_frame_rate_limit": 60,
          "video_matrix_limit": 2304000
        },
        "polls": {
            "max_options": 4,
            "max_characters_per_option": 50,
            "min_expiration": 300,
            "max_expiration": 2629746
        }
      },
      "contact_account": {
        "id": "1",
        "username": "test1",
        "acct": "test1",
        "display_name": "サンプル太郎",
        "locked": false,
        "bot": false,
        "discoverable": false,
        "group": false,
        "created_at": "2019-04-01T00:00:00.000Z",
        "note": "<p>note</p>",
        "url": "$URL_V3_INSTANCE/@test1",
        "avatar": "$URL_V3_INSTANCE/accounts/avatars/avater.png",
        "avatar_static": "$URL_V3_INSTANCE/accounts/avatars/original/avater.png",
        "header": "$URL_V3_INSTANCE/accounts/headers/header.png",
        "header_static": "$URL_V3_INSTANCE/accounts/headers/original/header.png",
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
      },
      "rules": [
        {
          "id": "1",
          "text": "ルール1"
        },
        {
          "id": "2",
          "text": "ルール2"
        }
      ]
    }
""".trim()
