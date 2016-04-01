//add comments for branch2
package hello;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import scala.Tuple2;

@Controller
public class GreetingController {
	@Autowired
	private MovieRecommendationRepository repository;
	@Autowired
	private WordCountRepository repository2;
	

	
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
		System.out.println(repository.findByName("Finding Dory"));
		MovieRecommendation m = repository.findByName("Finding Dory");
		model.addAttribute("name", m.getName());
    	model.addAttribute("year", m.getReleaseYr());
    	model.addAttribute("rating", m.getRating());
    	try {
    		//JavaWordCount.WordCount("README.md");
		
    		
    		List<Tuple2<String, Integer>> output = JavaWordCount.INSTANCE.WordCount(name);
			model.addAttribute("wordcounts", output);
			System.out.println("WordCount complete");
		    for (Tuple2<?,?> tuple : output) {
		      System.out.println(tuple._1() + ": " + tuple._2());
		      repository2.save(new WordCount((String)(tuple._1), (Integer)(tuple._2)));
		    }
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
        return "greeting";
    }

}
