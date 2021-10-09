package com.example.todo.contracts

import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit.PactProviderRule
import au.com.dius.pact.consumer.junit.PactVerification
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.example.todo.CoroutineTestRule
import com.example.todo.models.ToDo
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
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
class AddToDoContractTest {
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
            .given("test POST")
            .uponReceiving("POST REQUEST")
            .method("POST")
            .headers(headers)
            .body("{\"text\":\"Pick up pickles\",\"checked\":false}")
            .path("/todos")
            .willRespondWith()
            .status(201)
            .body("{\"text\":\"Pick up pickles\",\"checked\":false}")
            .toPact()
    }

    @Before
    fun setup() {
        subject = ToDoRepository(mockProvider.url)
    }

    @Test
    @PactVerification("To Do API")
    @Throws(
        InterruptedException::class,
        ExecutionException::class,
        TimeoutException::class
    )
    fun addToDo_shouldReturn201() = runBlocking<Unit> {
        val newToDo = ToDo(null, "Pick up pickles", false)
        val toDo = subject?.addToDo(newToDo)

        assertThat(toDo?.text).isEqualTo("Pick up pickles")
        assertThat(toDo?.checked).isFalse()
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