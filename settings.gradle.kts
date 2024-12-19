rootProject.name = "kacosmetology-api"

plugins {
    id("com.gradle.develocity") version ("3.19")
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
    }
}