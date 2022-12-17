package noctiluca.authentication.domain.usecase

interface SearchMastodonInstancesUseCase : suspend (String) -> List<String>