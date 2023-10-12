package com.hepipat.bookish.feature.scan

import com.hepipat.bookish.core.data.repository.TestBooksRepository
import com.hepipat.bookish.core.data.testdata.BooksTestData
import com.hepipat.bookish.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class ScanViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val booksRepository = TestBooksRepository()

    private lateinit var viewModel: ScanViewModel

    @Before
    fun setup() {
        viewModel = ScanViewModel(booksRepository)
    }

    @Test
    fun uiStateBooks_whenInitialized_thenShowLoading() = runTest {
        assertEquals(ScanBooksUiState.Loading, viewModel.booksUiState.value)
    }

    @Test
    fun uiStateBooks_whenSuccess_thenBooksScanned() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.booksUiState.collect() }

        val testInput = BooksTestData.booksUi.isbnCode
        viewModel.scanBooks(testInput)

        val result = viewModel.booksUiState.value
        assertIs<ScanBooksUiState.Scanned>(result)

        assertEquals(testInput, result.booksUi.isbnCode)

        collectJob.cancel()
    }

    @Test
    fun uiStateBooks_whenSuccess_thenBooksNotFound() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.booksUiState.collect() }

        val testInput = "4040404" // random input
        viewModel.scanBooks(testInput)

        val result = viewModel.booksUiState.value
        assertEquals(ScanBooksUiState.NotFound, result)

        collectJob.cancel()
    }

    @Test
    fun uiStateBooks_whenEmptyParams_thenShowError() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.booksUiState.collect() }

        val testInput = ""
        viewModel.scanBooks(testInput)

        val expected = BooksTestData.emptyErrorMessage

        val result = viewModel.booksUiState.value
        assertIs<ScanBooksUiState.Error>(result)

        assertEquals(expected, result.message)

        collectJob.cancel()
    }
}