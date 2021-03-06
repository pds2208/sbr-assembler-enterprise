package spark.calculations


import global.AppParams
import org.apache.spark.sql.{DataFrame, SparkSession}
import uk.gov.ons.registers.methods._

trait SmlAdminDataCalculator extends PayeCalculator with VatCalculator with Serializable {

  def calculate(unitsDF: DataFrame, appconf: AppParams)(implicit spark: SparkSession): DataFrame = {
    val vatDF = spark.read.option("header", "true").csv(appconf.PATH_TO_VAT)
    val payeDF = spark.read.option("header", "true").csv(appconf.PATH_TO_PAYE)

    val payeCalculated: DataFrame = calculatePAYE(unitsDF, payeDF)

    calculateVAT(unitsDF, payeCalculated, vatDF)
  }

}
