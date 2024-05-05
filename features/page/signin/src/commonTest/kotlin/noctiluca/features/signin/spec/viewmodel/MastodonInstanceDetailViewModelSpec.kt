package noctiluca.features.signin.spec.viewmodel

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.should
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import noctiluca.features.shared.model.LoadState
import noctiluca.features.signin.mock.FakeInstanceRepository
import noctiluca.features.signin.model.InstanceDetailModel
import noctiluca.features.signin.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.features.signin.mock.instance as MockInstance

@OptIn(ExperimentalCoroutinesApi::class)
class MastodonInstanceDetailViewModelSpec : DescribeSpec({
    coroutineTestScope = true

    val dispatcher = UnconfinedTestDispatcher()
    val repository = object : FakeInstanceRepository() {
        override suspend fun fetchInstance(domain: String) {
            if (domain == MockInstance.domain) {
                instances.value = MockInstance
                return
            }

            instances.value = null
        }
    }

    beforeSpec {
        Dispatchers.setMain(dispatcher)
    }

    describe("#load") {
        context("when the domain exists") {
            it("returns instance") {
                val viewModel = MastodonInstanceDetailViewModel(repository)
                val job = launch(dispatcher) { viewModel.uiModel.collect() }

                viewModel.uiModel.value should be(InstanceDetailModel())
                viewModel.load(MockInstance.domain)
                viewModel.uiModel.value should be(
                    InstanceDetailModel(
                        instance = MockInstance,
                        instanceLoadState = LoadState.Loaded,
                        statusesLoadState = LoadState.Loaded,
                    ),
                )
                job.cancel()
            }
        }
    }

    afterSpec {
        Dispatchers.resetMain()
    }
})
