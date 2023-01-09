package com.easyhi.manage.net

class Computer3 private constructor(
    private val cpu: String,
    private val name: String
) {

    private constructor(builder: Builder) : this(
        builder.cpu,
        builder.name
    )

    override fun toString(): String {
        return "Computer3(cpu='$cpu', name='$name')"
    }

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {

        var cpu: String = ""
            private set

        var name: String = ""
            private set

        fun setCpu(inputCpu: String) = apply {
            this.cpu = inputCpu
        }

        fun setName(name: String) = apply {
            this.name = name
        }

        fun build() = Computer3(this)

    }

}

fun main() {
    val computer3 = Computer3.build {
        setCpu("cpu4")
        setName("computer1")
    }
    println(computer3)
}























