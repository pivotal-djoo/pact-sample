package com.example.todo.contracts

import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit.PactProviderRule
import au.com.dius.pact.consumer.junit.PactVerification
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.example.todo.CoroutineTestRule
import com.example.todo.repositories.ToDoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.net.ServerSocket
import java.util.*

@ExperimentalCoroutinesApi
class GetToDoContractTest {
    private var subject: ToDoRepository? = null

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @get:Rule
    var mockProvider = PactProviderRule("To Do API", "localhost", availablePort, this)

    @Pact(provider = "To Do API", consumer = "To Do Android")
    fun createPact(builder: PactDslWithProvider): RequestResponsePact {
        val headers: MutableMap<String, String> = HashMap()
        headers["Content-Type"] = "application/json"
        return builder
            .given("test GET")
            .uponReceiving("GET REQUEST")
            .path("/todos")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(headers)
            .body("[{\"id\":1,\"text\":\"Pick up pickles\",\"checked\":false},{\"id\":2,\"text\":\"Pick up kids\",\"checked\":true}]")
            .toPact()
    }

    @Before
    fun setup() {
        subject = ToDoRepository(mockProvider.url)
    }

    @Test
    @PactVerification("To Do API")
    fun toDos_shouldReturnToDosList() = runBlocking<Unit> {
        val toDos = subject?.getToDos()
        assertThat(toDos).hasSize(2)
        assertThat(toDos?.get(0)?.text).isEqualTo("Pick up pickles")
        assertThat(toDos?.get(0)?.checked).isFalse()
        assertThat(toDos?.get(1)?.text).isEqualTo("Pick up kids")
        assertThat(toDos?.get(1)?.checked).isTrue()
    }

    companion object {
        private val availablePort: Int
            get() = Random()
                .ints(6000, 9000)
                .filter { port: Int -> isFree(port) }
                .findFirst()
                .orElse(8080)

        private fun isFree(port: Int): Boolean {
            return try {
                ServerSocket(port).close()
                true
            } catch (e: IOException) {
                false
            }
        }
    }
}
