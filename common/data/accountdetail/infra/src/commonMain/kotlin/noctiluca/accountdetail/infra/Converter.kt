package noctiluca.accountdetail.infra

import noctiluca.accountdetail.model.AccountDetail
import noctiluca.api.mastodon.json.account.FieldJson

internal fun FieldJson.toValueObject() = AccountDetail.Field(name, value)
