package noctiluca.accountdetail.infra

import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.api.mastodon.json.account.FieldJson

internal fun FieldJson.toValueObject() = AccountAttributes.Field(name, value)
