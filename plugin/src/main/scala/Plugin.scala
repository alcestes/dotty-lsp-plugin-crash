package crash.plugin

import scala.language.implicitConversions

import dotty.tools.dotc.transform

import dotty.tools.dotc.core.{Names, Types, TypeApplications}
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.core.Phases.Phase
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Decorators._
import dotty.tools.dotc.core.Symbols.Symbol

import dotty.tools.dotc.plugins
import dotty.tools.dotc.ast.Trees
import dotty.tools.dotc.ast.tpd
import dotty.tools.dotc.ast.tpd.Tree

class Plugin extends plugins.PluginPhase with plugins.StandardPlugin {
  val name: String = "crashPlugin"
  override val description: String = "a plugin to crash the Dotty Language Server"

  val phaseName = name

  override val runsAfter = Set(transform.Staging.name)
  override val runsBefore = Set(transform.FirstTransform.name)

  def init(options: List[String]): List[plugins.PluginPhase] = this :: Nil

  def Annotation(implicit ctx: Context) = {
    ctx.requiredClassRef("crash.plugin.annotation").symbol.asClass
  }

  override def transformDefDef(tree: tpd.DefDef)
                              (implicit ctx: Context): Tree = {
    check(tree, tree.tpt.tpe)
  }

  override def transformValDef(tree: tpd.ValDef)
                              (implicit ctx: Context): Tree = {
    check(tree, tree.tpt.tpe)
  }

  override def transformTypeDef(tree: tpd.TypeDef)
                               (implicit ctx: Context): Tree = {
    check(tree, tree.rhs.tpe)
  }

  private def check(tree: Tree, tpe: Types.Type)
                   (implicit ctx: Context): Tree = {
    if (tree.symbol.hasAnnotation(Annotation)) {
      ctx.log(s"Annotation found!", tree.pos)
    }
    tree
  }
}
