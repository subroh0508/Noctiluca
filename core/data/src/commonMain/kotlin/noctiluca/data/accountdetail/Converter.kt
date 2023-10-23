package noctiluca.data.accountdetail

import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.network.mastodon.json.account.FieldJson

internal fun FieldJson.toValueObject() = AccountAttributes.Field(name, value)
