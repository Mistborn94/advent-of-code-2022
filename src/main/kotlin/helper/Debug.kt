package helper

sealed interface Debug {

    operator fun invoke(action: () -> Unit)

    object Enabled : Debug {
        override fun invoke(action: () -> Unit) {
            action()
        }
    }

    object Disabled : Debug {
        override fun invoke(action: () -> Unit) {/*Do nothing*/
        }
    }
}