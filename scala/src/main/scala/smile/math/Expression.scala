/*******************************************************************************
 * (C) Copyright 2015 Haifeng Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package smile.math

import smile.math.matrix.{ColumnMajorMatrix, DenseMatrix}

/**
 * Vector Expression.
 */
sealed trait VectorExpression {
  def length: Int
  def apply(i: Int): Double
  def toArray: Array[Double]
  override def toString = runtime.ScalaRunTime.stringOf(toArray)

  def + (b: VectorExpression) = VectorAddVector(this, b)
  def - (b: VectorExpression) = VectorSubVector(this, b)
  def * (b: VectorExpression) = VectorMulVector(this, b)
  def / (b: VectorExpression) = VectorDivVector(this, b)

  def + (b: Double) = VectorAddValue(this, b)
  def - (b: Double) = VectorSubValue(this, b)
  def * (b: Double) = VectorMulValue(this, b)
  def / (b: Double) = VectorDivValue(this, b)
}

case class VectorLift(x: Array[Double]) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = x(i)
  override def toArray: Array[Double] = x
}

case class VectorAddValue(x: VectorExpression, y: Double) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = x(i) + y
  override lazy val toArray: Array[Double] = {
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = x(i) + y
    z
  }
}
case class VectorSubValue(x: VectorExpression, y: Double) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = x(i) - y
  override lazy val toArray: Array[Double] = {
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = x(i) - y
    z
  }
}
case class VectorMulValue(x: VectorExpression, y: Double) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = x(i) * y
  override lazy val toArray: Array[Double] = {
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = x(i) * y
    z
  }
}
case class VectorDivValue(x: VectorExpression, y: Double) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = x(i) / y
  override lazy val toArray: Array[Double] ={
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = x(i) / y
    z
  }
}

case class ValueAddVector(y: Double, x: VectorExpression) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = y + x(i)
  override lazy val toArray: Array[Double] = {
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = y + x(i)
    z
  }
}
case class ValueSubVector(y: Double, x: VectorExpression) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = y - x(i)
  override lazy val toArray: Array[Double] = {
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = y - x(i)
    z
  }
}
case class ValueMulVector(y: Double, x: VectorExpression) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = y * x(i)
  override lazy val toArray: Array[Double] = {
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = y * x(i)
    z
  }
}
case class ValueDivVector(y: Double, x: VectorExpression) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = x(i) / y
  override lazy val toArray: Array[Double] = {
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = y / x(i)
    z
  }
}

case class VectorAddVector(x: VectorExpression, y: VectorExpression) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = x(i) + y(i)
  override lazy val toArray: Array[Double] = {
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = x(i) + y(i)
    z
  }
}
case class VectorSubVector(x: VectorExpression, y: VectorExpression) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = x(i) + y(i)
  override lazy val toArray: Array[Double] = {
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = x(i) - y(i)
    z
  }
}
case class VectorMulVector(x: VectorExpression, y: VectorExpression) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = x(i) + y(i)
  override lazy val toArray: Array[Double] = {
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = x(i) * y(i)
    z
  }
}
case class VectorDivVector(x: VectorExpression, y: VectorExpression) extends VectorExpression {
  override def length: Int = x.length
  override def apply(i: Int): Double = x(i) + y(i)
  override lazy val toArray: Array[Double] = {
    val z = new Array[Double](x.length)
    for (i <- 0 until x.length) z(i) = x(i) / y(i)
    z
  }
}

sealed trait MatrixExpression {
  def nrows: Int
  def ncols: Int
  def apply(i: Int, j: Int): Double
  def toMatrix: DenseMatrix
  override def toString = runtime.ScalaRunTime.stringOf(toMatrix)

  def + (b: MatrixExpression) = MatrixAddMatrix(this, b)
  def - (b: MatrixExpression) = MatrixSubMatrix(this, b)
  /** Element-wise multiplication */
  def * (b: MatrixExpression) = MatrixMulMatrix(this, b)
  def / (b: MatrixExpression) = MatrixDivMatrix(this, b)

  /** Matrix transpose */
  def unary_~ = MatrixTranspose(this)

  /** A * x */
  def  * (b: VectorExpression) = Ax(this, b)
  /** A' * x */
  def ~* (b: VectorExpression) = Atx(this, b)

