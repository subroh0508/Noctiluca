package noctiluca.data.accountdetail

import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.network.mastodon.data.account.NetworkField

internal fun NetworkField.toValueObject() = AccountAttributes.Field(name, value)
