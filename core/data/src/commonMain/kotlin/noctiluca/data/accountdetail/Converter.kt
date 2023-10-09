package noctiluca.data.accountdetail

import noctiluca.api.mastodon.json.account.FieldJson
import noctiluca.model.accountdetail.AccountAttributes

internal fun FieldJson.toValueObject() = AccountAttributes.Field(name, value)
