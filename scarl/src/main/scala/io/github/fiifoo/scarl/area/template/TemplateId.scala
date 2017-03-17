package io.github.fiifoo.scarl.area.template

case class TemplateId(value: String) {
  def apply(t: Map[TemplateId, Template]): Template = t(this)
}
