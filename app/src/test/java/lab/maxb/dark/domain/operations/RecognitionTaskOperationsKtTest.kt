package lab.maxb.dark.domain.operations

import lab.maxb.dark.domain.model.User
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test


internal class RecognitionTaskOperationsKtTest {
    private val user = User("test", 0)

    @Test
    fun createRecognitionTask() {
        assertNotNull(
            lab.maxb.dark.domain.operation.createRecognitionTask(
                listOf("1"),
                listOf(""),
                user
            )
        )
        assertNull(
            lab.maxb.dark.domain.operation.createRecognitionTask(
                listOf(),
                listOf(""),
                user
            )
        )
        assertNull(
            lab.maxb.dark.domain.operation.createRecognitionTask(
                listOf(""),
                listOf(),
                user
            )
        )
    }
}