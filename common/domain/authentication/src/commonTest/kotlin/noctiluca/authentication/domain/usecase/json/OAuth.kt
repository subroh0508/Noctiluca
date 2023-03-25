package noctiluca.authentication.domain.usecase.json

const val TEST_CODE = "dummy_code"
const val TEST_ACCESS_TOKEN = "AAABBBCCC"

val JSON_OAUTH_TOKEN = """
    {
      "access_token": "$TEST_ACCESS_TOKEN",
      "token_type": "Bearer",
      "scope": "read write follow push",
      "created_at": 1648738800
    }
""".trim()
