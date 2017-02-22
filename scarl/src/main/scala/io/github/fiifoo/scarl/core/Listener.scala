package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.effect.{EffectListener, NullEffectListener}

class Listener(val effect: EffectListener = NullEffectListener)
