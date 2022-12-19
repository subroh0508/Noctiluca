package noctiluca.instance.infra.repository

import noctiluca.instance.model.Instance

interface InstanceRepository {
    suspend fun search(query: String): List<Instance>
}