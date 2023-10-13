/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hepipat.bookish.core.data.testdata

import com.hepipat.bookish.core.data.remote.response.BooksResponse
import com.hepipat.bookish.core.domain.model.BooksUi

object BooksTestData {

    val booksResponse: BooksResponse =
        BooksResponse(
            items = listOf(
                BooksResponse.Item(
                    id = "0",
                    volumeInfo =
                        BooksResponse.Item.VolumeInfo(
                            title = "Clean Code",
                            description = "Clean Code is a book by Uncle Bob",
                            publishedDate = "01-08-2008",
                            authors = listOf("Robert C. Martin"),
                            industryIdentifiers = listOf(
                                BooksResponse.Item.VolumeInfo.IndustryIdentifier(
                                    type = "ISBN-13",
                                    identifier = "1234567890123"
                                )
                            )
                        )
                )
            )
        )

    val emptyBooksResponse: BooksResponse =
        BooksResponse(
            items = listOf(),
            totalItems = 0,
        )

    const val emptyErrorMessage = "ISBN code is empty"
}