  /** Matrix multiplication A * B */
  def %*% (b: MatrixExpression) = MatrixMultiplicationExp(this, b)
  /** Out product A * B' */
  def %*~ (b: MatrixExpression) = MatrixOutProduct(this, b)
  /** Cross product A' * B */
  def ~*% (b: MatrixExpression) = MatrixCrossProduct(this, b)

  def + (b: Double) = MatrixAddValue(this, b)
  def - (b: Double) = MatrixSubValue(this, b)
  def * (b: Double) = MatrixMulValue(this, b)
  def / (b: Double) = MatrixDivValue(this, b)
}

case class Ax(A: MatrixExpression, x: VectorExpression) extends VectorExpression {
  override def length: Int = A.nrows
  override def apply(i: Int): Double = toArray(i)
  override lazy val toArray: Array[Double] = {
    val y = new Array[Double](A.nrows)
    A.toMatrix.ax(x, y)
  }
}

case class Atx(A: MatrixExpression, x: VectorExpression) extends VectorExpression {
  override def length: Int = A.ncols
  override def apply(i: Int): Double = toArray(i)
  override lazy val toArray: Array[Double] = {
    val y = new Array[Double](A.ncols)
    A.toMatrix.atx(x, y)
  }
}

case class MatrixLift(A: DenseMatrix) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = A(i, j)
  override def toMatrix: DenseMatrix = A
}

case class MatrixTranspose(A: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.ncols
  override def ncols: Int = A.nrows
  override def apply(i: Int, j: Int): Double = A(j, i)
  override def toMatrix: DenseMatrix = A.toMatrix.transpose()
}

case class MatrixMultiplicationExp(A: MatrixExpression, B: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = B.ncols
  override def apply(i: Int, j: Int): Double = toMatrix(i, j)
  override lazy val toMatrix: DenseMatrix = A.toMatrix.abmm(B.toMatrix)
}

case class MatrixCrossProduct(A: MatrixExpression, B: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.ncols
  override def ncols: Int = B.ncols
  override def apply(i: Int, j: Int): Double = toMatrix(i, j)
  override lazy val toMatrix: DenseMatrix = A.toMatrix.atbmm(B.toMatrix)
}

case class MatrixOutProduct(A: MatrixExpression, B: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = B.nrows
  override def apply(i: Int, j: Int): Double = toMatrix(i, j)
  override lazy val toMatrix: DenseMatrix = A.toMatrix.abtmm(B.toMatrix)
}

case class MatrixAddValue(A: MatrixExpression, y: Double) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = A(i, j) + y
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = A(i, j) + y
    z
  }
}
case class MatrixSubValue(A: MatrixExpression, y: Double) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = A(i, j) - y
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = A(i, j) - y
    z
  }
}
case class MatrixMulValue(A: MatrixExpression, y: Double) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = A(i, j) * y
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = A(i, j) * y
    z
  }
}
case class MatrixDivValue(A: MatrixExpression, y: Double) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = A(i, j) / y
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = A(i, j) / y
    z
  }
}

case class ValueAddMatrix(y: Double, A: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = y + A(i, j)
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = y + A(i, j)
    z
  }
}
case class ValueSubMatrix(y: Double, A: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = y - A(i, j)
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = y - A(i, j)
    z
  }
}
case class ValueMulMatrix(y: Double, A: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = y * A(i, j)
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = y * A(i, j)
    z
  }
}
case class ValueDivMatrix(y: Double, A: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = y / A(i, j)
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = y / A(i, j)
    z
  }
}

case class MatrixAddMatrix(A: MatrixExpression, B: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = A(i, j) + B(i, j)
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = A(i, j) + B(i, j)
    z
  }
}
case class MatrixSubMatrix(A: MatrixExpression, B: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = A(i, j) - B(i, j)
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = A(i, j) - B(i, j)
    z
  }
}
case class MatrixMulMatrix(A: MatrixExpression, B: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = A(i, j) * B(i, j)
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = A(i, j) * B(i, j)
    z
  }
}
case class MatrixDivMatrix(A: MatrixExpression, B: MatrixExpression) extends MatrixExpression {
  override def nrows: Int = A.nrows
  override def ncols: Int = A.ncols
  override def apply(i: Int, j: Int): Double = A(i, j) / B(i, j)
  override lazy val toMatrix: DenseMatrix = {
    val z = new ColumnMajorMatrix(A.nrows, A.ncols)
    for (i <- 0 until ncols)
      for (j <- 0 until nrows)
        z(i, j) = A(i, j) / B(i, j)
    z
  }
}
