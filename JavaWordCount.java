package hello;

import scala.Tuple2;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

enum JavaWordCount {
	INSTANCE;
	private Pattern SPACE = Pattern.compile(" ");

	public List<Tuple2<String, Integer>> WordCount(String filename) throws Exception {

		SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount").setMaster("local");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		try {
			JavaRDD<String> lines = ctx.textFile(filename, 1);
			JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
				@Override
				public Iterable<String> call(String s) {
					return Arrays.asList(SPACE.split(s));
				}
			});

			JavaPairRDD<String, Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {
				@Override
				public Tuple2<String, Integer> call(String s) {
					return new Tuple2<String, Integer>(s, 1);
				}
			});

			JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {
				@Override
				public Integer call(Integer i1, Integer i2) {
					return i1 + i2;
				}
			});

			List<Tuple2<String, Integer>> output = counts.collect();
			for (Tuple2<?, ?> tuple : output) {
				System.out.println(tuple._1() + ": " + tuple._2());
			}
			return output;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			ctx.stop();
			ctx.close();
		}
	}
}
