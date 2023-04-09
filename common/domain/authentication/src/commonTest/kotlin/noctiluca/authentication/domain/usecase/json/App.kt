package noctiluca.authentication.domain.usecase.json

const val TEST_CLIENT_NAME = "client-name"
const val TEST_REDIRECT_SCHEMA = "oauth2redirect"
const val TEST_REDIRECT_URL = "$TEST_REDIRECT_SCHEMA://$TEST_CLIENT_NAME"

const val TEST_CLIENT_ID = "xxx"
const val TEST_CLIENT_SECRET = "yyy"

const val TEST_ENCODED_REDIRECT_URL = "$TEST_REDIRECT_SCHEMA%3A%2F%2F$TEST_CLIENT_NAME"

val JSON_APP_CREDENTIAL = """
    {
      "id": "10",
      "name": "$TEST_CLIENT_NAME",
      "website": null,
      "redirect_uri": "$TEST_CLIENT_NAME",
      "client_id": "$TEST_CLIENT_ID",
      "client_secret": "$TEST_CLIENT_SECRET",
      "vapid_key": "zzz"
    }
""".trim()
