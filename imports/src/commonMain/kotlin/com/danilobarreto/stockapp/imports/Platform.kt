package com.danilobarreto.stockapp.imports

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
