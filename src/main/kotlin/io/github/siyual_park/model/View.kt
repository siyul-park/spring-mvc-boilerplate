package io.github.siyual_park.model

interface View {
    interface Public
    interface Protect : Public
    interface Private : Protect
}
