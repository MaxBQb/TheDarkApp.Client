package lab.maxb.dark.domain.operations

import lab.maxb.dark.domain.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class RecognitionTaskOperationsKtTest {
    private val user = User("test", 0)

    @Test
    fun createRecognitionTask() {
        assertNotNull(createRecognitionTask(
            listOf("1"),
            listOf(""),
            user
        ))
        assertNull(createRecognitionTask(
            listOf(),
            listOf(""),
            user
        ))
        assertNull(createRecognitionTask(
            listOf(""),
            listOf(),
            user
        ))
    }
}