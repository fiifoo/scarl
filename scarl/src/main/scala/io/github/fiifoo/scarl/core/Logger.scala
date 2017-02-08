package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.effect.Effect

class Logger(val effect: (State, Effect) => Unit = (_, _) => ())
